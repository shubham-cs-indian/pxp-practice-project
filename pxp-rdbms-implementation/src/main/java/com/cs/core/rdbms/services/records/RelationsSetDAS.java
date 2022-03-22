package com.cs.core.rdbms.services.records;

import com.cs.core.csexpress.CSEElement;
import com.cs.core.csexpress.CSEList;
import com.cs.core.data.CombinedIIDs;
import com.cs.core.data.Text;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.dto.EntityRelationDTO;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.NodeResolver;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author vallee
 */
public class RelationsSetDAS extends AbstractRecordDAS implements IRecordDAS {
  
  private static final String         Q_UPDATE_SIDE1_CONTEXTUALIID  = "update pxp.relation set side1contextualobjectiid = ? where side1entityiid =? and side2entityiid = ? ";
  private static final String         Q_UPDATE_SIDE2_CONTEXTUALIID  = "update pxp.relation set side2contextualobjectiid = ? where side1entityiid = ? and side2entityiid = ? ";
  private static final String         Q_GET_EXTENDED_CLASSIFIER_IID = "select classifieriid from pxp.baseEntity where baseentityiid = ?";
  private static final String         Q_GET_BASEENTITY_ID           = "select baseentityid from pxp.baseEntity where baseentityiid = ?";
  private static final String         Q_GET_BASEENTITY_ID_BY_IID          = "select baseEntityId,baseEntityIID from pxp.baseEntity where baseentityiid in (%s)";
  private static final String         Q_GET_RELATION                = "select * from pxp.relation where side1entityiid =? and side2entityiid = ? ";
  
  private final RelationsSetDTO       relationsSetRecord;
  private final Set<IRelationsSetDTO> addedRelationsSet            = new HashSet<>();
  private final Set<IRelationsSetDTO> removedRelationsSet          = new HashSet<>();
  
  /**
   * Relations data service constructor
   *
   * @param connection
   * @param record
   * @param baseentityIID to which the property belongs
   */
  RelationsSetDAS(RDBMSConnection connection, LocaleCatalogDAO catalog,
      IPropertyRecordDTO record, long baseentityIID)
  {
    super( connection, catalog, record, baseentityIID);
    relationsSetRecord = (RelationsSetDTO) record;
  }
  
