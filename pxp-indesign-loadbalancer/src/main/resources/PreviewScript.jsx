// script for converting indesign file to pdf


var JSON = JSON || {};

// implement JSON.parse de-serialization
JSON.parse = JSON.parse || function (str) {
  if (str === "") str = '""';
  eval("var p=" + str + ";");
  return p;
};

var sFunctionArgs = app.scriptArgs.getValue("functionArgs");
var oScriptRequestModel =  JSON.parse(sFunctionArgs);

//get asset file from script request model
var sAssetFilePath = oScriptRequestModel.assetsDirectory;
var documentName = oScriptRequestModel.documentName;
var result = "";

if(sAssetFilePath){
    try {
            var assetFile = File (sAssetFilePath); 
            var activeDocument = null;
            try{
               activeDocument = app.open(assetFile);            
            }
            catch(e){
                app.consoleout("Error in opening the file");
                result = "failure";
            }
            var extension = documentName.substring(documentName.lastIndexOf('.'));
            var filePath = oScriptRequestModel.assetsDirectory.replace (extension, ".pdf" );    
            activeDocument.exportFile(ExportFormat.PDF_TYPE, File (filePath));
            activeDocument.close();
            result = "success"        
    }
    catch(e) {
         result = "failure";
    }
}

result;