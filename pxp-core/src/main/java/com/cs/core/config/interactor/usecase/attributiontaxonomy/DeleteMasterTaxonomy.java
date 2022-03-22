package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.attributiontaxonomy.IDeleteMasterTaxonomyService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteMasterTaxonomy
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteMasterTaxonomy {
  
  @Autowired
  protected IDeleteMasterTaxonomyService deleteMasterTaxonomyService;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    
    return deleteMasterTaxonomyService.execute(model);
  }
  
}
