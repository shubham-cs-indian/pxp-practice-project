package com.cs.core.rdbms.entity.dto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * The DTO representation of contextual data attached to a value record
 *
 * @author vallee
 */
public final class ContextualDataDTO extends RDBMSRootDTO implements IContextualDataDTO {

  private static final long  serialVersionUID     = 1L;
  public static final String ALLOW_DUPLICATE      = PXONTag.dupl.toTag(); // TODO: must be a context information
  private static final String LINKED_ENTITIES      = PXONTag.link.toCSEListTag();
  private TagsContentDTO contextTagRecords = new TagsContentDTO();
  private final Set<Long>    linkedBaseEntityIIDs = new HashSet<>(); // TODO: now objects are referenced by ID, not by IIDs
  private ContextDTO         context          = new ContextDTO();
  private long               startTime            = 0;
  private long               endTime              = 0;
  private boolean            allowDuplicate       = false;
  
  /**
   * Enabled default constructor
   */
  public ContextualDataDTO()
  {
  }

  /**
   * Enable Copy constructor
   * @param contextualDataDTO
   */
  public ContextualDataDTO(IContextualDataDTO contextualDataDTO)
  {
    super(contextualDataDTO.getContextualObjectIID());
    this.startTime = contextualDataDTO.getContextStartTime();
    this.endTime = contextualDataDTO.getContextEndTime();
    ITagDTO[] tagValues = contextualDataDTO.getContextTagValues().toArray(new ITagDTO[contextualDataDTO.getContextTagValues().size()]);
    setContextTagValues(tagValues);
    this.context = (ContextDTO) contextualDataDTO.getContext();
    Long[] linkedEntities = contextualDataDTO.getLinkedBaseEntityIIDs().toArray(new Long[contextualDataDTO.getLinkedBaseEntityIIDs().size()]);
    setLinkedBaseEntityIIDs(linkedEntities);
  }
  /**
   * Constructor with a predefined context (can be null)
   *
   * @param cxtCode
   */
  public ContextualDataDTO(String cxtCode)
  {
    setContextCode(cxtCode);
    setChanged(false);
  }
  
  /**
   * Value constructor
   *
   * @param contextualObjectIID
   * @param cxtCode
   * @param startTime
   * @param endTime
   */
  public ContextualDataDTO(long contextualObjectIID, String cxtCode, long startTime, long endTime)
  {
    super(contextualObjectIID);
    setContextCode(cxtCode);
    this.startTime = startTime;
    this.endTime = endTime;
    setChanged(true);
  }
  
  /**
   * Constructor from query
   *
   * @param parser
   * @throws SQLException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public ContextualDataDTO(IResultSetParser parser) throws SQLException, RDBMSException, CSFormatException
  {
    mapFromContextualObject(parser);
  }
  
  /**
   * reset the object to null/void information
   */
  private void resetToNull() {
    context = new ContextDTO();
    startTime = 0;
    endTime = 0;
    contextTagRecords.getTags().clear();
    linkedBaseEntityIIDs.clear();
  }

