import {getTranslations as oTranslations} from '../store/helper/translation-manager.js';
import MappingTypeDictionary from './mapping-type-dictionary';

const mappings = function () {
  return [
    {
      id: MappingTypeDictionary.INBOUND_MAPPING,
      label: oTranslations().INBOUND_MAPPING
    },
    {
      id: MappingTypeDictionary.OUTBOUND_MAPPING,
      label: oTranslations().OUTBOUND_MAPPING
    },
  ];
};

export default mappings;