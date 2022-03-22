package com.cs.core.runtime.interactor.model.fileinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.fileinstance.OnboardingFileInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IOnboardingFileInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetAllFilesForUserModel implements IGetAllFilesForUserModel {
  
  private static final long                         serialVersionUID = 1L;
  protected List<? extends IOnboardingFileInstance> instances        = new ArrayList<>();
  protected IKlass                                  typeKlass;
  
  @Override
  public List<? extends IOnboardingFileInstance> getInstances()
  {
    
    return instances;
  }
  
  @JsonDeserialize(contentAs = OnboardingFileInstance.class)
  @Override
  public void setInstances(List<? extends IOnboardingFileInstance> instances)
  {
    this.instances = instances;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    
    return typeKlass;
  }
  
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