  /**
   * build a series of relation sets from the return from
   * RelationsSetSideXFullContent
   *
   * @param parser
   *          the query return parser ordered by propertyRecord IID asc.
   * @return the setContentFrom of new relation sets
   * @throws SQLException
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  private static Set<IRelationsSetDTO> newRelationsSets(
      RDBMSConnection connection, IResultSetParser parser, LocaleCatalogDAO catalog)
      throws SQLException, RDBMSException, CSFormatException
  {
    Set<IRelationsSetDTO> newRelationSets = new HashSet<>();
    Map<Long, IRelationsSetDTO> relationsSetsMap = new HashMap<>();
    while (parser.next()) {
      long propertyIID = parser.getLong("propertyIID");
      RelationSide side = RelationSide.valueOf(parser.getInt("side"));
      long sideEntityIID = parser.getLong("sideEntityIID");
      String sideEntityID = ""; 
      PreparedStatement query = connection.prepareStatement(Q_GET_BASEENTITY_ID);
      query.setLong(1, sideEntityIID);
      IResultSetParser resultSetParser = connection.getResultSetParser(query.executeQuery());
      if (resultSetParser.next()) {
        sideEntityID = resultSetParser.getString("baseentityid");
      }
      
      EntityRelationDTO relation = new EntityRelationDTO(sideEntityID, sideEntityIID);
      ContextualDataDTO cxtObject = getContextualObjectByIID( connection,
          parser.getLong("sidecontextualobject"));
      relation.setContextualObject(cxtObject);
      // if already identified then complete to the existing relations set
      ContextualDataDTO otherCxtObject =getContextualObjectByIID( connection,
          parser.getLong("othersidecontextualobject"));
      relation.setOtherSideContextualObject(otherCxtObject);
      if (!relationsSetsMap.containsKey(propertyIID)) {
        IPropertyDTO property = catalog.getPropertyByIID(propertyIID);
        property.setRelationSide(side);
        IRelationsSetDTO relationsSet = new RelationsSetDTO(side, parser, property);
        relationsSetsMap.put(property.getPropertyIID(), relationsSet);
        newRelationSets.add(relationsSet);
      }
      relationsSetsMap.get(propertyIID)
          .getRelations()
          .add(relation);
    }
    return newRelationSets;
  }

  public static final String entityRelationFormat = "%d__%d__%d__%d__%d";

  private static Map<Long, Map<Long, IRelationsSetDTO>> prepareRelationSets(RDBMSConnection connection, IResultSetParser parser,
      LocaleCatalogDAO catalog) throws SQLException, RDBMSException, CSFormatException
  {
    Map<Long, Map<Long, IRelationsSetDTO>> baseEntityIIDvsRelations = new HashMap<>();

    Map<String, EntityRelationDTO> entityRelations = new HashMap<>();
    Map<String, String> entityIIDVsSideEntityID = new HashMap<>();

    while (parser.next()) {
      long propertyIID = parser.getLong("propertyIID");
      RelationSide side = RelationSide.valueOf(parser.getInt("side"));
      long sideEntityIID = parser.getLong("sideEntityIID");
      long contextObjectIID = parser.getLong("sideContextualObject");
      long otherSideContextObjectIID = parser.getLong("otherSideContextualObject");

      String entityRelation = String.format(entityRelationFormat, propertyIID, side.ordinal(), sideEntityIID, contextObjectIID,
          otherSideContextObjectIID);

      long entityIID = parser.getLong("entityIID");
      RecordStatus recordStatus = RecordStatus.valueOf(parser.getInt("recordStatus"));
      CouplingType couplingType = CouplingType.valueOf(parser.getInt("couplingType"));
      String masterEntityIID = parser.getString("masterEntityIID");
      String masterPropertyIID = parser.getString("masterPropertyIID");
      String masterNodeID = masterPropertyIID == null || masterEntityIID == null || masterEntityIID.isEmpty() || masterPropertyIID.isEmpty() ?
          "" :
          masterEntityIID + ":" + masterPropertyIID;

      IPropertyDTO property = catalog.getPropertyByIID(propertyIID);
      property.setRelationSide(side);
      Map<Long, IRelationsSetDTO> relationsSetMap = baseEntityIIDvsRelations.computeIfAbsent(entityIID, k -> new HashMap<>());
      IRelationsSetDTO relationsSet = relationsSetMap.computeIfAbsent(propertyIID,
          k -> new RelationsSetDTO(entityIID, property, side, recordStatus, couplingType, masterNodeID));
      EntityRelationDTO relation = new EntityRelationDTO(sideEntityIID);
      relationsSet.getRelations().add(relation);
      entityRelations.put(entityRelation, relation);
    }

    if(baseEntityIIDvsRelations.isEmpty()){
      return baseEntityIIDvsRelations;
    }
    Set<String> baseEntityIIDs = new HashSet<>();
    Set<String> contextIIDs = new HashSet<>();
    for (String entityRelation : entityRelations.keySet()) {
      String[] s = entityRelation.split("__");
      baseEntityIIDs.add(s[2]);
      if (!s[3].equals("0")) {
        contextIIDs.add(s[3]);
        ;
      }
      if (!s[4].equals("0")) {
        contextIIDs.add(s[4]);
      }
    }

    String finalQuery = String.format(Q_GET_BASEENTITY_ID_BY_IID, Text.join(",", baseEntityIIDs));
    PreparedStatement query = connection.prepareStatement(finalQuery);
    IResultSetParser resultSetParser = connection.getResultSetParser(query.executeQuery());
    while (resultSetParser.next()) {
      entityIIDVsSideEntityID.put(String.valueOf(resultSetParser.getLong("baseEntityIID")), resultSetParser.getString("baseEntityId"));
    }
    Map<Long, ContextualDataDTO> contextualObjectByIIDs = getContextualObjectByIIDs(connection, contextIIDs);

    for (Map.Entry<String, EntityRelationDTO> entry : entityRelations.entrySet()) {
      String key = entry.getKey();
      String[] s = key.split("__");
      String sideEntityIID = s[2];
      String contextIID = s[3];
      String otherContextIID = s[4];
      EntityRelationDTO entityRelation = entry.getValue();

      entityRelation.setOtherSideEntityID(entityIIDVsSideEntityID.get(sideEntityIID));
      if (!contextIID.equals("0")) {
        entityRelation.setContextualObject(contextualObjectByIIDs.get(Long.parseLong(contextIID)));
      }
      if (!otherContextIID.equals("0")) {
        entityRelation.setOtherSideContextualObject(contextualObjectByIIDs.get(Long.parseLong(otherContextIID)));
      }
    }

    return baseEntityIIDvsRelations;
  }
  /**
   * fetch classifier iid of extended class in relationship
   * 
   * @param connection
   * @param extendedEntityIID
   * @return
   * @throws SQLException
   * @throws RDBMSException
   */
  private static long getExtendedClassifierIID(RDBMSConnection connection, long extendedEntityIID) throws SQLException, RDBMSException
  {
    PreparedStatement query = connection.prepareStatement(Q_GET_EXTENDED_CLASSIFIER_IID);
    query.setLong(1, extendedEntityIID);
    IResultSetParser resultSetParser = connection.getResultSetParser(query.executeQuery());
    if (resultSetParser.next()) {
      return resultSetParser.getLong("classifieriid");
    }
    return 0;
  }
  
