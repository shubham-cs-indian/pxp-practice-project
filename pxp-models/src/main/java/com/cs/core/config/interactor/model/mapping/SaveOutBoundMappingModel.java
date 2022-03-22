package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SaveOutBoundMappingModel implements ISaveOutBoundMappingModel {
  
  private static final long                                serialVersionUID                = 1L;
  protected String                                         id;
  protected String                                         label;
  protected String                                         type;
  protected Boolean                                        isRuntimeMappingEnabled;
  protected Boolean                                        isDefault;
  protected String                                         indexName;
  protected String                                         code;
  protected String                                         mappingType;
  protected List<IConfigRuleAttributeOutBoundMappingModel> addedAttributeMappings;
  protected List<IConfigRuleAttributeOutBoundMappingModel> modifiedAttributeMappings;
  protected List<String>                                   deletedAttributeMappings;
  protected List<IConfigRuleTagOutBoundMappingModel>       addedTagMappings;
  protected List<IConfigRuleTagOutBoundMappingModel>       modifiedTagMappings;
  protected List<String>                                   deletedTagMappings;
  protected List<IConfigRulePropertyMappingModel>          propertyCollectionMappings;
  protected List<String>                                   addedPropertyCollectionIds;
  protected List<String>                                   modifiedPropertyCollectionIds;
  protected List<String>                                   deletedPropertyCollectionIds;
  protected String                                         selectedPropertyCollectionId;
  protected String                                         tabId;
  protected List<String>                                   configRuleIdsForAttribute       = new ArrayList<String>();
  protected List<String>                                   configRuleIdsForTag             = new ArrayList<String>();
  protected List<IConfigRuleClassMappingModel>             addedClassMappings;
  protected List<IConfigRuleClassMappingModel>             modifiedClassMappings;
  protected List<String>                                   deletedClassMappings;
  protected List<IConfigRuleTaxonomyMappingModel>          addedTaxonomyMappings;
  protected List<IConfigRuleTaxonomyMappingModel>          modifiedTaxonomyMappings;
  protected List<String>                                   deletedTaxonomyMappings;
  protected List<IConfigRuleRelationshipMappingModel>      addedRelationshipMappings       = new ArrayList<>();
  protected List<IConfigRuleRelationshipMappingModel>      modifiedRelationshipMappings    = new ArrayList<>();
  protected List<String>                                   deletedRelationshipMappings     = new ArrayList<>();
  protected IOutBoundMapping                               outboundMapping;
  
  public SaveOutBoundMappingModel()
  {
    outboundMapping = new OutBoundMapping();
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleClassMappingModel.class)
  public void setAddedClassMappings(List<IConfigRuleClassMappingModel> addedClassMappings)
  {
    this.addedClassMappings = addedClassMappings;
  }
  
  @Override
  public List<IConfigRuleClassMappingModel> getAddedClassMappings()
  {
    
    return addedClassMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleClassMappingModel.class)
  public void setModifiedClassMappings(List<IConfigRuleClassMappingModel> modifiedClassMappings)
  {
    this.modifiedClassMappings = modifiedClassMappings;
  }
  
  @Override
  public List<IConfigRuleClassMappingModel> getModifiedClassMappings()
  {
    
    return modifiedClassMappings;
  }
  
  @Override
  public void setDeletedClassMappings(List<String> deletedClassMappings)
  {
    this.deletedClassMappings = deletedClassMappings;
  }
  
  @Override
  public List<String> getDeletedClassMappings()
  {
    
    return deletedClassMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTaxonomyMappingModel.class)
  public void setAddedTaxonomyMappings(List<IConfigRuleTaxonomyMappingModel> addedTaxonomyMappings)
  {
    this.addedTaxonomyMappings = addedTaxonomyMappings;
  }
  
  @Override
  public List<IConfigRuleTaxonomyMappingModel> getAddedTaxonomyMappings()
  {
    
    return addedTaxonomyMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTaxonomyMappingModel.class)
  public void setModifiedTaxonomyMappings(
      List<IConfigRuleTaxonomyMappingModel> modifiedTaxonomyMappings)
  {
    this.modifiedTaxonomyMappings = modifiedTaxonomyMappings;
  }
  
  @Override
  public List<IConfigRuleTaxonomyMappingModel> getModifiedTaxonomyMappings()
  {
    
    return modifiedTaxonomyMappings;
  }
  
  @Override
  public void setDeletedTaxonomyMappings(List<String> deletedTaxonomyMappings)
  {
    this.deletedTaxonomyMappings = deletedTaxonomyMappings;
  }
  
  @Override
  public List<String> getDeletedTaxonomyMappings()
  {
    
    return deletedTaxonomyMappings;
  }
  
  @Override
  public Boolean getIsAllClassesSelected()
  {
    return outboundMapping.getIsAllClassesSelected();
  }
  
  @Override
  public void setIsAllClassesSelected(Boolean isAllClassesSelected)
  {
    outboundMapping.setIsAllClassesSelected(isAllClassesSelected);
  }
  
  @Override
  public Boolean getIsAllTaxonomiesSelected()
  {
    return this.outboundMapping.getIsAllTaxonomiesSelected();
  }
  
  @Override
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected)
  {
    outboundMapping.setIsAllTaxonomiesSelected(isAllTaxonomiesSelected);
  }
  
  @Override
  public Boolean getIsAllPropertyCollectionSelected()
  {
    return outboundMapping.getIsAllPropertyCollectionSelected();
  }
  
  @Override
  public void setIsAllPropertyCollectionSelected(Boolean isAllPropertyCollectionSelected)
  {
    outboundMapping.setIsAllPropertyCollectionSelected(isAllPropertyCollectionSelected);
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
  public List<IConfigRuleAttributeOutBoundMappingModel> getAddedAttributeMappings()
  {
    return addedAttributeMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleAttributeOutBoundMappingModel.class)
  public void setAddedAttributeMappings(
      List<IConfigRuleAttributeOutBoundMappingModel> addedAttributeMappings)
  {
    this.addedAttributeMappings = addedAttributeMappings;
  }
  
  @Override
  public List<IConfigRuleAttributeOutBoundMappingModel> getModifiedAttributeMappings()
  {
    return modifiedAttributeMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleAttributeOutBoundMappingModel.class)
  public void setModifiedAttributeMappings(
      List<IConfigRuleAttributeOutBoundMappingModel> modifiedAttributeMappings)
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
  public List<IConfigRuleTagOutBoundMappingModel> getAddedTagMappings()
  {
    return addedTagMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTagOutBoundMappingModel.class)
  public void setAddedTagMappings(List<IConfigRuleTagOutBoundMappingModel> addedTagMappings)
  {
    this.addedTagMappings = addedTagMappings;
  }
  
  @Override
  public List<IConfigRuleTagOutBoundMappingModel> getModifiedTagMappings()
  {
    return modifiedTagMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTagOutBoundMappingModel.class)
  public void setModifiedTagMappings(List<IConfigRuleTagOutBoundMappingModel> modifiedTagMappings)
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
  public void setLastModifiedBy(String lastModifiedBy)
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
  public void setMappingType(String mappingType)
  {
    this.mappingType = mappingType;
  }
  
  @Override
  public String getMappingType()
  {
    
    return this.mappingType;
  }
  
  public Boolean getIsRuntimeMappingEnabled()
  {
    return isRuntimeMappingEnabled;
  }
  
  public void setIsRuntimeMappingEnabled(Boolean isRuntimeMappingEnabled)
  {
    this.isRuntimeMappingEnabled = isRuntimeMappingEnabled;
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
  public Boolean getIsAllRelationshipsSelected()
  {
    return outboundMapping.getIsAllRelationshipsSelected();
  }

  @Override
  public void setIsAllRelationshipsSelected(Boolean isAllRelationshipsSelected)
  {
    outboundMapping.setIsAllRelationshipsSelected(isAllRelationshipsSelected);
  }

  @Override
  public Boolean getIsAllContextsSelected()
  {
    return outboundMapping.getIsAllContextsSelected();
  }

  @Override
  public void setIsAllContextsSelected(Boolean isAllContextsSelected)
  {
    outboundMapping.setIsAllContextsSelected(isAllContextsSelected);
  }
}
