package br.net.woodstock.epm.orm;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_candidate_group")
public class CandidateGroup extends AbstractIntegerEntity {

	private static final long		serialVersionUID	= 2080523447660931952L;

	@Id
	@Column(name = "candidate_group_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer					id;

	@Column(name = "candidate_group_name", length = 50, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 50)
	private String					name;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "task_id", referencedColumnName = "task_id", nullable = false)
	@NotNull
	private Task					task;

	@OneToMany(mappedBy = "candidateGroup", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<CandidateGroupItem>	items;

	public CandidateGroup() {
		super();
	}

	public CandidateGroup(final Integer id) {
		super();
		this.id = id;
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

	public Task getTask() {
		return this.task;
	}

	public void setTask(final Task task) {
		this.task = task;
	}

	public Set<CandidateGroupItem> getItems() {
		return this.items;
	}

	public void setItems(final Set<CandidateGroupItem> items) {
		this.items = items;
	}

}
