package com.cs.core.config.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;

public interface IGetAllChildMajorTaxonomiesByParentId extends IGetConfigInteractor<IGetChildMajorTaxonomiesRequestModel, IGetMajorTaxonomiesResponseModel> {
  
}