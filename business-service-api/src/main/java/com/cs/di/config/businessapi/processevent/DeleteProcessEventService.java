package com.cs.di.config.businessapi.processevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.strategy.cammunda.broadcast.IDeleteProcessDefinationStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.IDeleteProcessEventResponseModel;
import com.cs.di.config.strategy.usecase.processevent.IDeleteProcessEventStrategy;

@Service
public class DeleteProcessEventService extends AbstractDeleteConfigService<IIdsListParameterModel, IDeleteProcessEventResponseModel>
    implements IDeleteProcessEventService {
  
  @Autowired
  protected IDeleteProcessEventStrategy      deleteProcessEventStrategy;
  
  @Autowired
  protected IDeleteProcessDefinationStrategy deleteProcessDefinationStrategy;
  
  @Override
  public IDeleteProcessEventResponseModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    IDeleteProcessEventResponseModel bulkDeleteReturnModel = deleteProcessEventStrategy.execute(dataModel);
    // jmsConsumerConfig.removeListener(dataModel.getIds());
    IdsListParameterModel listModel = new IdsListParameterModel();
    listModel.getIds().addAll(bulkDeleteReturnModel.getProcessDefinationIds());
    deleteProcessDefinationStrategy.execute(listModel);
    return bulkDeleteReturnModel;
  }
}
