package br.net.woodstock.epm.web.configuration.process;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.Process;
import br.net.woodstock.epm.process.api.ProcessService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.rockframework.core.utils.IO;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.web.faces.DataRepository;
import br.net.woodstock.rockframework.web.faces.primefaces.PrimeFacesDataModel;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ProcessAction extends AbstractAction {

	private static final long	serialVersionUID	= -3462021569529103583L;

	@Autowired(required = true)
	private ProcessService		processService;

	public ProcessAction() {
		super();
	}

	public boolean edit(final Process process, final ProcessForm form) {
		if (process != null) {
			Process p = this.processService.getProcessById(process.getId());
			form.setActive(p.getActive());
			form.setDescription(p.getDescription());
			form.setId(p.getId());
			form.setName(p.getName());
			form.setType(p.getType());
			return true;
		}
		return false;
	}

	public boolean view(final Process process, final ProcessForm form) {
		if (process != null) {
			Process p = this.processService.getProcessById(process.getId());
			form.setActive(p.getActive());
			form.setDescription(p.getDescription());
			form.setId(p.getId());
			form.setName(p.getName());
			form.setProcessDefinition(p.getProcessDefinition());
			form.setType(p.getType());
			return true;
		}
		return false;
	}

	public void save(final ProcessForm form) throws IOException {
		Process process = new Process();
		process.setActive(form.getActive());
		process.setDescription(form.getDescription());
		process.setId(form.getId());
		process.setName(form.getName());
		process.setType(form.getType());

		process.setBin(IO.toByteArray(form.getFile()));

		if (process.getId() != null) {
			// this.businessProcessService.update(businessProcess);
		} else {
			this.processService.save(process);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public PrimeFacesDataModel<Process> search(final ProcessSearch search) {
		DataRepository repository = new DataRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public ORMResult getResult(final Page page) {
				return ProcessAction.this.getBusinessProcessService().listBusinessProcessByName(search.getName(), page);
			}

			@Override
			public Object getObject(final Object id) {
				return ProcessAction.this.getBusinessProcessService().getProcessById((Integer) id);
			}

		};
		PrimeFacesDataModel<Process> dataModel = new PrimeFacesDataModel<Process>(WebConstants.PAGE_SIZE, repository);

		return dataModel;
	}

	public PrimeFacesDataModel<Process> getTasks(final Integer processId) {
		DataRepository repository = new DataRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public ORMResult getResult(final Page page) {
				return ProcessAction.this.getBusinessProcessService().listTaskByProcessId(processId, page);
			}

			@Override
			public Object getObject(final Object id) {
				return ProcessAction.this.getBusinessProcessService().getProcessById((Integer) id);
			}

		};
		PrimeFacesDataModel<Process> dataModel = new PrimeFacesDataModel<Process>(WebConstants.PAGE_SIZE, repository);

		return dataModel;
	}

	protected ProcessService getBusinessProcessService() {
		return this.processService;
	}

}
