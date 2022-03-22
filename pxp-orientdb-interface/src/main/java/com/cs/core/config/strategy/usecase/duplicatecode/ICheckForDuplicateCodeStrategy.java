package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeModel;
import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeReturnModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICheckForDuplicateCodeStrategy
    extends IConfigStrategy<ICheckForDuplicateCodeModel, ICheckForDuplicateCodeReturnModel> {
  
}
