package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetContentForGridResponseModel extends IModel {
  
  public static final String KLASS_INSTANCE = "klassInstance";
  
  public IContentInstance getKlassInstance();
  
  public void setKlassInstance(IContentInstance klassInstance);
}
