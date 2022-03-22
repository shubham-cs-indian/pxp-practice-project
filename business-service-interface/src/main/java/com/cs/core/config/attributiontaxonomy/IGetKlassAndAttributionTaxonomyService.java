package com.cs.core.config.attributiontaxonomy;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;

public interface IGetKlassAndAttributionTaxonomyService
    extends IGetConfigService<IGetTaxonomyRequestModel, IGetMasterTaxonomyWithoutKPModel> {
  
}
