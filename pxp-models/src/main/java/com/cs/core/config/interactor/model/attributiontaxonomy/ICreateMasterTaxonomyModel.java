package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateMasterTaxonomyModel extends IModel {
  
  public static final String TAXONOMY_ID        = "taxonomyId";
  public static final String PARENT_TAXONOMY_ID = "parentTaxonomyId";
  public static final String PARENT_TAG_ID      = "parentTagId";
  public static final String TAG_VALUE_ID       = "tagValueId";
  public static final String IS_NEWLY_CREATED   = "isNewlyCreated";
  public static final String LABEL              = "label";
  public static final String TAXONOMY_TYPE      = "taxonomyType";
  public static final String CODE               = "code";
  public static final String BASE_TYPE          = "baseType";
  public static final String CLASSIFIER_IID     = "classifierIID";
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getTaxonomyId();
  
  public void setTaxonomyId(String taxonomyId);
  
  public String getParentTaxonomyId();
  
  public void setParentTaxonomyId(String parentTaxonomyId);
  
  public String getParentTagId();
  
  public void setParentTagId(String parentTagId);
  
  public String getTagValueId();
  
  public void setTagValueId(String tagValueId);
  
  public Boolean getIsNewlyCreated();
  
  public void setIsNewlyCreated(Boolean isNewlyCreated);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
  
  public Long getClassifierIID();
  
  public void setClassifierIID(Long classifierIID);
}
