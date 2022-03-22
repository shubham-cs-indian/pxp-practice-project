package com.cs.core.rdbms.entity.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IGRBucketTagDTO extends ISimpleDTO {
  
  public static final String BUCKET_ID    = "bucketId";
  public static final String TAG_ID       = "tagId";
  public static final String VALUE        = "value";
  
  Long getBucketId();
  
  void setBucketId(Long bucketId);
  
  String getTagId();
  
  String getHStoreFormat();
  
  List<String> getTagValueCodes();

}
