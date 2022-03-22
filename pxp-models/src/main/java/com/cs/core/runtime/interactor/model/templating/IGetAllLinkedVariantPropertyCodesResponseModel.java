package com.cs.core.runtime.interactor.model.templating;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllLinkedVariantPropertyCodesResponseModel extends IModel {
  
  public static final String LINKED_VARIANT_CODES = "linkedVariantCodes";
  
  public List<String> getLinkedVariantCodes();
  
  public void setLinkedVariantCodes(List<String> linkedVariantCodes);
}