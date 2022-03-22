package com.cs.core.config.interactor.entity.goldenrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class MergeEffect implements IMergeEffect {
  
  private static final long        serialVersionUID = 1L;
  protected List<IMergeEffectType> attributes;
  protected List<IMergeEffectType> tags;
  protected List<IMergeEffectType> relationships;
  protected List<IMergeEffectType> natureRelationships;
  
  @Override
  public List<IMergeEffectType> getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = MergeEffectType.class)
  public void setAttributes(List<IMergeEffectType> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IMergeEffectType> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = MergeEffectType.class)
  public void setTags(List<IMergeEffectType> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IMergeEffectType> getNatureRelationships()
  {
    return natureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = MergeEffectType.class)
  public void setNatureRelationships(List<IMergeEffectType> natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
  @Override
  public List<IMergeEffectType> getRelationships()
  {
    return relationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = MergeEffectType.class)
  public void setRelationships(List<IMergeEffectType> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public String getCode()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setCode(String code)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
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
}
