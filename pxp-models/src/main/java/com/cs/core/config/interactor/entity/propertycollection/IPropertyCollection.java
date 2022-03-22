package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IPropertyCollection extends IConfigMasterEntity {
  
  public static final String ELEMENTS               = "elements";
  public static final String IS_STANDARD            = "isStandard";
  public static final String IS_FOR_X_RAY           = "isForXRay";
  public static final String IS_DEFAULT_FOR_X_RAY   = "isDefaultForXRay";
  public static final String ATTRIBUTE_IDS          = "attributeIds";
  public static final String TAG_IDS                = "tagIds";
  public static final String PROPERTY_SEQUENCE_IDS  = "propertySequenceIds";
  
  public List<IPropertyCollectionElement> getElements();

  public void setElements(List<IPropertyCollectionElement> elements);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public Boolean getIsForXRay();
  
  public void setIsForXRay(Boolean isForXRay);
  
  public Boolean getIsDefaultForXRay();
  
  public void setIsDefaultForXRay(Boolean isDefaultForXRay);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public List<String> getPropertySequenceIds();
  
  public void setPropertySequenceIds(List<String> propertySequenceIds);
}
