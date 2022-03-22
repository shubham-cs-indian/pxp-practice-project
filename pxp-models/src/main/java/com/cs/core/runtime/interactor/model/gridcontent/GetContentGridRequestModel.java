package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetContentGridRequestModel implements IGetContentGridRequestModel {
  
  private static final long      serialVersionUID = 1L;
  public List<String>            selectedPropertyIds;
  protected List<IIdAndBaseType> klassInstanceIds;
  
  @Override
  public List<IIdAndBaseType> getKlassInstances()
  {
    return klassInstanceIds;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setKlassInstances(List<IIdAndBaseType> klassInstanceIds)
  {
    if (klassInstanceIds == null) {
      klassInstanceIds = new ArrayList<>();
    }
    this.klassInstanceIds = klassInstanceIds;
  }
  
  @Override
  public List<String> getSelectedPropertyIds()
  {
    return selectedPropertyIds;
  }
  
  @Override
  public void setSelectedPropertyIds(List<String> selectedPropertyIds)
  {
    if (selectedPropertyIds == null) {
      selectedPropertyIds = new ArrayList<>();
    }
    this.selectedPropertyIds = selectedPropertyIds;
  }
  
}
