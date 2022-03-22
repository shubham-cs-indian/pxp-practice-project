package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.comment.Comment;
import com.cs.core.config.interactor.entity.comment.IComment;
import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.config.interactor.model.role.ModifiedRoleInstanceModel;
import com.cs.core.runtime.interactor.entity.eventinstance.*;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.taskinstance.CamundaFormField;
import com.cs.core.runtime.interactor.entity.taskinstance.ICamundaFormField;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.entity.taskinstance.TaskInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedRoleInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SaveTaskInstanceModel implements ISaveTaskInstanceModel {
  
  private static final long                        serialVersionUID       = 1L;
  
  protected ITaskInstance                          entity;
  protected List<IEventInstanceSchedule>           addedInclusions;
  protected List<IEventInstanceSchedule>           deletedInclusions;
  protected List<IEventInstanceSchedule>           modifiedInclusions;
  
  protected List<IEventInstanceSchedule>           addedExclusions;
  protected List<IEventInstanceSchedule>           deletedExclusions;
  protected List<IEventInstanceSchedule>           modifiedExclusions;
  
  protected List<ILinkedEntities>                  addedLinkedEntities    = new ArrayList<>();
  protected List<String>                           deletedLinkedEntities  = new ArrayList<>();
  protected List<ILinkedEntities>                  modifiedLinkedEntities = new ArrayList<>();
  
  protected List<ITagInstance>                     addedTags              = new ArrayList<>();
  protected List<String>                           deletedTags            = new ArrayList<>();
  protected List<IModifiedContentTagInstanceModel> modifiedTags           = new ArrayList<>();
  
  protected List<IRoleInstance>                    addedRoles             = new ArrayList<>();
  protected List<String>                           deletedRoles           = new ArrayList<>();
  protected List<IModifiedRoleInstanceModel>       modifiedRoles          = new ArrayList<>();
  
  protected List<IComment>                         addedComments          = new ArrayList<>();
  protected List<String>                           deletedComments        = new ArrayList<>();
  protected List<IComment>                         modifiedComments       = new ArrayList<>();
  
  protected List<ITaskInstance>                    addedSubtasks          = new ArrayList<>();
  protected List<String>                           deletedSubtasks        = new ArrayList<>();
  protected List<ISaveTaskInstanceModel>           modifiedSubtasks;
  
  protected List<String>                           addedAttachments       = new ArrayList<>();
  protected List<String>                           deletedAttachments     = new ArrayList<>();
  
  protected String                                 statusTagId;
  protected String                                 priorityTagId;
  protected Long                                   iid;
  
  protected ITaskRoleSaveEntity                    responsible;
  protected ITaskRoleSaveEntity                    accountable;
  protected ITaskRoleSaveEntity                    consulted;
  protected ITaskRoleSaveEntity                    informed;
  protected ITaskRoleSaveEntity                    verify;
  protected ITaskRoleSaveEntity                    signoff;
  
  public SaveTaskInstanceModel()
  {
    entity = new TaskInstance();
  }
  
  public List<ISaveTaskInstanceModel> getModifiedSubtasks()
  {
    return modifiedSubtasks;
  }
  
  @JsonDeserialize(contentAs = SaveTaskInstanceModel.class)
  public void setModifiedSubtasks(List<ISaveTaskInstanceModel> modifiedSubtasks)
  {
    this.modifiedSubtasks = modifiedSubtasks;
  }
  
  public List<String> getAddedAttachments()
  {
    return addedAttachments;
  }
  
  public void setAddedAttachments(List<String> addedAttachments)
  {
    this.addedAttachments = addedAttachments;
  }
  
  public List<String> getDeletedAttachments()
  {
    return deletedAttachments;
  }
  
  public void setDeletedAttachments(List<String> deletedAttachments)
  {
    this.deletedAttachments = deletedAttachments;
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
  public String getColor()
  {
    return entity.getColor();
  }
  
  @Override
  public void setColor(String color)
  {
    entity.setColor(color);
  }
  
  @Override
  public IEventInstanceLocation getLocation()
  {
    return entity.getLocation();
  }
  
  @JsonDeserialize(as = EventInstanceLocation.class)
  @Override
  public void setLocation(IEventInstanceLocation location)
  {
    entity.setLocation(location);
  }
  
  @Override
  public TimeZone getTimezone()
  {
    return entity.getTimezone();
  }
  
  @Override
  public void setTimezone(TimeZone timezone)
  {
    entity.setTimezone(timezone);
  }
  
  @Override
  public List<IEventInstanceNotification> getNotifications()
  {
    return entity.getNotifications();
  }
  
  @JsonDeserialize(contentAs = EventInstanceNotification.class)
  @Override
  public void setNotifications(List<IEventInstanceNotification> notifications)
  {
    entity.setNotifications(notifications);
  }
  
  @Override
  public IEventInstanceSchedule getEventSchedule()
  {
    return entity.getEventSchedule();
  }
  
  @JsonDeserialize(as = EventInstanceSchedule.class)
  @Override
  public void setEventSchedule(IEventInstanceSchedule schedule)
  {
    entity.setEventSchedule(schedule);
  }
  
  @Override
  public List<ITimeRange> getCalendarDates()
  {
    return entity.getCalendarDates();
  }
  
  @JsonDeserialize(contentAs = TimeRange.class)
  @Override
  public void setCalendarDates(List<ITimeRange> dates)
  {
    entity.setCalendarDates(dates);
  }
  
  @Override
  public List<IEventInstanceSchedule> getInclusions()
  {
    return entity.getInclusions();
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setInclusions(List<IEventInstanceSchedule> inclusions)
  {
    entity.setInclusions(inclusions);
  }
  
  @Override
  public List<IEventInstanceSchedule> getExclusions()
  {
    return entity.getExclusions();
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setExclusions(List<IEventInstanceSchedule> exclusions)
  {
    entity.setExclusions(exclusions);
  }
  
  @Override
  public String getCreatedBy()
  {
    return entity.getCreatedBy();
  }
  
  @Override
  public void setCreatedBy(String userId)
  {
    entity.setCreatedBy(userId);
  }
  
  @Override
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long date)
  {
    entity.setCreatedOn(date);
  }
  
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  public String getShortDescription()
  {
    return entity.getShortDescription();
  }
  
  @Override
  public void setShortDescription(String shortDescription)
  {
    entity.setShortDescription(shortDescription);
  }
  
  @Override
  public String getLongDescription()
  {
    return entity.getLongDescription();
  }
  
  @Override
  public void setLongDescription(String longDescription)
  {
    entity.setLongDescription(longDescription);
  }
  
  @Override
  public String getImage()
  {
    return entity.getImage();
  }
  
  @Override
  public void setImage(String image)
  {
    entity.setImage(image);
  }
  
  @Override
  public String getJobId()
  {
    return entity.getJobId();
  }
  
  @Override
  public void setJobId(String jobId)
  {
    entity.setJobId(jobId);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public List<ILinkedEntities> getLinkedEntities()
  {
    return entity.getLinkedEntities();
  }
  
  @JsonDeserialize(contentAs = LinkedEntities.class)
  @Override
  public void setLinkedEntities(List<ILinkedEntities> linkedEntities)
  {
    entity.setLinkedEntities(linkedEntities);
  }
  
  @Override
  public List<? extends IContentTagInstance> getTags()
  {
    return entity.getTags();
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    entity.setTags(tags);
  }
  
  @Override
  public List<ITaskInstance> getSubTasks()
  {
    return entity.getSubTasks();
  }
  
  @JsonDeserialize(contentAs = TaskInstance.class)
  @Override
  public void setSubTasks(List<ITaskInstance> subTasks)
  {
    entity.setSubTasks(subTasks);
  }
  
  @Override
  public List<IComment> getComments()
  {
    return entity.getComments();
  }
  
  @JsonDeserialize(contentAs = Comment.class)
  @Override
  public void setComments(List<IComment> comments)
  {
    entity.setComments(comments);
  }
  
  @Override
  public Boolean getIsPublic()
  {
    return entity.getIsPublic();
  }
  
  @Override
  public void setIsPublic(Boolean isPublic)
  {
    entity.setIsPublic(isPublic);
  }
  
  @Override
  public List<String> getAttachments()
  {
    return entity.getAttachments();
  }
  
  @Override
  public void setAttachments(List<String> attachments)
  {
    entity.setAttachments(attachments);
  }
  
  @Override
  public Long getOverDueDate()
  {
    return entity.getOverDueDate();
  }
  
  @Override
  public void setOverDueDate(Long overDueDate)
  {
    entity.setOverDueDate(overDueDate);
  }
  
  @Override
  public ITagInstance getPriority()
  {
    return entity.getPriority();
  }
  
  @Override
  public void setPriority(ITagInstance priority)
  {
    entity.setPriority(priority);
  }
  
  @Override
  public List<IEventInstanceSchedule> getAddedInclusions()
  {
    return addedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setAddedInclusions(List<IEventInstanceSchedule> addedInclusions)
  {
    this.addedInclusions = addedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getDeletedInclusions()
  {
    return deletedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setDeletedInclusions(List<IEventInstanceSchedule> deletedInclusions)
  {
    this.deletedInclusions = deletedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getAddedExclusions()
  {
    return addedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setAddedExclusions(List<IEventInstanceSchedule> addedExclusions)
  {
    this.addedExclusions = addedExclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getDeletedExclusions()
  {
    return deletedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setDeletedExclusions(List<IEventInstanceSchedule> deletedExclusions)
  {
    this.deletedExclusions = deletedExclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getModifiedInclusions()
  {
    return this.modifiedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setModifiedInclusions(List<IEventInstanceSchedule> modifiedInclusions)
  {
    this.modifiedInclusions = modifiedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getModifiedExclusions()
  {
    return modifiedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setModifiedExclusions(List<IEventInstanceSchedule> modifiedExclusions)
  {
    this.modifiedExclusions = modifiedExclusions;
  }
  
  @Override
  public List<ILinkedEntities> getAddedLinkedEntities()
  {
    return addedLinkedEntities;
  }
  
  @JsonDeserialize(contentAs = LinkedEntities.class)
  @Override
  public void setAddedLinkedEntities(List<ILinkedEntities> addedLinkedEntities)
  {
    this.addedLinkedEntities = addedLinkedEntities;
  }
  
  @Override
  public List<String> getDeletedLinkedEntities()
  {
    return deletedLinkedEntities;
  }
  
  @Override
  public void setDeletedLinkedEntities(List<String> deletedLinkedEntities)
  {
    this.deletedLinkedEntities = deletedLinkedEntities;
  }
  
  @Override
  public List<ILinkedEntities> getModifiedLinkedEntities()
  {
    return modifiedLinkedEntities;
  }
  
  @JsonDeserialize(contentAs = LinkedEntities.class)
  @Override
  public void setModifiedLinkedEntities(List<ILinkedEntities> modifiedLinkedEntities)
  {
    this.modifiedLinkedEntities = modifiedLinkedEntities;
  }
  
  @Override
  public List<ITagInstance> getAddedTags()
  {
    return addedTags;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setAddedTags(List<ITagInstance> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public List<IModifiedContentTagInstanceModel> getModifiedTags()
  {
    return modifiedTags;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = AbstractModifiedContentTagInstanceModel.class)
  @Override
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<IComment> getAddedComments()
  {
    return addedComments;
  }
  
  @JsonDeserialize(contentAs = Comment.class)
  @Override
  public void setAddedComments(List<IComment> addedComments)
  {
    this.addedComments = addedComments;
  }
  
  @Override
  public List<String> getDeletedComments()
  {
    return deletedComments;
  }
  
  @Override
  public void setDeletedComments(List<String> deletedComments)
  {
    this.deletedComments = deletedComments;
  }
  
  @Override
  public List<IComment> getModifiedComments()
  {
    return modifiedComments;
  }
  
  @JsonDeserialize(contentAs = Comment.class)
  @Override
  public void setModifiedComments(List<IComment> modifiedComments)
  {
    this.modifiedComments = modifiedComments;
  }
  
  @Override
  public List<ITaskInstance> getAddedSubtasks()
  {
    return addedSubtasks;
  }
  
  @JsonDeserialize(contentAs = TaskInstance.class)
  @Override
  public void setAddedSubtasks(List<ITaskInstance> addedSubtasks)
  {
    this.addedSubtasks = addedSubtasks;
  }
  
  @Override
  public List<String> getDeletedSubtasks()
  {
    return deletedSubtasks;
  }
  
  @Override
  public void setDeletedSubtasks(List<String> deletedSubtasks)
  {
    this.deletedSubtasks = deletedSubtasks;
  }
  
  @Override
  public Boolean getIsFavourite()
  {
    return entity.getIsFavourite();
  }
  
  @Override
  public void setIsFavourite(Boolean isFavourite)
  {
    entity.setIsFavourite(isFavourite);
  }
  
  @Override
  public ITagInstance getStatus()
  {
    return entity.getStatus();
  }
  
  @Override
  public void setStatus(ITagInstance status)
  {
    entity.setStatus(status);
  }
  
  @Override
  public List<IRoleInstance> getAddedRoles()
  {
    if (addedRoles == null) {
      addedRoles = new ArrayList<IRoleInstance>();
    }
    return addedRoles;
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setAddedRoles(List<IRoleInstance> addedRoles)
  {
    this.addedRoles = addedRoles;
  }
  
  @Override
  public List<String> getDeletedRoles()
  {
    if (deletedRoles == null) {
      deletedRoles = new ArrayList<String>();
    }
    return deletedRoles;
  }
  
  @Override
  public void setDeletedRoles(List<String> deletedRoles)
  {
    this.deletedRoles = deletedRoles;
  }
  
  @Override
  public List<IModifiedRoleInstanceModel> getModifiedRoles()
  {
    if (modifiedRoles == null) {
      modifiedRoles = new ArrayList<IModifiedRoleInstanceModel>();
    }
    return modifiedRoles;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = ModifiedRoleInstanceModel.class)
  @Override
  public void setModifiedRoles(List<IModifiedRoleInstanceModel> modifiedRoles)
  {
    this.modifiedRoles = modifiedRoles;
  }
  
  @Override
  public List<IRoleInstance> getRoles()
  {
    return entity.getRoles();
  }
  
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    entity.setRoles(roles);
  }
  
  @Override
  public Long getDueDate()
  {
    return entity.getDueDate();
  }
  
  @Override
  public void setDueDate(Long dueDate)
  {
    entity.setDueDate(dueDate);
  }
  
  @Override
  public Long getStartDate()
  {
    return entity.getStartDate();
  }
  
  @Override
  public void setStartDate(Long startDate)
  {
    entity.setStartDate(startDate);
  }
  
  @Override
  public String getStatusTagId()
  {
    return statusTagId;
  }
  
  @Override
  public void setStatusTagId(String statusTagId)
  {
    this.statusTagId = statusTagId;
  }
  
  @Override
  public String getPriorityTagId()
  {
    return priorityTagId;
  }
  
  @Override
  public void setPriorityTagId(String priorityTagId)
  {
    this.priorityTagId = priorityTagId;
  }
  
  @Override
  public String getName()
  {
    return entity.getName();
  }
  
  @Override
  public void setName(String name)
  {
    entity.setName(name);
  }
  
  @Override
  public Boolean getIsNotifiedForDueDate()
  {
    return entity.getIsNotifiedForDueDate();
  }
  
  @Override
  public void setIsNotifiedForDueDate(Boolean isTaskNotifiedForDueDate)
  {
    entity.setIsNotifiedForDueDate(isTaskNotifiedForDueDate);
  }
  
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributeInstances)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getBaseType()
  {
    return entity.getBaseType();
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    entity.setBaseType(baseType);
  }
  
  @Override
  public List<String> getTypes()
  {
    return entity.getTypes();
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    entity.setTypes(types);
  }
  
  @Override
  public String getComponentId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    // TODO Auto-generated method stub
    
  }
  
  /*@Override
  public String getOwner()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    // TODO Auto-generated method stub
  }*/
  
  @Override
  public Long getLastModified()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    // TODO Auto-generated method stub
  }
  
  @JsonIgnore
  @Override
  public List<String> getTaxonomyIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return this.entity.getPhysicalCatalogId();
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatelogId)
  {
    this.entity.setPhysicalCatalogId(physicalCatelogId);
  }
  
  @Override
  public String getPortalId()
  {
    return this.entity.getPortalId();
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.entity.setPortalId(portalId);
  }
  
  @Override
  public String getLogicalCatalogId()
  {
    return this.entity.getLogicalCatalogId();
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatelogId)
  {
    this.entity.setLogicalCatalogId(logicalCatelogId);
  }
  
  @Override
  public String getSystemId()
  {
    return this.entity.getSystemId();
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.entity.setSystemId(systemId);
  }
  
  @Override
  public String getOrganizationId()
  {
    return this.entity.getOrganizationId();
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.entity.setOrganizationId(organizationId);
  }
  
  @Override
  public String getEndpointId()
  {
    return this.entity.getEndpointId();
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.entity.setEndpointId(endpointId);
  }
  
  @JsonIgnore
  @Override
  public String getOriginalInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getCamundaTaskInstanceId()
  {
    return this.entity.getCamundaTaskInstanceId();
  }
  
  @Override
  public void setCamundaTaskInstanceId(String camundaTaskInstanceId)
  {
    this.entity.setCamundaTaskInstanceId(camundaTaskInstanceId);
  }
  
  @Override
  public List<ICamundaFormField> getFormFields()
  {
    return this.entity.getFormFields();
  }
  
  @Override
  @JsonDeserialize(contentAs = CamundaFormField.class)
  public void setFormFields(List<ICamundaFormField> formFields)
  {
    this.entity.setFormFields(formFields);
  }
  
  @Override
  public Boolean getIsCamundaCreated()
  {
    return this.entity.getIsCamundaCreated();
  }
  
  @Override
  public void setIsCamundaCreated(Boolean isCamundaCreated)
  {
    this.entity.setIsCamundaCreated(isCamundaCreated);
  }
  
  @Override
  public String getCamundaProcessInstanceId()
  {
    return this.entity.getCamundaProcessInstanceId();
  }
  
  @Override
  public void setCamundaProcessInstanceId(String camundaProcessInstanceId)
  {
    this.entity.setCamundaProcessInstanceId(camundaProcessInstanceId);
  }
  
  @Override
  public String getCamundaProcessDefinationId()
  {
    return this.entity.getCamundaProcessDefinationId();
  }
  
  @Override
  public void setCamundaProcessDefinationId(String camundaProcessDefinationId)
  {
    this.entity.setCamundaProcessDefinationId(camundaProcessDefinationId);
  }
  
  @Override
  public ITaskRoleSaveEntity getResponsible()
  {
    if (responsible == null) {
      responsible = new TaskRoleSaveEntity();
    }
    return responsible;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleSaveEntity.class)
  public void setResponsible(ITaskRoleSaveEntity responsible)
  {
    this.responsible = responsible;
  }
  
  @Override
  public ITaskRoleSaveEntity getAccountable()
  {
    if (accountable == null) {
      accountable = new TaskRoleSaveEntity();
    }
    return accountable;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleSaveEntity.class)
  public void setAccountable(ITaskRoleSaveEntity accountable)
  {
    this.accountable = accountable;
  }
  
  public ITaskRoleSaveEntity getConsulted()
  {
    if (consulted == null) {
      consulted = new TaskRoleSaveEntity();
    }
    return consulted;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleSaveEntity.class)
  public void setConsulted(ITaskRoleSaveEntity consulted)
  {
    this.consulted = consulted;
  }
  
  public ITaskRoleSaveEntity getInformed()
  {
    if (informed == null) {
      informed = new TaskRoleSaveEntity();
    }
    return informed;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleSaveEntity.class)
  public void setInformed(ITaskRoleSaveEntity informed)
  {
    this.informed = informed;
  }
  
  public ITaskRoleSaveEntity getVerify()
  {
    if (verify == null) {
      verify = new TaskRoleSaveEntity();
    }
    return verify;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleSaveEntity.class)
  public void setVerify(ITaskRoleSaveEntity verify)
  {
    this.verify = verify;
  }
  
  public ITaskRoleSaveEntity getSignoff()
  {
    if (signoff == null) {
      signoff = new TaskRoleSaveEntity();
    }
    return signoff;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleSaveEntity.class)
  public void setSignoff(ITaskRoleSaveEntity signoff)
  {
    this.signoff = signoff;
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
