package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedContextKlassModel extends IModel {
  
  public static final String CONTEXT_KLASS_ID    = "contextKlassId";
  public static final String ADDED_ATTRIBUTES    = "addedAttributes";
  public static final String DELETED_ATTRIBUTES  = "deletedAttributes";
  public static final String MODIFIED_ATTRIBUTES = "modifiedAttributes";
  public static final String ADDED_TAGS          = "addedTags";
  public static final String DELETED_TAGS        = "deletedTags";
  public static final String MODIFIED_TAGS       = "modifiedTags";
  public static final String CONTEXT_ID          = "contextId";
  
  public String getContextKlassId();
  
  public void setContextKlassId(String contextKlassId);
  
  public List<IIdAndCouplingTypeModel> getAddedAttributes();
  
  public void setAddedAttributes(List<IIdAndCouplingTypeModel> addedAttributes);
  
  public List<String> getDeletedAttributes();
  
  public void setDeletedAttributes(List<String> deletedAttributes);
  
  public List<IIdAndCouplingTypeModel> getModifiedAttributes();
  
  public void setModifiedAttributes(List<IIdAndCouplingTypeModel> modifiedAttributes);
  
  public List<IIdAndCouplingTypeModel> getAddedTags();
  
  public void setAddedTags(List<IIdAndCouplingTypeModel> addedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<IIdAndCouplingTypeModel> getModifiedTags();
  
  public void setModifiedTags(List<IIdAndCouplingTypeModel> modifiedTags);
  
  public String getContextId();
  
  public void setContextId(String contextId);
}
