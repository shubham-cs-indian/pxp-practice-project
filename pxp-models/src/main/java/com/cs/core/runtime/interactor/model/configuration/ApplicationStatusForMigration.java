package com.cs.core.runtime.interactor.model.configuration;

import org.springframework.stereotype.Component;

@Component
public class ApplicationStatusForMigration {
  
  protected boolean isCleanInstallation = false;
  
  public boolean isCleanInstallation()
  {
    return isCleanInstallation;
  }
  
  public void setCleanInstallation(boolean isCleanInstallation)
  {
    this.isCleanInstallation = isCleanInstallation;
  }
}
