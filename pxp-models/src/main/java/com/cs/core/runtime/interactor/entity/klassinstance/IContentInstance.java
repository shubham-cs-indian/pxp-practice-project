package com.cs.core.runtime.interactor.entity.klassinstance;

import java.util.List;

import com.cs.core.runtime.interactor.entity.contentidentifier.IContentIdentifier;
import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndVersionId;
import com.cs.core.runtime.interactor.entity.language.ILanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflict;
import com.cs.core.runtime.interactor.entity.summary.IKlassInstanceVersionSummary;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;

public interface IContentInstance extends IKlassInstance {
  
  public static final String DEFAULT_ASSET_INSTANCE_ID = "defaultAssetInstanceId";
  public static final String RULE_VIOLATION            = "ruleViolation";
  public static final String MESSAGES                  = "messages";
  public static final String CONTEXT                   = "context";
  public static final String BRANCH_OF                 = "branchOf";
  public static final String VERSION_OF                = "versionOf";
  public static final String SUMMARY                   = "summary";
  // public static final String IS_FROM_EXTERNAL_SOURCE =
  // "isFromExternalSource";
  // public static final String IS_SKIPPED = "isSkipped";
  public static final String KLASS_INSTANCE_ID         = "klassInstanceId";
  public static final String PARENT_ID                 = "parentId";
  public static final String PATH                      = "path";
  public static final String RELATIONSHIPS             = "relationships";
  public static final String NATURE_RELATIONSHIPS      = "natureRelationships";
  public static final String VARIANTS                  = "variants";
  public static final String ATTRIBUTE_VARIANTS        = "attributeVariants";
  public static final String PARTNER_SOURCES           = "partnerSources";
  public static final String LANGUAGE_CODES            = "languageCodes";
  public static final String CREATION_LANGUAGE         = "creationLanguage";
  public static final String LANGUAGE_INSTANCES        = "languageInstances";
  public static final String IS_EMBEDDED               = "isEmbedded";
  public static final String CLONE_OF                  = "cloneOf";
  public static final String RELATIONSHIP_CONFLICTING_VALUES = "relationshipConflictingValues";
  public static final String TAXONOMY_CONFLICTING_VALUES     = "taxonomyConflictingValues";

  public String getDefaultAssetInstanceId();
  
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId);
  
  public List<IRuleViolation> getRuleViolation();
  
  public void setRuleViolation(List<IRuleViolation> ruleViolation);
  
  public IMessageInformation getMessages();
  
  public void setMessages(IMessageInformation messages);
  
  public String getBranchOf();
  
  public void setBranchOf(String branchOf);
  
  public String getVersionOf();
  
  public void setVersionOf(String versionOf);
  
  public IContextInstance getContext();
  
  public void setContext(IContextInstance context);
  
  public IKlassInstanceVersionSummary getSummary();
  
  public void setSummary(IKlassInstanceVersionSummary summary);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String masterContentId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  /*public Boolean getIsFromExternalSource();
  public void setIsFromExternalSource(Boolean isFromExternalSource);*/
  
  /*public Boolean getIsSkipped();
  public void setIsSkipped(Boolean isSkipped);*/
  
  public List<String> getPath();
  
  public void setPath(List<String> path);
  
  public List<String> getRelationships();
  
  public void setRelationships(List<String> relationships);
  
  public List<String> getNatureRelationships();
  
  public void setNatureRelationships(List<String> natureRelationships);
  
  public List<String> getVariants();
  
  public void setVariants(List<String> variants);
  
  public List<IIdAndVersionId> getAttributeVariants();
  
  public void setAttributeVariants(List<IIdAndVersionId> attributeVariants);
  
  public List<IContentIdentifier> getPartnerSources();
  
  public void setPartnerSources(List<IContentIdentifier> partnerSources);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getCreationLanguage();
  
  public void setCreationLanguage(String creationLanguage);
  
  public List<ILanguageAndVersionId> getLanguageInstances();
  
  public void setLanguageInstances(List<ILanguageAndVersionId> languageInstances);
  
  public Boolean getIsEmbedded();
  
  public void setIsEmbedded(Boolean isEmbedded);
  
  public String getCloneOf();
  
  public void setCloneOf(String cloneOf);
  
  public List<IRelationshipConflict> getRelationshipConflictingValues();
  
  public void setRelationshipConflictingValues(List<IRelationshipConflict> relationshipConflictingValues);
  
  public List<ITaxonomyConflict> getTaxonomyConflictingValues();
  
  public void setTaxonomyConflictingValues(List<ITaxonomyConflict> taxonomyConflictingValues);
  
}
