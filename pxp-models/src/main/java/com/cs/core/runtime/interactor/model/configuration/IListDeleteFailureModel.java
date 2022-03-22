package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IListDeleteFailureModel extends IModel {
  
  public static String ID                 = "id";
  public static String LINKED_ITEM_LABELS = "linkedItemLabels";
  public static String MESSAGE            = "message";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getLinkedItemLabels();
  
  public void setLinkedItemsLabel(List<String> linkedItemsLabel);
  
  public String getMessage();
  
  public void setMessage(String message);
}
