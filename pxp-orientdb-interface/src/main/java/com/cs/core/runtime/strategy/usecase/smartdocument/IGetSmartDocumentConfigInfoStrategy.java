package com.cs.core.runtime.strategy.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentInfoModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSmartDocumentConfigInfoStrategy
    extends IConfigStrategy<IIdParameterModel, IGetSmartDocumentInfoModel> {
  
}
