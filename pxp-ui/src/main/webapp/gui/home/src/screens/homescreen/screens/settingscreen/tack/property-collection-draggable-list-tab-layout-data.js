import {getTranslations as oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';

const PropertyCollectionDraggableListTabsLayoutData = function () {
  return {
    tabsList: [
      {
        label: oTranslations().ATTRIBUTES,
        className: "attribute ",
        id: "attributes",
        type: "attribute"
      },
      {
        label: oTranslations().TAGS,
        className: "tags ",
        id: "tags",
        type: "tag"
      },
      {
        label: oTranslations().MINOR_TAXONOMIES,
        className: "classification ",
        id: "taxonomies",
        type: "taxonomy"
      }
    ]
  }
};

export default PropertyCollectionDraggableListTabsLayoutData;