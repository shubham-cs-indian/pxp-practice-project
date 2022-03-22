package com.cs.core.config.interactor.usecase.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.attribute.IGetAllStandardAttributesService;
import com.cs.core.config.interactor.model.attribute.IMandatoryAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.IGetAllMandatoryAttributeModel;

@Service
public class GetAllStandardProperties
    extends AbstractGetConfigInteractor<IMandatoryAttributeModel, IGetAllMandatoryAttributeModel>
    implements IGetAllStandardAttributes {
  
  @Autowired
  IGetAllStandardAttributesService getAllStandardAttributesService;
  
  @Override
  public IGetAllMandatoryAttributeModel executeInternal(IMandatoryAttributeModel dataModel) throws Exception
  {
    return getAllStandardAttributesService.execute(dataModel);
  }
}
