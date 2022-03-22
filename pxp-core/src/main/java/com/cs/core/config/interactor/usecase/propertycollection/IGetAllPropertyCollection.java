package com.cs.core.config.interactor.usecase.propertycollection;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionRequestModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionResponseModel;

public interface IGetAllPropertyCollection
    extends IGetConfigInteractor<IGetAllPropertyCollectionRequestModel, IGetAllPropertyCollectionResponseModel> {
  
}
