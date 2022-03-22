package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;
import java.util.Map;

public interface IGetSide2InstanceFilterByRelationshipResponseModel extends IModel {
  
  public static final String CONTENT_ID_VS_BASE_TYPE                   = "contentIdVsBaseType";
  public static final String SIDE_ID_VS_APPLICABLE_CONTENTS            = "sideIdVsApplicableContents";
  public static final String TARGET_INSTANCE_ID_VS_SOURCE_INSTANCE_IDS = "targetInstanceIdVsSourceInstanceIds";
  public static final String RELATIONSHIPID_VS_SIDE_IDS                = "relationshipIdVsSideIds";
  
  public Map<String, IIdAndBaseType> getContentIdVsBaseType();
  
  public void setContentIdVsBaseType(Map<String, IIdAndBaseType> contentIdVsBaseType);
  
  public Map<String, List<String>> getSideIdVsApplicableContents();
  
  public void setSideIdVsApplicableContents(Map<String, List<String>> sideIdVsApplicableContents);
  
  public Map<String, List<String>> getTargetInstanceIdVsSourceInstanceIds();
  
  public void setTargetInstanceIdVsSourceInstanceIds(
      Map<String, List<String>> targetInstanceIdVsSourceInstanceIds);
  
  public Map<String, List<String>> getRelationshipIdVsSideIds();
  
  public void setRelationshipIdVsSideIds(Map<String, List<String>> relationshipIdVsSideIds);
}
