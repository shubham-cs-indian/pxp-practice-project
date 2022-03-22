package com.cs.core.config.interactor.model.grideditpropertylist;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;

import java.util.List;

public interface IGetGridEditPropertiesResponseModel extends IModel {
  
  public static final String GRID_EDIT_PROPERTIES = "gridEditProperties";
  public static final String TOTAL_COUNT          = "totalCount";
      
  
  public List<IIdLabelTypeModel> getGridEditProperties();
  public void setGridEditProperties(List<IIdLabelTypeModel> gridEditProperties);
  
  public Long getTotalCount();
  public void setTotalCount(Long totalCount);
}
