package com.cs.core.rdbms.services.records;

import com.cs.config.standard.IStandardConfig.GraphCase;
import com.cs.core.data.Text;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.NodeResolver;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Abstract implementation of record service
 *
 * @author vallee
 */
public abstract class AbstractRecordDAS extends RDBMSDataAccessService implements IRecordDAS {
  
  private static final String            Q_COUPLING_TARGETS       = "select entityIID || ':' || propertyIID as nodeID, couplingBehavior from pxp.coupledrecord "
      + "where masterNodeID = ? and  recordStatus <> " + RecordStatus.NOTIFIED.ordinal();
  private static final String            Q_IS_CLONED_RECORD       = "select count(1) from pxp.clonedpropertyrecords where clonedpropertyrecords.propertyrecordiid = ?";
  private static final String            Q_CONTEXTUAL_OBJECT      = "select contextualObjectIID, contextCode, cxtTags,"
      + " lower(cxtTimeRange) as cxtStartTime, upper(cxtTimeRange) as cxtEndTime"
      + " from pxp.contextualObject where contextualObjectIID = ?";

  private static final String            Q_CONTEXTUAL_OBJECTS      = "select contextualObjectIID, contextCode, cxtTags,"
      + " lower(cxtTimeRange) as cxtStartTime, upper(cxtTimeRange) as cxtEndTime"
      + " from pxp.contextualObject where contextualObjectIID in (%s)";
  private static final String            GET_COUPLED_RECORD       = "Select count(*) from pxp.conflictingvalues where targetentityiid = ? and propertyiid = ?";
  protected final NodeResolver           nodeResolver;
  protected final long                   baseEntityIID;
  protected final List<String>           calculationTargetNodeIDs = new ArrayList<>();
  protected final List<String>           couplingTargetNodeIDs    = new ArrayList<>();
  protected final List<CouplingBehavior> targetCouplingBehavior   = new ArrayList<>();
  protected PropertyRecordDTO            record;
  protected LocaleCatalogDAO             catalog;
  private int                            calculationLoadedCase    = -1;
  private boolean                        couplingTargetLoaded     = false;
  
  /**
   * Create a new service interface
   *
   * @param connection current connection
   * @param catalog
   * @param record the underlying record
   * @param baseentityIID baseentityIID to which the property belongs
   */
  protected AbstractRecordDAS(RDBMSConnection connection,
      LocaleCatalogDAO catalog, IPropertyRecordDTO record, long baseentityIID)
  {
    super(connection);
    this.nodeResolver = new NodeResolver( connection, catalog);
    this.record = (PropertyRecordDTO) record;
    this.baseEntityIID = baseentityIID;
    this.catalog = catalog;
  }
  
  /**
   * @param nb
   *          of asking marks
   * @return a strings of comma separated asking marks for JDBC statements
   */
  protected static String buildAskingMarkSeries(int nb)
  {
    StringBuffer askingMarkSeries = new StringBuffer();
    for (int i = 0; i < nb; i++) {
      askingMarkSeries.append("?,");
    }
    return askingMarkSeries.deleteCharAt(askingMarkSeries.length() - 1)
        .toString();
  }
  
