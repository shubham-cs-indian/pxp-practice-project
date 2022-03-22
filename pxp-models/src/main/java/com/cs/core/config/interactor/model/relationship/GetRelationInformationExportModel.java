package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.relationship.IGetRelationInformationExportModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetRelationInformationExportModel implements IGetRelationInformationExportModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected String             id;
  protected String             side1InstanceId;
  protected String             side2InstanceId;
  protected String             side1OriginalInstanceId;
  protected String             side2OriginalInstanceId;
  protected String             relationshipId;
  protected List<ITagInstance> tags;
  protected String             contextId;
  protected IInstanceTimeRange timerange;
  
  @Override
  public List<ITagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<ITagInstance>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<ITagInstance> tags)
  {
    this.tags = tags;
  }
  
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
  public String getSide1InstanceId()
  {
    return side1InstanceId;
  }
  
  @Override
  public void setSide1InstanceId(String side1InstanceId)
  {
    this.side1InstanceId = side1InstanceId;
  }
  
  @Override
  public String getSide2InstanceId()
  {
    return side2InstanceId;
  }
  
  @Override
  public void setSide2InstanceId(String side2InstanceId)
  {
    this.side2InstanceId = side2InstanceId;
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
  public IInstanceTimeRange getTimeRange()
  {
    return timerange;
  }
  
  @Override
  @JsonDeserialize(as = InstanceTimeRange.class)
  public void setTimeRange(IInstanceTimeRange timeRange)
  {
    this.timerange = timeRange;
  }
  
  @Override
  public String getSide1OriginalInstanceId()
  {
    return side1OriginalInstanceId;
  }
  
  @Override
  public void setSide1OriginalInstanceId(String side1OriginalInstanceId)
  {
    this.side1OriginalInstanceId = side1OriginalInstanceId;
  }
  
  @Override
  public String getSide2OriginalInstanceId()
  {
    return side2OriginalInstanceId;
  }
  
  @Override
  public void setSide2OriginalInstanceId(String side2OriginalInstanceId)
  {
    this.side2OriginalInstanceId = side2OriginalInstanceId;
  }
}
