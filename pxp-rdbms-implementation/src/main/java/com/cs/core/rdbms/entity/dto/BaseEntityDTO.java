package com.cs.core.rdbms.entity.dto;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.data.LocaleID;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.SimpleTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.*;

/**
 * @author farooq.kadri
 */
public class BaseEntityDTO extends BaseEntityIDDTO implements IBaseEntityDTO {
  
  private static final long             serialVersionUID     = 1L;
  private static final String           ENTITY_NAME          = PXONTag.name.toPrivateTag();
  private static final String           SOURCE_CATALOG       = PXONTag.src.toReadOnlyTag();
  private static final String           ORIGIN_ENTITY        = PXONTag.origin.toReadOnlyCSETag();
  private static final String           ENTITY_EXTENSION     = PXONTag.ext.toJSONContentTag();
  private static final String           CREATED              = PXONTag.created.toJSONContentTag();
  private static final String           LAST_MODIFIED        = PXONTag.modified.toJSONContentTag();
  private static final String           PROPERTY_RECORDS     = PXONTag.record.toJSONArrayTag();
  private static final String           EMBEDDED_TYPE        = PXONTag.embdtype.toReadOnlyTag();
  private static final String           PARENT               = PXONTag.parent.toReadOnlyCSETag();
  private static final String           TOP_PARENT           = PXONTag.top.toReadOnlyCSETag();
  private static final String           CHILDREN             = PXONTag.embd.toCSEListTag();
  private static final String           IS_EXPIRED           = PXONTag.isexpired.toJSONContentTag();
  private static final String           IS_DUPLICATE         = PXONTag.isDuplicate.toJSONContentTag();
  private static final String           ENDPOINT_CODE        = PXONTag.endpoint.toReadOnlyTag();
  private static final String           LOCALE_IDS           = PXONTag.localeIds.toJSONArrayTag();
  private static final String           IS_MERGED            = PXONTag.isMerged.toJSONContentTag();
  private final Set<IPropertyRecordDTO> propertyRecords      = new TreeSet<>();
  private final Set<IBaseEntityChildrenDTO> children            = new HashSet<>();
  private String                        entityName           = "";
  private String                        sourceCatalogCode    = "";
  private long                          originBaseEntityIID  = 0L;
  private JSONContent                   entityExtension      = new JSONContent();
  private SimpleTrackingDTO             created              = new SimpleTrackingDTO();
  private SimpleTrackingDTO             lastModified         = new SimpleTrackingDTO();
  private EmbeddedType                  embeddedType         = EmbeddedType.UNDEFINED;
  private long                          parentIID            = 0L;
  private String                        parentID             = "";
  private long                          topParentIID         = 0L;
  private String                        topParentID          = "";
  private EmbeddedType                  childrenEmbeddedType = EmbeddedType.UNDEFINED;
  private boolean                       isExpired            = false;
  private boolean                       isDuplicate          = false;
  private List<String>                  localeIds            = new ArrayList<>();
  private boolean                       isClone              = false;
  private String                        endpointCode         = "";
  public static final String            KNULL                = "$null";
  private boolean                       isMerged             = false;

  /**
   * Enabled default constructor
   */
  public BaseEntityDTO()
  {
  }
  
  /**
   * Ancestor constructor
   *
   * @param entityIdentity
   */
  public BaseEntityDTO(BaseEntityIDDTO entityIdentity)
  {
    super(entityIdentity);
  }
  
  /**
   * Value constructor
   *
   * @param baseEntityID
   * @param baseType
   * @param baseLocaleID
   * @param catalog
   * @param natureClass
   */
  public BaseEntityDTO(String baseEntityID, BaseType baseType, String baseLocaleID,
      ICatalogDTO catalog, ClassifierDTO natureClass)
  {
    super(baseEntityID, baseType, baseLocaleID, catalog, natureClass);
  }
  
