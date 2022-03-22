package com.cs.core.config.business.tagtype;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTagTypeService extends IGetConfigService<IIdParameterModel, ITagTypeModel> {
  
}

