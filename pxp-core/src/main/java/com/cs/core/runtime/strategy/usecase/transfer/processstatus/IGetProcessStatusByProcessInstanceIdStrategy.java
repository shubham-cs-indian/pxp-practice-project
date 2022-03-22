package com.cs.core.runtime.strategy.usecase.transfer.processstatus;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetProcessStatusByProcessInstanceIdStrategy
    extends IConfigStrategy<IProcessStatusDetailsModel, IListModel<IProcessStatusDetailsModel>> {
  
}
