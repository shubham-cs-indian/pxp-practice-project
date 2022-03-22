package com.cs.core.rdbms.relationship.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;

public interface IRelationCoupleRecordDTO extends IRootDTO{
  
  public static final String TARGET_ENTITY_ID                = "targetentityid";
  public static final String SOURCE_ENTITY_ID                = "sourceentityid";
  public static final String COUPLING_TYPE                   = "couplingtype";
  public static final String NATURE_RELATIONSHIP_ID          = "naturerelationshipid";
  public static final String PROPAGABLE_RELATIONSHIP_ID      = "propagablerelationshipid";
  public static final String PROPAGABLE_RELATIONSHIP_SIDE_ID = "propagablerelationshipsideid";
  public static final String IS_RESOLVED                     = "isresolved";
  
  /**
   * @return targetEntity id
   */
  public long getTargetEntityId();
  
  /**
   * @return sourceEntity id
   */
  public long getSourceEntityId();
  
  /**
   * @return type of Coupling
   */
  public CouplingType getCouplingType();
  
  /**
   * @return natureRelationship id
   */
  public long getNatureRelationshipId();
  
  /**
   * @return propogableRelationship id
   */
  public long getPropagableeRelationshipId();
  
  /**
   * @return propogableRelationshipSide id
   */
  public String getPropagableeRelationshipSideId();
  
  /**
   * @return  is Resolved 
   */
  public boolean getIsResolved();
  
  enum CouplingType
  {

    tightlyCoupled, dynamicCoupled, undefined; 

    public static final CouplingType[] values = values();

    public static CouplingType valueOf(int ordinal)
    {
      return values[ordinal];
    }
  }
  
}
