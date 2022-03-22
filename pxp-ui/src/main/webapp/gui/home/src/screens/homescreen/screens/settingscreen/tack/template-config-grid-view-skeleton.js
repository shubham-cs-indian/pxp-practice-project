import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../viewlibraries/tack/view-library-constants';

let TemplateConfigGridSkeleton = function () {
  return {
    fixedColumns: [
      {
        id: "label",
        label: oTranslations().NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      },
      {
        id: "code",
        label: oTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      }
    ],
    scrollableColumns: [
      {
        id: "propertyCollectionIds",
        label: oTranslations().PROPERTY_COLLECTIONS,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 300
      },
      {
        id: "relationshipIds",
        label: oTranslations().RELATIONSHIPS,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 300
      },
      {
        id: "contextIds",
        label: oTranslations().CONTEXTS,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 300
      },
      {
        id: "natureRelationshipIds",
        label: oTranslations().NATURE_RELATIONSHIPS,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 300
      }
    ],
    actionItems: [{
        id: "delete",
        label: oTranslations().DELETE,
        class: "deleteIcon"
      }
    ],
    selectedContentIds: []
  }
};

export default TemplateConfigGridSkeleton;