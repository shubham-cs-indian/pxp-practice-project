/**
 * Created by CS99 on 23/08/2017.
 */
import CS from '../libraries/cs';
import DutchTranslationContent from '../screens/homescreen/screens/contentscreen/tack/content-screen-translations-de';
import DutchTranslationContentRevised from '../devUtilities/content-screen-translations-de-3';

var updateContentScreenTranslationDe = function () {
  var contentScreenOriginal = DutchTranslationContent.ContentScreenTranslations;
  var contentScreenRevised = DutchTranslationContentRevised.ContentScreenTranslations;

  CS.assign(contentScreenOriginal, contentScreenRevised);

  for (var key in contentScreenOriginal) {
    console.log("\"" + key + "\"" + " : " + "\"" + contentScreenOriginal[key] + "\",");
  }
};

updateContentScreenTranslationDe();