package com.cs.core.runtime.interactor.model.context;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CheckDuplicateLinkedVariantRequestModel
    implements ICheckDuplicateLinkedVariantRequestModel {
  
  private static final long           serialVersionUID = 1L;
  protected String                    id;
  protected String                    relationshipId;
  protected String                    contextId;
  protected List<IContentTagInstance> tags             = new ArrayList<>();
  protected IInstanceTimeRange        timeRange;
  
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
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public List<IContentTagInstance> getTags()
  {
    return tags;
  }
  
  @Override
  public void setTags(List<IContentTagInstance> tags)
  {
    this.tags = tags;
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
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }

}
