package com.cs.core.runtime.interactor.model.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class RelationshipInstanceElementsWithContextInfoModel
    implements IRelationshipInstanceElementsWithContextInfoModel {
  
  private static final long          serialVersionUID = 1L;
  protected List<IIdAndContextModel> linkedElements   = new ArrayList<>();
  
  @Override
  public List<IIdAndContextModel> getLinkedElements()
  {
    return linkedElements;
  }
  
  @JsonDeserialize(contentAs = IdAndContextModel.class)
  @Override
  public void setLinkedElements(List<IIdAndContextModel> linkedElements)
  {
    this.linkedElements = linkedElements;
  }
}
