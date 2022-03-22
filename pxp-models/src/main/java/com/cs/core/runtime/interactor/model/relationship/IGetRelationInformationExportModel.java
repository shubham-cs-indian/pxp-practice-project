package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetRelationInformationExportModel extends IModel {
  
  public static final String ID                         = "id";
  public static final String SIDE1_INSTANCE_ID          = "side1InstanceId";
  public static final String SIDE2_INSTANCE_ID          = "side2InstanceId";
  public static final String RELATIONSHIP_ID            = "relationshipId";
  public static final String TAGS                       = "tags";
  public static final String CONTEXT_ID                 = "contextId";
  public static final String TIME_RANGE                 = "timeRange";
  public static final String SIDE1_ORIGINAL_INSTANCE_ID = "side1OriginalInstanceId";
  public static final String SIDE2_ORIGINAL_INSTANCE_ID = "side2OriginalInstanceId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getSide1InstanceId();
  
  public void setSide1InstanceId(String side1InstanceId);
  
  public String getSide2InstanceId();
  
  public void setSide2InstanceId(String side2InstanceId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public List<ITagInstance> getTags();
  
  public void setTags(List<ITagInstance> tags);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public String getSide1OriginalInstanceId();
  
  public void setSide1OriginalInstanceId(String side1OriginalInstanceId);
  
  public String getSide2OriginalInstanceId();
  
  public void setSide2OriginalInstanceId(String side2OriginalInstanceId);
}
