package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.datarule.IGetDataRuleService;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDataRule extends AbstractGetConfigInteractor<IIdParameterModel, IDataRuleModel>
    implements IGetDataRule {
  
  @Autowired
  IGetDataRuleService getDataRuleService;
  
  @Override
  public IDataRuleModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getDataRuleService.execute(dataModel);
  }
}
