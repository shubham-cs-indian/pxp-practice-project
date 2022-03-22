package com.cs.core.config.textasset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;

public interface IGetAllTextAssetsService
    extends IGetConfigService<ITextAssetModel, IListModel<IKlassInformationModel>> {
  
}
