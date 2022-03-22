package com.cs.core.runtime.interactor.model.languageinstance;

import java.util.ArrayList;
import java.util.List;

public class BulkPropogationForDeletedTranslationsRequestModel
    implements IBulkPropogationForDeletedTranslationsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          baseType;
  protected String          contentId;
  protected List<String>    languageCodes;
  protected List<String>    variantIds;
  protected List<String>    criids;
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
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
  
  @Override
  public List<String> getVariantIds()
  {
    if (variantIds == null) {
      variantIds = new ArrayList<>();
    }
    return variantIds;
  }
  
  @Override
  public void setVariantIds(List<String> variantIds)
  {
    this.variantIds = variantIds;
  }
  
  @Override
  public List<String> getCriids()
  {
    return criids;
  }
  
  @Override
  public void setCriids(List<String> criids)
  {
    this.criids = criids;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
