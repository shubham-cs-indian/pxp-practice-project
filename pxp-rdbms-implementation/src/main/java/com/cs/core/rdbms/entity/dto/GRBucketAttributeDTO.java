package com.cs.core.rdbms.entity.dto;

import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketAttributeDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;

public class GRBucketAttributeDTO extends SimpleDTO implements IGRBucketAttributeDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Long              bucketId         = 0l;
  private String            attributeId      = "";
  private String            value            = "";
  
  public GRBucketAttributeDTO () {
    
  }
  
  public GRBucketAttributeDTO (Long bucketId, String attributeId, String value) throws Exception{
    this.bucketId = bucketId;
    this.attributeId = attributeId;
    this.value = value;
  }
  
  public GRBucketAttributeDTO (IResultSetParser result) throws Exception{
    this.bucketId = result.getLong(IGRBucketAttributeDTO.BUCKET_ID);
    this.attributeId = result.getString(IGRBucketAttributeDTO.ATTRIBUTE_ID);
    this.value = result.getString(IGRBucketAttributeDTO.VALUE);
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public Long getBucketId()
  {
    return bucketId;
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

}
