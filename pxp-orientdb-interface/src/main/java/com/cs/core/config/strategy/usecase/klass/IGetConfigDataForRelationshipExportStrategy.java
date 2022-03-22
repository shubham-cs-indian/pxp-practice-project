package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.klass.IGetConfigDataForRelationshipExportModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetConfigDataForRelationshipExportStrategy
    extends IConfigStrategy<IGetConfigDataForRelationshipExportModel, IGetConfigDataResponseModel> {
  
}
