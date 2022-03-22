package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.ReferencedRelationshipProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class KlassRelationshipSide implements IKlassRelationshipSide {
  
  private static final long serialVersionUID            = 1L;
  
  protected String          cardinality                 = null;
  
  protected String          klassId                     = null;
  
  protected Boolean         isVisible                   = true;
  
  protected String          label                       = null;
  
  protected String          id                          = null;
  
  protected String          sourceCardinality           = null;
  
  protected String          targetRelationshipMappingId = null;
  
  protected String          relationshipMappingId       = null;
  
  protected String          targetType                  = null;
  
  protected Boolean         isOppositeVisible           = false;
  
  protected String          contextId;
  
  protected String          code;
  
  protected String          elementId;
  
  protected String          type;
  
  protected List<IReferencedRelationshipProperty> relationships       = new ArrayList<>();
  
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
  public String getCardinality()
  {
    return cardinality;
  }
  
  @Override
  public void setCardinality(String cardinality)
  {
    this.cardinality = cardinality;
  }
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
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
  public Boolean getIsVisible()
  {
    return isVisible;
  }
  
  @Override
  public void setIsVisible(Boolean isVisible)
  {
    this.isVisible = isVisible;
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
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  public String getSourceCardinality()
  {
    
    return sourceCardinality;
  }
  
  @Override
  public void setSourceCardinality(String sourceCardinality)
  {
    this.sourceCardinality = sourceCardinality;
  }
  
  @Override
  public String getTargetRelationshipMappingId()
  {
    
    return targetRelationshipMappingId;
  }
  
  @Override
  public void setTargetRelationshipMappingId(String targetRelationshipMappingId)
  {
    this.targetRelationshipMappingId = targetRelationshipMappingId;
  }
  
  @Override
  public String getTargetType()
  {
    
    return targetType;
  }
  
  @Override
  public void setTargetType(String targetType)
  {
    this.targetType = targetType;
  }
  
  @Override
  public String getRelationshipMappingId()
  {
    
    return relationshipMappingId;
  }
  
  @Override
  public void setRelationshipMappingId(String relationshipMappingId)
  {
    this.relationshipMappingId = relationshipMappingId;
  }
  
  @Override
  public Boolean getIsOppositeVisible()
  {
    
    return isOppositeVisible;
  }
  
  @Override
  public void setIsOppositeVisible(Boolean isOppositeVisible)
  {
    this.isOppositeVisible = isOppositeVisible;
  }
  
  @Override
  public List<IReferencedRelationshipProperty> getAttributes()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  @Override
  public void setAttributes(List<IReferencedRelationshipProperty> attributes)
  {
    // TODO Auto-generated method stubo
  }
  
  @Override
  public List<IReferencedRelationshipProperty> getTags()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  @Override
  public void setTags(List<IReferencedRelationshipProperty> tags)
  {
    // TODO Auto-generated method stub
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public String getElementId()
  {
    return elementId;
  }
  
  @Override
  public void setElementId(String elementId)
  {
    this.elementId = elementId;
  }

  @Override
  public List<IReferencedRelationshipProperty> getRelationships()
  {
    if(relationships == null) {
      relationships = new ArrayList<>();
    }
    return relationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  public void setRelationships(List<IReferencedRelationshipProperty> relationships)
  {
    this.relationships = relationships;
  }

  @Override
  public void setType(String type)
  {
    this.type = type;
  }

  @Override
  public String getType()
  {
    return type;
  }
}
