package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.model.goldenrecord.AttributeRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IAttributeRecommendationModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Map;

public class DependentAttributesRecommendedCustomDeserializer
    extends StdDeserializer<Map<String, IAttributeRecommendationModel>> {
  
  private static final long serialVersionUID = 1L;
  
  public DependentAttributesRecommendedCustomDeserializer()
  {
    this(null);
  }
  
  protected DependentAttributesRecommendedCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public Map<String, IAttributeRecommendationModel> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    Map<String, IAttributeRecommendationModel> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<Map<String, AttributeRecommendationModel>>()
        {
          
        });
    
    return returnList;
  }
}
