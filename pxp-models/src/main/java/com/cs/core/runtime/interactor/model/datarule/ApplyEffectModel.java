package com.cs.core.runtime.interactor.model.datarule;

import java.util.ArrayList;
import java.util.List;

public class ApplyEffectModel implements IApplyEffectModel {
  
  private static final long serialVersionUID  = 1L;
  
  protected String          id;
  protected List<String>    typesToApply      = new ArrayList<>();
  protected List<String>    taxonomiesToApply = new ArrayList<>();
  protected String          baseType;
  
  public String getBaseType()
  {
    return baseType;
  }
  
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
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
  public List<String> getTypesToApply()
  {
    return typesToApply;
  }
  
  @Override
  public void setTypesToApply(List<String> typesToApply)
  {
    this.typesToApply = typesToApply;
  }
  
  @Override
  public List<String> getTaxonomiesToApply()
  {
    return taxonomiesToApply;
  }
  
  @Override
  public void setTaxonomiesToApply(List<String> taxonomiesToApply)
  {
    this.taxonomiesToApply = taxonomiesToApply;
  }
}
