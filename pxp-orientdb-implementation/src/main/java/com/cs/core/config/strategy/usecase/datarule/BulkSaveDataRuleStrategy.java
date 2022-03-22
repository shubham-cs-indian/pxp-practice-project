package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.BulkSaveDataRuleResponseModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BulkSaveDataRuleStrategy extends OrientDBBaseStrategy
    implements IBulkSaveDataRuleStrategy {
  
  @Override
  public IBulkSaveDataRuleResponseModel execute(IListModel<IBulkSaveDataRuleModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(OrientDBBaseStrategy.BULK_SAVE_DATA_RULE, requestMap,
        BulkSaveDataRuleResponseModel.class);
  }
}
