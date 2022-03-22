package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.asset.IDeleteAssetService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteAsset extends
    AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> implements IDeleteAsset {
  
  @Autowired
  protected IDeleteAssetService deleteAssetService;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model)
      throws Exception
  {
    return deleteAssetService.execute(model);
  }
}
