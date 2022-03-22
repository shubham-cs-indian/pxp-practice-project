package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlassBasic;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IReferencedKlassDetailStrategyModel extends IKlassBasic, IModel {
  
  public static final String PROPERTY_COLLECTIONS           = "propertyCollections";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN = "numberOfVersionsToMaintain";
  public static final String NATURE_TYPE                    = "natureType";
  public static final String IS_NATURE                      = "isNature";
  public static final String PREVIEW_IMAGE                  = "previewImage";
  public static final String CLASSIFIER_IID                 = "classifierIID";
  public static final String TRACK_DOWNLOADS                = "trackDownloads";
  
  public List<String> getPropertyCollections();
  
  public void setPropertyCollections(List<String> propertyCollections);
  
  public Long getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Long numberOfVersionsToMaintain);
  
  public String getNatureType();
  
  public void setNatureType(String natureType);
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);
  
  public String getPreviewImage();
  
  public void setPreviewImage(String previewImage);
  
  public long getClassifierIID();
  
  public void setClassifierIID(long classifierIID);
  
  public Boolean getTrackDownloads();
  
  public void setTrackDownloads(Boolean trackDownloads);
}
