package com.cs.core.config.datarule;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.strategy.usecase.datarule.IGetDataRuleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDataRuleService extends AbstractGetConfigService<IIdParameterModel, IDataRuleModel>
    implements IGetDataRuleService {
  
  @Autowired
  IGetDataRuleStrategy orientDBGetDataRuleStrategy;
  
  @Override
  public IDataRuleModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return orientDBGetDataRuleStrategy.execute(dataModel);
  }
}
