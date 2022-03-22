package com.cs.core.runtime.interactor.entity.klassinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.entity.contentidentifier.ContentIdentifier;
import com.cs.core.runtime.interactor.entity.contentidentifier.IContentIdentifier;
import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict;
import com.cs.core.runtime.interactor.entity.datarule.RuleViolation;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflict;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndVersionId;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndVersionId;
import com.cs.core.runtime.interactor.entity.language.ILanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.language.LanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflict;
import com.cs.core.runtime.interactor.entity.relationshipinstance.RelationshipConflict;
import com.cs.core.runtime.interactor.entity.summary.IKlassInstanceVersionSummary;
import com.cs.core.runtime.interactor.entity.summary.KlassInstanceVersionSummary;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractContentInstance extends AbstractKlassInstance
    implements IContentInstance {
  
  private static final long              serialVersionUID = 1L;
  
  protected String                       defaultAssetInstanceId;
  protected String                       branchOf         = "-1";
  protected String                       versionOf;
  protected List<IRuleViolation>         ruleViolation;
  protected IMessageInformation          messages;
  protected IContextInstance             context;
  protected IKlassInstanceVersionSummary summary;
  protected List<IContentIdentifier>     partnerSources;
  
  /* variant fields */
  protected String                       klassInstanceId;
  protected String                       parentId;
  protected Boolean                      isSkipped;
  protected Boolean                      isFromExternalSource;
  protected List<String>                 path;
  protected List<String>                 relationships;
  protected List<String>                 natureRelationships;
  protected List<String>                 variants;
  protected List<IIdAndVersionId>        attributeVariants;
  protected List<ILanguageAndVersionId>  languageInstances;
  protected List<String>                 languageCodes;
  protected String                       creationLanguage;
  protected Boolean                      isEmbedded       = false;
  protected String                       cloneOf;
  protected List<IRelationshipConflict>  relationshipConflictingValues;
  protected List<ITaxonomyConflict>             taxonomyConflictingValues;

  
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
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
  public String getBranchOf()
  {
    return this.branchOf;
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    this.branchOf = branchOf;
  }
  
  @Override
  public String getVersionOf()
  {
    return versionOf;
  }
  
  @Override
  public void setVersionOf(String versionOf)
  {
    this.versionOf = versionOf;
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
  public IContextInstance getContext()
  {
    return context;
  }
  
  @JsonDeserialize(as = ContextInstance.class)
  @Override
  public void setContext(IContextInstance context)
  {
    this.context = context;
  }
  
  @Override
  public IKlassInstanceVersionSummary getSummary()
  {
    return summary;
  }
  
  @JsonDeserialize(as = KlassInstanceVersionSummary.class)
  @Override
  public void setSummary(IKlassInstanceVersionSummary summary)
  {
    this.summary = summary;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String masterContentId)
  {
    this.klassInstanceId = masterContentId;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  /*@Override
  public Boolean getIsSkipped()
  {
    return isSkipped;
  }*/
  
  /*@Override
    public void setIsSkipped(Boolean isSkipped)
    {
      this.isSkipped = isSkipped;
    }
  */
  /*@Override
  public Boolean getIsFromExternalSource()
  {
    return isFromExternalSource;
  }*/
  
  /*@Override
  public void setIsFromExternalSource(Boolean isFromExternalSource)
  {
    this.isFromExternalSource = isFromExternalSource;
  }*/
  
  @Override
  public List<String> getPath()
  {
    if (path == null) {
      path = new ArrayList<>();
    }
    return path;
  }
  
  @Override
  public void setPath(List<String> path)
  {
    this.path = path;
  }
  
  @Override
  public List<String> getRelationships()
  {
    if (relationships == null) {
      relationships = new ArrayList<>();
    }
    return this.relationships;
  }
  
  @Override
  public void setRelationships(List<String> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public List<String> getNatureRelationships()
  {
    if (natureRelationships == null) {
      natureRelationships = new ArrayList<>();
    }
    return this.natureRelationships;
  }
  
  @Override
  public void setNatureRelationships(List<String> natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
  @Override
  public List<String> getVariants()
  {
    if (variants == null) {
      variants = new ArrayList<>();
    }
    return this.variants;
  }
  
  @Override
  public void setVariants(List<String> variants)
  {
    this.variants = variants;
  }
  
  @Override
  public List<IIdAndVersionId> getAttributeVariants()
  {
    if (attributeVariants == null) {
      attributeVariants = new ArrayList<>();
    }
    return this.attributeVariants;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndVersionId.class)
  public void setAttributeVariants(List<IIdAndVersionId> attributeVariants)
  {
    this.attributeVariants = attributeVariants;
  }
  
  @Override
  public List<IContentIdentifier> getPartnerSources()
  {
    if (partnerSources == null) {
      partnerSources = new ArrayList<>();
    }
    return partnerSources;
  }
  
  @JsonDeserialize(contentAs = ContentIdentifier.class)
  @Override
  public void setPartnerSources(List<IContentIdentifier> partnerSources)
  {
    this.partnerSources = partnerSources;
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
  public List<ILanguageAndVersionId> getLanguageInstances()
  {
    if (languageInstances == null) {
      languageInstances = new ArrayList<>();
    }
    return languageInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = LanguageAndVersionId.class)
  public void setLanguageInstances(List<ILanguageAndVersionId> languageInstances)
  {
    this.languageInstances = languageInstances;
  }
  
  @Override
  public Boolean getIsEmbedded()
  {
    return isEmbedded;
  }
  
  @Override
  public void setIsEmbedded(Boolean isEmbedded)
  {
    this.isEmbedded = isEmbedded;
  }
  
  @Override
  public String getCloneOf()
  {
    return this.cloneOf;
  }
  
  @Override
  public void setCloneOf(String cloneOf)
  {
    this.cloneOf = cloneOf;
  }
  
  @Override
  public List<IRelationshipConflict> getRelationshipConflictingValues()
  {
    if(relationshipConflictingValues == null) {
      relationshipConflictingValues = new ArrayList<>();
    }
    return relationshipConflictingValues;
  }

  @Override
  @JsonDeserialize(contentAs = RelationshipConflict.class)
  public void setRelationshipConflictingValues(List<IRelationshipConflict> relationshipConflictingValues)
  {
    this.relationshipConflictingValues = relationshipConflictingValues;
  }

  public List<ITaxonomyConflict> getTaxonomyConflictingValues()
  {
    if(taxonomyConflictingValues == null) {
      taxonomyConflictingValues = new ArrayList<>();
    }
    return taxonomyConflictingValues;
  }

  @Override
  @JsonDeserialize(contentAs = TaxonomyConflict.class)
  public void setTaxonomyConflictingValues(List<ITaxonomyConflict> taxonomyConflictingValues)
  {
    this.taxonomyConflictingValues = taxonomyConflictingValues;
  }
}
