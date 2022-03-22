package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IGetAllModel;

import java.util.List;

public interface IGetKlassInstancesForComparisonRequestModel extends IGetAllModel {
  
  public static final String IDS               = "ids";
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String KlassInstanceId);
}
