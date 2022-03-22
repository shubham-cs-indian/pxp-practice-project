package com.cs.core.rdbms.entity.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.data.CombinedIIDs;
import com.cs.core.data.Text;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DTO implementation of the setContentFrom of DTOs
 *
 * @author vallee
 */
public class RelationsSetDTO extends PropertyRecordDTO implements IRelationsSetDTO {
  
  public static final String                RELATIONS         = PXONTag.link.toJSONArrayTag();
  public static final String                EXTENSION_CLASS   = PXONTag.extclass.toReadOnlyCSETag();
  private final TreeSet<IEntityRelationDTO> relations         = new TreeSet<>();
  private RelationSide                      holderSide        = RelationSide.UNDEFINED;
  private long                              extensionClassIID = 0;
  
  /**
   * Enabled default constructor
   */
  public RelationsSetDTO()
  {
  }
  
  /**
   * Value constructor
   *
   * @param entityIID
   * @param property
   * @param holderSide
   */
  public RelationsSetDTO(long entityIID, IPropertyDTO property, RelationSide holderSide)
  {
    super(entityIID, property);
    this.holderSide = holderSide;
  }

  public RelationsSetDTO(long entityIID, IPropertyDTO property, RelationSide holderSide, RecordStatus status, CouplingType coupling,
      String masterNodeID)
  {
    super(entityIID, property);
    this.holderSide = holderSide;
    this.recordStatus = status;
    this.couplingType = coupling;
    this.masterNodeID = masterNodeID;
  }
  /**
   * Constructor from query
   *
   * @param side
   * @param parser
   * @param property
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public RelationsSetDTO(RelationSide side, IResultSetParser parser, IPropertyDTO property)
      throws SQLException, CSFormatException
  {
    super(parser, property);
    holderSide = side;
  }
  
  /**
   * Clone this with a different set of relations
   *
   * @param thatRelationSet
   * @return a identical RelationsSetDTO with the defined set of relations
   */
  public RelationsSetDTO cloneWithRelations(Set<EntityRelationDTO> thatRelationSet)
  {
    RelationsSetDTO that = new RelationsSetDTO(this.getIID(), this.property, this.holderSide);
    that.relations.addAll(thatRelationSet);
    return that;
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject gcse = initCSExpressID(CSEObjectType.RelationSideRecord);
    gcse.setSpecification(ICSEElement.Keyword.$side, String.format("%d", holderSide.ordinal()));
    return gcse;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    super.fromCSExpressID(cse);
    String side = cse.getSpecification(ICSEElement.Keyword.$side);
    holderSide = RelationSide.valueOf(Integer.parseInt(side));
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    String extClassCse = parser.getString(EXTENSION_CLASS);
    if (!extClassCse.isEmpty())
      extensionClassIID = ((CSEObject) parser.getCSEElement(EXTENSION_CLASS)).getIID();
    else
      extensionClassIID = 0L;
    relations.clear();
    for (Object relationJSON : parser.getJSONArray(RELATIONS)) {
      JSONContentParser relationParser = new JSONContentParser((JSONObject) relationJSON);
      EntityRelationDTO relation = new EntityRelationDTO();
      relation.fromJSON(relationParser);
      relations.add(relation);
    }
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    CSEObject extClass = new CSEObject(CSEObjectType.Classifier);
    extClass.setIID(extensionClassIID);
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        JSONBuilder.newJSONArray(RELATIONS, relations),
        extensionClassIID != 0L ? JSONBuilder.newJSONField(EXTENSION_CLASS, extClass) : JSONBuilder.VOID_FIELD);
  }
  
  @Override
  public Long[] getReferencedBaseEntityIIDs()
  {
    Long[] referencedBaseEntityIIDs = new Long[relations.size()];
    int i = 0;
    for (IEntityRelationDTO relation : relations) {
      referencedBaseEntityIIDs[i++] = relation.getOtherSideEntityIID();
    }
    return referencedBaseEntityIIDs;
  }
  
  @Override
  public long getEntityIID()
  {
    return getIID(); // the owner of the relations set
  }
  
  @Override
  public RelationSide getSide()
  {
    return holderSide;
  }
  
  @Override
  public boolean isEmpty()
  {
    return (relations.isEmpty());
  }
  
  @Override
  public Set<IEntityRelationDTO> getRelations()
  {
    return relations;
  }
  
  @Override
  public void setRelations(Long... baseEntityIIDs)
  {
    this.relations.clear();
    addRelations(baseEntityIIDs);
  }
  
  @Override
  public void addRelations(Long... baseEntityIIDs)
  {
    setChanged(baseEntityIIDs.length > 0);
    for (long baseEntityIID : baseEntityIIDs) {
      this.relations.add(new EntityRelationDTO(baseEntityIID));
    }
  }

  @Override
  public void addRelations(IEntityRelationDTO... relationDTOs)
  {
    setChanged(relationDTOs.length > 0);
    for (IEntityRelationDTO relation : relationDTOs) {
      this.relations.add(new EntityRelationDTO(relation));
    }
  }

  @Override
  public void removeRelations(Long... baseEntityIIDs)
  {
    setChanged(baseEntityIIDs.length > 0);
    Iterator<IEntityRelationDTO> iterator = this.relations.iterator();
    while (iterator.hasNext()) {
      IEntityRelationDTO relation = iterator.next();
      for (long baseEntityIID : baseEntityIIDs) {
        if (relation.getOtherSideEntityIID() == baseEntityIID) {
          iterator.remove();
          break;
        }
      }
    }
  }
  
  /**
   * Remove relations with given base entities
   *
   * @param baseEntityIIDs
   */
  public void removeRelations(Collection<Long> baseEntityIIDs)
  {
    removeRelations(baseEntityIIDs.toArray(new Long[0]));
  }
  
  /**
   * @param extensionClassIID
   *          overwritten extension classifier IID
   * @param relations
   *          overwritten setContentFrom of relations
   */
  public void setRelations(long extensionClassIID, Set<IEntityRelationDTO> relations)
  {
    this.extensionClassIID = extensionClassIID;
    this.relations.clear();
    this.relations.addAll(relations);
    setChanged(true);
  }
  
  @Override
  public void setContentFrom(IPropertyRecordDTO source)
  {
    RelationsSetDTO relSource = (RelationsSetDTO) source;
    this.holderSide = relSource.holderSide;
    this.relations.clear();
    this.relations.addAll(relSource.getRelations());
    setChanged(true);
  }
  
  @Override
  public IEntityRelationDTO getRelationByIID(long baseEntityIID)
  {
    for (IEntityRelationDTO relation : relations) {
      if (relation.getOtherSideEntityIID() == baseEntityIID) {
        return relation;
      }
    }
    return null;
  }
  
  /**
   * @return the list of side base entity IIDs
   */
  public List<Long> getSideBaseEntityIIDs()
  {
    List<Long> entityIIDList = new ArrayList<>();
    relations.forEach((dto) -> {
      entityIIDList.add(dto.getOtherSideEntityIID());
    });
    return entityIIDList;
  }
  
  /**
   * @return the list of side base entity IIDs as a CSV String
   */
  public String joinSideBaseEntityIIDs()
  {
    return Text.join( ",", relations.stream()
            .map( IEntityRelationDTO::getOtherSideEntityIID)
            .collect( Collectors.toSet()));
  }
  
  /**
   * @param key
   *          the JSON key to use in tracking data
   * @param holderEntity
   * @return the setContentFrom of tracking data used to side effect events
   * @throws CSFormatException in case of format error
   */
  public List<String> getSideTrackingData(ITimelineDTO.ChangeCategory key,
      IBaseEntityDTO holderEntity) throws CSFormatException
  {
    List<String> trakingData = new ArrayList<>();
    for (IEntityRelationDTO relation : relations) {
      RelationsSetDTO reversedRelation = new RelationsSetDTO(relation.getOtherSideEntityIID(),
          property, holderSide.getOppositeSide());
      TimelineDTO change = new TimelineDTO(key, reversedRelation);
      change.register(key, IPXON.PXONMeta.Content,
          String.format("%d", holderEntity.getBaseEntityIID()));
      trakingData.add(change.toJSON());
    }
    return trakingData;
  }
  
  /**
   * Make the negative difference between reference relations and the content of
   * this relations Set
   *
   * @param refRelations
   *          a comparison set of relations taken as reference
   * @return the set of missing relations in this DTO
   */
  public Set<EntityRelationDTO>  identifyMissingRelations(Set<IEntityRelationDTO> refRelations)
  {
    HashSet<EntityRelationDTO> result = new HashSet<>();
    refRelations.forEach((IEntityRelationDTO relation) -> {
      if (!relations.contains((EntityRelationDTO) relation)) {
        result.add((EntityRelationDTO) relation);
      }
    });
    return result;
  }
  
  /**
   * Make the positive difference between reference relations and the content of
   * this relations Set
   *
   * @param refRelations
   *          a comparison set of relations taken as reference
   * @return the set of additional relations in this DTO
   */
  public Set<EntityRelationDTO> identifyAdditionalRelations(Set<IEntityRelationDTO> refRelations)
  {
    HashSet<EntityRelationDTO> result = new HashSet<>();
    relations.forEach((IEntityRelationDTO relation) -> {
      if (!refRelations.contains((EntityRelationDTO) relation)) {
        result.add((EntityRelationDTO) relation);
      }
    });
    return result;
  }
  
  /**
   * @return the context IID attached to the relations of this relations Set or
   *         0 if not defined
   */
  public String getRelationsContextCode()
  {
    // if contextual object exists, there is the same context for all relations
    // => only the first
    // element is checked
    if (!relations.isEmpty()) {
      IContextualDataDTO firstContextualObject = relations.first()
          .getContextualObject();
      return (firstContextualObject == null ? "" : firstContextualObject.getContextCode());
    }
    return "";
  }

  /**
   * @return the context IID attached to the relations of this relations Set or
   *         0 if not defined
   */
  public String getOtherSideContextCode()
  {
    // if contextual object exists, there is the same context for all relations
    // => only the first
    // element is checked
    if (!relations.isEmpty()) {
      IContextualDataDTO firstContextualObject = relations.first().getOtherSideContextualObject();
      return (firstContextualObject == null ? "" : firstContextualObject.getContextCode());
    }
    return "";
  }
  
  /**
   * Set the contextual object IIDs of the relations from a combined IIDs string
   * as returned by PSQL
   *
   * @param contextualObjectIIDs
   *          the contextualObjectIIDs of relations as returned as String
   * @return true when contextual information has been updated
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public boolean hasCompletedContextualObjectIID(String[] contextualObjectIIDs)
      throws RDBMSException
  {
    if (contextualObjectIIDs.length <= 1) {
      return false; // no contextual object IIDs returned in that IIDs
    }
    // When a context is specified, it is required to get the returned
    // contextual Object IIDs
    // from position 1 in contextualObjectIIDs
    if (contextualObjectIIDs.length != relations.size() + 1) {
      throw new RDBMSException(100, "Inconsitency",
          "Number of contextual IIDs don't match number of relations");
    }
    int index = 1;
    for (IEntityRelationDTO relation : relations) {
      ContextualDataDTO contextualObject = (ContextualDataDTO) relation.getContextualObject();
      if (contextualObject == null) {
        continue; // no context is defined for this relation (should not happen)
      }
      contextualObject.setIID(Long.parseLong(contextualObjectIIDs[index]));
      index++;
    }
    return true;
  }
  
  /**
   * @param combinedIIDs
   * @throws RDBMSException
   */
  public void updateFromCombinedIIDs(CombinedIIDs combinedIIDs) throws RDBMSException
  {
    if (combinedIIDs.hasCombinedIIDs()) {
      
      long[] contextualObjectIIDs = combinedIIDs.getContextualObjectIIDs();
      long[] otherContextualObjectIIDs = combinedIIDs.getOtherContextualObjectIIDs();

      if (contextualObjectIIDs.length != relations.size()) {
        // should never happen
        throw new RDBMSException(100, "Inconsitency",
            "Number of contextual IIDs don't match number of relations");
      }
      
      int index = 0;
      for (IEntityRelationDTO relation : relations) {
        ContextualDataDTO contextualObject = (ContextualDataDTO) relation.getContextualObject();
        
        if (contextualObject != null && !contextualObject.isNull())
          contextualObject.setIID(contextualObjectIIDs[index]);

        ContextualDataDTO otherCtxObject = (ContextualDataDTO) relation.getOtherSideContextualObject();
        if (otherCtxObject != null && !otherCtxObject.isNull())
          otherCtxObject.setIID(otherContextualObjectIIDs[index]);

        index++;
      }
    }
  }
  
  @Override
  public int compareTo(Object t)
  {
    int comparison = super.compareTo(t);
    if (comparison != 0) {
      return comparison;
    }
    return (holderSide.ordinal() - ((RelationsSetDTO) t).holderSide.ordinal());
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(7, 11).append(super.hashCode())
        .append(holderSide)
        .build();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    boolean test = super.equals(obj);
    if (!test) {
      return false;
    }
    return holderSide == ((RelationsSetDTO) obj).holderSide;
  }
  /**
   * implementation of IRelationsSetDTOBuilder
   * @author Janak.Gurme
   *
   */
  public static class RelationsSetDTOBuilder implements IRelationsSetDTOBuilder {
    
    private final RelationsSetDTO relationSetDTO;
    
    /**
     * minimal mandatory fields to prepare RelationsSetDTO
     * 
     * @param entityIID
     * @param relationship
     * @param side
     */
    public RelationsSetDTOBuilder(long entityIID, IPropertyDTO relationship,
        IPropertyDTO.RelationSide side)
    {
      relationSetDTO = new RelationsSetDTO(entityIID, relationship, side);
    }
    
    @Override
    public IRelationsSetDTOBuilder relations(Long... baseEntityIIDs)
    {
      relationSetDTO.setRelations(baseEntityIIDs);
      return this;
    }
    
    @Override
    public IRelationsSetDTOBuilder addRelations(Long... baseEntityIIDs)
    {
      relationSetDTO.addRelations(baseEntityIIDs);
      return this;
    }
    
    @Override
    public IRelationsSetDTOBuilder recordStatus(RecordStatus recordStatus)
    {
      relationSetDTO.recordStatus = recordStatus;
      return this;
    }
    
    @Override
    public IRelationsSetDTOBuilder couplingType(CouplingType couplingType)
    {
      relationSetDTO.couplingType = couplingType;
      return this;
    }

    @Override
    public IRelationsSetDTOBuilder isVersionable(boolean isVersionable)
    {
      relationSetDTO.isVersionable = isVersionable;
      return this;
    }

    @Override
    public IRelationsSetDTO build()
    {
      return relationSetDTO;
    }

    
  }
}
