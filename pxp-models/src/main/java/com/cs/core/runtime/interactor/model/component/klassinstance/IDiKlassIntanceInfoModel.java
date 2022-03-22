package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.component.jms.IDiModel;

import java.util.List;

public interface IDiKlassIntanceInfoModel extends IDiModel {
  
  public static String KLASS_INSTANCE_ID = "klassInstanceId";
  public static String KLASS_TYPE_IDS    = "klassTypeIds";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public List<String> getKlassTypeIds();
  
  public void setKlassTypeIds(List<String> klassTypeIds);
}
