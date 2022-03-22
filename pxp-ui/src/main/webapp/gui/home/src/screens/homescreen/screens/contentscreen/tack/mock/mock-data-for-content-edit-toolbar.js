import ScreenModeUtils from '../../store/helper/screen-mode-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;
var ContentEditToolbarData = function () {
  return[
    {
      id: 'refresh',
      label: getTranslation().REFRESH
    },
    {
      id: 'save',
      label: getTranslation().SAVE
    },
    {
      id: 'saveWithWarning',
      label: getTranslation().SAVE_WITH_WARNINGS
    },
    {
      id: 'discard',
      label: getTranslation().DISCARD_CHANGES
    },
    {
      id: 'createClone',
      label: getTranslation().CLONE
    },
    {
      id: "deleteLanguage",
      label: getTranslation().DELETE_LANGUAGE
    },
    {
      id: "saveAndPublish",
      label: getTranslation().SAVE_AND_PUBLISH
    },
    {
      id: "viewSources",
      label: getTranslation().VIEW_SOURCES
    },
    {
      id: "remergeSources",
      label: getTranslation().REMERGE_SOURCES
    }
  ]
};
export default ContentEditToolbarData;