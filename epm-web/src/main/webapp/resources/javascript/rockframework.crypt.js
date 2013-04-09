if((Rockframework == undefined) || (Rockframework == null)) {
	var Rockframework = {};
}

if((Rockframework.Crypt == undefined) || (Rockframework.Crypt == null)) {
	Rockframework.Crypt = {};
	Rockframework.Crypt.ERROR_INTERNAL_ERROR = "error:internalError";
	Rockframework.Crypt.ERROR_NO_MATCHING_CERT = "error:noMatchingCert";
	Rockframework.Crypt.ERROR_USER_CANCEL = "error:userCancel";
	
	Rockframework.Crypt.signText = function(value) {
		var status = null;
		var data = null;
		var message = null;
		try {
			var c = window.crypto;
			if((c != undefined) && (c != null)) {
				if(c.signText != undefined) {
					var encrypted = c.signText(value, "ask");
					if(encrypted == Rockframework.Crypt.CRYPT_ERROR_NO_MATCHING_CERT) {
						status = false;
						message = "Certificate not found";
					}
					else if(encrypted == Rockframework.Crypt.CRYPT_ERROR_INTERNAL_ERROR) {
						status = false;
						message = "Error sign: " + encrypted;
					}
					else {
						status = true;
						message = "OK";
						data = encrypted;
					}
				}
				else {
					status = false;
					message = "'window.crypt.signText()' not found";
				}
			}
			else {
				status = false;
				message = "'window.crypt' not found";
			}
		}
		catch(ex) {
			alert("Error: " + ex.message);
		}
		var result = {"status": status, "data": data, "message": message};
		return result;
	};
}