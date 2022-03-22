package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class OutBoundMappingModel extends AbstractMappingModel implements IOutBoundMappingModel {
  
  private static final long                                serialVersionUID  = 1L;
  protected IOutBoundMapping                               mapping;
  protected List<IConfigRuleAttributeOutBoundMappingModel> attributeMappings = new ArrayList<>();
  protected List<IConfigRuleTagOutBoundMappingModel>       tagMappings       = new ArrayList<>();
  protected List<IConfigRuleRelationshipMappingModel>      relationshipMappings = new ArrayList<>();
  protected String                                         code;
  protected String                                         mappingType;
  protected String                                         selectedPropertyCollectionId;
  protected List<String>                                   propertyCollectionIds;
  protected String                                         selectedContextId;
  protected List<String>                                   contextIds;
  
  
  public OutBoundMappingModel()
  {
    mapping = new OutBoundMapping();
  }
  
  public OutBoundMappingModel(IOutBoundMapping profile)
  {
    mapping = profile;
  }
  
  @Override
  public IOutBoundMapping getEntity()
  {
    return this.mapping;
  }
  
  @Override
  public void setPropertyCollectionIds(List<String> propertyCollectionIds)
  {
    this.propertyCollectionIds = propertyCollectionIds;
  }
  
  @Override
  public List<String> getPropertyCollectionIds()
  {
    return propertyCollectionIds;
  }
  
  public Boolean getIsAllClassesSelected()
  {
    return mapping.getIsAllClassesSelected();
  }
  
  public void setIsAllClassesSelected(Boolean isAllClassesSelected)
  {
    mapping.setIsAllClassesSelected(isAllClassesSelected);
  }
  
  public Boolean getIsAllTaxonomiesSelected()
  {
    return mapping.getIsAllTaxonomiesSelected();
  }
  
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected)
  {
    mapping.setIsAllTaxonomiesSelected(isAllTaxonomiesSelected);
  }
  
  public Boolean getIsAllPropertyCollectionSelected()
  {
    return mapping.getIsAllPropertyCollectionSelected();
  }
  
  public void setIsAllPropertyCollectionSelected(Boolean isAllPropertyCollectionSelected)
  {
    mapping.setIsAllPropertyCollectionSelected(isAllPropertyCollectionSelected);
  }
  
  public Boolean getIsAllRelationshipsSelected()
  {
    return mapping.getIsAllRelationshipsSelected();
  }
  
  public void setIsAllRelationshipsSelected(Boolean isAllRelationshipsSelected)
  {
    mapping.setIsAllRelationshipsSelected(isAllRelationshipsSelected);
  }
  
  @Override
  public String getCode()
  {
    return mapping.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    mapping.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return mapping.getId();
  }
  
  @Override
  public void setId(String id)
  {
    mapping.setId(id);
  }
  
  @Override
  public String getLabel()
  {
    return mapping.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    mapping.setLabel(label);
  }
  
  @Override
  public Long getVersionId()
  {
    return mapping.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    mapping.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return mapping.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    mapping.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    mapping.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return mapping.getLastModifiedBy();
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public Boolean getIsDefault()
  {
    return mapping.getIsDefault();
  }
  
  @Override
  public void setIsDefault(Boolean isDefault)
  {
    mapping.setIsDefault(isDefault);
    
  }
  
  @Override
  public String getType()
  {
    
    return mapping.getType();
  }
  
  @Override
  public void setType(String type)
  {
    this.mapping.setType(type);
  }
  
  @Override
  public String getIndexName()
  {
    
    return mapping.getIndexName();
  }
  
  @Override
  public void setIndexName(String indexName)
  {
    this.mapping.setIndexName(indexName);
  }
  
  @Override
  public void setMappingType(String mappingType)
  {
    mapping.setMappingType(mappingType);
  }
  
  @Override
  public String getMappingType()
  {
    return mapping.getMappingType();
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleAttributeOutBoundMappingModel.class)
  public void setAttributeMappings(List<IConfigRuleAttributeOutBoundMappingModel> attributeMappings)
  {
    this.attributeMappings = attributeMappings;
  }
  
  @Override
  public List<IConfigRuleAttributeOutBoundMappingModel> getAttributeMappings()
  {
    
    return attributeMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTagOutBoundMappingModel.class)
  public void setTagMappings(List<IConfigRuleTagOutBoundMappingModel> tagMappings)
  {
    this.tagMappings = tagMappings;
  }
  
  @Override
  public List<IConfigRuleTagOutBoundMappingModel> getTagMappings()
  {
    
    return tagMappings;
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
  public String getSelectedContextId()
  {
    return selectedContextId;
  }

  @Override
  public void setSelectedContextId(String selectedContextId)
  {
    this.selectedContextId = selectedContextId;
  }

  @Override
  public List<String> getContextIds()
  {
    return contextIds;
  }

  @Override
  public void setContextIds(List<String> contextIds)
  {
    this.contextIds = contextIds;
  }
  
  @Override
  public List<IConfigRuleRelationshipMappingModel> getRelationshipMappings()
  {
    return relationshipMappings;
  }

  @Override
  @JsonDeserialize(contentAs = ConfigRuleRelationshipMappingModel.class)
  public void setRelationshipMappings(List<IConfigRuleRelationshipMappingModel> relationshipMappings)
  {
    this.relationshipMappings = relationshipMappings;
  }

  @Override
  public Boolean getIsAllContextsSelected()
  {
    return mapping.getIsAllContextsSelected();
  }

  @Override
  public void setIsAllContextsSelected(Boolean isAllContextsSelected)
  {
    mapping.setIsAllContextsSelected(isAllContextsSelected);    
  }
}
