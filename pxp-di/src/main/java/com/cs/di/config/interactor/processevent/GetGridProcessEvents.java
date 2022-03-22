package com.cs.di.config.interactor.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.businessapi.processevent.IGetGridProcessEventsService;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetGridProcessEventsResponseModel;

@Service("getGridProcessEvents")
public class GetGridProcessEvents extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridProcessEventsResponseModel>
    implements IGetGridProcessEvents {
  
  @Autowired
  protected IGetGridProcessEventsService getGridProcessEventsService;
  
  @Override
  public IGetGridProcessEventsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getGridProcessEventsService.execute(dataModel);
  }
}