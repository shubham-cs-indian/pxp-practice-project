package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@SuppressWarnings("unchecked")
public class TaskInstanceInformationModel implements ITaskInstanceInformationModel {
  
  private static final long       serialVersionUID = 1L;
  protected String                id;
  protected List<String>          types;
  protected String                icon;
  protected Long                  overDueDate;
  protected List<ILinkedEntities> linkedEntities;
  protected ITagInstance          priority;
  protected String                longDescription;
  protected IRoleInstance         responsible;
  protected ITagInstance          status;
  protected List<IRoleInstance>   roles            = new ArrayList<>();
  protected String                name;
  protected Long                  dueDate;
  protected Boolean               isCamundaCreated;
  
  @Override
  public IRoleInstance getResponsible()
  {
    return responsible;
  }
  
  @Override
  public void setResponsible(IRoleInstance responsible)
  {
    this.responsible = responsible;
  }
  
  @Override
  public String getLongDescription()
  {
    return longDescription;
  }
  
  @Override
  public void setLongDescription(String longDescription)
  {
    this.longDescription = longDescription;
  }
  
  @Override
  public List<ILinkedEntities> getLinkedEntities()
  {
    if(this.linkedEntities == null) {
      this.linkedEntities = new ArrayList<>();
    }
    return linkedEntities;
  }
  
  @JsonDeserialize(contentAs = LinkedEntities.class)
  @Override
  public void setLinkedEntities(List<ILinkedEntities> linkedEntities)
  {
    this.linkedEntities = linkedEntities;
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
  public List<String> getTypes()
  {
    if(this.types == null) {
      this.types = new ArrayList<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public Long getOverDueDate()
  {
    return overDueDate;
  }
  
  @Override
  public void setOverDueDate(Long overDueDate)
  {
    this.overDueDate = overDueDate;
  }
  
  @Override
  public ITagInstance getPriority()
  {
    return priority;
  }
  
  @Override
  public void setPriority(ITagInstance priority)
  {
    this.priority = priority;
  }
  
  @Override
  public ITagInstance getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(ITagInstance status)
  {
    this.status = status;
  }
  
  @Override
  public List<IRoleInstance> getRoles()
  {
    return roles;
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    this.roles = (List<IRoleInstance>) roles;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public Long getDueDate()
  {
    return dueDate;
  }
  
  @Override
  public void setDueDate(Long dueDate)
  {
    this.dueDate = dueDate;
  }
  
  @Override
  public Boolean getIsCamundaCreated()
  {
    return isCamundaCreated;
  }
  
  @Override
  public void setIsCamundaCreated(Boolean isCamundaCreated)
  {
    this.isCamundaCreated = isCamundaCreated;
  }
}
