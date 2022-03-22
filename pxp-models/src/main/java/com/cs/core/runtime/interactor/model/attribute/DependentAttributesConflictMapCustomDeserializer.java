package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DependentAttributesConflictMapCustomDeserializer
    extends StdDeserializer<Map<String, List<IConflictingValue>>> {
  
  private static final long serialVersionUID = 1L;
  
  public DependentAttributesConflictMapCustomDeserializer()
  {
    this(null);
  }
  
  protected DependentAttributesConflictMapCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public Map<String, List<IConflictingValue>> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    Map<String, List<IConflictingValue>> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<Map<String, ArrayList<AttributeConflictingValue>>>()
        {
          
        });
    return returnList;
  }
}
