package com.cs.core.config.goldenrecord;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IGetGoldenRecordRuleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetGoldenRecordRuleService extends AbstractGetConfigService<IIdParameterModel, IGetGoldenRecordRuleModel>
    implements IGetGoldenRecordRuleService {
  
  @Autowired
  protected IGetGoldenRecordRuleStrategy getGoldenRecordRuleStrategy;
  
  @Override
  public IGetGoldenRecordRuleModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getGoldenRecordRuleStrategy.execute(model);
  }
}
