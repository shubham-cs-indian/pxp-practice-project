/**
 * Created on 05/02/2019.
 */
import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';

import SettingScreenModuleDictionary from './../../../../../../commonmodule/tack/setting-screen-module-dictionary';
const TRANSLATIONS = "translations";
let aSmartDocumentEntityList = function () {
  var sSplitter = require('../../store/helper/setting-utils').default.getSplitter();
  return [
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.SMART_DOCUMENT_TEMPLATE,
      entityType: TRANSLATIONS,
      className: 'actionItemSmartDocumentTemplate',
      label: getTranslations().TEMPLATES,
      properties: ['label']
    },
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.SMART_DOCUMENT_PRESET,
      entityType: TRANSLATIONS,
      className: 'actionItemSmartDocumentPreset',
      label: getTranslations().PRESETS,
      properties: ['label']
    }
  ];
};

export default aSmartDocumentEntityList;