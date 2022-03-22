package com.cs.api.estordbmsmigration.interactor.migration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.cs.api.estordbmsmigration.services.RequestHandler;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.elastic.Index;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class MigrationOfArchivedFromEsToRdbms extends AbstractRuntimeService<IVoidModel, IVoidModel>  implements IMigrationOfArchivedFromEsToRdbms {
  
  protected static final List<String> docTypes                   = Arrays.asList("klassinstancecache", "assetinstancecache", "targetinstancemarketcache",
      "virtualcataloginstancecache", "textassetinstancecache", "supplierinstancecache");
  
  protected static final String       separator                  = "__";
 
  protected static Long               batchSize                  = 20L;

  protected static final String GET_ENTITY_BY_ID = "select baseEntityIID, lastmodifiedby, creationlanguage, physicalcatalogid, organizationid from staging.baseEntity where id = ?";
  protected static final String GET_USER_BY_ID = "Select userIID from staging.helper_userconfig where userid =  ?";

  
  @Override
  protected IVoidModel executeInternal(IVoidModel model) throws Exception
  {
    String index = "csarchive";
    
    for (String docType : docTypes) {
      Set<String> archivedInstanceIds = new HashSet<>();
      Integer totalCount;
      Long from = 0L;
      try {
        totalCount = RequestHandler.getTotalCount(index, docType);
        while (from < totalCount) {
          List<Map<String, Object>> documents = RequestHandler.getDocumentsFromServer(index, docType, from, batchSize);
          for (Map<String, Object> documentDataMap : documents) {
            archivedInstanceIds.add((String) documentDataMap.get("_id"));
          }                                                  
          from = from + batchSize;
        }
        
        for(String entityIID : archivedInstanceIds) {
          
         try {
           
           Map<String, Object> entityIIDFromKlassInstanceId = getEntityInfoFromKlassInstanceId(entityIID);
           String userName = (String) entityIIDFromKlassInstanceId.get("userName");
           String baseLocaleId = (String) entityIIDFromKlassInstanceId.get("baseLocaleId");
           String catalogCode = (String) entityIIDFromKlassInstanceId.get("catalogCode");
           String organizationCode = (String) entityIIDFromKlassInstanceId.get("organizationCode");
           Long baseEntityIID = (Long) entityIIDFromKlassInstanceId.get("baseEntityIID");
           
           UserDTO user = new UserDTO(getUserIIDFromUserName(userName), userName);
           UserSessionDTO userSession = new UserSessionDTO(new SessionDTO(), user, LogoutType.UNDEFINED, 0l, "");
           
           ILocaleCatalogDAO localeCatalogDao = new LocaleCatalogDAO(userSession, new LocaleCatalogDTO(baseLocaleId, new CatalogDTO(catalogCode, organizationCode)));
           
           BaseEntityDTO baseEntityDTO = (BaseEntityDTO) localeCatalogDao.getEntityByIID(baseEntityIID);
           
           IBaseEntityDAO baseEntityDao = localeCatalogDao.openBaseEntity(baseEntityDTO); 
           baseEntityDao.delete();
           
           Index elasticIndex = Index.getIndexByBaseType(baseEntityDTO.getBaseType());
           String id = String.valueOf(baseEntityIID);
           Map<String, Object> instance = ElasticServiceDAS.instance().getDocument(id, elasticIndex.name());
           if (instance != null) {
             Index archiveIndex = Index.getArchiveIndexByBaseType(baseEntityDTO.getBaseType());
             ElasticServiceDAS.instance().indexDocument(instance, archiveIndex.name(), id);
             ElasticServiceDAS.instance().deleteDocument(id, elasticIndex.name());
           }
           
         }catch(Exception e) {
           System.out.println("Failed EntityIID " +entityIID);
           e.printStackTrace();
         }
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return model;
  }
  
  private Map<String, Object> getEntityInfoFromKlassInstanceId(String archivedInstanceId) throws RDBMSException
  {
    Map<String, Object> baseEntityInfo = new HashMap<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(GET_ENTITY_BY_ID);
      statement.setString(1, archivedInstanceId);
      ResultSet executeQuery = statement.executeQuery();
      if (executeQuery.next())
        baseEntityInfo.put("baseEntityIID",executeQuery.getLong("baseEntityIID"));
        baseEntityInfo.put("userName",executeQuery.getString("lastmodifiedby"));
        baseEntityInfo.put("baseLocaleId",executeQuery.getString("creationlanguage"));
        baseEntityInfo.put("catalogCode",executeQuery.getString("physicalcatalogid"));
        baseEntityInfo.put("organizationCode",executeQuery.getString("organizationid"));
      
    });
    return baseEntityInfo;
  }
  
  private Long getUserIIDFromUserName(String username) throws RDBMSException
  {
    AtomicLong userIID = new AtomicLong();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(GET_USER_BY_ID);
      statement.setString(1, username);
      ResultSet executeQuery = statement.executeQuery();
      if (executeQuery.next())
        userIID.set(executeQuery.getLong("userIID"));
    });
    return userIID.get();
    
  }
}
