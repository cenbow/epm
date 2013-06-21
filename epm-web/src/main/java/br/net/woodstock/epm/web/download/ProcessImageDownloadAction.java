package br.net.woodstock.epm.web.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.woodstock.epm.process.api.ProcessService;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ProcessImageDownloadAction extends AbstractDownloadAction {

	private static final long	serialVersionUID	= 4749811631957666964L;

	private static final String	FILE_NAME			= "process.png";

	private static final String	CONTENT_TYPE		= "image/png";

	@Autowired(required = true)
	private ProcessService		service;

	public ProcessImageDownloadAction() {
		super();
	}

	@Override
	protected DownloadFile getDownloadFile(final String id) {
		byte[] bytes = this.service.getProcessImageById(Integer.valueOf(id));
		DownloadFile downloadFile = new DownloadFile(ProcessImageDownloadAction.FILE_NAME, bytes, ProcessImageDownloadAction.CONTENT_TYPE);
		return downloadFile;
	}

}
