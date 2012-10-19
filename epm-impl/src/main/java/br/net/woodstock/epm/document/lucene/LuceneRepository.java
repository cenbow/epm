package br.net.woodstock.epm.document.lucene;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import br.net.woodstock.epm.acl.api.User;
import br.net.woodstock.epm.document.api.Document;
import br.net.woodstock.epm.document.api.DocumentResultContainer;
import br.net.woodstock.epm.document.util.DocumentContent;
import br.net.woodstock.epm.util.EPMLog;
import br.net.woodstock.epm.util.Page;
import br.net.woodstock.rockframework.domain.service.ServiceException;
import br.net.woodstock.rockframework.persistence.Repository;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.ConditionUtils;

class LuceneRepository implements Repository {

	private static final long	serialVersionUID	= 3103838379823202153L;

	private static final int	DEFAULT_PAGE_SIZE	= 10;

	private static final int	DEFAULT_PAGE_NUMBER	= 0;

	private static final String	DAY_PATTERN			= "dd";

	private static final String	MONTH_PATTERN		= "MM";

	private static final String	YEAR_PATTERN		= "yyyy";

	private static final String	LATIN_PATTERN		= "dd/MM/yyyy";

	private static final String	US_PATTERN			= "yyyy-MM-dd";

	private Version				version;

	private Analyzer			analyzer;

	private Directory			directory;

	private IndexReader			reader;

	private IndexWriter			writer;

	// private LuceneIndexWriter writer;

	public LuceneRepository(final String path) {
		super();
		Assert.notEmpty(path, "path");
		this.init(new File(path));
	}

	public LuceneRepository(final File path) {
		super();
		Assert.notNull(path, "path");
		this.init(path);
	}

	private void init(final File path) {
		try {
			this.version = Version.LUCENE_36;
			this.analyzer = new StandardAnalyzer(this.version);
			this.directory = FSDirectory.open(path);

			this.reader = IndexReader.open(this.directory);

			IndexWriterConfig writerConfig = new IndexWriterConfig(this.version, this.analyzer);
			writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND); // Create

			this.writer = new IndexWriter(this.directory, new IndexWriterConfig(this.version, new StandardAnalyzer(this.version)));
		} catch (Exception e) {
			EPMLog.getLogger().error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.close();
	}

	public void close() throws IOException {
		this.reader.clone();
		this.writer.close();
	}

	public Document get(final String id) throws IOException {
		TermQuery query = new TermQuery(new Term(LuceneField.ID.getName(), id));
		DocumentResultContainer container = this.searchInternal(query, new Page(1, 1));
		Document[] documents = container.getItems();
		if (ConditionUtils.isNotEmpty(documents)) {
			return documents[0];
		}
		return null;
	}

	public boolean remove(final String id) throws IOException {
		TermQuery query = new TermQuery(new Term(LuceneField.ID.getName(), id));
		this.writer.deleteDocuments(query);
		this.writer.commit();
		return true;
	}

	public void save(final Document document) throws IOException, SAXException, TikaException {
		org.apache.lucene.document.Document d = this.toDocument(document);
		this.writer.addDocument(d);
		this.writer.commit();
	}

	public boolean update(final Document document) throws IOException, SAXException, TikaException {
		Term term = new Term(LuceneField.ID.getName(), document.getId());
		org.apache.lucene.document.Document d = this.toDocument(document);
		this.writer.updateDocument(term, d);
		this.writer.commit();
		return true;
	}

	public DocumentResultContainer search(final String filter, final Page page) throws IOException {
		try {
			QueryParser queryParser = new QueryParser(this.version, LuceneField.TEXT.getName(), this.analyzer);
			Query query = queryParser.parse(filter);

			return this.searchInternal(query, page);
		} catch (ParseException e) {
			EPMLog.getLogger().info(e.getMessage(), e);
			Page p = new Page();
			p.setPageNumber(1);
			if (page != null) {
				p.setPageSize(page.getPageSize());
			} else {
				p.setPageSize(LuceneRepository.DEFAULT_PAGE_SIZE);
			}
			DocumentResultContainer container = new DocumentResultContainer(0, new Document[0], p);
			return container;
		}
	}

