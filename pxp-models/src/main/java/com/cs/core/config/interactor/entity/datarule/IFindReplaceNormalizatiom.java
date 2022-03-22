package com.cs.core.config.interactor.entity.datarule;

public interface IFindReplaceNormalizatiom extends INormalization {
  
  public static final String FIND_TEXT    = "findText";
  public static final String REPLACE_TEXT = "replaceText";
  
  public String getFindText();
  
  public void setFindText(String findText);
  
  public String getReplaceText();
  
  public void setReplaceText(String replaceText);
}
