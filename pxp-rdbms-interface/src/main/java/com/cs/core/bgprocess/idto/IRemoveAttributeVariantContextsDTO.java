package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IRemoveAttributeVariantContextsDTO extends ISimpleDTO{
  
  public static final String ATTRIBUTE_ID          = "attributeId";
  public static final String ATTRIBUTE_CONTEXT_IDS = "attributeContextIds";
  
  public String getAttributeId();
  public void setAttributeId(String attributeId);
  
  public List<String> getAttributeContextsIds();
  public void setAttributeContextsIds(List<String> attributeContextsIds);
}
