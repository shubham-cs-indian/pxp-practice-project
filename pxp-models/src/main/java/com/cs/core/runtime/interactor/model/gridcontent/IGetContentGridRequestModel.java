package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetContentGridRequestModel extends IModel {
  
  String KLASS_INSTANCES       = "klassInstances";
  String SELECTED_PROPERTY_IDS = "selectedPropertyIds";
  
  public List<IIdAndBaseType> getKlassInstances();
  
  public void setKlassInstances(List<IIdAndBaseType> klassInstances);
  
  public List<String> getSelectedPropertyIds();
  
  public void setSelectedPropertyIds(List<String> selectedPropertyIds);
}
