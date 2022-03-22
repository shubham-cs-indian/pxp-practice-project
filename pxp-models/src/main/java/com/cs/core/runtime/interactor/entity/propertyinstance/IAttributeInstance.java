package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import java.util.List;

public interface IAttributeInstance extends IContentAttributeInstance {
  
  public static final String VALUE                 = "value";
  public static final String VALUE_AS_NUMBER       = "valueAsNumber";
  public static final String VALUE_AS_HTML         = "valueAsHtml";
  public static final String VALUE_AS_EXPRESSION   = "valueAsExpression";
  public static final String IS_MANDATORY_VIOLATED = "isMandatoryViolated";
  public static final String IS_SHOULD_VIOLATED    = "isShouldViolated";
  public static final String IS_MATCH_AND_MERGE    = "isMatchAndMerge";
  public static final String CONTEXT               = "context";
  public static final String ORIGINAL_INSTANCE_ID  = "originalInstanceId";
  public static final String IS_CONFLICT_RESOLVED  = "isConflictResolved";
  public static final String LANGUAGE              = "language";
  public static final String CODE                  = "code";
  
  public String getValue();
  
  public void setValue(String baseType);
  
  public Double getValueAsNumber();
  
  public void setValueAsNumber(Double valueAsNumber);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public List<IConcatenatedOperator> getValueAsExpression();
  
  public void setValueAsExpression(List<IConcatenatedOperator> valueAsExpression);
  
  public Boolean getIsMandatoryViolated();
  
  public void setIsMandatoryViolated(Boolean isMandatoryViolated);
  
  public Boolean getIsShouldViolated();
  
  public void setIsShouldViolated(Boolean isShouldViolated);
  
  public Boolean getIsMatchAndMerge();
  
  public void setIsMatchAndMerge(Boolean isMatchAndMerge);
  
  public IContextInstance getContext();
  
  public void setContext(IContextInstance context);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public Boolean getIsConflictResolved();
  
  public void setIsConflictResolved(Boolean isConflictResolved);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLanguage();
  
  public void setLanguage(String language);
}
