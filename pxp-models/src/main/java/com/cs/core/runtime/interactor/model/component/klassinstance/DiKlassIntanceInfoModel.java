package com.cs.core.runtime.interactor.model.component.klassinstance;

import java.util.ArrayList;
import java.util.List;

public class DiKlassIntanceInfoModel implements IDiKlassIntanceInfoModel {
  
  private static final long serialVersionUID = 1L;
  
  private String            klassInstanceId;
  private List<String>      klassTypeIds;
  
  @Override
  public String getKlassInstanceId()
  {
    return this.klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public List<String> getKlassTypeIds()
  {
    if (this.klassTypeIds == null) {
      this.klassTypeIds = new ArrayList<>();
    }
    return this.klassTypeIds;
  }
  
  @Override
  public void setKlassTypeIds(List<String> klassTypeIds)
  {
    this.klassTypeIds = klassTypeIds;
  }
}
