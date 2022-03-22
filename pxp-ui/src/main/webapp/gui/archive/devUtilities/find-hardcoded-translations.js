//To find hardcoded translations, run this file in node terminal
import fs from 'graceful-fs';
import path from 'path';
var aNoRegexPre = ['className', 'ClassName', 'class-name', 'Class', 'class', 'imageSrc', 'duration', 'ref', 'Ref', 'type', 'Type', 'key', 'mode', 'Comparator', 'comparator', 'Event', 'sortField', 'SortField', 'Discount', 'discount', 'event', 'Suffix', 'suffix', 'image', 'Image', 'transition', 'sortOrder', 'Key', 'url', 'Url', 'context', 'Context', 'style', 'Style', 'value', 'background', 'horizontal', 'vertical', 'border', 'color', 'Color', 'bottom', 'clear', 'clip', 'display', 'float', 'height', 'Height', 'left', 'margin', 'width', 'overflow', 'padding', 'right', 'top', 'visibility',
  'flex', 'align', 'order', 'hyphens', 'id', 'Id', 'font', 'direction', 'baseType', 'hyphens', 'position', 'animation', 'rest', 'marquee', 'outline', 'fill', 'stroke', 'placement', 'background-attachment', 'background-blend-mode', 'border-bottom', 'border-bottom-style', 'border-image-outset', 'border-image-repeat', 'border-image-slice', 'border-left', 'border-left-color', 'border-left-style', 'border-left-width', 'border-radius', 'border-right', 'border-right-style',
  'border-style', 'border-top', 'borderBottom', 'border-top-left-radius', 'border-top-right-radius', 'border-top-style', 'box-decoration-break', 'box-shadow', 'margin-bottom', 'overflow-x', 'overflow-y', 'vertical-align', 'z-index', 'align-content', 'align-items', 'align-self', 'flex-basis', 'flex-direction', 'flex-flow', 'flex-grow', 'flex-shrink', 'flex-wrap', 'justify-content', 'order', 'hanging-punctuation', 'hyphens', 'letter-spacing', 'line-break', 'overflow-wrap',
  'text-align-last', 'textTransform', 'text-combine-upright', 'BackgroundColorHex', 'htmlFor', 'text-indent', 'text-justify', 'text-transform', 'white-space', 'word-break', 'boxShadow', 'word-spacing', 'word-wrap', 'font-family', 'font-feature-settings', 'font-kerning', 'font-language-override', 'font-size', 'font-size-adjust', 'font-stretch', 'font-style', 'font-synthesis', 'font-variant', 'font-variant-alternates', 'font-variant-caps', 'fontSize', 'font-weight', 'text-orientation', 'text-combine-upright', 'unicode-bidi', 'backface-visibility', 'perspective',
  'perspective-origin', 'transform', 'transform-origin', 'resize', 'cursor', 'ime-mode', 'textAlign', 'fontStyle', 'Degree1', 'tab-size', '?', '=', '_', '+', '!'];
