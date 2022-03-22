package com.cs.core.rdbms.relationship.dto;

import java.sql.SQLException;

import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTOBuilder;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class RelationCoupleRecordDTO extends RDBMSRootDTO implements IRelationCoupleRecordDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private long         targetEntityId               = 0L;
  private long         sourceEntityId               = 0L;
  private long         propagableRelationshipId     = 0L;
  private long         natureRelationshipId         = 0L;
  private String       propagableRelationshipSideId = "";
  private CouplingType couplingType                 = CouplingType.undefined;
  private boolean      isResolved                   = false;
  
  public RelationCoupleRecordDTO(IResultSetParser parser) throws SQLException
  {
    this.targetEntityId = parser.getLong("targetentityid");
    this.sourceEntityId = parser.getLong("sourceentityid");
    this.propagableRelationshipId = parser.getLong("propagablerelationshipid");
    this.natureRelationshipId = parser.getLong("naturerelationshipid");
    this.propagableRelationshipSideId = parser.getString("propagablerelationshipsideid");
    this.couplingType = couplingType.valueOf(parser.getInt("couplingtype"));
    this.isResolved = parser.getBoolean("isresolved");
  }
  
  public RelationCoupleRecordDTO()
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
  
  @Override
  public long getTargetEntityId()
  {
    return targetEntityId;
  }
  
  @Override
  public long getSourceEntityId()
  {
    return sourceEntityId;
  }
  
  @Override
  public long getNatureRelationshipId()
  {
    return natureRelationshipId;
  }
  
  @Override
  public long getPropagableeRelationshipId()
  {
    return propagableRelationshipId;
  }
  
  @Override
  public String getPropagableeRelationshipSideId()
  {
    return propagableRelationshipSideId;
  }
  
  @Override
  public boolean getIsResolved()
  {
    return isResolved;
  }
  
  @Override
  public CouplingType getCouplingType()
  {
    return couplingType;
  }
  
  public static class RelationCoupleRecordDTOBuilder implements IRelationCoupleRecordDTOBuilder {
    
    private final RelationCoupleRecordDTO relationCoupleRecordDto;
    
    public RelationCoupleRecordDTOBuilder()
    {
      relationCoupleRecordDto = new RelationCoupleRecordDTO();
    }
    
    @Override
    public IRelationCoupleRecordDTO build()
    {
      return relationCoupleRecordDto;
    }
    
    @Override
    public IRelationCoupleRecordDTOBuilder targetEntityId(Long targetEntityId)
    {
      relationCoupleRecordDto.targetEntityId = targetEntityId;
      return this;
    }
    
    @Override
    public IRelationCoupleRecordDTOBuilder sourceEntityId(Long sourceEntityId)
    {
      relationCoupleRecordDto.sourceEntityId = sourceEntityId;
      return this;
    }
    
    @Override
    public IRelationCoupleRecordDTOBuilder natureRelationshipId(Long natureRelationshipId)
    {
      relationCoupleRecordDto.natureRelationshipId = natureRelationshipId;
      return this;
    }
    
    @Override
    public IRelationCoupleRecordDTOBuilder propagableRelationshipId(Long propagableRelationshipId)
    {
      relationCoupleRecordDto.propagableRelationshipId = propagableRelationshipId;
      return this;
    }
    
    @Override
    public IRelationCoupleRecordDTOBuilder propagableRelationshipSideId(String propagableRelationshipSideId)
    {
      relationCoupleRecordDto.propagableRelationshipSideId = propagableRelationshipSideId;
      return this;
    }
    
    @Override
    public IRelationCoupleRecordDTOBuilder isResolved(Boolean isResolved)
    {
      relationCoupleRecordDto.isResolved = isResolved;
      return this;
    }
    
    @Override
    public IRelationCoupleRecordDTOBuilder couplingType(CouplingType couplingType)
    {
      relationCoupleRecordDto.couplingType = couplingType;
      return this;
    }
  }
  
}
