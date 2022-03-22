package com.cs.core.config.interactor.model.grideditpropertylist;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetGridEditPropertiesResponseModel implements IGetGridEditPropertiesResponseModel {
  
  private static final long         serialVersionUID = 1L;
  protected List<IIdLabelTypeModel> gridEditProperties;
  protected Long                    totalCount;
  
  @Override
  public List<IIdLabelTypeModel> getGridEditProperties()
  {
    return gridEditProperties;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setGridEditProperties(List<IIdLabelTypeModel> gridEditProperties)
  {
    if (gridEditProperties == null) {
      gridEditProperties = new ArrayList<>();
    }
    this.gridEditProperties = gridEditProperties;
  }
  
  @Override
  public Long getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
}
