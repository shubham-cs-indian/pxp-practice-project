package com.cs.core.config.interactor.usecase.causeeffectrule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.strategy.usecase.causeeffectrule.IGetCauseEffectRuleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetCauseEffectRule
    extends AbstractGetConfigInteractor<IIdParameterModel, ICauseEffectRulesModel>
    implements IGetCauseEffectRule {
  
  @Autowired
  IGetCauseEffectRuleStrategy getCauseEffectRuleStrategy;
  
  @Override
  public ICauseEffectRulesModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getCauseEffectRuleStrategy.execute(dataModel);
  }
}
