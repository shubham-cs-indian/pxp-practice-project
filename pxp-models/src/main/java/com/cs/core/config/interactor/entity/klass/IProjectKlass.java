package com.cs.core.config.interactor.entity.klass;

import java.util.Date;

public interface IProjectKlass extends IKlassStructure {
  
  public static final String GTIN_KLASS_ID = "gtinKlassId";
  
  public Date getStartDate();
  
  public void setStartDate(Date date);
  
  public Date getEndDate();
  
  public void setEndDate(Date date);
  
  public Boolean getShouldVersion();
  
  public void setShouldVersion(Boolean shouldVersion);
  
  public String getGtinKlassId();
  
  public void setGtinKlassId(String gtinKlassId);
}
