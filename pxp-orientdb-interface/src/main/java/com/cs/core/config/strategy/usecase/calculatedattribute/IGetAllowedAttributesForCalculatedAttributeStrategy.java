package com.cs.core.config.strategy.usecase.calculatedattribute;

import com.cs.core.config.interactor.model.calculatedattribute.IGetAllowedAttributesForCalculatedAttributeRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataAttributeResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllowedAttributesForCalculatedAttributeStrategy extends
    IConfigStrategy<IGetAllowedAttributesForCalculatedAttributeRequestModel, IGetConfigDataAttributeResponseModel> {
  
}
