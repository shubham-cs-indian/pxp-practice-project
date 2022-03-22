package com.cs.core.rdbms.entity.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTOBuilder;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.SQLException;

/**
 * DTO representation of an entity relation
 *
 * @author vallee
 */
public class EntityRelationDTO extends SimpleDTO implements IEntityRelationDTO {
  
  private static final long serialVersionUID     = 1L;
  private static final String SIDE_BASEENTITY    = PXONTag.entity.toReadOnlyCSETag();
  private static final String CONTEXTUAL_OBJECT  = PXONTag.cxtual.toJSONContentTag();
  
  private boolean             isChanged          = false;
  private long                sideBaseEntityIID  = 0L;
  private String              sideBaseEntityID   = "";
  private ContextualDataDTO   sideContextualData = new ContextualDataDTO();
  private ContextualDataDTO   otherSideContextualData  = new ContextualDataDTO();

  
  /**
   * Enabled default constructor -> use in case of transfer and clone.
   */
  public EntityRelationDTO(IEntityRelationDTO relation) {
    this.sideBaseEntityIID = relation.getOtherSideEntityIID();
    this.sideContextualData = new ContextualDataDTO(relation.getContextualObject());
    this.sideBaseEntityID = relation.getOtherSideEntityID();
    this.otherSideContextualData = new ContextualDataDTO(relation.getOtherSideContextualObject());
  }

  /**
   * Copy constructor
   */
  public EntityRelationDTO() {
  }
  /**
   * sideBaseEntityIID constructor
   *
   * @param sideBaseEntityIID
   */
  public EntityRelationDTO(long sideBaseEntityIID) {
    this.sideBaseEntityIID = sideBaseEntityIID;
  }
  
  public EntityRelationDTO(String sideEntityID, Long sideEntityIID) {
    this.sideBaseEntityID = sideEntityID;
    this.sideBaseEntityIID = sideEntityIID;
  }

  /**
   * Value and all sides context constructor
   *
   * @param sideBaseEntityIID
   * @param cxtCode
   */
  public EntityRelationDTO(long sideBaseEntityIID, String cxtCode) {
    this.sideBaseEntityIID = sideBaseEntityIID;
    if (!cxtCode.isEmpty()) {
      this.sideContextualData.setContextCode(cxtCode);
    }
  }
  
  /**
   * Value and all sides context constructor
   * @param sideBaseEntityIID
   * @param sideBaseEntityID
   * @param cxtCode
   */
  public EntityRelationDTO(long sideBaseEntityIID, String sideBaseEntityID, String cxtCode) {
    this.sideBaseEntityID = sideBaseEntityID;
    this.sideBaseEntityIID = sideBaseEntityIID;
    if (!cxtCode.isEmpty()) {
      this.sideContextualData.setContextCode(cxtCode);
    }
  }

  /**
   * @return other side  contextual object on the other side of this relation
   */
  public IContextualDataDTO getOtherSideContextualObject() {
    return otherSideContextualData;
  }

  /**
   * @return other side  contextual object on the other side of this relation
   */
  public void setOtherSideContextualObject(ContextualDataDTO ctxData) {
     otherSideContextualData = ctxData;
  }
  /**
   * Constructor through a query result
   *
   * @param parser
   * @throws SQLException in case of JDBC error
   * @throws CSFormatException in case of format error
   */
  public EntityRelationDTO(IResultSetParser parser) throws SQLException, CSFormatException {
    sideBaseEntityIID = parser.getLong("sideEntityIID");
  }

  /**
   * @return change status
   */
  public boolean isChanged() {
    return isChanged;
  }

