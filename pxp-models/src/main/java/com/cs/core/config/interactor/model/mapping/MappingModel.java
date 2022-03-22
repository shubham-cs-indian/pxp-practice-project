package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.entity.mapping.Mapping;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class MappingModel extends AbstractMappingModel implements IMappingModel {
  
  private static final long                        serialVersionUID  = 1L;
  
  protected IMapping                               mapping;
  // protected List<IConfigRuleTagMappingModel> configRuleMappings = new
  // ArrayList<>();
  protected List<IConfigRuleAttributeMappingModel> attributeMappings = new ArrayList<>();
  protected List<IConfigRuleTagMappingModel>       tagMappings       = new ArrayList<>();
  protected String                                 code;
  protected String                                 mappingType;
  protected String                                 selectedPropertyCollectionId;
  protected List<String>                           propertyCollectionIds;
  protected List<IConfigRuleRelationshipMappingModel> relationshipMappings = new ArrayList<>();
  protected List<String>                           contextIds = new ArrayList<>();
  protected String                                 selectedContextId;

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
  
  public MappingModel()
  {
    this.mapping = new Mapping();
  }
  
  public MappingModel(IMapping profile)
  {
    this.mapping = profile;
  }
  
  @Override
  public IEntity getEntity()
  {
    return mapping;
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
  
  /*@Override
  @JsonDeserialize(contentAs = ConfigRuleTagMappingModel.class)
  public void setConfigRuleMappings(List<IConfigRuleTagMappingModel> configRuleTagMappings)
  {
    this.configRuleMappings = configRuleTagMappings;
  }
  
  @Override
  public List<IConfigRuleTagMappingModel> getConfigRuleMappings()
  {
    
    return configRuleMappings;
  }*/
  
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
  @JsonDeserialize(contentAs = ConfigRuleTaxonomyMappingModel.class)
  public void setAttributeMappings(List<IConfigRuleAttributeMappingModel> attributeMappings)
  {
    this.attributeMappings = attributeMappings;
  }
  
  @Override
  public List<IConfigRuleAttributeMappingModel> getAttributeMappings()
  {
    
    return attributeMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTagMappingModel.class)
  public void setTagMappings(List<IConfigRuleTagMappingModel> tagMappings)
  {
    this.tagMappings = tagMappings;
  }
  
  @Override
  public List<IConfigRuleTagMappingModel> getTagMappings()
  {
    
    return tagMappings;
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

  
  public String getSelectedPropertyCollectionId()
  {
    return selectedPropertyCollectionId;
  }

  
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId)
  {
    this.selectedPropertyCollectionId = selectedPropertyCollectionId;
  }

  
  public List<String> getPropertyCollectionIds()
  {
    return propertyCollectionIds;
  }

  
  public void setPropertyCollectionIds(List<String> propertyCollectionIds)
  {
    this.propertyCollectionIds = propertyCollectionIds;
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
