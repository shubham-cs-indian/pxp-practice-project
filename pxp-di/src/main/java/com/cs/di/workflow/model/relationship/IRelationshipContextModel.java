package com.cs.di.workflow.model.relationship;


import com.cs.core.runtime.interactor.model.component.klassinstance.IDiContextModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IRelationshipContextModel extends IModel {
  
  public static String SIDE_2_ID = "side2Id";
  public static String CONTEXT   = "context";

  public String getSide2Id();
  public void setSide2Id(String side2Id);
  
  public IDiContextModel getContext();
  public void setContext(IDiContextModel context);
}