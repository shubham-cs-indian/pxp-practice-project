package com.cs.core.config.interactor.usecase.duplicatecode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.duplicatecode.IBulkCheckForDuplicateCodesModel;
import com.cs.core.config.strategy.usecase.duplicatecode.IBulkCheckForDuplicateCodesReturnModel;
import com.cs.core.config.strategy.usecase.duplicatecode.IBulkCheckForDuplicateCodesStrategy;

@Service
public class BulkCheckForDuplicateCodes
    extends AbstractGetConfigInteractor<IListModel<IBulkCheckForDuplicateCodesModel>, IBulkCheckForDuplicateCodesReturnModel>
    implements IBulkCheckForDuplicateCodes {
  
  @Autowired
  protected IBulkCheckForDuplicateCodesStrategy bulkCheckForDuplicateCodesStrategy;
  
  @Override
  public IBulkCheckForDuplicateCodesReturnModel executeInternal(IListModel<IBulkCheckForDuplicateCodesModel> dataModel) throws Exception
  {
    return bulkCheckForDuplicateCodesStrategy.execute(dataModel);
  }
}
