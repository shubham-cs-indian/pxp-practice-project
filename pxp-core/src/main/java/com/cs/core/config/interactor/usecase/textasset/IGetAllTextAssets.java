package com.cs.core.config.interactor.usecase.textasset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;

public interface IGetAllTextAssets
    extends IGetConfigInteractor<ITextAssetModel, IListModel<IKlassInformationModel>> {
  
}
