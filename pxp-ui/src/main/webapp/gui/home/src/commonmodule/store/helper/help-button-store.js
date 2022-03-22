import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import BreadCrumbProps from '../../props/breadcrumb-props';
import HelpScreenMappingDictionary from '../../tack/help-screen-mapping-dictionary';

let HelpButtonStore = (function () {

  let _handleHelpButtonClicked = function () {
    let aBreadCrumbData = BreadCrumbProps.getBreadCrumbData();
    let iIndex = aBreadCrumbData.length;
    let oLatestBreadCrumb = aBreadCrumbData[iIndex -1];
    let sHelpScreenId = oLatestBreadCrumb.helpScreenId || "MDM";
   /* let sURL = HelpScreenMappingDictionary()[sHelpScreenId];*/
    let sURL = HelpScreenMappingDictionary();
   /* if(!sURL) {
      sURL =  HelpScreenMappingDictionary()["fallbackURL"] + sHelpScreenId;
    }*/

    window.open(sURL);
  };

 return {
    handleHelpButtonClicked : function () {
      _handleHelpButtonClicked();
    },
  };

})();

MicroEvent.mixin(HelpButtonStore);

export default HelpButtonStore;
