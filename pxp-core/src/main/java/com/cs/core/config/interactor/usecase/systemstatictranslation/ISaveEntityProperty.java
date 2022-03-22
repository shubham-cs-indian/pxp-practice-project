package com.cs.core.config.interactor.usecase.systemstatictranslation;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.entity.hidden.ISaveEntityPropertyResponseModel;
import com.cs.core.config.interactor.model.hidden.IPropertyModificationInputModel;

public interface ISaveEntityProperty
    extends ISaveConfigInteractor<IPropertyModificationInputModel, ISaveEntityPropertyResponseModel> {
  
}
