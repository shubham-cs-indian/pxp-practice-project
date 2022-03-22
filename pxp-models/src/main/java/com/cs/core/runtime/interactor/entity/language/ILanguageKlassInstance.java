package com.cs.core.runtime.interactor.entity.language;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;
import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndVersionId;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import java.util.List;

public interface ILanguageKlassInstance extends IRuntimeEntity {
  
  String COMMON_INSTANCE_ID   = "commonInstanceId";
  String DEPENDENT_ATTRIBUTES = "dependentAttributes";
  String RULE_VIOLATION       = "ruleViolation";
  String NAME                 = "name";
  String LANGUAGE             = "language";
  String MESSAGES             = "messages";
  String ATTRIBUTE_VARIANTS   = "attributeVariants";
  
  public String getCommonInstanceId();
  
  public void setCommonInstanceId(String commonInstanceId);
  
  public List<? extends IContentAttributeInstance> getDependentAttributes();
  
  public void setDependentAttributes(List<? extends IContentAttributeInstance> dependentAttributes);
  
  public String getName();
  
  public void setName(String name);
  
  public List<IRuleViolation> getRuleViolation();
  
  public void setRuleViolation(List<IRuleViolation> ruleViolation);
  
  public String getLanguage();
  
  public void setLanguage(String language);
  
  public IMessageInformation getMessages();
  
  public void setMessages(IMessageInformation messages);
  
  public List<IIdAndVersionId> getAttributeVariants();
  
  public void setAttributeVariants(List<IIdAndVersionId> attributeVariants);
}
