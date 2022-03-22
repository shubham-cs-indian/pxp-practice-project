package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface ISaveOutBoundMappingModel extends IModel, IOutBoundMapping {
  
  public static final String PROPERTY_COLLECTION_MAPPINGS        = "propertyCollectionMappings";
  public static final String ADDED_TPROPERTY_COLLECTION_IDS      = "addedPropertyCollectionIds";
  public static final String MODIFIED_PROPERTY_COLLECTION_IDS    = "modifiedPropertyCollectionIds";
  public static final String DELETED_PROPERTY_COLLECTION_IDS     = "deletedPropertyCollectionIds";
  public static final String ADDED_ATRRIBUTE_MAPPINGS            = "addedAttributeMappings";
  public static final String MODIFIED_ATTRIBUTE_MAPPINGS         = "modifiedAttributeMappings";
  public static final String DELETED_ATTRIBUTE_MAPPINGS          = "deletedAttributeMappings";
  public static final String ADDED_TAG_MAPPINGS                  = "addedTagMappings";
  public static final String MODIFIED_TAG_MAPPINGS               = "modifiedTagMappings";
  public static final String DELETED_TAG_MAPPINGS                = "deletedTagMappings";
  public static final String TAB_ID                              = "tabId";
  public static final String SELECTED_PROPERTY_COLLECTION_ID     = "selectedPropertyCollectionId";
  public static final String CONFIG_RULE_IDS_FOR_ATTRIBUTE       = "configRuleIdsForAttribute";
  public static final String CONFIG_RULE_IDS_FOR_TAG             = "configRuleIdsForTag";
  public static final String ADDED_CLASS_MAPPINGS                = "addedClassMappings";
  public static final String MODIFIED_CLASS_MAPPINGS             = "modifiedClassMappings";
  public static final String DELETED_CLASS_MAPPINGS              = "deletedClassMappings";
  public static final String ADDED_TAXONOMY_MAPPINGS             = "addedTaxonomyMappings";
  public static final String MODIFIED_TAXONOMY_MAPPINGS          = "modifiedTaxonomyMappings";
  public static final String DELETED_TAXONOMY_MAPPINGS           = "deletedTaxonomyMappings";
  public static final String ADDED_RELATIONSHIP_MAPPINGS         = "addedRelationshipMappings";
  public static final String MODIFIED_RELATIONSHIP_MAPPINGS      = "modifiedRelationshipMappings";
  public static final String DELETED_RELATIONSHIP_MAPPINGS       = "deletedRelationshipMappings";
  
  public void setAddedClassMappings(List<IConfigRuleClassMappingModel> addedClassMappings);
  
  public List<IConfigRuleClassMappingModel> getAddedClassMappings();
  
  public void setModifiedClassMappings(List<IConfigRuleClassMappingModel> modifiedClassMappings);
  
  public List<IConfigRuleClassMappingModel> getModifiedClassMappings();
  
  public void setDeletedClassMappings(List<String> deletedClassMappings);
  
  public List<String> getDeletedClassMappings();
  
  public void setAddedTaxonomyMappings(List<IConfigRuleTaxonomyMappingModel> addedTaxonomyMappings);
  
  public List<IConfigRuleTaxonomyMappingModel> getAddedTaxonomyMappings();
  
  public void setModifiedTaxonomyMappings(
      List<IConfigRuleTaxonomyMappingModel> modifiedTaxonomyMappings);
  
  public List<IConfigRuleTaxonomyMappingModel> getModifiedTaxonomyMappings();
  
  public void setDeletedTaxonomyMappings(List<String> deletedTaxonomyMappings);
  
  public List<String> getDeletedTaxonomyMappings();
  
  public void setPropertyCollectionMappings(
      List<IConfigRulePropertyMappingModel> propertyCollectionMappings);
  
  public List<IConfigRulePropertyMappingModel> getPropertyCollectionMappings();
  
  public List<String> getAddedPropertyCollectionIds();
  
  public void setAddedPropertyCollectionIds(List<String> addedPropertyCollectionIds);
  
  public List<String> getModifiedPropertyCollectionIds();
  
  public void setModifiedPropertyCollectionIds(List<String> modifiedPropertyCollectionIds);
  
  public List<String> getDeletedPropertyCollectionIds();
  
  public void setDeletedPropertyCollectionIds(List<String> deletedPropertyCollectionIds);
  
  public void setAddedAttributeMappings(
      List<IConfigRuleAttributeOutBoundMappingModel> addedAttributeMappings);
  
  public List<IConfigRuleAttributeOutBoundMappingModel> getAddedAttributeMappings();
  
  public void setModifiedAttributeMappings(
      List<IConfigRuleAttributeOutBoundMappingModel> modifiedAttributeMappings);
  
  public List<IConfigRuleAttributeOutBoundMappingModel> getModifiedAttributeMappings();
  
  public void setDeletedAttributeMappings(List<String> deletedAttributeMappings);
  
  public List<String> getDeletedAttributeMappings();
  
  public void setAddedTagMappings(List<IConfigRuleTagOutBoundMappingModel> addedTagMappings);
  
  public List<IConfigRuleTagOutBoundMappingModel> getAddedTagMappings();
  
  public void setModifiedTagMappings(List<IConfigRuleTagOutBoundMappingModel> modifiedTagMappings);
  
  public List<IConfigRuleTagOutBoundMappingModel> getModifiedTagMappings();
  
  public void setDeletedTagMappings(List<String> deletedTagMappings);
  
  public List<String> getDeletedTagMappings();
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public String getSelectedPropertyCollectionId();
  
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId);
  
  public List<String> getConfigRuleIdsForAttribute();
  
  public void setConfigRuleIdsForAttribute(List<String> configRuleIdsForAttribute);
  
  public List<String> getConfigRuleIdsForTag();
  
  public void setConfigRuleIdsForTag(List<String> configRuleIdsForTag);
  
  public List<IConfigRuleRelationshipMappingModel> getAddedRelationshipMappings();
  
  public void setAddedRelationshipMappings(List<IConfigRuleRelationshipMappingModel> addedRelationshipMappings);
  
  public List<IConfigRuleRelationshipMappingModel> getModifiedRelationshipMappings();
  
  public void setModifiedRelationshipMappings(List<IConfigRuleRelationshipMappingModel> modifiedRelationshipMappings);
  
  public List<String> getDeletedRelationshipMappings();
  
  public void setDeletedRelationshipMappings(List<String> deletedRelationshipMappings);
}
