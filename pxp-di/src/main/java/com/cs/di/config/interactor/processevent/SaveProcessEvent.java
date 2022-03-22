
package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.di.config.businessapi.processevent.ISaveProcessEventService;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveProcessEvent extends AbstractSaveConfigInteractor<ISaveProcessEventModel, ICreateOrSaveProcessEventResponseModel>
    implements ISaveProcessEvent {
  
  @Autowired
  protected ISaveProcessEventService saveProcessEventService;

  @Override
  public ICreateOrSaveProcessEventResponseModel executeInternal(ISaveProcessEventModel model) throws Exception
  {
    return saveProcessEventService.execute(model);
  }
}
