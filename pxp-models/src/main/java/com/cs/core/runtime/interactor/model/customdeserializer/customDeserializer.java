package com.cs.core.runtime.interactor.model.customdeserializer;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class customDeserializer extends StdDeserializer<List<IKlassInstanceInformationModel>> {
  
  public customDeserializer()
  {
    this(null);
  }
  
  protected customDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  @Override
  public List<IKlassInstanceInformationModel> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    List<IKlassInstanceInformationModel> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<ArrayList<KlassInstanceInformationModel>>()
        {
          
        });
    return returnList;
  }
}
