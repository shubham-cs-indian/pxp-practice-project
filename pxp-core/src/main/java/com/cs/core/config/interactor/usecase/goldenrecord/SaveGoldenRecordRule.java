package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.goldenrecord.ISaveGoldenRecordRuleService;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveGoldenRecordRule extends AbstractSaveConfigInteractor<ISaveGoldenRecordRuleModel, IGetGoldenRecordRuleModel>
    implements ISaveGoldenRecordRule {
  
  @Autowired
  protected ISaveGoldenRecordRuleService saveGoldenRecordRuleService;

  @Override
  public IGetGoldenRecordRuleModel executeInternal(ISaveGoldenRecordRuleModel model) throws Exception
  {
    return saveGoldenRecordRuleService.execute(model);
  }
}
