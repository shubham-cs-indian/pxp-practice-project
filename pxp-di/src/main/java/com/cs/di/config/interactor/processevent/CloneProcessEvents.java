package com.cs.di.config.interactor.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.di.config.businessapi.processevent.ICloneProcessEventsService;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;

@Service
public class CloneProcessEvents
    extends AbstractCreateConfigInteractor<IListModel<IConfigCloneEntityInformationModel>, IBulkProcessEventSaveResponseModel>
    implements ICloneProcessEvents {
  
  @Autowired
  protected ICloneProcessEventsService cloneProcessEventsService;

  
  /**
   * This function will fetch src processes and then create new process models
   * respectively and send a call to plugin to create those entries in database
   */
  @Override
  public IBulkProcessEventSaveResponseModel executeInternal(IListModel<IConfigCloneEntityInformationModel> newProcessListInfoModels)
      throws Exception
  {
    return cloneProcessEventsService.execute(newProcessListInfoModels);

  }
}
