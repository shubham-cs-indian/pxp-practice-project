package com.cs.core.config.strategy.usecase.causeeffectrule;

import com.cs.core.config.interactor.model.causeeffectrule.CauseEffectRulesModel;
import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateCauseEffectRuleStrategy extends OrientDBBaseStrategy
    implements ICreateCauseEffectRuleStrategy {
  
  public static final String useCase = "CreateCauseEffectRule";
  
  @Override
  public ICauseEffectRulesModel execute(ICauseEffectRulesModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("causeEffectRule", model);
    return super.execute(useCase, requestMap, CauseEffectRulesModel.class);
  }
}
