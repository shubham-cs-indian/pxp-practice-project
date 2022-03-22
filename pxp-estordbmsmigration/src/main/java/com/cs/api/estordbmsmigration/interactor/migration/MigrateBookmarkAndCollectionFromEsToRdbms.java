package com.cs.api.estordbmsmigration.interactor.migration;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.api.estordbmsmigration.services.MigrationProperties;
import com.cs.api.estordbmsmigration.services.RequestHandler;
import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.data.Text;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.collection.dao.CollectionDAS;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.CollectionDTO;
import com.cs.core.rdbms.entity.dto.CollectionDTO.CollectionDTOBuilder;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.dto.SimpleTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class MigrateBookmarkAndCollectionFromEsToRdbms extends AbstractService<IVoidModel, IVoidModel>
    implements IMigrateBookmarkAndCollectionFromEsToRdbms {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsUtils;
  
  protected static Long         batchSize = 20L;
  
  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws Exception
  {
    batchSize = MigrationProperties.instance()
        .getLong("batchsize");
    migrateBookmarksAndCollectionData();
    return null;
  }
  
  private void migrateBookmarksAndCollectionData()
  {
    String docType = "collectioncache";
    Map<String, Object> requestMap = new HashMap<String, Object>();
    requestMap.put("docTypes", Arrays.asList(docType));
    Integer totalCount;
    Long from = 0L;
    try {
      totalCount = RequestHandler.getTotalCount("cs", docType);
      while (from < totalCount) {
        List<Map<String, Object>> documents = RequestHandler.getDocumentsFromServer("cs", docType, from,
            batchSize);
        List<Map<String, Object>> bookmarks = new ArrayList<>();
        List<Map<String, Object>> collections = new ArrayList<>();
        segregateData(documents, bookmarks, collections);
        
        insertBookmarkData(bookmarks);
        insertCollectionData(collections);
        from = from + batchSize;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println(docType + " data fatching from :" + from + "failed.");
    }
  }
  
  private void segregateData(List<Map<String, Object>> documents,
      List<Map<String, Object>> bookmarks, List<Map<String, Object>> collections)
  {
    for (Map<String, Object> documentDataMap : documents) {
      Map<String, Object> esInstance = (Map<String, Object>) documentDataMap.get("_source");
      if (esInstance.get("type")
          .equals(CollectionType.dynamicCollection.toString())) {
        bookmarks.add(esInstance);
      }
      else {
        collections.add(esInstance);
      }
    }
  }
  
  private void insertBookmarkData(List<Map<String, Object>> bookmarks) throws Exception
  {
    for (Map<String, Object> bookmark : bookmarks) {
      validateBookmark(bookmark);
      Map<String, Object> getRequestMap = (Map<String, Object>) bookmark.get("getRequestModel");
      IJSONContent searchCriteria = new JSONContent(
          ObjectMapperUtil.writeValueAsString(getRequestMap));
      
      CollectionDTO collectionDTO = (CollectionDTO) new CollectionDTOBuilder(
          CollectionType.dynamicCollection, (String) bookmark.get("label"),
          (String) bookmark.get("portalId"), "")
              .parentIID(Long.parseLong((String) bookmark.get("parentId")))
              .isPublic((boolean) bookmark.get("isPublic"))
              .linkedBaseEntityIIDs(new HashSet<Long>())
              .searchCriteria(searchCriteria)
              .build();
      ISimpleTrackingDTO created = new SimpleTrackingDTO((String) bookmark.get("createdBy"),
          (Long) bookmark.get("createdOn"));
      collectionDTO.setCreatedTrack(created);
      RDBMSConnectionManager.instance()
          .runTransaction((RDBMSConnection currentConn) -> {
            CollectionDAS entityDAS = new CollectionDAS(currentConn);
            entityDAS.createCollectionForMigration(collectionDTO);
          });
    }
  }
  
  private void insertCollectionData(List<Map<String, Object>> collections) throws Exception
  {
    List<String> collectionIds = new ArrayList<>();
    Map<String, Long> newCollectionMappingTable = new HashMap<>();
    Map<String, Object> collectionIdMapping = new HashMap<>();
    newCollectionMappingTable.put("-1", -1L);
    for (Map<String, Object> collection : collections) {
      String id = (String) collection.get("id");
      collectionIds.add(id);
      collectionIdMapping.put(id, collection);
    }
    while (!collectionIds.isEmpty()) {
      for (int i = 0; i < collectionIds.size();) {
        String collectionId = collectionIds.get(i);
        Map<String, Object> collection = (Map<String, Object>) collectionIdMapping
            .get(collectionId);
        String parentId = (String) collection.get("parentId");
        if (newCollectionMappingTable.containsKey(parentId)) {
          List<String> klassInstanceIds = (List<String>) collection.get("klassInstanceIds");
          Set<Long> baseEntityIid = getBaseEntityIid(klassInstanceIds);
          CollectionDTO collectionDTO = (CollectionDTO) new CollectionDTOBuilder(
              CollectionType.staticCollection, (String) collection.get("label"),
              (String) collection.get("portalId"), "")
                  .parentIID(newCollectionMappingTable.get(parentId))
                  .isPublic((boolean) collection.get("isPublic"))
                  .linkedBaseEntityIIDs(baseEntityIid)
                  .searchCriteria(new JSONContent())
                  .build();
          ISimpleTrackingDTO created = new SimpleTrackingDTO((String) collection.get("createdBy"),
              (Long) collection.get("createdOn"));
          collectionDTO.setCreatedTrack(created);
          RDBMSConnectionManager.instance()
              .runTransaction((RDBMSConnection currentConn) -> {
                CollectionDAS entityDAS = new CollectionDAS(currentConn);
                Long collectionIid = entityDAS
                    .createCollectionForMigration((CollectionDTO) collectionDTO);
                newCollectionMappingTable.put(collectionId, collectionIid);
              });
          collectionIds.remove(i);
        }
        else {
          i++;
        }
      }
    }
  }
  
  private void validateBookmark(Map<String, Object> bookmark) throws RDBMSException
  {
    Map<String, Object> getRequestMap = (Map<String, Object>) bookmark.get("getRequestModel");
    List<Map<String, Object>> attributes = (List<Map<String, Object>>) getRequestMap
        .get("attributes");
    for (Map<String, Object> attribute : attributes) {
      String id = (String) attribute.get("id");
      attribute.put("id", getSelectedEntityId(id));
      String type = (String) attribute.get("type");
      String updatedType = null;
      if (type.contains("com.cs.config.interactor.entity.concrete.attribute.standard")) {
        updatedType = type.replace("com.cs.config.interactor.entity.concrete.attribute.standard",
            "com.cs.core.config.interactor.entity.standard.attribute");
      }
      else {
        updatedType = type.replace("com.cs.config.interactor.entity.concrete.attribute",
            "com.cs.core.config.interactor.entity.attribute");
      }
      if (updatedType != null) {
        attribute.put("type", updatedType);
      }
      
      List<Map<String, Object>> mandatories = (List<Map<String, Object>>) attribute
          .get("mandatory");
      for (Map<String, Object> mandatory : mandatories) {
        String baseType = (String) mandatory.get("baseType");
        mandatory.put("baseType",
            baseType.replace("com.cs.runtime.interactor.model.klassinstance.filter",
                "com.cs.core.runtime.interactor.model.filter"));
      }
    }
    
    List<Map<String, Object>> tags = (List<Map<String, Object>>) getRequestMap.get("tags");
    for (Map<String, Object> tag : tags) {
      String id = (String) tag.get("id");
      tag.put("id", getSelectedEntityId(id));
      List<Map<String, Object>> mandatories = (List<Map<String, Object>>) tag.get("mandatory");
      for (Map<String, Object> mandatory : mandatories) {
        String mandatoryId = (String) mandatory.get("id");
        mandatory.put("id", getSelectedEntityId(mandatoryId));
        String baseType = (String) mandatory.get("baseType");
        mandatory.replace("baseType",
            baseType.replace("com.cs.runtime.interactor.model.klassinstance.filter",
                "com.cs.core.runtime.interactor.model.filter"));
      }
    }
    
    List<Map<String, Object>> sortOptions = (List<Map<String, Object>>) getRequestMap
        .get("sortOptions");
    for (Map<String, Object> sortOption : sortOptions) {
      String sortField = (String) sortOption.get("sortField");
      sortOption.put("sortField", getSelectedEntityId(sortField));
    }
    
    getRequestMap.put("selectedTaxonomyIds",
        getSelectedEntityIds((List<String>) getRequestMap.get("selectedTaxonomyIds")));
    getRequestMap.put("klassIdsHavingRP",
        getSelectedEntityIds((List<String>) getRequestMap.get("klassIdsHavingRP")));
    getRequestMap.put("taxonomyIdsHavingRP",
        getSelectedEntityIds((List<String>) getRequestMap.get("taxonomyIdsHavingRP")));
    getRequestMap.put("selectedTypes",
        getSelectedEntityIds((List<String>) getRequestMap.get("selectedTypes")));
    getRequestMap.remove("majorTaxonomyIds");
  }
  
  private Set<Long> getBaseEntityIid(List<String> klassInstanceId) throws RDBMSException
  {
    Set<Long> baseEntityIIDs = new HashSet<Long>();
    if (klassInstanceId.isEmpty()) {
      return baseEntityIIDs;
    }
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          StringBuilder query = new StringBuilder();
          query
              .append(String.format("select baseentityiid from staging.baseentity where id in (%s)",
                  Text.join(",", klassInstanceId, "'%s'")));
          PreparedStatement stmt = connection.prepareStatement(query.toString());
          stmt.execute();
          IResultSetParser resultSet = connection.getResultSetParser(stmt.getResultSet());
          while (resultSet.next()) {
            baseEntityIIDs.add(resultSet.getLong("baseentityiid"));
          }
        });
    return baseEntityIIDs;
    
  }
  
  private List<String> getSelectedEntityIds(List<String> entityIds) throws RDBMSException
  {
    List<String> selectedEntityIds = new ArrayList<>();
    if (entityIds.isEmpty()) {
      return selectedEntityIds;
    }
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          StringBuilder query = new StringBuilder();
          query.append(String.format("select code from staging.helper_config where cid in (%s)",
              Text.join(",", entityIds, "'%s'")));
          PreparedStatement stmt = connection.prepareStatement(query.toString());
          stmt.execute();
          IResultSetParser resultSet = connection.getResultSetParser(stmt.getResultSet());
          while (resultSet.next()) {
            selectedEntityIds.add(resultSet.getString("code"));
          }
        });
    return selectedEntityIds;
    
  }
  
  private String getSelectedEntityId(String entityId) throws RDBMSException
  {
    String[] entityCode = { "" };
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          StringBuilder query = new StringBuilder();
          query.append(
              String.format("select code from staging.helper_config where cid = '%s'", entityId));
          PreparedStatement stmt = connection.prepareStatement(query.toString());
          stmt.execute();
          IResultSetParser resultSet = connection.getResultSetParser(stmt.getResultSet());
          if (resultSet.next()) {
            entityCode[0] = resultSet.getString("code");
          }
        });
    return entityCode[0];
    
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
