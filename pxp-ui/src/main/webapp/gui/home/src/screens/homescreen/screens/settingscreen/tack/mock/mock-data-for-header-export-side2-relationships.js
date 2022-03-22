import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import TabHeaderData from "./mock-data-for-map-summery-header";


const headerDataForExportSide2Relationships = function () {
  return [
      {
        id: TabHeaderData.relationship,
        label: oTranslations().RELATIONSHIPS
      },
      {
        id: TabHeaderData.attribute,
        label: oTranslations().ATTRIBUTES
      },
      {
        id: TabHeaderData.tag,
        label: oTranslations().TAGS
      },
    ]
};

export default headerDataForExportSide2Relationships;