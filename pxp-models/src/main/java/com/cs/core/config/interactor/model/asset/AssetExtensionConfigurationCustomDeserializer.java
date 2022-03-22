package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssetExtensionConfigurationCustomDeserializer
    extends StdDeserializer<List<IAssetExtensionConfigurationModel>> {
  
  private static final long serialVersionUID = 1L;
  
  protected AssetExtensionConfigurationCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  public AssetExtensionConfigurationCustomDeserializer()
  {
    this(null);
  }
  
  @Override
  public List<IAssetExtensionConfigurationModel> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    List<IAssetExtensionConfigurationModel> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<ArrayList<AssetExtensionConfigurationModel>>()
        {
          
        });
    return returnList;
  }
}
