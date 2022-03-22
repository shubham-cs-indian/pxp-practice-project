function isUsernameValid(sUsername) {
  if(sUsername != "") {
    var regExp = /^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._\u00c4\u00d6\u00dc\u00df\u00e4\u00f6\u00fc]+$/;
    return regExp.test(sUsername);
  } else {
    return false;
  }
}

function isEmailValid(sEmail) {
  if(sEmail != "") {
    var sRegExp = /^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/;
    return sRegExp.test(sEmail);
  } else {
    return false;
  }
}