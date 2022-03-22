/**
 * Created by CS99 on 31/08/2017.
 */
//To find unused images, run this file in node terminal
import CS from '../libraries/cs';
import fs from 'graceful-fs';
import path from 'path';
var aAllImagesFiles = [];
var aNotFoundImages = [];
var sThemeFile= fs.readFileSync('../libraries/themeloader/theme-loader.js', 'utf8');
var aHtmlFiles = fs.readdirSync('../html/');
var sMockDataImagesFile= fs.readFileSync('../screens/otherscreens/onboardingscreen/screens/contentscreen/tack/mock/mock-data-for-default-image-path.js', 'utf8');
var sCompactContentSphereFile= fs.readFileSync('../../appbuild/compactcontentsphere.js', 'utf8');
var sContentsphereFile= fs.readFileSync('../../appbuild/compactcontentsphere.js', 'utf8');
var sMockViewSettingFile= fs.readFileSync('../screens/otherscreens/onboardingscreen/screens/contentscreen/tack/mock/mock-data-for-class-view-setting.js', 'utf8');
var sMaterialViewFile= fs.readFileSync('../screens/otherscreens/onboardingscreen/screens/contentscreen/view/content-tile-material-view.jsx', 'utf8');
var sMockCircledTagFile= fs.readFileSync('../screens/otherscreens/POCscreen/tack/mock/mock-data-for-circled-tag-view.js', 'utf8');
var themeLoader = fs.readFileSync('../libraries/themeloader/theme-loader.js', 'utf8');
var styleGuideImageScss = fs.readFileSync('../styleguide/_images.scss', 'utf8');
var styleGuideImageScssWrite = ('../styleguide/_images.scss', 'utf8');

