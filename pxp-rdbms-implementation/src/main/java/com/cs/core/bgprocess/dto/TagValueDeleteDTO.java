package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.ITagValueDeleteDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;
import java.util.HashSet;
import java.util.Set;

public class TagValueDeleteDTO extends SimpleDTO implements ITagValueDeleteDTO{
  
  public static final String DELETED_TAG_VALUES = "deletedTagValues";
  
  private Set<String>  tagValues          = new HashSet<>();
  
  public void setTagValues(Set<String> tagValues)
  {
    this.tagValues.clear();
    this.tagValues.addAll(tagValues);
  }
  
  public Set<String> getTagValues()
  {
    return tagValues;
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return  JSONBuilder.newJSONStringArray(DELETED_TAG_VALUES, tagValues);
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    tagValues.clear();
    json.getJSONArray(DELETED_TAG_VALUES)
        .forEach((tagValue) -> {
          tagValues.add(tagValue.toString());
        });
    
  }
  
}
