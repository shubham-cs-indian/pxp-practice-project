package com.cs.di.config.businessapi.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetGridProcessEventsResponseModel;
import com.cs.di.config.strategy.usecase.processevent.IGetGridProcessEventsStrategy;

@Service
public class GetGridProcessEventsService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetGridProcessEventsResponseModel>
    implements IGetGridProcessEventsService {
  
  @Autowired
  protected IGetGridProcessEventsStrategy getGridProcessEventsStrategy;
  
  @Override
  public IGetGridProcessEventsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getGridProcessEventsStrategy.execute(dataModel);
  }
}