package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.relationship.IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel;
import com.cs.core.config.relationship.IGetAllSide2NatureKlassesAndNatureRelationshipService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component
public class GetAllSide2NatureKlassesFromNatureRelationship
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel>
    implements IGetAllSide2NatureKlassesAndNatureRelationship {
  
  @Autowired
  protected IGetAllSide2NatureKlassesAndNatureRelationshipService getAllSide2NatureKlassesAndNatureRelationshipService;
  
  @Override
  protected IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getAllSide2NatureKlassesAndNatureRelationshipService.execute(model);
  }
}
