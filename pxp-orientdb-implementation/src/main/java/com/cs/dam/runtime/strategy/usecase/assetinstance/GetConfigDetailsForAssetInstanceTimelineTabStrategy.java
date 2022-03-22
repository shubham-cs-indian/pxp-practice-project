package com.cs.dam.runtime.strategy.usecase.assetinstance;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import org.springframework.stereotype.Component;

@Component("getConfigDetailsForAssetInstanceTimlineTab")
public class GetConfigDetailsForAssetInstanceTimelineTabStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForAssetInstanceTimelineTabStrategy {

  
  @Override
  public IGetConfigDetailsForCustomTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    String userId = transactionThread.getTransactionData().getUserId();
    model.setUserId(userId);
    return execute(GET_CONFIG_DETAILS_FOR_ASSET_INSTANCE_TIMELINE_TAB, model,
        GetConfigDetailsForCustomTabModel.class);
  }
}
