package com.cs.dam.runtime.migration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.usecase.migration.IMigrateAssetPurgeRecords;
import com.cs.core.data.Text;
import com.cs.core.data.TextArchive;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class MigrateAssetPurgeRecords extends AbstractRuntimeService<IVoidModel, IVoidModel>
    implements IMigrateAssetPurgeRecords {
  
  static final String ALTER_OBJECT_REVISION = "alter table pxp.objectrevision add column IF NOT EXISTS assetobjectkey varchar";
  static final String ALTER_BASE_ENTITY_ARCHIVE = "alter table pxp.baseentityarchive add column IF NOT EXISTS assetobjectkey varchar";
  static final String GET_BASE_ENTITY_ARCHIVE = "select objectarchive from pxp.baseentityarchive where entityiid in (?)";
  static final String UPDATE_BASE_ENTITY_ARCHIVE_RECORD = "update pxp.baseentityarchive set assetobjectkey = ? where entityiid = ?";
  static final String GET_OBJECT_REVISION = "select objectarchive from pxp.objectrevision where objectiid in (?)";
  static final String UPDATE_OBJECT_REVISION_RECORD = "update pxp.objectrevision set assetobjectkey = ? where objectiid = ?";
  static final String CREATE_ASSETS_TO_BE_PURGED_TABLE = "create table pxp.assetstobepurged (assetobjectkey character varying,"
      + "thumbkey character varying,previewimagekey character varying,type character varying) ";
  
  int batchSize = 100;
 
  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement baseEntityArchiveAlterQuery = currentConn.prepareStatement(ALTER_BASE_ENTITY_ARCHIVE);
      baseEntityArchiveAlterQuery.executeUpdate();
      PreparedStatement objectRevisionAlterQuery = currentConn.prepareStatement(ALTER_OBJECT_REVISION);
      objectRevisionAlterQuery.executeUpdate();
      PreparedStatement functionUpdateQuery = currentConn.prepareStatement(getFunctionUpdateQuery());
      functionUpdateQuery.executeUpdate();
      PreparedStatement createAssetsToBePurgedTableQuery = currentConn.prepareStatement(CREATE_ASSETS_TO_BE_PURGED_TABLE);
      createAssetsToBePurgedTableQuery.executeUpdate();
      Set<String> baseentityiids = new HashSet<String>();
      try {
        //Get baseentityiids from elastic assetinstancearchivecache.
        baseentityiids.addAll(getAllBaseentityiidsFromIndex("assetinstancearchivecache"));
        //Update baseentityarchive table assetobjectkey column values based on entityiid.
        updateAssetObjectKeyValueForTable(currentConn, baseentityiids, GET_BASE_ENTITY_ARCHIVE, UPDATE_BASE_ENTITY_ARCHIVE_RECORD);
        //Add all baseentityiids from elastic assetinstancecache to elastic assetinstancearchivecache.
        baseentityiids.addAll(getAllBaseentityiidsFromIndex("assetinstancecache"));
        //Update objectrevision table assetobjectkey column values based on objectiid.
        updateAssetObjectKeyValueForTable(currentConn, baseentityiids, GET_OBJECT_REVISION, UPDATE_OBJECT_REVISION_RECORD);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    });
    
    return null;
  }

  private Set<String> getAllBaseentityiidsFromIndex(String index) throws Exception {
    return scrollThrough(ElasticServiceDAS.instance().initialize(), Arrays.asList(index), QueryBuilders.matchAllQuery());    
  }
  
  private void updateAssetObjectKeyValueForTable(RDBMSConnection currentConn, Set<String> baseentityiids, String getQuery, String updateQuery) 
      throws Exception {
    int sizeOfIIDs = baseentityiids.size();
    for (float iidIterator = 0; iidIterator < (float) sizeOfIIDs / batchSize; iidIterator++) {
      String queryWithIIDs = getQuery.replace("?", Text.join(",", baseentityiids.stream().skip((int) iidIterator*batchSize).limit(batchSize).collect(Collectors.toSet())));
      PreparedStatement baseEntityArchiveGetQuery = currentConn.prepareStatement(queryWithIIDs);
      RDBMSAbstractDriver driver = currentConn.getDriver();
      IResultSetParser getBaseEntityArchiveResult = driver.getResultSetParser(baseEntityArchiveGetQuery.executeQuery());
      while (getBaseEntityArchiveResult.next()) {
        String jsonContent = TextArchive.unzip(getBaseEntityArchiveResult.getBinaryBlob("objectarchive")).trim();
        BaseEntityDTO entity = new BaseEntityDTO();
        entity.fromPXON(new JSONContentParser(jsonContent));
        IJSONContent entityExtension = entity.getEntityExtension();
        String assetObjectKey = entityExtension.getInitField("assetObjectKey", "");
        PreparedStatement baseEntityArchiveUpdateQuery = currentConn.prepareStatement(updateQuery);
        baseEntityArchiveUpdateQuery.setString(1, assetObjectKey);
        baseEntityArchiveUpdateQuery.setLong(2, entity.getBaseEntityIID());
        baseEntityArchiveUpdateQuery.executeUpdate();
      }
    }
  }
  
  private Set<String> scrollThrough(RestHighLevelClient client, List<String> indices, QueryBuilder query) throws IOException
  {
    Set<String> baseEntityIIDs = new HashSet<String>();
    final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
    SearchRequest searchRequest = new SearchRequest(indices.toArray(String[]::new));
    searchRequest.scroll(scroll);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(query);
    searchRequest.source(searchSourceBuilder);

    //max of 1000 hits will be returned for each scroll
    //Scroll until no hits are returned

    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    String scrollId = searchResponse.getScrollId();
    SearchHit[] searchHits = searchResponse.getHits().getHits();

    while (searchHits != null && searchHits.length > 0) {
      for(SearchHit hit : searchHits){
        baseEntityIIDs.add(hit.getId());
      }

      //new Scroll Request
      SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
      scrollRequest.scroll(scroll);
      searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
      scrollId = searchResponse.getScrollId();
      searchHits = searchResponse.getHits().getHits();
    }
    
    return baseEntityIIDs;
  }
  
  private String getFunctionUpdateQuery() {
    
    String query = "CREATE OR REPLACE FUNCTION pxp.fn_createobjectrevision(\r\n" + 
        "  p_objectiid bigint,\r\n" + 
        "  p_classifieriid bigint,\r\n" + 
        "  p_created bigint,\r\n" + 
        "  p_comment character varying,\r\n" + 
        "  p_trackiid bigint,\r\n" + 
        "  p_timeline json,\r\n" + 
        "  p_archive bytea,\r\n" + 
        "  p_isarchived boolean,\r\n" + 
        "  p_assetobjectkey character varying)\r\n" + 
        "    RETURNS integer\r\n" + 
        "    LANGUAGE 'plpgsql'\r\n" + 
        "\r\n" + 
        "    COST 100\r\n" + 
        "    VOLATILE \r\n" + 
        "AS $BODY$ declare\r\n" + 
        "    vNewRevisionNo integer;\r\n" + 
        "begin\r\n" + 
        "    begin\r\n" + 
        "        select max(revisionNo) + 1 into vNewRevisionNo from pxp.objectRevision\r\n" + 
        "        where objectIID = p_ObjectIID and revisionNo >= 0;\r\n" + 
        "    exception\r\n" + 
        "        when no_data_found then vNewRevisionNo := null;\r\n" + 
        "    end;\r\n" + 
        "    if ( vNewRevisionNo is null ) then\r\n" + 
        "        vNewRevisionNo := 0;\r\n" + 
        "    end if;\r\n" + 
        "    insert into pxp.ObjectRevision (objectIID, classifierIID, created, revisionNo, revisionComment, trackIID, revisionTimeline, objectArchive, isArchived, assetObjectkey)\r\n" + 
        "        values ( p_ObjectIID, p_classifierIID, p_created, vNewRevisionNo, p_comment, p_trackIID, p_timeline, p_archive, p_isArchived, p_assetobjectkey);\r\n" + 
        "    return vNewRevisionNo;\r\n" + 
        "end\r\n" + 
        "$BODY$;\r\n" + 
        "\r\n" + 
        "ALTER FUNCTION pxp.fn_createobjectrevision(bigint, bigint, bigint, character varying, bigint, json, bytea, boolean, character varying)\r\n" + 
        "    OWNER TO pxp;";
    
    return query;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
