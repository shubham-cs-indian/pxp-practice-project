package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.configdetails.SearchHitInfoModel;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ApplyEffectModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.datarule.IRuleViolationModel;
import com.cs.core.runtime.interactor.model.datarule.RuleViolationModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.propagation.PropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.ISearchHitInfoModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KlassInstanceInformationModel implements IKlassInstanceInformationModel {
  
  private static final long                                                serialVersionUID        = 1L;
  
  protected IContentInstance                                               entity;
  
  protected String                                                         id;
  protected String                                                         originalInstanceId;
  protected String                                                         name;
  protected List<? extends IContentAttributeInstance>                      attributes;
  protected List<? extends IContentTagInstance>                            tags;
  protected List<String>                                                   types;
  protected String                                                         owner;
  protected String                                                         branchOf                = "-1";
  protected Long                                                           versionId;
  protected String                                                         lastModifiedBy;
  protected Long                                                           lastModified;
  protected IGlobalPermission                                              globalPermission;
  protected String                                                         baseType;
  protected List<? extends IRoleInstance>                                  roles;
  protected Integer                                                        relevance;
  protected List<IRuleViolationModel>                                      ruleViolation;
  protected IMessageInformation                                            messages;
  protected List<ISearchHitInfoModel>                                      hits;
  protected Integer                                                        count                   = 1;
  protected String                                                         branchOfLabel;
  protected String                                                         versionOfLabel;
  protected Integer                                                        mandatoryViolationCount = 0;
  protected Integer                                                        shouldViolationCount    = 0;
  protected IApplyEffectModel                                              applyEffect;
  protected List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation;
  protected Integer                                                        isUniqueViolationCount  = 0;
  protected Long                                                           createdOn;
  protected List<String>                                                   languageCodes;
  protected String                                                         creationLanguage;
  protected IEventInstanceSchedule                                         eventSchedule           = new EventInstanceSchedule();
  protected List<String>                                                   selectedTaxonomyIds;
  protected String                                                         defaultAssetInstanceId;
  protected IAssetInformationModel                                         assetInformation;
  protected Boolean                                                        isAssetExpired;
  protected boolean                                                        isDuplicate;
  protected String                                                         variantOfLabel;
  protected String                                                         variantOf;
  protected List<? extends IAssetAttributeInstanceInformationModel>        referencedAssets;
  
  public KlassInstanceInformationModel()
  {
  }
  
  public KlassInstanceInformationModel(IContentInstance entity)
  {
    this.entity = entity;
  }
  
  @Override
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
  }
  
  @Override
  public List<String> getLanguageCodes()
  {
    if (this.entity != null) {
      return this.entity.getLanguageCodes();
    }
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public String getId()
  {
    if (this.entity != null) {
      return this.entity.getId();
    }
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getBranchOf()
  {
    if (this.entity != null) {
      return this.entity.getBranchOf();
    }
    return branchOf;
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    this.branchOf = branchOf;
  }
  
  @Override
  public String getName()
  {
    if (this.entity != null) {
      return this.entity.getName();
    }
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    if (this.entity != null) {
      return this.entity.getAttributes();
    }
    return attributes;
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<? extends IContentTagInstance> getTags()
  {
    if (this.entity != null) {
      return (List<? extends IContentTagInstance>) this.entity.getTags();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<String> getTypes()
  {
    if (this.entity != null) {
      return this.entity.getTypes();
    }
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  /*@Override
  public String getOwner()
  {
    if (this.entity != null) {
      return this.entity.getOwner();
    }
    return owner;
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    this.owner = owner;
  }*/
  
  @Override
  public Long getVersionId()
  {
    if (this.entity != null) {
      return this.entity.getVersionId();
    }
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    if (this.entity != null) {
      return this.entity.getLastModifiedBy();
    }
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public Long getLastModified()
  {
    if (this.entity != null) {
      return this.entity.getLastModified();
    }
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public String getBaseType()
  {
    if (this.entity != null) {
      return this.entity.getBaseType();
    }
    return baseType;
  }
  
  @Override
  public void setBaseType(String type)
  {
    this.baseType = type;
  }
  
  @Override
  public List<? extends IRoleInstance> getRoles()
  {
    if (this.entity != null) {
      return this.entity.getRoles();
    }
    return roles;
  }
  
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public Integer getRelevance()
  {
    return relevance;
  }
  
  @Override
  public void setRelevance(Integer relevance)
  {
    this.relevance = relevance;
  }
  
  @Override
  public List<IRuleViolationModel> getRuleViolation()
  {
    if (ruleViolation == null) {
      ruleViolation = new ArrayList<>();
    }
    return ruleViolation;
  }
  
  @JsonDeserialize(contentAs = RuleViolationModel.class)
  @Override
  public void setRuleViolation(List<IRuleViolationModel> ruleViolation)
  {
    this.ruleViolation = ruleViolation;
  }
  
  @Override
  @JsonIgnore
  public IEntity getEntity()
  {
    return this.entity;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public IMessageInformation getMessages()
  {
    if (this.entity != null) {
      return this.entity.getMessages();
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
  public List<ISearchHitInfoModel> getHits()
  {
    if (hits == null) {
      hits = new ArrayList<>();
    }
    return hits;
  }
  
  @Override
  @JsonDeserialize(contentAs = SearchHitInfoModel.class)
  public void setHits(List<ISearchHitInfoModel> hits)
  {
    this.hits = hits;
  }
  
  @Override
  public int getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(int count)
  {
    this.count = count;
  }
  
  @Override
  public String getBranchOfLabel()
  {
    return branchOfLabel;
  }
  
  @Override
  public void setBranchOfLabel(String branchOfLabel)
  {
    this.branchOfLabel = branchOfLabel;
  }
  
  @Override
  public Integer getMandatoryViolationCount()
  {
    return mandatoryViolationCount;
  }
  
  @Override
  public void setMandatoryViolationCount(Integer mandatoryViolationCount)
  {
    this.mandatoryViolationCount = mandatoryViolationCount;
  }
  
  @Override
  public Integer getShouldViolationCount()
  {
    return shouldViolationCount;
  }
  
  @Override
  public void setShouldViolationCount(Integer shouldViolationCount)
  {
    this.shouldViolationCount = shouldViolationCount;
  }
  
  @Override
  public String getVersionOfLabel()
  {
    return versionOfLabel;
  }
  
  @Override
  public void setVersionOfLabel(String versionOfLabel)
  {
    this.versionOfLabel = versionOfLabel;
  }
  
  @Override
  public IApplyEffectModel getApplyEffect()
  {
    return applyEffect;
  }
  
  @JsonDeserialize(as = ApplyEffectModel.class)
  @Override
  public void setApplyEffect(IApplyEffectModel applyEffect)
  {
    this.applyEffect = applyEffect;
  }
  
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
  }
  
  @Override
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getDataForUniquenessStatEvaluation()
  {
    if (dataForUniquenessStatEvaluation == null) {
      dataForUniquenessStatEvaluation = new ArrayList<>();
    }
    return dataForUniquenessStatEvaluation;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyInstanceUniquenessEvaluationForPropagationModel.class)
  public void setDataForUniquenessStatEvaluation(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation)
  {
    this.dataForUniquenessStatEvaluation = dataForUniquenessStatEvaluation;
  }
  
  @Override
  public Integer getIsUniqueViolationCount()
  {
    return isUniqueViolationCount;
  }
  
  @Override
  public void setIsUniqueViolationCount(Integer isUniqueViolationCount)
  {
    this.isUniqueViolationCount = isUniqueViolationCount;
  }
  
  @Override
  public String getCreationLanguage()
  {
    return creationLanguage;
  }
  
  @Override
  public void setCreationLanguage(String creationLanguage)
  {
    this.creationLanguage = creationLanguage;
  }
  
  @Override
  public IEventInstanceSchedule getEventSchedule()
  {
    return eventSchedule;
  }
  
  @Override
  @JsonDeserialize(as = EventInstanceSchedule.class)
  public void setEventSchedule(IEventInstanceSchedule eventSchedule)
  {
    this.eventSchedule = eventSchedule;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    if(selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return this.defaultAssetInstanceId;
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    this.defaultAssetInstanceId = defaultAssetInstanceId;
  }
  
  @Override
  public IAssetInformationModel getAssetInformation() {
  	return this.assetInformation;
  }

  @Override
  public void setAssetInformation(IAssetInformationModel assetInformation) {
  	this.assetInformation = assetInformation;
  }
  
  @Override
  public Boolean getIsAssetExpired()
  {
    return isAssetExpired;
  }

  @Override
  public void setIsAssetExpired(Boolean isAssetExpired)
  {
    this.isAssetExpired = isAssetExpired;
  }

  @Override
  public boolean getIsDuplicate()
  {
    return this.isDuplicate;
  }

  @Override
  public void setIsDuplicate(boolean isDuplicate)
  {
    this.isDuplicate = isDuplicate;
  }
  
  public String getVariantOfLabel()
  {
    return variantOfLabel;
  }

  
  public void setVariantOfLabel(String variantOfLabel)
  {
    this.variantOfLabel = variantOfLabel;
  }
  
  public String getVariantOf()
  {
    return variantOf;
  }

  
  public void setVariantOf(String variantOf)
  {
    this.variantOf = variantOf;
  }
  
  @Override
  public List<? extends IAssetAttributeInstanceInformationModel> getReferencedAssets()
  {
    return referencedAssets;
  }
  
  @JsonDeserialize(contentAs = AssetAttributeInstanceInformationModel.class)
  @Override
  public void setReferencedAssets(
      List<? extends IAssetAttributeInstanceInformationModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }

}
