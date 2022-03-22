package com.cs.core.runtime.strategy.usecase.variants;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInfoForDataTransferRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInfoForDataTransferResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetImmediateVariantsDetailStrategy extends
IRuntimeStrategy<IGetVariantInfoForDataTransferRequestModel, IListModel<IGetVariantInfoForDataTransferResponseModel>> {
  
}
