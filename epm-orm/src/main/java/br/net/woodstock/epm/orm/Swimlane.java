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

import br.net.woodstock.rockframework.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_swimlane")
public class Swimlane extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= 2080523447660931952L;

	@Id
	@Column(name = "swimlane_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "swimlane_name", length = 50, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 50)
	private String				name;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "business_process_id", referencedColumnName = "business_process_id", nullable = false)
	@NotNull
	private BusinessProcess		businessProcess;

	@OneToMany(mappedBy = "swimlane", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<SwimlaneItem>	items;

	public Swimlane() {
		super();
	}

	public Swimlane(final Integer id) {
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

	public BusinessProcess getBusinessProcess() {
		return this.businessProcess;
	}

	public void setBusinessProcess(final BusinessProcess businessProcess) {
		this.businessProcess = businessProcess;
	}

	public Set<SwimlaneItem> getItems() {
		return this.items;
	}

	public void setItems(final Set<SwimlaneItem> items) {
		this.items = items;
	}

}
