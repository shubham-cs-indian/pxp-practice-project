package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetDefaultKlassesModel extends IModel {
  
  public static final String CHILDREN             = "children";
  public static final String ABSTRACT_KLASSES_IDS = "abstractKlassesIds";
  
  public List<IKlassInformationModel> getChildren();
  
  public void setChildren(List<IKlassInformationModel> children);
  
  public List<String> getAbstractKlassesIds();
  
  public void setAbstractKlassesIds(List<String> abstractKlassesIds);
}
