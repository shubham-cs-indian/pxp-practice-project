package com.cs.core.runtime.interactor.model.propagation;

import java.util.List;
import java.util.Map;

public class EvaluateIdentifierAttributesStrategyModel
    implements IEvaluateIdentifierAttributesStrategyModel {
  
  protected String                    klassInstanceId;
  protected Map<String, List<String>> klassIdIdentifierAttributeIds;
  protected String                    baseType;
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public Map<String, List<String>> getKlassIdIdentifierAttributeIds()
  {
    return klassIdIdentifierAttributeIds;
  }
  
  @Override
  public void setKlassIdIdentifierAttributeIds(
      Map<String, List<String>> klassIdIdentifierAttributeIds)
  {
    this.klassIdIdentifierAttributeIds = klassIdIdentifierAttributeIds;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
