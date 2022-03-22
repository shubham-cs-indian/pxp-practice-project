package com.cs.core.runtime.interactor.model.fileinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.fileinstance.OnboardingFileInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IOnboardingFileInstance;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.summary.IKlassInstanceVersionSummary;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.List;

@JsonTypeInfo(use = Id.NONE)
public class OnboardingFileInstanceModel extends AbstractContentInstance
    implements IOnboardingFileInstanceModel {
  
  private static final long         serialVersionUID = 1L;
  
  protected IOnboardingFileInstance entity;
  
  Boolean                           isSkipped;
  
  public OnboardingFileInstanceModel()
  {
    entity = new OnboardingFileInstance();
  }
  
  public OnboardingFileInstanceModel(IOnboardingFileInstance entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  /*@Override
  public void setAssets(List<? extends IAssetAttributeInstance> assets)
  {
    entity.setAssets(assets);
  }
  */
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  /* @JsonIgnore
  @Override
  public List<? extends IAssetAttributeInstance> getAssets()
  {
    return entity.getAssets();
  }*/
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getName()
  {
    return entity.getName();
  }
  
  @Override
  public void setName(String name)
  {
    entity.setName(name);
  }
  
  @Override
  public List<IContentTagInstance> getTags()
  {
    return (List<IContentTagInstance>) entity.getTags();
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    entity.setTags(tags);
  }
  
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    return entity.getAttributes();
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributeInstances)
  {
    entity.setAttributes(attributeInstances);
  }
  
  @Override
  public List<IRoleInstance> getRoles()
  {
    return (List<IRoleInstance>) entity.getRoles();
  }
  
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    entity.setRoles(roles);
  }
  
  @Override
  public List<String> getTypes()
  {
    return entity.getTypes();
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    entity.setTypes(types);
  }
  
  @Override
  public String getCreatedBy()
  {
    return entity.getCreatedBy();
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    entity.setCreatedBy(createdBy);
  }
  
  @Override
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    entity.setCreatedOn(createdOn);
  }
  
  /*@Override
  public String getOwner()
  {
    return entity.getOwner();
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    entity.setOwner(owner);
  }*/
  
  @Override
  public String getBaseType()
  {
    return entity.getBaseType();
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    entity.setBaseType(baseType);
  }
  
  @Override
  public Long getLastModified()
  {
    return entity.getLastModified();
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    entity.setLastModified(lastModified);
  }
  
  @Override
  @JsonIgnore
  public String getBranchOf()
  {
    return entity.getBranchOf();
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    entity.setBranchOf(branchOf);
  }
  
  @JsonIgnore
  @Override
  public List<IRuleViolation> getRuleViolation()
  {
    return entity.getRuleViolation();
  }
  
  @Override
  public void setRuleViolation(List<IRuleViolation> ruleViolation)
  {
    entity.setRuleViolation(ruleViolation);
  }
  
  @JsonIgnore
  @Override
  public IMessageInformation getMessages()
  {
    return entity.getMessages();
  }
  
  @Override
  public void setMessages(IMessageInformation messages)
  {
    entity.setMessages(messages);
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return entity.getDefaultAssetInstanceId();
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    entity.setDefaultAssetInstanceId(defaultAssetInstanceId);
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getJobId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setJobId(String jobId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getVersionOf()
  {
    
    return entity.getVersionOf();
  }
  
  @Override
  public void setVersionOf(String versionOf)
  {
    entity.setVersionOf(versionOf);
  }
  
  @Override
  public String getComponentId()
  {
    return entity.getComponentId();
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    entity.setComponentId(componentId);
  }
  
  @Override
  public IContextInstance getContext()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setContext(IContextInstance context)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public IKlassInstanceVersionSummary getSummary()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setSummary(IKlassInstanceVersionSummary summary)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getKlassInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setKlassInstanceId(String masterContentId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getParentId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setParentId(String parentVariantId)
  {
    // TODO Auto-generated method stub
    
  }
  
  /*@Override
  public Boolean getIsFromExternalSource()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  
  /*@Override
  public void setIsFromExternalSource(Boolean isFromExternalSource)
  {
    // TODO Auto-generated method stub
  
  }*/
  
  /*@Override
  public Boolean getIsSkipped()
  {
    // TODO Auto-generated method stub
    return isSkipped;
  }*/
  
  /*@Override
  public void setIsSkipped(Boolean isSkipped)
  {
    this.isSkipped = isSkipped;
  }*/
  
}
