package com.cs.core.rdbms.entity.dto;

import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.technical.exception.CSFormatException;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * DTO Representation of a tag record (i.e. a tag value with a range)
 *
 * @author vallee
 */
public class TagDTO extends SimpleDTO implements ITagDTO {
  
  private static final String TAG          = PXONTag.tag.toReadOnlyTag();
  private static final String RANGE        = PXONTag.range.toTag();
  private String              tagValueCode = "";
  private int                 range        = 0;
  
  /**
   * Enabled default constructor
   */
  public TagDTO()
  {
  }
  
  /**
   * Ancestor constructor
   *
   * @param tagValue
   * @param relevance
   */
  public TagDTO(String tagValue, int relevance)
  {
    this.tagValueCode = tagValue;
    this.range = relevance;
  }
  
  /**
   * Factory method that build new Tag records from string arrays containing tag
   * data
   *
   * @param aggTagValueCodes
   *          array of tag value Codes
   * @param aggTagRelevances
   *          array of tag record relevances
   * @return the corresponding set of tag records
   */
  public static Set<ITagDTO> newTagRecords(String aggTagRelevances, String aggTagValueCodes)
  {
    Set<ITagDTO> tagRecords = new TreeSet<>();
    String[] tagValueCodes = aggTagValueCodes.split(",");
    String[] relevances = aggTagRelevances.split(",");
    for (int i = 0; i < tagValueCodes.length; i++) {
      if (!tagValueCodes[i].isEmpty()) {
        TagDTO tagRecord = new TagDTO(tagValueCodes[i], Integer.parseInt(relevances[i]));
        tagRecords.add(tagRecord);
      }
    }
    return tagRecords;
  }
  
  /**
   * Factory method that build new Tag records
   *
   * @param relevances
   *          array of tag record relevances
   * @param tagValueCodes
   *          array of tag value codes
   * @return the corresponding set of tag records
   */
  public static Set<ITagDTO> newTagRecords(Integer[] relevances, String[] tagValueCodes)
  {
    Set<ITagDTO> tagRecords = new TreeSet<>();
    for (int i = 0; i < tagValueCodes.length; i++) {
      if (!tagValueCodes[i].isEmpty()) {
        TagDTO tagRecord = new TagDTO(tagValueCodes[i], relevances[i]);
        tagRecords.add(tagRecord);
      }
    }
    return tagRecords;
  }
  
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    range = parser.getInt(RANGE);
    tagValueCode = parser.getString(TAG);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONField(TAG, tagValueCode),
        JSONBuilder.newJSONField(RANGE, range));
  }
  
  @Override
  public int getRange()
  {
    return range;
  }
  
  @Override
  public void setRange(int relevance)
  {
    this.range = relevance;
  }
  
  @Override
  public String getTagValueCode()
  {
    return tagValueCode;
  }
  
  /**
   * @param code
   *          overwritten tag value code
   */
  public void setTagValueCode(String code)
  {
    tagValueCode = code;
  }
  
  @Override
  public int compareTo(Object t)
  {
    TagDTO that = ((TagDTO) t);
    if (!tagValueCode.isEmpty() || !that.tagValueCode.isEmpty()) {
      return new CompareToBuilder().append(this.tagValueCode,that.tagValueCode).toComparison();
    }
    return 0;
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(5, 17).append(tagValueCode)
        .build();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TagDTO other = (TagDTO) obj;
    if (!new EqualsBuilder().append(tagValueCode, other.tagValueCode).isEquals()) {
      return false;
    }
    return true;
  }
}
