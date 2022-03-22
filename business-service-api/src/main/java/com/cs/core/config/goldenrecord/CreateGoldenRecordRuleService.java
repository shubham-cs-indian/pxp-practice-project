package com.cs.core.config.goldenrecord;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;
import com.cs.core.config.strategy.usecase.goldenrecord.ICreateGoldenRecordRuleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateGoldenRecordRuleService extends AbstractCreateConfigService<IGoldenRecordRuleModel, IGetGoldenRecordRuleModel>
    implements ICreateGoldenRecordRuleService {
  
  @Autowired
  protected ICreateGoldenRecordRuleStrategy createGoldenRecordRuleStrategy;
  
  @Autowired
  protected GoldenRecordRuleValidations     goldenRecordRuleValidations;
  
  @Override
  public IGetGoldenRecordRuleModel executeInternal(IGoldenRecordRuleModel model) throws Exception
  {
    // API level validations
    goldenRecordRuleValidations.validate(model);
    
    return createGoldenRecordRuleStrategy.execute(model);
  }
}
