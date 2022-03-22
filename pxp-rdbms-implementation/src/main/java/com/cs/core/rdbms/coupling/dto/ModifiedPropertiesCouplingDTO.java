package com.cs.core.rdbms.coupling.dto;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.IModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class ModifiedPropertiesCouplingDTO extends SimpleDTO implements IModifiedPropertiesCouplingDTO {

  private static final long serialVersionUID = 1L;
 
  public long propertyIID;
  public int  couplingBehavior;
  public int  couplingType;
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONField(PROPERTY_IID, propertyIID),
        JSONBuilder.newJSONField(COUPLINGTYPE, couplingBehavior),
        JSONBuilder.newJSONField(COUPLINGSOURCETYPE, couplingType));
  
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    propertyIID = json.getLong(PROPERTY_IID);
    couplingBehavior = json.getInt(COUPLINGTYPE);
    couplingType = json.getInt(COUPLINGSOURCETYPE);
  }
  
  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }
  
  @Override
  public void setPropertyIID(long propertyIID)
  {
    this.propertyIID = propertyIID;
  }
  
  @Override
  public int getCouplingType()
  {
    return couplingBehavior;
  }
  
  @Override
  public void setCouplingType(int couplingBehavior)
  {
    this.couplingBehavior = couplingBehavior;
  }
  
  @Override
  public int getCouplingSourceType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingSourceType(int couplingType)
  {
    this.couplingType = couplingType;
  }
  
  
}
