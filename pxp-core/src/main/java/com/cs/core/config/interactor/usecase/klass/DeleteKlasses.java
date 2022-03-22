package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.klass.IDeleteKlassService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteKlasses extends
    AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel> implements IDeleteKlass {
  
  @Autowired
  protected IDeleteKlassService deleteKlassesService;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model)
      throws Exception
  {
    return deleteKlassesService.execute(model);
  }
}
