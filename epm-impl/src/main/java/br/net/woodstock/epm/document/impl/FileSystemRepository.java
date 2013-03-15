package br.net.woodstock.epm.document.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.net.woodstock.epm.document.api.ContentRepository;
import br.net.woodstock.rockframework.core.util.Assert;
import br.net.woodstock.rockframework.core.utils.IO;
import br.net.woodstock.rockframework.domain.ServiceException;
import br.net.woodstock.rockframework.security.digest.DigestType;
import br.net.woodstock.rockframework.security.digest.impl.AsStringDigester;
import br.net.woodstock.rockframework.security.digest.impl.BasicDigester;
import br.net.woodstock.rockframework.security.digest.impl.HexDigester;

public class FileSystemRepository implements ContentRepository {

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

	@Override
	public byte[] getContentById(final Integer id) {
		try {
			String realId = this.parseId(id);
			File file = new File(this.root, realId);
			if (file.exists()) {
				InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(file);
					byte[] data = IO.toByteArray(inputStream);
					return data;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveContent(final Integer id, final byte[] data) {
		this.store(id, data, false);
	}

	@Override
	public void updateContent(final Integer id, final byte[] data) {
		this.store(id, data, true);
	}

	private void store(final Integer id, final byte[] data, final boolean isUpdate) {
		try {
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

				if (!isUpdate) {
					if ((file != null) && (file.exists())) {
						file.delete();
					}
				}
				throw e;
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private String parseId(final Integer id) {
		return this.digester.digestAsString(id.toString());
	}
}
