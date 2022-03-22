package com.cs.core.config.strategy.usecase.tagtype;

import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTagTypeStrategy extends IConfigStrategy<IIdParameterModel, ITagTypeModel> {
  
}
