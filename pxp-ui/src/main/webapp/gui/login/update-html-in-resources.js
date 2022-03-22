const fs = require('fs');

console.log("Updating HTML after build");
fs.copyFile('./build/index.html', './../../../resources/login-start.html', (err) => {
  if (err) throw err;
  console.log('HTML for login screen (login-start.html) has been updated in the resources folder');
});
