package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.asset.IGetAllAssetsService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;

@Service
public class GetAllAssets
    extends AbstractGetConfigInteractor<IAssetModel, IListModel<IKlassInformationModel>>
    implements IGetAllAssets {
  
  @Autowired
  IGetAllAssetsService getAllAssetsService;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(IAssetModel model) throws Exception
  {
    return getAllAssetsService.execute(model);
  }
}
