package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAssetService extends AbstractGetConfigService<IIdParameterModel, IAssetModel> implements IGetAssetService {
  
  @Autowired
  IGetAssetStrategy neo4jGetAssetStrategy;
  
  @Override
  public IAssetModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return neo4jGetAssetStrategy.execute(idModel);
  }
}