  /**
   * initialize contextual data from a request return
   * @param parser
   * @throws SQLException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  private void mapFromContextualObject(IResultSetParser parser) throws SQLException, RDBMSException, CSFormatException
  {
    String contextCode = parser.getString("contextCode");
    if ( !contextCode.isEmpty() ) {
      this.setIID(parser.getLong("contextualObjectIID"));
      context = (ContextDTO) ConfigurationDAO.instance().getContextByCode(contextCode);
      this.startTime = parser.getLong("cxtStartTime");
      // Decrementing end time by 1 as in DB int8range is used which increments the given TO/endTime value by 1.
      long cxtEndTime = parser.getLong("cxtEndTime");
      this.endTime = this.startTime == cxtEndTime ? cxtEndTime : cxtEndTime - 1;
      contextTagRecords = new TagsContentDTO(parser.getString("cxtTags"));
    }
    else {
      resetToNull();
    }
  }
  
  /**
   * initialize contextual data from a request return that contains as well linked entities
   * @param parser
   * @throws SQLException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public void mapFromContextualObjectWithLinkedEntities(IResultSetParser parser) throws SQLException, RDBMSException, CSFormatException
  {
    mapFromContextualObject(parser);
    String[] linkedEntities = parser.getString("cxtlinkedBaseEntitiyIIDs").split(",");
    for (String linkedEntity : linkedEntities) {
      if (!linkedEntity.isEmpty())
        this.linkedBaseEntityIIDs.add(Long.parseLong(linkedEntity));
    }
  }

  /**
   * initialize contextual data from an other one (IID is not changed)
   * @param source 
   */
  public void setFrom(IContextualDataDTO source) {
    setContextCode( source.getContextCode());
    setContextStartTime( source.getContextStartTime());
    setContextEndTime( source.getContextEndTime());
    Set<ITagDTO> sourceContextTagValues = source.getContextTagValues();
    contextTagRecords.setTags(sourceContextTagValues.toArray(new ITagDTO[sourceContextTagValues.size()]));
    linkedBaseEntityIIDs.clear();
    linkedBaseEntityIIDs.addAll( source.getLinkedBaseEntityIIDs());
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = new CSEObject(ICSEElement.CSEObjectType.ContextualObject);
    cse.setCode( context.getCode());
    if (!context.isNull())
      cse.setSpecification(ICSEElement.Keyword.$type, context.getContextType());
    if ( exportIID )
      cse.setIID( getIID());
    if ( startTime > 0 )
      cse.setSpecification(ICSEElement.Keyword.$start, String.format("%d", startTime));
    if ( endTime > 0 )
      cse.setSpecification(ICSEElement.Keyword.$end, String.format("%d", endTime));
    if ( !contextTagRecords.getTags().isEmpty() )
      cse.setSpecification(ICSEElement.Keyword.$tag, contextTagRecords.toCSETagSpecification());
    return cse;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException {
    CSEObject gcse = (CSEObject) cse;
    try {
      IContextDTO.ContextType contextType = IContextDTO.ContextType.valueOf( cse.getSpecification(Keyword.$type));
      if (contextType != IContextDTO.ContextType.UNDEFINED) {
        context = (ContextDTO) ConfigurationDAO.instance().createContext(gcse.getCode(), contextType);
      }
    } catch (IllegalArgumentException | RDBMSException ex) {
      RDBMSLogger.instance().warn("Contextual Object from %s has unknown context code", cse.toString());
      throw new CSFormatException( "Wrong context information from " + cse.toString());
    }
    setIID( gcse.getIID());
    if ( cse.containsSpecification(Keyword.$start) ) {
      String startSpec = cse.getSpecification(Keyword.$start);
      startTime = Long.parseLong( startSpec); // TODO: could also be in ISODATE TIME format
    }
    else {
      startTime = 0;
    }
    if ( cse.containsSpecification(Keyword.$end) ) {
      String endSpec = cse.getSpecification(Keyword.$end);
      endTime = Long.parseLong( endSpec); // TODO: could also be in ISODATE TIME format
    }
    else {
      endTime = 0;
    }
    contextTagRecords.getTags().clear();
    if ( cse.containsSpecification(Keyword.$tag) ) {
      contextTagRecords.fromCSETagSpecification( cse.getSpecification(Keyword.$tag));
    }
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    allowDuplicate = parser.getBoolean(ALLOW_DUPLICATE);
    contextTagRecords.getTags().clear();
    linkedBaseEntityIIDs.clear();
    String linksCse = parser.getString(LINKED_ENTITIES);
    if (!linksCse.isEmpty()) {
      CSEList entityList = (CSEList) (new CSEParser()).parseDefinition(linksCse);
      entityList.getSubElements().forEach((CSEObject) -> {
        linkedBaseEntityIIDs.add(((ICSEObject) CSEObject).getIID());
      });
    }
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    CSEList entityList = new CSEList();
    for (long entityIID : linkedBaseEntityIIDs) {
      CSEObject entity = new CSEObject(ICSEElement.CSEObjectType.Entity);
      entity.setIID(entityIID);
      entityList.getSubElements().add(entity);
    }
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        !entityList.isEmpty() ? JSONBuilder.newJSONField(LINKED_ENTITIES, entityList)
            : JSONBuilder.VOID_FIELD,
        allowDuplicate ? JSONBuilder.newJSONField(ALLOW_DUPLICATE, true)
            : JSONBuilder.VOID_FIELD);
  }
  
  @Override
  public long getContextualObjectIID()
  {
    return getIID();
  }
  
  @Override
  public long getContextStartTime()
  {
    return startTime;
  }
  
