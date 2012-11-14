package br.net.woodstock.epm.office.form;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jopendocument.dom.ContentTypeVersioned;
import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.ODXMLDocument;
import org.jopendocument.dom.template.JavaScriptFileTemplate;

import br.net.woodstock.epm.office.OfficeDocument;
import br.net.woodstock.epm.office.OfficeException;
import br.net.woodstock.rockframework.utils.IOUtils;

public class FieldManager implements Serializable {

	private static final long	serialVersionUID			= -9115115136340597093L;

	private static final String	NS_TEXT_10					= "urn:oasis:names:tc:opendocument:xmlns:text:1.0";

	private static final String	TEXT_INPUT_ELEMENT			= "text-input";

	private static final String	TEXT_INPUT_NAME_ATTRIBUTE	= "description";

	private static final String	TMP_FILE_PREFIX				= "OOTMP_";

	private static final String	TMP_FILE_SEPARATOR			= ".";

	public FieldManager() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public Set<String> getFields(final OfficeDocument document) {
		try {
			ODPackage pack = new ODPackage(new ByteArrayInputStream(document.getContent()));
			ODXMLDocument content = pack.getContent();
			Namespace namespace = Namespace.getNamespace(FieldManager.NS_TEXT_10);
			ElementFilter filter = new ElementFilter(FieldManager.TEXT_INPUT_ELEMENT, namespace);
			Set<String> set = new HashSet<String>();
			Iterator i = content.getDocument().getDescendants(filter);
			while (i.hasNext()) {
				Element e = (Element) i.next();
				String name = e.getAttributeValue(FieldManager.TEXT_INPUT_NAME_ATTRIBUTE, namespace);
				set.add(name);
			}
			return set;
		} catch (Exception e) {
			throw new OfficeException(e);
		}
	}

	public byte[] setValues(final byte[] bytes, final Map<String, Object> values) {
		try {
			ODPackage pack = new ODPackage(new ByteArrayInputStream(bytes));
			ODXMLDocument content = pack.getContent();
			if (content != null) {
				ContentTypeVersioned contentType = pack.getContentType();
				if (contentType != null) {
					String templateExtension = null;
					String outputExtension = null;
					byte[] output = null;

					if ((contentType == ContentTypeVersioned.PRESENTATION) || (contentType == ContentTypeVersioned.PRESENTATION_TEMPLATE)) {
						templateExtension = contentType.getExtension();
						outputExtension = ContentTypeVersioned.PRESENTATION.getExtension();
					} else if ((contentType == ContentTypeVersioned.SPREADSHEET) || (contentType == ContentTypeVersioned.SPREADSHEET_TEMPLATE)) {
						templateExtension = contentType.getExtension();
						outputExtension = ContentTypeVersioned.SPREADSHEET.getExtension();
					} else if ((contentType == ContentTypeVersioned.TEXT) || (contentType == ContentTypeVersioned.TEXT_TEMPLATE)) {
						templateExtension = contentType.getExtension();
						outputExtension = ContentTypeVersioned.TEXT.getExtension();
					}

					if (templateExtension != null) {
						File tmpFileIn = File.createTempFile(FieldManager.TMP_FILE_PREFIX, FieldManager.TMP_FILE_SEPARATOR + templateExtension);
						File tmpFileOut = File.createTempFile(FieldManager.TMP_FILE_PREFIX, FieldManager.TMP_FILE_SEPARATOR + outputExtension);

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

}
