package com.cs.core.runtime.interactor.model.contextdatatransfer;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;

import java.util.List;

public interface IContextInfoForContextualDataTransferModel extends IModel {
  
  public static final String MODIFIED_DEPENDENT_ATTRIBUTES   = "modifiedDependentAttributes";
  public static final String MODIFIED_INDEPENDENT_ATTRIBUTES = "modifiedInDependentAttributes";
  public static final String MODIFIED_TAGS                   = "modifiedTags";
  public static final String DELETED_DEPENDENT_ATTRIBUTES    = "deletedDependentAttributes";
  public static final String DELETED_INDEPENDENT_ATTRIBUTES  = "deletedInDependentAttributes";
  public static final String DELETED_TAGS                    = "deletedTags";
  public static final String KLASS_ID                        = "klassId";
  public static final String CONTEXT_KLASS_ID                = "contextKlassId";
  public static final String KLASS_TYPE                      = "klassType";
  
  public List<IIdCodeCouplingTypeModel> getModifiedDependentAttributes();
  
  public void setModifiedDependentAttributes(
      List<IIdCodeCouplingTypeModel> modifiedDependentAttributes);
  
  public List<IIdCodeCouplingTypeModel> getModifiedInDependentAttributes();
  
  public void setModifiedInDependentAttributes(
      List<IIdCodeCouplingTypeModel> modifiedInDependentAttributes);
  
  public List<IIdCodeCouplingTypeModel> getModifiedTags();
  
  public void setModifiedTags(List<IIdCodeCouplingTypeModel> modifiedTags);
  
  public List<String> getDeletedDependentAttributes();
  
  public void setDeletedDependentAttributes(List<String> deletedDependentAttributes);
  
  public List<String> getDeletedInDependentAttributes();
  
  public void setDeletedInDependentAttributes(List<String> deletedInDependentAttributes);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public String getContextKlassId();
  
  public void setContextKlassId(String contextKlassId);
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
}
