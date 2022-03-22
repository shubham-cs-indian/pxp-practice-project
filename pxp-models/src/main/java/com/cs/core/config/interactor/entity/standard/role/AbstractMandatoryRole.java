package com.cs.core.config.interactor.entity.standard.role;

import com.cs.core.config.interactor.entity.datarule.IMandatoryRole;
import com.cs.core.config.interactor.entity.role.AbstractRole;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMandatoryRole extends AbstractRole implements IMandatoryRole {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    klassType;
  
  public AbstractMandatoryRole()
  {
  }
  
  public AbstractMandatoryRole(String label)
  {
    this.label = label;
  }
  
  public List<String> getKlassType()
  {
    if (klassType == null) {
      klassType = new ArrayList<String>();
    }
    return klassType;
  }
  
  @Override
  public void setKlassType(List<String> klassType)
  {
    this.klassType = klassType;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
}
