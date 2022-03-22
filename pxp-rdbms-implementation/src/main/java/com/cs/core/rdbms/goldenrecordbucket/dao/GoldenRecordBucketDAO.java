package com.cs.core.rdbms.goldenrecordbucket.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.cs.config.standard.IStandardConfig.StandardClassifier;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.data.Text;
import com.cs.core.elastic.Index;
import com.cs.core.elastic.builders.SearchBuilder;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.elastic.ibuilders.ISearchBuilder;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.GRBucketAttributeDTO;
import com.cs.core.rdbms.entity.dto.GRBucketTagDTO;
import com.cs.core.rdbms.entity.dto.GoldenRecordBucketDTO;
import com.cs.core.rdbms.entity.dto.GoldenRecordDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketAttributeDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketTagDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordBucketDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordBucketDAO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class GoldenRecordBucketDAO implements IGoldenRecordBucketDAO {
  
  protected List<IPropertyDTO.PropertyType> numericTypes = Arrays.asList(IPropertyDTO.PropertyType.CALCULATED,
      IPropertyDTO.PropertyType.MEASUREMENT, IPropertyDTO.PropertyType.PRICE, IPropertyDTO.PropertyType.NUMBER,
      IPropertyDTO.PropertyType.DATE);
  
  int count = 0;
  
  public static final String INSERT_GOLDEN_RECORD_BUCKET           = "INSERT INTO goldenrecordbucket ("
      + GoldenRecordBucketDTO.BUCKET_ID + ", " + GoldenRecordBucketDTO.ORGANISATION_CODE + ", "
      + GoldenRecordBucketDTO.IS_SEARCHABLE + ", " + GoldenRecordBucketDTO.CATALOG_CODE + ", "
      + GoldenRecordBucketDTO.ENDPOINT_CODE + ", " + GoldenRecordBucketDTO.RULE_ID + ", "
      + GoldenRecordBucketDTO.CREATED_TIME + ", " + GoldenRecordBucketDTO.LAST_MODIFIED_TIME + ") "
      + "VALUES (?,?,?,?,?,?,?,?) ";
  
  public static final String INSERT_GOLDEN_RECORD_BUCKET_ATTRIBUTE = "INSERT INTO goldenrecordbucketattributelink ("
      + GRBucketAttributeDTO.BUCKET_ID + ", " + GRBucketAttributeDTO.ATTRIBUTE_ID + ", "
      + GRBucketAttributeDTO.VALUE + ") " + "VALUES (?,?,?) ";
  
  public static final String INSERT_GOLDEN_RECORD_BUCKET_TAG       = "INSERT INTO goldenrecordbuckettaglink ("
      + GRBucketTagDTO.BUCKET_ID + ", " + GRBucketTagDTO.TAG_ID + ", " + GRBucketTagDTO.VALUE + ") "
      + "VALUES (?,?,?  :: hstore) ";
  
  public static final String INSERT_GOLDEN_RECORD_BUCKET_BASE_ENTITY_IIDS       = "INSERT INTO goldenrecordbucketbaseentitylink ("
      + IGoldenRecordBucketDTO.BUCKET_ID + ", " + IGoldenRecordBucketDTO.BASE_ENTITY_IID + ") "
      + "VALUES (?,?) ";
  
  public static final String SELECT_GOLDEN_RECORD_BUCKET            = "SELECT * FROM goldenrecordbucket WHERE bucketid = ?";
  public static final String SELECT_GOLDEN_RECORD_BUCKET_ATTRIBUTES = "SELECT * FROM goldenrecordbucketattributelink "
      + "WHERE bucketid = ? ";
  public static final String SELECT_GOLDEN_RECORD_BUCKET_TAGS       = "SELECT * FROM goldenrecordbuckettaglink "
      + "WHERE bucketid = ? ";
  public static final String SELECT_GOLDEN_RECORD_BUCKET_BASE_ENTITY_IIDS       = "SELECT * FROM goldenrecordbucketbaseentitylink "
      + "WHERE bucketid = ? ";
  
  /**Update Queries */
  public static final String UPDATE_GOLDEN_RECORD_BUCKET   = "update pxp.goldenrecordbucket set isSearchable = ?, "
      + "lastModifiedTime = ? where bucketid = ?";
  public static final String UPDATE_GOLDEN_RECORD_BUCKET_BASE_ENTITY_IID   = "update pxp.goldenrecordbucketbaseentitylink "
      + "set baseentityiid = ? where bucketid = ?";

  public static final String DELETE_GOLDEN_RECORD_BUCKET_BASE_ENTITY_IID  = "DELETE from pxp.goldenrecordbucketbaseentitylink"
      + " WHERE bucketid = ?";
  public static final String DELETE_GOLDEN_RECORD_BUCKET_ATTRIBUTES  = "DELETE from pxp.goldenrecordbucketattributelink"
      + " WHERE bucketid = ?";
  public static final String DELETE_GOLDEN_RECORD_BUCKET_TAGS  = "DELETE from pxp.goldenrecordbuckettaglink"
      + " WHERE bucketid = ?";
  public static final String DELETE_GOLDEN_RECORD_BUCKET  = "DELETE from pxp.goldenrecordbucket"
      + " WHERE bucketid = ?";
  public static final String NEXT_VALUE  = "SELECT nextval('pxp.seqBucketID')";
  
  public static final String SELECT_GOLDEN_RECORD_BUCKET_BY_RULE_ID = "SELECT bucketid FROM goldenrecordbucket WHERE ruleid = ?";
  
  public static final String GET_BASEENTITYIIDS_LINKED_WITH_GOLDEN_RECORD_ARTICLE = "SELECT side1entityiid FROM pxp.relation WHERE propertyiid = ? AND  side2entityiid = ? ";
  
  public static final String GET_ALL_GOLDEN_RECORD_BUCKETS_BY_BASEENTITYIID = "SELECT bucketid FROM pxp.goldenrecordbucketbaseentitylink WHERE baseentityiid = ?";
  
  public static final String SET_BASEENTITYIID_ISMERGED = "UPDATE pxp.baseentity SET ismerged = true where baseentityiid IN (%s)";
  
  public static final String GOLDEN_RECORD_IID_IN_BUCKET = "SELECT becl.baseentityiid FROM pxp.baseentityclassifierlink becl JOIN pxp.classifierconfig cc ON becl.otherclassifieriid = cc.classifieriid WHERE cc.classifiercode = '" + StandardClassifier.golden_article_klass.name() + "' AND becl.baseentityiid IN (%s)";
  
  @Override
  public void createGoldenRecordBucket(IGoldenRecordBucketDTO... goldenRecordBucketDTOs)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement preparedStatement = currentConn
              .prepareStatement(INSERT_GOLDEN_RECORD_BUCKET);
          for (IGoldenRecordBucketDTO goldenRecordBucketDTO : goldenRecordBucketDTOs) {
            Long currentTimeMillis = System.currentTimeMillis();
            preparedStatement.setLong(1, goldenRecordBucketDTO.getBucketId());
            preparedStatement.setString(2, goldenRecordBucketDTO.getOrganisationCode());
            preparedStatement.setBoolean(3, goldenRecordBucketDTO.getIsSearchable());
            preparedStatement.setString(4, goldenRecordBucketDTO.getCatalogCode());
            preparedStatement.setString(5, goldenRecordBucketDTO.getEndpointCode());
            preparedStatement.setString(6, goldenRecordBucketDTO.getRuleId());
            preparedStatement.setLong(7, currentTimeMillis);
            preparedStatement.setLong(8, currentTimeMillis);
            preparedStatement.executeUpdate();
            
            createGoldenRecordBucketAttribute(goldenRecordBucketDTO.getBucketAttributes()
                .toArray(new GRBucketAttributeDTO[] {}));
            createGoldenRecordBucketTag(goldenRecordBucketDTO.getBucketTags()
                .toArray(new GRBucketTagDTO[] {}));
            createGoldenRecordBucketBaseEntityIID(goldenRecordBucketDTO.getBucketId(),
                goldenRecordBucketDTO.getLinkedBaseEntities());
             try {
               updateGoldenRecordSearchableDocument(goldenRecordBucketDTO);
             }
             catch (IOException e) {
               e.printStackTrace();
             }
          }
        });
    
  }
  
  @Override
  public void createGoldenRecordBucketAttribute(IGRBucketAttributeDTO... bucketAttributeDTOs)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement preparedStatement = currentConn
              .prepareStatement(INSERT_GOLDEN_RECORD_BUCKET_ATTRIBUTE);
          for (IGRBucketAttributeDTO bucketAttributeDTO : bucketAttributeDTOs) {
            preparedStatement.setLong(1, bucketAttributeDTO.getBucketId());
            preparedStatement.setString(2, bucketAttributeDTO.getAttributeId());
            preparedStatement.setString(3, bucketAttributeDTO.getValue());
            preparedStatement.executeUpdate();
          }
        });
    
  }
  
  @Override
  public void createGoldenRecordBucketTag(IGRBucketTagDTO... bucketTagDTOs) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_GOLDEN_RECORD_BUCKET_TAG);
      for (IGRBucketTagDTO bucketTagDTO : bucketTagDTOs) {
        preparedStatement.setLong(1, bucketTagDTO.getBucketId());
        preparedStatement.setString(2, bucketTagDTO.getTagId());
        preparedStatement.setString(3, bucketTagDTO.getHStoreFormat());
        preparedStatement.executeUpdate();
      }
    });
  }
  
  @Override
  public void createGoldenRecordBucketBaseEntityIID(Long bucketId, 
      List<Long> bucketBaseEntityIIDs) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_GOLDEN_RECORD_BUCKET_BASE_ENTITY_IIDS);
      for (Long bucketBaseEntityIID : bucketBaseEntityIIDs) {
        preparedStatement.setLong(1, bucketId);
        preparedStatement.setLong(2, bucketBaseEntityIID);
        preparedStatement.executeUpdate();
      }
    });
  }

  String query = "";
  String queryCondition = " where b.ruleid = ? "
      + "and b.organisationcode = ? and b.catalogcode = ? and b.endpointcode = ?";
  @Override
  public void evaluateGoldenRecordBucket(List<Map<String, Object>> attributesToSave,
      List<IGRBucketTagDTO> tagsToSave, IBaseEntityDTO baseEntity, String goldenRecordRuleId,
      List<IGoldenRecordBucketDTO> goldenRecordBucketDTOsToCreate,
      List<IGoldenRecordBucketDTO> goldenRecordBucketDTOsToUpdate, List<String> booleanTagsToSave) 
          throws Exception
  {
    String endpointCode = baseEntity.getEndpointCode();
    String catalogCode = baseEntity.getCatalog()
        .getCode();
    String organizationCode = baseEntity.getCatalog()
        .getOrganizationCode();
    
    //TODO: TO be done properly through SearchDAO
    BoolQueryBuilder elasticQuery = new BoolQueryBuilder();
    elasticQuery.filter(QueryBuilders.termsQuery(IGoldenRecordBucketDTO.ENDPOINT_CODE, endpointCode));
    elasticQuery.filter(QueryBuilders.termsQuery(IGoldenRecordBucketDTO.CATALOG_CODE, catalogCode));
    elasticQuery.filter(QueryBuilders.termsQuery(IGoldenRecordBucketDTO.ORGANISATION_CODE, organizationCode));
    elasticQuery.filter(QueryBuilders.termsQuery(IGoldenRecordBucketDTO.RULE_ID, goldenRecordRuleId));
    
    BoolQueryBuilder elasticAttributeQuery = new BoolQueryBuilder();
    attributesToSave.stream().forEach(attributeToSave -> {
      try {
        String attributeCode = (String) attributeToSave.get(IAttribute.ID);
        String attributeValue = (String) attributeToSave.get(IGRBucketAttributeDTO.VALUE);
        IPropertyDTO property = ConfigurationDAO.instance().getPropertyByCode(attributeCode);
        PropertyType propertyType = property.getPropertyType();
        String prefix = ISearchBuilder.Fields.propertyObjects.name() +"."+ ISearchBuilder.Fields.attribute.name()+"."+ISearchBuilder.Fields.independent.name();
        if(numericTypes.contains(propertyType)) {
          prefix = prefix + ".i_" + attributeCode;
        } else {
          prefix = prefix + ".t_" + attributeCode;
        }
        prefix = prefix + "." + ISearchBuilder.FieldType.raw.name();
        elasticAttributeQuery.filter(QueryBuilders.termsQuery(prefix, attributeValue));
      }
      catch (RDBMSException e) {
        e.printStackTrace();
      }
    });
    BoolQueryBuilder nestedAttributeQuery = new BoolQueryBuilder();
    nestedAttributeQuery.filter(QueryBuilders.nestedQuery(SearchBuilder.Fields.propertyObjects.name(), elasticAttributeQuery, ScoreMode.None));
    elasticQuery.must(nestedAttributeQuery);
    
    BoolQueryBuilder elasticTagQuery = new BoolQueryBuilder();
    String fieldTagId = ISearchBuilder.Fields.propertyObjects.name() + "." + ISearchBuilder.Fields.tag.name() + ".%TAG_ID%";
    tagsToSave.forEach(tagToSave -> {
      elasticTagQuery.filter(QueryBuilders.termsQuery(
          fieldTagId.replace("%TAG_ID%", tagToSave.getTagId()), tagToSave.getTagValueCodes()));
    });
    
    booleanTagsToSave.forEach(booleanTagId -> {
      elasticTagQuery.mustNot(QueryBuilders.existsQuery(
          fieldTagId.replace("%TAG_ID%", booleanTagId)));
    });
    
    BoolQueryBuilder nestedTagQuery = new BoolQueryBuilder();
    nestedTagQuery.filter(QueryBuilders.nestedQuery(SearchBuilder.Fields.propertyObjects.name(), elasticTagQuery, ScoreMode.None));
    elasticQuery.must(nestedTagQuery);
    
    SearchSourceBuilder builder = new SearchSourceBuilder();
    builder.query(elasticQuery);
    builder.from(0);
    builder.size(1);
    SearchHit[] search = ElasticServiceDAS.instance().search(builder, Index.goldenrecordbucketcache.name());
    //search[0].getId()
    /*query = "";
    Long[] bucketIds = {0L};
    query = "SELECT b.bucketId from pxp.goldenrecordbucket b";


    String endpointCode = baseEntity.getEndpointCode();
    String catalogCode = baseEntity.getCatalog()
        .getCode();
    String organizationCode = baseEntity.getCatalog()
        .getOrganizationCode();
    if (!attributesToSave.isEmpty()) {
      query = query + ", pxp.goldenrecordbucketattributelink a";
    }
    if (!tagsToSave.isEmpty()) {
      query = query + ", pxp.goldenrecordbuckettaglink t";
    }
    query = query + queryCondition;
    query = query + getAttributeQuery(attributesToSave);
    query = query + getTagQuery(tagsToSave);
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(query);
          stmt.setString(1, goldenRecordRuleId);
          stmt.setString(2, organizationCode);
          stmt.setString(3, catalogCode);
          stmt.setString(4, endpointCode);
          IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
          // count = result.getInt("count");
          count = 0;
    //          bucketId = result.getLong("bucketId");
          while (result.next()) {
            count++;
            bucketIds[0] = result.getLong("bucketId");
          }
        });
    
    if (count != (attributesToSave.size() + tagsToSave.size())) {*/
    if (search.length==0) {
      try {
        fillGoldenRecordBucketDTOToCreate(attributesToSave, tagsToSave, baseEntity,
            goldenRecordBucketDTOsToCreate, endpointCode, catalogCode, organizationCode,
            goldenRecordRuleId);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    else {
      IGoldenRecordBucketDTO goldenRecordBucketDTO = null;
      try {
        ;
        goldenRecordBucketDTO = getGoldenRecordBucketById(Long.valueOf(search[0].getId()));
        Long baseEntityIID = baseEntity.getBaseEntityIID();
        if(!goldenRecordBucketDTO.getLinkedBaseEntities().contains(baseEntityIID))
        {
          goldenRecordBucketDTO.getLinkedBaseEntities()
          .add(baseEntity.getBaseEntityIID());          
          goldenRecordBucketDTO.setIsSearchable(true);
        }
        goldenRecordBucketDTOsToUpdate.add(goldenRecordBucketDTO);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  private void fillGoldenRecordBucketDTOToCreate(List<Map<String, Object>> attibutesToSave,
      List<IGRBucketTagDTO> tagsToSave, IBaseEntityDTO baseEntity,
      List<IGoldenRecordBucketDTO> goldenRecordBucketDTOsToCreate, String endpointCode,
      String catalogCode, String organizationCode, String ruleId) throws Exception
  {
    IGoldenRecordBucketDTO goldenRecordBucketDTO;
    List<Long> baseEntitIIDs = new ArrayList<Long>();
    baseEntitIIDs.add(baseEntity.getBaseEntityIID());
    Long uniqueId = newBucketId();
    goldenRecordBucketDTO = new GoldenRecordBucketDTO(uniqueId, endpointCode, catalogCode,
        organizationCode, false, ruleId, 0L, 0L, baseEntitIIDs);
    Set<IGRBucketAttributeDTO> bucketAttributes = goldenRecordBucketDTO.getBucketAttributes();
    for (Map<String, Object> attribute : attibutesToSave) {
      bucketAttributes.add(new GRBucketAttributeDTO(goldenRecordBucketDTO.getBucketId(), (String)attribute.get("id"), (String)attribute.get("value")));
    }
    Set<IGRBucketTagDTO> bucketTags = goldenRecordBucketDTO.getBucketTags();
     for (IGRBucketTagDTO tag : tagsToSave) {
       tag.setBucketId(goldenRecordBucketDTO.getBucketId());
       bucketTags.add(tag);
     }
     goldenRecordBucketDTOsToCreate.add(goldenRecordBucketDTO);
  }
  
  private String getAttributeQuery(List<Map<String, Object>> attributesToSave)
  {
    if (attributesToSave.isEmpty()) {
      return "";
    }
    String query = " and ( ";
    for (Map<String, Object> attribute : attributesToSave) {
      query = query + " a.attributeid = '" + attribute.get(IAttribute.ID) + "' AND a.value = '"
          + attribute.get(IGRBucketAttributeDTO.VALUE) + "' OR";
    }
    query = query.substring(0, (query.length()-2));
    return query + " ) AND b.bucketid = a.bucketid ";
  }
  
  private String getTagQuery(List<IGRBucketTagDTO> tagsToSave)
  {
    if (tagsToSave.isEmpty()) {
      return "";
    }
    String query = " and ( ";
    for (IGRBucketTagDTO tag : tagsToSave) {
      query = query + " t.tagid = '" + tag.getTagId() + "' AND t.value = '"
          + tag.getHStoreFormat() + "' OR";
    }
    query = query.substring(0, (query.length()-2));
    return query + " ) AND b.bucketid = t.bucketid ";
  }
  
  @Override
  public IGoldenRecordBucketDTO getGoldenRecordBucketById(Long bucketId) throws Exception {
    
    List<IGoldenRecordBucketDTO>  goldenRecordBucketDTOs = new ArrayList<IGoldenRecordBucketDTO>();
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(SELECT_GOLDEN_RECORD_BUCKET);
      stmt.setLong(1, bucketId);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
      while (result.next()) {
        try {
          goldenRecordBucketDTOs.add(new GoldenRecordBucketDTO(result));
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      
      stmt = currentConn.prepareStatement(SELECT_GOLDEN_RECORD_BUCKET_ATTRIBUTES);
      stmt.setLong(1, bucketId);
      result = currentConn.getResultSetParser(stmt.executeQuery());
      IGoldenRecordBucketDTO goldenRecordBucketDTO = goldenRecordBucketDTOs.get(0);
      Set<IGRBucketAttributeDTO> bucketAttributes = goldenRecordBucketDTO.getBucketAttributes();
      while (result.next()) {
        try {
          bucketAttributes.add(new GRBucketAttributeDTO(result));
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      
      stmt = currentConn.prepareStatement(SELECT_GOLDEN_RECORD_BUCKET_TAGS);
      stmt.setLong(1, bucketId);
      result = currentConn.getResultSetParser(stmt.executeQuery());
      Set<IGRBucketTagDTO> bucketTags = goldenRecordBucketDTO.getBucketTags();
      while (result.next()) {
        try {
          bucketTags.add(new GRBucketTagDTO(result));
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }

      stmt = currentConn.prepareStatement(SELECT_GOLDEN_RECORD_BUCKET_BASE_ENTITY_IIDS);
      stmt.setLong(1, bucketId);
      result = currentConn.getResultSetParser(stmt.executeQuery());
      List<Long> baseEntityIIDs = new ArrayList<Long>();
      while (result.next()) {
        try {
          baseEntityIIDs.add(result.getLong("baseEntityIID"));
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      goldenRecordBucketDTO.setLinkedBaseEntities(baseEntityIIDs);

    });
    
    return goldenRecordBucketDTOs.get(0);
    
  }

  @Override
  public void updateGoldenRecordBucket(IGoldenRecordBucketDTO... goldenRecordBucketDTOs)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement preparedStatement = currentConn
          .prepareStatement(UPDATE_GOLDEN_RECORD_BUCKET);
      for (IGoldenRecordBucketDTO goldenRecordBucketDTO : goldenRecordBucketDTOs) {
        Long currentTimeMillis = System.currentTimeMillis();
        preparedStatement.setBoolean(1, goldenRecordBucketDTO.getIsSearchable());
        preparedStatement.setLong(2, currentTimeMillis);
        preparedStatement.setLong(3, goldenRecordBucketDTO.getBucketId());
        preparedStatement.executeUpdate();
        try {
          updateGoldenRecordBucketBaseEntityIIDs(goldenRecordBucketDTO.getBucketId(), goldenRecordBucketDTO.getLinkedBaseEntities());
          updateGoldenRecordSearchableDocument(goldenRecordBucketDTO);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  
  @Override
  public void updateGoldenRecordBucketBaseEntityIIDs(Long bucketId, List<Long> bucketBaseEntityIIDs) throws Exception
  {
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement preparedStatement = currentConn
              .prepareStatement(DELETE_GOLDEN_RECORD_BUCKET_BASE_ENTITY_IID);
          preparedStatement.setLong(1, bucketId);
          preparedStatement.execute();
          createGoldenRecordBucketBaseEntityIID(bucketId, bucketBaseEntityIIDs);
        });
  }
  
  private void updateGoldenRecordSearchableDocument(IGoldenRecordBucketDTO goldenRecordBucket) throws IOException
  {
    Map<String, Object> elasticDocument = goldenRecordBucket.toElasticDocument();
    String bucketId = String.valueOf(elasticDocument.get(IGoldenRecordBucketDTO.BUCKET_ID));
    String goldenRecordBucketIndexName = Index.goldenrecordbucketcache.name();
    ElasticServiceDAS.instance().indexDocument(elasticDocument, goldenRecordBucketIndexName, bucketId);
  }

  private Long newBucketId() throws Exception
  {
    List<Long> newBucketIds = new ArrayList<Long>();
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(NEXT_VALUE);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
      while (result.next()) {
        newBucketIds.add(result.getLong("nextval"));
      }
    });
    return newBucketIds.get(0);

  }

  @Override
  public void deleteGoldenRecordBucketsByRuleId(String ruleId) throws RDBMSException
  {
    List<Long> bucketIdsToDelete = new ArrayList<Long>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement preparedStatement = currentConn.prepareStatement(SELECT_GOLDEN_RECORD_BUCKET_BY_RULE_ID);
      preparedStatement.setString(1, ruleId);
      IResultSetParser result = currentConn.getResultSetParser(preparedStatement.executeQuery());

      while (result.next()) {
        try {
          bucketIdsToDelete.add(result.getLong(IGoldenRecordBucketDTO.BUCKET_ID));
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      try {
        deleteGoldenRecordBucketByIds(bucketIdsToDelete);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    });
    
    
    
  }

  @Override
  public void deleteGoldenRecordBucketByIds(List<Long> bucketIdsToDelete)
      throws Exception
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      currentConn.getProcedure( "pxp.sp_deleteGoldenRecordBuckets")
      .setInput(ParameterType.IID_ARRAY, bucketIdsToDelete)
      .execute();
      
      List<String> newList = bucketIdsToDelete.stream()
          .map(s -> String.valueOf(s))
          .collect(Collectors.toList());
      try {
        String goldenRecordBucketIndexName = Index.goldenrecordbucketcache.name();
        ElasticServiceDAS.instance().bulkDelete(newList, goldenRecordBucketIndexName);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      
    });
  }
  
  @Override
  public List<String> getGoldenRecordLinkedBaseEntityIidsById(String bucketId) throws RDBMSException
  {
    List<String> baseEntityIids = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement preparedStatement = currentConn.prepareStatement(SELECT_GOLDEN_RECORD_BUCKET_BASE_ENTITY_IIDS);
      preparedStatement.setLong(1, Long.parseLong(bucketId));
      IResultSetParser result = currentConn.getResultSetParser(preparedStatement.executeQuery());
      
      while (result.next()) {
        try {
          baseEntityIids.add(result.getString(IGoldenRecordBucketDTO.BASE_ENTITY_IID));
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    return baseEntityIids;
  }
  
  @Override
  public List<Long> getBaseEntityIidsLinkedWithGoldenRecordArticle(String goldenRecordId) throws RDBMSException
  {
    List<Long> baseEntityIids = new ArrayList<>();
    IPropertyDTO relationshipProperty = ConfigurationDAO.instance()
        .getPropertyByCode(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID);
    Long propertyIID = relationshipProperty.getPropertyIID();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement preparedStatement = currentConn.prepareStatement(GET_BASEENTITYIIDS_LINKED_WITH_GOLDEN_RECORD_ARTICLE);
      preparedStatement.setLong(1, propertyIID);
      preparedStatement.setLong(2, Long.parseLong(goldenRecordId));
      IResultSetParser result = currentConn.getResultSetParser(preparedStatement.executeQuery());
      
      while (result.next()) {
        try {
          baseEntityIids.add(result.getLong("side1entityiid"));
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    baseEntityIids.add(Long.parseLong(goldenRecordId));
    
    return baseEntityIids;
  }
  
  @Override
  public List<Long> getAllGoldenRecordBucketsOfBaseEntityIid(Long baseEntityIid) throws RDBMSException
  {
    List<Long> baseEntityIids = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement preparedStatement = currentConn.prepareStatement(GET_ALL_GOLDEN_RECORD_BUCKETS_BY_BASEENTITYIID);
      preparedStatement.setLong(1, baseEntityIid);
      IResultSetParser result = currentConn.getResultSetParser(preparedStatement.executeQuery());
      
      while (result.next()) {
        try {
          baseEntityIids.add(result.getLong("bucketid"));
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    return baseEntityIids;
  }
  
  @Override
  public void setBaseEntityIidsToIsMerged(List<String> baseEntityIids) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuffer query = new StringBuffer();
      query.append(String.format(SET_BASEENTITYIID_ISMERGED, Text.join(",", baseEntityIids, "'%s'")));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
    });
  }
  
  @Override
  public IGoldenRecordDTO getGoldenRecordRuleAndBaseEntityIIDs(String bucketId,
      String goldenRecordId) throws Exception
  {
    IGoldenRecordDTO goldenRecordDTO = new GoldenRecordDTO();
    if (bucketId != null) {
      goldenRecordDTO = getGoldenRecordBucketById(Long.parseLong(bucketId));
    }
    else if (goldenRecordId != null) {
      goldenRecordDTO.setLinkedBaseEntities(getBaseEntityIidsLinkedWithGoldenRecordArticle(goldenRecordId));
    }
    return goldenRecordDTO;
  }
  
  @Override
  public Boolean goldenRecordExistInBucket(List<Long> baseEntityIids) throws RDBMSException
  {
    long[] goldenRecordIid = new long[1];
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(GOLDEN_RECORD_IID_IN_BUCKET, Text.join(",", baseEntityIids));
      PreparedStatement stmt = currentConn.prepareStatement(query);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
      
      goldenRecordIid[0] = result.next() ? result.getLong("baseentityiid") : 0l;
      
    });
    return goldenRecordIid[0] != 0l;
  }
}
