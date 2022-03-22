package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.GetAllGoldenRecordRulesModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetAllGoldenRecordRulesModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetAllGoldenRecordRulesStrategy extends OrientDBBaseStrategy
    implements IGetAllGoldenRecordRulesStrategy {
  
  @Override
  public IGetAllGoldenRecordRulesModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_ALL_GOLDEN_RECORD_RULES, model,
        GetAllGoldenRecordRulesModel.class);
  }
}
