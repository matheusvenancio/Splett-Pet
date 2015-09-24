package arquivo.mb;

import java.io.File;
import java.io.FileNotFoundException;
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
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import organizacao.Organizacao;
import organizacao.dao.OrganizacaoDao;

import evento.Evento;
import evento.dao.EventoDao;

import sistema.Sistema;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import usuario.mb.SenhaMd5MB;
import animal.Animal;
import animal.dao.AnimalDao;
import arquivo.Arquivo;
import arquivo.ArquivoDataModel;
import arquivo.dao.ArquivoDao;

@ManagedBean(name = "arquivoMB")
@ViewScoped
public class ArquivoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private Arquivo arquivo;
	private Usuario usuario;
	private Animal animal;
	private Evento evento;
	private Organizacao organizacao;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	@ManagedProperty(value = "#{senhaMd5}")
	private SenhaMd5MB senhaMd5;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{eventoDao}")
	private EventoDao eventoDao;

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	private List<String> imgsView = new ArrayList<String>();
	private List<Arquivo> imgs = new ArrayList<Arquivo>();

	private List<Arquivo> files = new ArrayList<Arquivo>();

	private Arquivo arqView;

	private CroppedImage imgCropped;
	private String imgPreviewName;
	private String imgNomeNew;

	private Arquivo arqSelecionado;
	private ArquivoDataModel arquivos;

	@PostConstruct
	public void init() {
		arquivos = new ArquivoDataModel(imgs);
	}

	public void cancelar() {
		arquivo = null;
	}

	public void novo() {
		arquivo = new Arquivo();
	}

	public void removerImgPreview() {
		File file = new File(arqView.getPath());
		file.delete();
		arquivoDao.remover(arqView);
		imgs.remove(arqView);

		arqView = null;
	}

	public void uploadPbl(FileUploadEvent event) {
		try {
			String nome = event.getFile().getFileName();
			String ext = nome.substring(nome.lastIndexOf('.'));
			System.out.println("ext: " + ext);
			if (ext.matches(".jpg|.png|.jpeg")) {
				File file = new File(Sistema.PATH_PLB
						+ "/"
						+ senhaMd5.criptografar(nome
								+ Calendar.getInstance(Locale.getDefault()))
						+ ext);

				InputStream is = event.getFile().getInputstream();
				OutputStream out = new FileOutputStream(file);
				byte buf[] = new byte[1024];
				int len;
				while ((len = is.read(buf)) > 0)
					out.write(buf, 0, len);
				is.close();
				out.close();

				arquivo = new Arquivo();
				arquivo.setNome(file.getName());
				arquivo.setNomeReal(nome);
				arquivo.setPath(file.getPath());
				arquivoDao.salvar(arquivo);
				imgs.add(arquivo);
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, null, "Formato invalido");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Houve um erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void uploadOrg(FileUploadEvent event) {
		try {
			System.out.println("UPLOAD ORG");
			String nome = event.getFile().getFileName();
			String ext = nome.substring(nome.lastIndexOf('.'));
			File file = new File(Sistema.PATH_ORG
					+ "/"
					+ senhaMd5.criptografar(nome
							+ Calendar.getInstance(Locale.getDefault())) + ext);

			InputStream is = event.getFile().getInputstream();
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();
			System.out.println("NOVO ARQUIVO");
			arquivo = new Arquivo();
			arquivo.setNome(file.getName());
			arquivo.setNomeReal(nome);
			arquivo.setPath(file.getPath());
			arquivoDao.salvar(arquivo);
			imgs.add(arquivo);

		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Houve um erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void removerAvatar() {
		if (usuario.getAvatar().getId() != 1) {
			File file = new File(arquivo.getPath());
			file.delete();
			arquivoDao.remover(arquivo);
		}
		usuario.setAvatar(arquivoDao.findAvatarPadrao());
		usuarioDao.update(usuario);
	}

	public void uploadAvatar(FileUploadEvent event) {
		try {
			String nome = event.getFile().getFileName();
			String ext = nome.substring(nome.lastIndexOf('.'));
			File file = new File(Sistema.PATH_USR
					+ "/"
					+ senhaMd5.criptografar(nome
							+ Calendar.getInstance(Locale.getDefault())) + ext);
			InputStream is = event.getFile().getInputstream();
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();
			if (usuario != null) {
				if (usuario.getAvatar() != null
						&& usuario.getAvatar().getId() != 1) {
					Arquivo avatar = usuario.getAvatar();
					File avatarFile = new File(avatar.getPath());
					avatarFile.delete();
					arquivoDao.remover(avatar);
				}
			}

			System.out.println("NOVO AVATAR");
			arquivo = new Arquivo();
			arquivo.setNome(file.getName());
			arquivo.setNomeReal(nome);
			arquivo.setPath(file.getPath());
			arquivo.setPath(file.getPath());
			arquivoDao.salvar(arquivo);
			imgs.add(arquivo);

			if (usuario != null) {
				usuario.setAvatar(arquivo);
				usuarioDao.update(usuario);
			}
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					null, "Pronto!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Houve um erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void uploadNovoAvatar(FileUploadEvent event) {
		try {
			String nome = event.getFile().getFileName();
			String ext = nome.substring(nome.lastIndexOf('.'));
			File file = new File(Sistema.PATH_USR
					+ "/"
					+ senhaMd5.criptografar(nome
							+ Calendar.getInstance(Locale.getDefault())) + ext);
			InputStream is = event.getFile().getInputstream();
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();

			System.out.println("NOVO AVATAR");
			arquivo = new Arquivo();
			arquivo.setNome(file.getName());
			arquivo.setNomeReal(nome);
			arquivo.setPath(file.getPath());
			arquivo.setPath(file.getPath());
			arquivoDao.salvar(arquivo);
			imgs.add(arquivo);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					null, "Pronto!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Houve um erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void uploadEvt(FileUploadEvent event) {
		try {
			String nome = event.getFile().getFileName();
			String ext = nome.substring(nome.lastIndexOf('.'));
			File file = new File(Sistema.PATH_USR
					+ "/"
					+ senhaMd5.criptografar(nome
							+ Calendar.getInstance(Locale.getDefault())) + ext);
			InputStream is = event.getFile().getInputstream();
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();

			if (evento.getImgMain() != null && evento.getImgMain().getId() != 1) {
				Arquivo avatar = evento.getImgMain();
				File avatarFile = new File(avatar.getPath());
				avatarFile.delete();
				arquivoDao.remover(avatar);
			}

			System.out.println("NOVO EVENTO IMG");
			arquivo = new Arquivo();
			arquivo.setNome(file.getName());
			arquivo.setNomeReal(nome);
			arquivo.setPath(file.getPath());
			arquivo.setPath(file.getPath());
			arquivoDao.salvar(arquivo);
			imgs.add(arquivo);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					null, "Pronto!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Houve um erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void uploadAnimal(FileUploadEvent event) {
		try {
			String nome = event.getFile().getFileName();
			System.out.println("nome arquivo: " + nome);
			String ext = nome.substring(nome.lastIndexOf('.'));
			File file = new File(Sistema.PATH_USR
					+ "/"
					+ senhaMd5.criptografar(nome
							+ Calendar.getInstance(Locale.getDefault())) + ext);
			System.out.println("File name: " + file.getName() + "path: "
					+ file.getPath());
			InputStream is = event.getFile().getInputstream();
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();

			arquivo = new Arquivo();
			arquivo.setNome(file.getName());
			arquivo.setNomeReal(nome);
			arquivo.setPath(file.getPath());
			arquivoDao.salvar(arquivo);
			imgs.add(arquivo);

			animal.setImgMain(arquivo);
			animalDao.update(animal);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					null, "Pronto!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (IOException e) {
			e.printStackTrace();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Houve um erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void uploadMsg(FileUploadEvent event) {
		try {
			String nome = event.getFile().getFileName();
			String ext = nome.substring(nome.lastIndexOf('.'));
			File file = new File(Sistema.PATH_MSG
					+ "/"
					+ senhaMd5.criptografar(nome
							+ Calendar.getInstance(Locale.getDefault())) + ext);

			InputStream is = event.getFile().getInputstream();
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();

			arquivo = new Arquivo();
			arquivo.setNome(file.getName());
			arquivo.setNomeReal(nome);
			arquivo.setPath(file.getPath());
			arquivoDao.salvar(arquivo);

			files.add(arquivo);

		} catch (IOException e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Houve um erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String crop() {
		if (imgCropped == null) {
			return null;
		}
		String newFileExt = imgPreviewName.substring(
				imgPreviewName.lastIndexOf("."), imgPreviewName.length());
		String newFileName = senhaMd5.criptografar(imgPreviewName + ""
				+ System.currentTimeMillis());
		imgNomeNew = newFileName + newFileExt;
		System.out.println(imgNomeNew);
		FileImageOutputStream imageOutput;
		try {
			imageOutput = new FileImageOutputStream(new File(Sistema.PATH_USR
					+ "\\" + imgNomeNew));
			imageOutput.write(imgCropped.getBytes(), 0,
					imgCropped.getBytes().length);
			imageOutput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
						System.out.println("usurio nomee?: "
								+ usuario.getNome());
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

	//
	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public Arquivo getArqSelecionado() {
		return arqSelecionado;
	}

	public void setArqSelecionado(Arquivo arqSelecionado) {
		this.arqSelecionado = arqSelecionado;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public List<String> getImgsView() {
		return imgsView;
	}

	public void setImgsView(List<String> imgsView) {
		this.imgsView = imgsView;
	}

	public List<Arquivo> getImgs() {
		return imgs;
	}

	public void setImgs(List<Arquivo> imgs) {
		this.imgs = imgs;
	}

	public CroppedImage getImgCropped() {
		return imgCropped;
	}

	public void setImgCropped(CroppedImage imgCropped) {
		this.imgCropped = imgCropped;
	}

	public Arquivo getArqView() {
		return arqView;
	}

	public void setArqView(Arquivo arqView) {
		this.arqView = arqView;
	}

	public String getImgPreviewName() {
		return imgPreviewName;
	}

	public void setImgPreviewName(String imgPreviewName) {
		this.imgPreviewName = imgPreviewName;
	}

	public String getImgNomeNew() {
		return imgNomeNew;
	}

	public void setImgNomeNew(String imgNomeNew) {
		this.imgNomeNew = imgNomeNew;
	}

	public SenhaMd5MB getSenhaMd5() {
		return senhaMd5;
	}

	public void setSenhaMd5(SenhaMd5MB senhaMd5) {
		this.senhaMd5 = senhaMd5;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public ArquivoDataModel getArquivos() {
		return arquivos;
	}

	public void setArquivos(ArquivoDataModel arquivos) {
		this.arquivos = arquivos;
	}

	public List<Arquivo> getFiles() {
		return files;
	}

	public void setFiles(List<Arquivo> files) {
		this.files = files;
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public AnimalDao getAnimalDao() {
		return animalDao;
	}

	public void setAnimalDao(AnimalDao animalDao) {
		this.animalDao = animalDao;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public EventoDao getEventoDao() {
		return eventoDao;
	}

	public void setEventoDao(EventoDao eventoDao) {
		this.eventoDao = eventoDao;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
	}

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
	}

}
