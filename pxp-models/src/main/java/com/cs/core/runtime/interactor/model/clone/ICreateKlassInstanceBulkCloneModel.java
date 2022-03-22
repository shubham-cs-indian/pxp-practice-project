package com.cs.core.runtime.interactor.model.clone;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ICreateKlassInstanceBulkCloneModel extends IModel {
  
  public static final String CONTENTS_TO_CLONE = "contentsToClone";
  
  public List<IIdAndTypeModel> getContentsToClone();
  
  public void setContentsToClone(List<IIdAndTypeModel> contentsToClone);
}
