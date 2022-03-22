package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.relationship.IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllSide2NatureKlassesAndNatureRelationshipService
    extends IGetConfigService<IIdParameterModel, IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel> {
  
}
