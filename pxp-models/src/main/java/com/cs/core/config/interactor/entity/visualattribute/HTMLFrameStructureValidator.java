package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.AbstractStructureValidator;

import java.util.ArrayList;
import java.util.List;

public class HTMLFrameStructureValidator extends AbstractStructureValidator
    implements IHTMLFrameStructureValidator {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    allowedRTEIcons  = new ArrayList<>();
  
  protected Long            allowedMaxCharacters;
  
  @Override
  public List<String> getAllowedRTEIcons()
  {
    return this.allowedRTEIcons;
  }
  
  @Override
  public void setAllowedRTEIcons(List<String> allowedRTEIcons)
  {
    this.allowedRTEIcons = allowedRTEIcons;
  }
  
  @Override
  public Long getAllowedMaxCharacters()
  {
    return this.allowedMaxCharacters;
  }
  
  @Override
  public void setAllowedMaxCharacters(Long allowedMaxCharacters)
  {
    this.allowedMaxCharacters = allowedMaxCharacters;
  }
}
