package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.core.config.goldenrecord.IGetGoldenRecordRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IGetGoldenRecordRuleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetGoldenRecordRule extends AbstractGetConfigInteractor<IIdParameterModel, IGetGoldenRecordRuleModel>
    implements IGetGoldenRecordRule {
  
  @Autowired
  protected IGetGoldenRecordRuleService getGoldenRecordRuleService;
  
  @Override
  public IGetGoldenRecordRuleModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getGoldenRecordRuleService.execute(model);
  }
}
