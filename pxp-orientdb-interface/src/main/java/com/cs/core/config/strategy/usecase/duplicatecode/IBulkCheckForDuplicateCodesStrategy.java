package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkCheckForDuplicateCodesStrategy extends
    IConfigStrategy<IListModel<IBulkCheckForDuplicateCodesModel>, IBulkCheckForDuplicateCodesReturnModel> {
  
}
