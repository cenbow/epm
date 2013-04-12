package br.net.woodstock.epm.web.configuration.process;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.orm.BusinessProcess;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.process.api.BusinessProcessService;
import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.epm.web.WebConstants;
import br.net.woodstock.rockframework.core.utils.IO;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;
import br.net.woodstock.rockframework.web.faces.EntityRepository;
import br.net.woodstock.rockframework.web.faces.primefaces.EntityDataModel;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BusinessProcessAction extends AbstractAction {

	private static final long		serialVersionUID	= -3462021569529103583L;

	@Autowired(required = true)
	private BusinessProcessService	businessProcessService;

	public BusinessProcessAction() {
		super();
	}

	public boolean edit(final BusinessProcess businessProcess, final BusinessProcessForm form) {
		if (businessProcess != null) {
			form.setActive(businessProcess.getActive());
			form.setDescription(businessProcess.getDescription());
			form.setId(businessProcess.getId());
			form.setName(businessProcess.getName());
			form.setType(businessProcess.getType());
			return true;
		}
		return false;
	}

	public void save(final BusinessProcessForm form) throws IOException {
		BusinessProcess businessProcess = new BusinessProcess();
		businessProcess.setActive(form.getActive());
		businessProcess.setDescription(form.getDescription());
		businessProcess.setId(form.getId());
		businessProcess.setName(form.getName());
		businessProcess.setType(form.getType());

		businessProcess.setBin(IO.toByteArray(form.getFile()));
		
		if (businessProcess.getId() != null) {
			// this.businessProcessService.update(businessProcess);
		} else {
			this.businessProcessService.save(businessProcess);
			form.reset();
		}

		this.addFacesMessage(this.getMessageOK());
	}

	public EntityDataModel<User> search(final BusinessProcessSearch search) {
		EntityRepository repository = new EntityRepository() {

			private static final long	serialVersionUID	= -7098011024917168622L;

			@Override
			public ORMResult getResult(final Page page) {
				return BusinessProcessAction.this.getBusinessProcessService().listBusinessProcessByName(search.getName(), page);
			}

			@Override
			public Object getEntity(final Object id) {
				return BusinessProcessAction.this.getBusinessProcessService().getBusinessProcessById((Integer) id);
			}

		};
		EntityDataModel<User> users = new EntityDataModel<User>(WebConstants.PAGE_SIZE, repository);

		return users;
	}

	protected BusinessProcessService getBusinessProcessService() {
		return this.businessProcessService;
	}

}
