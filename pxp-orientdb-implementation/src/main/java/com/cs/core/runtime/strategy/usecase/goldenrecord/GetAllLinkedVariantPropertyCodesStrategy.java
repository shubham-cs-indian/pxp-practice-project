package com.cs.core.runtime.strategy.usecase.goldenrecord;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.GetAllLinkedVariantPropertyCodesResponseModel;
import com.cs.core.runtime.interactor.model.templating.IGetAllLinkedVariantPropertyCodesResponseModel;

@Component
public class GetAllLinkedVariantPropertyCodesStrategy extends OrientDBBaseStrategy implements IGetAllLinkedVariantPropertyCodesStrategy {
  
  @Override
  public IGetAllLinkedVariantPropertyCodesResponseModel execute(IModel model) throws Exception
  {
    return execute(GET_ALL_LINKED_VARIANT_PROPERTY_CODES, model, GetAllLinkedVariantPropertyCodesResponseModel.class);
  }
}