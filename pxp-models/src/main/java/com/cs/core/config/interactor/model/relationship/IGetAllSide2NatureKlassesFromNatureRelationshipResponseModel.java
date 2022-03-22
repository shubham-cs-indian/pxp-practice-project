package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel extends IModel {
  
  public static final String SIDE2_NATURE_KLASSES = "side2NatureKlasses";
  
  public Map<String, IConfigEntityInformationModel> getSide2NatureKlasses();
  
  public void setSide2NatureKlasses(Map<String, IConfigEntityInformationModel> side2NatureKlasses);
}
