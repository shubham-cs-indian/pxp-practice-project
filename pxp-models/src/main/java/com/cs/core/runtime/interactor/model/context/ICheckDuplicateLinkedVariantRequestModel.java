package com.cs.core.runtime.interactor.model.context;

import java.util.List;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICheckDuplicateLinkedVariantRequestModel extends IModel {
  
  public static final String ID                        = "id";
  public static final String CONTEXT_ID                = "contextId";
  public static final String RELATIONSHIP_ID           = "relationshipId";
  public static final String TAGS                      = "tags";
  public static final String TIME_RANGE                = "timeRange";
  
  public String getContextId();
  public void setContextId(String contextId);
  
  public String getId();
  public void setId(String id);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
  public List<IContentTagInstance> getTags();
  public void setTags(List<IContentTagInstance> tags);
  
  public IInstanceTimeRange getTimeRange();
  public void setTimeRange(IInstanceTimeRange timeRange);
  
}
