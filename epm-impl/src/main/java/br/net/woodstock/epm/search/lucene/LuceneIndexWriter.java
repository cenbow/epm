package br.net.woodstock.epm.search.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
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

import br.net.woodstock.epm.api.Log;
import br.net.woodstock.rockframework.config.CoreLog;
import br.net.woodstock.rockframework.domain.service.ServiceException;
import br.net.woodstock.rockframework.io.FileExtensionFilter;
import br.net.woodstock.rockframework.text.impl.RandomGenerator;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.ConditionUtils;
import br.net.woodstock.rockframework.utils.SystemUtils;

public class LuceneIndexWriter implements Runnable {

	private static final long	SLEEP_TIME			= 5000;

	private static final String	DIR_NAME			= "lucene-tmp";

	private static final String	ADD_FILE_EXTENSION	= "add";

	private static final String	DEL_FILE_EXTENSION	= "del";

	private static final String	FILE_SEPARATOR		= ".";

	private Version				version;

	private Analyzer			analyzer;

	private Directory			directory;

	private File				tmpDir;

	private FilenameFilter		addFilter;

	private FilenameFilter		delFilter;

	private RandomGenerator		generator			= new RandomGenerator(32);

	private boolean				stop;

	public LuceneIndexWriter(final Version version, final Analyzer analyzer, final Directory directory) {
		super();
		this.version = version;
		this.analyzer = analyzer;
		this.directory = directory;

		File parent = new File(SystemUtils.getProperty(SystemUtils.JAVA_IO_TMPDIR_PROPERTY));

		this.tmpDir = new File(parent, LuceneIndexWriter.DIR_NAME);

		this.addFilter = new FileExtensionFilter(LuceneIndexWriter.ADD_FILE_EXTENSION);
		this.delFilter = new FileExtensionFilter(LuceneIndexWriter.DEL_FILE_EXTENSION);

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

				File[] addFiles = this.tmpDir.listFiles(this.addFilter);
				if (ConditionUtils.isNotEmpty(addFiles)) {
					this.addFiles(writer, addFiles);
				}

				File[] delFiles = this.tmpDir.listFiles(this.delFilter);
				if (ConditionUtils.isNotEmpty(delFiles)) {
					this.delFiles(writer, delFiles);
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

	private void addFiles(final IndexWriter writer, final File[] files) {
		for (File file : files) {
			Log.getLogger().info("Saving: " + file);
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
	}

	private void delFiles(final IndexWriter writer, final File[] files) {
		for (File file : files) {
			Log.getLogger().info("Deleting: " + file);
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
	}

	public void add(final Document document) throws IOException {
		Assert.notNull(document, "document");

		File file = new File(this.tmpDir, this.generator.generate() + LuceneIndexWriter.FILE_SEPARATOR + LuceneIndexWriter.ADD_FILE_EXTENSION);

		file.createNewFile();

		Log.getLogger().info("Adding: " + file);

		FileOutputStream outputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(document);
		objectOutputStream.close();
		outputStream.close();
	}

	public void delete(final Query query) throws IOException {
		Assert.notNull(query, "query");

		File file = new File(this.tmpDir, this.generator.generate() + LuceneIndexWriter.FILE_SEPARATOR + LuceneIndexWriter.DEL_FILE_EXTENSION);

		file.createNewFile();

		Log.getLogger().info("Adding: " + file);

		FileOutputStream outputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(query);
		objectOutputStream.close();
		outputStream.close();
	}

	public void stop() {
		this.stop = true;
	}
}
