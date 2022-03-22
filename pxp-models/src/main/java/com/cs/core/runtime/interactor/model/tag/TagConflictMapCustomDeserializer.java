package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TagConflictMapCustomDeserializer extends StdDeserializer<List<IConflictingValue>> {
  
  private static final long serialVersionUID = 1L;
  
  public TagConflictMapCustomDeserializer()
  {
    this(null);
  }
  
  protected TagConflictMapCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public List<IConflictingValue> deserialize(JsonParser arg0, DeserializationContext arg1)
      throws IOException, JsonProcessingException
  {
    List<IConflictingValue> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<ArrayList<TagConflictingValue>>()
        {
          
        });
    return returnList;
  }
}
