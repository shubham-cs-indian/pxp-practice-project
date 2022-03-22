package com.cs.core.runtime.interactor.model.eventinstance;

import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.runtime.interactor.entity.eventinstance.*;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.TimeZone;

public class SaveEventInstanceModel implements ISaveEventInstanceModel {
  
  private static final long              serialVersionUID = 1L;
  
  protected IEventInstance               entity;
  protected List<IEventInstanceSchedule> addedInclusions;
  protected List<IEventInstanceSchedule> deletedInclusions;
  protected List<IEventInstanceSchedule> modifiedInclusions;
  
  protected List<IEventInstanceSchedule> addedExclusions;
  protected List<IEventInstanceSchedule> deletedExclusions;
  protected List<IEventInstanceSchedule> modifiedExclusions;
  
  protected List<ILinkedEntities>        addedLinkedEntities;
  protected List<String>                 deletedLinkedEntities;
  protected List<ILinkedEntities>        modifiedLinkedEntities;
  protected String                       name;
  protected List<String>                 types;
  protected Long                         iid;
  
  protected SaveEventInstanceModel()
  {
    entity = new EventInstance();
  }
  
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String toString()
  {
    return entity.toString();
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
    return modifiedInclusions;
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
  public List<String> getTypes()
  {
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  /*@Override
  public String getOwner()
  {
    return entity.getOwner();
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    entity.setOwner(owner);
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
  public List<? extends IContentTagInstance> getTags()
  {
    return entity.getTags();
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    entity.setTags(tags);
  }
  
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    return entity.getAttributes();
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributeInstances)
  {
    entity.setAttributes(attributeInstances);
  }
  
  @Override
  public List<? extends IRoleInstance> getRoles()
  {
    return entity.getRoles();
  }
  
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    entity.setRoles(roles);
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
    return entity.getTaxonomyIds();
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    entity.setTaxonomyIds(taxonomyIds);
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    return entity.getSelectedTaxonomyIds();
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    entity.setSelectedTaxonomyIds(selectedTaxonomyIds);
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
