package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.attributiontaxonomy.ICreateMasterTaxonomyService;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;

@Service
public class CreateMasterTaxonomy
    extends AbstractCreateConfigInteractor<ICreateMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel>
    implements ICreateMasterTaxonomy {
  
  @Autowired
  protected ICreateMasterTaxonomyService createMasterTaxonomyService;

  @Override
  protected IGetMasterTaxonomyWithoutKPModel executeInternal(ICreateMasterTaxonomyModel model)
      throws Exception
  {
    return createMasterTaxonomyService.execute(model);
  }
}
