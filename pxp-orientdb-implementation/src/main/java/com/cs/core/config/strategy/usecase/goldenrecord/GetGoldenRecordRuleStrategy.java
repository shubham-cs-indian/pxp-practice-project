package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.GetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetGoldenRecordRuleStrategy extends OrientDBBaseStrategy
    implements IGetGoldenRecordRuleStrategy {
  
  @Override
  public IGetGoldenRecordRuleModel execute(IIdParameterModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_GOLDEN_RECORD_RULE, model,
        GetGoldenRecordRuleModel.class);
  }
}
