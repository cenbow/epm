package br.net.woodstock.epm.office.opendocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.jopendocument.dom.ContentTypeVersioned;
import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.ODXMLDocument;
import org.jopendocument.dom.template.JavaScriptFileTemplate;

import br.net.woodstock.epm.office.OfficeDocument;
import br.net.woodstock.epm.office.OfficeException;
import br.net.woodstock.rockframework.utils.IOUtils;

public class OpenDocumentClient implements Serializable {

	private static final long	serialVersionUID	= -9115115136340597093L;

	private static final String	TMP_FILE_PREFIX		= "OOTMP_";

	private static final String	TMP_FILE_SEPARATOR	= ".";

	public OpenDocumentClient() {
		super();
	}

	public OfficeDocument openDocument(final byte[] bytes) {
		try {
			// ODPackage pack = new ODPackage(new ByteArrayInputStream(bytes));
			return null;
		} catch (Exception e) {
			throw new OfficeException(e);
		}
	}

	public byte[] populateTemplate(final byte[] bytes, final Map<String, Object> values) {
		try {
			ODPackage pack = new ODPackage(new ByteArrayInputStream(bytes));
			ODXMLDocument content = pack.getContent();
			if (content != null) {
				ContentTypeVersioned contentType = pack.getContentType();
				if (contentType != null) {
					String templateExtension = null;
					String outputExtension = null;
					byte[] output = null;

					if (contentType == ContentTypeVersioned.PRESENTATION_TEMPLATE) {
						templateExtension = contentType.getExtension();
						outputExtension = ContentTypeVersioned.PRESENTATION.getExtension();
					} else if (contentType == ContentTypeVersioned.SPREADSHEET_TEMPLATE) {
						templateExtension = contentType.getExtension();
						outputExtension = ContentTypeVersioned.SPREADSHEET.getExtension();
					} else if ((contentType == ContentTypeVersioned.TEXT) || (contentType == ContentTypeVersioned.TEXT_TEMPLATE)) {
						templateExtension = contentType.getExtension();
						outputExtension = ContentTypeVersioned.TEXT.getExtension();
					}

					if (templateExtension != null) {
						File tmpFileIn = File.createTempFile(OpenDocumentClient.TMP_FILE_PREFIX, OpenDocumentClient.TMP_FILE_SEPARATOR + templateExtension);
						File tmpFileOut = File.createTempFile(OpenDocumentClient.TMP_FILE_PREFIX, OpenDocumentClient.TMP_FILE_SEPARATOR + outputExtension);

						FileOutputStream outputStream = new FileOutputStream(tmpFileIn);
						outputStream.write(bytes);
						outputStream.close();

						JavaScriptFileTemplate template = new JavaScriptFileTemplate(tmpFileIn);

						for (Entry<String, Object> entry : values.entrySet()) {
							String key = entry.getKey();
							Object value = entry.getValue();
							template.setField(key, value);
						}

						template.saveAs(tmpFileOut);

						FileInputStream inputStream = new FileInputStream(tmpFileOut);

						output = IOUtils.toByteArray(inputStream);

						inputStream.close();

						tmpFileIn.delete();
						tmpFileOut.delete();
					}

					return output;
				}

			}
			return null;
		} catch (Exception e) {
			throw new OfficeException(e);
		}
	}

	/*
	 * public OfficeDocument mergeDocuments() { }
	 */

}
