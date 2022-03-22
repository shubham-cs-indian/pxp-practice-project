package com.cs.core.config.interactor.model.mapping;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveMappingModel implements ISaveMappingModel {
  
  private static final long                        serialVersionUID = 1L;
  protected String                                    id;
  protected String                                    label;
  protected String                                    type;
  protected Boolean                                   isDefault;
  protected String                                    indexName;
  protected List<IConfigRuleAttributeMappingModel>    addedAttributeMappings;
  protected List<IConfigRuleAttributeMappingModel>    modifiedAttributeMappings;
  protected List<String>                              deletedAttributeMappings;
  protected List<IConfigRuleTagMappingModel>          addedTagMappings;
  protected List<IConfigRuleTagMappingModel>          modifiedTagMappings;
  protected List<String>                              deletedTagMappings;
  protected List<IConfigRuleClassMappingModel>        addedClassMappings;
  protected List<IConfigRuleClassMappingModel>        modifiedClassMappings;
  protected List<String>                              deletedClassMappings;
  protected List<IConfigRuleTaxonomyMappingModel>     addedTaxonomyMappings;
  protected List<IConfigRuleTaxonomyMappingModel>     modifiedTaxonomyMappings;
  protected List<String>                              deletedTaxonomyMappings;
  protected Boolean                                   isRuntimeMappingEnabled;
  protected String                                    code;
  protected String                                    mappingType;
  protected List<IConfigRulePropertyMappingModel>     propertyCollectionMappings;
  protected List<String>                              addedPropertyCollectionIds;
  protected List<String>                              modifiedPropertyCollectionIds;
  protected List<String>                              deletedPropertyCollectionIds;
  protected String                                    selectedPropertyCollectionId;
  protected String                                    tabId;
  protected List<String>                              configRuleIdsForAttribute    = new ArrayList<String>();
  protected List<String>                              configRuleIdsForTag          = new ArrayList<String>();
  protected List<IConfigRuleRelationshipMappingModel> addedRelationshipMappings    = new ArrayList<>();
  protected List<IConfigRuleRelationshipMappingModel> modifiedRelationshipMappings = new ArrayList<>();
  protected List<String>                              deletedRelationshipMappings  = new ArrayList<>();
  
  protected List<String>                              addedContextMappings;
  protected List<String>                              modifiedContextMappings;
  protected List<String>                              deletedContextMappings;
  protected String                                    selectedContextId;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getId()
  {
    
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLabel()
  {
    
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Boolean getIsDefault()
  {
    
    return isDefault;
  }
  
  @Override
  public void setIsDefault(Boolean isDefault)
  {
    this.isDefault = isDefault;
  }
  
  @Override
  public List<IConfigRuleAttributeMappingModel> getAddedAttributeMappings()
  {
    
    return addedAttributeMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleAttributeMappingModel.class)
  public void setAddedAttributeMappings(
      List<IConfigRuleAttributeMappingModel> addedAttributeMappings)
  {
    this.addedAttributeMappings = addedAttributeMappings;
  }
  
  @Override
  public List<IConfigRuleAttributeMappingModel> getModifiedAttributeMappings()
  {
    
    return modifiedAttributeMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleAttributeMappingModel.class)
  public void setModifiedAttributeMappings(
      List<IConfigRuleAttributeMappingModel> modifiedAttributeMappings)
  {
    this.modifiedAttributeMappings = modifiedAttributeMappings;
  }
  
  @Override
  public List<String> getDeletedAttributeMappings()
  {
    
    return deletedAttributeMappings;
  }
  
  @Override
  public void setDeletedAttributeMappings(List<String> deletedAttributeMappings)
  {
    this.deletedAttributeMappings = deletedAttributeMappings;
  }
  
  @Override
  public List<IConfigRuleTagMappingModel> getAddedTagMappings()
  {
    
    return addedTagMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTagMappingModel.class)
  public void setAddedTagMappings(List<IConfigRuleTagMappingModel> addedTagMappings)
  {
    this.addedTagMappings = addedTagMappings;
  }
  
  @Override
  public List<IConfigRuleTagMappingModel> getModifiedTagMappings()
  {
    
    return modifiedTagMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTagMappingModel.class)
  public void setModifiedTagMappings(List<IConfigRuleTagMappingModel> modifiedTagMappings)
  {
    this.modifiedTagMappings = modifiedTagMappings;
  }
  
  @Override
  public List<String> getDeletedTagMappings()
  {
    
    return deletedTagMappings;
  }
  
  @Override
  public void setDeletedTagMappings(List<String> deletedTagMappings)
  {
    this.deletedTagMappings = deletedTagMappings;
  }
  
  @Override
  public String getType()
  {
    
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getIndexName()
  {
    
    return indexName;
  }
  
  @Override
  public void setIndexName(String indexName)
  {
    this.indexName = indexName;
  }
  
  @Override
  public List<IConfigRuleClassMappingModel> getAddedClassMappings()
  {
    
    return addedClassMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleClassMappingModel.class)
  public void setAddedClassMappings(List<IConfigRuleClassMappingModel> addedClassMappings)
  {
    this.addedClassMappings = addedClassMappings;
  }
  
  @Override
  public List<IConfigRuleClassMappingModel> getModifiedClassMappings()
  {
    
    return modifiedClassMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleClassMappingModel.class)
  public void setModifiedClassMappings(List<IConfigRuleClassMappingModel> modifiedClassMappings)
  {
    this.modifiedClassMappings = modifiedClassMappings;
  }
  
  @Override
  public List<String> getDeletedClassMappings()
  {
    
    return deletedClassMappings;
  }
  
  @Override
  public void setDeletedClassMappings(List<String> deletedClassMappings)
  {
    this.deletedClassMappings = deletedClassMappings;
  }
  
  @Override
  public List<IConfigRuleTaxonomyMappingModel> getAddedTaxonomyMappings()
  {
    
    return addedTaxonomyMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTaxonomyMappingModel.class)
  public void setAddedTaxonomyMappings(List<IConfigRuleTaxonomyMappingModel> addedTaxonomyMappings)
  {
    this.addedTaxonomyMappings = addedTaxonomyMappings;
  }
  
  @Override
  public List<IConfigRuleTaxonomyMappingModel> getModifiedTaxonomyMappings()
  {
    
    return modifiedTaxonomyMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTaxonomyMappingModel.class)
  public void setModifiedTaxonomyMappings(
      List<IConfigRuleTaxonomyMappingModel> modifiedTaxonomyMappings)
  {
    this.modifiedTaxonomyMappings = modifiedTaxonomyMappings;
  }
  
  @Override
  public List<String> getDeletedTaxonomyMappings()
  {
    
    return deletedTaxonomyMappings;
  }
  
  @Override
  public void setDeletedTaxonomyMappings(List<String> deletedTaxonomyMappings)
  {
    this.deletedTaxonomyMappings = deletedTaxonomyMappings;
  }
  
  @Override
  public void setMappingType(String mappingType)
  {
    this.mappingType = mappingType;
  }
  
  @Override
  public String getMappingType()
  {
    return this.mappingType;
  }
  @Override
  public void setPropertyCollectionMappings(
      List<IConfigRulePropertyMappingModel> propertyCollectionMappings)
  {
    this.propertyCollectionMappings = propertyCollectionMappings;
  }
  
  @Override
  public List<IConfigRulePropertyMappingModel> getPropertyCollectionMappings()
  {
    return this.propertyCollectionMappings;
  }
  
  @Override
  public List<String> getAddedPropertyCollectionIds()
  {
    return addedPropertyCollectionIds;
  }
  
  @Override
  public void setAddedPropertyCollectionIds(List<String> addedPropertyCollectionIds)
  {
    this.addedPropertyCollectionIds = addedPropertyCollectionIds;
  }
  
  @Override
  public List<String> getModifiedPropertyCollectionIds()
  {
    return modifiedPropertyCollectionIds;
  }
  
  @Override
  public void setModifiedPropertyCollectionIds(List<String> modifiedPropertyCollectionIds)
  {
    this.modifiedPropertyCollectionIds = modifiedPropertyCollectionIds;
  }
  
  @Override
  public List<String> getDeletedPropertyCollectionIds()
  {
    return deletedPropertyCollectionIds;
  }
  
  @Override
  public void setDeletedPropertyCollectionIds(List<String> deletedPropertyCollectionIds)
  {
    this.deletedPropertyCollectionIds = deletedPropertyCollectionIds;
  }
  @Override
  public String getSelectedPropertyCollectionId()
  {
    return selectedPropertyCollectionId;
  }
  
  @Override
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId)
  {
    this.selectedPropertyCollectionId = selectedPropertyCollectionId;
  }
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  @Override
  public List<String> getConfigRuleIdsForAttribute()
  {
    return configRuleIdsForAttribute;
  }
  
  @Override
  public void setConfigRuleIdsForAttribute(List<String> configRuleIdsForAttribute)
  {
    this.configRuleIdsForAttribute = configRuleIdsForAttribute;
  }
  
  @Override
  public List<String> getConfigRuleIdsForTag()
  {
    return configRuleIdsForTag;
  }
  
  @Override
  public void setConfigRuleIdsForTag(List<String> configRuleIdsForTag)
  {
    this.configRuleIdsForTag = configRuleIdsForTag;
  }
 
  @Override
  public List<IConfigRuleRelationshipMappingModel> getAddedRelationshipMappings()
  {
    
    return addedRelationshipMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleRelationshipMappingModel.class)
  public void setAddedRelationshipMappings(List<IConfigRuleRelationshipMappingModel> addedRelationshipMappings)
  {
    this.addedRelationshipMappings = addedRelationshipMappings;
  }
  
  @Override
  public List<IConfigRuleRelationshipMappingModel> getModifiedRelationshipMappings()
  {
    
    return modifiedRelationshipMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleRelationshipMappingModel.class)
  public void setModifiedRelationshipMappings(
      List<IConfigRuleRelationshipMappingModel> modifiedRelationshipMappings)
  {
    this.modifiedRelationshipMappings = modifiedRelationshipMappings;
  }
  
  @Override
  public List<String> getDeletedRelationshipMappings()
  {
    
    return deletedRelationshipMappings;
  }
  
  @Override
  public void setDeletedRelationshipMappings(List<String> deletedRelationshipMappings)
  {
    this.deletedRelationshipMappings = deletedRelationshipMappings;
  }

  @Override
  public List<String> getAddedContextMappings()
  {
    return addedContextMappings;
  }

  @Override
  public void setAddedContextMappings(List<String> addedContextMappings)
  {
    this.addedContextMappings = addedContextMappings;
  }

  @Override
  public List<String> getModifiedContextMappings()
  {
    return modifiedContextMappings;
  }

  @Override
  public void setModifiedContextMappings(List<String> modifiedContextMappings)
  {
    this.modifiedContextMappings = modifiedContextMappings;
  }

  @Override
  public List<String> getDeletedContextMappings()
  {
    return deletedContextMappings;
  }

  @Override
  public void setDeletedContextMappings(List<String> deletedContextMappings)
  {
    this.deletedContextMappings = deletedContextMappings;
  }

  @Override
  public String getSelectedContextId()
  {
    return selectedContextId;
  }

  @Override
  public void setSelectedContextId(String selectedContextId)
  {
    this.selectedContextId = selectedContextId;
  }
}
