package com.cs.core.runtime.interactor.model.logger;

import org.springframework.stereotype.Component;

@Component
public class InteractorThreadData {
  
  ThreadLocal<InteractorData> interactorData = new ThreadLocal<>();
  
  public InteractorData getInteractorData()
  {
    InteractorData data = interactorData.get();
    if (data == null) {
      data = new InteractorData();
      interactorData.set(data);
    }
    return data;
  }
  
  public void setInteractorData(InteractorData controllerData)
  {
    this.interactorData.set(controllerData);
  }
  
  public void removeInteractorData()
  {
    this.interactorData.remove();
  }
}
