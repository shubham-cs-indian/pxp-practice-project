package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IRelationshipInheritanceModifiedRelationshipDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class RelationshipInheritanceModifiedRelationshipDTO extends SimpleDTO
implements IRelationshipInheritanceModifiedRelationshipDTO {

  private static final long serialVersionUID = 1L;
  
  public static final String        ID                     = "id";
  public static final String        SIDE                   = "side";
  public static final String        COUPLING_TYPE          = "couplingType";
  
  private String                    id;
  private String                    side;
  private String                    couplingType;

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
  public String getSide()
  {
    return side;
  }

  @Override
  public void setSide(String side)
  {
    this.side = side;
  }

  @Override
  public String getCouplingType()
  {
    return couplingType;
  }

  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
      return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(ID, id),
    JSONBuilder.newJSONField(SIDE, side),
    JSONBuilder.newJSONField(COUPLING_TYPE, couplingType));
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    id = json.getString(ID);
    side = json.getString(SIDE);
    couplingType = json.getString(COUPLING_TYPE);
  }
  
}
