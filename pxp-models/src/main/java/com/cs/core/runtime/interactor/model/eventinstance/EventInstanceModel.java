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

public class EventInstanceModel implements IEventInstanceModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IEventInstance  entity;
  protected Long            iid;
  
  public EventInstanceModel()
  {
    entity = new EventInstance();
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
  public List<? extends IContentTagInstance> getTags()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    // TODO Auto-generated method stub
    
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
  public List<? extends IRoleInstance> getRoles()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
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
  public List<String> getTaxonomyIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    // TODO Auto-generated method stub
    
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
