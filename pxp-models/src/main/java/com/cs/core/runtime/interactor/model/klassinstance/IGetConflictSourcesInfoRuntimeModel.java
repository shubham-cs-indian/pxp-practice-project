package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdAndNameModel;

import java.util.Map;

public interface IGetConflictSourcesInfoRuntimeModel extends IModel {
  
  public static final String CONTENTS = "contents";
  
  public Map<String, IIdAndNameModel> getContents();
  
  public void setContents(Map<String, IIdAndNameModel> contents);
}
