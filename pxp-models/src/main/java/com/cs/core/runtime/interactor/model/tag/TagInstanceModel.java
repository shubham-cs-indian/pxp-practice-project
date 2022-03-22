package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractPropertyInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class TagInstanceModel extends AbstractPropertyInstanceModel implements ITagInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            iid;
  
  public TagInstanceModel(ITagInstance tagInstance)
  {
    super(tagInstance);
  }
  
  public TagInstanceModel()
  {
    super(new TagInstance());
  }
  
  @Override
  public Boolean getIsMandatoryViolated()
  {
    
    return ((ITagInstance) entity).getIsMandatoryViolated();
  }
  
  @Override
  public void setIsMandatoryViolated(Boolean isMandatoryViolated)
  {
    ((ITagInstance) entity).setIsMandatoryViolated(isMandatoryViolated);
  }
  
  @Override
  public Boolean getIsShouldViolated()
  {
    
    return ((ITagInstance) entity).getIsShouldViolated();
  }
  
  @Override
  public void setIsShouldViolated(Boolean isShouldViolated)
  {
    ((ITagInstance) entity).setIsShouldViolated(isShouldViolated);
  }
  
  @Override
  public List<ITagInstanceValue> getTagValues()
  {
    return ((ITagInstance) entity).getTagValues();
  }
  
  @Override
  public void setTagValues(List<ITagInstanceValue> tagValues)
  {
    ((ITagInstance) entity).setTagValues(tagValues);
  }
  
  @Override
  public String getTagId()
  {
    return ((ITagInstance) entity).getTagId();
  }
  
  @Override
  public void setTagId(String tagId)
  {
    ((ITagInstance) entity).setTagId(tagId);
  }
  
  @Override
  public List<? extends ITagInstance> getTags()
  {
    return ((ITagInstance) entity).getTags();
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    ((ITagInstance) entity).setTags(tags);
  }
  
  @Override
  public List<ITagConflictingValue> getConflictingValues()
  {
    return ((ITagInstance) entity).getConflictingValues();
  }
  
  @Override
  public void setConflictingValues(List<ITagConflictingValue> conflictingValues)
  {
    ((ITagInstance) entity).setConflictingValues(conflictingValues);
  }
  
  @Override
  public Boolean getIsMatchAndMerge()
  {
    return ((ITagInstance) entity).getIsMatchAndMerge();
  }
  
  @Override
  public void setIsMatchAndMerge(Boolean isMatchAndMerge)
  {
    ((ITagInstance) entity).setIsMatchAndMerge(isMatchAndMerge);
  }
  
  @Override
  public String getContextInstanceId()
  {
    return ((ITagInstance) entity).getContextInstanceId();
  }
  
  @Override
  public void setContextInstanceId(String contextInstanceId)
  {
    ((ITagInstance) entity).setContextInstanceId(contextInstanceId);
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return ((ITagInstance) entity).getKlassInstanceId();
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    ((ITagInstance) entity).setKlassInstanceId(klassInstanceId);
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return ((ITagInstance) entity).getVariantInstanceId();
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    ((ITagInstance) entity).setVariantInstanceId(variantInstanceId);
  }
  
  @JsonIgnore
  @Override
  public Boolean getIsConflictResolved()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setIsConflictResolved(Boolean isConflictResolved)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getIid()
  {
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
