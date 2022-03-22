package com.cs.core.rdbms.entity.dto;

import java.util.HashSet;
import java.util.Set;

import com.cs.core.rdbms.entity.idto.IChangedPropertiesDTO;

public class ChangedPropertiesDTO implements IChangedPropertiesDTO {
  
  private Set<String> attributeCodes;
  private Set<String> tagCodes;
  
  @Override
  public Set<String> getAttributeCodes()
  {
    if(attributeCodes == null) {
      attributeCodes = new HashSet<>();
    }
    return attributeCodes;
  }
  
  @Override
  public void setAttributeCodes(Set<String> attributeCodes)
  {
    this.attributeCodes = attributeCodes;
  }
  
  @Override
  public Set<String> getTagCodes()
  {
    if(tagCodes == null) {
      tagCodes = new HashSet<>();
    }
    return tagCodes;
  }
  
  @Override
  public void setTagCodes(Set<String> tagCodes)
  {
    this.tagCodes = tagCodes;
  }
  
  
}
