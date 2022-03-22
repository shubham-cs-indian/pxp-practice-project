package com.cs.core.config.interactor.entity.state;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;

import java.util.List;

public interface IState extends IEntity {
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<? extends IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<? extends IContentAttributeInstance> attributes);
  
  public List<? extends ITagInstance> getTags();
  
  public void setTags(List<? extends ITagInstance> tags);
  
  public List<? extends IRoleInstance> getRoles();
  
  public void setRoles(List<? extends IRoleInstance> roles);
}