  /**
   * Ancestor constructor with name
   *
   * @param entityIdentity
   * @param entityName
   */
  public BaseEntityDTO(BaseEntityIDDTO entityIdentity, String entityName)
  {
    super(entityIdentity);
    this.entityName = entityName;
  }
  
  /**
   * Set parent information
   *
   * @param parentIID
   * @param embeddedType
   */
  public void setParent(long parentIID, EmbeddedType embeddedType)
  {
    this.parentIID = parentIID;
    this.embeddedType = embeddedType;
  }
  
  /**
   * Set top parent information
   *
   * @param parentIID
   */
  public void setTopParent(long parentIID)
  {
    this.topParentIID = parentIID;
  }
  
  /**
   * Set parent information from the result of request
   *
   * @param parser
   * @throws java.sql.SQLException
   */
  public void setParent(IResultSetParser parser) throws SQLException
  {
    parentIID = parser.getLong("parentBaseEntityIID");
    embeddedType = EmbeddedType.valueOf(parser.getInt("relationType"));
  }
  
  @Override
  public void mapFromBaseEntityTrackingWithName(IResultSetParser parser)
      throws SQLException, RDBMSException, CSFormatException
  {
    super.mapFromBaseEntityTrackingWithName(parser);
    mapFromBaseEntity(parser);
    this.entityName = parser.getString("baseEntityName");
  }
  
  @Override
  public void mapFromBaseEntityTrackingWithoutName(IResultSetParser parser) throws SQLException, RDBMSException, CSFormatException
  {
    super.mapFromBaseEntityTrackingWithoutName(parser);
    mapFromBaseEntity(parser);
  }

  private void mapFromBaseEntity(IResultSetParser parser) throws SQLException, CSFormatException
  {
    created = new SimpleTrackingDTO(parser.getString("createusername"),
        parser.getLong("createtime"));
    lastModified = new SimpleTrackingDTO(parser.getString("modifyusername"),
        parser.getLong("lastModifiedTime"));
    this.sourceCatalogCode = parser.getString("sourcecatalogcode");
    this.originBaseEntityIID = parser.getLong("originbaseentityIID");
    this.parentIID = parser.getLong("parentIID");
    this.topParentIID = parser.getLong("topparentIID");
    //In PXON without setting ID we won't get IID info
    if(parentIID != 0) {
      this.parentID = KNULL;
      this.topParentID = KNULL;
    }
    this.embeddedType = EmbeddedType.valueOf(parser.getInt("embeddedType"));
    this.entityExtension.fromString(parser.getStringFromJSON("entityextension"));
    this.isExpired = parser.getBoolean("isExpired");
    this.isDuplicate = parser.getBoolean("isDuplicate");
    this.isClone = parser.getBoolean("isClone");
    this.endpointCode = parser.getString("endpointCode");
    this.isMerged = parser.getBoolean("isMerged");
  }
  
