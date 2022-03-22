package com.cs.core.config.interactor.entity.action;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.state.IState;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;

import java.util.List;

public interface IParameters extends IEntity {
  
  public List<? extends IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<? extends IContentAttributeInstance> attributes);
  
  public List<? extends ITagInstance> getTags();
  
  public void setTags(List<? extends ITagInstance> tags);
  
  public List<? extends IRoleInstance> getRoles();
  
  public void setRoles(List<? extends IRoleInstance> roles);
  
  public List<? extends IState> getStates();
  
  public void setStates(List<? extends IState> states);
  
  public List<? extends IUser> getUsers();
  
  public void setUsers(List<? extends IUser> users);
  
  /*public List<? extends IKlass> getTypes();
  
  public void setTypes(List<? extends IKlass> types);*/
}