  /**
   * Load relations setContentFrom records attached to a base entity
   *
   * @param connection
   *          the current connection
   * @param catalog
   * @param baseEntityIID
   *          the concerned base entity IID
   * @param properties
   *          the related setContentFrom of properties
   * @return a setContentFrom of loaded property records
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static Set<IPropertyRecordDTO> loadRecords(
      RDBMSConnection connection, LocaleCatalogDAO catalog, long baseEntityIID,
      IPropertyDTO... properties) throws SQLException, RDBMSException, CSFormatException
  {
    
    Set<Long> relationSetsSide1PropertyIIDs = new HashSet<>();
    Set<Long> relationSetsSide2PropertyIIDs = new HashSet<>();
    for (IPropertyDTO property : properties) {
      if (property.getPropertyType()
          .getSuperType() == IPropertyDTO.SuperType.RELATION_SIDE) {
        switch (property.getRelationSide()) {
          case SIDE_1:
            relationSetsSide1PropertyIIDs.add(((PropertyDTO) property).getIID());
            break;
          case SIDE_2:
            relationSetsSide2PropertyIIDs.add(((PropertyDTO) property).getIID());
            break;
          default:
            throw new RDBMSException(0, "Program ERROR",
                " Relation Side is undefined while loading records");
        }
      }
    }
    Set<IPropertyRecordDTO> records = new HashSet<>();
    if (!relationSetsSide1PropertyIIDs.isEmpty()) {
      IResultSetParser rRecordResult = connection.getFunction( 
              RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_readRelationsSetRecord")
          .setInput(ParameterType.IID, baseEntityIID)
          .setInput(ParameterType.IID_ARRAY, relationSetsSide1PropertyIIDs)
          .setInput(ParameterType.INT, 1)
          .execute();
      records.addAll(newRelationsSets( connection, rRecordResult, catalog));
    }
    if (!relationSetsSide2PropertyIIDs.isEmpty()) {
      IResultSetParser rRecordResult = connection.getFunction(
              RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_readRelationsSetRecord")
          .setInput(ParameterType.IID, baseEntityIID)
          .setInput(ParameterType.IID_ARRAY, relationSetsSide2PropertyIIDs)
          .setInput(ParameterType.INT, 2)
          .execute();
      records.addAll(newRelationsSets( connection, rRecordResult, catalog));
    }
    return records;
  }

  /**
   * Load relations setContentFrom records attached to a base entity
   *
   * @param connection
   *          the current connection
   * @param catalog
   * @param baseEntityIID
   *          the concerned base entity IID
   * @param properties
   *          the related setContentFrom of properties
   * @return a setContentFrom of loaded property records
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static final String GET_ALL_RELATIONS = "SELECT * FROM pxp.allRelations where entityIID in (%s)";
  public static Map<Long, Map<Long, IRelationsSetDTO>> loadAllRecords(RDBMSConnection connection, LocaleCatalogDAO catalog, List<Long> baseEntityIIDs) throws SQLException, RDBMSException, CSFormatException
  {
    String finalQuery =  String.format(GET_ALL_RELATIONS, Text.join(",", baseEntityIIDs));

    PreparedStatement query = connection.prepareStatement(finalQuery);
    IResultSetParser rRecordResult = connection.getResultSetParser(query.executeQuery());
    Map<Long, Map<Long, IRelationsSetDTO>> relationSets = prepareRelationSets(connection, rRecordResult, catalog);

    return relationSets;
  }
  
  public Set<IRelationsSetDTO> getAddedRelationsSet()
  {
    return addedRelationsSet;
  }
  
  public Set<IRelationsSetDTO> getRemovedRelationsSet()
  {
    return removedRelationsSet;
  }
  
  private CSEList getRelations(Set<IRelationsSetDTO> relationsSets) throws CSFormatException
  {
    CSEList content = new CSEList();
    for (IRelationsSetDTO relationsSet : relationsSets) {
      CSEElement record = (CSEElement) relationsSet.toCSExpressID();
      record.addMeta(IPXON.PXONMeta.Content,
          ((RelationsSetDTO) relationsSet).joinSideBaseEntityIIDs());
      content.addElement(record);
    }
    return content;
  }
  
  /**
   * @return the relations and entities added by last update operation
   */
  public CSEList getAddedRelations() throws CSFormatException
  {
    return getRelations(addedRelationsSet);
  }
  
