package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.io.IOException;
import java.util.Map;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ConfiguredRelsPerNatureRelsCustomDeserializer extends StdDeserializer<Map<String, IRelationshipConflictingSource>>{
  
  private static final long serialVersionUID = 1L;

  public ConfiguredRelsPerNatureRelsCustomDeserializer()
  {
    this(null);
  }
  
  protected ConfiguredRelsPerNatureRelsCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public Map<String, IRelationshipConflictingSource> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    Map<String, IRelationshipConflictingSource> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<Map<String, RelationshipConflictingSource>>()
        {
        });
    return returnList;
  }
}