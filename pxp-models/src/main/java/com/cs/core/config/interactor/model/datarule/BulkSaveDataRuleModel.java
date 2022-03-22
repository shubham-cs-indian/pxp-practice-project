package com.cs.core.config.interactor.model.datarule;

import java.util.ArrayList;
import java.util.List;

public class BulkSaveDataRuleModel implements IBulkSaveDataRuleModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          label;
  protected String          code;
  protected String          type;
  protected Boolean         isStandard;
  protected Boolean         isLanguageDependent;
  protected List<String>    languages;
  protected List<String>    physicalCatalogIds;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    if (isStandard == null) {
      isStandard = false;
    }
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public Boolean getIsLanguageDependent()
  {
    if (isLanguageDependent == null) {
      isLanguageDependent = false;
    }
    return isLanguageDependent;
  }
  
  @Override
  public void setIsLanguageDependent(Boolean isLanguageDependent)
  {
    this.isLanguageDependent = isLanguageDependent;
  }
  
  @Override
  public List<String> getLanguages()
  {
    return languages;
  }
  
  @Override
  public void setLanguages(List<String> languages)
  {
    this.languages = languages;
  }

  @Override
  public List<String> getPhysicalCatalogIds()
  {
    if (physicalCatalogIds == null) {
      return new ArrayList<>();
    }
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
}
