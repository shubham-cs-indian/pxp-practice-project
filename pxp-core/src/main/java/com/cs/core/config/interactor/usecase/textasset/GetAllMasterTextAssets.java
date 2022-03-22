package com.cs.core.config.interactor.usecase.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.textasset.IGetAllMasterTextAssetsService;

@Service
public class GetAllMasterTextAssets
    extends AbstractGetConfigInteractor<ITextAssetModel, IListModel<ITextAssetModel>>
    implements IGetAllMasterTextAssets {
  
  @Autowired
  IGetAllMasterTextAssetsService getAllMasterTextAssetsService;
  
  @Override
  public IListModel<ITextAssetModel> executeInternal(ITextAssetModel model) throws Exception
  {
    return getAllMasterTextAssetsService.execute(model);
  }
}
