import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';
import RuleTypeDictionary from './rule-type-dictionary';
const oRuleTypeDictionary = new RuleTypeDictionary();

const oRules = function () {
  return [
    {
      id: oRuleTypeDictionary.CLASSIFICATION_RULE,
      label: oTranslations().CLASSIFICATION
    },
    {
      id: oRuleTypeDictionary.STANDARDIZATION_AND_NORMALIZATION_RULE,
      label: oTranslations().STANDARDIZATION_AND_NORMALIZATION
    },
    {
      id: oRuleTypeDictionary.VIOLATION_RULE,
      label: oTranslations().VIOLATION
    }
  ];
};

export default oRules;