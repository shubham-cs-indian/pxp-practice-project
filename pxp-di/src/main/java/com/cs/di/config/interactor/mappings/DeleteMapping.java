package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.mappings.IDeleteMappingService;

@Service
public class DeleteMapping
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteMapping {

  @Autowired
  protected IDeleteMappingService deleteMappingService;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return deleteMappingService.execute(dataModel);
  }

}