	private DocumentResultContainer searchInternal(final Query query, final Page page) throws IOException {
		IndexSearcher searcher = null;
		try {
			searcher = new IndexSearcher(this.reader);

			int pageSize = LuceneRepository.DEFAULT_PAGE_SIZE;
			int pageNumber = LuceneRepository.DEFAULT_PAGE_NUMBER;

			if (page != null) {
				pageNumber = page.getPageNumber();
				pageSize = page.getPageSize();
			}

			TopDocs docs = searcher.search(query, Integer.MAX_VALUE);
			ScoreDoc[] scores = docs.scoreDocs;
			int total = scores.length;

			if (total > pageSize) {
				int index = (pageNumber - 1) * pageSize;
				ScoreDoc scoreDoc = scores[index];
				docs = searcher.searchAfter(scoreDoc, query, pageSize);
				scores = docs.scoreDocs;
			}

			Document[] documents = new Document[scores.length];
			for (int index = 0; index < scores.length; index++) {
				int id = scores[index].doc;
				org.apache.lucene.document.Document document = searcher.doc(id);

				Document d = new Document();
				d.setCreated(new Date(this.getLongValue(document, LuceneField.CREATED.getName())));
				d.setId(this.getStringValue(document, LuceneField.ID.getName()));
				d.setMimeType(this.getStringValue(document, LuceneField.MIME_TYPE.getName()));
				d.setModified(new Date(this.getLongValue(document, LuceneField.MODIFIED.getName())));
				d.setName(this.getStringValue(document, LuceneField.NAME.getName()));
				d.setOwner(this.getUser(this.getStringValue(document, LuceneField.OWNER.getName())));
				d.setText(this.getStringValue(document, LuceneField.TEXT.getName()));

				documents[index] = d;
			}
			searcher.close();

			DocumentResultContainer container = new DocumentResultContainer(total, documents, page);
			return container;
		} finally {
			if (searcher != null) {
				try {
					searcher.close();
				} catch (IOException e) {
					throw new ServiceException(e);
				}
			}
		}
	}

	private org.apache.lucene.document.Document toDocument(final Document document) throws IOException, SAXException, TikaException {
		org.apache.lucene.document.Document d = new org.apache.lucene.document.Document();
		DateFormat dayDateFormat = new SimpleDateFormat(LuceneRepository.DAY_PATTERN);
		DateFormat monthDateFormat = new SimpleDateFormat(LuceneRepository.MONTH_PATTERN);
		DateFormat yearDateFormat = new SimpleDateFormat(LuceneRepository.YEAR_PATTERN);
		DateFormat latinDateFormat = new SimpleDateFormat(LuceneRepository.LATIN_PATTERN);
		DateFormat usDateFormat = new SimpleDateFormat(LuceneRepository.US_PATTERN);

		Date created = document.getCreated();
		Date modified = document.getModified();
		String mimeType = document.getMimeType();
		String text = document.getText();

		if (created == null) {
			created = new Date();
		}
		if (modified == null) {
			modified = created;
		}
		if ((ConditionUtils.isEmpty(mimeType)) || (ConditionUtils.isEmpty(text))) {
			DocumentContent content = DocumentContent.getInstance(document.getBinary());
			if (ConditionUtils.isEmpty(mimeType)) {
				mimeType = content.getMimeType();
			}
			if (ConditionUtils.isEmpty(text)) {
				text = content.getText();
			}
		}

		d.add(new Field(LuceneField.CREATED.getName(), Long.toString(created.getTime()), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.CREATED_DAY.getName(), dayDateFormat.format(created), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.CREATED_MONTH.getName(), monthDateFormat.format(created), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.CREATED_YEAR.getName(), yearDateFormat.format(created), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.CREATED_DATE_LATIN.getName(), latinDateFormat.format(created), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.CREATED_DATE_US.getName(), usDateFormat.format(created), Store.YES, Index.ANALYZED_NO_NORMS));

		d.add(new Field(LuceneField.ID.getName(), document.getId(), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.MIME_TYPE.getName(), mimeType, Store.YES, Index.ANALYZED_NO_NORMS));

		d.add(new Field(LuceneField.MODIFIED.getName(), Long.toString(modified.getTime()), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.MODIFIED_DAY.getName(), dayDateFormat.format(modified), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.MODIFIED_MONTH.getName(), monthDateFormat.format(modified), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.MODIFIED_YEAR.getName(), yearDateFormat.format(modified), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.MODIFIED_DATE_LATIN.getName(), latinDateFormat.format(modified), Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field(LuceneField.MODIFIED_DATE_US.getName(), usDateFormat.format(modified), Store.YES, Index.ANALYZED_NO_NORMS));

		d.add(new Field(LuceneField.TEXT.getName(), text, Store.YES, Index.ANALYZED));

		if (ConditionUtils.isNotEmpty(document.getName())) {
			d.add(new Field(LuceneField.NAME.getName(), document.getName(), Store.YES, Index.ANALYZED_NO_NORMS));
		}

		if ((document.getOwner() != null) && (ConditionUtils.isNotEmpty(document.getOwner().getName()))) {
			d.add(new Field(LuceneField.OWNER.getName(), document.getOwner().getId(), Store.YES, Index.ANALYZED_NO_NORMS));
		}

		return d;
	}

	private String getStringValue(final org.apache.lucene.document.Document document, final String name) {
		if (document == null) {
			return null;
		}
		Fieldable fieldable = document.getFieldable(name);
		if (fieldable == null) {
			return null;
		}
		return fieldable.stringValue();
	}

	private long getLongValue(final org.apache.lucene.document.Document document, final String name) {
		String str = this.getStringValue(document, name);
		if (str != null) {
			Long.parseLong(str);
		}
		return 0;
	}

	private User getUser(final String id) {
		if (ConditionUtils.isNotEmpty(id)) {
			return new User(id);
		}
		return null;
	}

}
