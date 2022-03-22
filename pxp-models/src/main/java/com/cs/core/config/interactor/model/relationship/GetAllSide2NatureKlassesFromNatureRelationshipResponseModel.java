package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GetAllSide2NatureKlassesFromNatureRelationshipResponseModel
    implements IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel {
  
  private static final long                            serialVersionUID = 1L;
  protected Map<String, IConfigEntityInformationModel> side2NatureKlasses;
  
  @Override
  public Map<String, IConfigEntityInformationModel> getSide2NatureKlasses()
  {
    if (side2NatureKlasses == null) {
      side2NatureKlasses = new HashMap<>();
    }
    return side2NatureKlasses;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setSide2NatureKlasses(Map<String, IConfigEntityInformationModel> side2NatureKlasses)
  {
    this.side2NatureKlasses = side2NatureKlasses;
  }
}
