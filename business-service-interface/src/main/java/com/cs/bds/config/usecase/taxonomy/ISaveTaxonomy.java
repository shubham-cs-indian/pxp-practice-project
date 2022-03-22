package com.cs.bds.config.usecase.taxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;

public interface ISaveTaxonomy {
  
  public IGetMasterTaxonomyWithoutKPStrategyResponseModel execute(ISaveMasterTaxonomyModel requestModel) throws Exception;
}
