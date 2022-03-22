package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagAndTagValuesIds;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAddedTagModel extends IModel, ITagAndTagValuesIds {
  
  public static final String IS_NEWLY_CREATED = "isNewlyCreated";
  public static final String LABEL            = "label";
  public static final String CODE             = "code";
  public static final String PROPERTY_IID     = "propertyIID";
  
  public Boolean getIsNewlyCreated();
  
  public void setIsNewlyCreated(Boolean isNewlyCreated);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
  
  public long getPropertyIID();
  
  public void setPropertyIID(long propertyIID);
}
