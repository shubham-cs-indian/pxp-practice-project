package com.cs.core.runtime.strategy.usecase.transfer.sourcedestination;

import com.cs.core.config.interactor.model.processdetails.IGetAllInstanceIdByProcessIdsModel;
import com.cs.core.config.interactor.model.processdetails.ISourceDestinationResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetSourceDestinationIdsByIdsStrategy
    extends IConfigStrategy<IGetAllInstanceIdByProcessIdsModel, ISourceDestinationResponseModel> {
  
}
