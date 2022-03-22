package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IGovernanceRuleEntityRule extends IEntity {
  
  public static final String TYPE                            = "type";
  public static final String ID                              = "id";
  public static final String TO                              = "to";
  public static final String FROM                            = "from";
  public static final String VALUES                          = "values";
  public static final String ATTRIBUTE_LINK_ID               = "attributeLinkId";
  public static final String SHOULD_COMPARE_WITH_SYSTEM_DATE = "shouldCompareWithSystemDate";
  public static final String CODE                            = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getType();
  
  public void setType(String type);
  
  @Override
  public String getId();
  
  @Override
  public void setId(String id);
  
  public String getTo();
  
  public void setTo(String to);
  
  public String getFrom();
  
  public void setFrom(String from);
  
  public List<String> getValues();
  
  public void setValues(List<String> values);
  
  public String getAttributeLinkId();
  
  public void setAttributeLinkId(String attributeLinkId);
  
  public Boolean getShouldCompareWithSystemDate();
  
  public void getShouldCompareWithSystemDate(Boolean shouldCompareWithSystemDate);
}
