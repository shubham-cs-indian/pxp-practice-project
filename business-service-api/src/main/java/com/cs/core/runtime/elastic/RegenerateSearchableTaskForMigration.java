package com.cs.core.runtime.elastic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.elastic.Index;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.elastic.ibuilders.ISearchBuilder;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.elastic.utils.ElasticSyncUtil;

@Component
@Scope("prototype")
public class RegenerateSearchableTaskForMigration implements  Runnable {

  
  private List<Long> iids;
  
  private ILocaleCatalogDAO localeCatalogDAO;
  
  public void setData(List<Long> baseEntityIIds,ILocaleCatalogDAO localeCatalogDAO)
  {
    this.iids = baseEntityIIds;
    this.localeCatalogDAO = localeCatalogDAO;
  }
  
  @Override
  public void run()
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver)RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        execute();
      }
    });
  }

  private void execute()
  {
    try {
      long start = System.currentTimeMillis();
      List<String> iidsAsString = iids.stream().map((Object s) -> String.valueOf(s)).collect(Collectors.toList());
      List<IBaseEntityDTO> baseEntities = localeCatalogDAO.getBaseEntitiesByIIDs(iidsAsString);
      Map<Long, List<Long>> allChildrenIIDsForEntities = localeCatalogDAO.getAllChildrenIIDsForEntities(iids);
      
      List<IndexRequest> indexRequests = new ArrayList<>();
      for (IBaseEntityDTO baseEntity : baseEntities) {
        long iid = baseEntity.getBaseEntityIID();
        try {
          List<Long> childrenIIDs = allChildrenIIDsForEntities.computeIfAbsent(iid,K-> new ArrayList<>());
          
          Map<String, Object> instance = new HashMap<>();
          
          ElasticSyncUtil.fillFlatLevelProperties(instance, baseEntity);
          ElasticSyncUtil.fillClassifiers(instance, localeCatalogDAO, baseEntity);
          ElasticSyncUtil.fillColorViolations(instance, localeCatalogDAO, iid);
          ElasticSyncUtil.fillCollectionId(instance, localeCatalogDAO, iid);
          fillAttributesAndTags(instance, localeCatalogDAO, childrenIIDs, baseEntity);
          
          Index index = Index.getIndexByBaseType(baseEntity.getBaseType());
          IndexRequest indexRequest = ElasticServiceDAS.instance().indexRequest(instance, index.name(),  String.valueOf(iid));
          indexRequests.add(indexRequest);
        }
        catch (Exception e) {
          RDBMSLogger.instance().info("Searchable generation failed baseentity iid:" + iid);
          RDBMSLogger.instance().exception(e);
        }
      }
      
      if(!indexRequests.isEmpty()) {
        ElasticServiceDAS.instance().bulkRequest(indexRequests);
      }
      
      RDBMSLogger.instance().info("Regenerate Searchable ||  Time for batch completion : %d ms",  System.currentTimeMillis() - start);
    }
    catch(Exception e) {
      RDBMSLogger.instance().info("Searchable generation failed for :" + iids.toString());
      RDBMSLogger.instance().exception(e);
    }
  }
  
  private void fillAttributesAndTags(Map<String, Object> instance,
      ILocaleCatalogDAO localeCatalogDAO, List<Long> childrenIIDs, IBaseEntityDTO baseEntity)
      throws RDBMSException
  {
    Long baseEntityIID = baseEntity.getBaseEntityIID();
    List<Long> allEntityIIDs = new ArrayList<>(childrenIIDs);
    allEntityIIDs.add(baseEntityIID);
    Long[] entityIIDs = allEntityIIDs.toArray(new Long[0]);
    
    Map<Long, List<IValueRecordDTO>> iidVSvalueRecords = localeCatalogDAO.getAllValueRecords(entityIIDs);
    Map<Long, List<ITagsRecordDTO>> iidVStagsRecords = localeCatalogDAO.getAllTagsRecords(entityIIDs);
    
    Set<Long> iids = new HashSet<>(iidVSvalueRecords.keySet());
    iids.addAll(iidVStagsRecords.keySet());
    
    List<Map<String, Object>> propertyObjects = new ArrayList<>();
    instance.put(ISearchBuilder.Fields.propertyObjects.name(), propertyObjects);
    
    for (Long iid : iids) {
      
      Map<String, Object> propertyObject = new HashMap<>();
      propertyObject.put(ISearchBuilder.Fields.identifier.name(), getIdentifier(iid, baseEntityIID));
      propertyObjects.add(propertyObject);
      
      Map<String, Object> attributes = new HashMap<>();
      Map<String, List<String>> tags = new HashMap<>();
      propertyObject.put(ISearchBuilder.Fields.attribute.name(), attributes);
      propertyObject.put(ISearchBuilder.Fields.tag.name(), tags);
      
      Map<String, Object> independentAttributes = new HashMap<>();
      attributes.put(ISearchBuilder.Fields.independent.name(), independentAttributes);
      ElasticSyncUtil.fillFlatLevelAttributes(independentAttributes, baseEntity);

      List<IValueRecordDTO> valueRecords = iidVSvalueRecords.get(iid);
      if (valueRecords != null) {
        fillAttributes(attributes, valueRecords);
      }
      
      List<ITagsRecordDTO> tagsRecords = iidVStagsRecords.get(iid);
      if (tagsRecords != null) {
        fillTags(tags, tagsRecords);
      }
    }
  }
  
  private void fillTags(Map<String, List<String>> tags, List<ITagsRecordDTO> tagsRecords)
  {
    for (ITagsRecordDTO record : tagsRecords) {
      List<String> tagValues = record.getTags().stream().map(ITagDTO::getTagValueCode).collect(Collectors.toList());
      tags.put(record.getProperty().getCode(), tagValues);
    }
  }

  @SuppressWarnings("unchecked")
  private void fillAttributes(Map<String, Object> attributes, List<IValueRecordDTO> valueRecords)
  {
    String key;
    Object value;
    Map<String, Object> independentAttributes = (Map<String, Object>) attributes.get(ISearchBuilder.Fields.independent.name());
    
    for (IValueRecordDTO record : valueRecords) {
      String localeId = record.getLocaleID();
      if (StringUtils.isEmpty(localeId)) {
        if (record.getProperty().isNumeric()) {
          if (IPropertyDTO.PropertyType.DATE == record.getProperty().getPropertyType()) {
            if (record.getAsNumber() == 0.0) {
              key = Index.number_mapping_prefix + record.getProperty().getCode();
              value = "";
            }
            else {
              key = Index.number_mapping_prefix + record.getProperty().getCode();
              value = Long.valueOf(record.getValue());
            }
          }
          else {
            key = Index.number_mapping_prefix + record.getProperty().getCode();
            value = record.getValue();
          }
        }
        else {
          key = Index.text_mapping_prefix + record.getProperty().getCode();
          value = record.getValue();
        }
        independentAttributes.put(key, value);
      }
      else {
        Map<String, Object> dependentAttributes = (Map<String, Object>) attributes.get(localeId);
        if (dependentAttributes == null) {
          dependentAttributes = new HashMap<>();
          attributes.put(localeId, dependentAttributes);
        }
        key = Index.text_mapping_prefix + record.getProperty().getCode();
        value = record.getValue();
        
        dependentAttributes.put(key, value);
      }
    }
  }
  
  
  private String getIdentifier(Long iid, Long baseEntityIID)
  {
    return iid.equals(baseEntityIID) ? ISearchBuilder.SELF : String.valueOf(iid);
  }

}
