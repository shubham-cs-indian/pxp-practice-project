package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.GetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateGoldenRecordRuleStrategy extends OrientDBBaseStrategy
    implements ICreateGoldenRecordRuleStrategy {
  
  @Override
  public IGetGoldenRecordRuleModel execute(IGoldenRecordRuleModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.CREATE_GOLDEN_RECORD_RULE, model,
        GetGoldenRecordRuleModel.class);
  }
}
