package com.cs.core.config.interactor.entity.relationship;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IReferencedRelationshipProperty extends IConfigEntity {
  
  public static final String ID            = "id";
  public static final String COUPLING_TYPE = "couplingType";
  public static final String PROPERTY_IID  = "propertyIID";
  public static final String TYPE          = "type";
  
  public String getId();
  
  public void setId(String id);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
  
  public long getPropertyIID();
  
  public void setPropertyIID(long propertyIID);
  
  public String getType();
  
  public void setType(String type);
}
