package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.datarule.IFindReplaceNormalizatiom;

public class FindReplaceNormalization extends Normalization implements IFindReplaceNormalizatiom {
  
  private static final long serialVersionUID = 1L;
  protected String          findText;
  protected String          replaceText;
  
  @Override
  public String getFindText()
  {
    return findText;
  }
  
  @Override
  public void setFindText(String findText)
  {
    this.findText = findText;
  }
  
  @Override
  public String getReplaceText()
  {
    return replaceText;
  }
  
  @Override
  public void setReplaceText(String replaceText)
  {
    this.replaceText = replaceText;
  }
}
