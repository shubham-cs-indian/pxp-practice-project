// require('babel-register');
import CS from '../libraries/cs';
import fs from 'fs';
import path from 'path';

function shim(str) {
  str.isRequired = str;
  return str;
}

function getShim () {
  return {
    type: 'custom',
    isRequired: {type: 'custom'}
  }
}

function arrayOf () {
  return {
    type: 'array',
    isRequired: {type: 'array'}
  }
}
let ContentAttributeElementViewModel = "";
let aUseCases = [];
let ContextMenuViewNewProps = "";
let CircledTagNodeModel ="";
let ContentConcatenatedAttributeViewModel = "";
let SimpleSearchBarViewPropTypes = "";
let ImageViewModel = "";
let ContentPopoverModel = "";
let ContextMenuViewModel = "";
let aPopoverPositions = "";
let ContextualAttributeElementViewModel = "";
let ThumbnailModel = "";
let ListItemModel = "";
let ListViewModel = "";
let DragNDropViewModel = "";
let DragViewModel = "";
let DropViewModel = "";
let ImageCoverflowModel ="";
let ImageGalleryDialogViewModel = "";
let LinkScrollViewModel = "";
let LinkViewModel = "";
let LinkedElementModel = "";
let ListInfoViewModel = "";
let ContentMeasurementMetricsViewModel = "";
let MenuViewModel = "";
let MultiSelectSearchViewModel = "";
let PaperViewModel = "";
let SmallTaxonomyViewModel = "";
let oDateRangeSelectorPropTypes = "";
let oSimpleSearchBarPropTypes = "";
let oContextMenuWithShufflePropTypes = "";
let TagGroupModel = "";
let ThumbnailItemModel = "";
let TreeViewModel = "";
let UserInformationViewModel = "";
let ContentUserViewModel = "";
// const ReactPropTypes = require('prop-types');
const ReactPropTypes = {
  array: {
    type: "array",
    isRequired: {type: "array"}
  },
  bool: {
    type: "bool",
    isRequired: {type: "bool"}
  },
  func: {
    type: "func",
    isRequired: {type: "func"}
  },
  number: {
    type: "number",
    isRequired: {type: "number"}
  },
  object: {
    type: "object",
    isRequired: {type: "object"}
  },
  string: {
    type: "string",
    isRequired: {type: "string"}
  },
  symbol: {
    type: "symbol",
    isRequired: {type: "symbol"}
  },

  any: shim,
  arrayOf: arrayOf,
  element: shim,
  instanceOf: getShim,
  node: shim,
  objectOf: getShim,
  oneOf: getShim,
  oneOfType: getShim,
  shape: getShim,
  exact: getShim
}

let React = {}
React.PropTypes = ReactPropTypes;
let aAllFilesPath = [];
String.prototype.splice = function(idx, rem, str) {
  return this.slice(0, idx) + str + this.slice(idx + Math.abs(rem));
};

let getAllFilePath = function (sRootFolder) {
  let aFolderOrFile = fs.readdirSync(sRootFolder);
  CS.forEach(aFolderOrFile, function (sFolderOrFile) {
    let sFileName = sRootFolder + sFolderOrFile;
    if (sFolderOrFile.includes('.')) {
      aAllFilesPath.push(sFileName);
    } else {
      getAllFilePath(sFileName + "/");
    }
  });
};


let getFileData = function (aRootFilePath) {
  CS.forEach(aRootFilePath, function (sFilePath) {
    console.log(sFilePath);
    // let oView = require(sFilePath);
    let sComment = "\n/**\n * @class";
    let sFileData = fs.readFileSync(sFilePath, "utf8");
    let sFileName = path.basename(sFilePath);
    let iClassIndex = sFileData.indexOf('class ');
    let iExtendsIndex = sFileData.indexOf(' extends');
    let sClassName = sFileData.substring(iClassIndex , iExtendsIndex);
    sComment += " " + sClassName.split(" ")[1] + "\n * @memberOf Views";
    //console.log(sClassName.split(" ")[1]);
    if(CS.includes(sFileData, "const oPropTypes = {")) {
      let iPropsIndex = sFileData.indexOf('oPropTypes = {');
      if (iPropsIndex) {
        let iStartIndex = sFileData.indexOf('{', iPropsIndex);
        let iEndIndex = sFileData.indexOf('};', iPropsIndex);
        let sProps = sFileData.substring(iStartIndex + 1, iEndIndex);

        let obj = eval('({'+ sProps +'})');
        CS.forEach(obj, function (value, key) {
          if (value && !value.isRequired) {
            sComment += "\n * @property {" + value.type + "} " + key ;
          } else if (value) {
            sComment += "\n * @property {" + value.type + "} [" + key + "]";
          }
        })
        sComment +="\n */\n";
        console.log(sComment);
        sFileData = sFileData.splice(iEndIndex + 2, 0, sComment);
        fs.writeFileSync(sFilePath, sFileData);
        //console.log(sComment);
      }
    }
    //console.log(sFileData);
  });
};
getAllFilePath('../../app/viewlibraries/');
getFileData(/*aAllFilesPath*/['D:\\ContentSphere\\CS-Refactored\\REST\\src\\main\\webapp\\gui\\app\\viewlibraries\\attributecontextview\\attribute-context-view.js']);
/*
CS.forEach(aAllFilesPath, function (sFilePath) {
  let FILE = require(sFilePath);
  console.log(FILE);
});
*/

