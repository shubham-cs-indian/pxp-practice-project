package com.cs.core.runtime.interactor.model.dynamichierarchy;

import java.util.List;

public interface IGoldenRecordSourceInfoModel extends IIdAndNameModel {
  
  public static final String LANGUAGE_CODES  = "languageCodes";
  public static final String ORGANIZATION_ID = "organizationId";
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
}
