package com.cs.core.runtime.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface ITaxonomyChildrenModel extends IConfigEntityInformationModel, ITreeEntity {
  
  public static final String LINKED_MASTER_TAG_ID = "linkedMasterTagId";
  
  public String getLinkedMasterTagId();
  
  public void setLinkedMasterTagId(String linkedMasterTagId);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
}
