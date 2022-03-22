package com.cs.core.config.interactor.usecase.duplicatecode;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeModel;
import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeReturnModel;

public interface ICheckForDuplicateCode
    extends IGetConfigInteractor<ICheckForDuplicateCodeModel, ICheckForDuplicateCodeReturnModel> {
  
}
