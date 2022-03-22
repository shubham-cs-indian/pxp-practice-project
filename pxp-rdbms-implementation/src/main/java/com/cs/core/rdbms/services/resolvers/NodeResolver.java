package com.cs.core.rdbms.services.resolvers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cs.core.csexpress.CSEMeta;
import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource.Predefined;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Data Access Services to select RDBMS elements from CSE definitions
 *
 * @author vallee
 */
public class NodeResolver extends RDBMSDataAccessService {
  
  private static final String     Q_ENTITY_BY_ID_SAME_CATALOG        = "select be0.baseentityIID from pxp.baseentity be0 "
      + "join pxp.baseEntity be1 on be1.catalogCode = be0.catalogCode and be0.ismerged != true "
      + "where be0.baseentityID = ? and be1.baseentityIID = ?";
  private static final String     Q_ENTITY_BY_ORIGIN_PARENTS         = "select originBaseEntityIID, parentIID, topParentIID from pxp.baseentity "
      + "where baseentityIID = ?";
  private static final String     Q_ENTITY_BY_SOURCE_CATALOG         = "select be0.baseentityIID from pxp.baseentity be0 "
      + "join pxp.baseEntity be1 on be1.baseentityID = be0.baseentityID and be1.sourceCatalogCode = be0.catalogCode "
      + "where be1.baseentityIID = ?";
  private static final String     Q_ENTITY_BY_RELATIONIID_SOURCE_1   = "select re.side1entityIID as sourceBaseEntityIID from pxp.Relation re "
      + "join pxp.baseentity be on re.side1entityIID = be.baseentityiid and be.ismerged != true "
      + "where propertyIID = ? and side2entityIID = ? " + "order by side1entityIID asc";                                                                        // In
                                                                                                                                                                // case
                                                                                                                                                                // of
                                                                                                                                                                // multiple
                                                                                                                                                                // relations,
                                                                                                                                                                // the
                                                                                                                                                                // first
                                                                                                                                                                // created
  // entity is returned
  private static final String     Q_ENTITY_BY_RELATIONIID_SOURCE_2   = "select re.side2entityIID as sourceBaseEntityIID from pxp.Relation re "
      + "join pxp.baseentity be on re.side2entityIID = be.baseentityiid and be.ismerged != true "
      + "where propertyIID = ? and side1entityIID = ? " + "order by side2entityIID asc";                                                                        // In
                                                                                                                                                                // case
                                                                                                                                                                // of
                                                                                                                                                                // multiple
                                                                                                                                                                // relations,
                                                                                                                                                                // the
                                                                                                                                                                // first
                                                                                                                                                                // created
  private static final String     Q_PROPERTY_RECORD                  = "select propertyRecordIID from pxp.propertyrecord "
      + "where baseentityIID = ? and propertyIID = ?";
    private static final String     Q_ENTITY_THROUGH_RELATION_SOURCE_1 = "select r.side1entityIID as sourceBaseEntityIID "
        + "from pxp.relation r " + "join pxp.baseentity be on r.side1entityIID = be.baseentityiid and be.ismerged != true "
        + "where r.propertyIID = ? and r.side2entityIID = ? "
      + "order by r.side1entityIID asc";                                                                                                                        // In
                                                                                                                                                                // case
   private static final String  ENTITY_THROUGH_CONFLICTING_VALUES = "select * from pxp.conflictingvalues cv " 
       + "where cv.couplingsourceiid = ? and cv.propertyiid = ?";                                  
                                                                                                                                                                // multiple
                                                                                                                                                                // relations,
                                                                                                                                                                // the
                                                                                                                                                                // created
                                                                                                                                                                // entity
                                                                                                                                                                // is
  // retained as the source
  private static final String     Q_ENTITY_THROUGH_RELATION_SOURCE_2 = "select r.side2entityIID as sourceBaseEntityIID "
      + "from pxp.relation r " + "join pxp.baseentity be on r.side2entityIID = be.baseentityiid and be.ismerged != true "
      +"where r.propertyIID = ? and r.side1entityIID = ? "
      + "order by r.side2entityIID asc";
  private static final String     Q_ENTITY_BY_NATURE_CLASS           = "select classifierIID from pxp.baseentity where baseentityIID = ?";
  // entity is returned
  private static final String     Q_ENTITY_BY_ID                     = "select baseentityIID from pxp.baseentityIID where baseentityID = ? and catalogcode = ?";
  private final ILocaleCatalogDAO catalog;
  
