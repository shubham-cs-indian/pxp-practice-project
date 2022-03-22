package com.cs.di.config.interactor.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.businessapi.processevent.IGetAllProcessEventsService;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;

@Service("getAllProcessEvents")
public class GetAllProcessEvents extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllProcessEventsResponseModel>
    implements IGetAllProcessEvents {
  
  @Autowired
  protected IGetAllProcessEventsService getAllProcessEventsService;
  
  @Override
  public IGetAllProcessEventsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllProcessEventsService.execute(dataModel);
  }
}
