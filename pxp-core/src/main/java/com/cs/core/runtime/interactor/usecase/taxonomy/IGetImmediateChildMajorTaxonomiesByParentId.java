package com.cs.core.runtime.interactor.usecase.taxonomy;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;

public interface IGetImmediateChildMajorTaxonomiesByParentId extends
    IGetConfigInteractor<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel> {
}
