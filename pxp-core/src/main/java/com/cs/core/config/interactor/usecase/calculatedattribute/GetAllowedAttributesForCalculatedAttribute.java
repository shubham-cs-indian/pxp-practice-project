package com.cs.core.config.interactor.usecase.calculatedattribute;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.calculatedattribute.IGetAllowedAttributesForCalculatedAttributeRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataAttributeResponseModel;
import com.cs.core.config.strategy.usecase.calculatedattribute.IGetAllowedAttributesForCalculatedAttributeStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class GetAllowedAttributesForCalculatedAttribute extends
    AbstractGetConfigInteractor<IGetAllowedAttributesForCalculatedAttributeRequestModel, IGetConfigDataAttributeResponseModel>
    implements IGetAllowedAttributesForCalculatedAttribute {
  
  @Autowired
  protected IGetAllowedAttributesForCalculatedAttributeStrategy getAllowedAttributesForCalculatedAttributeStrategy;
  
  @Override
  public IGetConfigDataAttributeResponseModel executeInternal(
      IGetAllowedAttributesForCalculatedAttributeRequestModel dataModel) throws Exception
  {
    InputStream stream = GetMappingForCalculatedAttributes.class.getClassLoader()
        .getResourceAsStream("CalculatedAttributeAllowedTypesAndConversionMapping.json");
    Reader reader = new InputStreamReader(stream, StandardCharsets.ISO_8859_1);
    Map<String, Map<String, Map<String, String>>> mapping = ObjectMapperUtil.readValue(reader,
        new TypeReference<Map<String, Map<String, Map<String, String>>>>()
        {
          
        });
    dataModel.setMapping(mapping);
    return getAllowedAttributesForCalculatedAttributeStrategy.execute(dataModel);
  }
}
