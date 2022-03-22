package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.taxonomy.IGetArticleTaxonomyListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetKlassTaxonomyListStrategy
    extends IConfigStrategy<IModel, IGetArticleTaxonomyListModel> {
  
}
