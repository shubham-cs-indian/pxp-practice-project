package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.filter.IKlassInstanceQuickListModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IQuickListKlassInstancesStrategy extends
    IRuntimeStrategy<IKlassInstanceQuickListModel, IListModel<IKlassInstanceInformationModel>> {
  
}
