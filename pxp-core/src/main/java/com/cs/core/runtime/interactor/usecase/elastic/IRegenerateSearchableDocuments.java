package com.cs.core.runtime.interactor.usecase.elastic;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.elastic.IDocumentRegenerationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IRegenerateSearchableDocuments
    extends IRuntimeInteractor<IDocumentRegenerationModel, IModel> {
  
}
