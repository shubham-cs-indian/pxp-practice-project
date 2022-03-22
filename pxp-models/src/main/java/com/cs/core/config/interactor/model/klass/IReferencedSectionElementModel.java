package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IReferencedSectionElementModel extends ISectionElement, IModel {
  
  public static final String CAN_READ            = "canRead";
  public static final String CONFLICTING_SOURCES = "conflictingSources";
  
  public Boolean getCanRead();
  
  public void setCanRead(Boolean canRead);
  
  public List<IElementConflictingValuesModel> getConflictingSources();
  
  public void setConflictingSources(List<IElementConflictingValuesModel> conflictingSources);
}