  /**
   * Create a new service interface for node resolution
   *
   * @param driver
   * @param connection
   *          current connection
   * @param localeCatalog
   */
  public NodeResolver(RDBMSConnection connection,
      ILocaleCatalogDAO localeCatalog)
  {
    super(connection);
    this.catalog = localeCatalog;
  }
  
  /**
   * Complete the coupling source with IID specification
   *
   * @param source
   * @param sourceObjectIID
   */
  private static void resolveSource(ICSECouplingSource source, long sourceObjectIID)
      throws CSFormatException
  {
    if (source.isRelation())
      return; // since the relation source is a moving target, there is not way
              // to resolve it
    String sourceCode = "";
    long sourceIID = 0L;
    if (!source.isPredefined() && source.isEntity()) {
      sourceCode = source.toEntity()
          .getCode();
      sourceIID = source.toEntity()
          .getIID();
    }
    else if (!source.isPredefined() && source.isClassifier()) {
      sourceCode = source.toClassifier()
          .getCode();
      sourceIID = source.toClassifier()
          .getIID();
    }
    else {
      sourceCode = source.toPredefined()
          .toString();
    }
    if (sourceIID != 0L) // already resolved
      return;
    CSEObject resolvedSource = new CSEObject(
        source.isEntity() ? CSEObjectType.Entity : ICSEElement.CSEObjectType.Classifier);
    // replace the record source with its identity based on IID
    if (!sourceCode.isEmpty())
      resolvedSource.addMeta(new CSEMeta("was", sourceCode));
    resolvedSource.setIID(sourceObjectIID);
    ((CSECouplingSource) source).setSource(resolvedSource);
  }
  
  /**
   * Determine the base entity IID which is source of coupling
   *
   * @param source
   *          the source definition (which must be relationship or entity based)
   * @param baseEntityIID
   *          determines the base entity of reference
   * @return the source base entity IID or 0 if not found
   * @throws CSFormatException
   * @throws RDBMSException
   * @throws SQLException
   */
  public long getSourceBaseEntityIID(ICSECouplingSource source, long baseEntityIID)
      throws CSFormatException, RDBMSException, SQLException
  {
    long sourceBaseEntityIID = 0L;
    PreparedStatement query;
    if (source.isPredefined() && source.toPredefined() == Predefined.$entity) { // when
                                                                                // the
                                                                                // entity
                                                                                // itself
                                                                                // is
                                                                                // fetched,
                                                                                // just
                                                                                // return
                                                                                // baseEntityIID
      resolveSource(source, baseEntityIID);
      return baseEntityIID;
    }
    if (!source.isPredefined() && source.isEntity() && source.toEntity()
        .getIID() > 0) {
      sourceBaseEntityIID = source.toEntity()
          .getIID();
    }
    else if (!source.isPredefined() && source.isEntity()) {
      String baseEntityID = source.toEntity()
          .getCode();
      query = currentConnection.prepareStatement(Q_ENTITY_BY_ID_SAME_CATALOG);
      query.setString(1, baseEntityID);
      query.setLong(2, baseEntityIID);
      ResultSet rs = query.executeQuery();
      sourceBaseEntityIID = (rs.next() ? rs.getLong("baseEntityIID") : 0L);
    }
    else if (!source.isPredefined() && source.isRelation()) {
      if (source.toRelation()
          .getSide() == 0) {
        throw new CSFormatException(
            "Missing side specification in Coupling relation: " + source.toRelation());
      }
      int sourceSide = source.toRelation()
          .getSide();
      if (source.toRelation()
          .getIID() == 0) {
        IPropertyDTO relationship = configDao.getPropertyByCode(source.toRelation()
            .getCode());
        ((CSEProperty) source.toRelation()).setIID(relationship.getPropertyIID());
      }
      String queryStr = (sourceSide == 1 ? Q_ENTITY_BY_RELATIONIID_SOURCE_1
          : Q_ENTITY_BY_RELATIONIID_SOURCE_2);
      query = currentConnection.prepareStatement(queryStr);
      query.setLong(1, source.toRelation()
          .getIID());
      query.setLong(2, baseEntityIID);
      ResultSet rs = query.executeQuery();
      if (rs.next()) {
        sourceBaseEntityIID = rs.getLong("sourceBaseEntityIID");
      }
    }
    else if (source.isPredefined() && source.toPredefined() == Predefined.$source) {
      query = currentConnection.prepareStatement(Q_ENTITY_BY_SOURCE_CATALOG);
      query.setLong(1, baseEntityIID);
      ResultSet rs = query.executeQuery();
      sourceBaseEntityIID = (rs.next() ? rs.getLong("baseentityIID") : 0L);
    }
    else if (source.isPredefined() && (source.toPredefined() == Predefined.$origin
        || source.toPredefined() == Predefined.$parent
        || source.toPredefined() == Predefined.$top)) {
      query = currentConnection.prepareStatement(Q_ENTITY_BY_ORIGIN_PARENTS);
      query.setLong(1, baseEntityIID);
      ResultSet rs = query.executeQuery();
      switch (source.toPredefined()) {
        case $origin:
          sourceBaseEntityIID = (rs.next() ? rs.getLong("originBaseEntityIID") : 0L);
          break;
        case $parent:
          sourceBaseEntityIID = (rs.next() ? rs.getLong("parentIID") : 0L);
          break;
        default: // necessarily topparent
          sourceBaseEntityIID = (rs.next() ? rs.getLong("topparentIID") : 0L);
      }
    }
    resolveSource(source, sourceBaseEntityIID);
    return sourceBaseEntityIID;
  }
  
