package com.cs.core.config.interactor.entity.klass;

import java.util.Date;

public interface IEditorial extends IKlassStructure {
  
  public Date getStartDate();
  
  public void setStartDate(Date date);
  
  public Date getEndDate();
  
  public void setEndDate(Date date);
  
  public Boolean getShouldVersion();
  
  public void setShouldVersion(Boolean shouldVersion);
}
