package com.cs.core.runtime.interactor.model.instance;

import java.util.List;

import com.cs.core.runtime.interactor.entity.contentidentifier.IContentIdentifier;
import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndVersionId;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.language.ILanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflict;
import com.cs.core.runtime.interactor.entity.summary.IKlassInstanceVersionSummary;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.klassinstance.AbstractKlassInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractContentInstanceModel extends AbstractKlassInstanceModel
    implements IContentInstance {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return ((IContentInstance) this.entity).getDefaultAssetInstanceId();
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    ((IContentInstance) this.entity).setDefaultAssetInstanceId(defaultAssetInstanceId);
  }
  
  @Override
  public List<String> getTypes()
  {
    return ((IContentInstance) this.entity).getTypes();
  }
  
  @Override
  public void setTypes(List<String> multiClassificationTypes)
  {
    ((IContentInstance) this.entity).setTypes(multiClassificationTypes);
  }
  
  @Override
  public List<IRuleViolation> getRuleViolation()
  {
    return ((IContentInstance) this.entity).getRuleViolation();
  }
  
  @Override
  public void setRuleViolation(List<IRuleViolation> ruleViolation)
  {
    ((IContentInstance) this.entity).setRuleViolation(ruleViolation);
  }
  
  @Override
  public IMessageInformation getMessages()
  {
    return ((IContentInstance) this.entity).getMessages();
  }
  
  @Override
  public void setMessages(IMessageInformation messages)
  {
    ((IContentInstance) this.entity).setMessages(messages);
  }
  
  @Override
  public String getBranchOf()
  {
    return ((IContentInstance) this.entity).getBranchOf();
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    ((IContentInstance) this.entity).setBranchOf(branchOf);
  }
  
  @Override
  public String getCloneOf()
  {
    return ((IContentInstance) this.entity).getCloneOf();
  }
  
  @Override
  public void setCloneOf(String cloneOf)
  {
    ((IContentInstance) this.entity).setCloneOf(cloneOf);
  }
  
  @Override
  public String getVersionOf()
  {
    return ((IContentInstance) this.entity).getVersionOf();
  }
  
  @Override
  public void setVersionOf(String versionOf)
  {
    ((IContentInstance) this.entity).setVersionOf(versionOf);
  }
  
  @Override
  public IContextInstance getContext()
  {
    return ((IContentInstance) this.entity).getContext();
  }
  
  @Override
  public void setContext(IContextInstance context)
  {
    ((IContentInstance) this.entity).setContext(context);
  }
  
  @Override
  public List<IContentIdentifier> getPartnerSources()
  {
    return ((IContentInstance) this.entity).getPartnerSources();
  }
  
  @Override
  public void setPartnerSources(List<IContentIdentifier> partnerSources)
  {
    ((IContentInstance) this.entity).setPartnerSources(partnerSources);
  }
  
  @Override
  public List<ILanguageAndVersionId> getLanguageInstances()
  {
    return ((IContentInstance) this.entity).getLanguageInstances();
  }
  
  @Override
  public void setLanguageInstances(List<ILanguageAndVersionId> languageInstances)
  {
    ((IContentInstance) this.entity).setLanguageInstances(languageInstances);
  }
  
  /**
   * ************** variant fields *****************
   */
  @Override
  public String getKlassInstanceId()
  {
    return ((IContentInstance) this.entity).getKlassInstanceId();
  }
  
  @Override
  public void setKlassInstanceId(String masterContentId)
  {
    ((IContentInstance) this.entity).setKlassInstanceId(masterContentId);
  }
  
  @Override
  public String getParentId()
  {
    return ((IContentInstance) this.entity).getParentId();
  }
  
  @Override
  public void setParentId(String parentVariantId)
  {
    ((IContentInstance) this.entity).setParentId(parentVariantId);
  }
  
  @Override
  public void setTaxonomyConflictingValues(List<ITaxonomyConflict> taxonomyConflictingValues)
  {
    ((IContentInstance) this.entity).setTaxonomyConflictingValues(taxonomyConflictingValues);
  }
  
  @Override
  public List<ITaxonomyConflict> getTaxonomyConflictingValues()
  {
    return ((IContentInstance) this.entity).getTaxonomyConflictingValues();
  }
  
  /*@Override
  public Boolean getIsFromExternalSource()
  {
    return ((IContentInstance) this.entity).getIsFromExternalSource();
  }
  
  @Override
  public void setIsFromExternalSource(Boolean isFromExternalSource)
  {
    ((IContentInstance) this.entity).setIsFromExternalSource(isFromExternalSource);
  }*/
  
  /*@Override
    public Boolean getIsSkipped()
    {
      return ((IContentInstance) this.entity).getIsSkipped();
    }
  */
  /*@Override
  public void setIsSkipped(Boolean isSkipped)
  {
    ((IContentInstance) this.entity).setIsSkipped(isSkipped);
  }*/
  
  @Override
  public List<String> getPath()
  {
    return ((IContentInstance) this.entity).getPath();
  }
  
  @Override
  public void setPath(List<String> path)
  {
    ((IContentInstance) this.entity).setPath(path);
  }
  
  @Override
  public List<String> getLanguageCodes()
  {
    return ((IContentInstance) this.entity).getLanguageCodes();
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    ((IContentInstance) this.entity).setLanguageCodes(languageCodes);
  }
  
  @Override
  public String getCreationLanguage()
  {
    return ((IContentInstance) this.entity).getCreationLanguage();
  }
  
  @Override
  public void setCreationLanguage(String languageCodes)
  {
    ((IContentInstance) this.entity).setCreationLanguage(languageCodes);
  }
  
  /**
   * ******** ignored fields *************
   */
  @JsonIgnore
  @Override
  public IKlassInstanceVersionSummary getSummary()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setSummary(IKlassInstanceVersionSummary summary)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getRelationships()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setRelationships(List<String> relationships)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getNatureRelationships()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setNatureRelationships(List<String> natureRelationships)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getVariants()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVariants(List<String> variants)
  {
  }
  
  @JsonIgnore
  @Override
  public List<IIdAndVersionId> getAttributeVariants()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setAttributeVariants(List<IIdAndVersionId> attributeVariants)
  {
  }
  
  @Override
  public Boolean getIsEmbedded()
  {
    return null;
  }
  
  @Override
  public void setIsEmbedded(Boolean isEmbedded)
  {
  }
  
  
  @Override
  public void setRelationshipConflictingValues(List<IRelationshipConflict> relationshipConflictingValues)
  {
    ((IContentInstance) this.entity).setRelationshipConflictingValues(relationshipConflictingValues);
  }
  
  @Override
  public List<IRelationshipConflict> getRelationshipConflictingValues()
  {
    return ((IContentInstance) this.entity).getRelationshipConflictingValues();
  }
}
