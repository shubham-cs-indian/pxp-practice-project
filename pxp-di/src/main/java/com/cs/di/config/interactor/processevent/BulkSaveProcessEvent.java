package com.cs.di.config.interactor.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.di.config.businessapi.processevent.IBulkSaveProcessEventService;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

@Service
public class BulkSaveProcessEvent extends
    AbstractSaveConfigInteractor<IListModel<ISaveProcessEventModel>, IBulkProcessEventSaveResponseModel> implements IBulkSaveProcessEvent {
  
  @Autowired
  protected IBulkSaveProcessEventService bulkSaveProcessEventService;
  
  @Override
  public IBulkProcessEventSaveResponseModel executeInternal(IListModel<ISaveProcessEventModel> saveProcessesModel) throws Exception
  {
    return bulkSaveProcessEventService.execute(saveProcessesModel);
  }
}