package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.model.duplicatecode.AbstractElementConflictingValuesModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElementConflictingValuesCustomDeserializer
    extends StdDeserializer<List<IElementConflictingValuesModel>> {
  
  public ElementConflictingValuesCustomDeserializer()
  {
    this(null);
  }
  
  protected ElementConflictingValuesCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public List<IElementConflictingValuesModel> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    List<IElementConflictingValuesModel> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<ArrayList<AbstractElementConflictingValuesModel>>()
        {
          
        });
    return returnList;
  }
}
