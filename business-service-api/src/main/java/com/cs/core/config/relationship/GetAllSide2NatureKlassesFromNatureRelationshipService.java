package com.cs.core.config.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.relationship.IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel;
import com.cs.core.config.strategy.usecase.relationship.IGetAllSide2NatureKlassesFromNatureRelationshipStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component
public class GetAllSide2NatureKlassesFromNatureRelationshipService
    extends AbstractGetConfigService<IIdParameterModel, IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel>
    implements IGetAllSide2NatureKlassesAndNatureRelationshipService {
  
  @Autowired
  protected IGetAllSide2NatureKlassesFromNatureRelationshipStrategy getAllSide2NatureKlassFromFromNatureRelationshipStrategy;
  
  @Override
  protected IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel executeInternal(IIdParameterModel model) throws Exception
  {
    model.setCurrentUserId(context.getUserId());
    return getAllSide2NatureKlassFromFromNatureRelationshipStrategy.execute(model);
  }
}
