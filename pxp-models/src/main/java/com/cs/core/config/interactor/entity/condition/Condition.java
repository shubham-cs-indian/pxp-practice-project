package com.cs.core.config.interactor.entity.condition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class Condition implements ICondition {
  
  private static final long          serialVersionUID = 1L;
  
  protected String                   id;
  protected Long                     versionId;
  protected Long                     versionTimeStamp;
  protected String                   lastModifiedBy;
  protected String                   label;
  protected List<IConditionsEntitys> attributes;
  protected List<IConditionsEntitys> roles;
  protected List<IConditionsEntitys> tags;
  protected List<IConditionsEntitys> klasses;
  protected List<IConditionsEntitys> relationships;
  protected String                   code;
  
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
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimeStamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimeStamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
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
  public List<IConditionsEntitys> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<IConditionsEntitys>();
    }
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setAttributes(List<IConditionsEntitys> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IConditionsEntitys> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<>();
    }
    return roles;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setRoles(List<IConditionsEntitys> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public List<IConditionsEntitys> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<IConditionsEntitys>();
    }
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setTags(List<IConditionsEntitys> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IConditionsEntitys> getTypes()
  {
    if (tags == null) {
      klasses = new ArrayList<IConditionsEntitys>();
    }
    return klasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setTypes(List<IConditionsEntitys> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public List<IConditionsEntitys> getRelationships()
  {
    if (relationships == null) {
      relationships = new ArrayList<IConditionsEntitys>();
    }
    return relationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setRelationships(List<IConditionsEntitys> relationships)
  {
    this.relationships = relationships;
  }
}
