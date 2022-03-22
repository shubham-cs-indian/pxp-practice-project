package com.cs.core.runtime.interactor.model.customdeserializer;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.promotioninstance.IPromotionContextModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;

public class ContextCustomDeserializer extends StdDeserializer<IConfigEntityInformationModel> {
  
  private static final long serialVersionUID = 1L;
  
  public ContextCustomDeserializer()
  {
    this(null);
  }
  
  protected ContextCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public IConfigEntityInformationModel deserialize(JsonParser jsonParser,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode node = oc.readTree(jsonParser);
    
    String type = node.get(IConfigEntityInformationModel.TYPE)
        .asText();
    
    if (Arrays.asList("promotionContext", "priceContext")
        .contains(type)) {
      return ObjectMapperUtil.readValue(node.toString(), new TypeReference<IPromotionContextModel>()
      {
        
      });
    }
    
    return ObjectMapperUtil.readValue(node.toString(),
        new TypeReference<ConfigEntityInformationModel>()
        {
          
        });
  }
}
