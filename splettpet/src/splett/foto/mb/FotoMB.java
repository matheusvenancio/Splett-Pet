package splett.foto.mb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.UploadedFile;

import splett.animal.Animal;
import splett.animal.dao.AnimalDao;
import splett.foto.Foto;
import splett.foto.dao.FotoDao;
import splett.perfil.mb.PerfilMB;
import splett.session.SessionMB;
import splett.usuario.Usuario;

@ManagedBean(name = "fotoMB")
@ViewScoped
public class FotoMB {

	@ManagedProperty(value = "#{fotoDao}")
	private FotoDao fotoDao;

	@ManagedProperty(value = "#{fotoLazyDataModel}")
	private FotoLazyDataModel fotoLazyDataModel;

	@ManagedProperty(value = "#{sessionMB}")
	private SessionMB sessionMB;

	@ManagedProperty(value = "#{perfilMB}")
	private PerfilMB perfilMB;

	private List<Foto> fotoFiltered;

	private Foto foto;

	private UploadedFile fileUp;

	private String destino = "//image//";

	private UploadArquivo arquivo = new UploadArquivo();

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;

	private Animal animal;

	public FotoMB() {
		fotoFiltered = new ArrayList<Foto>();
	}

	public void criar() {
		foto = new Foto();
	}

	public void transferirArquivo(String caminho, String nome, InputStream in) {
		try {
			File file = new File(caminho);
			file.mkdir();
			File f = new File(caminho + nome);

			f.createNewFile();
			OutputStream out = new FileOutputStream(f);
			int reader = 0;
			byte[] bytes = new byte[(int) getFileUp().getSize()];

			while ((reader = in.read(bytes)) != -1) {
				out.write(bytes, 0, reader);
			}
			in.close();
			out.flush();
			out.close();
		} catch (IOException exception) {

		}
	}

	public void doUpload() {
		Date data = new Date();
		String nome = data.getTime() + fileUp.getFileName();
		if (fileUp != null) {
			try {
				transferirArquivo(arquivo.getRealPath() + destino, nome,
						getFileUp().getInputstream());
			} catch (IOException exception) {

			}

			foto.setCaminho(arquivo.getRealPath() + destino + data.getTime()
					+ fileUp.getFileName());
			foto.setNome(nome);
			foto.setUsuario(new Usuario());
			foto.setUsuario(perfilMB.getUsuarioVisualizado());
			fotoDao.salvar(foto);
		}
	}

	public void salvar() {
		if (foto.getId() != null)
			fotoDao.update(foto);
		else
			fotoDao.salvar(foto);
		foto = null;
	}

	public void remover() {
		File file = new File(foto.getCaminho());
		file.delete();
		fotoDao.remover(foto);
	}

	public void doUploadFotoPerfil() {
		Date data = new Date();
		String nome = data.getTime() + fileUp.getFileName();
		if (fileUp != null) {
			try {
				transferirArquivo(arquivo.getRealPath() + destino, nome,
						getFileUp().getInputstream());
			} catch (IOException exception) {

			}

			Usuario usuario = perfilMB.getUsuarioVisualizado();
			usuario.setFotoPerfil(arquivo.getRealPath() + destino
					+ data.getTime() + fileUp.getFileName());

			usuario.setNomeFotoPerfil(nome);
			perfilMB.getUsuarioDao().salvar(usuario);
		}
	}

	public void doUploadFotoAnimal() {
		Date data = new Date();
		String nome = data.getTime() + fileUp.getFileName();
		if (fileUp != null) {
			try {
				transferirArquivo(arquivo.getRealPath() + destino, nome,
						getFileUp().getInputstream());
			} catch (IOException exception) {

			}

			animal.setFotoCaminho(arquivo.getRealPath() + destino
					+ data.getTime() + fileUp.getFileName());

			animal.setFotoNome(nome);
			animalDao.update(animal);

		}
	}

	public void cancelar() {
		if (foto.getCaminho() != null) {
			remover();
		}
		fileUp = null;
		foto = null;
	}

	public SessionMB getSessionMB() {
		return sessionMB;
	}

	public void setSessionMB(SessionMB sessionMB) {
		this.sessionMB = sessionMB;
	}

	public FotoDao getFotoDao() {
		return fotoDao;
	}

	public void setFotoDao(FotoDao fotoDao) {
		this.fotoDao = fotoDao;
	}

	public FotoLazyDataModel getFotoLazyDataModel() {
		return fotoLazyDataModel;
	}

	public AnimalDao getAnimalDao() {
		return animalDao;
	}

	public void setAnimalDao(AnimalDao animalDao) {
		this.animalDao = animalDao;
	}

	public void setFotoLazyDataModel(FotoLazyDataModel fotoLazyDataModel) {
		this.fotoLazyDataModel = fotoLazyDataModel;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public List<Foto> getFotoFiltered() {
		return fotoFiltered;
	}

	public void setFotoFiltered(List<Foto> fotoFiltered) {
		this.fotoFiltered = fotoFiltered;
	}

	public PerfilMB getPerfilMB() {
		return perfilMB;
	}

	public void setPerfilMB(PerfilMB perfilMB) {
		this.perfilMB = perfilMB;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public UploadedFile getFileUp() {
		return fileUp;
	}

	public void setFileUp(UploadedFile fileUp) {
		this.fileUp = fileUp;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public UploadArquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadArquivo arquivo) {
		this.arquivo = arquivo;
	}

}
