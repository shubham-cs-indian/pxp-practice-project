package com.cs.core.config.strategy.usecase.causeeffectrule;

import com.cs.core.config.interactor.model.causeeffectrule.CauseEffectRulesModel;
import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveCauseEffectRuleStrategy extends OrientDBBaseStrategy
    implements ISaveCauseEffectRuleStrategy {
  
  public static final String useCase = "SaveCauseEffectRule";
  
  @Override
  public ICauseEffectRulesModel execute(ICauseEffectRulesModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("causeEffectRule", model);
    return super.execute(useCase, requestMap, CauseEffectRulesModel.class);
  }
}
