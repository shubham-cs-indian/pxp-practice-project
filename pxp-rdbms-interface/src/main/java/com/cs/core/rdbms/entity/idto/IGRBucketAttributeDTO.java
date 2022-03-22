package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IGRBucketAttributeDTO extends ISimpleDTO {
  
  public static final String BUCKET_ID    = "bucketId";
  public static final String ATTRIBUTE_ID = "attributeId";
  public static final String VALUE        = "value";
  
  Long getBucketId();

  String getAttributeId();
  public void setAttributeId(String attributeId);
  
  String getValue();
}
