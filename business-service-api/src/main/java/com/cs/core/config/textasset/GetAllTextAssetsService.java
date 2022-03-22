package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.usecase.textasset.IGetAllTextAssetsStrategy;
import com.cs.core.config.textasset.IGetAllTextAssetsService;

@Service
public class GetAllTextAssetsService
    extends AbstractGetConfigService<ITextAssetModel, IListModel<IKlassInformationModel>>
    implements IGetAllTextAssetsService {
  
  @Autowired
  IGetAllTextAssetsStrategy getAllTextAssetsStrategy;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(ITextAssetModel model) throws Exception
  {
    return getAllTextAssetsStrategy.execute(model);
  }
}
