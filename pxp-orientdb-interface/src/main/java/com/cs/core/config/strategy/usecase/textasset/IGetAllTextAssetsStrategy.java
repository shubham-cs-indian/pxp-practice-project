package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllTextAssetsStrategy
    extends IConfigStrategy<ITextAssetModel, IListModel<IKlassInformationModel>> {
  
}
