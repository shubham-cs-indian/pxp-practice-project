package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IBranchWithNumberModel extends IModel {
  
  public String getId();
  
  public void setId(String id);
  
  public String getParent();
  
  public void setParent(String parent);
  
  public int getBranchNo();
  
  public void setBranchNo(int parent);
  
  public Map<String, Object> getVariantsToCopy();
  
  public void setVariantsToCopy(Map<String, Object> variantsToCopy);
}
