package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

public class KlassInstanceTypeInfoModel implements IKlassInstanceTypeInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected String          parentId;
  protected List<String>    klassIds;
  protected List<String>    taxonomyIds;
  protected List<String>    parentKlassIds;
  protected List<String>    parentTaxonomyIds;
  protected List<String>    parentLanguageCodes;
  protected List<String>    languageCodes;
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public List<String> getParentKlassIds()
  {
    if (parentKlassIds == null) {
      parentKlassIds = new ArrayList<>();
    }
    return parentKlassIds;
  }
  
  @Override
  public void setParentKlassIds(List<String> parentKlassIds)
  {
    this.parentKlassIds = parentKlassIds;
  }
  
  @Override
  public List<String> getParentTaxonomyIds()
  {
    if (parentTaxonomyIds == null) {
      parentTaxonomyIds = new ArrayList<>();
    }
    return parentTaxonomyIds;
  }
  
  @Override
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds)
  {
    this.parentTaxonomyIds = parentTaxonomyIds;
  }
  
  @Override
  public List<String> getParentLanguageCodes()
  {
    if (parentLanguageCodes == null) {
      parentLanguageCodes = new ArrayList<>();
    }
    return parentLanguageCodes;
  }
  
  @Override
  public void setParentLanguageCodes(List<String> parentLanguageCodes)
  {
    this.parentLanguageCodes = parentLanguageCodes;
  }
  
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
}
