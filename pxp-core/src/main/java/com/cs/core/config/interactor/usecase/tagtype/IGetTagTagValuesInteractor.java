package com.cs.core.config.interactor.usecase.tagtype;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesResponseModel;

public interface IGetTagTagValuesInteractor
    extends IGetConfigInteractor<IGetTagTagValuesRequestModel, IGetTagTagValuesResponseModel> {
  
}
