package com.cs.core.config.interactor.model.configdetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementForSwitchTypeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ReferencedElementListCustomDeserializer
    extends StdDeserializer<List<IReferencedSectionElementModel>> {
  
  public ReferencedElementListCustomDeserializer()
  {
    this(null);
  }
  
  protected ReferencedElementListCustomDeserializer(Class<?> referencedElementList)
  {
    super(referencedElementList);
  }
  
  @Override
  public List<IReferencedSectionElementModel> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    List<IReferencedSectionElementModel> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<ArrayList<AbstractReferencedSectionElementForSwitchTypeModel>>()
        {
        });
    return returnList;
  }
}
