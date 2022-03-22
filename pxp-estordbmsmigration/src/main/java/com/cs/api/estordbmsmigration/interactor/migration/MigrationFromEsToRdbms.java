package com.cs.api.estordbmsmigration.interactor.migration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.cs.api.estordbmsmigration.model.migration.IMigrationFromEsToRdbmsRequestModel;
import com.cs.api.estordbmsmigration.services.MigrationProperties;
import com.cs.api.estordbmsmigration.services.RequestHandler;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.data.Text;
import com.cs.core.elastic.services.ScrollUtils;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
@SuppressWarnings("unchecked")
public class MigrationFromEsToRdbms extends AbstractRuntimeService<IMigrationFromEsToRdbmsRequestModel, IVoidModel> implements IMigrationFromEsToRdbms {
  
  private static final String          REFERENCE_COUNT                  = "referenceCount";
  
  private static final String          RELATIONSHIP_COUNT               = "relationshipCount";
  
  private static final String          ENTITY_COUNT                     = "entityCount";
  
  private static final String          TASK_COUNT                     = "taskCount";

  public static final String          PARENT_VERSION_ID          = "parentVersionId";
  
  public static final String          INSERT_RELATIONSHIP_RECORD = "INSERT INTO staging.relationships ("
      + IRelationshipInstance.COMMON_RELATIONSHIP_INSTANCE_ID + ", " + "contextiid" + ", "
      + IRelationshipInstance.ORIGINAL_INSTANCE_ID + ", " + IRelationshipInstance.RELATIONSHIP_ID + ", "
      + IRelationshipInstance.RELATIONSHIP_OBJECT_ID + ", " + IRelationshipInstance.SIDE1_BASE_TYPE + ", "
      + IRelationshipInstance.SIDE1_INSTANCE_ID + ", " + IRelationshipInstance.SIDE1_INSTANCE_VERSION_ID + ", "
      + IRelationshipInstance.SIDE2_BASE_TYPE + ", " + IRelationshipInstance.SIDE2_INSTANCE_ID + ", "
      + IRelationshipInstance.SIDE2_INSTANCE_VERSION_ID + ", " + IRelationshipInstance.SIDE_ID + ", "
      + IRelationshipInstance.VERSION_TIMESTAMP + ", " + IRelationshipInstance.TAGS + ", " + IRelationshipInstance.COUNT + ", "
      + IRelationshipInstance.ID + ", " + IRelationshipInstance.VARIANT1_INSTANCE_ID + ", " + IRelationshipInstance.VARIANT2_INSTANCE_ID
      + ", " + CommonConstants.PARENT_ID_PROPERTY + ", " + CommonConstants.VERSION_ID + ", " + PARENT_VERSION_ID + ")"
      + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?::JSON,?,?,?,?,?,?,?) ";
  
  public static final String          INSERT_ENTITY_QUERY        = "INSERT INTO staging.baseentity "
      + "(baseType,id,branchOf,contextiid,createdBy,createdOn,creationLanguage,defaultAssetInstanceId,endpointId,hiddenSummary,isEmbedded,isMerged,jobId,klassInstanceId,languageCodes,languageInstances,lastModified,lastModifiedBy,logicalCatalogId,messages,name,organizationId,originalInstanceId,owner,parentId,partnerSources,path,physicalCatalogId,referenceConflictingValues,relationshipConflictingValues,roles,ruleViolation,saveComment,selectedTaxonomyIds,summary,systemId,taxonomyConflictingValues,taxonomyIds,types,variantId,variants,versionId,versionOf,versionTimestamp,attributeVariants,assetInformation, copyOf)"
      + "VALUES (?,?,?,?,?,?,?,?,?,?::JSON,?,?,?,?,?,?::JSON,?,?,?,?::JSON,?,?,?,?,?,?::JSON,?,?,?::JSON,?::JSON,?::JSON,?::JSON,?,?,?::JSON,?,?::JSON,?,?,?,?,?,?,?,?::JSON,?::JSON,?) ";
  
  public static final String          INSERT_VALUE_RECORD_QUERY  = "INSERT INTO staging.valuerecord "
      + "(attributeId,baseType,code,contextiid,createdBy,createdOn,duplicateStatus,id,isConflictResolved,isMandatoryViolated,isMatchAndMerge,isShouldViolated,isUnique,jobId,klassInstanceId,klassInstanceVersion,language,lastModified,lastModifiedBy,notification,originalInstanceId,saveComment,tags,value,valueAsExpression,valueAsHtml,valueAsNumber,variantInstanceId,versionId,versionTimestamp,unitsymbol,calculation)"
      + " VALUES (?,?,?,?,?,?,?::JSON,?,?,?,?,?,?,?,?,?,?,?,?,?::JSON,?,?,?::JSON,?,?::JSON,?,?,?,?,?,?,?)";
  
  public static final String          INSERT_TAG_RECORDS_QUERY   = "INSERT INTO staging.tagrecord"
      + "(baseType,contextInstanceId,createdBy,createdOn,id,isConflictResolved,isMandatoryViolated,isMatchAndMerge,isShouldViolated,jobid,klassInstanceId,lastModified,lastModifiedBy,notification,tagId,tagValues,variantInstanceId,versionId,versionTimestamp)"
      + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?::JSON,?,?,?,?,?) ";
  
  public static final String          INSERT_CONTEXT_QUERY       = "INSERT INTO staging.context"
      + "(id, contextId, lastModifiedBy, linkedInstances, tagInstanceIds, timeRange, versionId, versionTimestamp)"
      + "values (?,?,?,?::JSON,?,?::JSON,?,? ) "
      + "RETURNING contextiid";
  
  public static final String          INSERT_REFERENCE_RECORD    = "INSERT INTO staging.references (" + IRelationshipInstance.ID
      + ", " + IRelationshipInstance.ORIGINAL_INSTANCE_ID + ", " + IRelationshipInstance.SIDE1_INSTANCE_ID + ", "
      + IRelationshipInstance.SIDE2_INSTANCE_ID + ", " + CommonConstants.REFERENCE_ID_PROPERTY + ") " + "VALUES (?,?,?,?,?) ";
  
  public static final String          INSERT_CONFLICTING_VALUES_RECORD = "INSERT INTO staging.attributeconflictingvalues "
      + "(id,attributeId,couplingType,source,klassInstanceId,isMandatory,isShould,value,language,valueAsHtml,versionTimestamp,lastModifiedBy,versionId) " 
      + "VALUES (?,?,?,?::JSON,?,?,?,?,?,?,?,?,?)";
  
  public static final String          INSERT_CONFLICTING_TAGS_RECORD = "INSERT INTO staging.tagconflictingvalues "
      + "(id,tagId,couplingType,source,klassInstanceId,tagValues,isMandatory,isShould,versionTimestamp,lastModifiedBy,versionId) " 
      + "VALUES (?,?,?,?::JSON,?,?,?,?,?,?,?)";
  
  public static final String           INSERT_TASK_QUERY                = "INSERT INTO staging.task(id, type, name, "
      + "description, createdtime, startdate, duedate, overduedate, wfcreated, wfprocessid, "
      + "wftaskinstanceid, wfprocessinstanceid, attachments, position, comments, "
      + "parenttaskid, priority, contentId, status, propertyid) VALUES (?, ?, ?, ?, ?, ?, ?, "
      + "?, ?, ?, ?, ?, ?, ?::JSON, ?::JSON, ?, ?, ?, ?, ?)";
  
  public static final String           INSERT_TASK_USER_LINK_QUERY      = "INSERT INTO staging.taskuserlink(taskid,"
      + "userid, racivs) VALUES (?, ?, ?)";
  
  public static final String           INSERT_TASK_ROLE_LINK_QUERY      = "INSERT INTO staging.taskrolelink(taskid,"
      + "roleid, racivs) VALUES (?, ?, ?)";
  
  protected static final String        GET_USER_ID_BY_NAME              = "select userid from staging.helper_userconfig where username = ?";
  
  protected static final String        GET_PROPERTY_CODE              = "select code from staging.valuerecord where id = ?";
  
  protected static final List<String> docTypes                   = Arrays.asList("klassinstancecache", "assetinstancecache", "targetinstancemarketcache",
      "virtualcataloginstancecache", "textassetinstancecache", "supplierinstancecache", "attachmentinstancecache");
  
  protected static final String       separator                  = "__";
 
  protected static Long               batchSize                  = 20L;
  
  protected static Map<String, Object> attributeConfigDetails             = new HashMap<String, Object>();
  
  
  @Override
  public IVoidModel executeInternal(IMigrationFromEsToRdbmsRequestModel dataModel) throws Exception
  {
    batchSize = MigrationProperties.instance().getLong("batchsize");
    Boolean shouldMigrateArchive = dataModel.getShouldMigrateArchive();
    String index = shouldMigrateArchive ? "csarchive" : "cs";
    
    Map<String, Long> batchCount = getSuccessfulBatchCount(index);
    try {
      migrateEntityData(index, batchCount.get(ENTITY_COUNT));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      migrateRelationshipData(index, batchCount.get(RELATIONSHIP_COUNT));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      migrateReferenceData(index, batchCount.get(REFERENCE_COUNT));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      migrateTaskData(index, batchCount.get(TASK_COUNT));
      prepareInprogressTaskInatanceList();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    
    return null;
  }
  
  /**
   * Store Reference data into RDBMS staging database.
   * @param batchCount 
   * @throws Exception 
   * @throws CSInitializationException 
   * @throws IOException 
   */
  void migrateReferenceData(String index, Long batchCount) throws Exception
  {
    List<String> referenceDocTypes = new ArrayList<>();
    referenceDocTypes.add("referenceinstancecache");
    
    try {
      ScrollUtils.scrollThrough(this::referenceFunctionalityInScroll, RequestHandler.getRestClient(), Arrays.asList(index),
          QueryBuilders.matchAllQuery(), referenceDocTypes, batchCount, batchSize.intValue());
      batchCount++;
    }
    catch (Exception e) {
      updateBatchCount("referencebatchcount", batchCount, index);
      RDBMSLogger.instance().warn("Error while migrating Reference Data : " + e.getMessage());
      throw e;
    }
    updateBatchCount("referencebatchcount", batchCount, index);
  }
  
  private String referenceFunctionalityInScroll(SearchHit[] searchHits)
  {
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_REFERENCE_RECORD);
        for (SearchHit hit : searchHits) {
          Map<String, Object> referenceInstance = hit.getSourceAsMap();
          Object id = referenceInstance.get(IRelationshipInstance.ID);
          preparedStatement.setString(1, (String) id);
          preparedStatement.setString(2, (String) referenceInstance.get(IRelationshipInstance.ORIGINAL_INSTANCE_ID));
          preparedStatement.setString(3, (String) referenceInstance.get(IRelationshipInstance.SIDE1_INSTANCE_ID));
          preparedStatement.setString(4, (String) referenceInstance.get(IRelationshipInstance.SIDE2_INSTANCE_ID));
          preparedStatement.setString(5, (String) referenceInstance.get(CommonConstants.REFERENCE_ID_PROPERTY));
          preparedStatement.addBatch();
          RDBMSLogger.instance().info("Reference Id : "+ id + "inserted in batch.");
        }
        preparedStatement.executeBatch();
        currentConn.commit();
        RDBMSLogger.instance().info("Batch executed successfully.");
      });
    }
    catch (Exception e) {
      RDBMSLogger.instance().warn("Reference batch insert failed ::" + e.getMessage());
    }
  
    return "success";
  }
  
  /**
   * Store Relationship data into RDBMS staging database.
   * @param batchCount 
   * @throws Exception 
   */
  void migrateRelationshipData(String index, Long batchCount) throws Exception
  {
    List<String> relationshipDocTypes = new ArrayList<>();
    relationshipDocTypes.add("relationshipinstancecache");
    relationshipDocTypes.add("naturerelationshipinstancecache");
    
    try {
      ScrollUtils.scrollThrough(this::relationshipFunctionalityInScroll, RequestHandler.getRestClient(),
          Arrays.asList(index), QueryBuilders.matchAllQuery(), relationshipDocTypes, batchCount, batchSize.intValue());
      batchCount++;
    }
    
    catch (Exception e) {
      updateBatchCount("relationshipbatchcount", batchCount, index);
      RDBMSLogger.instance().warn("Error while migrating Relationship Data : " + e.getMessage());
      throw e;
    }
    
    updateBatchCount("relationshipbatchcount", batchCount, index);
  }
  
  private String relationshipFunctionalityInScroll(SearchHit[] searchHits) 
  {
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_RELATIONSHIP_RECORD);
        for (SearchHit hit : searchHits) {
          
          String type = hit.getType();
          try {
            
            Map<String, Object> relationshipInstance = hit.getSourceAsMap();
            Object id = relationshipInstance.get(IRelationshipInstance.ID);
            Map<String, Object> context = relationshipInstance.get(IRelationshipInstance.CONTEXT) != null
                    ? (Map<String, Object>) relationshipInstance.get(IRelationshipInstance.CONTEXT) : new HashMap<String, Object>();
                
            // insert context
            Long[] contextiids = { 0L };
            if (!context.isEmpty()) {
              contextiids[0] = insertContext(context, id, "Relationship", id);
            }
            
            if(hit.getIndex().equals("csarchive")) {
              
              String commonRelationshipId = (String) relationshipInstance.get(IRelationshipInstance.COMMON_RELATIONSHIP_INSTANCE_ID);
              
              String side1InstanceId = (String)relationshipInstance.get(IRelationshipInstance.SIDE1_INSTANCE_ID);
              String side2InstanceId = (String)relationshipInstance.get(IRelationshipInstance.SIDE2_INSTANCE_ID);
              String relationshipDocType = hit.getType();
              String side1InstanceBaseType = (String) relationshipInstance.get(IRelationshipInstance.SIDE1_BASE_TYPE);
              String side2InstanceBaseType = (String) relationshipInstance.get(IRelationshipInstance.SIDE2_BASE_TYPE);
              
              if(!checkIfInstanceContainsCRId(side1InstanceId, commonRelationshipId, relationshipDocType, side1InstanceBaseType) 
                  && !checkIfInstanceContainsCRId(side2InstanceId, commonRelationshipId, relationshipDocType, side2InstanceBaseType)) {
                continue;
              }
              
            }                        
            Object contextTags = relationshipInstance.get(IRelationshipInstance.TAGS);
            insertRelationshipData(preparedStatement, relationshipInstance, id, contextiids, contextTags);
            insertTagRecordsData(contextTags, id);
            RDBMSLogger.instance().info("Relationship inserted in batch : " + id);

          }
          catch (Exception e) {
            RDBMSLogger.instance().warn("Relationship batch insert failed Due to my change::" + e.getMessage());
            e.printStackTrace();
          }
        }
        preparedStatement.executeBatch();
        currentConn.commit();
        RDBMSLogger.instance().info("Batch executed successfully.");
      });
    }
    catch (Exception e ) {
      RDBMSLogger.instance().warn("Relationship batch insert failed ::" + e.getMessage());
      e.printStackTrace();
    }
    return "success";
  }
  
  public boolean checkIfInstanceContainsCRId(String contentId, String commonRelationshipId, 
      String relationshipDocType, String baseType) throws Exception
  {
    
    boolean result = false;
    String docType = getDocTypeByBaseType(baseType);
    
    RestHighLevelClient restClient = RequestHandler.getRestClient();
    GetResponse getResponse = getDocumentById(restClient,"csarchive", contentId, docType);
    
    if (!getResponse.isExists()) {
      return result;
    }
    
    Map<String, Object> document = getResponse.getSourceAsMap();
    
    List<String> crIIds = (List<String>) (relationshipDocType.equals("naturerelationshipinstancecache")
        ? document.get(IContentInstance.NATURE_RELATIONSHIPS)
        : document.get(IContentInstance.RELATIONSHIPS));
    
    if (crIIds != null && crIIds.contains(commonRelationshipId)) {
      result = true;
    }
    
    return result;
  }
 
  public GetResponse getDocumentById(RestHighLevelClient client, String indexName,
      String documentId, String doctype) throws Exception
  {
    GetRequest getRequest = new GetRequest(indexName).type(doctype).id(documentId);
    GetResponse getRequestActionFuture = client.get(getRequest, RequestOptions.DEFAULT);
    
    return getRequestActionFuture;
  }
  
  
  public static String getDocTypeByBaseType(String baseType)
  {
    if (baseType == null) {
      return null;
    }
    switch (baseType) {
      case "com.cs.runtime.interactor.entity.ArticleInstance":
        return Constants.ARTICLE_KLASS_INSTANCE_DOC_TYPE;
      case "com.cs.runtime.interactor.entity.AssetInstance":
        return Constants.ASSET_INSTANCE_DOC_TYPE;
      case "com.cs.runtime.interactor.entity.MarketInstance":
        return Constants.MARKET_TARGET_INSTANCE_DOC_TYPE;
      case "com.cs.runtime.interactor.entity.textassetinstance.TextAssetInstance":
        return Constants.TEXTASSET_INSTANCE_DOC_TYPE;
      /*case "com.cs.runtime.interactor.entity.virtualcataloginstance.VirtualCatalogInstance":
        return Constants.VIRTUAL_CATALOG_INSTANCE_DOC_TYPE;*/
      case "com.cs.runtime.interactor.entity.supplierinstance.SupplierInstance":
        return Constants.SUPPLIER_INSTANCE_DOC_TYPE;
      case "com.cs.imprt.runtime.interactor.entity.fileinstance.OnboardingFileInstance":
        return Constants.FILE_INSTANCE_DOC_TYPE;
     default:
        return "";
    }
  }
  
  private void insertRelationshipData(PreparedStatement preparedStatement, Map<String, Object> relationshipInstance,
      Object id, Long[] contextiids, Object contextTags) throws SQLException
  {
    preparedStatement.setString(1, (String) relationshipInstance.get(IRelationshipInstance.COMMON_RELATIONSHIP_INSTANCE_ID));
    preparedStatement.setLong(2, contextiids[0]);
    preparedStatement.setString(3, (String) relationshipInstance.get(IRelationshipInstance.ORIGINAL_INSTANCE_ID));
    preparedStatement.setString(4, (String) relationshipInstance.get(IRelationshipInstance.RELATIONSHIP_ID));
    preparedStatement.setString(5, (String) relationshipInstance.get(IRelationshipInstance.RELATIONSHIP_OBJECT_ID));
    preparedStatement.setString(6, (String) relationshipInstance.get(IRelationshipInstance.SIDE1_BASE_TYPE));
    preparedStatement.setString(7, (String) relationshipInstance.get(IRelationshipInstance.SIDE1_INSTANCE_ID));
    Object side1InstanceVersionId = relationshipInstance.get(IRelationshipInstance.SIDE1_INSTANCE_VERSION_ID);
    preparedStatement.setString(8, side1InstanceVersionId == null ? "" : ((Integer) side1InstanceVersionId).toString());
    preparedStatement.setString(9, (String) relationshipInstance.get(IRelationshipInstance.SIDE2_BASE_TYPE));
    preparedStatement.setString(10, (String) relationshipInstance.get(IRelationshipInstance.SIDE2_INSTANCE_ID));
    Object side2InstanceVersionId = relationshipInstance.get(IRelationshipInstance.SIDE2_INSTANCE_VERSION_ID);
    preparedStatement.setString(11, side2InstanceVersionId == null ? "" : ((Integer) side2InstanceVersionId).toString());
    preparedStatement.setString(12, (String) relationshipInstance.get(IRelationshipInstance.SIDE_ID));
    Object versionTimestamp = relationshipInstance.get(IRelationshipInstance.VERSION_TIMESTAMP);
    preparedStatement.setLong(13, versionTimestamp == null ? 0 : (long) versionTimestamp);
    JSONObject jSONObject = new JSONObject();
    if (contextTags != null) {
      jSONObject.put(IRelationshipInstance.TAGS, contextTags);
    }
    preparedStatement.setObject(14, jSONObject);
    Object count = relationshipInstance.get(IRelationshipInstance.COUNT);
    preparedStatement.setLong(15, count == null ? 0 : Long.valueOf((int) count));
    preparedStatement.setString(16, (String) id);
    preparedStatement.setString(17, (String) relationshipInstance.get(IRelationshipInstance.VARIANT1_INSTANCE_ID));
    preparedStatement.setString(18, (String) relationshipInstance.get(IRelationshipInstance.VARIANT2_INSTANCE_ID));
    preparedStatement.setString(19, (String) relationshipInstance.get(CommonConstants.PARENT_ID_PROPERTY));
    Object versionId = relationshipInstance.get(CommonConstants.VERSION_ID);
    preparedStatement.setLong(20, Long.valueOf(versionId == null ? 0 : (int) versionId));
    Object parentVersionId = relationshipInstance.get(PARENT_VERSION_ID);
    preparedStatement.setString(21, (String.valueOf(parentVersionId == null ? 0 : (int) parentVersionId)));
    preparedStatement.addBatch();
  }
  
  /**
   * Store instance , attributes, tags data into RDBMS staging database.
   * @param batchCount 
   * @throws Exception 
   */
  void migrateEntityData(String index, Long batchCount) throws Exception
  {
    try {
      ScrollUtils.scrollThrough(this::entityFunctionalityInScroll, RequestHandler.getRestClient(), Arrays.asList(index),
          QueryBuilders.matchAllQuery(), docTypes, batchCount, batchSize.intValue());
      batchCount++;
    }
    catch (Exception e) {
      updateBatchCount("entitycount", batchCount, index);
      RDBMSLogger.instance().warn("Error while migrating Entity Data : " + e.getMessage());
      throw e;
    }
    updateBatchCount("entitycount", batchCount, index);
  }
  
  private String entityFunctionalityInScroll(SearchHit[] searchHits)
  {
    try {
      getAttributeConfigDetails();
      
      for (SearchHit hit : searchHits) {
        Map<String, Object> esInstance = hit.getSourceAsMap();
        String index = hit.getIndex();
        addLanguageDepedentAttributesData(index, esInstance, hit.getType());
        insertEntityData(esInstance);
        insertValueRecordsData(index, esInstance);
        insertTagRecordsData(esInstance.get("tags"), esInstance.get("id"));
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().warn("Error while migrating entity :: " + e.getMessage());
    }
    
    return "success";
  }
  
  public static final String GET_BATCH_COUNT = "SELECT * FROM staging.successfulbatchcount where index = ?";
  private Map<String, Long> getSuccessfulBatchCount(String index)
  {
    Map<String, Long> batchCount = new HashMap<>();
    
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement prepareStatement = currentConn.prepareStatement(GET_BATCH_COUNT);
        prepareStatement.setString(1, index);
        ResultSet resultSet = prepareStatement.executeQuery();
        if (resultSet.next()) {
          batchCount.put(ENTITY_COUNT, resultSet.getLong(2));
          batchCount.put(RELATIONSHIP_COUNT, resultSet.getLong(3));
          batchCount.put(REFERENCE_COUNT, resultSet.getLong(4));
          batchCount.put(TASK_COUNT, resultSet.getLong(5));
        }
      });
    }
    catch (Exception e) {
      RDBMSLogger.instance().warn("Failure :: getSuccessfulBatchCount :: " + e.getMessage());
    }
    
    return batchCount;
  }
  
  private void updateBatchCount(String columnName, Long columnValue, String index)
  {
    StringBuilder query = new StringBuilder("UPDATE staging.successfulbatchcount set ")
        .append(columnName).append(" = ").append(columnValue).append(" where index = '" +index +"'");
    
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement prepareStatement = currentConn.prepareStatement(query);
        prepareStatement.executeUpdate();
        currentConn.commit();
      });
    }
    catch (Exception e) {
      RDBMSLogger.instance().warn("Failure :: updateBatchCount :: " + e.getMessage());
    }
  }

  protected void insertTagConflictingValues(RDBMSConnection currentConn, Object conflictingValuesObject, String tagId, String klassInstanceId)
  {
    List<Object> conflictingValuesList = conflictingValuesObject != null ? (List<Object>)conflictingValuesObject : new ArrayList<Object>();
    for (Object conflictValueObject : conflictingValuesList) {
      JSONObject conflictValueJSONObject = new JSONObject((Map<String, Object>) conflictValueObject);
      Object id = conflictValueJSONObject.get("id");
      try {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_CONFLICTING_TAGS_RECORD);
        preparedStatement.setString(1, (String) id);
        preparedStatement.setString(2, tagId);
        preparedStatement.setString(3, (String) conflictValueJSONObject.get("couplingType"));
        preparedStatement.setObject(4, conflictValueJSONObject.get("source"));
        preparedStatement.setString(5, klassInstanceId);
        List<Object> tagValues = (List<Object>) conflictValueJSONObject.get("tagValues");
        if (tagValues != null && !tagValues.isEmpty()) {
          Map<String, String> tagKeyValueMap = new HashMap<>();
          for (Object tagValue : tagValues) {
            JSONObject tagValueJSONObject = new JSONObject((Map<String, Object>) tagValue);
            int tagRelevance = (int) tagValueJSONObject.get("relevance");
            if (tagRelevance != 0) {
              tagKeyValueMap.put((String) tagValueJSONObject.get("tagId"), String.valueOf(tagRelevance));
            }
          }
          preparedStatement.setObject(6, tagKeyValueMap);
        } else {
          preparedStatement.setObject(6, null);
        }
        Object isMandatory = conflictValueJSONObject.get("isMandatory");
        preparedStatement.setBoolean(7, (boolean) ((isMandatory == null) ? false : isMandatory));
        Object isShould = conflictValueJSONObject.get("isShould");
        preparedStatement.setBoolean(8, (boolean) ((isShould == null) ? false : isShould));
        Object versionTimestamp = conflictValueJSONObject.get("versionTimestamp");
        preparedStatement.setLong(9, versionTimestamp == null ? 0 : (Long) versionTimestamp);
        preparedStatement.setString(10, (String) conflictValueJSONObject.get("lastModifiedBy"));
        preparedStatement.setString(11, (String) conflictValueJSONObject.get("versionId"));
        preparedStatement.executeUpdate();
        RDBMSLogger.instance().info("Tag conflicting values inserted successfully for tagId : " + tagId);
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().warn("Tag conflicting values insert failed Id : " + id);
      }
    }
  }
    
  protected void insertTagRecordsData(Object tagsObject, Object entityId)
  {
    List<Map<String, Object>> tagRecordDataList = tagsObject != null ? (ArrayList<Map<String, Object>>) tagsObject : new ArrayList<Map<String,Object>>();
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_TAG_RECORDS_QUERY);
        
        for (Map<String, Object> tagRecordMap : tagRecordDataList) {
          JSONObject tagRecordjSONObject = new JSONObject(tagRecordMap);
          String tagId = (String) tagRecordjSONObject.get(ITagInstance.TAG_ID);
          String variantInstanceId = (String) tagRecordjSONObject.get(ITagInstance.VARIANT_INSTANCE_ID);
          String klassInstanceId = StringUtils.isEmpty(variantInstanceId) ? (String) tagRecordjSONObject.get("klassInstanceId") : variantInstanceId;
          
          if (klassInstanceId != null) {
            preparedStatement.setString(1, (String) tagRecordjSONObject.get(ITagInstance.BASE_TYPE));
            JSONObject jSONObject = new JSONObject();
            //Inserting tag conflicting values
            insertTagConflictingValues(currentConn, tagRecordjSONObject.get(ITagInstance.CONFLICTING_VALUES), tagId, klassInstanceId);
            preparedStatement.setString(2, (String) tagRecordjSONObject.get(ITagInstance.CONTEXT_INSTANCE_ID));
            preparedStatement.setString(3, (String) tagRecordjSONObject.get(ITagInstance.CREATED_BY));
            Object createdOn = tagRecordjSONObject.get(ITagInstance.CREATED_ON);
            preparedStatement.setLong(4, createdOn == null ? 0 : (Long) createdOn);
            preparedStatement.setString(5, (String) tagRecordjSONObject.get(ITagInstance.ID));
            preparedStatement.setBoolean(6, (Boolean) tagRecordjSONObject.get(ITagInstance.IS_CONFLICT_RESOLVED));
            preparedStatement.setBoolean(7, (Boolean) tagRecordjSONObject.get(ITagInstance.IS_MANDATORY_VIOLATED));
            preparedStatement.setBoolean(8, (Boolean) tagRecordjSONObject.get(ITagInstance.IS_MATCH_AND_MERGE));
            preparedStatement.setBoolean(9, (Boolean) tagRecordjSONObject.get(ITagInstance.IS_SHOULD_VIOLATED));
            preparedStatement.setString(10, (String) tagRecordjSONObject.get(ITagInstance.JOB_ID));
            preparedStatement.setString(11, (String) tagRecordjSONObject.get(ITagInstance.KLASS_INSTANCE_ID));
            Object lastModified = tagRecordjSONObject.get(ITagInstance.LAST_MODIFIED);
            preparedStatement.setLong(12, lastModified == null ? 0 : (Long) lastModified);
            preparedStatement.setString(13, (String) tagRecordjSONObject.get(ITagInstance.LAST_MODIFIED_BY));
            preparedStatement.setObject(14, tagRecordjSONObject.get(ITagInstance.NOTIFICATION));
            preparedStatement.setString(15, tagId);
            jSONObject.clear();
            List<Object> tagValues = (List<Object>) tagRecordjSONObject.get("tagValues");
            if (tagValues != null && !tagValues.isEmpty()) {
              Map<String, String> tagKeyValueMap = new HashMap<>();
              for (Object tagValue : tagValues) {
                JSONObject tagValueJSONObject = new JSONObject((Map<String, Object>) tagValue);
                String relevance = String.valueOf(tagValueJSONObject.get("relevance"));
                if (!"0".equals(relevance)) {
                  tagKeyValueMap.put((String) tagValueJSONObject.get("tagId"), relevance);
                }
              }
              preparedStatement.setObject(16, tagKeyValueMap);
            } else {
              preparedStatement.setObject(16, null);
            }
            preparedStatement.setString(17, variantInstanceId == null ? "" : variantInstanceId);
            preparedStatement.setString(18, String.valueOf(tagRecordjSONObject.get(ITagInstance.VERSION_ID)));
            Object versionTimestamp = tagRecordjSONObject.get(ITagInstance.VERSION_TIMESTAMP);
            preparedStatement.setLong(19, versionTimestamp == null ? 0 : (Long) versionTimestamp);
            preparedStatement.addBatch();
            RDBMSLogger.instance().warn("Tag inserted Tag Id : " + tagRecordjSONObject.get("id") + ", Document Id: " + entityId);
          }
        }
        preparedStatement.executeBatch();
        currentConn.commit();
      });
      RDBMSLogger.instance().warn("Tag batch insertion successful, Document Id: " + entityId);
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().warn("Tag batch insertion failed, Document Id: " + entityId);
    }
  }
  
  private void getAttributeConfigDetails() throws Exception
  {
    List<String> types = Arrays.asList(CommonConstants.CALCULATED_ATTRIBUTE_TYPE, CommonConstants.CONCATENATED_ATTRIBUTE_TYPE,
        CommonConstants.LENGTH_ATTRIBUTE_TYPE, CommonConstants.AREA_ATTRIBUTE_TYPE, CommonConstants.AREA_PER_VOLUME_ATTRIBUTE_TYPE,
        CommonConstants.CAPACITANCE_ATTRIBUTE_TYPE, CommonConstants.ACCELERATION_ATTRIBUTE_TYPE, CommonConstants.CHARGE_ATTRIBUTE_TYPE,
        CommonConstants.CONDUCTANCE_ATTRIBUTE_TYPE, CommonConstants.CURRENT_ATTRIBUTE_TYPE, CommonConstants.CURRENCY_ATTRIBUTE_TYPE,
        CommonConstants.CUSTOM_UNIT_ATTRIBUTE_TYPE, CommonConstants.DIGITAL_STORAGE_ATTRIBUTE_TYPE,
        CommonConstants.ENERGY_ATTRIBUTE_TYPE, CommonConstants.FREQUENCY_ATTRIBUTE_TYPE, CommonConstants.HEATING_RATE_ATTRIBUTE_TYPE,
        CommonConstants.ILLUMINANCE_ATTRIBUTE_TYPE, CommonConstants.INDUCTANCE_ATTRIBUTE_TYPE, CommonConstants.FORCE_ATTRIBUTE_TYPE,
        CommonConstants.LUMINOSITY_ATTRIBUTE_TYPE, CommonConstants.MAGNETISM_ATTRIBUTE_TYPE, CommonConstants.MASS_ATTRIBUTE_TYPE,
        CommonConstants.POTENTIAL_ATTRIBUTE_TYPE, CommonConstants.POWER_ATTRIBUTE_TYPE, CommonConstants.PROPORTION_ATTRIBUTE_TYPE,
        CommonConstants.RADIATION_ATTRIBUTE_TYPE, CommonConstants.ROTATION_FREQUENCY_ATTRIBUTE_TYPE,
        CommonConstants.SPEED_ATTRIBUTE_TYPE, CommonConstants.SUBSTANCE_ATTRIBUTE_TYPE, CommonConstants.TEMPERATURE_ATTRIBUTE_TYPE,
        CommonConstants.THERMAL_INSULATION_ATTRIBUTE_TYPE, CommonConstants.VISCOCITY_ATTRIBUTE_TYPE,
        CommonConstants.VOLUME_ATTRIBUTE_TYPE, CommonConstants.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE,
        CommonConstants.WEIGHT_PER_TIME_ATTRIBUTE_TYPE, CommonConstants.WEIGHT_PER_AREA_ATTRIBUTE_TYPE,
        CommonConstants.RESISTANCE_ATTRIBUTE_TYPE, CommonConstants.PRESSURE_ATTRIBUTE_TYPE, CommonConstants.PLANE_ANGLE_ATTRIBUTE_TYPE,
        CommonConstants.DENSITY_ATTRIBUTE_TYPE);
    Map<String, Object> configGetAllRequestModel = new HashMap<>();
    configGetAllRequestModel.put("from", 0);
    configGetAllRequestModel.put("size", -1);
    configGetAllRequestModel.put("sortOrder", "asc");
    configGetAllRequestModel.put("searchText", "");
    configGetAllRequestModel.put("sortBy","label");
    configGetAllRequestModel.put("searchColumn","label");
    configGetAllRequestModel.put("types", types);
   
    JSONObject response = CSConfigServer.instance().request(configGetAllRequestModel, "GetGridAttributes", null); 
    if (response == null) {
      return;
    }
    List<Map<String, Object>> attributeList = (List<Map<String, Object>>) response.get("attributeList");
    Map<String, Object> attributeMap = new HashMap<String, Object>();
    for (Map<String, Object> attribute : attributeList) {
      if (attribute != null) {
        attributeMap.put((String) attribute.get("id"), attribute);
      }
    }
    Map<String, Object> configDetails = (Map<String, Object>) response.get("configDetails");
    configDetails.put("attributeConfigMap", attributeMap);
    attributeConfigDetails = configDetails;
  }

  private void addLanguageDepedentAttributesData(String index, Map<String, Object> document,  String docType) throws MalformedURLException, URISyntaxException, IOException, Exception
  {
    List<String> languageCodes = (List<String>) document.get("languageCodes");
    for (String languageCode : languageCodes) {
      String languageDocType = docType + separator + languageCode;
      String languageDocId = (String) document.get("id") + separator + languageCode;
      
      Map<String, Object> languageDocument = RequestHandler.getDocumentById(index, languageDocType, languageDocId);
      List<Map<String, Object>>attributes = (List<Map<String, Object>>) document.get("attributes");
      List<Map<String, Object>>dependentAttributes = (List<Map<String, Object>>) languageDocument.get("dependentAttributes");
      List<Map<String, Object>>attributesVariants = (List<Map<String, Object>>) document.get("attributeVariants");
      List<Map<String, Object>>dependentAttributeVariants = (List<Map<String, Object>>) languageDocument.get("attributeVariants");
      attributesVariants.addAll(dependentAttributeVariants);
      attributes.addAll(dependentAttributes);
    }
  }
  
  protected void insertAttributeConflictingValues(RDBMSConnection currentConn, Object conflictingValuesObject, String attributeId, String klassInstanceId, String language)
  {
    List<Object> conflictingValuesList = conflictingValuesObject != null ? (List<Object>)conflictingValuesObject : new ArrayList<Object>();
    for (Object conflictValueObject : conflictingValuesList) {
      JSONObject conflictValueJSONObject = new JSONObject((Map<String, Object>) conflictValueObject);
      Object id = conflictValueJSONObject.get("id");
      try {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_CONFLICTING_VALUES_RECORD);
        preparedStatement.setString(1, (String) id);
        preparedStatement.setString(2, attributeId);
        preparedStatement.setString(3, (String) conflictValueJSONObject.get("couplingType"));
        preparedStatement.setObject(4, conflictValueJSONObject.get("source"));
        preparedStatement.setString(5, klassInstanceId);
        Object isMandatory = conflictValueJSONObject.get("isMandatory");
        preparedStatement.setBoolean(6, (boolean) ((isMandatory == null) ? false : isMandatory));
        Object isShould = conflictValueJSONObject.get("isShould");
        preparedStatement.setBoolean(7, (boolean) ((isShould == null) ? false : isShould));
        preparedStatement.setString(8, (String) conflictValueJSONObject.get("value"));
        preparedStatement.setString(9, language);
        preparedStatement.setString(10, (String) conflictValueJSONObject.get("valueAsHtml"));
        Object versionTimestamp = conflictValueJSONObject.get("versionTimestamp");
        preparedStatement.setLong(11, versionTimestamp == null ? 0 : (Long) versionTimestamp);
        preparedStatement.setString(12, (String) conflictValueJSONObject.get("lastModifiedBy"));
        preparedStatement.setString(13, (String) conflictValueJSONObject.get("versionId"));
        preparedStatement.executeUpdate();
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().warn("Conflicting values insert failed Id : " + id);
      }
    }
  }

  protected void insertValueRecordsData(String index, Map<String, Object> documentDataMap)
  {
    List<Map<String, Object>> valueRecordDataList = (List<Map<String, Object>>) documentDataMap.get("attributes");
    // convert  event-schedule data into value records and add to the list of attributes in ES document.
    addEventScheduleValueRecords(documentDataMap);
    // Collect attribute Variants data 
    addAttributeVariantsData(index, documentDataMap);
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_VALUE_RECORD_QUERY);
        
        for (Map<String, Object> valueRecordMap : valueRecordDataList) {
          JSONObject valueRecordjSONObject = new JSONObject(valueRecordMap);
          Object id = valueRecordjSONObject.get("id");
          String code = (String) valueRecordjSONObject.get("code");
          
          Long [] contextiids  = {0L} ;
          Map<String, Object> context  = valueRecordjSONObject.get("context") != null ? (Map<String, Object> )valueRecordjSONObject.get("context") : new HashMap<String, Object>();
          if (!context.isEmpty()) {
            // insert context
            contextiids[0] = insertContext((Map<String, Object>) context, id, "Attribute", documentDataMap.get("id"));
          } 
          Object contextTags = valueRecordjSONObject.get("tags");
          // add Calculated, Unit and Concatenated attributes data 
          getCalulationAndUnitData(valueRecordjSONObject);
          
          String attributeId = (String) valueRecordjSONObject.get("attributeId");
          String klassInstanceId = StringUtils.isEmpty((String) valueRecordjSONObject.get("variantInstanceId")) ? (String) valueRecordjSONObject.get("klassInstanceId") : (String) valueRecordjSONObject.get("variantInstanceId");
          if ("assetcoverflowattribute".equals(attributeId)) {
            continue;
          }
          String language = (String) valueRecordjSONObject.get("language");
          preparedStatement.setString(1, attributeId);
          preparedStatement.setString(2, (String) valueRecordjSONObject.get("baseType"));
          preparedStatement.setString(3, code);
          // inserting in conflictingValues table
          insertAttributeConflictingValues(currentConn, valueRecordjSONObject.get("conflictingValues"), attributeId, klassInstanceId, language);
          preparedStatement.setLong(4, contextiids[0]);
          preparedStatement.setString(5, (String) valueRecordjSONObject.get("createdBy"));
          Object createdOn = valueRecordjSONObject.get("createdOn");
          preparedStatement.setLong(6, createdOn == null ? 0 : (Long)createdOn);
          JSONObject jSONObject = new JSONObject();
          jSONObject.put("duplicateStatus", valueRecordjSONObject.get("duplicateStatus"));
          preparedStatement.setObject(7, jSONObject);
          preparedStatement.setString(8, (String) id);
          preparedStatement.setBoolean(9, (Boolean) valueRecordjSONObject.get("isConflictResolved"));
          preparedStatement.setBoolean(10, (Boolean) valueRecordjSONObject.get("isMandatoryViolated"));
          preparedStatement.setBoolean(11, (Boolean) valueRecordjSONObject.get("isMatchAndMerge"));
          preparedStatement.setBoolean(12, (Boolean) valueRecordjSONObject.get("isShouldViolated"));
          preparedStatement.setInt(13, (Integer) valueRecordjSONObject.get("isUnique"));
          preparedStatement.setString(14, (String) valueRecordjSONObject.get("jobId"));
          preparedStatement.setString(15, (String) valueRecordjSONObject.get("klassInstanceId"));
          Object klassInstanceVersion = valueRecordjSONObject.get("klassInstanceVersion");
          preparedStatement.setLong(16, klassInstanceVersion ==null ? 0: (Long) klassInstanceVersion);
          preparedStatement.setString(17, language);
          Object lastModified = valueRecordjSONObject.get("lastModified");
          preparedStatement.setLong(18, lastModified == null ? 0 : (Long) lastModified);
          preparedStatement.setString(19, (String) valueRecordjSONObject.get("lastModifiedBy"));
          preparedStatement.setObject(20, valueRecordjSONObject.get("notification"));
          preparedStatement.setString(21, (String) valueRecordjSONObject.get("originalInstanceId"));
          preparedStatement.setString(22, (String) valueRecordjSONObject.get("saveComment"));
          jSONObject.clear();
          jSONObject.put("tags", contextTags);
          preparedStatement.setObject(23, jSONObject);
          preparedStatement.setString(24, (String) valueRecordjSONObject.get("value"));
          jSONObject.clear();
          jSONObject.put("valueAsExpression", valueRecordjSONObject.get("valueAsExpression"));
          preparedStatement.setObject(25, jSONObject);
          preparedStatement.setString(26, (String) valueRecordjSONObject.get("valueAsHtml"));
          Object valueAsNumber = valueRecordjSONObject.get("valueAsNumber");
          preparedStatement.setDouble(27, valueAsNumber == null ? 0 : (Double.valueOf(String.valueOf(valueAsNumber))));
          Object variantInstanceId = valueRecordjSONObject.get("variantInstanceId");
          preparedStatement.setString(28, variantInstanceId == null ? "" : (String) variantInstanceId);
          Object versionId = valueRecordjSONObject.get("versionId");
          preparedStatement.setString(29, String.valueOf(versionId));
          Object versionTimestamp = valueRecordjSONObject.get("versionTimestamp");
          preparedStatement.setLong(30, versionTimestamp == null ? 0 : (Long) versionTimestamp);
          preparedStatement.setString(31, (String) valueRecordjSONObject.get("unitsymbol"));
          preparedStatement.setString(32, (String) valueRecordjSONObject.get("calculation"));
          preparedStatement.addBatch();
          RDBMSLogger.instance().info("Attribute insert Attribute Id : " + id + ", Document Id: " + documentDataMap.get("id"));
          
          //insert context tags data.
          insertTagRecordsData(contextTags, id);
        }
        preparedStatement.executeBatch();
        currentConn.commit();
      });
      RDBMSLogger.instance().info("Attribute batch insertion successful, Document Id: " + documentDataMap.get("id"));
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().warn("Attribute batch insertion failed, Document Id: " + documentDataMap.get("id") + "::" + e.getMessage());
    }
  }
  
  private void getCalulationAndUnitData( JSONObject valueRecordjSONObject) 
  {
    try {
      Map<String, Object> referencedAttributes = (Map<String, Object>) attributeConfigDetails.get("referencedAttributes");
      Map<String, Object> referencedTags = (Map<String, Object>) attributeConfigDetails.get("referencedTags");
      Map<String, Object> attributeConfigMap = (Map<String, Object>) attributeConfigDetails.get("attributeConfigMap");
      Map<String, Object> attributeConfig = (Map<String, Object>) attributeConfigMap.get(valueRecordjSONObject.get("attributeId"));
      
      if (attributeConfigMap == null || attributeConfigMap.isEmpty() ||attributeConfig == null) {
        return;
      }
      String type = (String) attributeConfig.get("type");
      switch (type) {
        case CommonConstants.CALCULATED_ATTRIBUTE_TYPE:
          getCalculatedAttributeData(valueRecordjSONObject, referencedAttributes, attributeConfig);
          break;

        case CommonConstants.CONCATENATED_ATTRIBUTE_TYPE:
          getConcatenatedAttributeData(valueRecordjSONObject, referencedAttributes, attributeConfig, referencedTags);
          break;

        case CommonConstants.LENGTH_ATTRIBUTE_TYPE:
        case CommonConstants.AREA_ATTRIBUTE_TYPE:
        case CommonConstants.AREA_PER_VOLUME_ATTRIBUTE_TYPE:
        case CommonConstants.CAPACITANCE_ATTRIBUTE_TYPE:
        case CommonConstants.ACCELERATION_ATTRIBUTE_TYPE:
        case CommonConstants.CHARGE_ATTRIBUTE_TYPE:
        case CommonConstants.CONDUCTANCE_ATTRIBUTE_TYPE:
        case CommonConstants.CURRENT_ATTRIBUTE_TYPE:
        case CommonConstants.CURRENCY_ATTRIBUTE_TYPE:
        case CommonConstants.CUSTOM_UNIT_ATTRIBUTE_TYPE:
        case CommonConstants.DIGITAL_STORAGE_ATTRIBUTE_TYPE:
        case CommonConstants.ENERGY_ATTRIBUTE_TYPE:
        case CommonConstants.FREQUENCY_ATTRIBUTE_TYPE:
        case CommonConstants.HEATING_RATE_ATTRIBUTE_TYPE:
        case CommonConstants.ILLUMINANCE_ATTRIBUTE_TYPE:
        case CommonConstants.INDUCTANCE_ATTRIBUTE_TYPE:
        case CommonConstants.FORCE_ATTRIBUTE_TYPE:
        case CommonConstants.LUMINOSITY_ATTRIBUTE_TYPE:
        case CommonConstants.MAGNETISM_ATTRIBUTE_TYPE:
        case CommonConstants.MASS_ATTRIBUTE_TYPE:
        case CommonConstants.POTENTIAL_ATTRIBUTE_TYPE:
        case CommonConstants.POWER_ATTRIBUTE_TYPE:
        case CommonConstants.PROPORTION_ATTRIBUTE_TYPE:
        case CommonConstants.RADIATION_ATTRIBUTE_TYPE:
        case CommonConstants.ROTATION_FREQUENCY_ATTRIBUTE_TYPE:
        case CommonConstants.SPEED_ATTRIBUTE_TYPE:
        case CommonConstants.SUBSTANCE_ATTRIBUTE_TYPE:
        case CommonConstants.TEMPERATURE_ATTRIBUTE_TYPE:
        case CommonConstants.THERMAL_INSULATION_ATTRIBUTE_TYPE:
        case CommonConstants.VISCOCITY_ATTRIBUTE_TYPE:
        case CommonConstants.VOLUME_ATTRIBUTE_TYPE:
        case CommonConstants.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE:
        case CommonConstants.WEIGHT_PER_TIME_ATTRIBUTE_TYPE:
        case CommonConstants.WEIGHT_PER_AREA_ATTRIBUTE_TYPE:
        case CommonConstants.RESISTANCE_ATTRIBUTE_TYPE:
        case CommonConstants.PRESSURE_ATTRIBUTE_TYPE:
        case CommonConstants.PLANE_ANGLE_ATTRIBUTE_TYPE:
        case CommonConstants.DENSITY_ATTRIBUTE_TYPE:
          getMeasurementAttributeData(valueRecordjSONObject, attributeConfig);
          break;
        
        default:
          return;
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().warn("Failed while getting Calulation And Unit Data for attrubute id :"+ valueRecordjSONObject.get("id"));
      e.printStackTrace();
    }
  }

  private void getMeasurementAttributeData(JSONObject valueRecordjSONObject, Map<String, Object> attributeConfig)
  {
    valueRecordjSONObject.put("unitsymbol", attributeConfig.get("defaultUnit")); 
  }

  private void getCalculatedAttributeData(JSONObject valueRecordjSONObject, Map<String, Object> referencedAttributes, Map<String, Object> attributeConfig)
  {
    List<Map<String, Object>> attributeOperatorList = (List<Map<String, Object>>) attributeConfig.get("attributeOperatorList");
    
    StringBuilder mathExpression = new StringBuilder();
    if (!attributeOperatorList.isEmpty()) {
      mathExpression.append("= ");
    }
    valueRecordjSONObject.put("unitsymbol",attributeConfig.get("calculatedAttributeUnit"));
    for (Map<String, Object> attributeOperator : attributeOperatorList) {
      
      String attributeOperatorType = (String) attributeOperator.get("type");
      
      switch (attributeOperatorType) {
        case "ATTRIBUTE":
          String attributeId = (String) attributeOperator.get("attributeId");
          
          if (referencedAttributes.containsKey(attributeId) && referencedAttributes.get(attributeId) != null) {
            Map<String, Object> iAttribute = (Map<String, Object>) referencedAttributes.get(attributeId);
            mathExpression.append("[" + ((String) iAttribute.get("code")) + "]");
          }
          else {
            mathExpression.append("0");
          }
          break;
        
        case "ADD":
          mathExpression.append(" +");
          break;
        
        case "SUBTRACT":
          mathExpression.append(" -");
          break;
        
        case "MULTIPLY":
          mathExpression.append(" *");
          break;
        
        case "DIVIDE":
          mathExpression.append(" /");
          break;
        
        case "VALUE":
          mathExpression.append(" " + attributeOperator.get("value"));
          break;
        
        case "OPENING_BRACKET":
          mathExpression.append(" (");
          break;
        
        case "CLOSING_BRACKET":
          mathExpression.append(" )");
          break;
      }
    }
    valueRecordjSONObject.put("calculation", mathExpression.toString());
  }
  
  private void getConcatenatedAttributeData(JSONObject valueRecordjSONObject, Map<String, Object> referencedAttributes,
      Map<String, Object> attributeConfig, Map<String, Object> referencedTags) 
  {
    List<Map<String, Object>> attributeConcatenatedList = (List<Map<String, Object>>) attributeConfig.get("attributeConcatenatedList");
    
    StringBuilder mathExpression = new StringBuilder();
    for (Map<String, Object> concatenatedAttributeOperator : attributeConcatenatedList) {

      if (mathExpression.length() == 0) {
        mathExpression.append("=");
      }
      else {
        mathExpression.append("||");
      }

      String type = (String) concatenatedAttributeOperator.get("type");
      switch (type) {
        case "attribute":
          Object attributeId = concatenatedAttributeOperator.get("attributeId");
          if (referencedAttributes.containsKey(attributeId) && referencedAttributes.get(attributeId) != null) {
            Map<String, Object> configSourceAttribute = (Map<String, Object>) referencedAttributes.get(attributeId);
            mathExpression.append("[" + configSourceAttribute.get("code") + "]");
          }
          break;

        case "html":
          mathExpression.append("'" + concatenatedAttributeOperator.get("value") + "'");
          break;

        case "tag":
          Object tagId = concatenatedAttributeOperator.get("tagId");
          if (referencedTags.containsKey(tagId) && referencedTags.get(tagId) !=null) {
            Map<String, Object> tag = (Map<String, Object>)referencedTags.get(tagId) ;
            mathExpression.append("[" + tag.get("code") + "]");
          }
          break;
      }
    }
    valueRecordjSONObject.put("calculation",mathExpression.toString());
  }
  
  private void addEventScheduleValueRecords(Map<String, Object> documentDataMap)
  {
    if(documentDataMap.get("eventSchedule") == null) {
      return;
    }
    try {
      String startAttribute = "schedulestartattribute";
      String endAttribute = "scheduleendattribute";
      List<Map<String, Object>> valueRecordDataList = (List<Map<String, Object>>) documentDataMap.get("attributes");
      Map<String, Object> createdByAndCreatedOnAttributes = getCreatedByAndCreatedOnAttributes(valueRecordDataList);
      Map<String, Object> eventScheduleMap = (Map<String, Object>) documentDataMap.get("eventSchedule");
      Map<String, Object> scheduleStartAttribute = new HashMap<String, Object>();
      scheduleStartAttribute.put("attributeId", startAttribute);
      scheduleStartAttribute.put("baseType", "com.cs.runtime.interactor.entity.AttributeInstance");
      scheduleStartAttribute.put("code", startAttribute);
      scheduleStartAttribute.put("createdBy",createdByAndCreatedOnAttributes.get("createdBy")); 
      scheduleStartAttribute.put("createdOn",Long.valueOf((String) createdByAndCreatedOnAttributes.get("createdOn")));
      scheduleStartAttribute.put("duplicateStatus", new ArrayList<Object>());
      scheduleStartAttribute.put("id", UUID.randomUUID().toString());
      scheduleStartAttribute.put("isConflictResolved", false);
      scheduleStartAttribute.put("isMandatoryViolated", false);
      scheduleStartAttribute.put("isMatchAndMerge", false);
      scheduleStartAttribute.put("isShouldViolated", false);
      scheduleStartAttribute.put("isUnique", 0);
      scheduleStartAttribute.put("klassInstanceId", documentDataMap.get("id"));
      scheduleStartAttribute.put("lastModified", documentDataMap.get("lastModified"));
      scheduleStartAttribute.put("lastModifiedBy", eventScheduleMap.get("lastModifiedBy"));
      scheduleStartAttribute.put("value", String.valueOf(( eventScheduleMap.get("startTime"))));
      scheduleStartAttribute.put("valueAsNumber", eventScheduleMap.get("startTime"));
      scheduleStartAttribute.put("versionId", eventScheduleMap.get("versionId"));
      scheduleStartAttribute.put("versionTimestamp", eventScheduleMap.get("versionTimestamp"));
      
      Map<String, Object> scheduleEndAttribute = new HashMap<String, Object>();
      scheduleEndAttribute.put("attributeId", endAttribute);
      scheduleEndAttribute.put("baseType", "com.cs.runtime.interactor.entity.AttributeInstance");
      scheduleEndAttribute.put("code", endAttribute);
      scheduleEndAttribute.put("createdBy",createdByAndCreatedOnAttributes.get("createdBy")); 
      scheduleEndAttribute.put("createdOn",Long.valueOf((String) createdByAndCreatedOnAttributes.get("createdOn")));
      scheduleEndAttribute.put("duplicateStatus", new ArrayList<Object>());
      scheduleEndAttribute.put("id", UUID.randomUUID().toString());
      scheduleEndAttribute.put("isConflictResolved", false);
      scheduleEndAttribute.put("isMandatoryViolated", false);
      scheduleEndAttribute.put("isMatchAndMerge", false);
      scheduleEndAttribute.put("isShouldViolated", false);
      scheduleEndAttribute.put("isUnique", 0);
      scheduleEndAttribute.put("klassInstanceId", documentDataMap.get("id"));
      scheduleEndAttribute.put("lastModified", documentDataMap.get("lastModified"));
      scheduleEndAttribute.put("lastModifiedBy", eventScheduleMap.get("lastModifiedBy"));
      scheduleEndAttribute.put("value", String.valueOf(( eventScheduleMap.get("endTime"))));
      scheduleEndAttribute.put("valueAsNumber", eventScheduleMap.get("endTime"));
      scheduleEndAttribute.put("versionId", eventScheduleMap.get("versionId"));
      scheduleEndAttribute.put("versionTimestamp", eventScheduleMap.get("versionTimestamp"));
      
      valueRecordDataList.add(scheduleStartAttribute);
      valueRecordDataList.add(scheduleEndAttribute);
    }
    catch (Exception e) {
      RDBMSLogger.instance().warn("Event schedule attribute cretion failed for Id :" + documentDataMap.get("id") );
      e.printStackTrace();
    }
    
  }

  private Map<String, Object> getCreatedByAndCreatedOnAttributes(List<Map<String, Object>> valueRecordDataList)
  {
    Map<String, Object> map = new HashMap<String, Object>();
    for (Map<String, Object> valueRecord : valueRecordDataList) {
      String code = (String) valueRecord.get("code");
      if (code.equals("createdbyattribute")) {
        map.put("createdBy", valueRecord.get("value"));
      }
      if (code.equals("createdonattribute")) {
        map.put("createdOn", valueRecord.get("value"));
      }
    }
    return map;
  }

  private Long insertContext(Map<String, Object> contextMap, Object id, String entityType, Object documentId)
  {
    Long [] contextiids  = {0L} ;
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        RDBMSAbstractDriver driver = currentConn.getDriver();
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_CONTEXT_QUERY);
        
        preparedStatement.setString(1, (String) contextMap.get("id"));
        preparedStatement.setString(2, (String) contextMap.get("contextId"));
        preparedStatement.setString(3, (String) contextMap.get("lastModifiedBy"));
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("linkedInstances", contextMap.get("linkedInstances"));
        preparedStatement.setObject(4, jSONObject);
        Object tagInstanceIds = contextMap.get("tagInstanceIds");
        preparedStatement.setArray(5, currentConn.newStringArray(tagInstanceIds == null ? new ArrayList<String>() : (List<String>) tagInstanceIds));
        jSONObject.clear();
        jSONObject.put("timeRange", contextMap.get("timeRange"));
        preparedStatement.setObject(6, jSONObject);
        preparedStatement.setString(7, (String) contextMap.get("versionId"));
        Object versionTimestamp = contextMap.get("versionTimestamp");
        preparedStatement.setLong(8, versionTimestamp == null ? 0 : (Long) versionTimestamp);
        IResultSetParser result = driver.getResultSetParser(preparedStatement.executeQuery());
        if (result.next()) {
          contextiids[0] = result.getLong("contextiid");
        }
        currentConn.commit();
      });
      RDBMSLogger.instance().warn("Context inserted for " + entityType + ", Context Id : " + contextMap.get("id") +" entity id : "+id+ ", Document Id: " + documentId);
     
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().warn("Context insert failed for " + entityType + ", Id : " + id + ", Document Id: " + documentId);
    }
    return contextiids[0];
  }
  
  protected void insertEntityData(Map<String, Object> entityJDataMap) throws RDBMSException
  {
    JSONObject entityJSONObject = new JSONObject(entityJDataMap);
    Map<String, Object> context  = entityJSONObject.get("context") != null ? (Map<String, Object> )entityJSONObject.get("context") : new HashMap<String, Object>();
    Object eitityId = entityJSONObject.get("id");
    // insert context
    Long [] contextiids  = {0L} ;
    if (!context.isEmpty()) {
      contextiids[0] = insertContext((Map<String, Object>) context, eitityId, "Entity", eitityId);
    }
    List<Map<String, Object>> attributeVariants = (List<Map<String, Object>>) entityJSONObject.get("attributeVariants");
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_ENTITY_QUERY);
        
        preparedStatement.setString(1, (String) entityJSONObject.get("baseType"));
        preparedStatement.setString(2, (String) eitityId);
        preparedStatement.setString(3, (String) entityJSONObject.get("branchOf"));
        preparedStatement.setLong(4, contextiids[0]);
        preparedStatement.setString(5, (String) entityJSONObject.get("createdBy"));
        preparedStatement.setLong(6, (Long) entityJSONObject.get("createdOn"));
        preparedStatement.setString(7, (String) entityJSONObject.get("creationLanguage"));
        preparedStatement.setString(8, (String) entityJSONObject.get("defaultAssetInstanceId"));
        preparedStatement.setString(9, (String) entityJSONObject.get("endpointId"));
        preparedStatement.setObject(10, entityJSONObject.get("hiddenSummary"));
        preparedStatement.setBoolean(11, (Boolean) entityJSONObject.get("isEmbedded"));
        Object isMerged = entityJSONObject.get("isMerged");
        preparedStatement.setBoolean(12, isMerged == null ? false : (Boolean) isMerged);
        preparedStatement.setString(13, (String) entityJSONObject.get("jobId"));
        preparedStatement.setString(14, (String) entityJSONObject.get("klassInstanceId"));
        preparedStatement.setArray(15, currentConn.newStringArray((List<String>) entityJSONObject.get("languageCodes")));
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("languageInstances", entityJSONObject.get("languageInstances"));
        preparedStatement.setObject(16, jSONObject.toJSONString());
        preparedStatement.setLong(17, (Long) entityJSONObject.get("lastModified"));
        preparedStatement.setString(18, (String) entityJSONObject.get("lastModifiedBy"));
        preparedStatement.setString(19, (String) entityJSONObject.get("logicalCatalogId"));
        preparedStatement.setObject(20, entityJSONObject.get("messages"));
        preparedStatement.setString(21, (String) entityJSONObject.get("name"));
        preparedStatement.setString(22, (String) entityJSONObject.get("organizationId"));
        preparedStatement.setString(23, (String) entityJSONObject.get("originalInstanceId"));
        preparedStatement.setString(24, (String) entityJSONObject.get("owner"));
        preparedStatement.setString(25, (String) entityJSONObject.get("parentId"));
        jSONObject.clear();
        jSONObject.put("partnerSources", entityJSONObject.get("partnerSources"));
        preparedStatement.setObject(26, jSONObject.toJSONString());
        preparedStatement.setArray(27, currentConn.newStringArray((List<String>) entityJSONObject.get("path")));
        preparedStatement.setString(28, (String) entityJSONObject.get("physicalCatalogId"));
        jSONObject.clear();
        jSONObject.put("referenceConflictingValues", entityJSONObject.get("referenceConflictingValues"));
        preparedStatement.setObject(29, jSONObject.toJSONString());
        jSONObject.clear();
        jSONObject.put("relationshipConflictingValues", entityJSONObject.get("relationshipConflictingValues"));
        preparedStatement.setObject(30, jSONObject.toJSONString());
        jSONObject.clear();
        jSONObject.put("roles", entityJSONObject.get("roles"));
        preparedStatement.setObject(31, jSONObject.toJSONString());
        jSONObject.clear();
        jSONObject.put("ruleViolation", entityJSONObject.get("ruleViolation"));
        preparedStatement.setObject(32, jSONObject.toJSONString());
        preparedStatement.setString(33, (String) entityJSONObject.get("saveComment"));
        preparedStatement.setArray(34, currentConn.newStringArray((List<String>) entityJSONObject.get("selectedTaxonomyIds")));
        preparedStatement.setObject(35, entityJSONObject.get("summary"));
        preparedStatement.setString(36, (String) entityJSONObject.get("systemId"));
        jSONObject.clear();
        jSONObject.put("taxonomyConflictingValues", entityJSONObject.get("taxonomyConflictingValues"));
        preparedStatement.setObject(37, jSONObject.toJSONString());
        preparedStatement.setArray(38, currentConn.newStringArray((List<String>) entityJSONObject.get("taxonomyIds")));
        preparedStatement.setArray(39, currentConn.newStringArray((List<String>) entityJSONObject.get("types")));
        preparedStatement.setString(40, (String) entityJSONObject.get("variantId"));
        preparedStatement.setArray(41, currentConn.newStringArray((List<String>) entityJSONObject.get("variants")));
        preparedStatement.setString(42, String.valueOf(entityJSONObject.get("versionId")));
        preparedStatement.setString(43, (String) entityJSONObject.get("versionOf"));
        preparedStatement.setLong(44, (Long) entityJSONObject.get("versionTimestamp"));
        jSONObject.clear();
        jSONObject.put("attributeVariants", attributeVariants); 
        preparedStatement.setObject(45, jSONObject.toJSONString());
        jSONObject.clear();
        jSONObject.put("assetInformation", entityJSONObject.get("assetInformation")); 
        preparedStatement.setObject(46, jSONObject.toJSONString());
        preparedStatement.setString(47, (String) entityJSONObject.get("copyOf"));
        preparedStatement.executeUpdate();
        currentConn.commit();
      });
      RDBMSLogger.instance().info("Entity inserted : " + eitityId);
     
  }

  private void addAttributeVariantsData(String index, Map<String, Object> documentDataMap) 
  {
    try {
      Object object = documentDataMap.get("attributeVariants");
      List<Map<String, Object>> attributeVariants = object != null
          ? (ArrayList<Map<String, Object>>) documentDataMap.get("attributeVariants")
          : new ArrayList<Map<String, Object>>();
      if (!attributeVariants.isEmpty()) {
        List<Map<String, Object>> attributeVariantDataMapList =  new ArrayList<Map<String, Object>>();
        for (Map<String, Object> attributeVariant : attributeVariants) {
          String id = (String) attributeVariant.get("id");
          Map<String, Object> attributeCacheDocument = RequestHandler.getDocumentById(index, "attributecache", id);
          attributeVariantDataMapList.add(attributeCacheDocument);
        }
        //putting here to insert it into value records table
        ((List<Map<String, Object>>) documentDataMap.get("attributes")).addAll(attributeVariantDataMapList);
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().warn("failed wahile fatching attributeVariants data for id :"+ documentDataMap.get("id"));
      e.printStackTrace();
    }
  }
  
  /**
   * Store Task data into RDBMS staging database.
   * @param index
   * @param batchCount
   * @throws Exception
   */
  void migrateTaskData(String index, Long batchCount) throws Exception
  {
    // TODO Auto-generated method stub
    List<String> taskDocTypes = new ArrayList<>();
    taskDocTypes.add("taskinstancecache");
    
    try {
      ScrollUtils.scrollThrough(this::taskFunctionalityInScroll, RequestHandler.getRestClient(), Arrays.asList(index),
          QueryBuilders.matchAllQuery(), taskDocTypes, batchCount, batchSize.intValue());
      batchCount++;
    }
    catch (Exception e) {
      updateBatchCount("taskbatchcount", batchCount, index);
      RDBMSLogger.instance().warn("Error while migrating Task Data : " + e.getMessage());
      throw e;
    }
    updateBatchCount("taskbatchcount", batchCount, index);
  }
  
  
  private String taskFunctionalityInScroll(SearchHit[] searchHits)
  {
    try {
      for (SearchHit hit : searchHits) {
        Map<String, Object> taskInstance = hit.getSourceAsMap();
        Boolean isCamundaCreated = (Boolean)taskInstance.get("isCamundaCreated");          
        List<Map<String, Object>>linkedEntities = (List<Map<String, Object>>) taskInstance.get("linkedEntities");          
        String contentId = (String) linkedEntities.get(0).get("contentId");
        prepareTaskData(taskInstance, isCamundaCreated, true, null, contentId);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private void prepareTaskData(Map<String, Object> taskInstance, Boolean isCamundaCreated,
      Boolean isparent, String parentTaskId, String contentId)
  {
    String id = (String) taskInstance.get("id");
    String status = getTagValue(taskInstance, "status");
    if (isCamundaCreated && status != null && !status.equals("taskdone")) {
      return;
    }
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_TASK_QUERY);
        preparedStatement.setString(1, id);
        List<String>types = (List<String>) taskInstance.get("types");
        preparedStatement.setString(2, (String) types.get(0));
        preparedStatement.setString(3, (String) taskInstance.get("name"));
        preparedStatement.setString(4, (String) taskInstance.get("longDescription"));
        preparedStatement.setLong(5, taskInstance.get("createdOn") == null ? 0 : (Long) taskInstance.get("createdOn"));
        preparedStatement.setLong(6, taskInstance.get("startDate") == null ? 0 : (Long) taskInstance.get("startDate"));
        preparedStatement.setLong(7, taskInstance.get("dueDate") == null ? 0 : (Long) taskInstance.get("dueDate"));
        preparedStatement.setLong(8, taskInstance.get("overDueDate") == null ? 0 : (Long) taskInstance.get("overDueDate"));          
        preparedStatement.setBoolean(9, isCamundaCreated);
        preparedStatement.setString(10, (String) taskInstance.get("camundaProcessDefinationId"));
        preparedStatement.setString(11, (String) taskInstance.get("camundaTaskInstanceId"));
        preparedStatement.setString(12, (String) taskInstance.get("camundaProcessInstanceId"));
        List<String> attachments = (List<String>) taskInstance.get("attachments");
        Set<Long> lids = getAttachmentIds(attachments, currentConn);
        preparedStatement.setArray(13, currentConn.newIIDArray(lids));
        JSONObject jSONObject = new JSONObject();
        List<Map<String, Object>>linkedEntities = (List<Map<String, Object>>) taskInstance.get("linkedEntities");          
        Map<String, Object> position = (Map<String, Object>) linkedEntities.get(0).get("position");
        String elementId = (String) linkedEntities.get(0).get("elementId");
        Integer x = (Integer) position.get("x");
        Integer y = (Integer) position.get("y");
        if (x != -1 || y != -1) {
          jSONObject.put("x", x);
          jSONObject.put("y", y);
          preparedStatement.setString(20, IStandardConfig.StandardProperty.assetcoverflowattribute.toString());
        }
        else if (elementId != null && !elementId.trim().isEmpty()) {
          preparedStatement.setString(20, getPropertyId(elementId, currentConn));
        }
        else {
          preparedStatement.setString(20, null);
        }
        preparedStatement.setObject(14, jSONObject.toJSONString());
        jSONObject.clear();
        List<Map<String, Object>> comments = new ArrayList<>();
        List<Map<String, Object>>readComments = (List<Map<String, Object>>) taskInstance.get("comments");
        for(Map<String, Object> readComment : readComments) {
          Map<String, Object> comment = new HashMap<>();
          comment.put("text", readComment.get("text"));
          comment.put("userName", getUserIDFromUserName((String)readComment.get("postedBy"), currentConn));
          comment.put("time", Long.parseLong((String)readComment.get("timestamp")));
          attachments = (List<String>) readComment.get("attachments");
          comment.put("attachments", getAttachmentIds(attachments, currentConn));
          comments.add(comment);
        }
        jSONObject.put("comments", comments);
        preparedStatement.setObject(15, jSONObject.toJSONString());
        preparedStatement.setString(16, parentTaskId);
        if (isparent) {
          preparedStatement.setString(16, null);
          List<Map<String, Object>> subtasks =  (List<Map<String, Object>>) taskInstance.get("subTasks");
          if (!subtasks.isEmpty()) {
            for (Map<String, Object> subtask: subtasks) {
              prepareTaskData(subtask, false, false, id, contentId);
            }
          }
        }
        preparedStatement.setString(17, getTagValue(taskInstance, "priority"));    
        preparedStatement.setString(18, contentId);
        preparedStatement.setString(19, status);
        preparedStatement.executeUpdate();
        currentConn.commit();
        insertTaskUserRoleLinkData(taskInstance);
      });
    }
    catch (RDBMSException e) {
      e.printStackTrace();
      RDBMSLogger.instance().warn("Task insertion failed, Task Id: " + id);
    }
  }

  private String  getTagValue(Map<String, Object> taskInstance, String label)
  {
    String tagValue = null;
    Map<String, Object> object = (Map<String, Object>)taskInstance.get(label);
    if (object != null) {
      List<Map<String, String>>tagValues = (List<Map<String, String>>) object.get("tagValues");
      if (!tagValues.isEmpty()) {
        tagValue = (String) tagValues.get(0).get("tagId");      
      }
    }    
    return tagValue;
  }
  
  /**Store Task User And Role link into RDBMS staging database.
   * @param taskInstance
   */
  private void insertTaskUserRoleLinkData(Map<String, Object> taskInstance)
  {
    String taskId = (String) taskInstance.get("id");
    try {
      
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement preparedStatementForUserIds = currentConn.prepareStatement(INSERT_TASK_USER_LINK_QUERY);
        PreparedStatement preparedStatementForRoleIds = currentConn.prepareStatement(INSERT_TASK_ROLE_LINK_QUERY);
        
        //For RESPONSIBLE
        insertUserIdsAndRoleIdsLink(taskInstance, taskId, preparedStatementForUserIds,
            preparedStatementForRoleIds, "responsible", RACIVS.RESPONSIBLE);
        
      //For ACCOUNTABLE
        insertUserIdsAndRoleIdsLink(taskInstance, taskId, preparedStatementForUserIds,
            preparedStatementForRoleIds, "accountable", RACIVS.ACCOUNTABLE);
        
      //For CONSULTED
        insertUserIdsAndRoleIdsLink(taskInstance, taskId, preparedStatementForUserIds,
            preparedStatementForRoleIds, "consulted", RACIVS.CONSULTED);
        
      //For INFORMED
        insertUserIdsAndRoleIdsLink(taskInstance, taskId, preparedStatementForUserIds,
            preparedStatementForRoleIds, "informed", RACIVS.INFORMED);
        
      //For VERIFIER
        insertUserIdsAndRoleIdsLink(taskInstance, taskId, preparedStatementForUserIds,
            preparedStatementForRoleIds, "verify", RACIVS.VERIFIER);
        
      //For SIGNOFF
        insertUserIdsAndRoleIdsLink(taskInstance, taskId, preparedStatementForUserIds,
            preparedStatementForRoleIds, "signoff", RACIVS.SIGNOFF);
        
        
      preparedStatementForUserIds.executeBatch();
      preparedStatementForRoleIds.executeBatch();
      currentConn.commit();
    });
      RDBMSLogger.instance().warn("taskuseridlink & taskroleidlink batch insertion successful, Document Id: " + taskId);
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().warn("taskuseridlink & taskroleidlink batch insertion failed, Document Id: " + taskId);
    }
  }

  private void insertUserIdsAndRoleIdsLink(Map<String, Object> taskInstance, String taskId,
      PreparedStatement preparedStatementForUserIds, PreparedStatement preparedStatementForRoleIds,
      String racivslabel, RACIVS racivsvalue) throws SQLException
  {
    Map<String, List<String>> userIdsRoleIdsList = (Map<String, List<String>>) taskInstance.get(racivslabel);        
    Set<String> userIds = new HashSet<String>(userIdsRoleIdsList.get("userIds"));
    for(String userId: userIds) {
      preparedStatementForUserIds.setString(1, taskId);
      preparedStatementForUserIds.setString(2, userId);          
      preparedStatementForUserIds.setInt(3, racivsvalue.ordinal());
      preparedStatementForUserIds.addBatch();
    }
    Set<String> roleIds = new HashSet<String>(userIdsRoleIdsList.get("roleIds"));
    for(String roleId: roleIds) {
      preparedStatementForRoleIds.setString(1, taskId);
      preparedStatementForRoleIds.setString(2, roleId);          
      preparedStatementForRoleIds.setInt(3, racivsvalue.ordinal());
      preparedStatementForRoleIds.addBatch();
    }
  }
  
  private String getUserIDFromUserName(String username, RDBMSConnection currentConn)
  {
    try {
      PreparedStatement statement = currentConn.prepareStatement(GET_USER_ID_BY_NAME);
      statement.setString(1, username);
      ResultSet executeQuery = statement.executeQuery();
      if (executeQuery.next()) {
        String userId = executeQuery.getString(1);
        return userId;
      }
    }
    catch (SQLException | RDBMSException e) {
      RDBMSLogger.instance().warn("Task Instance Insertion failed due to UserId::" + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }
  
  private String getPropertyId(String instanceId, RDBMSConnection currentConn)
  {
    try {
      PreparedStatement statement = currentConn.prepareStatement(GET_PROPERTY_CODE);
      statement.setString(1, instanceId);
      ResultSet executeQuery = statement.executeQuery();
      if (executeQuery.next()) {
        String propertyId = executeQuery.getString(1);
        return propertyId;
      }
    }
    catch (SQLException | RDBMSException e) {
      RDBMSLogger.instance().warn("Task Instance Insertion failed due to propertyId::" + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }
  
  private Set<Long> getAttachmentIds(List<String> readAttachmentIds, RDBMSConnection currentConn)
  {
    
    StringBuilder query = new StringBuilder();
    query.append(String.format("select baseentityiid from staging.baseentity where id in (%s)", Text.join(",", readAttachmentIds, "'%s'")));
    Set<Long> attachmentIds = new HashSet<>();
    try {
      if (!readAttachmentIds.isEmpty()) {
        PreparedStatement statement = currentConn.prepareStatement(query.toString());
        ResultSet executeQuery = statement.executeQuery();
        while (executeQuery.next()) {
          attachmentIds.add(executeQuery.getLong(1));
        }
      }
    }
    catch (RDBMSException | SQLException e) {
      RDBMSLogger.instance().warn("Task Instance Insertion failed due to attachments::" + e.getMessage());
      e.printStackTrace();
    }    
    return attachmentIds;
  }
  
  //Prepare Helper table to identify incompleted taskInstanceIds
  public static final String INSERT_TASK_INPROGRESS_QUERY = "INSERT INTO staging.inprogresstaskinstances VALUES (?)";  
  private void prepareInprogressTaskInatanceList() throws CSInitializationException, SQLException
  {
    MigrationProperties migrationPropertiesInstance = MigrationProperties.instance();
    Connection connection = connnectESPostgresDb(migrationPropertiesInstance);
    String getInprogressTasks = "Select id_ from public.act_ru_task";
    PreparedStatement selectStmt = connection.prepareStatement(getInprogressTasks);
    ResultSet result = selectStmt.executeQuery();
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement prepareStatement = currentConn.prepareStatement(INSERT_TASK_INPROGRESS_QUERY);
        while (result.next()) {
          prepareStatement.setString(1, result.getString(1));
          prepareStatement.addBatch();
        }
        prepareStatement.executeBatch();
        currentConn.commit();
      });
    }
    catch (RDBMSException e) {
      e.printStackTrace();
    }
    connection.close();
  }

  /**
   * Connect with elastic search postgres database
   * @param migrationPropertiesInstance 
   * @return
   * @throws CSInitializationException
   * @throws SQLException
   */
  private Connection connnectESPostgresDb(MigrationProperties migrationPropertiesInstance) throws CSInitializationException, SQLException
  {
    String esPostgresIp = migrationPropertiesInstance.getString("postgres.host");
    String esPostgresPort = migrationPropertiesInstance.getString("postgres.port");
    String esPostgresDbName = migrationPropertiesInstance.getString("postgres.dbname");
    String esPostgresUsername = migrationPropertiesInstance.getString("postgres.username");
    String esPostgresPassword = migrationPropertiesInstance.getString("postgres.password");
    
    String url = "jdbc:postgresql://" + esPostgresIp + ":" + esPostgresPort + "/" + esPostgresDbName;
    Connection connection = DriverManager.getConnection(url, esPostgresUsername, esPostgresPassword);
    
    System.out.println("Connected with database jdbc:postgresql://localhost:5432/camundadb");
    return connection;
  }
}