  /**
   * Return coupling master information from a source, a base entity IID and a
   * property
   *
   * @param source
   *          a predefined or source entity
   * @param baseEntityIID
   * @param property
   *          the coupled property
   * @return coupling IIDs
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   */
  public CouplingMaster getEntityCouplingMaster(ICSECouplingSource source, long baseEntityIID,
      IPropertyDTO property) throws CSFormatException, RDBMSException, SQLException
  {
    CouplingMaster master = new CouplingMaster(property.getIID(), property.getSuperType());
    master.entityIID = getSourceBaseEntityIID(source, baseEntityIID);
    if (master.entityIID == 0L) {
      RDBMSLogger.instance()
          .warn("Not found coupling source from " + source.toString());
      return master; // => cancel the creation of coupling record
    }
    if (property.getSuperType() == SuperType.RELATION_SIDE) {
      master.side = property.getRelationSide();
    }
    return master; // => cancel the creation of coupling record when nodeID = 0
  }
  
  /**
   * Return coupling master information from a relationship, a base entity IID
   * and a property
   *
   * @param relation
   * @param baseEntityIID
   * @param property
   * @return coupling IIDs
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   */
  public CouplingMaster getRelationCouplingMaster(ICSEProperty relation, long baseEntityIID,
      IPropertyDTO property) throws CSFormatException, RDBMSException, SQLException
  {
    CouplingMaster master = new CouplingMaster(property.getPropertyIID(), property.getSuperType());
    if (relation.getIID() == 0) {
      IPropertyDTO relationship = configDao.getPropertyByCode(relation.getCode());
      ((CSEProperty) relation).setIID(relationship.getPropertyIID());
    }
    if (relation.getSide() == 0) { 
      throw new CSFormatException("Missing side specification in coupling relation " + relation);
    }
    
     List<Long> sourceEntityIID = new ArrayList<>(); 
     List<Long> targetEntityIID = new ArrayList<>(); 

    
    PreparedStatement stmt = currentConnection.prepareStatement(ENTITY_THROUGH_CONFLICTING_VALUES);
    stmt.setLong(1, relation.getIID());
    stmt.setLong(2, property.getPropertyIID());
    stmt.execute(); 
    ResultSet resultSet = stmt.getResultSet();
    
    while(resultSet.next()) {
      sourceEntityIID.add(resultSet.getLong("sourceentityiid"));
      targetEntityIID.add(resultSet.getLong("targetentityiid"));

    }
     
    RelationSide sourceSide = RelationSide.valueOf(relation.getSide());
    String queryStr = (sourceSide == RelationSide.SIDE_1 ? Q_ENTITY_THROUGH_RELATION_SOURCE_1
        : Q_ENTITY_THROUGH_RELATION_SOURCE_2);
    PreparedStatement query = currentConnection.prepareStatement(queryStr);
    query.setLong(1, relation.getIID());
    query.setLong(2, baseEntityIID);
    ResultSet rs = query.executeQuery();
    Long sourceBaseEntityIID = null;
    
    while(rs.next()) {
      if(!sourceEntityIID.contains(rs.getLong("sourceBaseEntityIID")) || !targetEntityIID.contains(baseEntityIID)) {
        sourceBaseEntityIID = rs.getLong("sourceBaseEntityIID");
      }
    }
    if (sourceBaseEntityIID != null) {
      master.entityIID = sourceBaseEntityIID;
      master.side = property.getPropertyType() == PropertyType.RELATIONSHIP
          ? RelationSide.valueOf(relation.getSide())
          : RelationSide.UNDEFINED; 
    }
    else {
      RDBMSLogger.instance()
          .warn("INVALID COUPLING no relationship source %s for entity IID = %d",
              relation.toString(), baseEntityIID);
    }
    return master;
  }
  
