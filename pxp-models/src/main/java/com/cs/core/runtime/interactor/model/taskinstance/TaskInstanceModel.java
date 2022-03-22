package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.comment.Comment;
import com.cs.core.config.interactor.entity.comment.IComment;
import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.runtime.interactor.entity.eventinstance.*;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.taskinstance.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.TimeZone;

@JsonTypeInfo(use = Id.NONE)
public class TaskInstanceModel implements ITaskInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected ITaskInstance   entity;
  protected Long            iid;
  
  public TaskInstanceModel()
  {
    entity = new TaskInstance();
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
  public Boolean getIsNotifiedForDueDate()
  {
    return entity.getIsNotifiedForDueDate();
  }
  
  @Override
  public void setIsNotifiedForDueDate(Boolean isNotifiedForDueDate)
  {
    entity.setIsNotifiedForDueDate(isNotifiedForDueDate);
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
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    return null;
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributeInstances)
  {
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
  public List<String> getTaxonomyIds()
  {
    return null;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
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
  public List<String> getSelectedTaxonomyIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getComponentId()
  {
    return entity.getComponentId();
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    entity.setComponentId(componentId);
  }
  
  /*@Override
  public String getOwner()
  {
    return null;
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
  
  }*/
  
  @Override
  public Long getLastModified()
  {
    return entity.getLastModified();
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    entity.setLastModified(lastModified);
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
  public ITaskRoleEntity getResponsible()
  {
    return entity.getResponsible();
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setResponsible(ITaskRoleEntity responsible)
  {
    entity.setResponsible(responsible);
  }
  
  @Override
  public ITaskRoleEntity getAccountable()
  {
    return entity.getAccountable();
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setAccountable(ITaskRoleEntity accountable)
  {
    entity.setAccountable(accountable);
  }
  
  @Override
  public ITaskRoleEntity getConsulted()
  {
    return entity.getConsulted();
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setConsulted(ITaskRoleEntity consulted)
  {
    entity.setConsulted(consulted);
  }
  
  @Override
  public ITaskRoleEntity getInformed()
  {
    return entity.getInformed();
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setInformed(ITaskRoleEntity informed)
  {
    entity.setInformed(informed);
  }
  
  @Override
  public ITaskRoleEntity getVerify()
  {
    return entity.getVerify();
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setVerify(ITaskRoleEntity verify)
  {
    entity.setVerify(verify);
  }
  
  @Override
  public ITaskRoleEntity getSignoff()
  {
    return entity.getSignoff();
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setSignoff(ITaskRoleEntity signoff)
  {
    entity.setSignoff(signoff);
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
