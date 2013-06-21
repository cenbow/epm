package br.net.woodstock.epm.web.download;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.net.woodstock.epm.web.AbstractAction;
import br.net.woodstock.rockframework.web.faces.utils.FacesContexts;

public abstract class AbstractDownloadAction extends AbstractAction {

	private static final long	serialVersionUID	= 6949565105414264111L;

	public void download(final String id) throws IOException {
		DownloadFile downloadFile = this.getDownloadFile(id);
		if (downloadFile == null) {
			FacesContext facesContext = FacesContexts.getFacesContext();
			HttpServletResponse httpServletResponse = FacesContexts.getResponse();
			httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			facesContext.responseComplete();
		}
		FacesContexts.download(downloadFile.getContent(), downloadFile.getName(), downloadFile.getContentType());
	}

	protected abstract DownloadFile getDownloadFile(final String id);

}
