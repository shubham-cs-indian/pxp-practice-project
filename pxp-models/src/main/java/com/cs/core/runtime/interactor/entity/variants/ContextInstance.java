package com.cs.core.runtime.interactor.entity.variants;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class ContextInstance implements IContextInstance {
  
  private static final long      serialVersionUID = 1L;
  
  protected String               id;
  protected List<String>         tagInstanceIds;
  protected String               contextId;
  protected IInstanceTimeRange   timeRange;
  protected List<IIdAndBaseType> linkedInstances  = new ArrayList<>();
  
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
  public List<String> getTagInstanceIds()
  {
    if (tagInstanceIds == null) {
      tagInstanceIds = new ArrayList<>();
    }
    return tagInstanceIds;
  }
  
  @Override
  public void setTagInstanceIds(List<String> tagInstanceIds)
  {
    this.tagInstanceIds = tagInstanceIds;
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
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @JsonDeserialize(as = InstanceTimeRange.class)
  @Override
  public void setTimeRange(IInstanceTimeRange timeRange)
  {
    this.timeRange = timeRange;
  }
  
  @Override
  public IInstanceTimeRange getTimeRange()
  {
    return timeRange;
  }
  
  @Override
  public List<IIdAndBaseType> getLinkedInstances()
  {
    return linkedInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setLinkedInstances(List<IIdAndBaseType> linkedInstances)
  {
    this.linkedInstances = linkedInstances;
  }
}
