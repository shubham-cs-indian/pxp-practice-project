package com.cs.di.workflow.model.relationship;

import com.cs.core.runtime.interactor.model.component.klassinstance.DiContextModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.IDiContextModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RelationshipContextModel implements IRelationshipContextModel {

  private static final long serialVersionUID = 1L;
  private IDiContextModel   context;
  private String            side2Id;
  
  @Override
  public IDiContextModel getContext()
  {
    return context;
  }
  
  @Override
  @JsonDeserialize(as = DiContextModel.class)
  public void setContext(IDiContextModel context)
  {
    this.context = context;
  }
  
  @Override
  public String getSide2Id()
  {
    return side2Id;
  }
  
  @Override
  public void setSide2Id(String side2Id)
  {
    this.side2Id = side2Id;
  }

}
