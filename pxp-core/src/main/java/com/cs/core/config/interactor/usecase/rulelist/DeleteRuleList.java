package com.cs.core.config.interactor.usecase.rulelist;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.rulelist.IDeleteRuleListService;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteRuleList extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteRuleList {
  
  @Autowired
  protected IDeleteRuleListService deleteRuleListService;
  
  @Autowired
  protected IGetEntityConfigurationStrategy                 getRuleListEntityConfigurationStrategy;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return deleteRuleListService.execute(dataModel);
  }
}
