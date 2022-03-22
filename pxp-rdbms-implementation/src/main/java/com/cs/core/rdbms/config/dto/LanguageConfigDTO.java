package com.cs.core.rdbms.config.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;

public class LanguageConfigDTO implements ILanguageConfigDTO {
  
  private long       languageIID  = 0L;
  private String     languageCode = "";
  private List<Long> parentIIDs   = new ArrayList<>();
  
  public LanguageConfigDTO()
  {
    
  }
  
  public LanguageConfigDTO(long languageIID, String languageCode, List<Long> parentIIDs)
  {
    super();
    this.languageIID = languageIID;
    this.languageCode = languageCode;
    this.parentIIDs = parentIIDs;
  }
  
  @Override
  public long getLanguageIID()
  {
    return languageIID;
  }
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }
  
  @Override
  public List<Long> getParentIIDs()
  {
    return parentIIDs;
  }
  
}
