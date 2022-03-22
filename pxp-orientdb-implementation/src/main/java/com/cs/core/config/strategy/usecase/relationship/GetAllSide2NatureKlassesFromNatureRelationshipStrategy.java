package com.cs.core.config.strategy.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.relationship.GetAllSide2NatureKlassesFromNatureRelationshipResponseModel;
import com.cs.core.config.interactor.model.relationship.IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;

@Component
public class GetAllSide2NatureKlassesFromNatureRelationshipStrategy extends OrientDBBaseStrategy implements IGetAllSide2NatureKlassesFromNatureRelationshipStrategy{

  @Autowired
  ISessionContext context;
  
  @Override
  public IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel execute(IIdParameterModel model) throws Exception
  {
    model.setCurrentUserId(context.getUserId());
    return execute(GET_ALL_SIDE2_NATURE_KLASSES_FROM_NATURE_RELATIONSHIP, model, GetAllSide2NatureKlassesFromNatureRelationshipResponseModel.class);
  }
}
