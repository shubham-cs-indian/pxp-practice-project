package com.cs.core.rdbms.entity.dto;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.IUniquenessViolationDTO;
import com.cs.core.rdbms.entity.idto.IUniquenessViolationDTOBuilder;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class UniquenessViolationDTO extends SimpleDTO implements IUniquenessViolationDTO {

  private static final long serialVersionUID = 1L;
  protected long sourceIID;
  protected long targetIID;
  protected long propertyIID;
  protected long classifierIID;

  public UniquenessViolationDTO()
  {
  }
  
  public UniquenessViolationDTO(long sourceIID, long targetIID,long propertyIID, long classifierIID)
  {
    this.sourceIID = sourceIID;
    this.targetIID = targetIID;
    this.propertyIID = propertyIID;
    this.classifierIID = classifierIID;
  }
  
  @Override
  public long getSourceIID()
  {
    return sourceIID;
  }

  @Override
  public long getTargetIID()
  {
    return targetIID;
  }
  
  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }
  
  
  @Override
  public long getClassifierIID()
  {
    return classifierIID;
  }

  @Override public void setExportOfIID(boolean status)
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
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }

  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {

  }


  public static class UniquenessViolationBuilder implements IUniquenessViolationDTOBuilder{

    UniquenessViolationDTO uniquenessViolationDTO = null;
    
    public UniquenessViolationBuilder(long sourceIID, long targetIID, long propertyIID, long classifierIID)
    {
      uniquenessViolationDTO = new UniquenessViolationDTO(sourceIID, targetIID, propertyIID, classifierIID);
    }
        
    @Override
    public IUniquenessViolationDTO build()
    {
      return uniquenessViolationDTO;
    }
  }

  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    sourceIID     = parser.getLong(SOURCE_IID);
    targetIID     = parser.getLong(TARGET_IID);
    classifierIID = parser.getLong(CLASSIFIER_IID);
    propertyIID   = parser.getLong(PROPERTY_IID);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(SOURCE_IID, sourceIID), 
           JSONBuilder.newJSONField(TARGET_IID, targetIID),
           JSONBuilder.newJSONField(CLASSIFIER_IID, classifierIID), 
           JSONBuilder.newJSONField(PROPERTY_IID, propertyIID));
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
  
}

