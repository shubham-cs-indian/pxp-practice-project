package com.cs.core.runtime.interactor.model.configdetails.instancetree;

public interface IConfigDetailsForCreateMCFilterAndSortInfoRequestModel extends IConfigDetailsForFilterAndSortInfoRequestModel {
  
  public static final String KLASS_ID         = "klassId";
  
  public String getKlassId();
  public void setKlassId(String targetId);
  
}
