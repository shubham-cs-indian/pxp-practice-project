package com.cs.core.rdbms.config.idto;

import java.util.List;

public interface ILanguageConfigDTO {
  
  
  public long getLanguageIID();
  
  public String getLanguageCode();
  
  public List<Long> getParentIIDs();
}
