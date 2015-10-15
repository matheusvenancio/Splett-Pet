package splett.foto.mb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.UploadedFile;

import splett.foto.Foto;
import splett.foto.dao.FotoDao;
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

	private List<Foto> fotoFiltered;

	private Foto foto;

	private UploadedFile fileUp;

	private String destino = "\\image\\";

	private UploadArquivo arquivo = new UploadArquivo();

	public FotoMB() {
		fotoFiltered = new ArrayList<Foto>();
	}

	public void criar() {
		foto = new Foto();
	}

	public void transferirArquivo(String nomeArquivo, InputStream in) {
		try {

			File file = new File(arquivo.getRealPath() + destino);
			file.mkdirs();

			File f = new File(arquivo.getRealPath() + destino + nomeArquivo);
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
		if (fileUp != null) {
			try {
				transferirArquivo(new java.util.Date().getTime() + ".jpg",
						getFileUp().getInputstream());
			} catch (IOException exception) {

			}

			foto.setContentType(fileUp.getContentType());
			foto.setCaminho(arquivo.getRealPath() + destino
					+ new java.util.Date().getTime() + ".jpg");
			foto.setNome(new java.util.Date().getTime() + ".jpg");
			foto.setUsuario(new Usuario());
			foto.setUsuario(sessionMB.getUsuarioLogado());
			fotoDao.salvar(foto);
		}
	}

	public void salvar() {
		if (foto.getId() != null)
			fotoDao.update(foto);
		else
			fotoDao.salvar(foto);
	}

	public void remover() {
		File file = new File(foto.getCaminho());
		file.delete();
		fotoDao.remover(foto);
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

	public void setFotoLazyDataModel(FotoLazyDataModel fotoLazyDataModel) {
		this.fotoLazyDataModel = fotoLazyDataModel;
	}

	public List<Foto> getFotoFiltered() {
		return fotoFiltered;
	}

	public void setFotoFiltered(List<Foto> fotoFiltered) {
		this.fotoFiltered = fotoFiltered;
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
