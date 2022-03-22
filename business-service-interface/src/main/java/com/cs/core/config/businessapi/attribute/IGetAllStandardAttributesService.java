package com.cs.core.config.businessapi.attribute;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.attribute.IMandatoryAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.IGetAllMandatoryAttributeModel;

public interface IGetAllStandardAttributesService extends IGetConfigService<IMandatoryAttributeModel, IGetAllMandatoryAttributeModel> {
  
}
