package br.net.woodstock.epm.search.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import br.net.woodstock.epm.search.api.DocumentMetadata;
import br.net.woodstock.epm.search.api.OrderBy;
import br.net.woodstock.rockframework.domain.service.ServiceException;
import br.net.woodstock.rockframework.persistence.Repository;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.utils.ConditionUtils;

public class LuceneRepository implements Repository {

	private static final long	serialVersionUID	= 3103838379823202153L;

	private Version				version;

	private Analyzer			analyzer;

	private Directory			directory;

	private LuceneIndexWriter	writer;

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
			this.version = Version.LUCENE_35;
			this.analyzer = new StandardAnalyzer(this.version);
			this.directory = FSDirectory.open(path);

			this.writer = new LuceneIndexWriter(this.version, this.analyzer, this.directory);
			Thread thread = new Thread(this.writer);
			thread.start();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public DocumentMetadata get(final String id) throws IOException {
		TermQuery query = new TermQuery(new Term(LuceneField.ID.getName(), id));
		Sort sort = new Sort(new SortField(SortField.FIELD_SCORE.getField(), SortField.SCORE));
		DocumentMetadata[] array = this.searchInternal(query, sort, 1);
		if (ConditionUtils.isNotEmpty(array)) {
			return array[0];
		}
		return null;
	}

	public void save(final DocumentMetadata metadata) throws IOException {
		Document document = new Document();
		document.add(new Field(LuceneField.CONTENT_TYPE.getName(), metadata.getContentType(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.DATE.getName(), metadata.getDate(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.GROUP.getName(), metadata.getGroup(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.ID.getName(), metadata.getId(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.NAME.getName(), metadata.getName(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.TEXT.getName(), metadata.getText(), Store.YES, Index.ANALYZED));
		document.add(new Field(LuceneField.USER.getName(), metadata.getUser(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.VERSION.getName(), metadata.getVersion(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));

		this.writer.add(document);
	}

	public DocumentMetadata[] search(final String filter, final OrderBy[] orders, final int maxResult) throws IOException {
		try {
			QueryParser queryParser = new QueryParser(this.version, LuceneField.TEXT.getName(), this.analyzer);
			Query query = queryParser.parse(filter);

			Sort sort = null;
			if (ConditionUtils.isNotEmpty(orders)) {
				SortField[] sortFields = new SortField[orders.length];
				for (int i = 0; i < orders.length; i++) {
					LuceneField ldf = LuceneField.fromField(orders[i].getField());
					sortFields[i] = new SortField(ldf.getName(), SortField.STRING, orders[i].isReverse());
				}
				sort = new Sort(sortFields);
			} else {
				sort = new Sort(new SortField(SortField.FIELD_SCORE.getField(), SortField.SCORE));
			}

			DocumentMetadata[] array = this.searchInternal(query, sort, maxResult);
			return array;
		} catch (ParseException e) {
			return new DocumentMetadata[0];
		}
	}

	private DocumentMetadata[] searchInternal(final Query query, final Sort sort, final int maxResults) throws IOException {
		IndexReader reader = null;
		IndexSearcher searcher = null;
		try {
			reader = IndexReader.open(this.directory);
			searcher = new IndexSearcher(reader);

			TopDocs docs = searcher.search(query, maxResults, sort);
			ScoreDoc[] scores = docs.scoreDocs;
			DocumentMetadata[] documents = new DocumentMetadata[scores.length];
			for (int index = 0; index < scores.length; index++) {
				int id = scores[index].doc;
				Document document = searcher.doc(id);

				DocumentMetadata metadata = new DocumentMetadata();
				metadata.setContentType(document.getFieldable(LuceneField.CONTENT_TYPE.getName()).stringValue());
				metadata.setDate(document.getFieldable(LuceneField.DATE.getName()).stringValue());
				metadata.setGroup(document.getFieldable(LuceneField.GROUP.getName()).stringValue());
				metadata.setId(document.getFieldable(LuceneField.ID.getName()).stringValue());
				metadata.setName(document.getFieldable(LuceneField.NAME.getName()).stringValue());
				metadata.setText(document.getFieldable(LuceneField.TEXT.getName()).stringValue());
				metadata.setUser(document.getFieldable(LuceneField.USER.getName()).stringValue());
				metadata.setVersion(document.getFieldable(LuceneField.VERSION.getName()).stringValue());

				documents[index] = metadata;
			}
			searcher.close();
			reader.close();
			return documents;
		} finally {
			if (searcher != null) {
				try {
					searcher.close();
				} catch (IOException e) {
					throw new ServiceException(e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new ServiceException(e);
				}
			}
		}
	}

}
