package com.cs.core.config.interactor.usecase.calculatedattribute;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.calculatedattribute.CalculatedAttributeMappingModel;
import com.cs.core.config.interactor.model.calculatedattribute.ICalculatedAttributeMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class GetMappingForCalculatedAttributes
    extends AbstractGetConfigInteractor<IModel, ICalculatedAttributeMappingModel>
    implements IGetMappingForCalculatedAttributes {
  
  @Override
  public ICalculatedAttributeMappingModel executeInternal(IModel dataModel) throws Exception
  {
    InputStream stream = GetMappingForCalculatedAttributes.class.getClassLoader()
        .getResourceAsStream("CalculatedAttributeAllowedTypesAndConversionMapping.json");
    Reader reader = new InputStreamReader(stream, StandardCharsets.ISO_8859_1);
    Map<String, Object> mapping = ObjectMapperUtil.readValue(reader,
        new TypeReference<Map<String, Object>>()
        {
          
        });
    return new CalculatedAttributeMappingModel(mapping);
  }
}
