package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;

public class InstancesOffboardingModel extends IdsListParameterModel
    implements IInstancesOffboardingModel {
  
  private static final long serialVersionUID = 1L;
  protected String          module;
  
  @Override
  public String getModule()
  {
    return this.module;
  }
  
  @Override
  public void setModule(String module)
  {
    this.module = module;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"moduleId\":" + "\"" + module + "\",");
    strBuilder.append("\"ids\":");
    
    strBuilder.append("[");
    int listSize = ids.size();
    for (int index = 0; index < listSize; index++) {
      if (index < listSize - 1) {
        strBuilder.append("\"" + ids.get(index) + "\",");
      }
      else {
        strBuilder.append("\"" + ids.get(index) + "\"");
      }
    }
    strBuilder.append("]");
    strBuilder.append("}");
    
    return strBuilder.toString();
  }
}
