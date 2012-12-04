package br.net.woodstock.epm.office.oo.impl;

abstract class OpenOfficeFilters {

	// PDF
	public static final String	PRESENTATION_TO_PDF			= "impress_pdf_Export";

	public static final String	PRESENTATION_TO_XHTML		= "XHTML Impress File";

	public static final String	SPREADSHEET_TO_PDF			= "calc_pdf_Export";

	public static final String	SPREADSHEET_TO_XHTML		= "XHTML Calc File";

	public static final String	TEXT_DOCUMENT_TO_PDF		= "writer_pdf_Export";

	public static final String	TEXT_DOCUMENT_TO_XHTML		= "XHTML Writer File";

	// Presentation
	public static final String	ODP							= "impress8";

	public static final String	ODP_TEMPLATE				= "impress8_template";

	public static final String	POWERPOINT_97				= "MS PowerPoint 97";

	public static final String	POWERPOINT_2007				= "Impress MS PowerPoint 2007 XML";

	public static final String	POWERPOINT_2007_TEMPLATE	= "Impress MS PowerPoint 2007 XML Template";

	public static final String	SXI							= "StarOffice XML (Impress)";

	public static final String	SXI_TEMPLATE				= "impress_StarOffice_XML_Impress_Template";

	// Spreadsheet
	public static final String	CSV							= "Text - txt - csv (StarCalc)";

	public static final String	EXCEL_97					= "MS Excel 97";

	public static final String	EXCEL_2003					= "MS Excel 2003 XML";

	public static final String	EXCEL_2007					= "Calc MS Excel 2007 XML";

	public static final String	EXCEL_2007_TEMPLATE			= "MS Word 2007 XML Template";

	public static final String	ODS							= "calc8";

	public static final String	ODS_TEMPLATE				= "calc8_template";

	public static final String	SXC							= "StarOffice XML (Calc)";

	public static final String	SXC_TEMPLATE				= "calc_StarOffice_XML_Calc_Template";

	// Text
	public static final String	ODT							= "writer8";

	public static final String	ODT_TEMPLATE				= "writer8_template";

	public static final String	RTF							= "Rich Text Format";

	public static final String	SXW							= "StarOffice XML (Writer)";

	public static final String	SXW_TEMPLATE				= "writer_StarOffice_XML_Writer_Template";

	public static final String	TEXT						= "Text";

	public static final String	WORD_97						= "MS Word 97";

	public static final String	WORD_2003					= "MS Word 2003 XML";

	public static final String	WORD_2007					= "MS Word 2007 XML";

	public static final String	WORD_2007_TEMPLATE			= "MS Word 2007 XML Template";

	private OpenOfficeFilters() {
		super();
	}

}
