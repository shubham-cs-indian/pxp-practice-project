package com.cs.core.config.interactor.model.language;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.attributiontaxonomy.Language;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class CreateLanguageModel implements ICreateLanguageModel {
  
  private static final long serialVersionUID = 1L;
  protected String          parentId;
  
  protected ILanguage       entity;
  
  public CreateLanguageModel()
  {
    this.entity = new Language();
  }
  
  public CreateLanguageModel(ILanguage entity)
  {
    this.entity = entity;
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
  @JsonIgnore
  public Boolean getIsStandard()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setIsStandard(Boolean isStandard)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public ITreeEntity getParent()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setParent(ITreeEntity parent)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public List<? extends ITreeEntity> getChildren()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setChildren(List<? extends ITreeEntity> children)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getIcon()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setIcon(String icon)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getIconKey()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setIconKey(String iconKey)
  {
    // TODO Auto-generated method stub
    
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
  
  @Override
  @JsonIgnore
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
