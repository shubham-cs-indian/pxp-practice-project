package com.cs.core.config.attributiontaxonomy;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;

public interface ISaveMasterTaxonomyService
    extends ISaveConfigService<ISaveMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel> {
  
}
