package com.cs.core.config.taxonomy;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;

public interface IGetImmediateChildMajorTaxonomiesByParentIdService extends
    IGetConfigService<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel> {
}
