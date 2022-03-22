/**
 * Created by CS99 on 25/12/2017.
 */
import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import SettingScreenModuleDictionary from './../../../../../../commonmodule/tack/setting-screen-module-dictionary';
const TRANSLATIONS = "translations";
let aPartnerAdminEntityList = function () {
  var sSplitter = require('../../store/helper/setting-utils').default.getSplitter();
  return [
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CONTENT_TAB,
      entityType: TRANSLATIONS,
      className: 'actionItemContent',
      label: getTranslations().CONTENTS,
      properties: ['label'],
      parentId: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.TABS
    },
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.DASHBOARD_TAB,
      entityType: TRANSLATIONS,
      className: 'actionItemDashboard',
      label: getTranslations().DASHBOARD_MENU_TITLE,
      parentId: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.TABS
    }
  ];
};

export default aPartnerAdminEntityList;