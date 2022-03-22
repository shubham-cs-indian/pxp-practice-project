package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IRelevanceCountInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IApplicableFilterModel extends IModel {
  
  public static final String ID             = "id";
  public static final String LABEL          = "label";
  public static final String TYPE           = "type";
  public static final String CHILDREN       = "children";
  public static final String RELEVANCE_INFO = "relevanceInfo";
  public static final String COUNT          = "count";
  public static final String DEFAULT_UNIT   = "defaultUnit";
  public static final String PRECISION      = "precision";
  public static final String TO             = "to";
  public static final String FROM           = "from";
  public static final String CODE           = "code";
  
  public String getType();
  
  public void setType(String type);
  
  public String getId();
  
  public void setId(String id);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IApplicableFilterModel> getChildren();
  
  public void setChildren(List<IApplicableFilterModel> children);
  
  List<IRelevanceCountInformationModel> getRelevanceInfo();
  
  void setRelevanceInfo(List<IRelevanceCountInformationModel> relevanceInfo);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer precision);
  
  public Long getTo();
  
  public void setTo(Long to);
  
  public Long getFrom();
  
  public void setFrom(Long from);
}
