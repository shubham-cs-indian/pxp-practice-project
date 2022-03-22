package com.cs.core.config.interactor.usecase.causeeffectrule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.strategy.usecase.causeeffectrule.ISaveCauseEffectRuleStrategy;

@Service
public class SaveCauseEffectRule
    extends AbstractSaveConfigInteractor<ICauseEffectRulesModel, ICauseEffectRulesModel>
    implements ISaveCauseEffectRule {
  
  @Autowired
  ISaveCauseEffectRuleStrategy orientSaveCauseEffectRuleStrategy;
  
  @Override
  public ICauseEffectRulesModel executeInternal(ICauseEffectRulesModel CauseEffectRuleModel)
      throws Exception
  {
    
    return orientSaveCauseEffectRuleStrategy.execute(CauseEffectRuleModel);
  }
}
