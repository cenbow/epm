package br.net.woodstock.epm.office;

public enum OfficeDocumentType {

	DOC("doc", "application/msword"), /**/
	DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"), /**/
	ODP("odp", "application/vnd.oasis.opendocument.presentation"), /**/
	ODS("ods", "application/vnd.oasis.opendocument.spreadsheet"), /**/
	ODT("odt", "application/vnd.oasis.opendocument.text"), /**/
	PDF("pdf", "application/pdf"), /**/
	PPS("pps", "application/vnd.ms-powerpoint"), /**/
	PPSX("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow"), /**/
	PPT("ppt", "application/vnd.ms-powerpoint"), /**/
	PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"), /**/
	RTF("rtf", "text/rtf"), /**/
	TXT("txt", "text/plain"), /**/
	XLS("xls", "application/vnd.ms-excel"), /**/
	XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

	private String	extension;

	private String	mimeType;

	private OfficeDocumentType(final String extension, final String mimeType) {
		this.extension = extension;
		this.mimeType = mimeType;
	}

	public String getExtension() {
		return this.extension;
	}

	public String getMimeType() {
		return this.mimeType;
	}

}