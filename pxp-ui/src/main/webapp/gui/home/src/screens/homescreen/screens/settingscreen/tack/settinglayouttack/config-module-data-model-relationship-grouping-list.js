import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';

let aDataModelRelationshipGroupingList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP,
      label: getTranslations().RELATIONSHIPS,
      children: [/**No children*/],
      className: "actionItemRelationships"
    },
  ];
};

export default aDataModelRelationshipGroupingList;
