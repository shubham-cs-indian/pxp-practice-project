import ScreenModeUtils from '../../store/helper/screen-mode-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

var getRuntimeMappingTabListItems = function () {
  return [
    {
      id: "properties",
      label: getTranslation().PROPERTIES
    },
    {
      id: "classes",
      label: getTranslation().CLASSES
    },
    {
      id: "taxonomies",
      label: getTranslation().TAXONOMIES
    }
  ]
};

export default getRuntimeMappingTabListItems;