package gmap.mb;

import endereco.Endereco;
import endereco.cidade.Cidade;
import endereco.estado.Estado;
import endereco.mb.EnderecoMB;
import gmap.GMap;
import gmap.dao.GMapDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean(name = "gMapMB")
@ViewScoped
public class GMapMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private MapModel emptyModel;
	private GMap gmap = new GMap();
	private Endereco endereco;

	@ManagedProperty(value = "#{gMapDao}")
	private GMapDao gMapDao;

	@ManagedProperty(value = "#{enderecoMB}")
	private EnderecoMB enderecoMB;
	private String logradouro;
	private String bairro;
	private String nomeEstado;
	private String nomeCidade;

	private String center;
	private String lat = "-25.3457471";
	private String lng = "-49.1851541";

	private URL url;

	public GMapMB() {
		emptyModel = new DefaultMapModel();
	}

	public void cancelar() {
		gmap = null;
	}

	public void addMarker(ActionEvent actionEvent) {
		Marker marker = new Marker(new LatLng(gmap.getLat(), gmap.getLng()),
				gmap.getTitle());
		emptyModel.addOverlay(marker);
		System.out.println("indo salvar o gmap");

		gMapDao.salvar(gmap);

	}

	public void onPointSelect(PointSelectEvent event) {
		LatLng latlng = event.getLatLng();
		System.out.println("evento latLong: " + event.getLatLng());
		emptyModel = new DefaultMapModel();
		Marker marker = new Marker(latlng, "a");
		center = latlng.getLat() + "," + latlng.getLng();
		gmap.setLat(event.getLatLng().getLat());
		gmap.setLng(event.getLatLng().getLng());
		emptyModel.addOverlay(marker);
	}


	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public void setEmptyModel(MapModel emptyModel) {
		this.emptyModel = emptyModel;
	}

	public GMap getGmap() {
		return gmap;
	}

	public void setGmap(GMap gmap) {
		this.gmap = gmap;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public GMapDao getgMapDao() {
		return gMapDao;
	}

	public void setgMapDao(GMapDao gMapDao) {
		this.gMapDao = gMapDao;
	}

	public EnderecoMB getEnderecoMB() {
		return enderecoMB;
	}

	public void setEnderecoMB(EnderecoMB enderecoMB) {
		this.enderecoMB = enderecoMB;
	}

	//
	public void pegarEstado(Estado estado) {
		String novoEstado = estado.getNome();
		try {
			novoEstado = estado.getNome().replaceAll(" ", "+");
			novoEstado = Normalizer.normalize(novoEstado, Normalizer.Form.NFD);
			novoEstado = novoEstado.replaceAll("[^\\p{ASCII}]", "");
			System.out.println("estado sem acento: " + novoEstado);
			nomeEstado = novoEstado;
		} catch (Exception e) {
			;
		}
		try {
			url = new URL(
					"http://maps.googleapis.com/maps/api/geocode/json?address="
							+ novoEstado + "&sensor=false");
		} catch (MalformedURLException e) {
			System.out.println("exception no estado");
			e.printStackTrace();
		}
		pegarLoc(url);
	}

	public void pegarCidadeEstado(Cidade cidade) {
		String novaCidade = cidade.getNome();
		System.out.println("nome da cidade: " + cidade.getNome());
		try {
			novaCidade = cidade.getNome().replaceAll(" ", "+");
			novaCidade = Normalizer.normalize(novaCidade, Normalizer.Form.NFD);
			novaCidade = novaCidade.replaceAll("[^\\p{ASCII}]", "");
			System.out.println("cidade sem acento: " + novaCidade);
			nomeCidade = novaCidade;
			System.out.println("nomecidade: " + nomeCidade);
			System.out.println("nomecidade: " + nomeEstado);
		} catch (Exception e) {
			;
		}
		
		try {
			url = new URL(
					"http://maps.googleapis.com/maps/api/geocode/json?address="
							+ novaCidade + nomeEstado + "&sensor=false");
		} catch (MalformedURLException e) {
			System.out.println("exception na cidade");
			e.printStackTrace();
		}
		pegarLoc(url);
	}
	
	public void pegarBairroRua(Endereco endereco) {
		System.out.println("endereco gmap: ");
		String novarua = endereco.getLogradouro();
		String novoBairro = endereco.getBairro();
		System.out.println("e o estado e: " + nomeEstado);
		try {
			novarua = logradouro.replaceAll(" ", "+");
			novarua = Normalizer.normalize(novarua, Normalizer.Form.NFD);
			novarua = novarua.replaceAll("[^\\p{ASCII}]", "");
			System.out.println("rua sem acento: " + novarua);
		} catch (Exception e) {
			;
		}
		try {
			novoBairro = novoBairro.replaceAll(" ", "+");
			novoBairro = Normalizer.normalize(novoBairro, Normalizer.Form.NFD);
			novoBairro = novoBairro.replaceAll("[^\\p{ASCII}]", "");
			System.out.println("bairro sem acento: " + novoBairro);
		} catch (Exception e) {
			;
		}
		try {
			url = new URL(
					"http://maps.googleapis.com/maps/api/geocode/json?address="
							+ nomeEstado + "+" + nomeCidade+ "+" + novarua+ "+" + novoBairro
							+ "&sensor=false");
			System.out.println("URL final: " + url.toString());
		} catch (MalformedURLException e) {
			System.out.println("exception pegarBairroRua");
			e.printStackTrace();
		}

		pegarLoc(url);

	}
	
	

	// geocoder
	public void pegarLoc(URL url) {
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;

			String json = "";
			while ((str = in.readLine()) != null) {
				json = json + str;
			}

			in.close();

			String location = json.substring(json.indexOf("\"location\" :"),
					json.indexOf("\"location_type\"") - 13);
			lat = location.substring((location.indexOf("\"lat\"") + 8),
					location.indexOf(","));
			lng = location.substring((location.indexOf("\"lng\"") + 8),
					(location.indexOf("}") - 12));

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void atribuirRua(String rua) {
		System.out.println("rua 1: " + rua);
		logradouro = rua;
		System.out.println("rua: " + logradouro);
	}

	

	public void bustarGMapPorEndereco(int id) {
		gmap = gMapDao.buscarGmByEnd(id);
	}

	public String removeAccents(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getNomeEstado() {
		return nomeEstado;
	}

	public void setNomeEstado(String nomeEstado) {
		this.nomeEstado = nomeEstado;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

}
