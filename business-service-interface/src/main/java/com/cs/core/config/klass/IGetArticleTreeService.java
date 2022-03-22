package com.cs.core.config.klass;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetArticleTreeService
    extends IGetConfigService<IIdParameterModel, IConfigEntityTreeInformationModel> {
  
}
