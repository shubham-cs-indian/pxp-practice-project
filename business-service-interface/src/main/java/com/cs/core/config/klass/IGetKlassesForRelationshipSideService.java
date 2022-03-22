package com.cs.core.config.klass;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassesForRelationshipModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKlassesForRelationshipSideService
    extends IGetConfigService<IIdParameterModel, IGetKlassesForRelationshipModel> {
  
}
