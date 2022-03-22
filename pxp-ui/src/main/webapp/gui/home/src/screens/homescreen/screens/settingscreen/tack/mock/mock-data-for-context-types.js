import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary.js';

const contextTypes = function () {
  return [
    {
      id: ContextTypeDictionary.CONTEXTUAL_VARIANT,
      label: oTranslations().CONTEXT_TYPES_CONTEXTUAL_VARIANT
    },
    {
      id: ContextTypeDictionary.PRODUCT_VARIANT,
      label: oTranslations().CONTEXT_TYPES_PRODUCT_VARIANT
    },
    {
      id: ContextTypeDictionary.LANGUAGE_VARIANT,
      label: oTranslations().CONTEXT_TYPES_LANGUAGE_VARIANT
    },
    {
      id: ContextTypeDictionary.PROMOTIONAL_VERSION,
      label: oTranslations().CLONE_CONTEXT
    },
    {
      id: ContextTypeDictionary.RELATIONSHIP_VARIANT,
      label: oTranslations().CONTEXT_TYPES_RELATIONSHIP_VARIANT
    },
    {
      id: ContextTypeDictionary.IMAGE_VARIANT,
      label: oTranslations().CONTEXT_TYPES_IMAGE_VARIANT
    },
    {
      id: ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT,
      label: oTranslations().ATTRIBUTE_CONTEXT_VARIANT
    },
    {
      id: ContextTypeDictionary.GTIN_CONTEXT,
      label: oTranslations().GTIN_CONTEXT
    }
  ];
};

export default contextTypes;