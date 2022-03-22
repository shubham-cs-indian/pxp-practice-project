package com.cs.core.runtime.strategy.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IDeleteTextAssetInstanceStrategy extends
IRuntimeStrategy<IIdsListParameterModel, IDeleteInstancesResponseModel>{
  
}
