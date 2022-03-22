package com.cs.core.runtime.interactor.entity.language;

import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.datarule.RuleViolation;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndVersionId;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class LanguageKlassInstance implements ILanguageKlassInstance {
  
  private static final long                           serialVersionUID = 1L;
  protected String                                    name;
  protected String                                    commonInstanceId;
  protected List<? extends IContentAttributeInstance> dependentAttributes;
  protected Long                                      versionId;
  protected List<IRuleViolation>                      ruleViolation;
  protected String                                    language;
  protected IMessageInformation                       messages;
  protected List<IIdAndVersionId>                     attributeVariants;
  protected Long                                      iid;
  
  @Override
  public String getCommonInstanceId()
  {
    return commonInstanceId;
  }
  
  @Override
  public void setCommonInstanceId(String commonInstanceId)
  {
    this.commonInstanceId = commonInstanceId;
  }
  
  @Override
  public List<? extends IContentAttributeInstance> getDependentAttributes()
  {
    if (dependentAttributes == null) {
      dependentAttributes = new ArrayList<>();
    }
    return dependentAttributes;
  }
  
  @Override
  public void setDependentAttributes(List<? extends IContentAttributeInstance> dependentAttributes)
  {
    this.dependentAttributes = dependentAttributes;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getLanguage()
  {
    return language;
  }
  
  @Override
  public void setLanguage(String language)
  {
    this.language = language;
  }
  
  @Override
  public List<IRuleViolation> getRuleViolation()
  {
    if (ruleViolation == null) {
      ruleViolation = new ArrayList<>();
    }
    return ruleViolation;
  }
  
  @JsonDeserialize(contentAs = RuleViolation.class)
  @Override
  public void setRuleViolation(List<IRuleViolation> ruleViolation)
  {
    this.ruleViolation = ruleViolation;
  }
  
  @Override
  public IMessageInformation getMessages()
  {
    if (messages == null) {
      messages = new MessageInformation();
    }
    return messages;
  }
  
  @JsonDeserialize(as = MessageInformation.class)
  @Override
  public void setMessages(IMessageInformation messages)
  {
    this.messages = messages;
  }
  
  @Override
  @JsonIgnore
  public String getCreatedBy()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setCreatedBy(String createdBy)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getCreatedOn()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setCreatedOn(Long createdOn)
  {
  }
  
  /*@Override
  @JsonIgnore
  public String getOwner()
  {
    return null;
  }*/
  
  /*@Override
  @JsonIgnore
  public void setOwner(String owner)
  {
  
  }*/
  
  @Override
  @JsonIgnore
  public Long getLastModified()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setLastModified(Long lastModified)
  {
  }
  
  @Override
  @JsonIgnore
  public String getJobId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setJobId(String jobId)
  {
  }
  
  @JsonIgnore
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public List<IIdAndVersionId> getAttributeVariants()
  {
    if (attributeVariants == null) {
      attributeVariants = new ArrayList<>();
    }
    return attributeVariants;
  }
  
  @Override
  public void setAttributeVariants(List<IIdAndVersionId> attributeVariants)
  {
    this.attributeVariants = attributeVariants;
  }
  
  @Override
  public Long getIid()
  {
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
