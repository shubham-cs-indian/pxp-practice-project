package com.cs.core.config.interactor.usecase.tagtype;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTagType extends IGetConfigInteractor<IIdParameterModel, ITagTypeModel> {
  
}
