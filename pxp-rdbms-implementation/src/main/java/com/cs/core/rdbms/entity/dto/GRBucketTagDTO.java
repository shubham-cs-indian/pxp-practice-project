package com.cs.core.rdbms.entity.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.postgresql.util.HStoreConverter;

import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketAttributeDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketTagDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsContentDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;

public class GRBucketTagDTO extends SimpleDTO implements IGRBucketTagDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Long              bucketId         = 0l;
  private String            tagId            = "";
  private ITagsContentDTO   tagRecords;
  
  public GRBucketTagDTO()
  {
    tagRecords = new TagsContentDTO();
  }
  
  public GRBucketTagDTO(String tagId, Set<ITagDTO> tagValuesToSave)
  {
    this.tagId = tagId;
    tagRecords = new TagsContentDTO();
    tagRecords.setTags(tagValuesToSave.toArray(new ITagDTO[] {}));
  }
  
  public GRBucketTagDTO (IResultSetParser result) throws Exception{
    this.bucketId = result.getLong(IGRBucketAttributeDTO.BUCKET_ID);
      String tagValues = result.getString("value");
      this.tagRecords = new TagsContentDTO(tagValues);
      this.tagId = result.getString("tagid");
  }
  
  @Override
  public Long getBucketId()
  {
    return bucketId;
  }
  
  @Override
  public void setBucketId(Long bucketId)
  {
    this.bucketId = bucketId;
  }
  
  @Override
  public String getTagId()
  {
    return tagId;
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
  public String getHStoreFormat()
  {
    Map<String, String> userTag = new HashMap<>();
    for (ITagDTO tag : tagRecords.getTags()) {
      if (tag.getRange() != 0) {
        userTag.put(tag.getTagValueCode(), String.valueOf(tag.getRange()));
      }
    }
    String hStoreValue = HStoreConverter.toString(userTag);
    return hStoreValue;
  }
  
  @Override
  public List<String> getTagValueCodes() {
    return tagRecords.getTags().stream().map(tagRecord-> tagRecord.getTagValueCode()).collect(Collectors.toList());
  }
}
