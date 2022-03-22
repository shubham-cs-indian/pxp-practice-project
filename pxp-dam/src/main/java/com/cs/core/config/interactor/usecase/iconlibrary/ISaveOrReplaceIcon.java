package com.cs.core.config.interactor.usecase.iconlibrary;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.asset.IIconResponseModel;
import com.cs.core.config.interactor.model.iconlibrary.ISaveOrReplaceIconRequestModel;

public interface ISaveOrReplaceIcon extends ISaveConfigInteractor<ISaveOrReplaceIconRequestModel, IIconResponseModel> {
  
}
