package com.cs.core.config.interactor.usecase.rulelist;

import com.cs.core.config.rulelist.IGetRuleListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.strategy.usecase.rulelist.IGetRuleListStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetRuleList extends AbstractGetConfigInteractor<IIdParameterModel, IRuleListModel>
    implements IGetRuleList {
  
  @Autowired
  protected IGetRuleListService getRuleListService;
  
  @Override
  public IRuleListModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getRuleListService.execute(dataModel);
  }
}
