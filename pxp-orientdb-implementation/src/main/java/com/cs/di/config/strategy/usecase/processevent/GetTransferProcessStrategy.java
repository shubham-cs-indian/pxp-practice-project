package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.strategy.usecase.dataintegration.IGetTransferProcessStrategy;
import org.springframework.stereotype.Component;

@Component("getTransferProcessStrategy")
public class GetTransferProcessStrategy extends OrientDBBaseStrategy implements IGetTransferProcessStrategy {
  
  @Override
  public IGetProcessEventModel execute(IIdParameterModel model) throws Exception
  {
    // return execute(GET_TRANSFER_PROCESS, model, GetProcessEventModel.class);
    return null;
  }
}
