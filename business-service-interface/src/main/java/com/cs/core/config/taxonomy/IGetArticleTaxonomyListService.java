package com.cs.core.config.taxonomy;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.taxonomy.IGetArticleTaxonomyListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetArticleTaxonomyListService
    extends IGetConfigService<IModel, IGetArticleTaxonomyListModel> {
}
