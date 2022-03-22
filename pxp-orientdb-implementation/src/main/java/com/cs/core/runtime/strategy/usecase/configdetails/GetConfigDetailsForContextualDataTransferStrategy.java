package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.contextdatatransfer.GetConfigDetailsForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IGetConfigDetailsForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeInfoModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForContextualDataTransferStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForContextualDataTransferStrategy {
  
  @Override
  public IGetConfigDetailsForContextualDataTransferModel execute(IKlassInstanceTypeInfoModel model)
      throws Exception
  {
    return execute("GetConfigDetailsForContextualDataTransfer", model,
        GetConfigDetailsForContextualDataTransferModel.class);
  }
}
