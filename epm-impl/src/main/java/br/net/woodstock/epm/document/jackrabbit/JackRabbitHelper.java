package br.net.woodstock.epm.document.jackrabbit;

import java.util.Calendar;
import java.util.Date;

import br.net.woodstock.rockframework.security.digest.DigestType;
import br.net.woodstock.rockframework.security.digest.impl.AsStringDigester;
import br.net.woodstock.rockframework.security.digest.impl.BasicDigester;
import br.net.woodstock.rockframework.security.digest.impl.HexDigester;

abstract class JackRabbitHelper {

	public static final String				CONTEXT_FACTORY			= "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory";

	public static final String				CONTEXT_PROVIDER_URL	= "localhost";

	public static final String				REPOSITORY_SETTINGS		= "repository.xml";

	// EPM
	public static final String				NODE_NAME_EPM_CREATED	= "epm:created";

	public static final String				NODE_NAME_EPM_ID		= "epm:id";

	public static final String				NODE_NAME_EPM_MODIFIED	= "epm:modified";

	public static final String				NODE_NAME_EPM_MIME_TYPE	= "epm:mimeType";

	public static final String				NODE_NAME_EPM_NAME		= "epm:name";

	public static final String				NODE_NAME_EPM_OWNER		= "epm:owner";

	public static final String				NODE_NAME_EPM_TEXT		= "epm:text";

	// DEFAULT
	public static final String				NODE_NAME_CONTENT		= "jcr:content";

	public static final String				NODE_NAME_DATA			= "jcr:data";

	public static final String				NODE_NAME_LAST_MODIFIED	= "jcr:lastModified";

	public static final String				NODE_NAME_MIME_TYPE		= "jcr:mimeType";

	public static final String				NODE_NAME_UUID			= "jcr:uuid";

	public static final String				NOTE_TYPE_FILE			= "nt:file";

	public static final String				NOTE_TYPE_FOLDER		= "nt:folder";

	public static final String				NOTE_TYPE_RESOURCE		= "nt:resource";

	public static final String				NOTE_TYPE_UNSTRUCTURED	= "nt:unstructured";

	public static final AsStringDigester	DIGESTER				= new AsStringDigester(new HexDigester(new BasicDigester(DigestType.SHA1)));

	public JackRabbitHelper() {
		super();
	}

	public static String parseId(final String id) {
		return JackRabbitHelper.DIGESTER.digestAsString(id);
	}

	public static Calendar toCalendar(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

}
