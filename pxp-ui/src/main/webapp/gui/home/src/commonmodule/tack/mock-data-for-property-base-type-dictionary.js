import CS from '../../libraries/cs';
import AttributeBaseTypeDictionary from './attribute-type-dictionary-new';
import TaxonomyBaseTypeDictionary from './mock-data-for-taxonomy-base-types-dictionary';

export default {
  attribute : CS.values(AttributeBaseTypeDictionary),
  tag : ["com.cs.core.config.interactor.entity.tag.Tag"],
  taxonomy : CS.values(TaxonomyBaseTypeDictionary),
}