package br.net.woodstock.epm.document.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.security.digest.DigestType;
import br.net.woodstock.rockframework.security.digest.impl.AsStringDigester;
import br.net.woodstock.rockframework.security.digest.impl.BasicDigester;
import br.net.woodstock.rockframework.security.digest.impl.HexDigester;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.IOUtils;

public class FileSystemRepository implements Service {

	private static final long	serialVersionUID	= -7290717732247236271L;

	private AsStringDigester	digester			= new AsStringDigester(new HexDigester(new BasicDigester(DigestType.SHA1)));

	private File				root;

	public FileSystemRepository(final String path) {
		super();
		Assert.notEmpty(path, "path");
		this.init(new File(path));
	}

	public FileSystemRepository(final File path) {
		super();
		Assert.notNull(path, "path");
		this.init(path);
	}

	private void init(final File path) {
		this.root = path;
		if (!this.root.exists()) {
			this.root.mkdirs();
		}
	}

	public byte[] get(final String id) throws IOException {
		String realId = this.parseId(id);
		File file = new File(this.root, realId);
		if (file.exists()) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				byte[] data = IOUtils.toByteArray(inputStream);
				return data;
			} catch (IOException e) {
				throw e;
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
		return null;
	}

	public boolean remove(final String id) {
		String realId = this.parseId(id);
		File file = new File(this.root, realId);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	public void save(final String id, final byte[] data) throws IOException {
		this.store(id, data, false);
	}

	public void update(final String id, final byte[] data) throws IOException {
		this.store(id, data, true);
	}

	private void store(final String id, final byte[] data, final boolean isUpdate) throws IOException {
		String realId = this.parseId(id);
		File file = null;
		OutputStream outputStream = null;
		try {
			file = new File(this.root, realId);

			if (!file.exists()) {
				if (isUpdate) {
					throw new FileNotFoundException();
				}
			}

			outputStream = new FileOutputStream(file);
			outputStream.write(data);
			outputStream.close();
		} catch (IOException e) {
			if (outputStream != null) {
				outputStream.close();
			}
			if (!isUpdate) {
				if ((file != null) && (file.exists())) {
					file.delete();
				}
			}
			throw e;
		}
	}

	private String parseId(final String id) {
		return this.digester.digestAsString(id);
	}
}
