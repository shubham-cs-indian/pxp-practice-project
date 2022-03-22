package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.usecase.textasset.IGetAllMasterTextAssetsStrategy;
import com.cs.core.config.textasset.IGetAllMasterTextAssetsService;

@Service
public class GetAllMasterTextAssetsService
    extends AbstractGetConfigService<ITextAssetModel, IListModel<ITextAssetModel>>
    implements IGetAllMasterTextAssetsService {
  
  @Autowired
  IGetAllMasterTextAssetsStrategy getAllMasterTextAssetsStrategy;
  
  @Override
  public IListModel<ITextAssetModel> executeInternal(ITextAssetModel model) throws Exception
  {
    return getAllMasterTextAssetsStrategy.execute(model);
  }
}
