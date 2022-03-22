package com.cs.core.config.interactor.entity.exportrule;

public interface ITagValueRule {
  
  public String getInnerTagId();
  
  public void setInnerTagId(String innerTagId);
  
  public Long getTo();
  
  public void setTo(Long to);
  
  public Long getFrom();
  
  public void setFrom(Long from);
}
