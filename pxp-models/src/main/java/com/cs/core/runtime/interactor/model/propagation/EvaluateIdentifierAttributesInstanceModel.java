package com.cs.core.runtime.interactor.model.propagation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EvaluateIdentifierAttributesInstanceModel
    implements IEvaluateIdentifierAttributesInstanceModel {
  
  private static final long           serialVersionUID             = 1L;
  
  protected String                    klassInstanceId;
  protected String                    baseType;
  protected Boolean                   isKlassInstanceTypeChanged   = false;
  protected Boolean                   isIdentifierAttributeChanged = false;
  protected List<String>              changedLanguageIndependentAttributeIds;
  protected Map<String, List<String>> typeIdIdentifierAttributeIds;
  
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
  public Boolean getIsKlassInstanceTypeChanged()
  {
    return isKlassInstanceTypeChanged;
  }
  
  @Override
  public void setIsKlassInstanceTypeChanged(Boolean isKlassInstanceTypeChanged)
  {
    this.isKlassInstanceTypeChanged = isKlassInstanceTypeChanged;
  }
  
  @Override
  public Boolean getIsIdentifierAttributeChanged()
  {
    return isIdentifierAttributeChanged;
  }
  
  @Override
  public void setIsIdentifierAttributeChanged(Boolean isIdentifierAttributeChanged)
  {
    this.isIdentifierAttributeChanged = isIdentifierAttributeChanged;
  }
  
  @Override
  public List<String> getChangedAttributeIds()
  {
    if (changedLanguageIndependentAttributeIds == null) {
      changedLanguageIndependentAttributeIds = new ArrayList<>();
    }
    return changedLanguageIndependentAttributeIds;
  }
  
  @Override
  public void setChangedAttributeIds(List<String> changedLanguageIndependentAttributeIds)
  {
    this.changedLanguageIndependentAttributeIds = changedLanguageIndependentAttributeIds;
  }
  
  @Override
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds()
  {
    return typeIdIdentifierAttributeIds;
  }
  
  @Override
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds)
  {
    this.typeIdIdentifierAttributeIds = typeIdIdentifierAttributeIds;
  }
}
