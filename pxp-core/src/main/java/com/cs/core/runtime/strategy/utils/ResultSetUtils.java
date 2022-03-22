package com.cs.core.runtime.strategy.utils;

import com.cs.core.config.interactor.entity.attribute.AbstractConcatenatedOperator;
import com.cs.core.runtime.interactor.constants.klassinstance.PostgreConstants;
import com.cs.core.runtime.interactor.entity.contentidentifier.ContentIdentifier;
import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.RuleViolation;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndVersionId;
import com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.cs.core.runtime.interactor.entity.language.LanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.DuplicateTypeAndInstanceIds;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.summary.KlassInstanceVersionSummary;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.exception.configuration.CSException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResultSetUtils {
  
  private ResultSetUtils()
  {
  }
  
  public static IArticleInstance map(ResultSet rs, IArticleInstance ai) throws SQLException
  {
    if (ai == null) {
      ai = new ArticleInstance();
    }
    ai.setId(rs.getString(PostgreConstants.ID));
    ai.setBaseType(rs.getString(PostgreConstants.COLUMN_NAME_BASE_TYPE));
    ai.setTypes(convertToList(rs.getArray(PostgreConstants.COLUMN_NAME_TYPES)));
    ai.setTaxonomyIds(convertToList(rs.getArray(PostgreConstants.COLUMN_NAME_TAXONOMY_IDS)));
    ai.setSelectedTaxonomyIds(
        convertToList(rs.getArray(PostgreConstants.COLUMN_NAME_TAXONOMY_IDS)));
    ai.setOrganizationId(rs.getString(PostgreConstants.COLUMN_NAME_ORGANIZATION_ID));
    ai.setPhysicalCatalogId(rs.getString(PostgreConstants.COLUMN_NAME_PHYSICAL_CATALOG_ID));
    ai.setLogicalCatalogId(rs.getString(PostgreConstants.COLUMN_NAME_LOGICAL_CATALOG_ID));
    ai.setSystemId(rs.getString(PostgreConstants.COLUMN_NAME_SYSTEM_ID));
    ai.setEndpointId(rs.getString(PostgreConstants.COLUMN_NAME_ENDPOINT_ID));
    ai.setOriginalInstanceId(rs.getString(PostgreConstants.COLUMN_NAME_ORIGINAL_INSTANCE_ID));
    ai.setDefaultAssetInstanceId(
        rs.getString(PostgreConstants.COLUMN_NAME_DEFAULT_ASSET_INSTANCE_ID));
    ai.setCloneOf(rs.getString(PostgreConstants.COLUMN_NAME_CLONE_OF));
    ai.setVersionOf(rs.getString(PostgreConstants.COLUMN_NAME_VERSION_OF));
    ai.setKlassInstanceId(rs.getString(PostgreConstants.COLUMN_NAME_KLASS_INSTANCE_ID));
    ai.setParentId(rs.getString(PostgreConstants.COLUMN_NAME_PARENT_ID));
    ai.setPath(convertToList(rs.getArray(PostgreConstants.COLUMN_NAME_PATH)));
    ai.setVariants(convertToList(rs.getArray(PostgreConstants.COLUMN_NAME_VARIANTS)));
    
    return ai;
  }
  
  public static IArticleInstance mapArticleInstance(ResultSet rs, int startIndex)
      throws SQLException, CSException
  {
    startIndex -= 1;
    
    ArticleInstance articleInstance = new ArticleInstance();
    try {
      articleInstance.setId(rs.getString(1 + startIndex));
      articleInstance.setBaseType(rs.getString(2 + startIndex));
      articleInstance.setTypes(convertToList(rs.getArray(3 + startIndex)));
      articleInstance.setTaxonomyIds(convertToList(rs.getArray(4 + startIndex)));
      articleInstance.setSelectedTaxonomyIds(convertToList(rs.getArray(5 + startIndex)));
      articleInstance.setOrganizationId(rs.getString(6 + startIndex));
      articleInstance.setPhysicalCatalogId(rs.getString(7 + startIndex));
      articleInstance.setLogicalCatalogId(rs.getString(8 + startIndex));
      articleInstance.setSystemId(rs.getString(9 + startIndex));
      articleInstance.setEndpointId(rs.getString(10 + startIndex));
      articleInstance.setOriginalInstanceId(rs.getString(11 + startIndex));
      articleInstance.setDefaultAssetInstanceId(rs.getString(12 + startIndex));
      articleInstance.setCloneOf(rs.getString(13 + startIndex));
      articleInstance.setVersionOf(rs.getString(14 + startIndex));
      articleInstance.setKlassInstanceId(rs.getString(15 + startIndex));
      articleInstance.setParentId(rs.getString(16 + startIndex));
      articleInstance.setPath(convertToList(rs.getArray(17 + startIndex)));
      articleInstance.setVariants(convertToList(rs.getArray(18 + startIndex)));
      articleInstance.setAttributeVariants(ObjectMapperUtil.readValue(rs.getString(19 + startIndex),
          new TypeReference<List<IdAndVersionId>>()
          {
            
          }));
      articleInstance.setLanguageInstances(ObjectMapperUtil.readValue(rs.getString(20 + startIndex),
          new TypeReference<List<LanguageAndVersionId>>()
          {
            
          })); // 20
      articleInstance.setLanguageCodes(convertToList(rs.getArray(21 + startIndex)));
      articleInstance.setCreationLanguage(rs.getString(22 + startIndex));
      articleInstance.setRuleViolation(ObjectMapperUtil.readValue(rs.getString(23 + startIndex),
          new TypeReference<List<RuleViolation>>()
          {
            
          })); // 23
      articleInstance.setMessages(
          ObjectMapperUtil.readValue(rs.getString(24 + startIndex), MessageInformation.class)); // 24
      articleInstance.setContext(
          ObjectMapperUtil.readValue(rs.getString(25 + startIndex), ContextInstance.class)); // 25
      articleInstance.setSummary(ObjectMapperUtil.readValue(rs.getString(26 + startIndex),
          KlassInstanceVersionSummary.class)); // 26
      articleInstance.setPartnerSources(ObjectMapperUtil.readValue(rs.getString(27 + startIndex),
          new TypeReference<List<ContentIdentifier>>()
          {
            
          })); // 27
      articleInstance.setRelationships(convertToList(rs.getArray(28 + startIndex)));
      articleInstance.setNatureRelationships(convertToList(rs.getArray(29 + startIndex)));
      articleInstance.setIsEmbedded(rs.getBoolean(30 + startIndex));
      // 31
      articleInstance.setVersionId(rs.getLong(32 + startIndex));
      articleInstance.setVersionTimestamp(rs.getTimestamp(33 + startIndex)
          .getTime());
      articleInstance.setCreatedBy(rs.getString(34 + startIndex));
      articleInstance.setCreatedOn(rs.getTimestamp(35 + startIndex)
          .getTime());
      articleInstance.setLastModifiedBy(rs.getString(36 + startIndex));
      articleInstance.setLastModified(rs.getTimestamp(37 + startIndex)
          .getTime());
    }
    catch (Exception cause) {
      throw new CSException("Failed in result set extraction", cause);
    }
    return articleInstance;
  }
  
  public static IAttributeInstance mapAttributeInstance(ResultSet rs, int startIndex)
      throws CSException
  {
    startIndex -= 1;
    AttributeInstance attributeInstance = new AttributeInstance();
    try {
      attributeInstance.setId(rs.getString(1 + startIndex));
      attributeInstance.setKlassInstanceId(rs.getString(2 + startIndex));
      attributeInstance.setCode(rs.getString(3 + startIndex));
      attributeInstance.setLanguage(rs.getString(4 + startIndex));
      attributeInstance.setConflictingValues(ObjectMapperUtil.readValue(
          rs.getString(5 + startIndex), new TypeReference<List<AttributeConflictingValue>>()
          {
            
          }));
      attributeInstance.setIsConflictResolved(rs.getBoolean(6 + startIndex));
      attributeInstance.setIsUnique(rs.getInt(7 + startIndex));
      attributeInstance.setBaseType(rs.getString(8 + startIndex));
      attributeInstance.setAttributeId(rs.getString(9 + startIndex));
      attributeInstance.setNotification(ObjectMapperUtil.readValue(rs.getString(10 + startIndex),
          new TypeReference<Map<String, Object>>()
          {
            
          }));
      attributeInstance.setIsShouldViolated(rs.getBoolean(11 + startIndex));
      attributeInstance.setContext(
          ObjectMapperUtil.readValue(rs.getString(12 + startIndex), ContextInstance.class));
      attributeInstance.setVariantInstanceId(rs.getString(13 + startIndex));
      attributeInstance.setValue(rs.getString(14 + startIndex));
      attributeInstance.setValueAsExpression(ObjectMapperUtil.readValue(
          rs.getString(15 + startIndex), new TypeReference<List<AbstractConcatenatedOperator>>()
          {
            
          }));
      attributeInstance.setValueAsNumber(rs.getDouble(16 + startIndex));
      attributeInstance.setValueAsHtml(rs.getString(17 + startIndex));
      attributeInstance.setOriginalInstanceId(rs.getString(18 + startIndex));
      attributeInstance.setTags(ObjectMapperUtil.readValue(rs.getString(19 + startIndex),
          new TypeReference<List<TagInstance>>()
          {
            
          }));
      attributeInstance.setIsMandatoryViolated(rs.getBoolean(20 + startIndex));
      attributeInstance.setDuplicateStatus(ObjectMapperUtil.readValue(rs.getString(21 + startIndex),
          new TypeReference<List<DuplicateTypeAndInstanceIds>>()
          {
            
          }));
      attributeInstance.setVersionId(rs.getLong(22 + startIndex));
      attributeInstance.setVersionTimestamp(rs.getTimestamp(23 + startIndex)
          .getTime());
      attributeInstance.setCreatedBy(rs.getString(24 + startIndex));
      attributeInstance.setCreatedOn(rs.getTimestamp(25 + startIndex)
          .getTime());
      attributeInstance.setLastModifiedBy(rs.getString(26 + startIndex));
      attributeInstance.setLastModified(rs.getTimestamp(27 + startIndex)
          .getTime());
    }
    catch (Exception cause) {
      throw new CSException("Failed in result set extraction", cause);
    }
    
    return attributeInstance;
  }
  
  public static ITagInstance mapTagInstance(ResultSet rs, int startIndex) throws CSException
  {
    startIndex -= 1;
    TagInstance tagInstance = new TagInstance();
    try {
      tagInstance.setId(rs.getString(1 + startIndex));
      tagInstance.setKlassInstanceId(rs.getString(2 + startIndex));
      tagInstance.setConflictingValues(ObjectMapperUtil.readValue(rs.getString(3 + startIndex),
          new TypeReference<List<TagConflictingValue>>()
          {
            
          })); // 3
      tagInstance.setIsMatchAndMerge(rs.getBoolean(4 + startIndex));
      tagInstance.setTagId(rs.getString(5 + startIndex));
      tagInstance.setBaseType(rs.getString(6 + startIndex));
      tagInstance.setNotification(ObjectMapperUtil.readValue(rs.getString(7 + startIndex),
          new TypeReference<Map<String, Object>>()
          {
            
          })); // 7
      tagInstance.setIsConflictResolved(rs.getBoolean(8 + startIndex));
      tagInstance.setIsShouldViolated(rs.getBoolean(9 + startIndex));
      tagInstance.setIsMandatoryViolated(rs.getBoolean(10 + startIndex));
      tagInstance.setVariantInstanceId(rs.getString(11 + startIndex));
      tagInstance.setContextInstanceId(rs.getString(12 + startIndex));
      tagInstance.setTagValues(ObjectMapperUtil.readValue(rs.getString(13 + startIndex),
          new TypeReference<List<TagInstanceValue>>()
          {
            
          })); // 13
      tagInstance.setVersionId(rs.getLong(14 + startIndex));
      tagInstance.setVersionTimestamp(rs.getTimestamp(15 + startIndex)
          .getTime());
      tagInstance.setCreatedBy(rs.getString(16 + startIndex));
      tagInstance.setCreatedOn(rs.getTimestamp(17 + startIndex)
          .getTime());
      tagInstance.setLastModifiedBy(rs.getString(18 + startIndex));
      tagInstance.setLastModified(rs.getTimestamp(19 + startIndex)
          .getTime());
    }
    catch (Exception cause) {
      throw new CSException("Failed in result set extraction", cause);
    }
    
    return tagInstance;
  }
  
  public static List<String> convertToList(Array array) throws SQLException
  {
    return array == null ? new ArrayList<>() : Arrays.asList((String[]) array.getArray());
  }
  
  public static void main(String[] args) throws SQLException
  {
    /*try (Connection connection = DataBaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * FROM  article.article");
        ResultSet resultSet = statement.executeQuery();) {
    
    
      IArticleInstance instance = map(resultSet, null);
    
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }*/
    
  }
}
