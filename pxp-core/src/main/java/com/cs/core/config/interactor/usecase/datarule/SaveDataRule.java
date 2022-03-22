package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.datarule.ISaveDataRuleService;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.datarule.ISaveDataRuleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveDataRule extends AbstractSaveConfigInteractor<ISaveDataRuleModel, IDataRuleModel>
    implements ISaveDataRule {
  
  @Autowired
  protected ISaveDataRuleService saveDataRuleService;

  @Override
  public IDataRuleModel executeInternal(ISaveDataRuleModel saveDataRuleModel) throws Exception
  {
    return saveDataRuleService.execute(saveDataRuleModel);
  }
  
 }