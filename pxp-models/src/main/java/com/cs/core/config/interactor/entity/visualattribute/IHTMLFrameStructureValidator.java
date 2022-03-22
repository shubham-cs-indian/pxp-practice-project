package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;

import java.util.List;

public interface IHTMLFrameStructureValidator extends IStructureValidator {
  
  public List<String> getAllowedRTEIcons();
  
  public void setAllowedRTEIcons(List<String> allowedRTEIcons);
  
  public Long getAllowedMaxCharacters();
  
  public void setAllowedMaxCharacters(Long allowedMaxCharacters);
}
