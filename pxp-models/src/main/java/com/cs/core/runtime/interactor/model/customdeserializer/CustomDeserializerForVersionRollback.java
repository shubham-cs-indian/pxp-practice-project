package com.cs.core.runtime.interactor.model.customdeserializer;

import com.cs.core.config.interactor.model.versionrollback.IPropertyCouplingInformationModel;
import com.cs.core.config.interactor.model.versionrollback.PropertyCouplingInformationModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomDeserializerForVersionRollback
    extends StdDeserializer<Map<String, IPropertyCouplingInformationModel>> {
  
  private static final long serialVersionUID = 1L;
  
  public CustomDeserializerForVersionRollback()
  {
    this(null);
  }
  
  protected CustomDeserializerForVersionRollback(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public Map<String, IPropertyCouplingInformationModel> deserialize(JsonParser jsonParser,
      DeserializationContext ctxt) throws IOException, JsonProcessingException
  {
    Map<String, IPropertyCouplingInformationModel> returnMapForVersionRollback = ObjectMapperUtil
        .readValue(jsonParser,
            new TypeReference<HashMap<String, PropertyCouplingInformationModel>>()
            {
              
            });
    return returnMapForVersionRollback;
  }
}
