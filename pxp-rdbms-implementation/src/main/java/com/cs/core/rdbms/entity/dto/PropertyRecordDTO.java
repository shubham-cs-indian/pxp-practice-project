package com.cs.core.rdbms.entity.dto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.coupling.CSECoupling;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * DTO for property record
 *
 * @author vallee
 */
public abstract class PropertyRecordDTO extends RDBMSRootDTO implements IPropertyRecordDTO {
  
  private static final String         COUPLING_TYPE      = PXONTag.cpl.toPrivateTag();
  private static final String         RECORD_STATUS      = PXONTag.status.toPrivateTag();
  private static final String         MASTER_SIGNATURE   = PXONTag.src.toPrivateTag();
  private static final String         COUPLING_RULE      = PXONTag.cplrule.toPrivateTag();
  private final TreeSet<ICSECoupling> couplingRules      = new TreeSet<>();
  protected IPropertyDTO              property           = new PropertyDTO();
  protected RecordStatus              recordStatus       = RecordStatus.DIRECT;
  protected CouplingType              couplingType       = CouplingType.NONE;
  protected String                    masterNodeID       = "";
  // transient information used to identify when coupling definition changed:
  private boolean                     changedCoupling    = false; 
  // same with coupling notification:
  private boolean                     notifiedByCoupling = false;
  protected Boolean                   isVersionable      = false;                         // transient information used for timeline
  
  /**
   * Enabled default constructor
   */
  protected PropertyRecordDTO()
  {
  }
  
  /**
   * Constructor with IID initialization for inherited DTOs
   *
   * @param entityIID
   * @param property
   */
  protected PropertyRecordDTO(long entityIID, IPropertyDTO property)
  {
    super(entityIID);
    this.property = property;
  }
  
  /**
   * Value constructor
   *
   * @param entityIID
   * @param property
   * @param coupling
   * @param recordStatus
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public PropertyRecordDTO(long entityIID, IPropertyDTO property, CouplingType coupling,
      RecordStatus recordStatus) throws CSFormatException
  {
    super(entityIID);
    this.property = property;
    this.couplingType = coupling;
    this.recordStatus = recordStatus;
  }
  
  /**
   * constructor from a query result
   *
   * @param parser
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public PropertyRecordDTO(IResultSetParser parser) throws SQLException, CSFormatException
  {
    super(parser.getLong("entityiid"));
    this.property = new PropertyDTO(parser.getLong("propertyIID"), parser.getString("propertyCode"),
        IPropertyDTO.PropertyType.valueOf(parser.getInt("propertyType")));
    this.recordStatus = RecordStatus.valueOf(parser.getInt("recordStatus"));
  }
  
  /**
   * constructor from a query result with a property already initialized
   *
   * @param parser
   * @param property
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public PropertyRecordDTO(IResultSetParser parser, IPropertyDTO property)
      throws SQLException, CSFormatException
  {
    super(parser.getLong("entityiid"));
    this.property = property;
    this.recordStatus = RecordStatus.valueOf(parser.getInt("recordStatus"));
    this.couplingType = CouplingType.valueOf(parser.getInt("couplingType"));
    this.masterNodeID = parseMasterNodeID(parser);
    String couplingRule = parser.getString("coupling");
    if (couplingRule != null && !couplingRule.isEmpty())
      couplingRules.add((new CSEParser())
          .parseCoupling(couplingRule));
  }
  
  private String parseMasterNodeID(IResultSetParser parser) throws SQLException
  {
    String masterEntityIID = parser.getString("masterEntityIID");
    String masterPropertyIID = parser.getString("masterPropertyIID");
    return masterPropertyIID == null || masterEntityIID == null || masterEntityIID.isEmpty()
        || masterPropertyIID.isEmpty() ? "" : masterEntityIID + ":" + masterPropertyIID;
  }
  
  /**
   * Initialize a CSExpress ID object for subclasses
   *
   * @param type
   * @return
   */
  protected CSEObject initCSExpressID(CSEObjectType type)
  {
    CSEObject gcse = new CSEObject(type);
    gcse.setCode( property.getCode());
    if ( exportIID )
      gcse.setIID( getIID());
    gcse.setSpecification(Keyword.$type, property.getPropertyType());
    return gcse;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException {
    CSEObject gcse = (CSEObject) cse;
    try {
      property = ConfigurationDAO.instance().getPropertyByCode(gcse.getCode());
    } catch (RDBMSException ex) {
      throw new CSFormatException(String.format(
              "Property code %d from expression %s doesn't not exist", gcse.getCode(), cse.toString()));
    }
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    couplingType = parser.getEnum(CouplingType.class, COUPLING_TYPE);
    recordStatus = parser.getEnum(RecordStatus.class, RECORD_STATUS);
    masterNodeID = parser.getString(MASTER_SIGNATURE);
    String couplingRule = parser.getString(COUPLING_RULE);
    if (!couplingRule.isEmpty())
      couplingRules.add((new CSEParser()).parseCoupling(couplingRule));
    this.changedCoupling = false;
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        JSONBuilder.newJSONField(COUPLING_TYPE, couplingType),
        JSONBuilder.newJSONField(RECORD_STATUS, recordStatus),
        masterNodeID != null && !masterNodeID.isEmpty()
            ? JSONBuilder.newJSONField(MASTER_SIGNATURE, masterNodeID)
            : JSONBuilder.VOID_FIELD,
        !couplingRules.isEmpty() ? JSONBuilder.newJSONField(COUPLING_RULE, couplingRules.first()
            .toString()) : JSONBuilder.VOID_FIELD);
  }
  
  
  @Override
  public void setEntityIID(Long entityIID)
  {
    setIID(entityIID);
  }
  
  
  @Override
  public IPropertyDTO getProperty()
  {
    return property;
  }
  