  /**
   * @param currentConnection
   * @param contextualObjectIID
   * @return the loaded contextual object
   * @throws SQLException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  protected static ContextualDataDTO getContextualObjectByIID(
      RDBMSConnection currentConnection, long contextualObjectIID)
      throws SQLException, RDBMSException, CSFormatException
  {
    if (contextualObjectIID <= 0)
      return new ContextualDataDTO();
    PreparedStatement cxtObjectQuery = currentConnection.prepareStatement(Q_CONTEXTUAL_OBJECT);
    cxtObjectQuery.setLong(1, contextualObjectIID);
    IResultSetParser cxtObjectResult = currentConnection.getResultSetParser(cxtObjectQuery.executeQuery());
    if (cxtObjectResult.next()) {
      return new ContextualDataDTO(cxtObjectResult);
    }
    throw new RDBMSException(0, "Program ERROR",
        "contextual object IID not found: " + contextualObjectIID);
  }


  /**
   * @param currentConnection
   * @param contextualObjectIID
   * @return the loaded contextual object
   * @throws SQLException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  protected static Map<Long, ContextualDataDTO> getContextualObjectByIIDs(
      RDBMSConnection currentConnection, Set<String> contextualObjectIID)
      throws SQLException, RDBMSException, CSFormatException
  {
    Map<Long, ContextualDataDTO> contextualData = new HashMap<>();
    if (!contextualObjectIID.isEmpty()) {
      String finalQuery = String.format(Q_CONTEXTUAL_OBJECTS, Text.join(",", contextualObjectIID));
      PreparedStatement cxtObjectQuery = currentConnection.prepareStatement(finalQuery);
      IResultSetParser cxtObjectResult = currentConnection.getResultSetParser(cxtObjectQuery.executeQuery());
      while (cxtObjectResult.next()) {
        ContextualDataDTO contextualDataDTO = new ContextualDataDTO(cxtObjectResult);
        contextualData.put(contextualDataDTO.getIID(), contextualDataDTO);
      }
    }
    return contextualData;
  }





  @Override
  public PropertyRecordDTO getRecord()
  {
    return record;
  }
  
  /**
   * Create the value part of a new default value
   *
   * @throws SQLException
   * @throws RDBMSException
   */
  protected abstract void createNewDefaultRecord(NodeResolver.CouplingMaster master)
      throws SQLException, RDBMSException;
  