  /**
   * @return the relations and entities removed by last update operation
   */
  public CSEList getRemovedRelations() throws CSFormatException
  {
    return getRelations(removedRelationsSet);
  }
  
  @Override
  public void createCalculatedRecord() throws SQLException, RDBMSException
  {
    if (!record.isCalculated()) {
      return; // exclude non calculated records
    }
    throw new RDBMSException(0, "Program ERROR", "Cannot create calculated relations set.");
  }
  
  @Override
  public void updateCalculatedRecord() throws RDBMSException, SQLException
  {
    if (!record.isCalculated()) {
      return; // exclude non calculated records
    }
    throw new RDBMSException(0, "Program ERROR", "Cannot update calculated relations set.");
  }
  
  @Override
  protected void createNewDefaultRecord(NodeResolver.CouplingMaster master)
      throws SQLException, RDBMSException
  {
    throw new RDBMSException(0, "Program Error",
        "Unapplicable case: cannot create defaut values for a relations set.");
  }
  
  @Override
  public void createSimpleRecord() throws SQLException, RDBMSException, CSFormatException
  {
    if (record.isCalculated() || record.isCoupled()) {
      return;
    }
    String contextCode = relationsSetRecord.getRelationsContextCode();
    String otherSideContextCode = relationsSetRecord.getRelationsContextCode();

    ILocaleCatalogDTO localeCatalogDTO = catalog.getLocaleCatalogDTO();
    RelationSide side = relationsSetRecord.getSide();
    IResultSetParser result = driver
        .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.STRING,
            "pxp.fn_createRelations")
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, baseEntityIID)
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, relationsSetRecord.getProperty().getIID())
        .setInput(RDBMSAbstractFunction.ParameterType.INT, side.ordinal())
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, contextCode.isEmpty() ? null : contextCode)
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, otherSideContextCode.isEmpty() ? null : otherSideContextCode)
        .setInput(RDBMSAbstractFunction.ParameterType.IID_ARRAY, relationsSetRecord.getSideBaseEntityIIDs())
        .setInput(ParameterType.STRING, localeCatalogDTO.getLocaleID())
        .setInput(ParameterType.STRING, localeCatalogDTO.getCatalogCode())
        .setInput(ParameterType.STRING, localeCatalogDTO.getOrganizationCode()).execute();
    // The function fn_createRelationsSetRecord returns a combined IIDs made of
    // pairs of contextual
    // Object IIDs and extension Object IIDs
    CombinedIIDs combinedIIDs = new CombinedIIDs(result.getString());
    relationsSetRecord.updateFromCombinedIIDs(combinedIIDs);
    for (IEntityRelationDTO relation : relationsSetRecord.getRelations()) {
      if (!relation.getContextualObject().isNull() && relation.getContextualObject().isChanged()) {
        updateRelationSideContext(side, relationsSetRecord.getEntityIID(), relation.getOtherSideEntityIID(),
            relation.getContextualObject());
        
      }
      if (!relation.getOtherSideContextualObject().isNull() && relation.getOtherSideContextualObject().isChanged()) {
        updateRelationSideContext(
            side == IPropertyDTO.RelationSide.SIDE_1 ? IPropertyDTO.RelationSide.SIDE_2 : IPropertyDTO.RelationSide.SIDE_1,
            relation.getOtherSideEntityIID(), relationsSetRecord.getEntityIID(), relation.getOtherSideContextualObject());
      }
    }
  }
  
  /**
   * Load a relation set with current relations
   *
   * @param baseEntityIID
   * @param relationsSet
   * @return
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  private Set<IEntityRelationDTO> getExistingEntityRelations(long baseEntityIID,
      RelationsSetDTO relationsSet) throws RDBMSException, SQLException, CSFormatException
  {
    Set<IPropertyRecordDTO> loadedRelationsSet = loadRecords( currentConnection, catalog,
        baseEntityIID, relationsSet.getProperty());
    if (loadedRelationsSet.isEmpty()) {
      return new HashSet<>();
    }
    return (((IRelationsSetDTO) (loadedRelationsSet.iterator()
        .next())).getRelations());
  }
  
  /**
   * Identify the relations that must be removed from DB by comparing with
   * existing relations
   *
   * @param existingRelations
   * @return a clone of the relations set with the missing relations or null if
   *         nothing is missing
   */
  private RelationsSetDTO cloneWithMissingRelations(Set<IEntityRelationDTO> existingRelations)
  {
    removedRelationsSet.clear();
    Set<EntityRelationDTO> missingRelationsInRecord = relationsSetRecord
        .identifyMissingRelations(existingRelations);
    if (!missingRelationsInRecord.isEmpty()) {
      // Make a clone of this DTO with the missing relations for deleting
      // purpose
      RelationsSetDTO cloneWithMissingRelations = relationsSetRecord
          .cloneWithRelations(missingRelationsInRecord);
      removedRelationsSet.add(cloneWithMissingRelations);
      return cloneWithMissingRelations;
    }
    return null;
  }
  
  /**
   * Identify the relations that must be added in DB by comparing with existing
   * relations
   *
   * @param existingRelations
   * @return a clone of the relations set with the added relations or null if
   *         nothing is identified
   */
  private RelationsSetDTO cloneWithAddedRelations(Set<IEntityRelationDTO> existingRelations)
  {
    addedRelationsSet.clear();
    Set<EntityRelationDTO> addedRelationsInRecord = relationsSetRecord
        .identifyAdditionalRelations(existingRelations);
    if (!addedRelationsInRecord.isEmpty()) {
      RelationsSetDTO cloneWithAddedRelations = relationsSetRecord
          .cloneWithRelations(addedRelationsInRecord);
      addedRelationsSet.add(cloneWithAddedRelations);
      return cloneWithAddedRelations;
    }
    return null;
  }
  
  @Override
  public void updateRecord() throws SQLException, RDBMSException, CSFormatException
  {
    RelationSide side = relationsSetRecord.getSide();
    if (relationsSetRecord.isChanged()) {
      String contextCode = relationsSetRecord.getRelationsContextCode();
      String otherSideContextCode = relationsSetRecord.getOtherSideContextCode();

      Set<IEntityRelationDTO> existingRelations = getExistingEntityRelations(baseEntityIID,
          relationsSetRecord);
      // Identify the relations that must be removed from DB
      RelationsSetDTO missingRelationsSet = cloneWithMissingRelations(existingRelations);
      if (missingRelationsSet != null) {
        deleteRelations(missingRelationsSet.getSideBaseEntityIIDs());
      }
      // Identify the relations that must be created in DB
      RelationsSetDTO addingRelationsSet = cloneWithAddedRelations(existingRelations);
      ILocaleCatalogDTO localeCatalogDTO = catalog.getLocaleCatalogDTO();
      if (addingRelationsSet != null) {
        IResultSetParser result = driver
            .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.STRING, "pxp.fn_createRelations")
            .setInput(RDBMSAbstractFunction.ParameterType.LONG, baseEntityIID)
            .setInput(RDBMSAbstractFunction.ParameterType.LONG, relationsSetRecord.getProperty().getIID())
            .setInput(RDBMSAbstractFunction.ParameterType.INT, side.ordinal())
            .setInput(RDBMSAbstractFunction.ParameterType.STRING, contextCode.isEmpty() ? null : contextCode)
            .setInput(RDBMSAbstractFunction.ParameterType.STRING, otherSideContextCode.isEmpty() ? null : contextCode)
            .setInput(RDBMSAbstractFunction.ParameterType.IID_ARRAY, addingRelationsSet.getSideBaseEntityIIDs())
            .setInput(ParameterType.STRING, localeCatalogDTO.getLocaleID())
            .setInput(ParameterType.STRING, localeCatalogDTO.getCatalogCode())
            .setInput(ParameterType.STRING, localeCatalogDTO.getOrganizationCode()).execute();
        CombinedIIDs combinedIIDs = new CombinedIIDs(result.getString());
        // Updating tracking relation set DTO with contextual Object IID and
        // extension Object IID
        addingRelationsSet.updateFromCombinedIIDs(combinedIIDs);
      }
    }
    for (IEntityRelationDTO relation : relationsSetRecord.getRelations()) {
    
      if (isRelationExist(baseEntityIID, relation.getOtherSideEntityIID()) && !relation.getContextualObject().isNull() && relation.getContextualObject().isChanged()) {
        updateRelationSideContext(side, relationsSetRecord.getEntityIID(), relation.getOtherSideEntityIID(),
            relation.getContextualObject());
        
      }
      if (!relation.getOtherSideContextualObject().isNull() && relation.getOtherSideContextualObject().isChanged()) {
        updateRelationSideContext(
            side == IPropertyDTO.RelationSide.SIDE_1 ? IPropertyDTO.RelationSide.SIDE_2
                : IPropertyDTO.RelationSide.SIDE_1,
            relation.getOtherSideEntityIID(), relationsSetRecord.getEntityIID(), relation.getOtherSideContextualObject());
      }
    }
  }
  
  public Boolean isRelationExist(long side1BaseEntityiid, long side2BaseEntityiid) throws SQLException, RDBMSException
  {
    PreparedStatement query = currentConnection.prepareStatement(Q_GET_RELATION);
    query.setLong(1, side1BaseEntityiid);
    query.setLong(2, side2BaseEntityiid);
    IResultSetParser resultSetParser = currentConnection.getResultSetParser(query.executeQuery());
    if (resultSetParser.next()) {
      return true;
    }
    
    return false;
  }

  @Override
  public void separateCoupledTargets(Long propertyIID, Long masterEntityIID) throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_CreateSeparatedValueTargets")
    .setInput(RDBMSAbstractFunction.ParameterType.LONG, propertyIID)
    .setInput(RDBMSAbstractFunction.ParameterType.LONG, masterEntityIID)
    .setInput(ParameterType.STRING, null)
    .execute();
  }
  
  @Override
  protected void updateAsTightlyCoupledRecord()
      throws SQLException, RDBMSException, CSFormatException
  {
    // This strictly requires to:
    // 1. identify the differences with existing in order to satisfy to change
    // events
    // 2. create a new content with the updated content
    if (relationsSetRecord.isChanged()) {
      Set<IEntityRelationDTO> existingRelations = getExistingEntityRelations(baseEntityIID,
          relationsSetRecord);
      // Identify the relations that must be removed from DB
      RelationsSetDTO missingRelationsSet = cloneWithMissingRelations(existingRelations);
      // Identify the relations that must be created in DB
      RelationsSetDTO addingRelationsSet = cloneWithAddedRelations(existingRelations);
      // The proceed to create the separated coupled record with current
      // contents
      String contextCode = relationsSetRecord.getRelationsContextCode();
      ILocaleCatalogDTO localeCatalogDTO = catalog.getLocaleCatalogDTO();
      IResultSetParser result = driver
          .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.STRING, "pxp.fn_updateCoupledRelationsSetRecord")
          .setInput(RDBMSAbstractFunction.ParameterType.IID, baseEntityIID)
          .setInput(RDBMSAbstractFunction.ParameterType.IID, relationsSetRecord.getProperty().getIID())
          .setInput(RDBMSAbstractFunction.ParameterType.INT, relationsSetRecord.getSide().ordinal())
          .setInput(RDBMSAbstractFunction.ParameterType.STRING, contextCode.isEmpty() ? null : contextCode)
          .setInput(RDBMSAbstractFunction.ParameterType.IID_ARRAY, relationsSetRecord.getSideBaseEntityIIDs())
          .setInput(ParameterType.STRING, localeCatalogDTO.getLocaleID()) 
          .setInput(ParameterType.STRING, localeCatalogDTO.getCatalogCode()) 
          .setInput(ParameterType.STRING, localeCatalogDTO.getOrganizationCode())
          .execute();
      // The function fn_updateCoupledRelationsSetRecord returns a combined IIDs
      // made of the
      // propertyRecord IID followed by pairs of contextual Object IIDs and
      // extension Object IIDs
      if (result.getString() != null) {        
        CombinedIIDs combinedIIDs = new CombinedIIDs(result.getString());
        relationsSetRecord.updateFromCombinedIIDs(combinedIIDs);
      }
    }
    
    for (IEntityRelationDTO relation : relationsSetRecord.getRelations()) {
      if (!relation.getContextualObject().isNull() && relation.getContextualObject().isChanged()) {
        updateRelationSideContext(relationsSetRecord.getSide(), relationsSetRecord.getEntityIID(), relation.getOtherSideEntityIID(),
            relation.getContextualObject());
      }
    }
  }
  
  @Override
  protected void updateAsDecoupledRecord() throws SQLException, RDBMSException, CSFormatException
  {
    updateAsTightlyCoupledRecord(); // same case here, the PSQL call to
                                    // pxp.sp_ResolveCoupling delte
    // the coupling entry according to the type of coupling
  }
  
  private void updateRelationSideContext(IPropertyDTO.RelationSide relationsSide, long entityIID, long otherSideEntityIID,
      IContextualDataDTO contextualObject) throws SQLException, RDBMSException
  {
    long currentContextualIId = contextualObject.getContextualObjectIID();
    long updateContextualIId = updateContextualData((ContextualDataDTO) contextualObject);
    if (currentContextualIId != updateContextualIId) {
      int count = 0;
      if (IPropertyDTO.RelationSide.SIDE_1 == relationsSide) {
        PreparedStatement statement = currentConnection.prepareStatement(Q_UPDATE_SIDE1_CONTEXTUALIID);
        statement.setLong(1, updateContextualIId);
        statement.setLong(2, entityIID);
        statement.setLong(3, otherSideEntityIID);
        count = statement.executeUpdate();
      }
      else if (IPropertyDTO.RelationSide.SIDE_2 == relationsSide) {
        
        PreparedStatement statement = currentConnection.prepareStatement(Q_UPDATE_SIDE2_CONTEXTUALIID);
        statement.setLong(1, updateContextualIId);
        statement.setLong(2, otherSideEntityIID);
        statement.setLong(3, entityIID);
        count = statement.executeUpdate();
        
      }
      else {
        throw new RDBMSException(0, "Invalid relation side", "Unable to update relation context because of invalid side");
      }
      if (count == 0) {
        throw new RDBMSException(0, "unable to update side context on relation",
            "side1entityiid or side2entityiid doesnot exist in relation");
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void deleteRecord() throws RDBMSException, SQLException, CSFormatException
  {
    deleteRelations(relationsSetRecord.getSideBaseEntityIIDs());
    removedRelationsSet.add(relationsSetRecord);
  }
  
  @Override
  public void deleteAsSourceOfCoupling() throws RDBMSException, SQLException, CSFormatException
  {
    driver.getProcedure(currentConnection, "pxp.sp_CreateRelationsFromAllTargetsAndDelete")
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, record.getNodeID())
        .setInput(RDBMSAbstractFunction.ParameterType.INT, relationsSetRecord.getSide()
            .ordinal())
        .execute();
  }
  
  private void deleteRelations(List<Long> list)
      throws RDBMSException, SQLException, CSFormatException
  {
    driver.getProcedure(currentConnection, "pxp.sp_deleteRelations")
        .setInput(RDBMSAbstractFunction.ParameterType.IID, relationsSetRecord.getProperty()
            .getPropertyIID())
        .setInput(RDBMSAbstractFunction.ParameterType.IID, relationsSetRecord.getEntityIID())
        .setInput(RDBMSAbstractFunction.ParameterType.INT, relationsSetRecord.getSide()
            .ordinal())
        .setInput(RDBMSAbstractFunction.ParameterType.IID_ARRAY, list)
        .execute();
  }
  
  @Override
  public void resolveNotification() throws RDBMSException, SQLException
  {
    if (record.isNotifiedByCoupling()) {
      driver.getProcedure(currentConnection, "pxp.sp_resolveRelationsRecordNotification")
          .setInput(RDBMSAbstractFunction.ParameterType.IID, baseEntityIID)
          .setInput(RDBMSAbstractFunction.ParameterType.IID, record.getProperty()
              .getIID())
          .setInput(RDBMSAbstractFunction.ParameterType.IID_ARRAY,
              relationsSetRecord.getSideBaseEntityIIDs())
          .setInput(RDBMSAbstractFunction.ParameterType.INT, relationsSetRecord.getSide()
              .ordinal())
          .setInput(RDBMSAbstractFunction.ParameterType.INT, record.getRecordStatus()
              .ordinal())
          .execute();
    }
  }
}
