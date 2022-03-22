package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.attributiontaxonomy.IGetMasterTaxonomyService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetMasterTaxonomyStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetMasterTaxonomy
    extends AbstractGetConfigInteractor<IGetTaxonomyRequestModel, IGetMasterTaxonomyWithoutKPModel>
    implements IGetMasterTaxonomy {
  
  @Autowired
  protected IGetMasterTaxonomyService getMasterTaxonomyService;
  
  @Override
  public IGetMasterTaxonomyWithoutKPModel executeInternal(IGetTaxonomyRequestModel dataModel)
      throws Exception
  {
    return getMasterTaxonomyService.execute(dataModel);
  }
  
}
