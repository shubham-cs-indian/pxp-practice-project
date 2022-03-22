package com.cs.core.runtime.interactor.usecase.runtimemapping;

import com.cs.core.config.interactor.model.mapping.IColumnTagValueAutoMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetTagValueFromColumnModel;
import com.cs.core.runtime.interactor.usecase.cammunda.IComponent;

public interface IGetTagValuesFromColumn
    extends IComponent<IGetTagValueFromColumnModel, IColumnTagValueAutoMappingModel> {
}
