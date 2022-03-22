package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.filter.IKlassInstanceQuickListModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IQuickListKlassInstanceForAssetStrategy extends
    IRuntimeStrategy<IKlassInstanceQuickListModel, IListModel<IKlassInstanceInformationModel>> {
  
}
