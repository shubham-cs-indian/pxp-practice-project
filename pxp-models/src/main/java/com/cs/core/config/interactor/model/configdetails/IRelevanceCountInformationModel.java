package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRelevanceCountInformationModel extends IModel {
  
  public static final String COUNT     = "count";
  public static final String RELEVANCE = "relevance";
  public static final String FROM      = "from";
  public static final String TO        = "to";
  
  long getCount();
  
  void setCount(long count);
  
  int getRelevance();
  
  void setRelevance(int relevance);
  
  int getFrom();
  
  void setFrom(int from);
  
  int getTo();
  
  void setTo(int to);
}
