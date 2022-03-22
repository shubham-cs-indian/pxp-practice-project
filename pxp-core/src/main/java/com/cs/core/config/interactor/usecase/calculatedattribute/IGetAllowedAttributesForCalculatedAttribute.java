package com.cs.core.config.interactor.usecase.calculatedattribute;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.calculatedattribute.IGetAllowedAttributesForCalculatedAttributeRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataAttributeResponseModel;

public interface IGetAllowedAttributesForCalculatedAttribute extends
    IGetConfigInteractor<IGetAllowedAttributesForCalculatedAttributeRequestModel, IGetConfigDataAttributeResponseModel> {
  
}
