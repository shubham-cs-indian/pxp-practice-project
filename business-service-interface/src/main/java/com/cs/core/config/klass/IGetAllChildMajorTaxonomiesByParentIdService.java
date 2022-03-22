package com.cs.core.config.klass;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;

public interface IGetAllChildMajorTaxonomiesByParentIdService
    extends IGetConfigService<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel> {
  
}