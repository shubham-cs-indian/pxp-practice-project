package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.klass.AbstractDeleteKlassesService;
import com.cs.core.config.strategy.usecase.textasset.IDeleteTextAssetsStrategy;
import com.cs.core.config.textasset.IDeleteTextAssetService;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteTextAssetService
    extends AbstractDeleteKlassesService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteTextAssetService {
  
  @Autowired
  protected IDeleteTextAssetsStrategy deleteTextAssetsStrategy;

  @Override
  protected String getKlassType()
  {
    return Constants.TEXT_ASSET_KLASS_TYPE;
  }
  
  @Override
  protected IBulkDeleteKlassResponseModel executeBulkDelete(IIdsListParameterModel model)
      throws Exception
  {
    return deleteTextAssetsStrategy.execute(model);
  }
}
