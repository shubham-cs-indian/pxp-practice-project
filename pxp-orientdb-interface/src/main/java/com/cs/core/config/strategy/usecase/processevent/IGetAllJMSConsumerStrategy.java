package com.cs.core.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;

public interface IGetAllJMSConsumerStrategy {
  
  public IListModel<IGetProcessEventModel> execute() throws Exception;
}
