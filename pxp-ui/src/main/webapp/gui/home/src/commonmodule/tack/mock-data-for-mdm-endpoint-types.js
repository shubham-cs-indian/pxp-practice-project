import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';
import EndpointTypeDictionary from './endpoint-type-dictionary';
const endpoints = function () {
  return [
    {
      id: EndpointTypeDictionary.INBOUND_ENDPOINT,
      label: oTranslations().INBOUND_ENDPOINT
    },
    {
      id: EndpointTypeDictionary.OUTBOUND_ENDPOINT,
      label: oTranslations().OUTBOUND_ENDPOINT
    },
  ];
};

export default endpoints;