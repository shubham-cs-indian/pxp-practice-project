var _ = require('lodash');
var fs = require('graceful-fs');
const aAllFileTypeNames = ['__en_US.json', '__de_DE.json', '__en_UK.json', '__es_ES.json', '__fr_FR.json'];
var sUITranslationFolderPath = '../../../../resources/UITranslations/';

var _oLanguageMapping = {
  '__de_DE.json': "_de",
  '__en_US.json': "",
  '__en_UK.json': "",
  '__es_ES.json': "_es",
  '__fr_FR.json': "_fr"
};


/** README
 * fileType can accept only following strings
 * ['content', 'dashboard', 'home', 'login', 'setting', 'view']
 *
 *
 * actionType can accept only following strings
 * ['add', 'remove']
 * */

let aFileTypes = ['content', 'dashboard', 'home', 'login', 'setting', 'view'];
var oUserInputs = {
  fileType: "content",
  actionType:"add",
  data: [
    {
      key: "PLEASE_ENTER_VALUE_TO_PROCEED",
      value: "Please enter value to proceed"
    }
  ]
};


var startProcess = function () {
  var sFileType = oUserInputs.fileType;

  _.forEach(aAllFileTypeNames, function (sFileNameSuccessor) {
    var sFileName = sUITranslationFolderPath + sFileType + sFileNameSuccessor;

    var oFileData = JSON.parse(fs.readFileSync(sFileName, 'utf8'));

    if(oUserInputs.actionType === "add"){
      /**
       * This block works only for adding any translation key value to the files.
       * */
      delete oFileData['ADD_ABOVE_THIS_LINE'];
      _.forEach(oUserInputs.data, function (oNewData) {
        //check if the added translation key is already present in other fileTypes with different value.
        _.forEach(aFileTypes, (sFileType)=> {
          if(sFileType !== oUserInputs.fileType && sFileNameSuccessor === '__en_US.json'){
            let sOtherFileName = sUITranslationFolderPath + sFileType + sFileNameSuccessor;
            let oOtherFileData = JSON.parse(fs.readFileSync(sOtherFileName, 'utf8'));
            if(oOtherFileData.hasOwnProperty(oNewData.key) && oOtherFileData[oNewData.key] !== oNewData.value){
              throw oNewData.key+" is already present in "+ sFileType + sFileNameSuccessor + " with different value ";
            }

          }
        });
        oFileData[oNewData.key] = oNewData.value + _oLanguageMapping[sFileNameSuccessor];
      });

      oFileData['ADD_ABOVE_THIS_LINE'] = "add above this line";
    }else {
      /**
       * This block works only for removing any translation key value to the files.
       * We do ont need value form the 'oUserInputs.data' object
       * but since we have that in the data model so kept it same for both add n remove cases
       * CONCLUSION: 'oUserInputs.data[n].value' IS NOT MANDATORY.
       * */
      _.forEach(oUserInputs.data, function (oNewData) {
        delete oFileData[oNewData.key];
      });
    }


    fs.writeFileSync(sFileName, JSON.stringify(oFileData, null, 2));

  });
};

startProcess();