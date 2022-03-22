package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class ReferencedKlassDetailsModel implements IReferencedKlassDetailStrategyModel {
  
  private static final long serialVersionUID           = 1L;
  
  protected String          id;
  protected List<String>    propertyCollections        = new ArrayList<>();
  protected String          label;
  protected String          type;
  protected String          icon;
  protected String          iconKey;
  protected Long            numberOfVersionsToMaintain = 1l;
  protected String          natureType;
  protected Boolean         isNature;
  protected String          previewImage;
  protected String          code;
  protected String          unitTagId;
  protected long            classifierIID;
  protected Boolean         trackDownloads             = false;
  
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
  public List<String> getPropertyCollections()
  {
    if (propertyCollections == null) {
      propertyCollections = new ArrayList<>();
    }
    return propertyCollections;
  }
  
  @Override
  public void setPropertyCollections(List<String> sections)
  {
    this.propertyCollections = sections;
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
  public Long getNumberOfVersionsToMaintain()
  {
    return numberOfVersionsToMaintain;
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Long numberOfVersionsToMaintain)
  {
    this.numberOfVersionsToMaintain = numberOfVersionsToMaintain;
  }
  
  /**
   * ******************************************* ignored Property
   * ****************************************************
   */
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  @JsonIgnore
  public ITreeEntity getParent()
  {
    return null;
  }
  
  @Override
  public void setParent(ITreeEntity parent)
  {
  }
  
  @Override
  @JsonIgnore
  public List<? extends ITreeEntity> getChildren()
  {
    return null;
  }
  
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
  }
  
  @Override
  public String getNatureType()
  {
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    this.natureType = natureType;
  }
  
  @Override
  public Boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }
  
  @Override
  public String getPreviewImage()
  {
    return previewImage;
  }
  
  @Override
  public void setPreviewImage(String previewImage)
  {
    this.previewImage = previewImage;
  }
  
  @Override
  public long getClassifierIID()
  {
    return classifierIID;
  }
  
  @Override
  public void setClassifierIID(long classifierIID)
  {
    this.classifierIID = classifierIID;
  }
  
  @Override
  public Boolean getTrackDownloads()
  {
    return trackDownloads;
  }

  @Override
  public void setTrackDownloads(Boolean trackDownloads)
  {
    this.trackDownloads = trackDownloads;
  }
}
