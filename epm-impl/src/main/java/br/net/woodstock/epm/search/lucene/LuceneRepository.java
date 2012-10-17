package br.net.woodstock.epm.search.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Fieldable;
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

import br.net.woodstock.epm.search.api.Item;
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

	public Item get(final String id) throws IOException {
		TermQuery query = new TermQuery(new Term(LuceneField.ID.getName(), id));
		Sort sort = new Sort(new SortField(SortField.FIELD_SCORE.getField(), SortField.SCORE));
		Item[] array = this.searchInternal(query, sort, 1);
		if (ConditionUtils.isNotEmpty(array)) {
			return array[0];
		}
		return null;
	}

	public void remove(final String id) throws IOException {
		TermQuery query = new TermQuery(new Term(LuceneField.ID.getName(), id));
		this.writer.delete(query);
	}

	public void save(final Item item) throws IOException {
		Document document = new Document();
		document.add(new Field(LuceneField.CONTENT_TYPE.getName(), item.getContentType(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.DATE.getName(), item.getDate(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.DESCRIPTION.getName(), item.getDescription(), Store.YES, Index.ANALYZED));
		document.add(new Field(LuceneField.EXTENSION.getName(), item.getExtension(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.ID.getName(), item.getId(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.NAME.getName(), item.getName(), Store.YES, Index.ANALYZED));
		document.add(new Field(LuceneField.OWNER.getName(), item.getOwner(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
		document.add(new Field(LuceneField.TEXT.getName(), item.getText(), Store.YES, Index.ANALYZED));
		document.add(new Field(LuceneField.TYPE.getName(), item.getType(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));

		this.writer.add(document);
	}

	public Item[] search(final String filter, final OrderBy[] orders, final int maxResult) throws IOException {
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

			Item[] array = this.searchInternal(query, sort, maxResult);
			return array;
		} catch (ParseException e) {
			return new Item[0];
		}
	}

	private Item[] searchInternal(final Query query, final Sort sort, final int maxResults) throws IOException {
		IndexReader reader = null;
		IndexSearcher searcher = null;
		try {
			reader = IndexReader.open(this.directory);
			searcher = new IndexSearcher(reader);

			TopDocs docs = searcher.search(query, maxResults, sort);
			ScoreDoc[] scores = docs.scoreDocs;
			Item[] items = new Item[scores.length];
			for (int index = 0; index < scores.length; index++) {
				int id = scores[index].doc;
				Document document = searcher.doc(id);

				Item item = new Item();
				item.setContentType(this.getStringValue(document, LuceneField.CONTENT_TYPE.getName()));
				item.setDate(this.getStringValue(document, LuceneField.DATE.getName()));
				item.setDescription(this.getStringValue(document, LuceneField.DESCRIPTION.getName()));
				item.setExtension(this.getStringValue(document, LuceneField.EXTENSION.getName()));
				item.setId(this.getStringValue(document, LuceneField.ID.getName()));
				item.setName(this.getStringValue(document, LuceneField.NAME.getName()));
				item.setOwner(this.getStringValue(document, LuceneField.OWNER.getName()));
				item.setText(this.getStringValue(document, LuceneField.TEXT.getName()));
				item.setType(this.getStringValue(document, LuceneField.TYPE.getName()));

				items[index] = item;
			}
			searcher.close();
			reader.close();
			return items;
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

	private String getStringValue(final Document document, final String name) {
		if (document == null) {
			return null;
		}
		Fieldable fieldable = document.getFieldable(name);
		if (fieldable == null) {
			return null;
		}
		return fieldable.stringValue();
	}

}