  @Override
  public void mapFromBaseEntityWithContextualData(IResultSetParser parser)
      throws SQLException, RDBMSException, CSFormatException
  {
    super.mapFromBaseEntityWithContextualData(parser);
    this.topParentIID = parser.getLong("topparentIID");
    this.parentIID = parser.getLong("parentIID");
    this.sourceCatalogCode = parser.getString("sourcecatalogCode");
    this.baseLocaleID = new LocaleID(parser.getString("baselocaleid"));
    this.embeddedType = EmbeddedType.valueOf(parser.getInt("embeddedType"));
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    entityName = parser.getString(ENTITY_NAME);
    sourceCatalogCode = parser.getString(SOURCE_CATALOG);
    isExpired = parser.getBoolean(IS_EXPIRED);
    isDuplicate = parser.getBoolean(IS_DUPLICATE);
    endpointCode = parser.getString(ENDPOINT_CODE);
    String originCse = parser.getString(ORIGIN_ENTITY);
    if (!originCse.isEmpty())
      originBaseEntityIID = ((CSEObject) parser.getCSEElement(ORIGIN_ENTITY)).getIID();
    else
      originBaseEntityIID = 0L;
    entityExtension = parser.getJSONContent(ENTITY_EXTENSION);
    created.fromJSON(parser.getJSONParser(CREATED));
    lastModified.fromJSON(parser.getJSONParser(LAST_MODIFIED));
    // load property records
    propertyRecords.clear();
    
    for (Object recordJSON : parser.getJSONArray(PROPERTY_RECORDS)) {
      JSONContentParser recordParser = new JSONContentParser((JSONObject) recordJSON);
      // Determine if the record is a Tag or a value and construct the record
      // accordingly:
      PropertyRecordDTO record = newPropertyRecordByType(recordParser);
      propertyRecords.add(record);
    }
    String parentCse = parser.getString(PARENT);
    if (!parentCse.isEmpty()) {
      CSEObject parent = (CSEObject) parser.getCSEElement(PARENT);
      parentIID = parent.getIID();
      parentID = parent.getCode();
    }
    else {
      parentIID = 0L;
    }
    embeddedType = parser.getEnum(EmbeddedType.class, EMBEDDED_TYPE);
    String topParentCse = parser.getString(TOP_PARENT);
    if (!topParentCse.isEmpty()) {
      CSEObject topParent = (CSEObject) parser.getCSEElement(TOP_PARENT);
      topParentIID = topParent.getIID();
      topParentID = topParent.getCode();
    }
    else {
      topParentIID = 0L;
    }
    // load children IIDs by relationship types
    String childrenCseStr = parser.getString(CHILDREN);
    if (!childrenCseStr.isEmpty()) {
      CSEList childrenCse = (CSEList) parser.getCSEElement(CHILDREN);
      for (ICSEElement childCse : childrenCse.getSubElements()) {
        childrenEmbeddedType = childCse.getSpecification(EmbeddedType.class, Keyword.$type);
        long childIID = ((CSEObject) childCse).getIID();
        String childID = ((CSEObject) childCse).getCode();
        IBaseEntityChildrenDTO childrenDTO = new BaseEntityChildrenDTO(childID, childIID);
        children.add(childrenDTO);
      }
    }
    
    localeIds.clear();
    parser.getJSONArray(LOCALE_IDS).forEach(id -> localeIds.add((String) id));
    localeIds.add(baseLocaleID.toString());
    isMerged = parser.getBoolean(IS_MERGED);
  }
  
