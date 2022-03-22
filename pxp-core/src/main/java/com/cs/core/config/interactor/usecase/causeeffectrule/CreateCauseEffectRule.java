package com.cs.core.config.interactor.usecase.causeeffectrule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.strategy.usecase.causeeffectrule.ICreateCauseEffectRuleStrategy;

@Service
public class CreateCauseEffectRule
    extends AbstractCreateConfigInteractor<ICauseEffectRulesModel, ICauseEffectRulesModel>
    implements ICreateCauseEffectRule {
  
  @Autowired
  ICreateCauseEffectRuleStrategy orientCreateCauseEffectRuleStrategy;
  
  public ICauseEffectRulesModel executeInternal(ICauseEffectRulesModel causeEffectRuleModel)
      throws Exception
  {
    ICauseEffectRulesModel savedCauseEffectRuleModel = orientCreateCauseEffectRuleStrategy
        .execute(causeEffectRuleModel);
    
    return savedCauseEffectRuleModel;
  }
}
