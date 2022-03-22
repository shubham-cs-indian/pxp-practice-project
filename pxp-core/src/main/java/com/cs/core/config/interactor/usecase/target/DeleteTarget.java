package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.interactor.usecase.klass.AbstractDeleteKlasses;
import com.cs.core.config.strategy.usecase.target.IDeleteTargetsStrategy;
import com.cs.core.config.target.IDeleteTargetService;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteTarget extends
    AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> implements IDeleteTarget {
  
  @Autowired
  protected IDeleteTargetService deleteTargetService;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model)
      throws Exception
  {
    return deleteTargetService.execute(model);
  }
}
