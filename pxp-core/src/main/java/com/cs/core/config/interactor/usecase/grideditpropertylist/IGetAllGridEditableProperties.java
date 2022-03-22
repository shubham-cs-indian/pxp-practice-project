package com.cs.core.config.interactor.usecase.grideditpropertylist;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertiesResponseModel;

public interface IGetAllGridEditableProperties
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetGridEditPropertiesResponseModel> {
  
}
