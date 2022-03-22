package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.goldenrecord.ICreateGoldenRecordRuleService;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;
import com.cs.core.config.strategy.usecase.goldenrecord.ICreateGoldenRecordRuleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateGoldenRecordRule extends AbstractCreateConfigInteractor<IGoldenRecordRuleModel, IGetGoldenRecordRuleModel>
    implements ICreateGoldenRecordRule {
  
  @Autowired
  protected ICreateGoldenRecordRuleService createGoldenRecordRuleService;
  
  @Override
  public IGetGoldenRecordRuleModel executeInternal(IGoldenRecordRuleModel model) throws Exception
  {
    return createGoldenRecordRuleService.execute(model);
  }
}
