<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.security.cert.X509Certificate"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.net.woodstock.rockframework.core.utils.Codecs"%>
<%@page import="br.net.woodstock.rockframework.security.sign.Signatory"%>
<%@page import="br.net.woodstock.rockframework.security.sign.Signature"%>
<%@page import="br.net.woodstock.rockframework.security.sign.Signer"%>
<%@page import="br.net.woodstock.rockframework.security.sign.impl.CMSSigner"%>
<%
	String data = request.getParameter("data");
	String output = request.getParameter("output");
	String submit = request.getParameter("submit");
	Signature[] signatures = null;
	if("1".equals(submit)) {
		Signer signer = new CMSSigner(null);

		byte[] bytes = Codecs.fromBase64(output.getBytes());
		
		signatures = signer.getSignatures(bytes);
	}
%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>Crypto Test</title>
		<meta http-equiv="content-type" content="text/html; charset=iso-8859-1">
		<style type="text/css">
			label {
				vertical-align: top;
			}
		</style>
		<script type="text/javascript" src="../resources/javascript/rockframework.crypt.js"></script>
		<script type="text/javascript">
			function doCrypt() {
				try {
					var data = document.getElementById("data").value;
					var result = Rockframework.Crypt.signText(data);
					if(result.status) {
						document.getElementById("output").value = result.data;
						return true;
					}
					else {
						alert(result.message);
						return false;
					}
				}
				catch(ex) {
					alert("Error: " + ex.message);
					return false;
				}
			}
			//document.body.onload = showCrypto;
		</script>
	</head>
	<body onload="showCrypto()">
		<form id="form" name="form" action="crypto.jsp" onsubmit="return doCrypt();" method="post">
			<input type="hidden" id="submit" name="submit" value="1" />
			<div>
				<label for="data">Data</label>
				<textarea id="data" name="data" cols="80" rows="8"><%= data != null ? data : "" %></textarea>
			</div>
			<div>
				<label for="output">Output</label>
				<textarea id="output" name="output" cols="80" rows="8" style="display: none"><%= output != null ? output : "" %></textarea>
				<pre><%= output != null ? output : "" %></pre>
			</div>
			<div>
				<input type="submit" id="btnSubmit" name="btnSubmit" value="Submit" />
			</div>
			<% if (signatures != null) { %>
				<% for (int i = 0; i < signatures.length; i++) { %>
					<% Signature signature = signatures[i]; %>
					<% Iterator iterator =  signature.getSignatories().iterator(); %>
					<div>Valid?: <%= signature.getValid() %></div>
					<% while(iterator.hasNext()) { %>
						<% Signatory signatory  = (Signatory) iterator.next(); %>
						<div>Subject: <%= signatory.getSubject() %></div>
						<div>Signatory: <%= signatory.getIssuer() %></div>
						<div>Certificate: <%= ((X509Certificate) signatory.getCertificate()).getSubjectDN() %></div>
					<% } %>
				<% } %>
			<% } %>
		</form>
	</body>
</html>
