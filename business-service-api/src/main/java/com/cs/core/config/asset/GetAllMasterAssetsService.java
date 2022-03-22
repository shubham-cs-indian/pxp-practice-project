package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.asset.IGetAllMasterAssetsStrategy;

@Service
public class GetAllMasterAssetsService
    extends AbstractGetConfigService<IAssetModel, IListModel<IAssetModel>>
    implements IGetAllMasterAssetsService {
  
  @Autowired
  IGetAllMasterAssetsStrategy neo4jGetAllMasterAssetsStrategy;
  
  @Override
  public IListModel<IAssetModel> executeInternal(IAssetModel model) throws Exception
  {
    return neo4jGetAllMasterAssetsStrategy.execute(model);
  }
}
