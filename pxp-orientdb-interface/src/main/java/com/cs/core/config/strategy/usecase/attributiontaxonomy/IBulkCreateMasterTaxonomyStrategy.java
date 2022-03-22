package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.IBulkCreateTaxonomyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkCreateMasterTaxonomyStrategy extends
    IConfigStrategy<IListModel<ICreateMasterTaxonomyModel>, IBulkCreateTaxonomyResponseModel> {
  
}
