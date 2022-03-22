package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;

public interface IGetKlassAndAttributionTaxonomy
    extends IGetConfigInteractor<IGetTaxonomyRequestModel, IGetMasterTaxonomyWithoutKPModel> {
  
}
