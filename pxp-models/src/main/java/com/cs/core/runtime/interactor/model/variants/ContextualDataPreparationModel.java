package com.cs.core.runtime.interactor.model.variants;

import java.util.Map;

public class ContextualDataPreparationModel implements IContextualDataPreparationModel {
  
  private static final long                             serialVersionUID = 1L;
  protected String                                      sourceContentId;
  protected String                                      klassInstanceId;
  protected String                                      baseType;
  protected Map<String, IPropagableContextualDataModel> contextualData;
  
  @Override
  public String getSourceContentId()
  {
    return sourceContentId;
  }
  
  @Override
  public void setSourceContentId(String sourceContentId)
  {
    this.sourceContentId = sourceContentId;
  }
  
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
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Map<String, IPropagableContextualDataModel> getContextualData()
  {
    return contextualData;
  }
  
  @Override
  public void setContextualData(Map<String, IPropagableContextualDataModel> contextualData)
  {
    this.contextualData = contextualData;
  }
}
