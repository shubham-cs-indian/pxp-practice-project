package com.cs.core.config.interactor.usecase.tag;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;

public interface IGetGridTags
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetTagGridResponseModel> {
  
}
