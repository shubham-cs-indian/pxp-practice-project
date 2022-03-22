import ScreenModeUtils from '../../screens/homescreen/screens/contentscreen/store/helper/screen-mode-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

var getRuntimeMappingAllCategoriesTaxonomies = function () {
  return [
    {
      id: "natureClasses",
      label: getTranslation().NATURE_CLASSES,
    },
    {
      id: "attributionClasses",
      label: getTranslation().ATTRIBUTION_CLASSES,
    },
    {
      id: "taxonomies",
      label: getTranslation().TAXONOMIES,
    }
  ]
};

export default getRuntimeMappingAllCategoriesTaxonomies;