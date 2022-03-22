package com.cs.core.config.strategy.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.ISide2NatureKlassFromNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.configdetails.Side2NatureKlassFromNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;

@Component
public class GetSide2NatureKlassFromNatureRelationshipStrategy extends OrientDBBaseStrategy implements IGetSide2NatureKlassFromNatureRelationshipStrategy{

  @Autowired
  ISessionContext context;
  
  @Override
  public ISide2NatureKlassFromNatureRelationshipModel execute(IIdParameterModel model) throws Exception
  {
    model.setCurrentUserId(context.getUserId());
    return execute(GET_SIDE2_NATURE_KLASS_FROM_NATURE_RELATIONSHIP, model, Side2NatureKlassFromNatureRelationshipModel.class);
  }
}
