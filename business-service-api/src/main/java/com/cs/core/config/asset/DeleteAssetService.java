package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.klass.AbstractDeleteKlassesService;
import com.cs.core.config.strategy.usecase.asset.IDeleteAssetsStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteAssetService extends
    AbstractDeleteKlassesService<IIdsListParameterModel, IBulkDeleteReturnModel> implements IDeleteAssetService {
  
  @Autowired
  protected IDeleteAssetsStrategy neo4jDeleteAssetsStrategy;
  
  @Override
  protected String getKlassType()
  {
    return Constants.ASSET_KLASS_TYPE;
  }
  
  @Override
  protected IBulkDeleteKlassResponseModel executeBulkDelete(IIdsListParameterModel model)
      throws Exception
  {
    return neo4jDeleteAssetsStrategy.execute(model);
  }
}
