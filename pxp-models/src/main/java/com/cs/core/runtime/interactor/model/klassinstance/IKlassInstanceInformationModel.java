package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.datarule.IRuleViolationModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.ISearchHitInfoModel;

public interface IKlassInstanceInformationModel extends IRuntimeModel {
  
  public static final String NAME                                = "name";
  public static final String ATTRIBUTES                          = "attributes";
  public static final String TYPES                               = "types";
  // public static final String OWNER = "owner";
  public static final String GLOBAL_PERMISSION                   = "globalPermission";
  public static final String TAGS                                = "tags";
  public static final String LAST_MODIFIED                       = "lastModified";
  public static final String ID                                  = "id";
  public static final String ORIGINAL_INSTANCE_ID                = "originalInstanceId";
  public static final String VERSION_ID                          = "versionId";
  public static final String BRANCH_OF                           = "branchOf";
  public static final String LAST_MODIFIED_BY                    = "lastModifiedBy";
  public static final String BASE_TYPE                           = "baseType";
  public static final String ROLES                               = "roles";
  public static final String RELEVANCE                           = "relevance";
  public static final String RULE_VIOLATION                      = "ruleViolation";
  public static final String MESSAGES                            = "messages";
  public static final String HITS                                = "hits";
  public static final String COUNT                               = "count";
  public static final String BRANCH_OF_LABEL                     = "branchOfLabel";
  public static final String MANDATORY_VIOLATION_COUNT           = "mandatoryViolationCount";
  public static final String SHOULD_VIOLATION_COUNT              = "shouldViolationCount";
  public static final String VERSION_OF_LABEL                    = "versionOfLabel";
  public static final String APPLY_EFFECT                        = "applyEffect";
  public static final String DATA_FOR_UNIQUENESS_STAT_EVALUATION = "dataForUniquenessStatEvaluation";
  public static final String IS_UNIQUE_VIOLATION_COUNT           = "isUniqueViolationCount";
  public static final String CREATED_ON                          = "createdOn";
  public static final String LANGUAGE_CODES                      = "languageCodes";
  public static final String CREATION_LANGUAGE                   = "creationLanguage";
  public static final String EVENT_SCHEDULE                      = "eventSchedule";
  public static final String SELECTED_TAXONOMY_IDS               = "selectedTaxonomyIds";
  public static final String DEFAULT_ASSET_INSTANCE_ID           = "defaultAssetInstanceId";
  public static final String ASSET_INFORMATION                   = "assetInformation";
  public static final String IS_ASSET_EXPIRED                    = "isAssetExpired";
  public static final String IS_DUPLICATE                        = "isDuplicate";
  public static final String VARIANT_OF_LABEL                    = "variantOfLabel";
  public static final String VARIANT_OF                          = "variantOf";
  public static final String REFERENCED_ASSETS                   = "referencedAssets";

  
  public String getName();
  
  public void setName(String name);
  
  public List<? extends IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<? extends IContentAttributeInstance> attributes);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  /*public void setOwner(String owner);
  public String getOwner();*/
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public List<? extends IContentTagInstance> getTags();
  
  public void setTags(List<? extends IContentTagInstance> tags);
  
  public Long getLastModified();
  
  public void setLastModified(Long lastModified);
  
  public String getId();
  
  public void setId(String id);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public String getBranchOf();
  
  public void setBranchOf(String branchOf);
  
  public String getLastModifiedBy();
  
  public void setLastModifiedBy(String lastModifiedBy);
  
  public String getBaseType();
  
  public void setBaseType(String type);
  
  List<? extends IRoleInstance> getRoles();
  
  void setRoles(List<? extends IRoleInstance> roles);
  
  public Integer getRelevance();
  
  public void setRelevance(Integer setRelevance);
  
  public List<IRuleViolationModel> getRuleViolation();
  
  public void setRuleViolation(List<IRuleViolationModel> ruleViolation);
  
  public IMessageInformation getMessages();
  
  public void setMessages(IMessageInformation messages);
  
  List<ISearchHitInfoModel> getHits();
  
  void setHits(List<ISearchHitInfoModel> hits);
  
  public int getCount();
  
  public void setCount(int count);
  
  public String getBranchOfLabel();
  
  public void setBranchOfLabel(String branchOfLabel);
  
  public Integer getMandatoryViolationCount();
  
  public void setMandatoryViolationCount(Integer mandatoryViolationCount);
  
  public Integer getShouldViolationCount();
  
  public void setShouldViolationCount(Integer shouldViolationCount);
  
  public String getVersionOfLabel();
  
  public void setVersionOfLabel(String versionOfLabel);
  
  public IApplyEffectModel getApplyEffect();
  
  public void setApplyEffect(IApplyEffectModel applyEffect);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getDataForUniquenessStatEvaluation();
  
  public void setDataForUniquenessStatEvaluation(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation);
  
  public Integer getIsUniqueViolationCount();
  
  public void setIsUniqueViolationCount(Integer isUniqueViolationCount);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getCreationLanguage();
  
  public void setCreationLanguage(String creationLanguage);
  
  public IEventInstanceSchedule getEventSchedule();
  
  public void setEventSchedule(IEventInstanceSchedule schedule);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public String getDefaultAssetInstanceId();
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId);
  
  public IAssetInformationModel getAssetInformation();
  
  public void setAssetInformation(IAssetInformationModel assetInformation);
  
  public Boolean getIsAssetExpired();
  public void setIsAssetExpired(Boolean isAssetExpired);
  
  public boolean getIsDuplicate();
  public void setIsDuplicate(boolean Duplicate);

  public String getVariantOfLabel();
  public void setVariantOfLabel(String variantOfLabel);
  
  public String getVariantOf();
  public void setVariantOf(String variantOf);
  
  public List<? extends IAssetAttributeInstanceInformationModel> getReferencedAssets();
  public void setReferencedAssets(List<? extends IAssetAttributeInstanceInformationModel> referencedAssets);
  
}
