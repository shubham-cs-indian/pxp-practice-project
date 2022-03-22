package com.cs.core.runtime.interactor.model.templating;

import java.util.ArrayList;
import java.util.List;

public class GetAllLinkedVariantPropertyCodesResponseModel implements IGetAllLinkedVariantPropertyCodesResponseModel {
  
  protected List<String> linkedVariantCodes;
  
  @Override
  public List<String> getLinkedVariantCodes()
  {
    if (linkedVariantCodes == null) {
      linkedVariantCodes = new ArrayList<String>();
    }
    return linkedVariantCodes;
  }
  
  @Override
  public void setLinkedVariantCodes(List<String> linkedVariantCodes)
  {
    this.linkedVariantCodes = linkedVariantCodes;
  }
  
}
