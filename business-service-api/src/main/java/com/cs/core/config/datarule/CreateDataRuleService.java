package com.cs.core.config.datarule;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.businessapi.base.Validations;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.strategy.usecase.datarule.ICreateDataRuleStrategy;
import com.cs.core.exception.InvalidCodeException;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CreateDataRuleService extends AbstractCreateConfigService<IDataRuleModel, IDataRuleModel>
    implements ICreateDataRuleService {
  
  @Autowired
  protected ICreateDataRuleStrategy orientDBCreateDataRuleStrategy;
  
  public IDataRuleModel executeInternal(IDataRuleModel dataRuleModel) throws Exception
  {
    String dataRuleCode = dataRuleModel.getCode();
    if (StringUtils.isEmpty(dataRuleCode)) {
      dataRuleCode = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RULE.getPrefix());
      dataRuleModel.setCode(dataRuleCode);
    }else if (!Validations.isCodeValid(dataRuleCode)) {
      throw new InvalidCodeException();
    }
    return orientDBCreateDataRuleStrategy.execute(dataRuleModel);
  }
}
