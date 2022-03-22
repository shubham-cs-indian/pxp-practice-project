package com.cs.core.runtime.interactor.usecase.taxonomy;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.taxonomy.IGetArticleTaxonomyListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetArticleTaxonomyList
    extends IGetConfigInteractor<IModel, IGetArticleTaxonomyListModel> {
}
