package com.cs.core.rdbms.entity.dto;

import com.cs.core.data.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.postgresql.util.HStoreConverter;

import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsContentDTO;
import com.cs.core.technical.exception.CSFormatException;

/**
 * Helper class to manage a set of tags in various formats
 * @author vallee
 */
public class TagsContentDTO extends SimpleDTO implements ITagsContentDTO {
 
  public static final String TAG_RECORD_IDS = IPXON.PXONTag.tag.toJSONArrayTag();
  private Set<ITagDTO>       tagRecords     = new TreeSet<>();
  
  /**
   * Enabled default constructor
   */
  public TagsContentDTO()
  {
  }
 
  /**
   * @return combination of tag Value and tag range in form of hstore
   */
  public Set<ITagDTO> getTags()
  {
    return tagRecords;
  }
  
  @Override
  public void setTags(ITagDTO... tags)
  {
    this.tagRecords.clear();
    this.tagRecords.addAll(Arrays.asList(tags));
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
  public boolean mergeTags(ITagDTO... tags)
  {
    boolean isChanged = false;
    for (ITagDTO tag : tags) {
      if (!tagRecords.add(tag)) { // Whenever a duplicate already exist, then
        tagRecords.remove(tag); // The former one will be removed based on
        tagRecords.add(tag); // The new one is added with its new values
        isChanged = true ;
      }
    }
    return isChanged;
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
    for (ITagDTO tag : tagRecords) {
      if (tag.getTagValueCode().equals(tagValueCode))
        return tag;
    }
    return null;
  }
  
  /**
   * @return the first tag code found with range = 100 or the tag code with the best range value by default
   */
  public String getBetterTagValueCode()
  {
    String[] betterTagCode = { "" };
    for (ITagDTO tag : tagRecords) {
      if (tag.getRange() == 100) {
        betterTagCode[0] = tag.getTagValueCode();
      }
    }
    return betterTagCode[0];
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

  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    for (Object recordJSON : parser.getJSONArray(TAG_RECORD_IDS)) {
      JSONContentParser recordParser = new JSONContentParser((JSONObject) recordJSON);
      TagDTO tag = new TagDTO();
      tag.fromJSON(recordParser);
      tagRecords.add(tag);
    }
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer( !tagRecords.isEmpty() ? JSONBuilder.newJSONArray(TAG_RECORD_IDS, tagRecords)
            : JSONBuilder.VOID_FIELD);
  }
  
  /**
   * Overwrite content from a tag specification
   * @param tagSpec
   */
  public void fromCSETagSpecification( String tagSpec) throws CSFormatException {
    tagRecords.clear();
    String[] tagCodeSpecs = tagSpec.split(",");
    for( String tagCodeSpec: tagCodeSpecs ) {
      if ( !tagCodeSpec.contains("[") ) {
        tagRecords.add( new TagDTO( tagCodeSpec, 100));
      } else {
        String[] tagCodeSpecElts =  tagCodeSpec.split("(\\[|\\])");
        if ( tagCodeSpecElts.length < 2 )
          throw new CSFormatException( "Wrong CSE Tag code in " + tagSpec);
        tagRecords.add( new TagDTO( tagCodeSpecElts[0], Integer.parseInt(tagCodeSpecElts[1])));
      }
    }
  }

  /**
   * @return a tag specification from the tag list
   */
  public String toCSETagSpecification() {
    List<String> tagCodeSpecs = new ArrayList<>();
    getTags().forEach((dto) -> {
      tagCodeSpecs.add( String.format( "%s[%d]", dto.getTagValueCode(), dto.getRange()));
    });
    return Text.join( ",", tagCodeSpecs);
  }

  /**
   * Build a tags content from a String hstore content
   * @param hstoreValue
   */
  public TagsContentDTO(String hstoreValue) // TODO: to be moved and generalized at driver level
  {
    Map<String, String> tagValue = HStoreConverter.fromString(hstoreValue);
    this.tagRecords.addAll(tagValue.keySet()
        .stream()
        .map(tag -> new TagDTO(tag, Integer.parseInt(tagValue.get(tag))))
        .collect(Collectors.toSet()));
  }

  /**
   * @return combination of tag Value and tag range in form of hstore
   */
  public String getHStoreFormat() // TODO: to be moved and generalized at driver level
  {
    Map<String, String> userTag = new HashMap<>();
    for (ITagDTO tag : tagRecords) {
      if (tag.getRange() != 0) {
        userTag.put(tag.getTagValueCode(), String.valueOf(tag.getRange()));
      }
    }
    String hStoreValue = HStoreConverter.toString(userTag);
    return hStoreValue;
  }
}
