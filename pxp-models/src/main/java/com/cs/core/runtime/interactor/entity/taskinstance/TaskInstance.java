package com.cs.core.runtime.interactor.entity.taskinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.cs.core.config.interactor.entity.comment.Comment;
import com.cs.core.config.interactor.entity.comment.IComment;
import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceLocation;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceNotification;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceLocation;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceNotification;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TaskInstance extends AbstractKlassInstance /*AbstractReferencedEntity*/
    implements ITaskInstance {
  
  private static final long                  serialVersionUID     = 1L;
  
  /* Task specific fields */
  protected String                           baseType             = this.getClass()
      .getName();
  protected String                           icon;
  protected List<ITaskInstance>              subTasks             = new ArrayList<>();
  protected List<IComment>                   comments             = new ArrayList<>();
  protected Boolean                          isPublic             = false;
  protected List<String>                     attachments          = new ArrayList<>();
  protected Long                             overDueDate;
  protected Long                             dueDate;
  protected Long                             startDate;
  protected ITagInstance                     priority;
  protected Boolean                          isFavourite;
  protected ITagInstance                     status;
  protected List<ILinkedEntities>            linkedEntities;
  
  /* Fields from AbstractTimelineInstance   */
  protected String                           shortDescription;
  protected String                           longDescription;
  protected String                           image;
  protected String                           color;
  protected IEventInstanceLocation           location;
  protected TimeZone                         timezone;
  protected List<IEventInstanceNotification> notifications;
  protected IEventInstanceSchedule           eventSchedule;
  protected List<ITimeRange>                 calendarDates;
  protected List<IEventInstanceSchedule>     exclusions           = new ArrayList<>();
  protected List<IEventInstanceSchedule>     inclusions           = new ArrayList<>();
  
  protected List<ITagInstance>               tags;
  protected List<IRoleInstance>              roles                = new ArrayList<>();
  protected Boolean                          isNotifiedForDueDate = false;
  protected String                           originalInstanceId;
  protected Boolean                          isCamundaCreated     = false;
  protected String                           camundaTaskInstanceId;
  protected List<ICamundaFormField>          formFields           = new ArrayList<>();
  protected String                           camundaProcessInstanceId;
  protected String                           camundaProcessDefinationId;
  
  protected ITaskRoleEntity                  responsible;
  protected ITaskRoleEntity                  accountable;
  protected ITaskRoleEntity                  consulted;
  protected ITaskRoleEntity                  informed;
  protected ITaskRoleEntity                  verify;
  protected ITaskRoleEntity                  signoff;
  
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
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
  public List<ITaskInstance> getSubTasks()
  {
    return subTasks;
  }
  
  @JsonDeserialize(contentAs = TaskInstance.class)
  @Override
  public void setSubTasks(List<ITaskInstance> subTasks)
  {
    this.subTasks = subTasks;
  }
  
  @Override
  public List<IComment> getComments()
  {
    if(comments == null) {
      comments = new ArrayList<>();
    }
    return comments;
  }
  
  @JsonDeserialize(contentAs = Comment.class)
  @Override
  public void setComments(List<IComment> comments)
  {
    this.comments = comments;
  }
  
  @Override
  public List<String> getAttachments()
  {
    if(attachments == null) {
      attachments = new ArrayList<>();
    }
    return attachments;
  }
  
  @Override
  public void setAttachments(List<String> attachments)
  {
    this.attachments = attachments;
  }
  
  @Override
  public List<IRoleInstance> getRoles()
  {
    return roles;
  }
  
  @SuppressWarnings("unchecked")
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    this.roles = ((List<IRoleInstance>) roles);
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
  public Boolean getIsFavourite()
  {
    return isFavourite;
  }
  
  @Override
  public void setIsFavourite(Boolean isFavourite)
  {
    this.isFavourite = isFavourite;
  }
  
  @Override
  public ITagInstance getStatus()
  {
    if (status == null) {
      status = new TagInstance();
    }
    return status;
  }
  
  @Override
  public void setStatus(ITagInstance status)
  {
    this.status = status;
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
  public Long getStartDate()
  {
    return startDate;
  }
  
  @Override
  public void setStartDate(Long startDate)
  {
    this.startDate = startDate;
  }
  
  @Override
  public Boolean getIsPublic()
  {
    return isPublic;
  }
  
  @Override
  public void setIsPublic(Boolean isPublic)
  {
    this.isPublic = isPublic;
  }
  
  @Override
  public Boolean getIsNotifiedForDueDate()
  {
    return isNotifiedForDueDate;
  }
  
  @Override
  public void setIsNotifiedForDueDate(Boolean isNotifiedForDueDate)
  {
    this.isNotifiedForDueDate = isNotifiedForDueDate;
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
  public String getShortDescription()
  {
    return shortDescription;
  }
  
  @Override
  public void setShortDescription(String shortDescription)
  {
    this.shortDescription = shortDescription;
  }
  
  @Override
  public String getColor()
  {
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
  
  @Override
  public IEventInstanceLocation getLocation()
  {
    return location;
  }
  
  @JsonDeserialize(as = EventInstanceLocation.class)
  @Override
  public void setLocation(IEventInstanceLocation location)
  {
    this.location = location;
  }
  
  @Override
  public TimeZone getTimezone()
  {
    return timezone;
  }
  
  @Override
  public void setTimezone(TimeZone timezone)
  {
    this.timezone = timezone;
  }
  
  @Override
  public List<IEventInstanceNotification> getNotifications()
  {
    return notifications;
  }
  
  @JsonDeserialize(contentAs = EventInstanceNotification.class)
  @Override
  public void setNotifications(List<IEventInstanceNotification> notifications)
  {
    this.notifications = notifications;
  }
  
  @Override
  public IEventInstanceSchedule getEventSchedule()
  {
    if(eventSchedule == null) {
      eventSchedule = new EventInstanceSchedule();
    }
    return eventSchedule;
  }
  
  @JsonDeserialize(as = EventInstanceSchedule.class)
  @Override
  public void setEventSchedule(IEventInstanceSchedule schedule)
  {
    this.eventSchedule = schedule;
  }
  
  @Override
  public List<ITimeRange> getCalendarDates()
  {
    if(calendarDates == null) {
      calendarDates = new ArrayList<>();
    }
    return calendarDates;
  }
  
  @JsonDeserialize(contentAs = TimeRange.class)
  @Override
  public void setCalendarDates(List<ITimeRange> flatDates)
  {
    this.calendarDates = flatDates;
  }
  
  @Override
  public List<IEventInstanceSchedule> getExclusions()
  {
    return exclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setExclusions(List<IEventInstanceSchedule> exclusions)
  {
    this.exclusions = exclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getInclusions()
  {
    return inclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setInclusions(List<IEventInstanceSchedule> inclusions)
  {
    this.inclusions = inclusions;
  }
  
  @Override
  public String getImage()
  {
    return image;
  }
  
  @Override
  public void setImage(String image)
  {
    this.image = image;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
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
  
  @Override
  public String getCamundaTaskInstanceId()
  {
    return camundaTaskInstanceId;
  }
  
  @Override
  public void setCamundaTaskInstanceId(String camundaTaskInstanceId)
  {
    this.camundaTaskInstanceId = camundaTaskInstanceId;
  }
  
  @Override
  public List<ICamundaFormField> getFormFields()
  {
    return formFields;
  }
  
  @Override
  @JsonDeserialize(contentAs = CamundaFormField.class)
  public void setFormFields(List<ICamundaFormField> formFields)
  {
    this.formFields = formFields;
  }
  
  @Override
  public String getCamundaProcessInstanceId()
  {
    return this.camundaProcessInstanceId;
  }
  
  @Override
  public void setCamundaProcessInstanceId(String camundaProcessInstanceId)
  {
    this.camundaProcessInstanceId = camundaProcessInstanceId;
  }
  
  @Override
  public String getCamundaProcessDefinationId()
  {
    return camundaProcessDefinationId;
  }
  
  @Override
  public void setCamundaProcessDefinationId(String camundaProcessDefinationId)
  {
    this.camundaProcessDefinationId = camundaProcessDefinationId;
  }
  
  @Override
  public ITaskRoleEntity getResponsible()
  {
    if (responsible == null) {
      responsible = new TaskRoleEntity();
    }
    return responsible;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setResponsible(ITaskRoleEntity responsible)
  {
    this.responsible = responsible;
  }
  
  @Override
  public ITaskRoleEntity getAccountable()
  {
    if (accountable == null) {
      accountable = new TaskRoleEntity();
    }
    return accountable;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setAccountable(ITaskRoleEntity accountable)
  {
    this.accountable = accountable;
  }
  
  @Override
  public ITaskRoleEntity getConsulted()
  {
    if (consulted == null) {
      consulted = new TaskRoleEntity();
    }
    return consulted;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setConsulted(ITaskRoleEntity consulted)
  {
    this.consulted = consulted;
  }
  
  @Override
  public ITaskRoleEntity getInformed()
  {
    if (informed == null) {
      informed = new TaskRoleEntity();
    }
    return informed;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setInformed(ITaskRoleEntity informed)
  {
    this.informed = informed;
  }
  
  @Override
  public ITaskRoleEntity getVerify()
  {
    if (verify == null) {
      verify = new TaskRoleEntity();
    }
    return verify;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setVerify(ITaskRoleEntity verify)
  {
    this.verify = verify;
  }
  
  @Override
  public ITaskRoleEntity getSignoff()
  {
    if (signoff == null) {
      signoff = new TaskRoleEntity();
    }
    return signoff;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setSignoff(ITaskRoleEntity signoff)
  {
    this.signoff = signoff;
  }
}
