package com.cs.core.config.interactor.model.action;

import com.cs.core.config.interactor.entity.state.IState;
import com.cs.core.config.interactor.entity.state.State;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ParametersModel implements IParametersModel {
  
  protected String                                    id;
  protected Long                                      versionId;
  protected Long                                      versionTimestamp;
  protected String                                    lastModifiedBy;
  protected List<? extends IContentAttributeInstance> attributes;
  protected List<? extends ITagInstance>              tags;
  protected List<? extends IRoleInstance>             roles;
  protected List<? extends IState>                    states;
  protected List<? extends IUser>                     users;
  
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
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
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
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @JsonDeserialize(contentAs = AttributeInstance.class)
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<? extends ITagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<? extends IRoleInstance> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<>();
    }
    return roles;
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public List<? extends IState> getStates()
  {
    if (states == null) {
      states = new ArrayList<>();
    }
    return states;
  }
  
  @JsonDeserialize(contentAs = State.class)
  @Override
  public void setStates(List<? extends IState> states)
  {
    this.states = states;
  }
  
  @Override
  public List<? extends IUser> getUsers()
  {
    if (users == null) {
      users = new ArrayList<>();
    }
    return users;
  }
  
  @JsonDeserialize(contentAs = User.class)
  @Override
  public void setUsers(List<? extends IUser> users)
  {
    this.users = users;
  }
}
