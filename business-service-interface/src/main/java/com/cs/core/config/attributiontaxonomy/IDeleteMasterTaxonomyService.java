package com.cs.core.config.attributiontaxonomy;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteMasterTaxonomyService
    extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
