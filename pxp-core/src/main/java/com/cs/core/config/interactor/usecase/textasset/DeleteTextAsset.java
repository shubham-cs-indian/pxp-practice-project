package com.cs.core.config.interactor.usecase.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.textasset.IDeleteTextAssetService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteTextAsset
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteTextAsset {
  
  @Autowired
  protected IDeleteTextAssetService deleteTextAssetsService;

  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model)
      throws Exception
  {
    return deleteTextAssetsService.execute(model);
  }
}
