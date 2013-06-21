package br.net.woodstock.epm.web.configuration.process;

import java.io.File;
import java.io.IOException;

import org.primefaces.event.FileUploadEvent;

import br.net.woodstock.epm.orm.DeploymentType;
import br.net.woodstock.epm.web.AbstractForm;
import br.net.woodstock.rockframework.core.utils.Files;
import br.net.woodstock.rockframework.core.utils.IO;

public class ProcessForm extends AbstractForm {

	private static final long	serialVersionUID	= 6243763629739870386L;

	private Integer				id;

	private String				name;

	private String				description;

	private Boolean				active;

	private DeploymentType		type;

	private File				file;

	private String				fileName;

	private String				processDefinition;

	public ProcessForm() {
		super();
		this.type = DeploymentType.XML;
	}

	@Override
	public void reset() {
		this.setActive(null);
		this.setDescription(null);
		this.setFile(null);
		this.setFileName(null);
		this.setId(null);
		this.setName(null);
		this.setProcessDefinition(null);
		// this.setType(null);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
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

	public DeploymentType getType() {
		return this.type;
	}

	public void setType(final DeploymentType type) {
		this.type = type;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(final File file) {
		this.file = file;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public String getProcessDefinition() {
		return this.processDefinition;
	}

	public void setProcessDefinition(final String processDefinition) {
		this.processDefinition = processDefinition;
	}

	// Aux
	public void onFileUpload(final FileUploadEvent e) throws IOException {
		this.fileName = e.getFile().getFileName();
		String prefix = this.fileName;
		String suffix = Files.getExtension(this.fileName);
		this.file = File.createTempFile(prefix, suffix);
		IO.copy(e.getFile().getInputstream(), this.file);
	}

}