  @Override
  public CouplingType getCouplingType()
  {
    return couplingType;
  }
  
  public void setCouplingType(CouplingType couplingType) {
    this.couplingType = couplingType;
  }
  
  @Override
  public List<ICSECoupling> getCouplingExpressions() throws CSFormatException
  {
    return new ArrayList<>(couplingRules);
  }
  
  public String getCouplingExpression()
  {
    return couplingRules.isEmpty() ? "" : couplingRules.first().toString();
  }
  
  @Override
  public boolean hasChangedCoupling()
  {
    return changedCoupling;
  }
  
  /**
   * @param coupling added coupling in the expression area of the property record
   */
  public void addCoupling(CSECoupling coupling, boolean creation)
  {
    boolean changedList = couplingRules.add(coupling);
    if (changedList && coupling == couplingRules.first()) {
      changedCoupling = true;
      if (creation) {
        couplingType = coupling.getCouplingType();
        recordStatus = coupling.isTransfer() ? RecordStatus.CLONED : RecordStatus.COUPLED;
      }
    }
  }
  
  @Override
  public void addRelationshipCoupling(IPropertyDTO relationship, int sourceSide,
      IPropertyDTO property, boolean dynamic) throws CSFormatException
  {
    if (sourceSide != 1 && sourceSide != 2) {
      throw new CSFormatException("Wrong source side specified - can only be 1 or 2");
    }
    CSEObject source = (CSEObject) relationship.toCSExpressID();
    source.setSpecification(Keyword.$side, String.format("%d", sourceSide));
    CSECoupling coupling = new CSECoupling(source, (CSEProperty) property.toCSExpressID());
    coupling.setDynamic(dynamic);
    addCoupling(coupling, true);
  }
  
  @Override
  public void addCLassificationCoupling(IClassifierDTO classifier, boolean dynamic) throws CSFormatException
  {
    CSEObject source = (CSEObject) classifier.toCSExpressID();
    CSECoupling coupling = new CSECoupling(source, (CSEProperty) property.toCSExpressID());
    coupling.setDynamic(dynamic);
    addCoupling(coupling, true);
  }
  