  /**
   * @param status overwritten change status
   */
  public void setChanged(boolean status) {
    isChanged = status;
  }

  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException {
    sideBaseEntityIID = ((CSEObject) (parser.getCSEElement(SIDE_BASEENTITY))).getIID();
    sideBaseEntityID = ((CSEObject) (parser.getCSEElement(SIDE_BASEENTITY))).getCode();
    if (!parser.getString(CONTEXTUAL_OBJECT)
        .isEmpty()) {
      CSEObject cxtual = (CSEObject)parser.getCSEElement(CONTEXTUAL_OBJECT);
      sideContextualData.fromCSExpressID(cxtual);
    }
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    CSEObject sideEntity = new CSEObject(CSEObjectType.Entity);
    sideEntity.setIID(sideBaseEntityIID);
    sideEntity.setCode(sideBaseEntityID);
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(SIDE_BASEENTITY, sideEntity),
            !sideContextualData.isNull()
            ? JSONBuilder.newJSONField(CONTEXTUAL_OBJECT, sideContextualData.toCSExpressID())
            : JSONBuilder.VOID_FIELD);
  }

  @Override
  public String getOtherSideEntityID() {
    return sideBaseEntityID;
  }

  @Override
  public long getOtherSideEntityIID() {
    return sideBaseEntityIID;
  }

  @Override
  public void setOtherSideEntityIID(long baseEntityIID) {
    sideBaseEntityIID = baseEntityIID;
  }

  @Override
  public void setOtherSideEntityID(String baseEntityID) {
    this.sideBaseEntityID = baseEntityID;
  }

  /**
   * @return a base entityDTO with iid only
   */
  public BaseEntityDTO getOtherSideEntity() {
    BaseEntityDTO sideBaseEntity = new BaseEntityDTO();
    sideBaseEntity.setIID(sideBaseEntityIID);
    return sideBaseEntity;
  }

  @Override
  public IContextualDataDTO getContextualObject() {
    return sideContextualData;
  }

  /**
   * @param cxtObject overwritten contextual object
   */
  public void setContextualObject(ContextualDataDTO cxtObject) {
    sideContextualData = cxtObject;
  }


  @Override
  public void setContextCode(String cxtCode) {
    this.sideContextualData.setChanged(true);
    this.sideContextualData.setContextCode(cxtCode);
  }

  @Override
  public void setOtherSideContextCode(String cxtCode) {
    this.otherSideContextualData.setChanged(true);
    this.otherSideContextualData.setContextCode(cxtCode);
  }

  @Override
  public int compareTo(Object t) {
    EntityRelationDTO that = (EntityRelationDTO) t;
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    int compare = compareToBuilder.append(sideBaseEntityIID, that.sideBaseEntityIID).toComparison();
    if (compare != 0) {
      return compare;
    }
    if (sideBaseEntityIID == 0L) {
      if (!sideBaseEntityID.isEmpty()) {
        return compareToBuilder.append(sideBaseEntityID, that.sideBaseEntityID).toComparison();
      }
    }
    return compare;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(5, 19).append(sideBaseEntityIID)
            .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final EntityRelationDTO other = (EntityRelationDTO) obj;
    return this.sideBaseEntityIID == other.sideBaseEntityIID;
  }
  
  @Override
  public long getSideBaseEntityIID()
  {
    return sideBaseEntityIID;
  }

  @Override
  public String getSideBaseEntityID()
  {
    return sideBaseEntityID;
  }
  /**
   * implementation of IEntityRelationDTOBuilder
   *
   * @author Janak.Gurme
   *
   */
  public static class EntityRelationDTOBuilder implements IEntityRelationDTOBuilder {
    
    private final EntityRelationDTO entityRelationDTO;
    
    public EntityRelationDTOBuilder()
    {
      entityRelationDTO = new EntityRelationDTO();
    }
    
    @Override
    public IEntityRelationDTOBuilder otherSideEntityIID(long otherSideBaseEntityIID)
    {
      entityRelationDTO.setOtherSideEntityIID(otherSideBaseEntityIID);
      return this;
    }

    @Override
    public IEntityRelationDTOBuilder OtherSideEntityID(String OtherSideEntityID)
    {
      entityRelationDTO.sideBaseEntityID = OtherSideEntityID;
      return this;
    }
    
    @Override
    public IEntityRelationDTOBuilder contextCode(String contextCode) {
      entityRelationDTO.setContextCode(contextCode);
      return this;
    }

    @Override
    public IEntityRelationDTO build() {
      return entityRelationDTO;
    }

  }
}
