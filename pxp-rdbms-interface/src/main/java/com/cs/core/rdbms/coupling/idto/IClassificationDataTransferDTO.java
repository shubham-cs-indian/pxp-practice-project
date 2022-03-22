package com.cs.core.rdbms.coupling.idto;

import java.util.Set;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;

public interface IClassificationDataTransferDTO extends IInitializeBGProcessDTO {
  
  public static final String BASE_ENTITY_IID           = "baseEntityIID";
  public static final String ADDED_OTHER_CLASSIFIERS   = "addedOtherClassifiers";
  public static final String REMOVED_OTHER_CLASSIFIERS = "removedOtherCLassifiers";
  public static final String ADDED_TRANSLATIONS        = "addedTranslations";
  public static final String REMOVED_TRANSLATIONS      = "removedTranslations";
  public static final String TRANSLATION_CHANGED    = "isTranslationCHanged";
  
  public void setBaseEntityIID(Long baseEntityIID);
  
  public Long getBaseEntityIID();
  
  public void setAddedOtherClassifiers(Set<IClassifierDTO> addedOtherCLassifiers);
  
  public Set<IClassifierDTO> getAddedOtherClassifiers();
  
  public void setRemovedOtherClassifiers(Set<IClassifierDTO> removedCLassifiers);
  
  public Set<IClassifierDTO> getRemovedOtherClassifiers();
  
  public void setAddedTranslations(Set<String> addedTranslations);
  
  public Set<String> getAddedTranslations();
  
  public void setTranslationChanged(Boolean translationChanged);
  
  public Boolean isTranslationChanged();
  
}
