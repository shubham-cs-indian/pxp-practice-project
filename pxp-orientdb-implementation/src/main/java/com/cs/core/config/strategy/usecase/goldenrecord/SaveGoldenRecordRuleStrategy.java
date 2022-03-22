package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.GetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaveGoldenRecordRuleStrategy extends OrientDBBaseStrategy
    implements ISaveGoldenRecordRuleStrategy {
  
  @Override
  public IGetGoldenRecordRuleModel execute(ISaveGoldenRecordRuleModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.SAVE_GOLDEN_RECORD_RULE, model,
        GetGoldenRecordRuleModel.class);
  }
}
