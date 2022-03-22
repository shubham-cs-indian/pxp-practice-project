import CS from '../libraries/cs';
import fs from 'fs';
import aLanguages from './locale';
var sFileContent = fs.readFileSync("./locale.js", 'utf8');
var aLines = sFileContent.split('\n');
var aLanguageLocales = [];

CS.forEach(aLines, function (sLine) {
    var aValues = CS.split(sLine, " (");
    var sLocaleLabel = CS.split(sLine, " :", 1)[0] + ")";
    var aLocalesFormats = CS.split(aValues[1], " : ");
    var sDateFormat = aLocalesFormats[1];
    var sNumberFormat = aLocalesFormats[2];

    aLanguageLocales.push({
      id: sLocaleLabel.substring(CS.indexOf(sLocaleLabel, "(") + 1, CS.indexOf(sLocaleLabel, ":")),
      label: sLocaleLabel,
      locale: aValues[0],
      dateFormat: sDateFormat,
      numberFormat: sNumberFormat
    });
});

console.log(aLanguageLocales.length);
console.log(JSON.stringify(aLanguageLocales, null, 2));
console.log(JSON.stringify(CS.groupBy(aLanguages, "numberFormat"), null, 2));
console.log(JSON.stringify(CS.groupBy(aLanguages, "dateFormat"), null, 2));
