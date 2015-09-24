/*!!
Epoch Prime AJAX JavaScript Calendar - Version 1.0.2
English Edition
Primary JavaScript File
(c) 2006-2007 MeanFreePath
http://www.meanfreepath.com/epochprime/index.html
All rights reserved.
!!*/

/**
* The main Epoch Prime class.  All publicly-accessible methods and properties are called from this class
*/
function EpochPrime(targetelement,xmlconfig) {
	var self = this; //workaround due to varying definitions of "this" in variable scopes. see http://www.meanfreepath.com/support/epochprime/epochprime.html#self for details
	//DEFINE PRIVATE METHODS
	//-----------------------------------------------------------------------------
	/**
	 * All language settings for Epoch are made here.
	 * Check Date.dateFormat() for the Date object's language settings
	 */
	function setLang() {
		self.daylist = new Array('S','M','T','W','T','F','S','S','M','T','W','T','F','S');
		self.months_sh = new Array('Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
		self.monthup_title = 'Go to the next month';
		self.monthdn_title = 'Go to the previous month';
		self.clearbtn_caption = 'Clear';
		self.clearbtn_title = 'Clears any dates selected on the calendar';
		self.maxrange_caption = 'This is the maximum range';
		self.closebtn_caption = 'Close';
		self.closebtn_title = 'Close the calendar';
	}
	//-----------------------------------------------------------------------------
	/**
	 * Declares and initializes the calendar variables.  All the variables here can be safely changed
	 * (within reason ;) by the developer
	 */
	function calConfig() {
		self.versionNumber = '1.0.2';
		self.name = 'epochprime'; //name prepended before any ID attributes in the calendar's DOM representation
		self.mode = 'flat'; //can be 'flat' or 'popup'
		self.selectMultiple = true; //whether the user can simultaneously select multiple dates
		self.displayYearInitial = self.curDate.getFullYear(); //the initial year to display on load
		self.displayMonthInitial = self.curDate.getMonth(); //the initial month to display on load (0-11)
		self.displayYear = self.displayYearInitial; //the currently displayed year
		self.displayMonth = self.displayMonthInitial; //the currently displayed month
		self.defDateFormat = 'm/d/Y';
		self.minDate = new Date(2006,0,1); //the earliest date the user can choose
		self.maxDate = new Date(2012,11,31); //the latest data the user can choose
		self.startDay = 0; //the day the week will 'start' on: 0(Sunday) to 6(Saturday)
		self.showWeeks = true; //whether the week numbers will be shown
		self.selCurMonthOnly = true; //allow user to only select dates in the currently displayed month
	}
	//-----------------------------------------------------------------------------
	/**
	 * Initializes the standard Gregorian Calendar parameters
	 */
	function setDays() {
		self.daynames = new Array();
		var j=0;
		for(var i=self.startDay;i<self.startDay + 7;i++) {
			self.daynames[j++] = self.daylist[i];
		}
		self.monthDayCount = new Array(31,((self.curDate.getFullYear() - 2000) % 4 ? 28 : 29),31,30,31,30,31,31,30,31,30,31);
	}
	//-----------------------------------------------------------------------------
	/**
	 * Creates the full DOM implementation of the calendar
	 */
	function createCalendar() {
		var tbody, tr, td;
		self.calendar = document.createElement('table');
		self.calendar.setAttribute('id',self.name+'_calendar');
		setClass(self.calendar,'calendar');
		self.calendar.style.display = 'none'; //default to invisible
		//to prevent IE from selecting text when clicking on the calendar
		addEventHandler(self.calendar,'selectstart', function() {return false;});
		addEventHandler(self.calendar,'drag', function() {return false;});
		tbody = document.createElement('tbody');
		var tr_tmp = document.createElement('tr'), td_tmp = document.createElement('td');

		//create the Main Calendar Heading
		tr = tr_tmp.cloneNode(false);
		td = td_tmp.cloneNode(false);
		td.appendChild(createMainHeading());
		tr.appendChild(td);
		tbody.appendChild(tr);

		//create the calendar Day Heading & the calendar Day Cells
		tr = tr_tmp.cloneNode(false);
		td = td_tmp.cloneNode(false);
		self.calendar.celltable = document.createElement('table');
		setClass(self.calendar.celltable,'cells');
		self.calendar.celltable.appendChild(createDayHeading());
		self.calendar.celltable.appendChild(createCalCells());
		td.appendChild(self.calendar.celltable);
		tr.appendChild(td);
		tbody.appendChild(tr);

		//create the calendar footer
		tr = tr_tmp.cloneNode(false);
		td = td_tmp.cloneNode(false);
		td.appendChild(createFooter());
		tr.appendChild(td);
		tbody.appendChild(tr);

		//add the tbody element to the main calendar table
		self.calendar.appendChild(tbody);

		//and add the onmouseover events to the calendar table
		addEventHandler(self.calendar,'mouseover',cal_onmouseover);
		addEventHandler(self.calendar,'mouseout',cal_onmouseout);
	}
	//-----------------------------------------------------------------------------
	/**
	 * Creates the primary calendar heading, with months & years
	 */
	function createMainHeading() {
		//create the containing <div> element
		var container = document.createElement('div');
		setClass(container,'mainheading');
		//create the child elements and other variables
		self.monthSelect = document.createElement('select');
		self.yearSelect = document.createElement('select');
		var monthDn = document.createElement('input'), monthUp = document.createElement('input');
		var opt_tmp = document.createElement('option'), opt, i;
		//fill the month select box
		for(i=0;i<12;i++) {
			opt = opt_tmp.cloneNode(false);
			opt.setAttribute('value',i);
			self.displayMonth == i ? opt.setAttribute('selected','selected') : opt.removeAttribute('selected');
			opt.appendChild(document.createTextNode(self.months_sh[i]));
			self.monthSelect.appendChild(opt);
		}
		//and fill the year select box
		var yrMax = self.maxDate.getFullYear(), yrMin = self.minDate.getFullYear();
		for(i=yrMin;i<=yrMax;i++) {
			opt = opt_tmp.cloneNode(false);
			opt.setAttribute('value',i);
			self.displayYear == i ? opt.setAttribute('selected','selected') : opt.removeAttribute('selected');
			opt.appendChild(document.createTextNode(i));
			self.yearSelect.appendChild(opt);
		}
		//add the appropriate children for the month buttons
		monthUp.setAttribute('type','button');
		monthUp.setAttribute('value','>');
		monthUp.setAttribute('title',self.monthup_title);
		monthDn.setAttribute('type','button');
		monthDn.setAttribute('value','<');
		monthDn.setAttribute('title',self.monthdn_title);
		self.monthSelect.owner = self.yearSelect.owner = monthUp.owner = monthDn.owner = self;  //hack to allow us to access self calendar in the events (<fix>??)

		//assign the event handlers for the controls
		function selectonchange()	{
			if(self.goToMonth(self.yearSelect.value,self.monthSelect.value)) {
				self.displayMonth = self.monthSelect.value;
				self.displayYear = self.yearSelect.value;
			}
			else {
				self.monthSelect.value = self.displayMonth;
				self.yearSelect.value = self.displayYear;
			}
		}
		addEventHandler(monthUp,'click',function(){self.nextMonth();});
		addEventHandler(monthDn,'click',function(){self.prevMonth();});
		addEventHandler(self.monthSelect,'change',selectonchange);
		addEventHandler(self.yearSelect,'change',selectonchange);

		//and finally add the elements to the containing div
		container.appendChild(monthDn);
		container.appendChild(self.monthSelect);
		container.appendChild(self.yearSelect);
		container.appendChild(monthUp);
		return container;
	}
	//-----------------------------------------------------------------------------
	/**
	 * Creates the footer of the calendar - goes under the calendar cells
	 */
	function createFooter() {
		var container = document.createElement('div');
		var clearSelected = document.createElement('input');
		clearSelected.setAttribute('type','button');
		clearSelected.setAttribute('value',self.clearbtn_caption);
		clearSelected.setAttribute('title',self.clearbtn_title);
		clearSelected.owner = self;
		addEventHandler(clearSelected,'click',function() {self.resetSelections(false);});
		container.appendChild(clearSelected);
		if(self.mode == 'popup') {
			var closeBtn = document.createElement('input');
			closeBtn.setAttribute('type','button');
			closeBtn.setAttribute('value',self.closebtn_caption);
			closeBtn.setAttribute('title',self.closebtn_title);
			addEventHandler(closeBtn,'click',function(){self.hide();});
			setClass(closeBtn,'closeBtn');
			container.appendChild(closeBtn);
		}
		return container;
	}
	//-----------------------------------------------------------------------------
	/**
	 * Creates the heading containing the day names
	 */
	function createDayHeading() {
		//create the table element
		self.calHeading = document.createElement('thead');
		setClass(self.calHeading,'caldayheading');
		var tr = document.createElement('tr'), th_tmp = document.createElement('th'),th;
		self.cols = new Array(false,false,false,false,false,false,false);

		//if we're showing the week headings, create an empty <td> for filler
		if(self.showWeeks) {
			th = th_tmp.cloneNode(false);
			setClass(th,'wkhead');
			tr.appendChild(th);
		}
		//populate the day titles
		for(var dow=0;dow<7;dow++) {
			th = th_tmp.cloneNode(false);
			th.appendChild(document.createTextNode(self.daynames[dow]));
			if(self.selectMultiple) { //if selectMultiple is true, assign the cell a CalHeading Object to handle all events
				th.headObj = new CalHeading(self,th,(dow + self.startDay < 7 ? dow + self.startDay : dow + self.startDay - 7));
			}
			tr.appendChild(th);
		}
		self.calHeading.appendChild(tr);
		return self.calHeading;
	}
	//-----------------------------------------------------------------------------
	/**
	 * Creates the table containing the calendar day cells
	 */
	function createCalCells() {
		self.rows = new Array(false,false,false,false,false,false);
		self.cells = new Array();
		var row = -1, totalCells = (self.showWeeks ? 48 : 42);
		var beginDate = new Date(self.displayYear,self.displayMonth,1);
		var endDate = new Date(self.displayYear,self.displayMonth,self.monthDayCount[self.displayMonth]);
		var sdt = new Date(beginDate);
		sdt.setDate(sdt.getDate() + (self.startDay - beginDate.getDay()) - (self.startDay - beginDate.getDay() > 0 ? 7 : 0) );
		//create the table element to hold the cells
		self.calCells = document.createElement('tbody');
		var tr_tmp = document.createElement('tr'),td_tmp = document.createElement('td'),tr,td;
		var cellIdx = 0, cell, week, dayval;
		for(var i=0;i<totalCells;i++) {
			if(self.showWeeks) { //if we are showing the week headings
				if(i % 8 == 0) {
					row++;
					week = sdt.getWeek(self.startDay);
					tr = tr_tmp.cloneNode(false);
					td = td_tmp.cloneNode(false);
					//if selectMultiple is enabled, create the associated weekObj objects - //otherwise just set the class of the td for consistent look
					self.selectMultiple ? td.weekObj = new WeekHeading(self,td,week,row) : setClass(td,'wkhead');
					td.appendChild(document.createTextNode(week));
					tr.appendChild(td);
					i++;
				}
			}
			else if(i % 7 == 0) { //otherwise, new row every 7 cells
				row++;
				week = sdt.getWeek(self.startDay);
				tr = tr_tmp.cloneNode(false);
			}
			//create the day cells
			dayval = sdt.getDate();
			td = td_tmp.cloneNode(false);
			td.appendChild(document.createTextNode(dayval));
			cell = new CalCell(self,td,sdt,row,week); //the CalCell object to be assigned to this cell
			self.cells[cellIdx] = cell;
			td.cellObj = cell;
			tr.appendChild(td);
			self.calCells.appendChild(tr);
			self.reDraw(cellIdx++); //and paint the cell according to its properties
			sdt.setDate(dayval+1); //increment the date
		}
		return self.calCells;
	}
	//-----------------------------------------------------------------------------
	/**
	 * Runs all the operations necessary to change the mode of the calendar
	 * @param HTMLInputElement targetelement
	 */
	function setMode(targetelement) {
		if(self.mode == 'popup') { //set positioning to absolute for popup
			self.calendar.style.position = 'absolute';
		}
		//if a target element has been set, append the calendar to it
		if(targetelement) {
			switch(self.mode) {
				case 'flat':
					self.tgt = targetelement;
					self.tgt.appendChild(self.calendar);
					self.visible = true;
					break;
				case 'popup':
					self.calendar.style.position = 'absolute';
					document.body.appendChild(self.calendar);
					self.setTarget(targetelement,false);
					break;
			}
		}
		else { //otherwise, add the calendar to the document.body (useful if targetelement will not be defined until after the calendar is initialized)
			document.body.appendChild(self.calendar);
			self.visible = false;
		}
	}
	//-----------------------------------------------------------------------------
	/**
	 * Removes the calendar table cells from the DOM (does not delete the cell objects associated with them)
	 */
	function deleteCells() {
		self.calendar.celltable.removeChild(self.calendar.celltable.childNodes[1]); //remove the tbody element from the cell table
	}
	//-----------------------------------------------------------------------------
	/**
	 * Sets the CSS class of the element, W3C & IE
	 * @param HTMLElement element
	 * @param string className
	 */
	function setClass(element,className) {
		element.setAttribute('class',className);
		element.setAttribute('className',className); //<iehack>
	}
	//-----------------------------------------------------------------------------
	/**
	 * Updates a cell's data, including css class and selection properties
	 * @param int cellindex
	 */
	function setCellProperties(cellindex) {
		var cell = self.cells[cellindex];
		var date;
		idx = self.dateInArray(self.dates,cell.date);
		if(idx > -1) {
			date = self.dates[idx]; //reduce indirection
			cell.date.selected = date.selected || false;
			cell.date.type = date.type;
			cell.date.canSelect = date.canSelect;
			cell.setTitle(date.title);
			cell.setURL(date.href);
			cell.setHTML(date.cellHTML);
		}
		else {
			cell.date.selected = false; //if the cell's date isn't in the dates array, set it's selected value to false
		}
		//make all cells lying outside the min and max dates un-selectable
		if(cell.date.getTime() < self.minDate.getTime() || cell.date.getTime() > self.maxDate.getTime()) {
			cell.date.canSelect = false;
		}
		cell.setClass();
	}
	//-----------------------------------------------------------------------------
	function cal_onmouseover() {
		self.mousein = true;
	}
	//-----------------------------------------------------------------------------
	function cal_onmouseout()	{
		self.mousein = false;
	}
	//-----------------------------------------------------------------------------
	/**
	 * Initializes the calendar's XML Parser and loads the configuration XML
	 * @param string xml
	 */
	function initXML(xml) {
		try { //first try the Microsoft version
			self.xmlParser = new ActiveXObject('Microsoft.XMLDOM');
			self.domType = 'ie';
		}
		catch (E) { //if not Microsoft, try eveyone else's
			self.xmlParser = new DOMParser();
			self.domType = 'mz';
		}
		if(xml) {
			processXML(xml,true); //and process the config XML
		}
	}
	//-----------------------------------------------------------------------------
	/**
	 * Takes an xml string or XMLDoc and inserts the dates into the calendar's dates array
	 * See http://www.meanfreepath.com/epoch-prime/xmlspecs.html for instructions on the xml schema
	 * @param mixed xml
	 * @param bool init
	 */
	function processXML(xml,init) {
		var dateRegExp = new RegExp('(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)'); //regular expression to match dates of form 'yyyy-mm-dd'
		var xmlobj;
		//fetches the value of a tag
		function tagVal(parent,tagName) {
			var tag = parent.getElementsByTagName(tagName);
			return tag.length ? tag[0].firstChild.nodeValue : null;
		}
		//gets a DOM handle on a tag
		function tagHnd(parent,tagName) {
			return parent ? parent.getElementsByTagName(tagName)[0] : null;
		}

		//if user passes an xml string, parse it into an xml doc
		if(typeof(xml) == 'string') {
			switch(self.domType) {
				case 'ie':
					xmlobj = self.xmlParser;
					xmlobj.loadXML(xml);
					break;
				case 'mz':
					xmlobj = self.xmlParser.parseFromString(xml, 'text/xml');
					break;
			}
		}
		else {
			xmlobj = xml;
		}
		//Load the config variables, if specified
		var cfgNode = tagHnd(xmlobj,'configs');
		if(cfgNode) {
			//language and certain config variables CANNOT be changed after calendar initialization
			if(init) {
				var langNode = tagHnd(cfgNode,'lang');
				//if loading language configs
				if(langNode) {
					//get a handle on the various language nodes
					var daynames_sh_Node = tagHnd(langNode,'daylist_sh');
					var daynames_ln_Node = tagHnd(langNode,'daylist_ln');
					var monthnames_sh_Node = tagHnd(langNode,'months_sh');
					var monthnames_ln_Node = tagHnd(langNode,'months_ln');
					var ltNode = tagHnd(langNode,'langtexts');
					//language text values
					if(ltNode) {
						self.clearbtn_caption = tagVal(ltNode,'clearbtn_caption') || self.clearbtn_caption;
						self.maxrange_caption = tagVal(ltNode,'maxrange_caption') || self.maxrange_caption;
						self.closebtn_caption = tagVal(ltNode,'closebtn_caption') || self.closebtn_caption;
						self.monthup_title = tagVal(ltNode,'monthup_title') || self.monthup_title;
						self.monthdn_title = tagVal(ltNode,'monthdn_title') || self.monthdn_title;
						self.clearbtn_title = tagVal(ltNode,'clearbtn_title') || self.clearbtn_title;
						self.closebtn_title = tagVal(ltNode,'closebtn_title') || self.closebtn_title;
					}
					//create the day name arrays
					if(daynames_sh_Node) {
						self.daylist = new Array();
						for(var i=0;i<7;i++) {
							self.daylist[i] = daynames_sh_Node.childNodes[i].firstChild.nodeValue;
							self.daylist[i+7] = self.daylist[i];
						}
					}
					/*if(daynames_ln_Node) {
						self.daylist = new Array();
						for(var i=0;i<7;i++) {
							self.daylist[i] = daynames_ln_Node.childNodes[i].firstChild.nodeValue;
							self.daylist[i+7] = self.daylist[i];
						}
					}*/
					//create the month name arrays
					if(monthnames_sh_Node) {
						self.months_sh = new Array();
						for(var i=0;i<12;i++) {
							self.months_sh[i] = monthnames_sh_Node.childNodes[i].firstChild.nodeValue;
						}
					}
					if(monthnames_ln_Node) {
						self.months_ln = new Array();
						for(var i=0;i<12;i++) {
							self.months_ln[i] = monthnames_ln_Node.childNodes[i].firstChild.nodeValue;
						}
					}
				}
				//init config values.  Only used on calendar initialization
				var icNode = tagHnd(cfgNode,'initcfg');
				if(icNode) {
					var tmp;
					self.name = tagVal(icNode,'name') || self.name; //name prepended before any ID attributes in the calendar's DOM representation
					self.mode = tagVal(icNode,'mode') || self.mode; //can be 'flat' or 'popup'
					//we use the type-safe NOT operator (!==) since the below config values may loosely evaluate to false
					tmp = tagVal(icNode,'multiselect');
					if(tmp){self.selectMultiple = (tmp == 'yes');} //whether to allow multiple dates to be simultaneously selected
					if(self.mode == 'popup') {
						self.selectMultiple = false; //always set to false if in popup mode
					}
					tmp = tagVal(icNode,'startday');
					if(tmp){self.startDay = parseInt(tmp);} // the day the week will 'start' on: 0(Sun) to 6(Sat)
					tmp = tagVal(icNode,'showweeks');
					if(tmp){self.showWeeks = (tmp == 'yes');} //whether the week numbers will be shown
					tmp = tagVal(icNode,'selcurmonthonly');
					if(tmp){self.selCurMonthOnly = (tmp == 'yes');} //allow user to only select dates in the currently displayed month
				}
			}
			//other config values.  These may be set at any time
			var stcNode = tagHnd(cfgNode,'statecfg');
			if(stcNode) {
				var tmp;
				self.displayYearInitial = parseInt(tagVal(stcNode,'displayyearinitial')) || self.displayYearInitial; //the initial year to display on load
				tmp = tagVal(stcNode,'displaymonthinitial');
				if(tmp){self.displayMonthInitial = parseInt(tmp) - 1;} //the initial month to display on load (0-11)
				self.displayYear = parseInt(tagVal(stcNode,'displayyear')) || self.displayYear;
				tmp = tagVal(stcNode,'displaymonth');
				if(tmp){self.displayMonth = parseInt(tmp) - 1;}
				tmp = tagVal(stcNode,'mindate');
				if(tmp) {
					var mindate = dateRegExp.exec(tmp);
					self.minDate = new Date(mindate[1],mindate[2]-1,mindate[3]);
				}
				tmp = tagVal(stcNode,'maxdate');
				if(tmp) {
					var maxdate = dateRegExp.exec(tmp);
					self.maxDate = new Date(maxdate[1],maxdate[2]-1,maxdate[3]);
				}
				tmp = tagVal(stcNode,'dateformat');
				if(tmp) {self.defDateFormat = tmp;}
			}
		}
		//and load the dates, if specified
		var datesadd = tagHnd(xmlobj,'datesadd');
		var datesrem = tagHnd(xmlobj,'datesrem');
		//if removing dates
		if(datesrem) {
			var dates = datesrem.getElementsByTagName('date');
			var dateArray = new Array, ds;
			//create the list of dates to remove from the dates array
			for(var i=0;i<dates.length;i++) {
				//parse the value of the date
				ds = dateRegExp.exec(dates[i].getAttribute('value'));
				dateArray[i] = new Date(ds[1],ds[2]-1,ds[3]);
			}
			self.removeDates(dateArray,false);
		}

		//if adding dates
		if(datesadd) {
			var dates = datesadd.getElementsByTagName('date');
			var dateArray = new Array, ds, dateObj;
			//add the individual dates to the dates array
			for(var i=0;i<dates.length;i++) {
				//parse the value of the date
				ds = dateRegExp.exec(dates[i].getAttribute('value'));
				dateObj = new Date(ds[1],ds[2]-1,ds[3]);
				//and set the other attributes
				dateObj.type = dates[i].getAttribute('type') || 'normal';
				dateObj.title = dates[i].getAttribute('title') || '';
				dateObj.href = dates[i].getAttribute('href') || '';
				//set the date's attributes based on the XML
				var selected = dates[i].getAttribute('selected');
				dateObj.canSelect = dateObj.selected = true; //default cell to be selectable AND selected
				try {
					switch(selected) {
						case 'disabled':
							dateObj.canSelect = dateObj.selected = false;
							break;
						case 'no':
							dateObj.selected = false;
							break;
						default:
							if(selected && selected != 'yes') {
								dateObj.selected = false;
								throw 'Invalid value for selected: \''+selected+'\'';
							}
					}
				}
				catch(er) {
					alert(er);
				}
				//get the HTML for the cell contents
				dateObj.cellHTML = dates[i].firstChild ? dates[i].firstChild.nodeValue : '';
				dateArray[dateArray.length] = dateObj; //and add the date to the temporary array
			}
			self.addDates(dateArray,false);
		}
	}
	//-----------------------------------------------------------------------------
	/**
	 * Updates the calendar's selectedDates pointer array
	 */
	function updateSelectedDates() {
		var idx = 0;
		self.selectedDates = new Array();
		for(i=0;i<self.dates.length;i++) {
			if(self.dates[i].selected) {
				self.selectedDates[idx++] = self.dates[i];
			}
		}
	}
	//PUBLIC METHODS
	//-----------------------------------------------------------------------------
	/**
	 * Find a date in the given array, returning its index if found, -1 if not
	 * @param array arr
	 * @param Date searchVal
	 * @param int startIndex
	 * @return int
	 */
	self.dateInArray = function(arr,searchVal,startIndex) {
		startIndex = (startIndex != null ? startIndex : 0); //default startIndex to 0, if not set
		for(var i=startIndex;i<arr.length;i++) {
			if(searchVal.getUeDay() == arr[i].getUeDay()) {
				return i;
			}
		}
		return -1;
	};
	//-----------------------------------------------------------------------------
	/**
	 * Changes the target element of this calendar to another input.
	 * Many thanks to Jake Olefsky - jake@olefsky.com
	 * @param HTMLInputElement targetelement
	 * @param bool focus
	 */
	self.setTarget = function (targetelement, focus) {
		//if this is a popup calendar
		if(self.mode == 'popup') {
			//declare the event handlers for the target element
			function popupFocus() {
				self.show();
			}
			function popupBlur() {
				if(!self.mousein){
					self.hide();
				}
			}
			function popupKeyDown() {
				self.hide();
			}
			//unset old target element event handlers (if there is one yet)
			if(self.tgt) {
				removeEventHandler(self.tgt,'focus',popupFocus);
				removeEventHandler(self.tgt,'blur',popupBlur);
				removeEventHandler(self.tgt,'keydown',popupKeyDown);
			}
			//and set the new target element
			self.tgt = targetelement;
			var dto = self.tgt.dateObj,pdateArr = new Array;
			//if a date is set for the target element
			if(dto) {
				if(self.tgt.value.length) { //load it into the calendar...
					pdateArr[0] = dto;
				}
				self.goToMonth(dto.getFullYear(),dto.getMonth()); //...and go to the target's month/year
			}
			self.selectDates(pdateArr,true,true,true);
			self.topOffset = self.tgt.offsetHeight; // the vertical distance (in pixels) to display the calendar from the Top of its input element
			self.leftOffset = 0; 					// the horizontal distance (in pixels) to display the calendar from the Left of its input element
			self.updatePos(self.tgt);
			//and add the event handlers to the new element
			addEventHandler(self.tgt,'focus',popupFocus);
			addEventHandler(self.tgt,'blur',popupBlur);
			addEventHandler(self.tgt,'keydown',popupKeyDown);
			if(focus !== false) { //focus the target element immediately, unless otherwise specified
				popupFocus();
			}
		}
		else { //if this is a flat or inline calendar
			//if the target is already set, remove the calendar's DOM representation from it
			if(self.tgt) {
				self.tgt.removeChild(self.calendar);
			}
			//now, set the calendar's target to the new target element, and show the calendar
			self.tgt = targetelement;
			self.tgt.appendChild(self.calendar);
			self.show();
		}
	};
	//-----------------------------------------------------------------------------
	/**
	 * Go to the next month.  if the month is December, go to January of the next year
	 * Returns true if the month will be incremented
	 * @return bool
	 */
	self.nextMonth = function () {
		var month = self.displayMonth;
		var year = self.displayYear;
		//increment the month/year values, provided they're within the min/max ranges
		if(self.displayMonth < 11) { //i.e. if currently in the year
			month++;
		}
		else if(self.yearSelect.value < self.maxDate.getFullYear()) { //if not, increment the year as well
			month = 0;
			year++;
		}
		return self.goToMonth(year,month);
	};
	//-----------------------------------------------------------------------------
	/**
	 * Go to the previous month - if the month is January, go to December of the previous year.
	 * Returns true if the month will be decremented
	 * @return bool
	 */
	self.prevMonth = function () {
		var month = self.displayMonth;
		var year = self.displayYear;
		//increment the month/year values, provided they're within the min/max ranges
		if(self.displayMonth > 0) { //i.e. if currently in the year
			month--;
		}
		else { //if not, decrement the year as well
			month = 11;
			year--;
		}
		return self.goToMonth(year,month);
	};
	//-----------------------------------------------------------------------------
	/**
	 * Sets the calendar to display the requested month/year, returning true if the
	 * date is within the minimum and maximum allowed dates
	 * @param int year
	 * @param int month
	 * @return bool
	 */
	self.goToMonth = function (year,month) {
		var testdatemin = new Date(year, month, 31);
		var testdatemax = new Date(year, month, 1);
		if(testdatemin >= self.minDate && testdatemax <= self.maxDate) {
			self.monthSelect.value = self.displayMonth = month;
			self.yearSelect.value = self.displayYear = year;
			//recreate the calendar for the new month
			createCalCells();
			deleteCells();
			self.calendar.celltable.appendChild(self.calCells);
			return true;
		}
		else {
			alert(self.maxrange_caption);
			return false;
		}
	};
	//-----------------------------------------------------------------------------
	/**
	 * Moves the calendar's position to the target element's location (popup mode only)
	 */
	self.updatePos = function (target) {
		if(self.mode == 'popup') {
			self.calendar.style.top = getTop(target) + self.topOffset + 'px';
			self.calendar.style.left = getLeft(target) + self.leftOffset + 'px';
		}
	};
	//-----------------------------------------------------------------------------
	/**
	 * Displays the calendar
	 */
	self.show = function ()	{
		self.updatePos(self.tgt); //update the calendar position, in case the page layout has changed since loading
		self.calendar.style.display = 'block'; //'table'; //<iehack> 'table' is the W3C-recommended spec, but IE isn't a fan of those
		self.visible = true;
	};
	//-----------------------------------------------------------------------------
	/**
	 * Hides the calendar
	 */
	self.hide = function () {
		self.calendar.style.display = 'none';
		self.visible = false;
	};
	//-----------------------------------------------------------------------------
	/**
	 * Toggles (shows/hides) the calendar depending on its current state
	 */
	self.toggle = function () {
		self.visible ? self.hide() : self.show();
	};
	//-----------------------------------------------------------------------------
	/**
	 * Adds the array "dates" to the calendar's dates array, removing duplicate dates,
	 * and redraws the calendar if redraw is true
	 * @param array dates
	 * @param bool redraw
	 */
	self.addDates = function (dates,redraw) {
		for(var i=0;i<dates.length;i++) {
			if(self.dateInArray(self.dates,dates[i]) == -1) { //if the date isn't already in the array, add it!
				self.dates[self.dates.length] = dates[i];
			}
		}
		updateSelectedDates();
		if(redraw != false) { //redraw  the calendar if "redraw" is false or undefined
			self.reDraw();
		}
	};
	//-----------------------------------------------------------------------------
	/**
	 * Removes the dates from the calendar's dates array and redraws the calendar
	 * if redraw is true
	 * @param array dates
	 * @param bool redraw
	 */
	self.removeDates = function (dates,redraw) {
		var idx;
		for(var i=0;i<dates.length;i++)
		{
			idx = self.dateInArray(self.dates,dates[i]);
			if(idx != -1) { //search for the dates in the dates array, removing them if the dates match
				self.dates.splice(idx,1);
			}
		}
		updateSelectedDates();
		if(redraw != false) { //redraw  the calendar if "redraw" is true or undefined
			self.reDraw();
		}
	};
	//-----------------------------------------------------------------------------
	/**
	 * Selects or Deselects an array of dates
	 * @param Array inpdates
	 * @param bool selectVal
	 * @param bool redraw
	 * @param bool removeothers
	 */
	self.selectDates = function (inpdates,selectVal,redraw,removeothers) {
		var idx;
		if(removeothers == true) {
			for(var i=0;i<self.dates.length;i++) {
				self.dates[i].selected = false;
			}
		}
		for(var i=0;i<inpdates.length;i++) {
			idx = self.dateInArray(self.dates,inpdates[i]);
			if(selectVal == true) {
				inpdates[i].selected = true;
				if(idx == -1) { //if the date does not exist in the calendar's dates array, add it
					self.dates[self.dates.length] = inpdates[i];
				}
				else { //if not, just select it
					self.dates[idx].selected = true;
				}
			}
			else { //if deselecting...
				if(idx > -1) { //if the date is found, deselect and/or remove it from the calendar's dates array
					self.dates[idx].selected = inpdates[i].selected = false;
					if(self.dates[idx].type == 'normal') { //remove 'normal' dates from the dates array, since they're useless unless selected
						self.dates.splice(idx,1);
					}
				}
			}
		}
		updateSelectedDates();
		if(self.mode == 'popup' && self.selectedDates.length) {
			self.tgt.value = self.selectedDates[0].dateFormat(self.defDateFormat);
		}
		if(redraw != false) { //redraw the calendar if "redraw" is false or undefined
			self.reDraw();
		}
	};
	//-----------------------------------------------------------------------------
	/**
	 * Adds the dates in dates as hidden inputs to the form "form".  inputname
	 * is the name of each hidden element. "form" can either be a pointer to the form's
	 * DOM element or its id string.
	 * @param mixed form
	 * @param string inputname
	 */
	self.sendForm = function(form,inputname) {
		var inpname = inputname || 'epochdates', f, inp;
		f = (typeof(form) == 'string' ? document.getElementById(form) : form);
		if(!f) {
			alert('ERROR: Invalid form input');
			return false;
		}
		for(var i=0;i<self.dates.length;i++) {
		  //only send selected dates to the server
		  if(self.dates[i].canSelect && self.dates[i].selected) {
			  inp = document.createElement('input');
			  inp.setAttribute('type','hidden');
			  inp.setAttribute('name',inpname + '['+i+']');
			  inp.setAttribute('value',encodeURIComponent(self.dates[i].dateFormat('Y-m-d')));  //default to the ISO date format
			  f.appendChild(inp);
		  }
		}
		return true;
	};
	//-----------------------------------------------------------------------------
	/**
	 * Outputs a HTTP query string to send to the server via an xmlHTTP object or other method
	 * @param string varname
	 * @param string dateFormat
	 * @param bool includeConfig
	 */
	self.outputAjaxQueryString = function(varname,dateFormat,includeConfig) {
		var qstr = '', index = 0;
		var name = varname || 'epochdate', format = dateFormat || 'Y-m-d'; //default to ISO date format (i.e. 2006-01-01)
		for(var i=0;i<self.dates.length;i++) {
			if(self.dates[i].selected == true) {
				qstr += name +'['+(index++)+']='+ encodeURIComponent(self.dates[i].dateFormat(format)) + (self.dates[i+1] ? '&' : '');
			}
		}
		if(includeConfig === true) {
			qstr += '&'+self.name+'_displayyearinitial=' + self.displayYearInitial;
			qstr += '&'+self.name+'_displaymonthinitial=' + (self.displayMonthInitial + 1);
			qstr += '&'+self.name+'_displayyear=' + self.displayYear;
			qstr += '&'+self.name+'_displaymonth=' + (self.displayMonth + 1);
			qstr += '&'+self.name+'_mindate=' + encodeURIComponent(self.minDate.dateFormat(format));
			qstr += '&'+self.name+'_maxdate=' + encodeURIComponent(self.maxDate.dateFormat(format));
		}
		return qstr;
	};
	//-----------------------------------------------------------------------------
	/**
	 * Imports dates and selected configuration values into the calendar.
	 * May be called safely at any time.  xmldata can be a string or
	 * an already-parsed XMLDocument type.
	 * @param mixed xmldata
	 */
	self.importXML = function(xmldata) {
		processXML(xmldata,false);
		self.goToMonth(self.displayYear,self.displayMonth);
	};
	//-----------------------------------------------------------------------------
	/**
	 * Export the calendar's configuration and dates to an xml string
	 * @return string
	 */
	self.exportXML = function() {
		var dt, selected;
		var xml = '<?xml version="1.0" encoding="UTF-8"?>\n<importdata>\n';
		xml += ' <configs>\n  <statecfg>\n';
		xml += '   <displayyearinitial>' + self.displayYearInitial + '</displayyearinitial>\n';
		xml += '   <displaymonthinitial>' + (self.displayMonthInitial + 1) + '</displaymonthinitial>\n';
		xml += '   <displayyear>' + self.displayYear + '</displayyear>\n';
		xml += '   <displaymonth>' + (self.displayMonth + 1) + '</displaymonth>\n';
		xml += '   <mindate>' + self.minDate.dateFormat('Y-m-d') + '<mindate>\n';
		xml += '   <maxdate>' + self.maxDate.dateFormat('Y-m-d') + '<maxdate>\n';
		xml += '  </statecfg>\n  </configs>\n';
		xml += ' <datesadd>\n';
		for(var i=0;i<self.dates.length;i++) {
			dt = self.dates[i];
			selected = (dt.selected ? 'yes' : (dt.canSelect ? 'no' : 'disabled'));
			xml += '  <date value="' + dt.dateFormat('Y-m-d') + (dt.type.length ? '" type="' +dt.type +'"' : '') + (dt.title ? ' title="' + dt.title + '"' : '') +' selected="' + selected +'">' +(dt.cellHTML ? '<![CDATA[' +dt.cellHTML +']]>' : '') + '</date>\n';
		}
		xml += ' </datesadd>\n';
		xml += '</importdata>\n';
		return xml;
	};
	//-----------------------------------------------------------------------------
	/**
	 * Erases the dates array and resets the calendar's selection variables to defaults.
	 * If retMonth is true, the calendar will return to the initial default month/year
	 * @param bool retMonth
	 */
	self.resetSelections = function (retMonth) {
		var dateArray = new Array();
		var dt = self.dates;
		for(var i=0;i<dt.length;i++) {
			if(dt[i].selected) {
				dateArray[dateArray.length] = dt[i];
			}
		}
		self.selectDates(dateArray,false,false);
		self.rows = new Array(false,false,false,false,false,false,false);
		self.cols = new Array(false,false,false,false,false,false,false);
		if(self.mode == 'popup') { //hide the calendar and clear the input element if in popup mode
			self.tgt.value = '';
			self.hide();
		}
		retMonth == true ? self.goToMonth(self.displayYearInitial,self.displayMonthInitial) : self.reDraw();
	};
	//-----------------------------------------------------------------------------
	/**
	 * Reapplies all the CSS classes for the calendar cells - usually called after changing their state
	 * If index is specified, it will redraw that cell only.
	 * @param int index
	 */
	self.reDraw = function (index) {
		self.state = 1;
		var len = index ? index + 1 : self.cells.length;
		for(var i = index || 0;i<len;i++) {
			setCellProperties(i);
		}
		self.state = 2;
	};
	//-----------------------------------------------------------------------------
	/**
	 * Returns the index of the cell whose date value matches "date", or -1 if not found
	 * @param Date date
	 * @return int
	 */
	self.getCellIndex = function(date) {
		for(var i=0;i<self.cells.length;i++) {
			if(self.cells[i].date.getUeDay() == date.getUeDay()) {
				return i;
			}
		}
		return -1;
	};
	//-----------------------------------------------------------------------------
	//begin constructor code:
	//var self = this;

	//PUBLIC VARIABLES
	self.versionNumber = '2.0.0';
	self.state = 0;

	self.curDate = new Date();
	//the various calendar variables
	self.dates = new Array();
	self.selectedDates = new Array(); //selectedDates is an array whose elements point to the SELECTED dates in the dates array

	self.calendar;
	self.calHeading;
	self.calCells;
	self.rows;
	self.cols;
	self.cells = new Array();
	//The controls
	self.monthSelect;
	self.yearSelect;
	self.mousein = false;

	//Initialize the calendar and its variables
	calConfig();
	setLang();
	initXML(xmlconfig);
	setDays();
	createCalendar(); //create the calendar DOM element and its children, and their related objects
	targetelement = typeof(targetelement) == 'string' ? document.getElementById(targetelement) : targetelement;
	setMode(targetelement);
	self.state = 2; //0: initializing, 1: redrawing, 2: finished!
	self.visible ? self.show() : self.hide();
}
//-----------------------------------------------------------------------------
/*****************************************************************************/
/**
* Object that contains the methods and properties for the calendar day headings
*/
function CalHeading(owner,tableCell,dayOfWeek) {
	//-----------------------------------------------------------------------------
	function DayHeadingonclick() {//selects/deselects the days for this object's day of week
		//reduce indirection:
		var sdates = owner.dates;
		var cells = owner.cells;
		var dateArray = new Array();
		owner.cols[dayOfWeek] = !owner.cols[dayOfWeek];
		for(var i=0;i<cells.length;i++) { //cycle through all the cells in the calendar, selecting all cells with the same dayOfWeek as this heading
			if(cells[i].dayOfWeek == dayOfWeek && cells[i].date.canSelect && (!owner.selCurMonthOnly || cells[i].date.getMonth() == owner.displayMonth && cells[i].date.getFullYear() == owner.displayYear)) { //if the cell's DoW matches, with other conditions
				dateArray[dateArray.length] = cells[i].date;
			}
		}
		owner.selectDates(dateArray,owner.cols[dayOfWeek],true);
	}
	//-----------------------------------------------------------------------------
	var self = this;
	self.dayOfWeek = dayOfWeek;
	addEventHandler(tableCell,'mouseup',DayHeadingonclick);
}
/*****************************************************************************/
/**
* Object that contains the methods and properties for the calendar week headings
*/
function WeekHeading(owner,tableCell,week,tableRow) {
	//-----------------------------------------------------------------------------
	function weekHeadingonclick() {
		//reduce indirection:
		var cells = owner.cells;
		var sdates = owner.dates;
		var dateArray = new Array();
		owner.rows[tableRow] = !owner.rows[tableRow];
		for(var i=0;i<cells.length;i++) {
			if(cells[i].tableRow == tableRow && cells[i].date.canSelect && (!owner.selCurMonthOnly || cells[i].date.getMonth() == owner.displayMonth && cells[i].date.getFullYear() == owner.displayYear)) { //if the cell's DoW matches, with other conditions)
				dateArray[dateArray.length] = cells[i].date;
			}
		}
		owner.selectDates(dateArray,owner.rows[tableRow],true);
	}
	//-----------------------------------------------------------------------------
	var self = this;
	self.week = week;
	tableCell.setAttribute('class','wkhead');
	tableCell.setAttribute('className','wkhead'); //<iehack>
	addEventHandler(tableCell,'mouseup',weekHeadingonclick);
}
/*****************************************************************************/
/**
* Object that holds all data & code related to a calendar cell
*/
/**
 * The CalCell constructor function
 * @param Epoch owner
 * @param HTMLTableCellElement tableCell
 * @param Date dateObj
 * @param int row
 * @param int week
 */
function CalCell(owner,tableCell,dateObj,row,week) {
	var self = this;
	//-----------------------------------------------------------------------------
	function calCellonclick() {
		if(self.date.canSelect) { //if the date can be selected
			if(owner.selectMultiple == true) { //if we can select multiple cells simultaneously, add the currently selected self's date to the dates array
				owner.selectDates(new Array(self.date),!self.date.selected,false);
				self.setClass(); //update the current cell's style to reflect the changes - a full redraw isn't necessary
			}
			else { //if we can only select one date at a time
				owner.selectDates(new Array(self.date),true,false,true);
				if(owner.mode == 'popup') { //update the target element's value and hide the calendar if in popup mode
					//owner.tgt.value = self.date.dateFormat(self.defDateFormat); //use the default date format defined in dateFormat
					owner.tgt.dateObj = new Date(self.date); //add a Date object to the target element for later reference
					owner.hide();
				}
				owner.reDraw(); //redraw all the calendar cells
			}
		}
	}
	//-----------------------------------------------------------------------------
	/**
	 * Replicate the CSS :hover effect for non-supporting browsers <iehack>
	 */
	function calCellonmouseover() {
		if(self.date.canSelect) {
			tableCell.setAttribute('class',self.cellClass + ' hover');
			tableCell.setAttribute('className',self.cellClass + ' hover');
		}
	}
	//-----------------------------------------------------------------------------
	/**
	 * Replicate the CSS :hover effect for non-supporting browsers <iehack>
	 */
	function calCellonmouseout() {
		self.setClass();
	}
	//-----------------------------------------------------------------------------
	/**
	 * Sets the CSS class of the cell based on the specified criteria
	 */
	self.setClass = function ()
	{
		if(self.date.canSelect !== false) { //if the cell can be selected, apply the styles in the following precedence:
			if(self.date.selected) {
				self.cellClass = 'cell_selected';
			}
			else if(owner.displayMonth != self.date.getMonth() ) {
				self.cellClass = 'notmnth';
			}
			else if(self.date.type == 'holiday') {
				self.cellClass = 'hlday';
			}
			else if(self.dayOfWeek > 0 && self.dayOfWeek < 6) {
				self.cellClass = 'wkday';
			}
			else {
				self.cellClass = 'wkend';
			}
		}
		else {
			self.cellClass = 'noselect';
		}
		//highlight the current date
		if(self.date.getUeDay() == owner.curDate.getUeDay()) {
			self.cellClass = self.cellClass + ' curdate';
		}
		tableCell.setAttribute('class',self.cellClass);
		tableCell.setAttribute('className',self.cellClass); //<iehack>
	};
	//-----------------------------------------------------------------------------
	/**
	 * Sets the cell's hyperlink, if declared
	 * @param string href
	 * @param string type ('anchor' or 'js' - default 'anchor')
	 */
	self.setURL = function(href,type) {
		if(href) {
			if(type == 'js') { //Make the WHOLE cell be a clickable link
				addEventHandler(self.tableCell,'mousedown',function(){window.location.href = href;});
			}
			else { //make only the date number of the cell a clickable link:
				var url = document.createElement('a');
				url.setAttribute('href',href);
				url.appendChild(document.createTextNode(self.date.getDate()));
				self.tableCell.replaceChild(url,self.tableCell.firstChild); //assumes the first child of the cell DOM node is the date text
			}
		}
	};
	//-----------------------------------------------------------------------------
	/**
	 * Sets the title (i.e. tooltip) that appears when a user holds their mouse cursor over a cell
	 * @param string titleStr
	 */
	self.setTitle = function(titleStr) {
		if(titleStr && titleStr.length > 0) {
			self.title = titleStr;
			self.tableCell.setAttribute('title',titleStr);
		}
	};
	//-----------------------------------------------------------------------------
	/**
	 * Sets the internal html of the cell, using a string containing html markup
	 * @param string html
	 */
	self.setHTML = function(html) {
		if(html && html.length > 0) {
			if(self.tableCell.childNodes[1]) {
				self.tableCell.childNodes[1].innerHTML = html;
			}
			else {
				var htmlCont = document.createElement('div');
				htmlCont.innerHTML = html;
				self.tableCell.appendChild(htmlCont);
			}
		}
	};
	//-----------------------------------------------------------------------------
	self.cellClass;			//the CSS class of the cell
	self.tableRow = row;
	self.tableCell = tableCell;
	self.date = new Date(dateObj);
	self.date.canSelect = true; //whether this cell can be selected or not - always true unless set otherwise externally
	self.date.type = 'normal';  //i.e. normal date, holiday, etc - always true unless set otherwise externally
	self.date.selected = false;	//whether the cell is selected (and is therefore stored in the owner's dates array)
	self.date.cellHTML = '';
	self.dayOfWeek = self.date.getDay();
	self.week = week;
	//assign the event handlers for the table cell element
	addEventHandler(tableCell,'click', calCellonclick);
	addEventHandler(tableCell,'mouseover', calCellonmouseover);
	addEventHandler(tableCell,'mouseout', calCellonmouseout);
	self.setClass();
}
/*****************************************************************************/
Date.prototype.getDayOfYear = function () //returns the day of the year for this date
{
	return parseInt((this.getTime() - new Date(this.getFullYear(),0,1).getTime())/86400000 + 1);
};
//-----------------------------------------------------------------------------
/**
 * Returns the week number for this date.  dowOffset is the day of week the week
 * "starts" on for your locale - it can be from 0 to 6. If dowOffset is 1 (Monday),
 * the week returned is the ISO 8601 week number.
 * @param int dowOffset
 * @return int
 */
Date.prototype.getWeek = function (dowOffset) {
	dowOffset = typeof(dowOffset) == 'int' ? dowOffset : 0; //default dowOffset to zero
	var newYear = new Date(this.getFullYear(),0,1);
	var day = newYear.getDay() - dowOffset; //the day of week the year begins on
	day = (day >= 0 ? day : day + 7);
	var weeknum, daynum = Math.floor((this.getTime() - newYear.getTime() - (this.getTimezoneOffset()-newYear.getTimezoneOffset())*60000)/86400000) + 1;
	//if the year starts before the middle of a week
	if(day < 4) {
		weeknum = Math.floor((daynum+day-1)/7) + 1;
		if(weeknum > 52) {
			nYear = new Date(this.getFullYear() + 1,0,1);
			nday = nYear.getDay() - dowOffset;
			nday = nday >= 0 ? nday : nday + 7;
			weeknum = nday < 4 ? 1 : 53; //if the next year starts before the middle of the week, it is week #1 of that year
		}
	}
	else {
		weeknum = Math.floor((daynum+day-1)/7);
	}
	return weeknum;
};
//-----------------------------------------------------------------------------
Date.prototype.getUeDay = function () //returns the number of DAYS since the UNIX Epoch - good for comparing the date portion
{
	return parseInt(Math.floor((this.getTime() - this.getTimezoneOffset() * 60000)/86400000)); //must take into account the local timezone
};
//-----------------------------------------------------------------------------
Date.prototype.dateFormat = function(format)
{
	if(!format) { // the default date format to use - can be customized to the current locale
		format = 'd.m.Y';
	}
	LZ = function(x) {return(x < 0 || x > 9 ? '' : '0') + x};
	var MONTH_NAMES = new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
	var DAY_NAMES = new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');
	var result="";
	var i_format=0;
	var c="";
	var token="";
	var y=this.getFullYear().toString();
	var M=this.getMonth()+1;
	var d=this.getDate();
	var E=this.getDay();
	var H=this.getHours();
	var m=this.getMinutes();
	var s=this.getSeconds();
	value = {
		Y: y.toString(),
		y: y.substring(2),
		n: M,
		m: LZ(M),
		F: MONTH_NAMES[M-1],
		M: MONTH_NAMES[M+11],
		j: d,
		d: LZ(d),
		D: DAY_NAMES[E+7],
		l: DAY_NAMES[E],
		G: H,
		H: LZ(H)
	};
	if (H==0) {value['g']=12;}
	else if (H>12){value['g']=H-12;}
	else {value['g']=H;}
	value['h']=LZ(value['g']);
	if (H > 11) {value['a']='pm'; value['A'] = 'PM';}
	else { value['a']='am'; value['A'] = 'AM';}
	value['i']=LZ(m);
	value['s']=LZ(s);
	//construct the result string
	while (i_format < format.length) {
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
		}
		if (value[token] != null) { result=result + value[token]; }
		else { result=result + token; }
	}
	return result;
};
/*****************************************************************************/
//-----------------------------------------------------------------------------
function addEventHandler(element, type, func) { //unfortunate hack to deal with Internet Explorer's horrible DOM event model <iehack>
	if(element.addEventListener) {
		element.addEventListener(type,func,false);
	}
	else if (element.attachEvent) {
		element.attachEvent('on'+type,func);
	}
}
//-----------------------------------------------------------------------------
function removeEventHandler(element, type, func) { //unfortunate hack to deal with Internet Explorer's horrible DOM event model <iehack>
	if(element.removeEventListener) {
		element.removeEventListener(type,func,false);
	}
	else if (element.attachEvent) {
		element.detachEvent('on'+type,func);
	}
}
//-----------------------------------------------------------------------------
function getTop(element) {//returns the absolute Top value of element, in pixels
	var oNode = element;
	var iTop = 0;

	while(oNode.tagName != 'HTML') {
		iTop += oNode.offsetTop || 0;
		if(oNode.offsetParent) { //i.e. the parent element is not hidden
			oNode = oNode.offsetParent;
		}
		else {
			break;
		}
	}
	return iTop;
}
//-----------------------------------------------------------------------------
function getLeft(element) { //returns the absolute Left value of element, in pixels
	var oNode = element;
	var iLeft = 0;
	while(oNode.tagName != 'HTML') {
		iLeft += oNode.offsetLeft || 0;
		if(oNode.offsetParent) { //i.e. the parent element is not hidden
			oNode = oNode.offsetParent;
		}
		else {
			break;
		}
	}
	return iLeft;
}
//-----------------------------------------------------------------------------
