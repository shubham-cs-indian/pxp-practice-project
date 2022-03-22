package com.cs.core.runtime.interactor.model.fileinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.klassinstance.IOnboardingFileInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllFilesForUserModel extends IModel {
  
  public static final String INSTANCES  = "instances";
  public static final String TYPE_KLASS = "typeKlass";
  
  public List<? extends IOnboardingFileInstance> getInstances();
  
  public void setInstances(List<? extends IOnboardingFileInstance> fileInstances);
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass fileKlass);
}
