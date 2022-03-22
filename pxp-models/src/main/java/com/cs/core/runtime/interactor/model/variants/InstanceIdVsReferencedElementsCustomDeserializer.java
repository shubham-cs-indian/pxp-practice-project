package com.cs.core.runtime.interactor.model.variants;

import java.io.IOException;
import java.util.Map;

import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class InstanceIdVsReferencedElementsCustomDeserializer
    extends StdDeserializer<Map<String, AbstractReferencedSectionElementModel>> {
  
  private static final long serialVersionUID = 1L;

  protected InstanceIdVsReferencedElementsCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  public InstanceIdVsReferencedElementsCustomDeserializer()
  {
    this(null);
  }
  

  @Override
  public Map<String, AbstractReferencedSectionElementModel> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    Map<String, AbstractReferencedSectionElementModel> object = ObjectMapperUtil.readValue(arg0,
        new TypeReference<Map<String, AbstractReferencedSectionElementModel>>()
        {
        });
    return object;
  }
  
}
