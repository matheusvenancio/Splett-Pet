package sistema;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import arquivo.Arquivo;
import arquivo.dao.ArquivoDao;

public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	@Inject
	private static ArquivoDao arquivoDao;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String nome;
		try {
			nome = request.getParameter("nome");
		} catch (NumberFormatException e) {
			nome = null;
		}

		if (nome == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
			return;
		}

		try {
			Arquivo arquivo = arquivoDao.findByNome(nome);
			String mimeType = URLConnection.guessContentTypeFromName(arquivo
					.getNome());
			File arq = new File(arquivo.getPath());
			byte[] content = this.getBytesFromFile(arq);

			response.reset();
			response.setBufferSize(DEFAULT_BUFFER_SIZE);
			response.setContentType(mimeType);
			response.setContentLength(content.length);
			response.setHeader("Content-Disposition", "inline; filename=\""
					+ arquivo.getNome() + "\"");

			BufferedOutputStream output = null;

			try {
				output = new BufferedOutputStream(response.getOutputStream(),
						DEFAULT_BUFFER_SIZE);
				output.write(content);
			} finally {
				close(output);
			}
		} catch (NoResultException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
			return;
		}
	}

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();

		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}
}
