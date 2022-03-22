package com.cs.core.config.interactor.usecase.relationship;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.relationship.IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllSide2NatureKlassesAndNatureRelationship extends IGetConfigInteractor<IIdParameterModel,IGetAllSide2NatureKlassesFromNatureRelationshipResponseModel> {
  
}