  /**
   * Create a separated coupled record with updated content
   *
   * @throws SQLException
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  protected abstract void updateAsTightlyCoupledRecord()
      throws SQLException, RDBMSException, CSFormatException;
  
  /**
   * Create a separated decoupled record with updated content
   *
   * @throws SQLException
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  protected abstract void updateAsDecoupledRecord()
      throws SQLException, RDBMSException, CSFormatException;
  
  @Override
  public void createCoupledRecord() throws SQLException, RDBMSException, CSFormatException
  {
    if (!record.isCoupled())
      return; 
    ICSECoupling couplingDef = record.getCouplingExpressions()
        .get(0);
    boolean defaultValueCase = false;
    NodeResolver.CouplingMaster master; 
    switch (record.getCouplingType()) {
      case DYN_INHERITANCE:
      case TIGHT_INHERITANCE: // then resolve the source node of coupling
        master = nodeResolver.getEntityCouplingMaster(couplingDef.getSource(), baseEntityIID,
            record.getProperty());

        break;
      case DYN_RELATIONSHIP:
        master = nodeResolver.getRelationCouplingMaster(couplingDef.getSource()
            .toRelation(), baseEntityIID, record.getProperty());

        driver.getProcedure(currentConnection, "pxp.sp_createConflictingValuesForDynamicCoupling")
        .setInput(ParameterType.LONG, baseEntityIID)
        .setInput(ParameterType.LONG, record.getProperty().getIID())
        .setInput(ParameterType.INT, record.getCouplingBehavior().ordinal())
        .setInput(ParameterType.INT, record.getCouplingType().ordinal())
        .setInput(ParameterType.STRING, (!master.getNodeID().isEmpty() ? master.getNodeID() : ""))
        .setInput(ParameterType.STRING, record.getCouplingExpression())
        .setInput(ParameterType.IID, master.getEntityIID())
        .setInput(ParameterType.IID, master.getPropertyIID())
        .setInput(ParameterType.IID, couplingDef.getSource().toRelation().getIID())
        .execute();
        break;
      case TIGHT_RELATIONSHIP: // then resolve the other side origin of the
                               // relationship property

        master = nodeResolver.getRelationCouplingMaster(couplingDef.getSource()
            .toRelation(), baseEntityIID, record.getProperty());
        if(!master.getNodeID().isEmpty()) {
          
        driver.getProcedure(currentConnection, "pxp.sp_createconflictingvaluesfortightlycoupling")
        .setInput(ParameterType.LONG, baseEntityIID)
        .setInput(ParameterType.LONG, record.getProperty().getIID())
        .setInput(ParameterType.INT, RecordStatus.NOTIFIED.ordinal())
        .setInput(ParameterType.INT, record.getCouplingBehavior().ordinal())
        .setInput(ParameterType.INT, record.getCouplingType().ordinal())
        .setInput(ParameterType.STRING, (!master.getNodeID().isEmpty() ? master.getNodeID() : null))
        .setInput(ParameterType.STRING, record.getCouplingExpression())
        .setInput(ParameterType.IID, master.getEntityIID())
        .setInput(ParameterType.IID, master.getPropertyIID())
        .setInput(ParameterType.IID, couplingDef.getSource().toRelation().getIID())
        .execute();
        }
        
        break;
      case DYN_CLASSIFICATION:
      case TIGHT_CLASSIFICATION: // then resolve the source classifier of coupling
        defaultValueCase = true;
        master = nodeResolver.getClassifierCouplingMaster(couplingDef.getSource(), baseEntityIID,
            record.getProperty());
        break;
      default:
        throw new RDBMSException(0, "Program ERROR", "Try to resolve a non-coupling case"); // should
                                                                                            // never
                                                                                            // happen
    }
    record.setMasterNodeID(master.getNodeID());
    if (record.getMasterNodeID()
        .isEmpty()) { // this means the source record doesn't exists => so the
                      // coupled record
      // creation is canceled
      return;
    }
    if (defaultValueCase) { // property record for default value is synchronized
                            // and linked
      createNewDefaultRecord(master); // create record content
    }
  }
  
  @Override
  public Set<String> getCouplingTargetNodeIDs(boolean excludeDynamicBehavior)
      throws SQLException, RDBMSException
  {
    if (!couplingTargetLoaded) {
      String queryStr = Q_COUPLING_TARGETS;
      PreparedStatement query = currentConnection.prepareStatement(queryStr);
      query.setString(1, record.getNodeID());
      IResultSetParser result = driver.getResultSetParser(query.executeQuery());
      while (result.next()) {
        couplingTargetNodeIDs.add(result.getString("nodeID"));
        targetCouplingBehavior.add(CouplingBehavior.valueOf(result.getInt("couplingBehavior")));
      }
      couplingTargetLoaded = true;
    }
    Set<String> targets = new HashSet<>();
    if (!excludeDynamicBehavior)
      targets.addAll(couplingTargetNodeIDs); // Without filtering
    else {
      for (int i = 0; i < targetCouplingBehavior.size(); i++) {
        if (targetCouplingBehavior.get(i) == CouplingBehavior.TIGHTLY
            || targetCouplingBehavior.get(i) == CouplingBehavior.INITIAL) {
          targets.add(couplingTargetNodeIDs.get(i));
        }
      }
    }
    return targets;
  }
  
  @Override
  public boolean isSourceOfCoupling() throws SQLException, RDBMSException
  {
    // Targets are already loaded and identified
    if (couplingTargetLoaded && !couplingTargetNodeIDs.isEmpty()) return true;
    return !getCouplingTargetNodeIDs(false).isEmpty();
  }
  
  
  @Override
  public boolean hasSeparableCouplingDependency() throws SQLException, RDBMSException
  {
    return !getCouplingTargetNodeIDs(true).isEmpty();
  }
  
  @Override
  public Set<String> getTargetNodesOfCalculation(boolean onlyDirect)
      throws SQLException, RDBMSException
  {
    Set<String> targetNodeIDs = new HashSet<>();
    int loadingCase = (onlyDirect ? 0 : 1);
    if (calculationLoadedCase != loadingCase) {
      String currNodeID = record.getNodeID();
      String cursorName = (onlyDirect ? "pxp.fn_getDirectDependencyTargetNodes"
          : "pxp.fn_getAllDependencyTargetNodes");
      IResultSetParser result = driver.getFunction(currentConnection, ResultType.CURSOR, cursorName)
          .setInput(ParameterType.STRING, currNodeID)
          .setInput(ParameterType.INT, GraphCase.RECORD_DEPENDENCY.ordinal())
          .execute();
      while (result.next()) {
        calculationTargetNodeIDs.add(result.getString("nodeID"));
      }
      calculationLoadedCase = loadingCase;
    }
    targetNodeIDs.addAll(calculationTargetNodeIDs);
    return targetNodeIDs;
  }
  
  @Override
  public boolean isSourceOfCalculation() throws SQLException, RDBMSException
  {
    Set<String> signatures = getTargetNodesOfCalculation(false);
    return !signatures.isEmpty();
  }
  
  /**
   * @param contextualObject
   *          contextual information to be updated
   * @throws SQLException
   * @throws RDBMSException
   */
  protected long updateContextualData(ContextualDataDTO contextualObject)
      throws SQLException, RDBMSException
  {
    String cxtTags = contextualObject.getHStoreFormat();
    IResultSetParser result = driver
        .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.IID, "pxp.sp_upsertRecordContextualData")
        .setInput(ParameterType.IID, contextualObject.getContextualObjectIID() == 0 ? null : contextualObject.getContextualObjectIID())
        .setInput(ParameterType.STRING, contextualObject.getContextCode())
        .setInput(ParameterType.LONG, contextualObject.getContextStartTime())
        .setInput(ParameterType.LONG, contextualObject.getContextEndTime())
        .setInput(ParameterType.STRING, cxtTags.isEmpty() ? null : cxtTags)
        .setInput(ParameterType.INT_ARRAY, contextualObject.getLinkedBaseEntityIIDs()).execute();
    return result.getLong();
  }

  @Override
  public void updateCoupledRecord() throws RDBMSException, SQLException, CSFormatException
  {
    if (!record.isCoupled())
      return;
    switch (record.getCouplingType()) {
      case DYN_INHERITANCE:
      case DYN_RELATIONSHIP: 
        RDBMSLogger.instance()
            .info("Discarding improper attempt to update a dynamically coupled record: "
                + record.toCSExpressID());
        return; // Do noting => dynamic records are updated through their source
                // of coupling
      case DYN_CLASSIFICATION:
      case TIGHT_INHERITANCE:
      case TIGHT_RELATIONSHIP:
        updateAsTightlyCoupledRecord(); // update to separate record while
                                        // keeping coupling info
        // intact
        break;
      case TIGHT_CLASSIFICATION: // update to separate record discarding coupling info
        updateAsDecoupledRecord();
        break;
        
      case TIGHT_CONTEXTUAL:
        updateAsTightlyCoupledRecord(); // update to separate record while
        break;
        
      case DYN_CONTEXTUAL:
        break;
        
      case LANG_INHERITANCE:
        updateAsTightlyCoupledRecord();
        break;
        
      default:
        throw new RDBMSException(0, "Program ERROR",
            "Handling not fould for coupling type " + record.getCouplingType()
                .name()); // should never happen
    }
  }
  
  @Override
  public void notifyCouplingChange(Set<String> targetSignatures) throws RDBMSException, SQLException
  {
  }
  
  /**
   * Create dependencies across records, regarding the current record
   *
   * @param recordDependecies
   * @throws SQLException
   * @throws RDBMSException
   */
  protected void createDependencies(Set<String> recordDependecies)
      throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_multiInsertGraph")
        .setInput(ParameterType.STRING, record.getNodeID())
        .setInput(ParameterType.STRING_ARRAY, recordDependecies)
        .setInput(ParameterType.INT, GraphCase.RECORD_DEPENDENCY.ordinal())
        .execute();
  }
  
  /**
   * Remove a node ID from the graph dependencies
   *
   * @param recordDependecies
   * @throws SQLException
   * @throws RDBMSException
   */
  protected void renewDependencies(Set<String> recordDependecies)
      throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_renewGraphDependencies")
        .setInput(ParameterType.STRING, record.getNodeID())
        .setInput(ParameterType.STRING_ARRAY, recordDependecies)
        .setInput(ParameterType.INT, GraphCase.RECORD_DEPENDENCY.ordinal())
        .execute();
  }
  
  @Override
  public boolean isClonedRecord() throws RDBMSException, SQLException
  {
    PreparedStatement prepareStatement = currentConnection.prepareStatement(Q_IS_CLONED_RECORD);
    prepareStatement.setLong(1, this.record.getIID());
    IResultSetParser resultSetParser = driver.getResultSetParser(prepareStatement.executeQuery());
    return resultSetParser.next() && resultSetParser.getLong(1) > 0;
  }
}
