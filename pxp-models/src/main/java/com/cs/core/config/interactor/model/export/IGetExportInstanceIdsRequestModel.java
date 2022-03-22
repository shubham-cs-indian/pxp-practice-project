package com.cs.core.config.interactor.model.export;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetExportInstanceIdsRequestModel extends IModel {
  
  public static String DATE_CONDITION_ATTRIBUTE_ID    = "dateConditionAttributeId";
  public static String REPLICATION_ATTRIBUTE_ID       = "replicationAttributeId";
  public static String CHANNEL_INDEPENDENT_RELEASE_ID = "channelIndependentReleaseId";
  public static String OBJECT_TYPE_ATTRIBUTE_ID       = "objectTypeAttributeId";
  public static String OBJECT_TYPE_ATTRIBUTE_VALUES   = "objectTypeAttributeValues";
  public static String IDS                            = "ids";
  
  public String getDateConditionAttributeId();
  
  public void setDateConditionAttributeId(String attributeId);
  
  public String getReplicationAttributeId();
  
  public void setReplicationAttributeId(String replicationAttributeId);
  
  public String getChannelIndependentReleaseId();
  
  public void setChannelIndependentReleaseId(String channelIndependentReleaseId);
  
  public String getObjectTypeAttributeId();
  
  public void setObjectTypeAttributeId(String objectTypeAttributeId);
  
  public List<String> getObjectTypeAttributeValues();
  
  public void setObjectTypeAttributeValues(List<String> objectTypeAttributeValues);
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
}
