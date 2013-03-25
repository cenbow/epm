package br.net.woodstock.epm.orm;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_business_process")
public class BusinessProcess extends AbstractIntegerEntity {

	private static final long		serialVersionUID	= 6261267274337111940L;

	@Id
	@Column(name = "business_process_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer					id;

	@Column(name = "process_definition_id", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String					processDefinition;

	@Column(name = "business_process_name", length = 30, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 30)
	private String					name;

	@Column(name = "business_process_description", length = 100, nullable = true)
	@Size(min = 1, max = 100)
	private String					description;

	@Column(name = "business_process_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean					active;

	@Column(name = "business_process_bin_type", nullable = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private BusinessProcessBinType	type;

	@Column(name = "business_process_bin", nullable = false)
	@NotNull
	@Lob
	@Basic(optional = false, fetch = FetchType.LAZY)
	private byte[]					bin;

	@ManyToMany(mappedBy = "businessProcess", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<BusinessGroup>		groups;

	public BusinessProcess() {
		super();
	}

	public BusinessProcess(final Integer id) {
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

	public String getProcessDefinition() {
		return this.processDefinition;
	}

	public void setProcessDefinition(final String processDefinition) {
		this.processDefinition = processDefinition;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public BusinessProcessBinType getType() {
		return this.type;
	}

	public void setType(final BusinessProcessBinType type) {
		this.type = type;
	}

	public byte[] getBin() {
		return this.bin;
	}

	public void setBin(final byte[] bin) {
		this.bin = bin;
	}

	public Set<BusinessGroup> getGroups() {
		return this.groups;
	}

	public void setGroups(final Set<BusinessGroup> groups) {
		this.groups = groups;
	}

}
