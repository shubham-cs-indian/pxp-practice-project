import AttributeTypeDictionary from '../../../../../../commonmodule/tack/attribute-type-dictionary-new';

export default {
  [AttributeTypeDictionary.DATE]: [AttributeTypeDictionary.DATE],
  [AttributeTypeDictionary.HTML]: [AttributeTypeDictionary.TEXT, AttributeTypeDictionary.HTML, AttributeTypeDictionary.NUMBER],
  [AttributeTypeDictionary.NUMBER]: [AttributeTypeDictionary.NUMBER],
  [AttributeTypeDictionary.PRICE]: [AttributeTypeDictionary.PRICE],
  [AttributeTypeDictionary.TEXT]: [AttributeTypeDictionary.TEXT, AttributeTypeDictionary.NUMBER]
};