package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.model.datarule.IBranchWithNumberModel;

import java.util.Map;

public class BranchWithNumberModel implements IBranchWithNumberModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected String              id;
  protected int                 branchNo;
  protected String              parent;
  protected Map<String, Object> variantsToCopy;
  
  @Override
  public Map<String, Object> getVariantsToCopy()
  {
    return variantsToCopy;
  }
  
  @Override
  public void setVariantsToCopy(Map<String, Object> variantsToCopy)
  {
    this.variantsToCopy = variantsToCopy;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getParent()
  {
    return parent;
  }
  
  @Override
  public void setParent(String parent)
  {
    this.parent = parent;
  }
  
  @Override
  public int getBranchNo()
  {
    return branchNo;
  }
  
  @Override
  public void setBranchNo(int branchNo)
  {
    this.branchNo = branchNo;
  }
}