var styleBrandAllianz = fs.readFileSync('../themes/brandthemes/_brand-allianz.scss', 'utf8');
var styleBrandAuchan = fs.readFileSync('../themes/brandthemes/_brand-auchan.scss', 'utf8');
var styleBrandBut = fs.readFileSync('../themes/brandthemes/_brand-but.scss', 'utf8');
var styleBrandContentServ = fs.readFileSync('../themes/brandthemes/_brand-contentserv.scss', 'utf8');
var styleBrandEdeka = fs.readFileSync('../themes/brandthemes/_brand-edeka.scss', 'utf8');
var styleBrandFrauenthal = fs.readFileSync('../themes/brandthemes/_brand-frauenthal.scss', 'utf8');
var styleBrandMetro = fs.readFileSync('../themes/brandthemes/_brand-metro.scss', 'utf8');
var styleBrandValkyrie = fs.readFileSync('../themes/brandthemes/_brand-valkyrie.scss', 'utf8');
var styleBrandXxxlutz = fs.readFileSync('../themes/brandthemes/_brand-xxxlutz.scss', 'utf8');
var styleBrandZegna = fs.readFileSync('../themes/brandthemes/_brand-zegna.scss', 'utf8');
var styleColorThemes = fs.readFileSync('../themes/colorthemes/_color-dark.scss', 'utf8');
//let x =require('../themes/')
var aAllImages = [];
var findImageurl = function (sRootFolder) {
  var aFolders = fs.readdirSync(sRootFolder);
//console.log(themeLoader);
  aFolders.forEach(function (sFolder) {
    var aFolderPath = sRootFolder + sFolder;
    if (!sFolder.includes('.')) {
      if (sFolder.match('images')) {
        var aImageFiles = fs.readdirSync(sRootFolder + sFolder + '/');
        var aScssFiles = fs.readdirSync(sRootFolder + '/sass/');
        console.log('********************************************************* Folder: ' + sRootFolder + sFolder + '******************************************');
        //fs.appendFileSync('./NotFoundImagesLog.txt','********************************************* Folder: ' + sRootFolder + sFolder + '**************************************\r\n');
        aImageFiles.forEach(function (sImageFile) {
          aAllImagesFiles.push((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length));
          var isPresentInScss = false;

          /**
           *Searching file on scss files in same directory's sass directory.
           */
          aScssFiles.forEach(function (sScssFile) {

            if(sScssFile.includes('scss')){
              var sScssContent = fs.readFileSync(sRootFolder + 'sass/' + sScssFile, 'utf8');
              if (sScssContent.includes(sImageFile)) {
                isPresentInScss = true;
              }
            }
          });

          /**
           *Searching file on html directory's files.
           */
          aHtmlFiles.forEach(function (sHtmlFile) {
            var sScssContent = fs.readFileSync('../html/' + sHtmlFile, 'utf8');
            if (sScssContent.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))) {
              isPresentInScss = true;
            }
          });

          /**
           *Searching file on other files.
           */
          if(sThemeFile.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))){
            isPresentInScss = true;
          }
          if(sMockDataImagesFile.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))){
            isPresentInScss = true;
          }
          if(sCompactContentSphereFile.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))){
            isPresentInScss = true;
          }
          if(sContentsphereFile.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))){
            isPresentInScss = true;
          }
          if(sMockViewSettingFile.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))){
            isPresentInScss = true;
          }
          if(sMaterialViewFile.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))){
            isPresentInScss = true;
          }
          if(sMockCircledTagFile.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))){
            isPresentInScss = true;
          }
          if(themeLoader.includes((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length))){
            isPresentInScss = true;
          }
          if(styleGuideImageScss.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandAllianz.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandAuchan.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandBut.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandContentServ.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandFrauenthal.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandEdeka.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandMetro.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandValkyrie.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandXxxlutz.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleBrandZegna.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          if(styleColorThemes.includes(( sFolder + '/' + sImageFile))){
            isPresentInScss = true;
          }
          //console.log((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length));

          if (!isPresentInScss) {
            /* console.log((sRootFolder + sFolder + '/' + sImageFile).slice(2,(sRootFolder + sFolder + '/' + sImageFile).length));*/
          }else {
            let sourcePath = (sRootFolder + sFolder + '/' + sImageFile);
            // console.log(sourcePath);
            var styleGuideImageScss1 = fs.readFileSync('../styleguide/_images.scss', 'utf8');
            var sPathMaker = CS.split(sourcePath, '/');
            sPathMaker = sPathMaker[sPathMaker.length - 1];
            var sPathValue = "'../images/" + sPathMaker + "';"
            sPathMaker = CS.split(sPathMaker, '.');
            sPathMaker = sPathMaker[0];
            var sPathVariable = "$image-" + sPathMaker + ":";

            while (
            CS.includes(sPathVariable, " ") ||CS.includes(sPathVariable, "_") || CS.includes(sPathVariable, "@") ||
            CS.includes(sPathVariable, "(") || CS.includes(sPathVariable, ")") || CS.includes(sPathVariable, "%")
                )
            {
              sPathVariable = CS.replace(sPathVariable, ' ', '-');
              sPathVariable = CS.replace(sPathVariable, '@', '-');
              sPathVariable = CS.replace(sPathVariable, '(', '-');
              sPathVariable = CS.replace(sPathVariable, ')', '-');
              sPathVariable = CS.replace(sPathVariable, '_', '-');
              sPathVariable = CS.replace(sPathVariable, '%', '-');
            }

            if (!CS.includes(styleGuideImageScss1, sPathValue)) {
              console.log(sPathVariable + " " + sPathValue);

              let sFileForAppend;
              try {
                sFileForAppend = fs.openSync("../styleguide/_images.scss", 'a');
                styleGuideImageScss1 = fs.appendFileSync(sFileForAppend, sPathVariable + " " + sPathValue + '\n', 'utf8');

              } catch (err) {
                /* Handle the error */
              } finally {
                if (sFileForAppend !== undefined)
                  fs.closeSync(sFileForAppend);
              }
            }
          }
        });
      }

      if (!sFolder.match('images') && !sFolder.match('sass')) {
        findImageurl(aFolderPath + '/');
      }
    }
  });

};

