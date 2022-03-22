package com.cs.core.runtime.interactor.usecase.comparison;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelationshipInstancesCustomDeserializer
    extends StdDeserializer<List<IRelationshipInstance>> {
  
  public RelationshipInstancesCustomDeserializer()
  {
    this(null);
  }
  
  protected RelationshipInstancesCustomDeserializer(Class<?> relationshipInstance)
  {
    super(relationshipInstance);
  }
  
  @Override
  public List<IRelationshipInstance> deserialize(JsonParser arg0, DeserializationContext arg1)
      throws IOException, JsonProcessingException
  {
    List<IRelationshipInstance> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<ArrayList<RelationshipInstance>>()
        {
        });
    return returnList;
  }
}
