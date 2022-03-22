package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.strategy.usecase.rulelist.IGetRuleListStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetRuleListService extends AbstractGetConfigService<IIdParameterModel, IRuleListModel>
    implements IGetRuleListService {
  
  @Autowired
  IGetRuleListStrategy getRuleListStrategy;
  
  @Override
  public IRuleListModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getRuleListStrategy.execute(dataModel);
  }
}
