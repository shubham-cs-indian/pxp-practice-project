package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.strategy.usecase.asset.IGetAllAssetsStrategy;

@Service
public class GetAllAssetsService
    extends AbstractGetConfigService<IAssetModel, IListModel<IKlassInformationModel>>
    implements IGetAllAssetsService {
  
  @Autowired
  IGetAllAssetsStrategy neo4jGetAllAssetsStrategy;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(IAssetModel model) throws Exception
  {
    return neo4jGetAllAssetsStrategy.execute(model);
  }
}
