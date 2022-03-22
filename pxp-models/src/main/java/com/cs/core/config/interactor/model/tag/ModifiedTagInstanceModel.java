package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.datarule.AbstractConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedTagInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = ModifiedTagInstanceModel.class)
public class ModifiedTagInstanceModel extends AbstractModifiedContentTagInstanceModel
    implements IModifiedTagInstanceModel {
  
  private static final long         serialVersionUID = 1L;
  protected IConflictingValueSource source;
  protected Boolean                 isConflictResolved;
  protected Long                    iid;
  
  public ModifiedTagInstanceModel(ITagInstance tagInstance)
  {
    super(tagInstance);
  }
  
  public ModifiedTagInstanceModel()
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
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public List<ITagConflictingValue> getConflictingValues()
  {
    return entity.getConflictingValues();
  }
  
  @JsonDeserialize(contentAs = TagConflictingValue.class)
  @Override
  public void setConflictingValues(List<ITagConflictingValue> conflictingValues)
  {
    entity.setConflictingValues(conflictingValues);
  }
  
  @Override
  public IConflictingValueSource getSource()
  {
    return source;
  }
  
  @Override
  @JsonDeserialize(as = AbstractConflictingValueSource.class)
  public void setSource(IConflictingValueSource source)
  {
    this.source = source;
  }
  
  @Override
  public Boolean getIsConflictResolved()
  {
    if (isConflictResolved == null) {
      isConflictResolved = true;
    }
    return isConflictResolved;
  }
  
  @Override
  public void setIsConflictResolved(Boolean isConflictResolved)
  {
    this.isConflictResolved = isConflictResolved;
  }
  
  @JsonIgnore
  @Override
  public Boolean getIsMatchAndMerge()
  {
    return ((ITagInstance) entity).getIsMatchAndMerge();
  }
  
  @JsonIgnore
  @Override
  public void setIsMatchAndMerge(Boolean isMatchAndMerge)
  {
    ((ITagInstance) entity).setIsMatchAndMerge(isMatchAndMerge);
  }
  
  @Override
  public String getKlassInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getVariantInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    // TODO Auto-generated method stub
    
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
