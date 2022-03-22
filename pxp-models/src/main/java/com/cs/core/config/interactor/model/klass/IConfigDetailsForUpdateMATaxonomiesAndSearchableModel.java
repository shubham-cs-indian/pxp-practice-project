package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConfigDetailsForUpdateMATaxonomiesAndSearchableModel extends IModel {
  
  public static final String READ_ONLY_ATTRIBUTE_IDS        = "readOnlyAttributeIds";
  public static final String READ_ONLY_TAG_IDS              = "readOnlyTagIds";
  public static final String TAXONOMY_IDS_TO_TRANSFER       = "taxonomyIdsToTransfer";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN = "numberOfVersionToMaintain";
  
  public List<String> getReadOnlyAttributeIds();
  
  public void setReadOnlyAttributeIds(List<String> readOnlyAttributeIds);
  
  public List<String> getReadOnlyTagIds();
  
  public void setReadOnlyTagIds(List<String> readOnlyTagIds);
  
  public List<String> getTaxonomyIdsToTransfer();
  
  public void setTaxonomyIdsToTransfer(List<String> taxonomyIdsToTransfer);
  
  public Integer getNumberOfVersionToMaintain();
  
  public void setNumberOfVersionToMaintain(Integer numberOfVersionToMaintain);
}
