package timer;

import java.util.Timer;
import java.util.TimerTask;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "reminderController")
@SessionScoped
public class Reminder {
	Timer timer;

	public Reminder() {
		int seconds = 10;
		timer = new Timer();
		timer.schedule(new RemindTask(), seconds * 1000);
	}
	
	class RemindTask extends TimerTask {
		public void run() {
			System.out.println("Time's up!");
			timer.cancel(); // Terminate the timer thread
		}
	}

	public void verificaCantas() {
		System.out.println("Task scheduled.");
	}

}
