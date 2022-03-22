package com.cs.di.config.interactor.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.businessapi.processevent.IDeleteProcessEventService;
import com.cs.di.config.interactor.model.initializeworflowevent.IDeleteProcessEventResponseModel;

@Service
public class DeleteProcessEvent extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IDeleteProcessEventResponseModel>
    implements IDeleteProcessEvent {
  
  @Autowired
  protected IDeleteProcessEventService deleteProcessEventService;
  
  @Override
  public IDeleteProcessEventResponseModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return deleteProcessEventService.execute(dataModel);
  }
}
