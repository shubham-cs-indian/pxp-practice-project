package com.cs.core.runtime.strategy.usecase.transfer.processdetails;

import com.cs.core.config.interactor.model.processdetails.IGetAllInstanceIdByProcessIdsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAllVariantIdsByProcessIdStrategy
    extends IConfigStrategy<IGetAllInstanceIdByProcessIdsModel, IIdsListParameterModel> {
  
}
