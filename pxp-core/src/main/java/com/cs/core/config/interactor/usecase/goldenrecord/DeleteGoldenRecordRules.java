package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.core.config.goldenrecord.IDeleteGoldenRecordRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkDeleteGoldenRecordRuleStrategyModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkDeleteGoldenRecordRuleSuccessStrategyModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IDeleteGoldenRecordRulesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteGoldenRecordRules extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteGoldenRecordRules {
  
  @Autowired
  protected IDeleteGoldenRecordRulesService deleteGoldenRecordRulesService;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return deleteGoldenRecordRulesService.execute(dataModel);
  }

}
