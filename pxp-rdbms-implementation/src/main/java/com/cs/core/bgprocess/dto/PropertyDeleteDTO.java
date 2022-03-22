package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IPropertyDeleteDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;
import java.util.HashSet;
import java.util.Set;

public class PropertyDeleteDTO extends SimpleDTO implements IPropertyDeleteDTO {
  
  public static final String DELETED_PROPERTY = "deletedProperty";
  
  private Set<IPropertyDTO>  properties       = new HashSet<>();

  @Override
  public Set<IPropertyDTO> getProperties()
  {
    return properties;
  }

  @Override
  public void setProperties(Set<IPropertyDTO> properties)
  {
    this.properties = properties;
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    properties.clear();
    json.getJSONArray(DELETED_PROPERTY)
        .forEach((property) -> {
          try {
            IPropertyDTO propertyDTO = new PropertyDTO();
            propertyDTO.fromJSON(property.toString());
            properties.add(propertyDTO);
          }
          catch (CSFormatException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return  JSONBuilder.newJSONArray(DELETED_PROPERTY, properties);
  }
  
}
