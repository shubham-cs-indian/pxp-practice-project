package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.smartdocument.IMulticlassificationRequestModelForSmartDocument;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

public interface IGetConfigDetailsWithoutPermissionsForSmartDocumentStrategy extends
    IConfigStrategy<IMulticlassificationRequestModelForSmartDocument, IGetConfigDetailsForCustomTabModel> {
  
}
