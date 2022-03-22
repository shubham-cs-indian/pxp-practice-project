package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.strategy.usecase.rulelist.ICreateRuleListStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateRuleListService extends AbstractCreateConfigService<IRuleListModel, IRuleListModel>
    implements ICreateRuleListService {
  
  @Autowired
  ICreateRuleListStrategy createRuleListStrategy;
  
  public IRuleListModel executeInternal(IRuleListModel dataRuleModel) throws Exception
  {
    String ruleListCode = dataRuleModel.getCode();
    if(StringUtils.isEmpty(ruleListCode)) {
      ruleListCode = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RULELIST.getPrefix());
    }
    dataRuleModel.setCode(ruleListCode);
    dataRuleModel.setId(ruleListCode);
    
    RuleListValidations.validateRuleList(dataRuleModel, true);
    IRuleListModel savedRuleList = createRuleListStrategy.execute(dataRuleModel);
    
    return savedRuleList;
  }
}
