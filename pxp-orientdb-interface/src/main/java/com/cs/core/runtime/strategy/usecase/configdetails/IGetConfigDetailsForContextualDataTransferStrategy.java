package com.cs.core.runtime.strategy.usecase.configdetails;


import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IGetConfigDetailsForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeInfoModel;

public interface IGetConfigDetailsForContextualDataTransferStrategy extends
    IConfigStrategy<IKlassInstanceTypeInfoModel, IGetConfigDetailsForContextualDataTransferModel> {
  
}
