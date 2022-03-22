package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.concrete.tagtype.TagType;
import com.cs.core.config.interactor.entity.concrete.tagtype.TagValue;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.tag.ITagType;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class TagTypeModel implements ITagTypeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ITagType        entity;
  
  public TagTypeModel(ITagType entity)
  {
    this.entity = entity;
  }
  
  public TagTypeModel()
  {
    entity = new TagType();
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public Boolean getIsRange()
  {
    return entity.getIsRange();
  }
  
  @Override
  public void setIsRange(Boolean isRange)
  {
    entity.setIsRange(isRange);
  }
  
  @Override
  public List<ITagValue> getTagValues()
  {
    return entity.getTagValues();
  }
  
  @JsonDeserialize(contentAs = TagValue.class)
  @Override
  public void setTagValues(List<ITagValue> tagValues)
  {
    entity.setTagValues(tagValues);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
}
