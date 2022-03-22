package com.cs.core.config.strategy.usecase.calculatedattribute;

import com.cs.core.config.interactor.model.calculatedattribute.IGetAllowedAttributesForCalculatedAttributeRequestModel;
import com.cs.core.config.interactor.model.configdetails.GetConfigDataAttributeResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataAttributeResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetAllowedAttributesForCalculatedAttributeStrategy extends OrientDBBaseStrategy
    implements IGetAllowedAttributesForCalculatedAttributeStrategy {
  
  public static final String useCase = "GetAllowedAttributesForCalculatedAttribute";
  
  @Override
  public IGetConfigDataAttributeResponseModel execute(
      IGetAllowedAttributesForCalculatedAttributeRequestModel model) throws Exception
  {
    return super.execute(useCase, model, GetConfigDataAttributeResponseModel.class);
  }
}
