package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.datarule.ICreateDataRuleService;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateDataRule extends AbstractCreateConfigInteractor<IDataRuleModel, IDataRuleModel>
    implements ICreateDataRule {
  
  @Autowired
  protected ICreateDataRuleService createDataRuleService;
  
  public IDataRuleModel executeInternal(IDataRuleModel dataRuleModel) throws Exception
  {
    return createDataRuleService.execute(dataRuleModel);
  }
}
