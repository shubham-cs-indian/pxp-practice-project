package com.cs.core.runtime.interactor.model.gridcontent;

import java.util.ArrayList;
import java.util.List;

public class GetContentForGridRequestModel implements IGetContentForGridRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    klassInstanceIds;
  protected String          moduleId;
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    if (klassInstanceIds == null) {
      klassInstanceIds = new ArrayList<>();
    }
    this.klassInstanceIds = klassInstanceIds;
  }
  
  @Override
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }
}
