package com.cs.core.config.strategy.usecase.klass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.GetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForSaveRelationshipInstancesStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class GetConfigDetailsForSaveRelationshipInstancesStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForSaveRelationshipInstancesStrategy {
  
  
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Override
  @SuppressWarnings("unchecked")
  public IGetConfigDetailsForSaveRelationshipInstancesResponseModel execute(
      IGetConfigDetailsForSaveRelationshipInstancesRequestModel model) throws Exception
  {
    String userId = transactionThread.getTransactionData().getUserId();
    Map<String, Object> map = ObjectMapperUtil.convertValue(model, HashMap.class);
    map.put("userId", userId);
    return execute(GET_CONFIG_DETAILS_FOR_SAVE_RELATIONSHIP_INSTANCES, map,
        GetConfigDetailsForSaveRelationshipInstancesResponseModel.class);
  }
}
