package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class Language implements ILanguage {
  
  private static final long             serialVersionUID        = 1L;
  protected String                      abbreviation;
  protected String                      localeId;
  protected String                      dateFormat;
  protected String                      numberFormat;
  protected Boolean                     isDataLanguage          = false;
  protected Boolean                     isUserInterfaceLanguage = false;
  protected Boolean                     isDefaultLanguage       = false;
  protected Boolean                     isStandard;
  protected Long                        versionId;
  protected Long                        versionTimestamp;
  protected String                      lastModifiedBy;
  protected String                      id;
  protected ITreeEntity                 parent;
  protected List<? extends ITreeEntity> children;
  protected String                      code;
  protected String                      icon;
  protected String                      iconKey;
  protected String                      label;
  
  @Override
  public String getAbbreviation()
  {
    return abbreviation;
  }
  
  @Override
  public void setAbbreviation(String abbreviation)
  {
    this.abbreviation = abbreviation;
  }
  
  @Override
  public String getDateFormat()
  {
    return dateFormat;
  }
  
  @Override
  public void setDateFormat(String dateFormat)
  {
    this.dateFormat = dateFormat;
  }
  
  @Override
  public String getNumberFormat()
  {
    return numberFormat;
  }
  
  @Override
  public void setNumberFormat(String numberFormat)
  {
    this.numberFormat = numberFormat;
  }
  
  @Override
  public Boolean getIsDataLanguage()
  {
    return isDataLanguage;
  }
  
  @Override
  public void setIsDataLanguage(Boolean isDataLanguage)
  {
    this.isDataLanguage = isDataLanguage;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    if (isStandard == null) {
      return false;
    }
    else {
      return isStandard;
    }
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public Boolean getIsDefaultLanguage()
  {
    return isDefaultLanguage;
  }
  
  @Override
  public void setIsDefaultLanguage(Boolean isDefaultLanguage)
  {
    this.isDefaultLanguage = isDefaultLanguage;
  }
  
  @Override
  public Boolean getIsUserInterfaceLanguage()
  {
    return isUserInterfaceLanguage;
  }
  
  @Override
  public void setIsUserInterfaceLanguage(Boolean isUserInterfaceLanguage)
  {
    this.isUserInterfaceLanguage = isUserInterfaceLanguage;
  }
  
  @Override
  public String getLocaleId()
  {
    return this.localeId;
  }
  
  @Override
  public void setLocaleId(String localeId)
  {
    this.localeId = localeId;
  }
  
  @Override
  public ITreeEntity getParent()
  {
    return this.parent;
  }
  
  @JsonDeserialize(as = Language.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = parent;
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return this.children;
  }
  
  @JsonDeserialize(contentAs = Language.class)
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    this.children = children;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return this.versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return this.versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return this.lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
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
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  @JsonIgnore
  public String getType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setType(String type)
  {
    // TODO Auto-generated method stub
    
  }
}
