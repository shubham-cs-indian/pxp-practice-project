package com.cs.di.config.businessapi.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;
import com.cs.di.config.strategy.usecase.processevent.IGetAllProcessEventsStrategy;

@Service
public class GetAllProcessEventsService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllProcessEventsResponseModel>
    implements IGetAllProcessEventsService {
  
  @Autowired
  protected IGetAllProcessEventsStrategy getAllProcessEventsStrategy;
  
  @Override
  public IGetAllProcessEventsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllProcessEventsStrategy.execute(dataModel);
  }
}
