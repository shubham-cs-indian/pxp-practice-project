package com.cs.core.rdbms.entity.dto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsContentDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTOBuilder;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;

/**
 * @author vallee
 */
public class TagsRecordDTO extends PropertyRecordDTO implements ITagsRecordDTO {
  
  public static final String TAG_RECORD_IDS = PXONTag.tag.toJSONArrayTag();
  private ITagsContentDTO    tagRecords;

  /**
   * Enabled default constructor
   */
  public TagsRecordDTO()
  {
    tagRecords = new TagsContentDTO();
  }
  
  /**
   * Value constructor
   *
   * @param entityIID
   * @param property
   */
  public TagsRecordDTO(long entityIID, IPropertyDTO property)
  {
    super(entityIID, property);
    tagRecords = new TagsContentDTO();
  }
  
  /**
   * Load tags from a query result returning aggregated tags
   *
   * @param parser
   * @param property
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public TagsRecordDTO(IResultSetParser parser, IPropertyDTO property)
      throws SQLException, CSFormatException
  {
    super(parser, property);
    String tags = parser.getString("usrTags");
    this.tagRecords = new TagsContentDTO(tags);
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject gcse = initCSExpressID(CSEObjectType.TagsRecord);
    
    if (isVersionable)
      gcse.setSpecification(Keyword.$isver, Keyword.$true);
    
    return gcse;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    super.fromCSExpressID(cse);
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    for (Object recordJSON : parser.getJSONArray(TAG_RECORD_IDS)) {
      JSONContentParser recordParser = new JSONContentParser((JSONObject) recordJSON);
      TagDTO tag = new TagDTO();
      tag.fromJSON(recordParser);
      tagRecords.getTags().add(tag);
    }
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        !tagRecords.getTags().isEmpty() ? JSONBuilder.newJSONArray(TAG_RECORD_IDS, tagRecords.getTags())
            : JSONBuilder.VOID_FIELD);
  }
  
  @Override
  public long getEntityIID()
  {
    return getIID(); // the owner of the tags record
  }
  
  @Override
  public boolean isEmpty()
  {
    return (tagRecords.getTags().isEmpty());
  }
  
  @Override
  public Set<ITagDTO> getTags()
  {
    return tagRecords.getTags();
  }
  
  @Override
  public void setTags(ITagDTO... tags)
  {
    this.tagRecords.getTags().clear();
    setChanged(true);
    this.tagRecords.getTags().addAll(Arrays.asList(tags));
  }
  
  /**
   * @return the tag value IIDs in a list
   */
  public List<String> getTagValueCodes()
  {
    List<String> tagvalueiidlist = new ArrayList<>();
    getTags().forEach((dto) -> {
      tagvalueiidlist.add(dto.getTagValueCode());
    });
    return tagvalueiidlist;
  }
  
  @Override
  public void setContentFrom(IPropertyRecordDTO source)
  {
    TagsRecordDTO tagSource = (TagsRecordDTO) source;
    this.tagRecords.getTags().clear();
    setChanged(true);
    this.tagRecords.getTags().addAll(tagSource.getTags());
  }
  
  @Override
  public void mergeTags(ITagDTO... tags)
  {
    boolean mergeTags = tagRecords.mergeTags(tags);
    setChanged(mergeTags);
  }
  
  /**
   * @return the list of tag ranges per order
   */
  public List<Integer> getTagRanges()
  {
    List<Integer> tagValueRelevances = new ArrayList<>();
    getTags().forEach((dto) -> {
      tagValueRelevances.add(dto.getRange());
    });
    return tagValueRelevances;
  }
  
  @Override
  public ITagDTO getTagByCode(String tagValueCode)
  {
    return tagRecords.getTagByCode(tagValueCode);
  }
  
  public String getBetterTagValueCode()
  {
    return ((TagsContentDTO)tagRecords).getBetterTagValueCode();
  }
  
  @Override
  public int hashCode()
  {
    return super.hashCode();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    return super.equals(obj);
  }
  
  
  /**
   * implementation of ITagsRecordDTOBuilder
   * @author Janak.Gurme
   *
   */
  public static class TagsRecordDTOBuilder implements ITagsRecordDTOBuilder {
    
    private final TagsRecordDTO tagsRecordDTO;
    
    /**
     * minimal mandatory fields to prepare TagsRecordDTO
     * @param entityIID
     * @param property
     */
    public TagsRecordDTOBuilder(long entityIID, IPropertyDTO property)
    {
      tagsRecordDTO = new TagsRecordDTO(entityIID, property);
    }
    
    @Override
    public ITagsRecordDTOBuilder tags(ITagDTO... tags)
    {
      tagsRecordDTO.setTags(tags);
      return this;
    }
    
    @Override
    public ITagsRecordDTOBuilder recordStatus(RecordStatus recordStatus)
    {
      tagsRecordDTO.recordStatus = recordStatus;
      return this;
    }
    
    @Override
    public ITagsRecordDTOBuilder couplingType(CouplingType couplingType)
    {
      tagsRecordDTO.couplingType = couplingType;
      return this;
    }
    
    @Override
    public ITagsRecordDTOBuilder isVersionable(boolean isVersionable)
    {
      tagsRecordDTO.isVersionable = isVersionable;
      return this;
    }
    
    @Override
    public ITagsRecordDTO build()
    {
      return tagsRecordDTO;
    }

  }

  /**
   * @return combination of tag Value and tag range in form of hstore
   */
  public String getHStoreFormat()
  {
   return ((TagsContentDTO)tagRecords).getHStoreFormat();
  }

}
