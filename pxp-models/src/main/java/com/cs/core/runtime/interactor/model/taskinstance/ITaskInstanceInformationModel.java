package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITaskInstanceInformationModel extends IModel {
  
  public static final String ID                 = "id";
  public static final String TYPES              = "types";
  public static final String ICON               = "icon";
  public static final String OVERDUEDATE        = "overDueDate";
  public static final String LINKED_ENTITIES    = "linkedEntities";
  public static final String PRIORITY           = "priority";
  public static final String LONG_DESCRIPTION   = "longDescription";
  public static final String RESPONSIBLE        = "responsible";
  public static final String STATUS             = "status";
  public static final String ROLES              = "roles";
  public static final String NAME               = "name";
  public static final String DUE_DATE           = "dueDate";
  public static final String IS_CAMUNDA_CREATED = "isCamundaCreated";
  
  public String getId();
  
  public void setId(String id);
  
  public String getName();
  
  public void setName(String name);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public Long getOverDueDate();
  
  public void setOverDueDate(Long overDueDate);
  
  public List<ILinkedEntities> getLinkedEntities();
  
  public void setLinkedEntities(List<ILinkedEntities> linkedEntities);
  
  public ITagInstance getPriority();
  
  public void setPriority(ITagInstance priority);
  
  public String getLongDescription();
  
  public void setLongDescription(String longDescription);
  
  public IRoleInstance getResponsible();
  
  public void setResponsible(IRoleInstance responsible);
  
  public ITagInstance getStatus();
  
  public void setStatus(ITagInstance status);
  
  public List<IRoleInstance> getRoles();
  
  public void setRoles(List<? extends IRoleInstance> roles);
  
  public Long getDueDate();
  
  public void setDueDate(Long dueDate);
  
  public Boolean getIsCamundaCreated();
  
  public void setIsCamundaCreated(Boolean isCamundaCreated);
}
