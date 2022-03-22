package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskContentTypeResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskReferencesModel;

public interface IGetKlassInfoForLinkedContentStrategy
    extends IConfigStrategy<IConfigTaskReferencesModel, IConfigTaskContentTypeResponseModel> {
  
}
