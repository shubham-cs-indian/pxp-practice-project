package com.cs.core.runtime.interactor.usecase.klass;


import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetChildTaxonomiesByParentIdWithPaginationStrategy
    extends IConfigStrategy<IIdPaginationModel, ITaxonomyInformationModel> {
  
}