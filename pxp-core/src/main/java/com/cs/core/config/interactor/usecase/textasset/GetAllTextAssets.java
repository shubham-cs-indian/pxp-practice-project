package com.cs.core.config.interactor.usecase.textasset;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.usecase.textasset.IGetAllTextAssetsStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllTextAssets
    extends AbstractGetConfigInteractor<ITextAssetModel, IListModel<IKlassInformationModel>>
    implements IGetAllTextAssets {
  
  @Autowired
  IGetAllTextAssetsStrategy getAllTextAssetsStrategy;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(ITextAssetModel model) throws Exception
  {
    return getAllTextAssetsStrategy.execute(model);
  }
}
