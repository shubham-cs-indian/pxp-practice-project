package com.cs.dam.config.strategy.usecase.iconlibrary;

import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.config.interactor.model.asset.IIconResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveOrReplaceIconStrategy extends IConfigStrategy<IIconModel, IIconResponseModel> {
  
}
