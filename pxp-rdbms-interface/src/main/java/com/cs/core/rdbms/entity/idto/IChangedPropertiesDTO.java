package com.cs.core.rdbms.entity.idto;

import java.util.Set;

public interface IChangedPropertiesDTO {
  
  public static final String ATTRIBUTE_CODES = "attributeCodes";
  public static final String TAG_CODES       = "tagCodes";
  
  public Set<String> getAttributeCodes();
  public void setAttributeCodes(Set<String> attributeCodes);
  
  public Set<String> getTagCodes();
  public void setTagCodes(Set<String> tagCodes);
}