let aRootFilePath = [];
var getAllFilePath = function (sRootFolder) {
  var aFolderOrFile = fs.readdirSync(sRootFolder);

  CS.forEach(aFolderOrFile, function (sFolderOrFile) {
    var sFileName = sRootFolder + sFolderOrFile;
    if (sFolderOrFile.includes('.')) {
      if (sFolderOrFile.includes('scss'))
        aRootFilePath.push(sFileName);
    } else if (!sFolderOrFile.includes('.') && !sFolderOrFile.includes('brandthemes')) {
      getAllFilePath(sFileName + "/");
    }
  });
};


let replaceImage = function () {

  var sFileContent = fs.readFileSync("../styleguide/_images.scss", 'utf8');
  var aFileValue = CS.split(sFileContent, '\n');
  CS.forEach(aFileValue, function (sSingleValue) {

    if (CS.includes(sSingleValue, '$') && CS.includes(sSingleValue, "'")) {
      var aa = CS.split(sSingleValue, "'");
      var temp = CS.split(aa[0], ':')
      aa[0] = temp[0];
      var img = {
        key: aa[1],
        value: aa[0]
      }
      if (CS.includes(img.value, "$")) {
        aAllImages.push(img);
      }
    }
  });

  CS.forEach(aAllImages, function (sSingleValue) {
    CS.forEach(aRootFilePath, function (sSinglefile) {
      var sFilevalues = fs.readFileSync(sSinglefile, 'utf8');
      var newkye = "../../homescreen" + sSingleValue.key.slice(2);
      while (
      CS.includes(sFilevalues, '"' + sSingleValue.key + '"') || CS.includes(sFilevalues, "'" + sSingleValue.key + "'") ||
      CS.includes(sFilevalues, '"' + newkye + '"') || CS.includes(sFilevalues, "'" + newkye + "'")
          )
      {
        if (
            CS.includes(sFilevalues, '"' + sSingleValue.key + '"') || CS.includes(sFilevalues, "'" + sSingleValue.key + "'") ||
            CS.includes(sFilevalues, '"' + newkye + '"') || CS.includes(sFilevalues, "'" + newkye + "'"))
        {
          var updatedfile = CS.includes(sFilevalues, '"' + sSingleValue.key + '"') ?
              CS.replace(sFilevalues, '"' + sSingleValue.key + '"', sSingleValue.value) :
              CS.replace(sFilevalues, "'" + sSingleValue.key + "'", sSingleValue.value);

          if (CS.includes(sFilevalues, '"' + newkye + '"') || CS.includes(sFilevalues, "'" + newkye + "'")) {

            updatedfile = CS.includes(sFilevalues, '"' + newkye + '"') ? CS.replace(sFilevalues, '"' + newkye + '"', sSingleValue.value) : CS.replace(sFilevalues, "'" + newkye + "'", sSingleValue.value);
          }
          var importstatment = '@import "./../../../../styleguide/style-guide";';
          if (!CS.includes(updatedfile, importstatment)) {
            importstatment = importstatment + "\n";
            updatedfile = importstatment.concat(updatedfile);
          }

          sFilevalues = updatedfile;

          fs.writeFileSync(sSinglefile, updatedfile);
          console.log("file updated");
        }
      }
      /*while (CS.includes(sFilevalues, "'" + sSingleValue.key + "'")) {
        if (CS.includes(sFilevalues, "'"+ sSingleValue.key + "'")) {
          var updatedfile = CS.replace(sFilevalues, "'" + sSingleValue.key + "'", sSingleValue.value);
          var importstatment = '@import "./../../../../styleguide/style-guide";';
          if(!CS.includes(updatedfile,importstatment))
          {
            importstatment = importstatment + "\n";
            updatedfile =importstatment.concat(updatedfile);
          }

          sFilevalues = updatedfile;

          fs.writeFileSync(sSinglefile, updatedfile);
          console.log("file updated");
        }
      }*/
    });

  });
  console.log(aAllImages.length)
}

//findImageurl('../themes/');
getAllFilePath('../themes/');

replaceImage();
