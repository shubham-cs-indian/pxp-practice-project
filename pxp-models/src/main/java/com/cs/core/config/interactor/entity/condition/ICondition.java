package com.cs.core.config.interactor.entity.condition;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface ICondition extends IEntity {
  
  public static final String CODE = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IConditionsEntitys> getAttributes();
  
  public void setAttributes(List<IConditionsEntitys> attributes);
  
  public List<IConditionsEntitys> getTags();
  
  public void setTags(List<IConditionsEntitys> tags);
  
  public List<IConditionsEntitys> getRoles();
  
  public void setRoles(List<IConditionsEntitys> roles);
  
  public List<IConditionsEntitys> getTypes();
  
  public void setTypes(List<IConditionsEntitys> klasses);
  
  public List<IConditionsEntitys> getRelationships();
  
  public void setRelationships(List<IConditionsEntitys> relationships);
}
