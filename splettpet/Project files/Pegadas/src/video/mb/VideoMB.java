package video.mb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import sistema.Sistema;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import usuario.mb.SenhaMd5MB;
import video.Video;
import video.dao.VideoDao;

@ManagedBean(name = "videoMB")
@ViewScoped
public class VideoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private Video video;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{senhaMd5}")
	private SenhaMd5MB senhaMd5;

	@ManagedProperty(value = "#{videoDao}")
	private VideoDao videoDao;

	private Video videoView;

	private VideoDataModel listaVideos;
	private List<Video> videos = new ArrayList<Video>();
	private List<Video> vds = new ArrayList<Video>();

	@PostConstruct
	public void init() {
		System.out.println("init video MB");
		listaVideos = new VideoDataModel(vds);
	}

	public void novo() {
		video = new Video();
	}

	public void Salvar(Video video) {
		try {
			videoDao.salvar(video);
			videos.add(video);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadVideoPbl(FileUploadEvent event) {
		try {
			System.out.println("fazendo upload de video");
			String nome = event.getFile().getFileName();
			String ext = nome.substring(nome.lastIndexOf('.'));
			String nomeVd = nome;
			if (vds.size() < 2) {
				if (ext.matches(".mov|.mp4|.rmvb")) {
					File file = new File(
							Sistema.PATH_VIDEO_PLB
									+ "/"
									+ senhaMd5.criptografar(nome
											+ Calendar.getInstance(Locale
													.getDefault())) + ext);

					InputStream is = event.getFile().getInputstream();
					OutputStream out = new FileOutputStream(file);
					byte buf[] = new byte[10240];
					int len = 0;
					while ((len = is.read(buf)) > 0) {
						System.out.println("lendo..");
						out.write(buf, 0, len);
					}
					is.close();
					out.close();

					video = new Video();
					video.setUrl(file.getName());
					video.setPath(file.getPath());
					video.setNome(nomeVd);
					videoDao.salvar(video);
					vds.add(video);
				}

				else {
					FacesMessage msg = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, null,
							"Formato invalido");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}else{
				FacesMessage msg = new FacesMessage("O limite maximo de videos foi atingido");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Houve um erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public String embedFormatVideo(Video vd) {
		String embedType = vd.getNome()
				.substring(vd.getNome().lastIndexOf('.'));
		if (embedType.matches(".mov|.mp4")) {
			System.out.println("formato mp4");
			return "video/quicktime";
		} else {
			return "application/x-mplayer2";
		}
	}

	public List<Video> listarVideosPubli(int id) {
		videos = videoDao.findVideosByPubli(id);
		return videos;
	}

	public void removerVideoPreview() {
		File file = new File(videoView.getPath());
		file.delete();
		videoDao.remover(videoView);
		vds.remove(videoView);

		videoView = null;
	}

	public Usuario getUsrLogado() {
		try {
			usuario = new Usuario();
			SecurityContext context = SecurityContextHolder.getContext();
			if (context instanceof SecurityContext) {
				Authentication authentication = context.getAuthentication();
				if (authentication instanceof Authentication) {
					try {

						usuario.setUsername(context.getAuthentication()
								.getPrincipal().toString());
						usuario.setUsername(((User) authentication
								.getPrincipal()).getUsername());
						String username = usuario.getUsername();
						usuario = usuarioDao.findByLogin(username);

					} catch (Exception e) {
						this.usuario = (Usuario) context.getAuthentication()
								.getPrincipal();
						String username = usuario.getUsername();
						usuario = usuarioDao.findByLogin(username);
					}
				}
				return usuario;
			} else {
				System.out.println("nn instancia");
			}
		} catch (Exception e) {
			return null;
		}
		return usuario;

	}

	public void findUsrLogado() {
		usuario = getUsrLogado();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public VideoDao getVideoDao() {
		return videoDao;
	}

	public void setVideoDao(VideoDao videoDao) {
		this.videoDao = videoDao;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public SenhaMd5MB getSenhaMd5() {
		return senhaMd5;
	}

	public void setSenhaMd5(SenhaMd5MB senhaMd5) {
		this.senhaMd5 = senhaMd5;
	}

	public Video getVideoView() {
		return videoView;
	}

	public void setVideoView(Video videoView) {
		this.videoView = videoView;
	}

	public VideoDataModel getListaVideos() {
		return listaVideos;
	}

	public void setListaVideos(VideoDataModel listaVideos) {
		this.listaVideos = listaVideos;
	}

	public List<Video> getVds() {
		return vds;
	}

	public void setVds(List<Video> vds) {
		this.vds = vds;
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	//

}
