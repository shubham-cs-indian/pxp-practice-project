package com.cs.core.rdbms.entity.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.cs.core.elastic.Index;
import com.cs.core.elastic.ibuilders.ISearchBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketAttributeDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketTagDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordBucketDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class GoldenRecordBucketDTO extends GoldenRecordDTO implements IGoldenRecordBucketDTO {
  
  /**
   * 
   */
  private static final long          serialVersionUID     = 1L;
  
  private Long                       bucketId             = 0l;
  private String                     endpointCode         = "";
  private String                     catalogCode          = "";
  private String                     organisationCode     = "";
  private Long                       createdTime          = 0l;
  private Long                       lastModifiedTime     = 0l;
  private Boolean                    isSearchable         = false;
  private Set<IGRBucketAttributeDTO> bucketAttributes     = new TreeSet<>();
  private Set<IGRBucketTagDTO>       bucketTags           = new TreeSet<>();
  
  public GoldenRecordBucketDTO(Long bucketId, String endpointCode, String catalogCode,
      String organisationCode, Boolean isSearchable, String ruleId, Long createTime,
      Long lastModifiedTime, List<Long> linkedBaseEntityIIDs)
  {
    super(ruleId, linkedBaseEntityIIDs);
    this.bucketId = bucketId;
    this.endpointCode = endpointCode;
    this.catalogCode = catalogCode;
    this.organisationCode = organisationCode;
    this.isSearchable = isSearchable;
    this.createdTime = createTime;
    this.lastModifiedTime = lastModifiedTime;
    this.bucketAttributes = new HashSet<IGRBucketAttributeDTO>();
    this.bucketTags = new HashSet<IGRBucketTagDTO>();
  }
  
  public GoldenRecordBucketDTO(IResultSetParser result) throws Exception
  {
    super(result);
    this.bucketId = result.getLong(GoldenRecordBucketDTO.BUCKET_ID);
    this.endpointCode = result.getString(GoldenRecordBucketDTO.ENDPOINT_CODE);
    this.catalogCode = result.getString(GoldenRecordBucketDTO.CATALOG_CODE);
    this.organisationCode = result.getString(GoldenRecordBucketDTO.ORGANISATION_CODE);
    this.isSearchable = result.getBoolean(GoldenRecordBucketDTO.IS_SEARCHABLE);
    this.createdTime = result.getLong(GoldenRecordBucketDTO.CREATED_TIME);
    this.lastModifiedTime = result.getLong(GoldenRecordBucketDTO.LAST_MODIFIED_TIME);
    this.bucketAttributes = new HashSet<IGRBucketAttributeDTO>();
    this.bucketTags = new HashSet<IGRBucketTagDTO>();
  }

  @Override
  public Long getBucketId()
  {
    return bucketId;
  }
  
  @Override
  public String getEndpointCode()
  {
    return endpointCode;
  }
  
  @Override
  public String getCatalogCode()
  {
    return catalogCode;
  }
  
  @Override
  public String getOrganisationCode()
  {
    return organisationCode;
  }
  
  @Override
  public Boolean getIsSearchable()
  {
    return isSearchable;
  }
  
  @Override
  public void setIsSearchable(Boolean isSearchable)
  {
    this.isSearchable = isSearchable;
  }
  
  @Override
  public Set<IGRBucketAttributeDTO> getBucketAttributes()
  {
    return bucketAttributes;
  }
  
  @Override
  public void setBucketAttributes(Set<IGRBucketAttributeDTO> bucketAttributes) 
  {
    this.bucketAttributes.clear();
    this.bucketAttributes.addAll(bucketAttributes);
  }
  
  @Override
  public Set<IGRBucketTagDTO> getBucketTags()
  {
    return bucketTags;
  }
  
  @Override
  public void setBucketTags(Set<IGRBucketTagDTO> bucketTags)
  {
    this.bucketTags.clear();
    this.bucketTags.addAll(bucketTags);
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }



  @Override
  public Long getcreatedOnTime()
  {
    return createdTime;
  }



  @Override
  public Long getLastModifiedTime()
  {
    return lastModifiedTime;
  }

  @Override
  public Map<String, Object> toElasticDocument()
  {
    Map<String, Object> elasticInstance = new HashMap<>();
    elasticInstance.put(IGoldenRecordBucketDTO.BUCKET_ID, bucketId);
    elasticInstance.put(IGoldenRecordBucketDTO.CREATED_TIME, createdTime);
    elasticInstance.put(IGoldenRecordBucketDTO.LAST_MODIFIED_TIME, lastModifiedTime);
    elasticInstance.put(IGoldenRecordBucketDTO.LINKED_BASE_ENTITES, getLinkedBaseEntities());
    elasticInstance.put(IGoldenRecordBucketDTO.RULE_ID, getRuleId());
    elasticInstance.put(IGoldenRecordBucketDTO.IS_SEARCHABLE, isSearchable);
    elasticInstance.put(IGoldenRecordBucketDTO.ENDPOINT_CODE, endpointCode);
    elasticInstance.put(IGoldenRecordBucketDTO.CATALOG_CODE, catalogCode);
    elasticInstance.put(IGoldenRecordBucketDTO.ORGANISATION_CODE, organisationCode);
    Map<String, String> independentPropertyAttributes = bucketAttributes.stream().peek(bucketAttribute -> {
      String attributeId = bucketAttribute.getAttributeId();
      IPropertyDTO property = null;
      try {
        property = ConfigurationDAO.instance().getPropertyByCode(attributeId);
      }
      catch (RDBMSException e) {
        e.printStackTrace();
      }
      if(property.isNumeric()) {
        bucketAttribute.setAttributeId(Index.number_mapping_prefix + attributeId);
      }else {
        bucketAttribute.setAttributeId(Index.text_mapping_prefix + attributeId);
      }
    }
    ).collect(Collectors.toMap(IGRBucketAttributeDTO::getAttributeId, IGRBucketAttributeDTO::getValue));
    Map<String, Object> propertyAttributes = new HashMap<>();
    propertyAttributes.put(ISearchBuilder.Fields.independent.name(), independentPropertyAttributes);
    Map<String, Object> propertyObjects = new HashMap<>();
    propertyObjects.put(ISearchBuilder.Fields.attribute.name(), propertyAttributes);
    propertyObjects.put(ISearchBuilder.Fields.tag.name(), bucketTags.stream()
        .collect(Collectors.toMap(IGRBucketTagDTO::getTagId, IGRBucketTagDTO::getTagValueCodes)));
    elasticInstance.put(ISearchBuilder.Fields.propertyObjects.name(), propertyObjects);

    return elasticInstance;
  }
 
}