  private PropertyRecordDTO newPropertyRecordByType(JSONContentParser recordParser)
      throws CSFormatException
  {
    CSEObject recordCse = (CSEObject) recordParser.getCSEElement(PXONTag.csid.toTag());
    PropertyRecordDTO record;
    switch (recordCse.getObjectType()) {
      case ValueRecord:
        record = new ValueRecordDTO();
        break;
      case TagsRecord:
        record = new TagsRecordDTO();
        break;
      case RelationSideRecord:
        record = new RelationsSetDTO();
        break;
      default:
        throw new CSFormatException("Program ERROR: undefined record type from PXON " + recordCse);
    }
    record.fromPXON(recordParser);
    record.setIID(getIID());
    return record;
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    // prepare the children map first:
    CSEList childrenCse = new CSEList();
    
    for(IBaseEntityChildrenDTO child : children) {
      CSEObject childCse = new CSEObject(CSEObjectType.Entity);
      childCse.setCode(child.getChildrenID());
      childCse.setIID(child.getChildrenIID());
      childCse.setSpecification(Keyword.$type, childrenEmbeddedType);
      childrenCse.addElement(childCse);
    }
    CSEObject origin = new CSEObject(ICSEElement.CSEObjectType.Entity);
    origin.setIID(originBaseEntityIID);
    CSEObject parent = new CSEObject(ICSEElement.CSEObjectType.Entity);
    parent.setIID(parentIID);
    parent.setCode(parentID);
    CSEObject topParent = new CSEObject(ICSEElement.CSEObjectType.Entity);
    topParent.setIID(topParentIID);
    topParent.setCode(topParentID);
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        !entityName.isEmpty() ? JSONBuilder.newJSONField(ENTITY_NAME, entityName, true)
            : JSONBuilder.VOID_FIELD,
        !sourceCatalogCode.isEmpty() ? JSONBuilder.newJSONField(SOURCE_CATALOG, sourceCatalogCode)
            : JSONBuilder.VOID_FIELD,
        originBaseEntityIID > 0 ? JSONBuilder.newJSONField(ORIGIN_ENTITY, origin)
            : JSONBuilder.VOID_FIELD,
        !entityExtension.isEmpty()
            ? JSONBuilder.newJSONField(ENTITY_EXTENSION, entityExtension.toStringBuffer())
            : JSONBuilder.VOID_FIELD,
        !created.isNull() ? JSONBuilder.newJSONField(CREATED, created.toJSONBuffer())
            : JSONBuilder.VOID_FIELD,
        !lastModified.isNull()
            ? JSONBuilder.newJSONField(LAST_MODIFIED, lastModified.toJSONBuffer())
            : JSONBuilder.VOID_FIELD,
        propertyRecords.size() > 0 ? JSONBuilder.newJSONArray(PROPERTY_RECORDS, propertyRecords)
            : JSONBuilder.VOID_FIELD,
        !topParentID.isEmpty() ? JSONBuilder.newJSONField(TOP_PARENT, topParent) : JSONBuilder.VOID_FIELD,
        embeddedType != EmbeddedType.UNDEFINED
            ? JSONBuilder.newJSONField(EMBEDDED_TYPE, embeddedType)
            : JSONBuilder.VOID_FIELD,
        !parentID.isEmpty() ? JSONBuilder.newJSONField(PARENT, parent) : JSONBuilder.VOID_FIELD,
        children.size() > 0 ? JSONBuilder.newJSONField(CHILDREN, childrenCse)
            : JSONBuilder.VOID_FIELD,
        JSONBuilder.newJSONField(IS_EXPIRED, isExpired),
        JSONBuilder.newJSONField(IS_DUPLICATE, isDuplicate),
        JSONBuilder.newJSONField(IS_MERGED, isMerged),
        localeIds.size() > 0 ? JSONBuilder.newJSONStringArray(LOCALE_IDS, localeIds) : JSONBuilder.VOID_FIELD, 
        !endpointCode.isEmpty() ? JSONBuilder.newJSONField(ENDPOINT_CODE, endpointCode)
            : JSONBuilder.VOID_FIELD);
  }
  
  @Override
  public ISimpleTrackingDTO getCreatedTrack()
  {
    return created;
  }
  
  /**
   * @param created
   *          overwritten created tracking data
   */
  public void setCreatedTrack(ISimpleTrackingDTO created)
  {
    this.created.setWhen(created.getWhen());
    this.created.setWho(created.getWho());
  }
  
  @Override
  public ISimpleTrackingDTO getLastModifiedTrack()
  {
    return lastModified;
  }
  
  /**
   * @param modified
   *          overwritten modified tracking data
   */
  public void setLastModifiedTrack(ISimpleTrackingDTO modified)
  {
    this.lastModified.setWhen(modified.getWhen());
    this.lastModified.setWho(modified.getWho());
  }
  
  /**
   * return tracking information as property record
   *
   * @param property
   *          is the tracking property
   * @return the corresponding value record
   * @throws CSFormatException
   */
  public IValueRecordDTO getTrackingValueRecord(IPropertyDTO property) throws CSFormatException
  {
    ValueRecordDTO trackingRecord = new ValueRecordDTO(getIID(), -1L, property, CouplingType.NONE,
        RecordStatus.DIRECT, "", "", 0, "", "", null);
    if (property.getPropertyIID() == IStandardConfig.StandardProperty.createdbyattribute.getIID()) {
      trackingRecord.setValue(created.getWho());
    }
    else if (property.getPropertyIID() == IStandardConfig.StandardProperty.createdonattribute
        .getIID()) {
      trackingRecord.setValue(String.format("%d", created.getWhen()));
      trackingRecord.setAsNumber(created.getWhen());
    }
    else if (property.getPropertyIID() == IStandardConfig.StandardProperty.lastmodifiedbyattribute
        .getIID()) {
      trackingRecord.setValue(lastModified.getWho());
    }
    else if (property.getPropertyIID() == IStandardConfig.StandardProperty.lastmodifiedattribute
        .getIID()) {
      trackingRecord.setValue(String.format("%d", lastModified.getWhen()));
      trackingRecord.setAsNumber(lastModified.getWhen());
    }
    else {
      throw new CSFormatException("Requesting for non-tracking attribute: " + property.toPXON());
    }
    return trackingRecord;
  }
  
  /**
   * Update entity tracking data from value record
   *
   * @param record
   *          of tracking attribute
   */
  public void setTrackingData(IValueRecordDTO record)
  {
    if (record.getProperty()
        .getPropertyIID() == IStandardConfig.StandardProperty.createdbyattribute.getIID()) {
      created.setWho(record.getValue());
    }
    else if (record.getProperty()
        .getPropertyIID() == IStandardConfig.StandardProperty.createdonattribute.getIID()) {
      created.setWhen(Math.round(record.getAsNumber()));
    }
    else if (record.getProperty()
        .getPropertyIID() == IStandardConfig.StandardProperty.lastmodifiedbyattribute.getIID()) {
      lastModified.setWho(record.getValue());
    }
    else if (record.getProperty()
        .getPropertyIID() == IStandardConfig.StandardProperty.lastmodifiedattribute.getIID()) {
      lastModified.setWhen(Math.round(record.getAsNumber()));
    }
  }
  
  public void mapFromBaseEntityForImport(IResultSetParser parser) throws SQLException, RDBMSException, CSFormatException
  {
    super.mapFromBaseEntityTrackingWithoutName(parser);
    prepareMapFromBaseEntity(parser);
  }

  public void mapBasicInfo(IResultSetParser parser, LocaleCatalogDAO localeCatalogDAO) throws SQLException, RDBMSException, CSFormatException
  {
    setIID(parser.getLong("baseEntityIID"));
    this.baseEntityID = parser.getString("baseentityID");
    this.baseType = BaseType.valueOf(parser.getInt("basetype"));
    this.setLocaleCatalog((LocaleCatalogDTO) localeCatalogDAO.getLocaleCatalogDTO());
  }

  private void prepareMapFromBaseEntity(IResultSetParser parser) throws SQLException, CSFormatException
  {
    /*created = new SimpleTrackingDTO(parser.getString("createusername"),
        parser.getLong("createtime"));
    lastModified = new SimpleTrackingDTO(parser.getString("modifyusername"),
        parser.getLong("lastModifiedTime"));*/
    this.sourceCatalogCode = parser.getString("sourcecatalogcode");
    this.originBaseEntityIID = parser.getLong("originbaseentityIID");
    this.parentIID = parser.getLong("parentIID");
    this.topParentIID = parser.getLong("topparentIID");
    //In PXON without setting ID we won't get IID info
    if(parentIID != 0) {
      this.parentID = KNULL;
      this.topParentID = KNULL;
    }
    this.embeddedType = EmbeddedType.valueOf(parser.getInt("embeddedType"));
    this.entityExtension.fromString(parser.getStringFromJSON("entityextension"));
    this.isExpired = parser.getBoolean("isExpired");
    this.isDuplicate = parser.getBoolean("isDuplicate");
    this.isClone = parser.getBoolean("isClone");
    this.endpointCode = parser.getString("endpointCode");
    this.isMerged = parser.getBoolean("isMerged");
  }
  
  @Override
  public String getBaseEntityName()
  {
    return entityName;
  }
  
  @Override
  public String getSourceCatalogCode()
  {
    return sourceCatalogCode;
  }
  
  /**
   * @param catalogCode
   *          overwritten source catalog
   */
  public void setSourceCatalogCode(String catalogCode)
  {
    sourceCatalogCode = catalogCode;
  }
  
  @Override
  public long getOriginBaseEntityIID()
  {
    return originBaseEntityIID;
  }
  
  /**
   * @param iid
   *          overwritten source BaseEntity IID
   */
  public void setOriginBaseEntityIID(long iid)
  {
    originBaseEntityIID = iid;
  }
  
  @Override
  public IJSONContent getEntityExtension()
  {
    return entityExtension;
  }
  
  @Override
  public void setEntityExtension(String jsonContent) throws CSFormatException
  {
    entityExtension.fromString(jsonContent);
    setChanged(true);
  }
  
  @Override
  public Set<IPropertyRecordDTO> getPropertyRecords()
  {
    return propertyRecords;
  }
  
  @Override
  public void setPropertyRecords(IPropertyRecordDTO... records)
  {
    this.propertyRecords.clear();
    this.propertyRecords.addAll(Arrays.asList(records));
  }
  
  /**
   * add a record to this entity and ensure its entity IID is aligned with this
   * entity
   *
   * @param record
   */
  public void addPropertyRecord(IPropertyRecordDTO record)
  {
    boolean added = propertyRecords.add(record);
    if (added && getIID() > 0)
      ((PropertyRecordDTO) record).setIID(getIID());
  }
  
  @Override
  public IPropertyRecordDTO getPropertyRecord(long propertyIID)
  {
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      if (propertyRecord.getProperty()
          .getIID() == propertyIID) {
        return propertyRecord;
      }
    }
    return null;
  }
  
  @Override
  public long getTopParentIID()
  {
    return topParentIID;
  }
  
  public String getTopParentID()
  {
    return topParentID;
  }
  
  @Override
  public long getParentIID()
  {
    return parentIID;
  }
  
  public String getParentID()
  {
    return parentID;
  }
  
  @Override
  public EmbeddedType getEmbeddedType()
  {
    return embeddedType;
  }
  @Override
  
  public void setParentIID(EmbeddedType type, long baseEntityIID)
  {
    parentIID = baseEntityIID;
    embeddedType = type;
  }
  
  @Override
  public Set<Long> getChildrenIIDs()
  {
    Set<Long> childrenIIDs = new HashSet<Long>();
    children.forEach(child -> childrenIIDs.add(child.getChildrenIID()));
    return childrenIIDs;
  }
  
  @Override
  public void setChildren(EmbeddedType type, IBaseEntityIDDTO... childrenDTOs)
  {
    childrenEmbeddedType = type;
    children.clear();
    for(IBaseEntityIDDTO child : childrenDTOs) {
      IBaseEntityChildrenDTO childDTO = new BaseEntityChildrenDTO();
      childDTO.setChildrenID(child.getBaseEntityID());
      childDTO.setChildrenIID(child.getBaseEntityIID());
      children.add(childDTO);
    }
  }

  @Override
  public void setChildrenFromEntity(EmbeddedType type, Collection<IBaseEntityDTO> childrenDTOs)
  {
    childrenEmbeddedType = type;
    children.clear();
    for(IBaseEntityIDDTO child : childrenDTOs) {
      IBaseEntityChildrenDTO childDTO = new BaseEntityChildrenDTO();
      childDTO.setChildrenID(child.getBaseEntityID());
      childDTO.setChildrenIID(child.getBaseEntityIID());
      children.add(childDTO);
    }
  }
  
  @Override
  public Set<IBaseEntityChildrenDTO> getChildren()
  {
    return children; 
  }

  @Override
  public boolean isExpired()
  {
    return isExpired;
  }

  @Override
  public void setExpired(boolean isExpired)
  {
    this.isExpired = isExpired;
  }

  @Override
  public boolean isDuplicate()
  {
    return isDuplicate;
  }

  @Override
  public void setDuplicate(boolean isDuplicate)
  {
    this.isDuplicate = isDuplicate;
  }

  @Override
  public List<String> getLocaleIds()
  {
    return this.localeIds;
  }
  
  @Override
  public void setLocaleIds(List<String> localeIds)
  {
    this.localeIds = localeIds; 
  }
  
  @Override
  public boolean isClone()
  {
    return isClone;
  }

  @Override
  public String getEndpointCode()
  {
    return endpointCode;
  }
  
  @Override
  public boolean isMerged()
  {
    return isMerged;
  }

  @Override
  public void setMerged(boolean isMerged)
  {
    this.isMerged = isMerged;
  }
  @Override
  public void setSourceOrganizationCode(String sourceOrganizationCode)
  {
    this.sourceOrganizationCode = sourceOrganizationCode;
  }
  
  /**
   * implementation of IBaseEntityDTOBuilder
   * @author Janak.Gurme
   *
   */
  public static class BaseEntityDTOBuilder implements IBaseEntityDTOBuilder {
    
    private final BaseEntityDTO baseEntityDTO;
    
    /**
     * mandatory  minimal fields to prepare baseEntityDTO
     * @param baseEntityID
     * @param baseType
     * @param baseLocaleID
     * @param catalog
     * @param natureClass
     */
    public BaseEntityDTOBuilder(String baseEntityID, BaseType baseType, String baseLocaleID,
        ICatalogDTO catalog, ClassifierDTO natureClass)
    {
      baseEntityDTO = new BaseEntityDTO(baseEntityID, baseType, baseLocaleID, catalog,
          natureClass);
    }
    
    @Override
    public IBaseEntityDTOBuilder baseEntityIID(long baseEntityIID)
    {
      baseEntityDTO.setIID(baseEntityIID);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder contextDTO(IContextDTO contextDTO)
    {
      baseEntityDTO.contextualObject = new ContextualDataDTO(contextDTO.getCode());
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder defaultImageIID(long imageIID)
    {
      baseEntityDTO.setDefaultImageIID(imageIID);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder hashCode(String hashCode)
    {
      baseEntityDTO.setHashCode(hashCode);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder parentID(String parentID)
    {
      baseEntityDTO.parentID = parentID;
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder parentIID(long parentIID, EmbeddedType embeddedType)
    {
      baseEntityDTO.setParentIID(embeddedType, parentIID);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder topParentID(String topParentID)
    {
      baseEntityDTO.topParentID = topParentID;
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder topParentIID(long topParentIID)
    {
      baseEntityDTO.setTopParent(topParentIID);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder originBaseEntityIID(long originBaseEntityIID)
    {
      baseEntityDTO.setOriginBaseEntityIID(originBaseEntityIID);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder sourceCatalogCode(String catalogCode)
    {
      baseEntityDTO.setSourceCatalogCode(catalogCode);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder entityExtension(String jsonContent) throws CSFormatException
    {
      baseEntityDTO.setEntityExtension(jsonContent);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder propertyRecords(IPropertyRecordDTO... records)
    {
      baseEntityDTO.setPropertyRecords(records);
      return this;
    }
    
    @Override
    public IBaseEntityDTOBuilder propertyRecord(IPropertyRecordDTO record)
    {
      baseEntityDTO.propertyRecords.add(record);
      return this;
    }
    
    @Override
    public IBaseEntityDTO build()
    {
      return baseEntityDTO;
    }

    @Override
    public IBaseEntityDTOBuilder isClone(boolean isClone)
    {
      baseEntityDTO.isClone = isClone;
      return this;
    }

    @Override
    public IBaseEntityDTOBuilder endpointCode(String endpointCode)
    {
      baseEntityDTO.endpointCode = endpointCode;
      return this;
    }

  }

}