  @Override
  public void setContextStartTime(long time)
  {
    setChanged(startTime != time ? true : isChanged());
    startTime = time;
  }
  
  @Override
  public long getContextEndTime()
  {
    return endTime;
  }
  
  @Override
  public void setContextEndTime(long time)
  {
    setChanged(endTime != time ? true : isChanged());
    endTime = time;
  }
  
  @Override
  public Set<ITagDTO> getContextTagValues()
  {
    return contextTagRecords.getTags();
  }
  
  @Override
  public void setContextTagValues(ITagDTO... tags)
  {
    setChanged(true);
    contextTagRecords.setTags(tags);
  }
  
  @Override
  public String getContextCode()
  {
    return context.getCode();
  }
  
  /**
   * @param cxtCode overwritten context
   */
  public void setContextCode(String cxtCode)
  {
    if (cxtCode != null && !cxtCode.isEmpty()) {
      setChanged(true);
      try {
        context = (ContextDTO) ConfigurationDAO.instance().getContextByCode(cxtCode);
      }
      catch (RDBMSException ex) {
        RDBMSLogger.instance().exception(ex);
      }
    }
  }
  
  @Override
  public IContextDTO getContext()
  {
    return context;
  }
  
  /**
   * @return the context tag codes in an ordered list
   */
  public List<String> getContextTagValueCodes()
  {
    List<String> tagValueCodes = new ArrayList<>();
    getContextTagValues().forEach((dto) -> {
      tagValueCodes.add(dto.getTagValueCode());
    });
    return tagValueCodes;
  }
  
  /**
   * @return the context ranges in an ordered list
   */
  public List<Integer> getContextTagRanges()
  {
    List<Integer> relevancesList = new ArrayList<>();
    getContextTagValues().forEach((dto) -> {
      relevancesList.add(dto.getRange());
    });
    return relevancesList;
  }

  @Override
  public void setAllowDuplicate(boolean status) {
    allowDuplicate = status;
  }

  @Override
  public boolean getAllowDuplicate() {
    return allowDuplicate;
  }
  
  @Override
  public boolean isNull()
  {
    return context.isNull();
  }
  
  @Override
  public Set<Long> getLinkedBaseEntityIIDs()
  {
    return linkedBaseEntityIIDs;
  }
  
  @Override
  public void setLinkedBaseEntityIIDs(Long... linkedBaseEntityIIDs)
  {
    this.linkedBaseEntityIIDs.clear();
    setChanged(true);
    this.linkedBaseEntityIIDs.addAll(Arrays.asList(linkedBaseEntityIIDs));
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(7, 17).append(context.getCode())
        .append(contextTagRecords)
        .append(startTime)
        .append(endTime)
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
    final ContextualDataDTO other = (ContextualDataDTO) obj;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    if (!equalsBuilder.append(this.context.getCode(), other.context.getCode()).isEquals()) {
      return false;
    }
    if (this.startTime != other.startTime) {
      return false;
    }
    if (this.endTime != other.endTime) {
      return false;
    }
    if (!equalsBuilder.append(this.contextTagRecords, other.contextTagRecords).isEquals()) {
      return false;
    }
    return true;
  }
  
  @Override
  public int compareTo(Object obj)
  {
    if (this.getIID() != 0 && ((ContextualDataDTO) obj).getIID() != 0) {
      return super.compareTo(obj);
    }
    ContextualDataDTO that = (ContextualDataDTO) obj;
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    int compare = compareToBuilder.append(this.context.getCode(), that.context.getCode()).toComparison();
    if (compare != 0) {
      return compare;
    }
    compare = compareToBuilder.append(startTime, that.startTime).toComparison();
    if (compare != 0) {
      return compare;
    }
    compare = compareToBuilder.append(endTime, that.endTime).toComparison();
    if (compare != 0) {
      return compare;
    }
    for (ITagDTO thisTag : contextTagRecords.getTags()) {
      for (ITagDTO thatTag : that.contextTagRecords.getTags()) {
        compare = compareToBuilder.append(thisTag, thatTag).toComparison();
        if (compare != 0) {
          return compare;
        }
      }
    }
    return 0;
  }
  
  /**
   * 
   * @return combination of tag Value and tag range in form of hstore for contexttagvalues
   */
  public String getHStoreFormat()
  {
    return contextTagRecords.getHStoreFormat();
  }
}
