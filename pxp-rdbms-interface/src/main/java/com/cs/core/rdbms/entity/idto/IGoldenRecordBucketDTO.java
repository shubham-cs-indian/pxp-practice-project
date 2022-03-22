package com.cs.core.rdbms.entity.idto;

import java.util.Map;
import java.util.Set;

public interface IGoldenRecordBucketDTO extends IGoldenRecordDTO {
  
  public static final String BUCKET_ID           = "bucketId";
  public static final String ENDPOINT_CODE       = "endpointCode";
  public static final String CATALOG_CODE        = "catalogCode";
  public static final String ORGANISATION_CODE   = "organisationCode";
  public static final String IS_SEARCHABLE       = "isSearchable";
  public static final String CREATED_TIME        = "createdTime";
  public static final String LAST_MODIFIED_TIME  = "lastModifiedTime";
  public static final String BUCKET_ATTRIBUTES   = "bucketAttributes";
  public static final String BUCKET_TAGS         = "bucketTags";
  public static final String BASE_ENTITY_IID     = "baseEntityIID";

  public Long getBucketId();
  
  public String getEndpointCode();
  
  public String getCatalogCode();
  
  public String getOrganisationCode();
  
  public Boolean getIsSearchable();
  
  public void setIsSearchable(Boolean isSearchable);
  
  public Set<IGRBucketAttributeDTO> getBucketAttributes();
  
  public void setBucketAttributes(Set<IGRBucketAttributeDTO> bucketAttributes);

  public Set<IGRBucketTagDTO> getBucketTags();

  public void setBucketTags(Set<IGRBucketTagDTO> bucketTags);
  
  public Long getcreatedOnTime();
  
  public Long getLastModifiedTime();
  
  public Map<String, Object> toElasticDocument();
}
