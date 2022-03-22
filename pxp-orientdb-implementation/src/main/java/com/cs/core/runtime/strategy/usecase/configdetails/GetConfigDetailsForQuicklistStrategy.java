package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForQuickListModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForQuickListModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import org.springframework.stereotype.Component;

@Component("getConfigDetailsForQuicklistStrategy")
public class GetConfigDetailsForQuicklistStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForQuicklistStrategy {
  
  @Override
  public IConfigDetailsForQuickListModel execute(IGetTargetKlassesModel model) throws Exception
  {
    String userId = transactionThread.getTransactionData().getUserId();
    model.setUserId(userId);
    return execute(GET_CONFIG_DETAILS_FOR_QUICKLIST, model, ConfigDetailsForQuickListModel.class);
  }
}
