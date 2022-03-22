package com.cs.core.config.interactor.model.condition;

import com.cs.core.config.interactor.entity.condition.Condition;
import com.cs.core.config.interactor.entity.condition.ConditionsEntitys;
import com.cs.core.config.interactor.entity.condition.ICondition;
import com.cs.core.config.interactor.entity.condition.IConditionsEntitys;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class ConditionModel implements IConditionModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ICondition      entity;
  
  public ConditionModel()
  {
    entity = new Condition();
  }
  
  public ConditionModel(ICondition condition)
  {
    this.entity = condition;
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
  public List<IConditionsEntitys> getAttributes()
  {
    return entity.getAttributes();
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setAttributes(List<IConditionsEntitys> attributes)
  {
    entity.setAttributes(attributes);
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
  public List<IConditionsEntitys> getTags()
  {
    return entity.getTags();
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setTags(List<IConditionsEntitys> tags)
  {
    entity.setTags(tags);
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
  
  @Override
  public List<IConditionsEntitys> getRoles()
  {
    return entity.getRoles();
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setRoles(List<IConditionsEntitys> roles)
  {
    entity.setRoles(roles);
  }
  
  @Override
  public List<IConditionsEntitys> getTypes()
  {
    return entity.getTypes();
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setTypes(List<IConditionsEntitys> klasses)
  {
    entity.setTypes(klasses);
  }
  
  @Override
  public List<IConditionsEntitys> getRelationships()
  {
    return entity.getRelationships();
  }
  
  @Override
  @JsonDeserialize(contentAs = ConditionsEntitys.class)
  public void setRelationships(List<IConditionsEntitys> relationships)
  {
    entity.setRelationships(relationships);
  }
}
