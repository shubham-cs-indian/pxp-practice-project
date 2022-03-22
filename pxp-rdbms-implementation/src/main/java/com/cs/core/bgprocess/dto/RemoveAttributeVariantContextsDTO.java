package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.idto.IRemoveAttributeVariantContextsDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class RemoveAttributeVariantContextsDTO extends SimpleDTO implements IRemoveAttributeVariantContextsDTO{
  
  private static final long serialVersionUID = 1L;
  private String             attributeId;
  private final List<String> attributeContextsIds = new ArrayList<String>();

  
  public RemoveAttributeVariantContextsDTO()
  {

  }
  
  public RemoveAttributeVariantContextsDTO(String attributeId, List<String> attributeContextsIds){
    this.attributeId = attributeId;
    this.attributeContextsIds.clear();
    this.attributeContextsIds.addAll(attributeContextsIds);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONStringArray(ATTRIBUTE_CONTEXT_IDS, attributeContextsIds), 
        JSONBuilder.newJSONField(ATTRIBUTE_ID, attributeId));
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    attributeContextsIds.clear();
    json.getJSONArray(ATTRIBUTE_CONTEXT_IDS)
        .forEach((iid) -> {
          attributeContextsIds.add((String) iid);
        });
    attributeId = json.getString(ATTRIBUTE_ID);
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public List<String> getAttributeContextsIds()
  {
    return attributeContextsIds;
  }
  
  @Override
  public void setAttributeContextsIds(List<String> attributeContextsIds)
  {
    this.attributeContextsIds.clear();
    this.attributeContextsIds.addAll(attributeContextsIds);
  }
  
}