  @Override
  public void addInheritanceCoupling(IPropertyDTO property, boolean dynamic)
      throws CSFormatException
  {
    CSECoupling coupling = new CSECoupling(ICSECouplingSource.Predefined.$parent,
        (CSEProperty) property.toCSExpressID());
    coupling.setDynamic(dynamic);
    addCoupling(coupling, true);
  }
  
  @Override
  public void addDefaultValueCoupling(IClassifierDTO classifier, IPropertyDTO property,
      boolean dynamic) throws CSFormatException
  {
    CSECoupling coupling = new CSECoupling(classifier.toCSExpressID(),
        (CSEProperty) property.toCSExpressID());
    coupling.setDynamic(dynamic);
    addCoupling(coupling, true);
  }
  
  @Override
  public void addPredefinedCoupling(ICSECouplingSource.Predefined source, IPropertyDTO property,
      boolean dynamic) throws CSFormatException
  {
    CSECoupling coupling = new CSECoupling(source, (CSEProperty) property.toCSExpressID());
    coupling.setDynamic(dynamic);
    addCoupling(coupling, true);
  }
  
  /**
   * Clear all fields relating to coupling definitions
   */
  public void clearCoupling()
  {
    couplingType = CouplingType.NONE;
    couplingRules.clear();
    masterNodeID = "";
    setRecordStatus(RecordStatus.DIRECT);
  }
  
  @Override
  public void resolveCoupling()
  {
    if (!couplingRules.isEmpty())
      changedCoupling = true;
    clearCoupling();
  }
  
  /**
   * Manage the new record status in case of decoupling and according to the
   * type of coupling
   */
  public void setDecouplingStatus()
  {
    if (getCouplingBehavior() == CouplingBehavior.TIGHTLY) {
      setRecordStatus(RecordStatus.FORKED);
    }
    else if (getCouplingBehavior() == CouplingBehavior.INITIAL) {
      clearCoupling();
    }
  }
  
  @Override
  public RecordStatus getRecordStatus()
  {
    return recordStatus;
  }
  
  /**
   * @param status overwritten coupled record status
   */
  public void setRecordStatus(RecordStatus status)
  {
    recordStatus = status;
  }
  
  @Override
  public String getMasterNodeID()
  {
    return masterNodeID;
  }
  
  /**
   * @param masterNodeID overwritten master signature that drives this record by coupling
   */
  public void setMasterNodeID(String masterNodeID)
  {
    this.masterNodeID = masterNodeID;
  }
  
  @Override
  public long getMasterEntityIID()
  {
    if (masterNodeID == null || masterNodeID.isEmpty())
      return 0L;
    return Long.valueOf(masterNodeID.split(":")[0]);
  }
  
  @Override
  public long getMasterPropertyIID()
  {
    if (masterNodeID == null || masterNodeID.isEmpty())
      return 0L;
    return Long.valueOf(masterNodeID.split(":")[1]);
  }
  
  /**
   * Set the content of this property record from a source by coupling
   *
   * @param source
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public abstract void setContentFrom(IPropertyRecordDTO source) throws CSFormatException;
  
  @Override
  public boolean isNotifiedByCoupling()
  {
    return notifiedByCoupling;
  }
  
  /**
   * @param status
   *          overwritten notify status
   */
  public void setNotifiedByCoupling(boolean status)
  {
    notifiedByCoupling = status;
  }
  
  @Override
  public void isVersionable(Boolean isVersionable)
  {
    this.isVersionable = isVersionable;
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(11, 17).append(super.hashCode())
        .append(property.hashCode())
        .build();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (!super.equals(obj))
      return false;
    final PropertyRecordDTO other = (PropertyRecordDTO) obj;
    return this.property.equals(other.property);
  }
  
  @Override
  public int compareTo(Object t)
  {
    final PropertyRecordDTO other = (PropertyRecordDTO) t;
    int comparison = 0;
    if (!this.isNull() && !other.isNull()) {
      comparison = super.compareTo(t);
      if (comparison != 0) {
        return comparison;
      }
    }
    return this.property.compareTo(other.property);
  }
  
  @Override
  public boolean isEmpty()
  {
    return false;
  }
  
}
