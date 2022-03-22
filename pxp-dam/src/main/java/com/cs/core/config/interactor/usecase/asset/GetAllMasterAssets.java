package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.asset.IGetAllMasterAssetsService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

@Service
public class GetAllMasterAssets
    extends AbstractGetConfigInteractor<IAssetModel, IListModel<IAssetModel>>
    implements IGetAllMasterAssets {
  
  @Autowired
  IGetAllMasterAssetsService getAllMasterAssetsService;
  
  @Override
  public IListModel<IAssetModel> executeInternal(IAssetModel model) throws Exception
  {
    return getAllMasterAssetsService.execute(model);
  }
}
