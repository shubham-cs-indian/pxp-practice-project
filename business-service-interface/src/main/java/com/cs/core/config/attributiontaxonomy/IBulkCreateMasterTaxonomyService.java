package com.cs.core.config.attributiontaxonomy;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.attributiontaxonomy.IBulkCreateTaxonomyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

public interface IBulkCreateMasterTaxonomyService extends
    ICreateConfigService<IListModel<ICreateMasterTaxonomyModel>, IBulkCreateTaxonomyResponseModel> {
  
}
