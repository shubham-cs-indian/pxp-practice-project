package com.cs.core.runtime.interactor.model.dynamichierarchy;

import java.util.ArrayList;
import java.util.List;

public class GoldenRecordSourceInfoModel extends IdAndNameModel
    implements IGoldenRecordSourceInfoModel {
  
  private static final long serialVersionUID = 1L;
  private List<String>      languageCodes;
  private String            organizationId;
  
  @Override
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
}
