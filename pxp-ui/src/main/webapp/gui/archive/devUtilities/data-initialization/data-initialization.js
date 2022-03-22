const axios = require('axios');

console.log('\x1b[34m','Initializing Default Data.........');

axios.post('http://localhost:8092/REST/datainitialization', {
  headers: {
    'Content-Type': 'application/json'
  },
  body: {}
}).then(response => {
  console.log('\x1b[32m','Data Initialisation Operation Completed Successfully...');
  //console.log(response);
}).catch(error => {
  console.log('\x1b[31m',' Error Occurred In While Performing Data Initialisation Operation...');
  console.log(error);
});