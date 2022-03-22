package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.attribute.AttributeInformationModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SortDataModel implements ISortDataModel {
  
  protected List<IAttributeInformationModel> attributes;
  
  @Override
  public List<IAttributeInformationModel> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeInformationModel.class)
  public void setAttributes(List<IAttributeInformationModel> attributes)
  {
    this.attributes = attributes;
  }
}
