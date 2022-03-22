package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VariantDataTransferCustomDeserializer
    extends StdDeserializer<List<IGetVariantInfoForDataTransferResponseModel>> {
  
  private static final long serialVersionUID = 1L;
  
  public VariantDataTransferCustomDeserializer()
  {
    this(null);
  }
  
  protected VariantDataTransferCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public List<IGetVariantInfoForDataTransferResponseModel> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    List<IGetVariantInfoForDataTransferResponseModel> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<ArrayList<GetVariantInfoForDataTransferResponseModel>>()
        {
          
        });
    return returnList;
  }
}
