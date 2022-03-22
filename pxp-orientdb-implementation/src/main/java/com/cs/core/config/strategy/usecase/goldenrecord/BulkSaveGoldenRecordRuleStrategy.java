package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.BulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BulkSaveGoldenRecordRuleStrategy extends OrientDBBaseStrategy
    implements IBulkSaveGoldenRecordRuleStrategy {
  
  @Override
  public IBulkSaveGoldenRecordRuleResponseModel execute(IListModel<IIdLabelCodeModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(OrientDBBaseStrategy.BULK_SAVE_GOLDEN_RECORD_RULE, requestMap,
        BulkSaveGoldenRecordRuleResponseModel.class);
  }
}
