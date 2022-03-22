import {getTranslations as oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';

let PropertyCollectionTabLayoutData = function () {
  return {
    tabList: [
      {
        id: "Property_Collection_List_Tab",
        label: oTranslations().PROPERTY_COLLECTION_LIST_TAB,
        className: "propertyCollectionTabIcon"
      },
      {
        id: "Property_Collection_X_Ray_List_Tab",
        label: oTranslations().PROPERTY_COLLECTION_X_RAY_LIST_TAB,
        className: "xRayPropertyCollectionTabIcon"
      }
    ],

    tabListIds: {
      PROPERTY_COLLECTION_LIST_TAB: "Property_Collection_List_Tab",
      PROPERTY_COLLECTION_X_RAY_LIST_TAB: "Property_Collection_X_Ray_List_Tab"
    }
  };
};

export default PropertyCollectionTabLayoutData;