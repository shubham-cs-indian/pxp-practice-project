package com.cs.core.config.interactor.usecase.attribute;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.attribute.IMandatoryAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.IGetAllMandatoryAttributeModel;

public interface IGetAllStandardAttributes
    extends IGetConfigInteractor<IMandatoryAttributeModel, IGetAllMandatoryAttributeModel> {
  
}
