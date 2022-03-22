package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;

public interface ISaveMasterTaxonomy
    extends ISaveConfigInteractor<ISaveMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel> {
  
}
