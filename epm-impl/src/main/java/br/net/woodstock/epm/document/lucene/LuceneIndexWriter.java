package br.net.woodstock.epm.document.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import br.net.woodstock.epm.util.EPMLog;
import br.net.woodstock.rockframework.config.CoreLog;
import br.net.woodstock.rockframework.domain.service.ServiceException;
import br.net.woodstock.rockframework.text.impl.RandomGenerator;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.ConditionUtils;
import br.net.woodstock.rockframework.utils.SystemUtils;

/**
 * @deprecated IndexWriter is thread safe
 * @author lourival
 *
 */
@Deprecated
public class LuceneIndexWriter implements Runnable {

	private static final long	SLEEP_TIME				= 5000;

	private static final String	DIR_NAME				= "lucene-tmp";

	private static final String	ADD_FILE_EXTENSION		= "add";

	private static final String	DEL_FILE_EXTENSION		= "del";

	private static final String	UPDATE_FILE_EXTENSION	= "up";

	private static final String	FILE_SEPARATOR			= ".";

	private Version				version;

	private Analyzer			analyzer;

	private Directory			directory;

	private File				tmpDir;

	private RandomGenerator		generator				= new RandomGenerator(32);

	private boolean				stop;

	public LuceneIndexWriter(final Version version, final Analyzer analyzer, final Directory directory) {
		super();
		this.version = version;
		this.analyzer = analyzer;
		this.directory = directory;

		File parent = new File(SystemUtils.getProperty(SystemUtils.JAVA_IO_TMPDIR_PROPERTY));

		this.tmpDir = new File(parent, LuceneIndexWriter.DIR_NAME);

		if (!this.tmpDir.exists()) {
			this.tmpDir.mkdirs();
		}

		this.stop = false;
	}

	@Override
	public void run() {
		while (!this.stop) {
			IndexWriterConfig writerConfig = null;
			IndexWriter writer = null;
			try {
				writerConfig = new IndexWriterConfig(this.version, this.analyzer);
				writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND); // Create

				writer = new IndexWriter(this.directory, new IndexWriterConfig(this.version, new StandardAnalyzer(this.version)));

				File[] files = this.tmpDir.listFiles();
				if (ConditionUtils.isNotEmpty(files)) {
					for (File file : files) {
						String fileName = file.getName();
						if (fileName.endsWith(LuceneIndexWriter.ADD_FILE_EXTENSION)) {
							this.addFile(writer, file);
						} else if (fileName.endsWith(LuceneIndexWriter.ADD_FILE_EXTENSION)) {
							this.delFile(writer, file);
						} else if (fileName.endsWith(LuceneIndexWriter.ADD_FILE_EXTENSION)) {
							this.updateFile(writer, file);
						}
					}
				}
			} catch (IOException e) {
				throw new ServiceException(e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						throw new ServiceException(e);
					}
				}
			}
			try {
				Thread.sleep(LuceneIndexWriter.SLEEP_TIME);
			} catch (InterruptedException e) {
				CoreLog.getInstance().getLogger().info(e.getMessage(), e);
			}
		}
	}

	private void addFile(final IndexWriter writer, final File file) {
		EPMLog.getLogger().info("Saving: " + file);
		try {
			FileInputStream inputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

			Document document = (Document) objectInputStream.readObject();

			objectInputStream.close();
			inputStream.close();

			writer.addDocument(document);
			file.delete();
		} catch (IOException e) {
			CoreLog.getInstance().getLogger().info(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			CoreLog.getInstance().getLogger().info(e.getMessage(), e);
		}
	}

	private void delFile(final IndexWriter writer, final File file) {
		EPMLog.getLogger().info("Deleting: " + file);
		try {
			FileInputStream inputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

			Query query = (Query) objectInputStream.readObject();

			objectInputStream.close();
			inputStream.close();

			writer.deleteDocuments(query);
			file.delete();
		} catch (IOException e) {
			CoreLog.getInstance().getLogger().info(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			CoreLog.getInstance().getLogger().info(e.getMessage(), e);
		}
	}

	private void updateFile(final IndexWriter writer, final File file) {
		EPMLog.getLogger().info("Updating: " + file);
		try {
			FileInputStream inputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

			Document document = (Document) objectInputStream.readObject();

			objectInputStream.close();
			inputStream.close();

			writer.addDocument(document);
			file.delete();
		} catch (IOException e) {
			CoreLog.getInstance().getLogger().info(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			CoreLog.getInstance().getLogger().info(e.getMessage(), e);
		}
	}

	public void add(final Document document) throws IOException {
		Assert.notNull(document, "document");

		File file = new File(this.tmpDir, this.getFileName(LuceneIndexWriter.ADD_FILE_EXTENSION));

		file.createNewFile();

		EPMLog.getLogger().info("Adding: " + file);

		FileOutputStream outputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(document);
		objectOutputStream.close();
		outputStream.close();
	}

	public void delete(final Query query) throws IOException {
		Assert.notNull(query, "query");

		File file = new File(this.tmpDir, this.getFileName(LuceneIndexWriter.DEL_FILE_EXTENSION));

		file.createNewFile();

		EPMLog.getLogger().info("Adding: " + file);

		FileOutputStream outputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(query);
		objectOutputStream.close();
		outputStream.close();
	}

	public void update(final Document document) throws IOException {
		Assert.notNull(document, "document");

		File file = new File(this.tmpDir, this.getFileName(LuceneIndexWriter.UPDATE_FILE_EXTENSION));

		file.createNewFile();

		EPMLog.getLogger().info("Adding: " + file);

		FileOutputStream outputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(document);
		objectOutputStream.close();
		outputStream.close();
	}

	public void stop() {
		this.stop = true;
	}

	private String getFileName(final String extension) {
		StringBuilder builder = new StringBuilder();
		builder.append(System.currentTimeMillis());
		builder.append(this.generator.generate());
		builder.append(LuceneIndexWriter.FILE_SEPARATOR);
		builder.append(extension);
		return builder.toString();
	}
}
