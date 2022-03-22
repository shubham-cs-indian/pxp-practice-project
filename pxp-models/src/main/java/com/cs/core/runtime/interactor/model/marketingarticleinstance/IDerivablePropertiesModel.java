package com.cs.core.runtime.interactor.model.marketingarticleinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDerivablePropertiesModel extends IModel {
  
  public static final String DERIVABLE_ATTRIBUTE_IDS = "derivableAttributeIds";
  public static final String DERIVABLE_TAG_IDS       = "derivableTagIds";
  
  public List<String> getDerivableAttributeIds();
  
  public void setDerivableAttributeIds(List<String> derivableAttributeIds);
  
  public List<String> getDerivableTagIds();
  
  public void setDerivableTagIds(List<String> derivableTagIds);
}
