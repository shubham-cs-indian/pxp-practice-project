package com.cs.core.runtime.interactor.usecase.workflow.processinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetBGPJobLogDataService extends IRuntimeService<IIdParameterModel, IGetAssetDetailsResponseModel> {
  
}
