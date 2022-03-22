import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import CS from '../../../../../../libraries/cs';
import aTranslationsGroupList from '../translations-module-list';
import oModulesDictionary from "./config-modules-dictionary";

let aTranslationConfigurationList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY,
      label: getTranslations().LANGUAGE_TREE,
      children:[/**No children*/],
      className: 'actionItemTaxonomyLanguageList',
      parentId: oModulesDictionary.TRANSLATION_CONFIGURATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS,
      label: getTranslations().TRANSLATIONS,
      className: "actionItemTranslations",
      children: CS.sortBy(new aTranslationsGroupList().SettingScreenModules, "label"),
      parentId: oModulesDictionary.TRANSLATION_CONFIGURATION
    }
  ];
};

export default aTranslationConfigurationList;
