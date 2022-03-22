package com.cs.core.bgprocess.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.data.Text;
import com.cs.core.dataintegration.dto.PXONExportPlanDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Data Access services to BGP and task information
 *
 * @author vallee
 */
public class BGProcessDAS {
  
  private static final String Q_SELECT_BGP_TASK_WITH_USER = 
          "select * from pxp.backgroundprocess b join pxp.userconfig u on b.userIID = u.userIID ";
  private static final String Q_WHERE_TASKS_BY_STATUS     = " where b.status in (?,?) ";
  
  /**
   * Retrieve all current BGP events with status RUNNING or INTERRUPTED /!\ used
   * at starting phase only INTERRUPTED means the job has been shutdown and
   * requires to be restarted RUNNING means the job has been abruptly terminated
   * and requires to be restarted
   *
   * @return the list of BGP tasks
   * @throws RDBMSException
   */
  public List<IBGProcessDTO> getInterruptedJobs() throws RDBMSException
  {
    RDBMSAbstractDriver txDriver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(txDriver.getTransactionManager());
    transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
    return transactionTemplate.execute(new TransactionCallback<List<IBGProcessDTO>>()
    {
      
      @Override
      public List<IBGProcessDTO> doInTransaction(TransactionStatus status)
      {
        List<IBGProcessDTO> bgProcesses = new ArrayList<>();
        try {
          RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
            PreparedStatement query = connection.prepareStatement(Q_SELECT_BGP_TASK_WITH_USER + Q_WHERE_TASKS_BY_STATUS);
            query.setInt(1, IBGProcessDTO.BGPStatus.RUNNING.ordinal());
            query.setInt(2, IBGProcessDTO.BGPStatus.INTERRUPTED.ordinal());
            IResultSetParser result = connection.getResultSetParser(query.executeQuery());
            while (result.next()) {
              bgProcesses.add(new BGProcessDTO(result));
            }
          });
        }
        catch (RDBMSException e) {
          RDBMSLogger.instance().exception(e);
        }
        return bgProcesses;
      }
    });
    
  }
  
  private static final String Q_WHERE_TASKS_BY_SERVICE =
          " where b.service = ? and b.status = ? order by b.userpriority desc, b.created asc";
  
  /**
   * Retrieve the oldest BGP events with status PENDING or INTERRUPTED in a
   * service
   *
   * @param service
   *          specification of the service name
   * @return the found DTO or an empty DTO
   * @throws RDBMSException
   */
  public IBGProcessDTO getFirstPendingJob(String service) throws RDBMSException
  {
    BGProcessDTO[] bgProcess = { null };
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
          PreparedStatement query = connection.prepareStatement(Q_SELECT_BGP_TASK_WITH_USER + Q_WHERE_TASKS_BY_SERVICE);
          query.setString(1, service);
          query.setInt(2, IBGProcessDTO.BGPStatus.PENDING.ordinal());
          IResultSetParser result = connection.getResultSetParser(query.executeQuery());
          if (result.next()) {
            bgProcess[0] = new BGProcessDTO(result);
          }
        });
    return bgProcess[0];
  }
  
  /**
   * update run-time information of a BGP event
   *
   * @param jobIID
   *          the iid of the bgp
   * @param status
   *          the BGP status code
   * @param runTimeJSONString
   *          new JSON content for run time data
   * @param progressJSONString
   *          new JSON content for progress data
   * @param summaryDataJSONString
   *          Job summary Data
   * @param logDataJSONString
   *          Job log data
   * @param runningTime
   *          current running time of the job
   * @param startTime
   *          start time for first creation
   * @param endTime
   *          new end time when ended
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void updateBGProcessData(long jobIID, int status, String runTimeJSONString,
      String progressJSONString, String summaryDataJSONString, byte[] logDataJSONString,
      long runningTime, long startTime, long endTime) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
          connection.getProcedure( "pxp.sp_updateBackgroundprocess")
              .setInput(ParameterType.LONG, jobIID)
              .setInput(ParameterType.INT, status)
              .setInput(ParameterType.JSON, runTimeJSONString)
              .setInput(ParameterType.JSON, progressJSONString)
              .setInput(ParameterType.JSON, summaryDataJSONString)
              .setInput(ParameterType.BLOB, logDataJSONString)
              .setInput(ParameterType.LONG, runningTime)
              .setInput(ParameterType.LONG, startTime)
              .setInput(ParameterType.LONG, endTime)
              .execute();
        });
  }
  
  /**
   * Post a new pending task
   *
   * @param userIID
   *          current userIID
   * @param service
   *          the name of the service to be implemented
   * @param callBackURI
   * @param userPriority
   * @param entryDataJSONString
   * @return jobIID of new bgp task
   * @throws RDBMSException
   *           com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public Long postNewPendingTask(Long userIID, String service, String callBackURI, int userPriority,
      String entryDataJSONString) throws RDBMSException
  {
    Long[] jobIID = new Long[1];
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          IResultSetParser result = currentConn.getFunction( ResultType.IID, "pxp.fn_submitBackgroundprocess")
              .setInput(ParameterType.IID, userIID)
              .setInput(ParameterType.STRING, service)
              .setInput(ParameterType.STRING, callBackURI)
              .setInput(ParameterType.INT, userPriority)
              .setInput(ParameterType.JSON, entryDataJSONString)
              .setInput(ParameterType.LONG, System.currentTimeMillis())
              .execute();
          jobIID[0] = result.getIID();
        });
    return jobIID[0];
  }
  
  private static final String Q_WHERE_TASKS_BY_JOBIID = " where jobIID = ? ";
  
  /**
   * @param jobIID
   *          the iid of the bgp to retrieve
   * @return the found DTO or an empty DTO
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public BGProcessDTO getBGPProcess(long jobIID) throws RDBMSException
  {
    BGProcessDTO[] bgProcess = { null };
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
          PreparedStatement query = connection.prepareStatement(Q_SELECT_BGP_TASK_WITH_USER + Q_WHERE_TASKS_BY_JOBIID);
          query.setLong(1, jobIID);
          IResultSetParser result = connection.getResultSetParser(query.executeQuery());
          while (result.next()) {
            bgProcess[0] = new BGProcessDTO(result);
          }
        });
    return bgProcess[0];
  }
  
  private static final String Q_UPDATE_STATUS_BY_JOBIID = "UPDATE pxp.backgroundprocess SET status = ? where jobIID = ? ";
  
  /**
   * @param jobIID
   * @param status
   * @throws RDBMSException
   */
  public void updateStatus(Long jobIID, int status) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement query = currentConn.prepareStatement(Q_UPDATE_STATUS_BY_JOBIID);
          query.setInt(1, status);
          query.setLong(2, jobIID);
        });
  }
  
  private void appendStringParam(StringBuffer propertyIDsQuery, String property)
  {
    propertyIDsQuery.append("'")
        .append(property)
        .append("'")
        .append(",");
  }
  
  private static final String Q_BASE_ENTITY_IIDs    = "select distinct baseEntityIID from pxp.baseEntity where ";
  private static final String WHERE_BASE_ENTITY_IDs = " baseEntityID in (?) ";
  
  public Set<Long> getAllEntityIIDs(Collection<String> entityIDs) throws RDBMSException
  {
    Set<Long> baseEntityIIDs = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          String allEntityIIDQuery = getAllEntityIIDQuery(entityIDs);
          allEntityIIDQuery = Q_BASE_ENTITY_IIDs + allEntityIIDQuery;
          PreparedStatement prepareStmt = currentConn.prepareStatement(allEntityIIDQuery);
          ResultSet executeQuery = prepareStmt.executeQuery();
          while (executeQuery.next()) {
            baseEntityIIDs.add(executeQuery.getLong("baseEntityIID"));
          }
        });
    return baseEntityIIDs;
  }
  
  private String getAllEntityIIDQuery(Collection<String> entityIDs)
  {
    StringBuffer getBaseEntityIIDQuery = new StringBuffer();
    for (String id : entityIDs) {
      appendStringParam(getBaseEntityIIDQuery, id);
    }
    return WHERE_BASE_ENTITY_IDs.replace("?",
        getBaseEntityIIDQuery.deleteCharAt(getBaseEntityIIDQuery.length() - 1));
  }
  
  private static final String QUERY_GET_ALL_PROPERTY_IIDs = "Select distinct propertyIID from pxp.propertyConfig where ";
  private static final String WHERE_PROPERTY_CODEs        = "  propertycode in (?) ";
  
  public Set<Long> getAllBaseEntityPropertyIID(Collection<IPropertyDTO> baseEntityProperties)
      throws RDBMSException
  {
    Set<Long> propertyIIDs = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          String prepareGetAllPropertyQuery = new String();
          if (!baseEntityProperties.iterator()
              .next()
              .getPropertyCode()
              .isEmpty()) {
            prepareGetAllPropertyQuery = getAllPropertyIIDByCodesQuery(baseEntityProperties);
          }
          PreparedStatement prepareStmt = currentConn
              .prepareStatement(QUERY_GET_ALL_PROPERTY_IIDs + prepareGetAllPropertyQuery);
          ResultSet executeQuery = prepareStmt.executeQuery();
          while (executeQuery.next()) {
            propertyIIDs.add(executeQuery.getLong("propertyIID"));
          }
        });
    return propertyIIDs;
  }
  
  private String getAllPropertyIIDByCodesQuery(Collection<IPropertyDTO> baseEntityProperties)
  {
    StringBuffer propertyIDsQuery = new StringBuffer();
    for (IPropertyDTO property : baseEntityProperties) {
      appendStringParam(propertyIDsQuery, property.getPropertyCode());
    }
    return WHERE_PROPERTY_CODEs.replace("?",
        propertyIDsQuery.deleteCharAt(propertyIDsQuery.length() - 1));
  }
  
  private static final String QUERY_GET_CLASSIFIER_CODESs   = "select classifiercode, classifiertype as type from pxp.classifierconfig where ";
  private static final String WHERE_CLASSIFIER_IIDs         = "  classifieriid in (?) ";
  private static final String WHERE_CLASSIFIER_CODEs        = "  classifiercode in (?) ";
  private static final String WHERE_CLASSFIER_CODES_BY_TYPE = " classifierType in (?) ";
  
  private void getClassifierCodes(Set<String> codes, RDBMSConnection currentConn, String query)
      throws RDBMSException, SQLException
  {
    PreparedStatement prepareStmt = currentConn.prepareStatement(query);
    ResultSet executeQuery = prepareStmt.executeQuery();
    while (executeQuery.next()) {
      String code = executeQuery.getString("classifiercode");
      ClassifierType type = ClassifierType.valueOf(executeQuery.getInt("type"));
      codes.add(code + ":" + type);
    }
  }
  
  public Set<String> getAllTopHierarchiesCode(Collection<IClassifierDTO> topHierarchies)
      throws RDBMSException
  {
    Set<String> topHierarchiesCode = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          String prepareGetAllTopHierarchieQuery = new String();
          if (topHierarchies.size() > 0 && topHierarchies.iterator().next().getClassifierIID() != 0L) {
            prepareGetAllTopHierarchieQuery = getAllClassifierCodesByIIDsQuery(topHierarchies);
          }
          if (!prepareGetAllTopHierarchieQuery.isEmpty())
            getClassifierCodes(topHierarchiesCode, currentConn,
                QUERY_GET_CLASSIFIER_CODESs + prepareGetAllTopHierarchieQuery);
        });
    return topHierarchiesCode;
  }
  
  private String getAllClassifierCodesByIIDsQuery(Collection<IClassifierDTO> topHierarchies)
  {
    StringBuffer topHierarchiesIIDsQuery = new StringBuffer();
    for (IClassifierDTO classifier : topHierarchies) {
      topHierarchiesIIDsQuery.append(classifier.getClassifierIID())
          .append(",");
    }
    return WHERE_CLASSIFIER_IIDs.replace("?",
        topHierarchiesIIDsQuery.deleteCharAt(topHierarchiesIIDsQuery.length() - 1));
  }
  
  public Set<String> getClassifierCodesByType(Set<ClassifierType> classifierTypes)
      throws RDBMSException
  {
    Set<String> classifierCodes = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer getAllClassifierCodesQuery = new StringBuffer();
          for (ClassifierType classifierType : classifierTypes) {
            getAllClassifierCodesQuery.append(classifierType.ordinal())
                .append(",");
          }
          String query = WHERE_CLASSFIER_CODES_BY_TYPE.replace("?",
              getAllClassifierCodesQuery.deleteCharAt(getAllClassifierCodesQuery.length() - 1));
          getClassifierCodes(classifierCodes, currentConn, QUERY_GET_CLASSIFIER_CODESs + query);
        });
    return classifierCodes;
  }
  
  public Set<String> getClassifierCodesByIID(Set<Long> itemIIDs) throws RDBMSException
  {
    Set<String> classifierCodes = new TreeSet<String>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer getAllClassifierCodesQuery = new StringBuffer();
          getIIDQuery(itemIIDs, getAllClassifierCodesQuery);
          String query = WHERE_CLASSIFIER_CODEs.replace("?",
              getAllClassifierCodesQuery.deleteCharAt(getAllClassifierCodesQuery.length() - 1));
          getClassifierCodes(classifierCodes, currentConn, QUERY_GET_CLASSIFIER_CODESs + query);
        });
    return classifierCodes;
  }
  
  public Set<String> getClassifierInfoByCodes(Set<String> itemCodes) throws RDBMSException
  {
    Set<String> classifierCodes = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer getAllClassifierCodesQuery = new StringBuffer();
          getIDQuery(itemCodes, getAllClassifierCodesQuery);
          String query = WHERE_CLASSIFIER_CODEs.replace("?",
              getAllClassifierCodesQuery.deleteCharAt(getAllClassifierCodesQuery.length() - 1));
          getClassifierCodes(classifierCodes, currentConn, QUERY_GET_CLASSIFIER_CODESs + query);
        });
    return classifierCodes;
  }
  
  private static final String QUERY_GET_PROPERTY_CODES     = "select propertyCode, propertytype as type from pxp.propertyconfig where  ";
  private static final String WHERE_PROPERTY_CODES_BY_TYPE = " propertyType in (?) ";
  
  public Set<String> getPropertyCodesByType(Set<PropertyType> propertyTypes) throws RDBMSException
  {
    Set<String> propertyCodes = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer propertyCodeQuery = new StringBuffer();
          for (IPropertyDTO.PropertyType propertyType : propertyTypes) {
            propertyCodeQuery.append(propertyType
                .ordinal())
                .append(",");
          }
          String query = WHERE_PROPERTY_CODES_BY_TYPE.replace("?",
              propertyCodeQuery.deleteCharAt(propertyCodeQuery.length() - 1));
          getPropertyCodes(propertyCodes, currentConn, QUERY_GET_PROPERTY_CODES + query);
        });
    return propertyCodes;
  }
  
  private static final String WHERE_PROPERTY_CODES_BY_ID = " propertyId in (?) ";
  
  public Set<String> getPropertyCodesByID(Set<String> itemIDs) throws RDBMSException
  {
    Set<String> propertyCodes = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer propertyCodeQuery = new StringBuffer();
          getIDQuery(itemIDs, propertyCodeQuery);
          String query = WHERE_PROPERTY_CODES_BY_ID.replace("?",
              propertyCodeQuery.deleteCharAt(propertyCodeQuery.length() - 1));
          getPropertyCodes(propertyCodes, currentConn, QUERY_GET_PROPERTY_CODES + query);
        });
    return propertyCodes;
  }
  
  private static final String WHERE_PROPERTY_CODES_BY_IID = " propertyId in (?) ";
  
  public Set<String> getPropertyCodesByIID(Set<Long> itemIIDs) throws RDBMSException
  {
    Set<String> propertyCodes = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer propertyCodeQuery = new StringBuffer();
          getIIDQuery(itemIIDs, propertyCodeQuery);
          String query = WHERE_PROPERTY_CODES_BY_IID.replace("?",
              propertyCodeQuery.deleteCharAt(propertyCodeQuery.length() - 1));
          getPropertyCodes(propertyCodes, currentConn, QUERY_GET_PROPERTY_CODES + query);
        });
    return propertyCodes;
  }
  
  private static final String WHERE_PROPERTY_CODESINFO_BY_CODES = " propertyCode in (?) ";
  
  public Set<String> getPropertyInfoByCodes(Set<String> itemCodes) throws RDBMSException
  {
    Set<String> propertyCodes = new TreeSet<String>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer propertyCodeQuery = new StringBuffer();
          getIDQuery(itemCodes, propertyCodeQuery);
          String query = WHERE_PROPERTY_CODESINFO_BY_CODES.replace("?",
              propertyCodeQuery.deleteCharAt(propertyCodeQuery.length() - 1));
          getPropertyCodes(propertyCodes, currentConn, QUERY_GET_PROPERTY_CODES + query);
        });
    return propertyCodes;
  }
  
  private void getPropertyCodes(Set<String> codes, RDBMSConnection currentConn, String query)
      throws RDBMSException, SQLException
  {
    PreparedStatement prepareStmt = currentConn.prepareStatement(query);
    ResultSet executeQuery = prepareStmt.executeQuery();
    while (executeQuery.next()) {
      String code = executeQuery.getString("propertyCode");
      PropertyType type = PropertyType.valueOf(executeQuery.getInt("type"));
      codes.add(code + ":" + type);
    }
  }
  
  private void getIDQuery(Set<String> itemIDs, StringBuffer getAllCodesQuery)
  {
    for (String itemID : itemIDs) {
      appendStringParam(getAllCodesQuery, itemID);
    }
  }
  
  private void getIIDQuery(Set<Long> itemIIDs, StringBuffer getAllCodesQuery)
  {
    for (Long itemID : itemIIDs) {
      getAllCodesQuery.append(itemID)
          .append(",");
    }
  }
  
  private static final String QUERY_GET_CONTEXT_CODES = "select contextCode, contexttype as type from pxp.contextConfig where ";
  private static final String WHERE_CONTEXT_IIDs      = "  contextIID in (?) ";
  private static final String WHERE_CONTEXT_CODES     = "  contextCode in (?) ";
  
  public Set<String> getContextCodesByIID(Set<Long> itemIIDs) throws RDBMSException
  {
    Set<String> contextCodes = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer getAllcontextCodesQuery = new StringBuffer();
          getIIDQuery(itemIIDs, getAllcontextCodesQuery);
          String query = WHERE_CONTEXT_IIDs.replace("?",
              getAllcontextCodesQuery.deleteCharAt(getAllcontextCodesQuery.length() - 1));
          getContextCodes(contextCodes, currentConn, QUERY_GET_CONTEXT_CODES + query);
        });
    return contextCodes;
  }
  
  private void getContextCodes(Set<String> codes, RDBMSConnection currentConn, String query)
      throws RDBMSException, SQLException
  {
    PreparedStatement prepareStmt = currentConn.prepareStatement(query);
    ResultSet executeQuery = prepareStmt.executeQuery();
    while (executeQuery.next()) {
      String code = executeQuery.getString("contextCode");
      ContextType type = ContextType.valueOf(executeQuery.getInt("type"));
      codes.add(code + ":" + type);
    }
  }
  
  public Set<String> getContextInfoByCodes(Set<String> itemCodes) throws RDBMSException
  {
    Set<String> contextCodes = new TreeSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuffer getAllcontextCodesQuery = new StringBuffer();
          getIDQuery(itemCodes, getAllcontextCodesQuery);
          String query = WHERE_CONTEXT_CODES.replace("?",
              getAllcontextCodesQuery.deleteCharAt(getAllcontextCodesQuery.length() - 1));
          getContextCodes(contextCodes, currentConn, QUERY_GET_CONTEXT_CODES + query);
        });
    return contextCodes;
  }
  
  private static final String QUERY_GET_BASEENTITYIIDS_BY_KLASS_TYPES = "select distinct baseentityiid from pxp.baseentity "
      + "where catalogcode = %s and basetype in (%s) and baselocaleid = %s and organizationcode = %s and ismerged != true and classifieriid in "
      + "(select distinct classifieriid from pxp.classifierconfig where classifiercode in (%s))";
  private static final String QUERY_GET_BASEENTITYIIDS_BY_TAXONOMY = "select distinct baseentityiid from pxp.baseentity "
      + "where catalogcode = %s and basetype in (%s) and baselocaleid = %s and organizationcode = %s and ismerged != true and baseentityiid in "
      + "(select distinct baseentityiid from pxp.baseEntityclassifierlink "
      + "where otherclassifieriid in (select distinct classifieriid from pxp.classifierconfig where hierarchyiids && "
      + "array(select distinct classifieriid from pxp.classifierconfig where classifiercode in (%s))))";
  private static final String QUERY_GET_BASEENTITYIIDS_WITHOUT_TAXONOMY = "select distinct baseentityiid from pxp.baseentity "
      + "where catalogcode = %s and basetype in (%s) and baselocaleid = %s and organizationcode = %s and ismerged != true and baseentityiid not in "
      + "(select distinct baseentityiid from pxp.baseEntityclassifierlink)";
 private static final String QUERY_GET_BASEENTITYIIDS_OF_COLLECTION_WITH_CHILD = "select distinct baseentityiid from pxp.baseentity where "
      + " organizationcode = %s and basetype in (%s) and ismerged != true and baseentityiid in (select distinct baseentityiid from pxp.collectionbaseentitylink "
      + "where collectioniid in (select distinct collectioniid from pxp.collection where parentiid = %s or collectioniid = %s))";
  private static final String QUERY_GET_BASEENTITYIIDS_OF_COLLECTION_WITHOUT_CHILD = "select distinct baseentityiid from pxp.baseentity where "
      + " organizationcode = %s and basetype in (%s) and ismerged != true and baseentityiid in (select distinct baseentityiid from pxp.collectionbaseentitylink where collectioniid = %s)";
  
  /**
   * Get baseentityiids from given search criteria
   * 
   * @param pxonExportPlanDTO
   * @return
   * @throws RDBMSException
   */
  public Set<Long> getBaseEntityIIdsByQuery(PXONExportPlanDTO pxonExportPlanDTO) throws RDBMSException
  {
    Set<Long> baseentityiids = new HashSet<>();
    Map<String, Object> searchCriteria = pxonExportPlanDTO.getSearchCriteria();
    List<String> selectedBaseTypes = (List<String>) pxonExportPlanDTO.getSearchCriteria().get("selectedBaseTypes");
    List<Integer> basetypes = new ArrayList<>();
    selectedBaseTypes.forEach(moduleId -> basetypes.add(IBaseEntityIDDTO.BaseType.valueOf(moduleId.toString()).ordinal()));
    String query = null;
    String catalogCode = "'" + pxonExportPlanDTO.getCatalogCode() + "'";
    String languageCode = "'" + pxonExportPlanDTO.getLocaleID() + "'";
    String organizationcode = "'" + pxonExportPlanDTO.getOrganizationCode() + "'";
    String collectionId = (String) searchCriteria.get("collectionId");
    if (searchCriteria != null && !searchCriteria.isEmpty()) {
      String exportSubType = (String) searchCriteria.get("exportSubType");
      switch (exportSubType) {
        
        case "TAXONOMY_BASED_EXPORT":
          List<String> selectedTypeIdsList = (List<String>) searchCriteria.get("selectedTypes");
          List<String> selectedTaxonomyIdList = (List<String>) searchCriteria.get("selectedTaxonomyIds");
          if (!selectedTypeIdsList.isEmpty()) {
            StringBuffer selectedTypes = new StringBuffer();
            selectedTypeIdsList.forEach(type -> appendStringParam(selectedTypes, type));
            selectedTypes.deleteCharAt(selectedTypes.length() - 1);
            query = String.format(QUERY_GET_BASEENTITYIIDS_BY_KLASS_TYPES, catalogCode, Text.join(",", basetypes), languageCode, organizationcode,
                selectedTypes);
          }
          if (!selectedTaxonomyIdList.isEmpty()) {
            StringBuffer selectedTaxonomyIds = new StringBuffer();
            selectedTaxonomyIdList.forEach(type -> appendStringParam(selectedTaxonomyIds, type));
            selectedTaxonomyIds.deleteCharAt(selectedTaxonomyIds.length() - 1);
            query = String.format(QUERY_GET_BASEENTITYIIDS_BY_TAXONOMY, catalogCode, Text.join(",", basetypes), languageCode, organizationcode,
                selectedTaxonomyIds);
          }
          break;
        
        case "WITHOUT_TAXONOMY_EXPORT":
          query = String.format(QUERY_GET_BASEENTITYIIDS_WITHOUT_TAXONOMY, catalogCode, Text.join(",", basetypes), languageCode, organizationcode);
          break;
        case "COLLECTION_EXPORT_WITH_CHILD":
          query = String.format(QUERY_GET_BASEENTITYIIDS_OF_COLLECTION_WITH_CHILD, organizationcode, Text.join(",", basetypes), collectionId, collectionId);
          break;
        case "COLLECTION_EXPORT_WITHOUT_CHILD":
          query = String.format(QUERY_GET_BASEENTITYIIDS_OF_COLLECTION_WITHOUT_CHILD,  organizationcode, Text.join(",", basetypes), collectionId);
          break;
        
        default:
          break;
      }
      baseentityiids.addAll(getBaseEntityIIds(query));
    }
    return baseentityiids;
  }
  
  /**
   * Execute given query to fetch baseentityiids
   * 
   * @param query
   * @return
   * @throws RDBMSException
   */
  public Set<Long> getBaseEntityIIds(String query) throws RDBMSException
  {
    Set<Long> baseentityiids = new HashSet<>();
    if (query != null) {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement prepareStmt = currentConn.prepareStatement(query);
        ResultSet executeQuery = prepareStmt.executeQuery();
        while (executeQuery.next()) {
          baseentityiids.add(Long.parseLong(executeQuery.getString("baseentityiid")));
        }
      });
    }
    return baseentityiids;
  }
}
