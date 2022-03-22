package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;

public interface ICreateMasterTaxonomy
    extends ICreateConfigInteractor<ICreateMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel> {
  
}
