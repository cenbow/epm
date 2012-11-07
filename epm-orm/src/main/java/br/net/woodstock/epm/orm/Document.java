package br.net.woodstock.epm.orm;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import br.net.woodstock.rockframework.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_document")
@Indexed
public class Document extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= -38926493422850684L;

	@Id
	@Column(name = "document_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "document_name", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String				name;

	@Column(name = "document_text", nullable = true, columnDefinition = "LONGTEXT")
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String				text;

	@Column(name = "document_mime_type", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String				mimeType;

	@Column(name = "document_created", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	private Date				created;

	@Column(name = "document_modified", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	private Date				modified;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	@NotNull
	private User				user;

	public Document() {
		super();
	}

	public Document(final Integer id) {
		super(id);
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(final Date created) {
		this.created = created;
	}

	public Date getModified() {
		return this.modified;
	}

	public void setModified(final Date modified) {
		this.modified = modified;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

}
