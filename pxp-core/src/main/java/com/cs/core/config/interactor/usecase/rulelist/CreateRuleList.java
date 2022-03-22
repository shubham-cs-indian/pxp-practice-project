package com.cs.core.config.interactor.usecase.rulelist;

import com.cs.core.config.rulelist.ICreateRuleListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.strategy.usecase.rulelist.ICreateRuleListStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CreateRuleList extends AbstractCreateConfigInteractor<IRuleListModel, IRuleListModel>
    implements ICreateRuleList {
  
  @Autowired
  protected ICreateRuleListService createRuleListService;
  
  public IRuleListModel executeInternal(IRuleListModel dataRuleModel) throws Exception
  {
    return createRuleListService.execute(dataRuleModel);
  }
}
