import CS from '../../libraries/cs';
import SessionStorageManager from './../../libraries/sessionstoragemanager/session-storage-manager';
import SessionStorageConstants from './session-storage-constants';
var sRootURL = "https://contentserv.atlassian.net/wiki/spaces/DOCUPXP/pages/2253652501/PXP+Documentation";


let fGetLanguage = function () {
  let sLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
  return CS.split(sLanguage, '_')[0];
};

const HelpScreenMappings = function () {
  /*return(
      {
        dashboard: sRootURL + fGetLanguage() + "/latest/dashboardlanding.html",
        pimmodule: sRootURL + fGetLanguage() + "/latest/pimlanding.html",
        mammodule: sRootURL + fGetLanguage() + "/latest/damlanding.html",
        targetmodule: sRootURL + fGetLanguage() + "/latest/?q=target",
        suppliermodule: sRootURL + fGetLanguage() + "/latest/supplierlanding.html",
        textassetmodule: sRootURL + fGetLanguage() + "/latest/?q=textasset",
        virtualcatalogmodule: sRootURL + fGetLanguage() + "/latest/virtualcataloglanding.html",

        content: sRootURL + fGetLanguage() + "/latest/latestlanding.html",
        staticCollection: sRootURL + fGetLanguage() + "/latest/?q=staticCollection",
        dynamicCollection: sRootURL + fGetLanguage() + "/latest/?q=dynamicCollection",

        dashboardRuleViolation: sRootURL + fGetLanguage() + "/latest/?q=dashboardRuleViolation",
        explorerKpiTile: sRootURL + fGetLanguage() + "/latest/?q=explorerKpiTile",
        endpointScreen: sRootURL + fGetLanguage() + "/latest/peplanding.html",

        quickList: sRootURL + fGetLanguage() + "/latest/?q=quicklist",
        variantQuickList: sRootURL + fGetLanguage() + "/latest/?q=variantQuicklist",

        collectionHierarchy: sRootURL + fGetLanguage() + "/latest/?q=collectionHierarchy",
        filterHierarchy: sRootURL + fGetLanguage() + "/latest/?q=filterHierarchy",
        taxonomyHierarchy: sRootURL + fGetLanguage() + "/latest/?q=taxonomyHierarchy",
        MDM: sRootURL + fGetLanguage() + "/latest/",
        fallbackURL: sRootURL + fGetLanguage() + "/latest/search.html?q="
      })*/
  return sRootURL;
};

export default HelpScreenMappings;
