package com.cs.core.runtime.interactor.entity.taskinstance;

import com.cs.core.config.interactor.entity.comment.IComment;
import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.runtime.interactor.entity.configuration.base.ITimelineInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import java.util.List;

public interface IBaseTaskInstance extends IKlassInstance, ITimelineInstance {
  
  public static final String ICON                          = "icon";
  public static final String SUB_TASKS                     = "subTasks";
  public static final String COMMENTS                      = "comments";
  public static final String IS_PUBLIC                     = "isPublic";
  public static final String ATTACHMENTS                   = "attachments";
  public static final String OVER_DUE_DATE                 = "overDueDate";
  public static final String PRIORITY                      = "priority";
  public static final String IS_FAVOURITE                  = "isFavourite";
  public static final String STATUS                        = "status";
  public static final String DUE_DATE                      = "dueDate";
  public static final String START_DATE                    = "startDate";
  public static final String LINKED_ENTITIES               = "linkedEntities";
  public static final String ROLES                         = "roles";
  public static final String TAGS                          = "tags";
  public static final String IS_NOTIFIED_FOR_DUE_DATE      = "isNotifiedForDueDate";
  public static final String LONG_DESCRIPTION              = "longDescription";
  public static final String SHORT_DESCRIPTION             = "shortDescription";
  public static final String IS_CAMUNDA_CREATED            = "isCamundaCreated";
  public static final String CAMUNDA_TASK_INSTANCE_ID      = "camundaTaskInstanceId";
  public static final String FORM_FIELDS                   = "formFields";
  public static final String CAMUNDA_PROCESS_INSTANCE_ID   = "camundaProcessInstanceId";
  public static final String CAMUNDA_PROCESS_DEFINATION_ID = "camundaProcessDefinationId";
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public void setSubTasks(List<ITaskInstance> subTasks);
  
  public List<ITaskInstance> getSubTasks();
  
  public void setComments(List<IComment> comments);
  
  public List<IComment> getComments();
  
  public void setAttachments(List<String> attachments);
  
  public List<String> getAttachments();
  
  public void setRoles(List<? extends IRoleInstance> roles);
  
  public List<IRoleInstance> getRoles();
  
  public void setOverDueDate(Long overDueDate);
  
  public Long getOverDueDate();
  
  public void setPriority(ITagInstance priority);
  
  public ITagInstance getPriority();
  
  public void setIsFavourite(Boolean isFavourite);
  
  public Boolean getIsFavourite();
  
  public void setStatus(ITagInstance status);
  
  public ITagInstance getStatus();
  
  public Long getDueDate();
  
  public void setDueDate(Long dueDate);
  
  public Long getStartDate();
  
  public void setStartDate(Long startDate);
  
  public Boolean getIsPublic();
  
  public void setIsPublic(Boolean isPublic);
  
  public List<ILinkedEntities> getLinkedEntities();
  
  public void setLinkedEntities(List<ILinkedEntities> linkedEntities);
  
  public Boolean getIsNotifiedForDueDate();
  
  public void setIsNotifiedForDueDate(Boolean isNotifiedForDueDate);
  
  public String getLongDescription();
  
  public void setLongDescription(String longDescription);
  
  public String getShortDescription();
  
  public void setShortDescription(String shortDescription);
  
  public Boolean getIsCamundaCreated();
  
  public void setIsCamundaCreated(Boolean isCamundaCreated);
  
  public String getCamundaTaskInstanceId();
  
  public void setCamundaTaskInstanceId(String camundaTaskInstanceId);
  
  public List<ICamundaFormField> getFormFields();
  
  public void setFormFields(List<ICamundaFormField> formFields);
  
  public String getCamundaProcessInstanceId();
  
  public void setCamundaProcessInstanceId(String camundaProcessInstanceId);
  
  public String getCamundaProcessDefinationId();
  
  public void setCamundaProcessDefinationId(String camundaProcessDefinationId);
}
