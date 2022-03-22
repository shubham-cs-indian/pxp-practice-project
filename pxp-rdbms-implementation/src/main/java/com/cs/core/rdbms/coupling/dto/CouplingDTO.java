package com.cs.core.rdbms.coupling.dto;

import java.sql.SQLException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTOBuilder;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class CouplingDTO implements ICouplingDTO {
  
  private static final long serialVersionUID = 1L;
  
  public long               targetEntityIID;
  public long               sourceEntityIID;
  public long               propertyIID;
  public RecordStatus       recordStatus;
  public CouplingBehavior   couplingBehavior;
  public CouplingType       couplingType;
  public long               couplingSourceIID;
  public long               localeIID;
  
  public CouplingDTO()
  {
  }
  
  public CouplingDTO(IResultSetParser parser) throws SQLException, CSFormatException
  {
    this.targetEntityIID = parser.getLong("targetEntityIID");
    this.sourceEntityIID = parser.getLong("sourceEntityIID");
    this.propertyIID = parser.getLong("propertyIID");
    this.recordStatus = RecordStatus.valueOf(parser.getInt("recordStatus"));
    this.couplingBehavior = CouplingBehavior.valueOf(parser.getInt("couplingType"));
    this.couplingType = CouplingType.valueOf(parser.getInt("couplingSourceType"));
    this.couplingSourceIID = parser.getLong("couplingSourceIID");
    this.localeIID = parser.getLong("localeIID");
  }
  
  @Override
  public long getTargetEntityIID()
  {
    return targetEntityIID;
  }
  
  @Override
  public void setTargetEntityIID(long targetEntityIID)
  {
    this.targetEntityIID = targetEntityIID;
  }
  
  @Override
  public long getSourceEntityIID()
  {
    return sourceEntityIID;
  }
  
  @Override
  public void setSourceEntityIID(long sourceEntityIID)
  {
    this.sourceEntityIID = sourceEntityIID;
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
  public RecordStatus getRecordStatus()
  {
    return recordStatus;
  }
  
  @Override
  public void setRecordStatus(RecordStatus recordStatus)
  {
    this.recordStatus = recordStatus;
  }
  
  @Override
  public CouplingBehavior getCouplingType()
  {
    return couplingBehavior;
  }
  
  @Override
  public void setCouplingType(CouplingBehavior couplingBehavior)
  {
    this.couplingBehavior = couplingBehavior;
  }
  
  @Override
  public long getCouplingSourceIID()
  {
    return couplingSourceIID;
  }
  
  @Override
  public void setCouplingSourceIID(long couplingSourceIID)
  {
    this.couplingSourceIID = couplingSourceIID;
  }
  
  @Override
  public CouplingType getCouplingSourceType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingSourceType(CouplingType couplingType)
  {
    this.couplingType = couplingType;
  }
  
  @Override
  public long getLocaleIID()
  {
    return localeIID;
  }
  
  @Override
  public void setLocaleIID(long localeIID)
  {
    this.localeIID = localeIID;
  }
  
  @Override
  public void setExportOfIID(boolean status)
  {
    
  }
  
  @Override
  public boolean isNull()
  {
    return false;
  }
  
  @Override
  public boolean isChanged()
  {
    return false;
  }
  
  @Override
  public void setChanged(boolean status)
  {
    
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return null;
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(7, 17)
        .append(targetEntityIID)
        .append(sourceEntityIID)
        .append(propertyIID)
        .append(recordStatus)
        .append(couplingBehavior)
        .append(couplingType)
        .append(couplingSourceIID)
        .append(localeIID)
        .build();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CouplingDTO other = (CouplingDTO) obj;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    if (!equalsBuilder.append(sourceEntityIID, other.sourceEntityIID).isEquals()) {
      return false;
    }
    if (!equalsBuilder.append(targetEntityIID, other.targetEntityIID).isEquals()) {
      return false;
    }
    if (!equalsBuilder.append(propertyIID, other.propertyIID).isEquals()) {
      return false;
    }
    
    if (!equalsBuilder.append(couplingSourceIID, other.couplingSourceIID).isEquals()) {
      return false;
    }
    
    if (!equalsBuilder.append(localeIID, other.localeIID).isEquals()) {
      return false;
    }
    return true;
  }
  
  public static class CouplingDTOBuilder implements ICouplingDTOBuilder {
    
    public final CouplingDTO couplingDTO;
    
    public CouplingDTOBuilder() throws RDBMSException
    {
      couplingDTO = new CouplingDTO();
    }
    
    @Override
    public ICouplingDTO build()
    {
      return couplingDTO;
    }   
  }
  
}
