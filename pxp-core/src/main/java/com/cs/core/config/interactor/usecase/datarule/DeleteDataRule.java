package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.datarule.IDeleteDataRuleService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteDataRule extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteDataRule {
  
  @Autowired
  protected IDeleteDataRuleService deleteDataRuleService;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return deleteDataRuleService.execute(dataModel);
  }

}