var aNoFileRegex = ['/otherscreens/', '/POCscreen/', '.scss', 'mock', 'translations', 'inspector.js', 'inspector.js', '.css', 'toolbar.js', 'modules-data', 'props.js', '-list.js', '-mapping.js', 'stencil.js', 'dictionary.js'];
// var aRegex = ['="', '= "', '=\'', '= \'', '={\'', '={"', '= {\'', '= {"', '>{\'', '>{"', '> {\'', '> {"', ': "', ':\'', ':"', ': \''];
var aRegex = ['>{\'', '>{"', '> {\'', '> {"', '> { \'', '> { "'];
var sRegexForNumberAndSplChar = /^[0-9_@.\s\[\]\{}:\\/#&"\+\-,·\./\*^_%$]+$/;
var aRootFolders = ['../screens/', '../viewlibraries/'];
var sFileContent = '';

function prependPreviousFile () {
  fs.appendFile('./log.txt', sFileContent, function (err) {
    if (err) throw err;
  });
}

var startScanningFile = function (aRootFolders) {
  sFileContent = fs.readFileSync('./log.txt', 'utf8');
  fs.writeFile('./log.txt', '', function (err) {
    if (err) throw err;
  });
  var date = new Date();
  fs.appendFile('./log.txt', '\n*********************************************************' + date + '**********************************************\r\n\n', function (err) {
    if (err) throw err;
  });
  for (var iIndex = 0; iIndex < aRootFolders.length; iIndex++) {
    scanAllFiles(aRootFolders[iIndex]);
  }
  setTimeout(prependPreviousFile, 5000);
};


var scanAllFiles = function (sRootFolder) {
  fs.readdir(sRootFolder, (err, files) => {
    if (err) {
      return;
    }
    files.forEach(file => {
      var sFileName = sRootFolder + file;
      if (file.includes('.')) {
        var isFileValid = true;
        for (var iIndex = 0; iIndex < aNoFileRegex.length; iIndex++) {
          if (sFileName.includes(aNoFileRegex[iIndex])) {
            isFileValid = false;
            break;
          } else {
            isFileValid = true;
          }
        }
        if (isFileValid) {
          findHardcodedTranslations(sFileName);
        }
      } else {
        scanAllFiles(sFileName + "/");
      }
    });
  });
};

var findHardcodedTranslations = function (sFile) {
  var lineReader = require('readline').createInterface({
    input: fs.createReadStream(sFile)
  });
  var iLineNo = 1;
  var isCodeCommitted = false;
  lineReader.on('line', function (sLine) {
    if (sLine.includes('/*')) {
      isCodeCommitted = true;
    }
    if (sLine.includes('*/')) {
      isCodeCommitted = false;
    }
    if (!isCodeCommitted) {
      var isHardcoded = isHardcodedLine(sLine);
      if (isHardcoded) {
        var sHardcodedLine = path.basename(sFile) + ':' + iLineNo + ' :' + sLine;
        console.log(sHardcodedLine);
        fs.appendFile('./log.txt', sHardcodedLine + '\r\n', function (err) {
          if (err) throw err;
        });
      }
    }
    iLineNo++;
  });
};

var isHardcodedLine = function (sLine) {
  for (var iIndex = 0; iIndex < aRegex.length; iIndex++) {
    var sLineLocal = sLine.trim();
    var isHardcoded = false;
    var iSearchIndex = sLineLocal.search(aRegex[iIndex]);
    var iMinLineNo = 0;

    while (iSearchIndex != -1) {
      var iLineToSkip = 2;
      if (aRegex[iIndex].includes(' ') && aRegex[iIndex].includes('{')) {
        iLineToSkip = 4;
      } else if (aRegex[iIndex].includes(' ') || aRegex[iIndex].includes('{')) {
        iLineToSkip = 3;
      }
      var iLineNoCloseCote = sLineLocal.slice(iSearchIndex + iLineToSkip, sLineLocal.length).search('"');
      if (iLineNoCloseCote == -1) {
        iLineNoCloseCote = sLineLocal.slice(iSearchIndex + iLineToSkip, sLineLocal.length).search('\'');
      }
      if (iLineNoCloseCote == -1) {
        isHardcoded = false;
        sLineLocal = sLineLocal.slice(iSearchIndex + 1, sLineLocal.length);
        iSearchIndex = sLineLocal.search(aRegex[iIndex]);
        continue;
      }
      else {
        if (sLineLocal.slice(iSearchIndex + iLineToSkip, iSearchIndex + iLineToSkip + iLineNoCloseCote).match(sRegexForNumberAndSplChar)
            || (sLineLocal.slice(iSearchIndex + iLineToSkip, iSearchIndex + iLineToSkip + iLineNoCloseCote).length == 1 || (sLineLocal.slice(iSearchIndex + iLineToSkip, iSearchIndex + iLineToSkip + iLineNoCloseCote) == ''))
        /*|| sLineLocal.slice(iSearchIndex + iLineToSkip, iSearchIndex + iLineToSkip + iLineNoCloseCote).includes('shop') || sLineLocal.slice(iSearchIndex + iLineToSkip, iSearchIndex + iLineToSkip + iLineNoCloseCote).includes('Shop')*/) {
          isHardcoded = false;
          sLineLocal = sLineLocal.slice(iSearchIndex + 1, sLineLocal.length);
          iSearchIndex = sLineLocal.search(aRegex[iIndex]);
          continue;
        }
      }


      if (sLineLocal.slice(iSearchIndex, iSearchIndex + 4).includes('""') || sLineLocal.slice(iSearchIndex, iSearchIndex + 4).includes('\'\'') || sLine.trim().startsWith('//')
          || sLine.trim().startsWith('/*') || (sLine.includes('?') && sLine.includes(':'))) {
        isHardcoded = false;
        sLineLocal = sLineLocal.slice(iSearchIndex + 1, sLineLocal.length);
        iSearchIndex = sLineLocal.search(aRegex[iIndex]);
        continue;
      } else {
        for (var i = 0; i < aNoRegexPre.length; i++) {
          if ((iSearchIndex - (aNoRegexPre[i].length + 3)) < 0) {
            iMinLineNo = 0;
          } else {
            iMinLineNo = iSearchIndex - (aNoRegexPre[i].length + 3);
          }
          if (aNoRegexPre[i] == '_') {
            iMinLineNo = iSearchIndex - 12;
            if (iMinLineNo < 0)
              iMinLineNo = 0;
          }
          var sPreString = sLineLocal.slice(iMinLineNo, iSearchIndex);
          if (sPreString.includes(aNoRegexPre[i])) {
            isHardcoded = false;
            break;
          } else {
            isHardcoded = true;
          }
        }
      }

      if (isHardcoded) {
        sLineLocal = sLineLocal.slice(iSearchIndex + 1, sLineLocal.length);
        iSearchIndex = sLineLocal.search(aRegex[iIndex]);
        break;
      }
      sLineLocal = sLineLocal.slice(iSearchIndex + 1, sLineLocal.length);
      iSearchIndex = sLineLocal.search(aRegex[iIndex]);
    }
    if (isHardcoded) {
      break;
    }
  }

  if (!isHardcoded) {
    isHardcoded = false;
    iSearchIndex = sLineLocal.search('>');
    while (iSearchIndex != -1) {
      var n2 = sLineLocal.slice(iSearchIndex, sLineLocal.length).search('</');
      if (n2 != -1) {
        if (!(sLineLocal.slice(iSearchIndex + 1, iSearchIndex + n2).includes('{')) && !(sLineLocal.slice(iSearchIndex + 1, iSearchIndex + n2) == "") && (sLineLocal.slice(iSearchIndex + 1, iSearchIndex + n2).length != 1)
            /*&& !(sLineLocal.slice(iSearchIndex + 1, iSearchIndex + n2).includes('shop')) && !(sLineLocal.slice(iSearchIndex + 1, iSearchIndex + n2).includes('Shop'))*/
            && !(sLineLocal.slice(iSearchIndex + 1, iSearchIndex + n2).match(sRegexForNumberAndSplChar)) && !sLine.trim().startsWith('//') && !sLine.trim().startsWith('/*')) {
          isHardcoded = true;
          break;
        }
      }
      sLineLocal = sLineLocal.slice(iSearchIndex + 1, sLineLocal.length);
      iSearchIndex = sLineLocal.search('>');
    }
  }
  return isHardcoded;
};

startScanningFile(aRootFolders);

//findHardcodedTranslations('./sample.js');