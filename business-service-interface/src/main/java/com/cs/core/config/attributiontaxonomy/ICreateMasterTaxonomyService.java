package com.cs.core.config.attributiontaxonomy;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;

public interface ICreateMasterTaxonomyService
    extends ICreateConfigService<ICreateMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel> {
  
}
