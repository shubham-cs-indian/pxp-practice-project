package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class VariantCreateBulkModel implements IVariantCreateBulkModel {
  
  private static final long                 serialVersionUID     = 1L;
  
  protected String                          name;
  protected String                          variantId;
  protected List<IContentTagInstance>       contextTags          = new ArrayList<>();
  protected List<IContentAttributeInstance> attributes;
  protected List<ITagInstance>              statusTagInstances;
  protected String                          createdBy;
  protected Long                            createdOn;
  protected String                          lastModifiedBy;
  protected Long                            lastModified;
  protected String                          parentId;
  protected IInstanceTimeRange              timeRange;
  protected List<String>                    linkedInstances;
  protected List<IContentTagInstance>       tags;
  protected Boolean                         isFromExternalSource = false;
  
  @Override
  public List<ITagInstance> getStatusTagInstances()
  {
    if (statusTagInstances == null) {
      statusTagInstances = new ArrayList<>();
    }
    return statusTagInstances;
  }
  
  @Override
  public void setStatusTagInstances(List<ITagInstance> statusTagInstances)
  {
    this.statusTagInstances = statusTagInstances;
  }
  
  @Override
  public List<IContentAttributeInstance> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  public void setAttributes(List<IContentAttributeInstance> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IContentTagInstance> getContextTags()
  {
    return contextTags;
  }
  
  @Override
  public void setContextTags(List<IContentTagInstance> contextTags)
  {
    this.contextTags = contextTags;
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
  public String getVariantId()
  {
    return variantId;
  }
  
  @Override
  public void setVariantId(String variantId)
  {
    this.variantId = variantId;
  }
  
  @Override
  public String getCreatedBy()
  {
    return createdBy;
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  @Override
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
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
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public IInstanceTimeRange getTimeRange()
  {
    return timeRange;
  }
  
  @JsonDeserialize(as = InstanceTimeRange.class)
  @Override
  public void setTimeRange(IInstanceTimeRange timeRange)
  {
    this.timeRange = timeRange;
  }
  
  @Override
  public List<String> getLinkedInstances()
  {
    if (linkedInstances == null) {
      return new ArrayList<String>();
    }
    return linkedInstances;
  }
  
  @Override
  public void setLinkedInstances(List<String> linkedInstances)
  {
    this.linkedInstances = linkedInstances;
  }
  
  @Override
  public List<IContentTagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<IContentTagInstance>();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<IContentTagInstance> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Boolean getIsFromExternalSource()
  {
    return isFromExternalSource;
  }
  
  @Override
  public void setIsFromExternalSource(Boolean isFromExternalSource)
  {
    this.isFromExternalSource = isFromExternalSource;
  }
}
