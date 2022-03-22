package com.cs.core.config.interactor.model.smartdocument.preset;

import java.util.List;

import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPreset;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ISmartDocumentPresetModel extends ISmartDocumentPreset, IConfigModel {
  
  public static final String KLASS_CLASSIFIER_IDS    = "klassClassifierIds";
  public static final String TAXONOMY_CLASSIFIER_IDS = "taxonomyClassifierIds";
  
  public List<Long> getKlassClassifierIds();
  public void setKlassClassifierIds(List<Long> klassClassifierIds);
  
  public List<Long> getTaxonomyClassifierIds();
  public void setTaxonomyClassifierIds(List<Long> taxonomyClassifierIds);
  
}
