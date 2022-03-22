package com.cs.core.config.interactor.model.language;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.attributiontaxonomy.Language;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class LanguageModel implements ILanguageModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ILanguage       entity;
  
  public LanguageModel()
  {
    this.entity = new Language();
  }
  
  public LanguageModel(ILanguage entity)
  {
    this.entity = entity;
  }
  
  @Override
  @JsonIgnore
  public ILanguage getEntity()
  {
    return entity;
  }
  
  @Override
  public String getAbbreviation()
  {
    return entity.getAbbreviation();
  }
  
  @Override
  public void setAbbreviation(String abbreviation)
  {
    entity.setAbbreviation(abbreviation);
  }
  
  @Override
  public Boolean getIsDataLanguage()
  {
    return entity.getIsDataLanguage();
  }
  
  @Override
  public void setIsDataLanguage(Boolean isDataLanguage)
  {
    entity.setIsDataLanguage(isDataLanguage);
  }
  
  @Override
  public Boolean getIsUserInterfaceLanguage()
  {
    return entity.getIsUserInterfaceLanguage();
  }
  
  @Override
  public void setIsUserInterfaceLanguage(Boolean isUserInterfaceLanguage)
  {
    entity.setIsUserInterfaceLanguage(isUserInterfaceLanguage);
  }
  
  @Override
  public String getDateFormat()
  {
    return entity.getDateFormat();
  }
  
  @Override
  public void setDateFormat(String dateFormat)
  {
    entity.setDateFormat(dateFormat);
  }
  
  @Override
  public String getNumberFormat()
  {
    return entity.getNumberFormat();
  }
  
  @Override
  public void setNumberFormat(String numberFormat)
  {
    entity.setNumberFormat(numberFormat);
  }
  
  @Override
  public String getLocaleId()
  {
    return entity.getLocaleId();
  }
  
  @Override
  public void setLocaleId(String localeId)
  {
    entity.setLocaleId(localeId);
  }
  
  @Override
  public ITreeEntity getParent()
  {
    return entity.getParent();
  }
  
  @JsonDeserialize(as = Language.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    entity.setParent(parent);
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    return entity.getChildren();
  }
  
  @Override
  @JsonDeserialize(contentAs = Language.class)
  public void setChildren(List<? extends ITreeEntity> children)
  {
    entity.setChildren(children);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public Boolean getIsDefaultLanguage()
  {
    return entity.getIsDefaultLanguage();
  }
  
  @Override
  public void setIsDefaultLanguage(Boolean isDefaultLanguage)
  {
    entity.setIsDefaultLanguage(isDefaultLanguage);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return entity.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.entity.setIsStandard(isStandard);
  }
  
  @Override
  @JsonIgnore
  public String getType()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setType(String type)
  {
  }
}
