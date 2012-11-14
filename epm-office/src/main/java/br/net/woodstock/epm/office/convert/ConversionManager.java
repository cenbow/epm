package br.net.woodstock.epm.office.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.OfficeException;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class ConversionManager implements Serializable {

	private static final long				serialVersionUID	= -6582001460916597892L;

	private DefaultDocumentFormatRegistry	registry;

	private int								port;

	public ConversionManager(final int port) {
		super();
		this.port = port;
		this.registry = new DefaultDocumentFormatRegistry();

		// Excel
		DocumentFormat format = null;
		
		//format = new DocumentFormat("Microsoft Excel 2003 XML", DocumentFamily.TEXT, OfficeDocumentType.DOCX.getMimeType(), OfficeDocumentType.DOCX.getExtension());
		//format.setExportFilter(DocumentFamily.SPREADSHEET, "MS Excel 2003 XML");
		//this.registry.addDocumentFormat(format);

		format = new DocumentFormat("Microsoft Excel 2007 XML", DocumentFamily.SPREADSHEET, OfficeDocumentType.XLSX.getMimeType(), OfficeDocumentType.XLSX.getExtension());
		format.setExportFilter(DocumentFamily.SPREADSHEET, "Calc MS Excel 2007 XML");
		this.registry.addDocumentFormat(format);

		// Power Point
		format = new DocumentFormat("Microsoft PowerPoint 2007 XML", DocumentFamily.PRESENTATION, OfficeDocumentType.PPTX.getMimeType(), OfficeDocumentType.PPTX.getExtension());
		format.setExportFilter(DocumentFamily.PRESENTATION, "Impress MS PowerPoint 2007 XML");
		this.registry.addDocumentFormat(format);

		// Word
		//format = new DocumentFormat("Microsoft Word 2003 XML", DocumentFamily.TEXT, OfficeDocumentType.DOCX.getMimeType(), OfficeDocumentType.DOCX.getExtension());
		//format.setExportFilter(DocumentFamily.TEXT, "MS Word 2003 XML");
		//this.registry.addDocumentFormat(format);

		format = new DocumentFormat("Microsoft Word 2007 XML", DocumentFamily.TEXT, OfficeDocumentType.DOCX.getMimeType(), OfficeDocumentType.DOCX.getExtension());
		format.setExportFilter(DocumentFamily.TEXT, "MS Word 2007 XML");
		this.registry.addDocumentFormat(format);
	}

	public byte[] convert(final byte[] from, final OfficeDocumentType fromType, final OfficeDocumentType toType) {
		OpenOfficeConnection connection = null;
		try {
			connection = new SocketOpenOfficeConnection(this.port);

			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);

			InputStream sourceInputStream = new ByteArrayInputStream(from);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			converter.convert(sourceInputStream, this.getDocumentFormat(fromType), outputStream, this.getDocumentFormat(toType));

			return outputStream.toByteArray();
		} catch (Exception e) {
			throw new OfficeException(e);
		} finally {
			if ((connection != null) && (connection.isConnected())) {
				connection.disconnect();
			}
		}
	}

	private DocumentFormat getDocumentFormat(final OfficeDocumentType documentType) {
		if (documentType == null) {
			return null;
		}
		// MS Word 2007 XML
		//
		DocumentFormat format = this.registry.getFormatByFileExtension(documentType.getExtension());
		if (format == null) {
			format = new DocumentFormat(documentType.name(), documentType.getMimeType(), documentType.getExtension());
		}
		return format;
	}
}
