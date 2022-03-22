package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.BulkDeleteGoldenRecordRuleStrategyModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkDeleteGoldenRecordRuleStrategyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeleteGoldenRecordRuleStrategy extends OrientDBBaseStrategy
    implements IDeleteGoldenRecordRulesStrategy {
  
  @Override
  public IBulkDeleteGoldenRecordRuleStrategyModel execute(IIdsListParameterModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(OrientDBBaseStrategy.DELETE_GOLDEN_RECORD_RULE, requestMap,
        BulkDeleteGoldenRecordRuleStrategyModel.class);
  }
}
