import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';
import ProcessEventTypeDictionary from './process-event-type-dictionary';

export default function () {
  return [
    {
      id: ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT,
      label: oTranslations().BUSINESS_PROCESS_WORKFLOW
    },
    {
      id: ProcessEventTypeDictionary.INTEGRATION,
      label: oTranslations().INTEGRATION
    },
  ];
};
