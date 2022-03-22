package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.attributiontaxonomy.IBulkCreateTaxonomyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

public interface IBulkCreateMasterTaxonomy extends
    ICreateConfigInteractor<IListModel<ICreateMasterTaxonomyModel>, IBulkCreateTaxonomyResponseModel> {
  
}
