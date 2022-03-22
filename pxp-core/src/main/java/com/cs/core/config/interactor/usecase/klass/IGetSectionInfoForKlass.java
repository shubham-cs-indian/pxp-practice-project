package com.cs.core.config.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;

public interface IGetSectionInfoForKlass
    extends IGetConfigInteractor<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel> {
  
}
