package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.BulkSaveKpiRuleResponseModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BulkSaveKpiRuleStrategy extends OrientDBBaseStrategy
    implements IBulkSaveKpiRuleStrategy {
  
  @Override
  public IBulkSaveKpiRuleResponseModel execute(IListModel<IBulkSaveKpiRuleRequestModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(OrientDBBaseStrategy.BULK_SAVE_KPI_RULE, requestMap,
        BulkSaveKpiRuleResponseModel.class);
  }
}
