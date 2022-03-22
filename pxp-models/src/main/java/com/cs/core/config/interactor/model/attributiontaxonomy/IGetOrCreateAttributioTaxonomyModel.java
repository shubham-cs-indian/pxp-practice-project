package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagAndTagValuesIds;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetOrCreateAttributioTaxonomyModel extends IModel {
  
  public static final String TAXONOMY_ID   = "taxonomyId";
  public static final String LABEL         = "label";
  public static final String TAXONOMY_TYPE = "taxonomyType";
  public static final String CODE          = "code";
  public static final String LEVEL         = "level";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getTaxonomyId();
  
  public void setTaxonomyId(String taxonomyId);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
  
  public ITagAndTagValuesIds getLevel();
  
  public void setLevel(ITagAndTagValuesIds level);
}
