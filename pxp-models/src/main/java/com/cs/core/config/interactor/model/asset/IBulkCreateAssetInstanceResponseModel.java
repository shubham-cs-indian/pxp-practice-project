package com.cs.core.config.interactor.model.asset;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;


public interface IBulkCreateAssetInstanceResponseModel extends IKlassInstanceInformationModel {
  
  public static final String SUCCESS                          = "success";
  public static final String SUCCESS_INSTANCE_IIDS            = "successInstanceIIds";
  public static final String FAILURE                          = "failure";
  public static final String TIV_DUPLICATE_DETECTION_INFO_MAP = "tivDuplicateDetectionInfoMap";
  public static final String TIV_FAILURE                      = "tivFailure";
  public static final String TIV_SUCCESS                      = "tivSuccess";
  public static final String TIV_WARNING                      = "tivWarning";
  public static final String DUPLICATE_IID_SET                = "duplicateIIdSet";
  
  public void fillKlassInstanceInformationModel(IKlassInstanceInformationModel klassInstanceInformationModel);
  
  public List<String> getSuccess();
  public void setSuccess(List<String> success);
  
  public List<Long> getSuccessInstanceIIds();
  public void setSuccessInstanceIIds(List<Long> successInstanceIIds);

  public List<String> getFailure();
  public void setFailure(List<String> failure);
  
  public Map<Long, Object> getTivDuplicateDetectionInfoMap();
  public void setTivDuplicateDetectionInfoMap(Map<Long, Object> tivDuplicateDetectionInfoMap);
  
  public List<String> getTivFailure();
  public void setTivFailure(List<String> tivFailure);
  
  public List<String> getTivSuccess();
  public void setTivSuccess(List<String> tivSuccess);
  
  public List<String> getTivWarning();
  public void setTivWarning(List<String> tivWarning);
  
  public Set<Long> getDuplicateIIdSet();
  public void setDuplicateIIdSet(Set<Long> duplicateIIdSet);
}
