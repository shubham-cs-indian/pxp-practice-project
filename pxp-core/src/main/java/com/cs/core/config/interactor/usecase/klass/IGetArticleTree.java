package com.cs.core.config.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetArticleTree
    extends IGetConfigInteractor<IIdParameterModel, IConfigEntityTreeInformationModel> {
  
}