  /**
   * Return coupling master information from a classifier
   *
   * @param source
   * @param baseEntityIID
   * @param property
   * @return
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   */
  public CouplingMaster getClassifierCouplingMaster(ICSECouplingSource source, long baseEntityIID,
      IPropertyDTO property) throws CSFormatException, RDBMSException, SQLException
  {
    PreparedStatement query;
    CouplingMaster master = new CouplingMaster(property.getPropertyIID(), property.getSuperType());
    if (!source.isPredefined() && source.isClassifier()) {
      String classifierCode = source.toClassifier()
          .getCode();
      IClassifierDTO classifier = catalog.newClassifierDTO(0L, classifierCode,
          IClassifierDTO.ClassifierType.UNDEFINED);
      master.entityIID = classifier.getClassifierIID();
    }
    else if (source.isPredefined() && source.toPredefined() == Predefined.$nature) {
      query = currentConnection.prepareStatement(Q_ENTITY_BY_NATURE_CLASS);
      query.setLong(1, baseEntityIID);
      ResultSet rs = query.executeQuery();
      if (rs.next()) {
        master.entityIID = rs.getLong("classifierIID");
      }
      else {
        throw new CSFormatException("Wrong default classifier source " + source);
      }
    }
    resolveSource(source, master.entityIID);
    return master;
  }
  
  /**
   * Retrieve a base entity IID from its CSE object definition
   *
   * @param cseEntity
   *          the CSE definition of the object
   * @return the corresponding base entity IID or 0 if not found
   */
  long getBaseEntityIID(ICSEObject cseEntity) throws CSFormatException, RDBMSException, SQLException
  {
    if (cseEntity.getIID() > 0)
      return cseEntity.getIID();
    String entityID = cseEntity.getCode();
    String catalogCode = cseEntity.getSpecification(ICSEElement.Keyword.$ctlg);
    if (catalogCode.isEmpty())
      catalogCode = catalog.getLocaleCatalogDTO()
          .getCatalogCode();
    PreparedStatement query = currentConnection.prepareStatement(Q_ENTITY_BY_ID);
    query.setString(1, entityID);
    query.setString(2, catalogCode);
    ResultSet rs = query.executeQuery();
    if (rs.next()) {
      return rs.getLong("baseentityIID");
    }
    return 0;
  }
  
  /**
   * A data structure returned as coupling master information
   */
  public class CouplingMaster {
    
    private final long   propertyIID;                           // the property
                                                                // of coupling
    private SuperType    propertyType = SuperType.UNDEFINED;
    private long         entityIID    = 0L;                     // the entity
                                                                // IID that
                                                                // holds the
                                                                // master record
    private RelationSide side         = RelationSide.UNDEFINED; // the source of
                                                                // coupling side
    
    private CouplingMaster(long propertyIID, SuperType superType)
    {
      this.propertyIID = propertyIID;
      this.propertyType = superType;
    }
    
    public IPropertyDTO.SuperType getType()
    {
      return this.propertyType;
    }
    
    public long getEntityIID()
    {
      return entityIID;
    }
    
    public String getNodeID()
    {
      if (entityIID == 0L)
        return "";
      return String.format("%d:%d%s", entityIID, propertyIID,
          (side != RelationSide.UNDEFINED ? ":" + side.ordinal() : ""));
    }
    
    public long getPropertyIID()
    {
      return propertyIID;
    }
  }
}
