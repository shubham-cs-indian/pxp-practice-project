package com.cs.core.config.strategy.usecase.systemstatictranslation;

import com.cs.core.config.interactor.model.entity.hidden.ISaveEntityPropertyResponseModel;
import com.cs.core.config.interactor.model.hidden.IPropertyModificationInputModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveEntityPropertyStrategy
    extends IConfigStrategy<IPropertyModificationInputModel, ISaveEntityPropertyResponseModel> {
  
}
