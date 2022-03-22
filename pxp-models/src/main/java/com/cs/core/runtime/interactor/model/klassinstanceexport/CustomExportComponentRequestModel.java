package com.cs.core.runtime.interactor.model.klassinstanceexport;

import com.cs.core.runtime.interactor.model.offboarding.ICustomExportComponentRequestModel;

import java.util.ArrayList;
import java.util.List;

public class CustomExportComponentRequestModel extends CustomExportComponentConfigModel
    implements ICustomExportComponentRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    klassInstanceIds = new ArrayList<>();
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
  }
}
