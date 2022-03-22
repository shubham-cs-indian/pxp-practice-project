package com.cs.core.config.interactor.usecase.duplicatecode;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.duplicatecode.IBulkCheckForDuplicateCodesModel;
import com.cs.core.config.strategy.usecase.duplicatecode.IBulkCheckForDuplicateCodesReturnModel;

public interface IBulkCheckForDuplicateCodes
    extends IGetConfigInteractor<IListModel<IBulkCheckForDuplicateCodesModel>, IBulkCheckForDuplicateCodesReturnModel> {
  
}
