package com.cs.core.config.interactor.usecase.causeeffectrule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.strategy.usecase.causeeffectrule.IDeleteCauseEffectRuleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteCauseEffectRule
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteCauseEffectRule {
  
  @Autowired
  IDeleteCauseEffectRuleStrategy orientjDeleteCauseEffectRuleStrategy;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return orientjDeleteCauseEffectRuleStrategy.execute(dataModel);
  }
}
