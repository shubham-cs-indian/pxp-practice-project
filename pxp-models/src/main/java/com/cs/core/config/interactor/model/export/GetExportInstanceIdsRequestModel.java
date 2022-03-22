package com.cs.core.config.interactor.model.export;

import java.util.ArrayList;
import java.util.List;

public class GetExportInstanceIdsRequestModel implements IGetExportInstanceIdsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          dateConditionAttributeId;
  protected String          replicationAttributeId;
  protected String          channelIndependentReleaseId;
  protected String          objectTypeAttributeId;
  protected List<String>    objectTypeAttributeValues;
  protected List<String>    ids;
  
  public String getDateConditionAttributeId()
  {
    return dateConditionAttributeId;
  }
  
  public void setDateConditionAttributeId(String dateConditionAttributeId)
  {
    this.dateConditionAttributeId = dateConditionAttributeId;
  }
  
  public String getReplicationAttributeId()
  {
    return replicationAttributeId;
  }
  
  public void setReplicationAttributeId(String replicationAttributeId)
  {
    this.replicationAttributeId = replicationAttributeId;
  }
  
  public String getChannelIndependentReleaseId()
  {
    return channelIndependentReleaseId;
  }
  
  public void setChannelIndependentReleaseId(String channelIndependentReleaseId)
  {
    this.channelIndependentReleaseId = channelIndependentReleaseId;
  }
  
  @Override
  public String getObjectTypeAttributeId()
  {
    return objectTypeAttributeId;
  }
  
  @Override
  public void setObjectTypeAttributeId(String objectTypeAttributeId)
  {
    this.objectTypeAttributeId = objectTypeAttributeId;
  }
  
  @Override
  public List<String> getObjectTypeAttributeValues()
  {
    return objectTypeAttributeValues;
  }
  
  @Override
  public void setObjectTypeAttributeValues(List<String> objectTypeAttributeValues)
  {
    this.objectTypeAttributeValues = objectTypeAttributeValues;
  }
  
  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
}
