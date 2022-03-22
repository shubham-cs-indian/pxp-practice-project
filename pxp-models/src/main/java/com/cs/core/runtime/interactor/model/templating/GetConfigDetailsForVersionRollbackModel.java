package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.language.GetLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.relationship.ReferencedRelationshipModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.versionrollback.IPropertyCouplingInformationModel;
import com.cs.core.runtime.interactor.model.customdeserializer.CustomDeserializerForVersionRollback;
import com.cs.core.runtime.interactor.model.relationship.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForVersionRollbackModel
    implements IGetConfigDetailsForVersionRollbackModel {
  
  private static final long                                             serialVersionUID           = 1L;
  
  protected Map<String, IReferencedKlassDetailStrategyModel>            referencedKlasses;
  protected Map<String, IReferencedSectionElementModel>                 referencedElements;
  protected Map<String, IAttribute>                                     referencedAttributes;
  protected Map<String, ITag>                                           referencedTags;
  protected Integer                                                     numberOfVersionsToMaintain;
  protected Map<String, IReferencedArticleTaxonomyModel>                referencedTaxonomies;
  protected List<String>                                                referencedLifeCycleStatusTags;
  protected List<IDataRuleModel>                                        referencedDataRules;
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel>       technicalImageVariantWithIsAutoCreate;
  protected IReferencedContextModel                                     referencedVariantContexts;
  protected Map<String, List<String>>                                   typeIdIdentifierAttributeIds;
  protected Map<String, IGetReferencedNatureRelationshipModel>          referencedNatureRelationships;
  protected Map<String, IReferencedRelationshipPropertiesModel>         referencedRelationshipProperties;
  protected Map<String, IReferencedRelationshipModel>                   referencedRelationships;
  protected Map<String, Map<String, IPropertyCouplingInformationModel>> referencedPropertyCouplingInformation;
  protected Map<String, IGetLanguagesInfoModel>                         referencedLanguages;
  protected List<String>                                                versionableAttributes;
  protected List<String>                                                versionableTags;
  protected List<String>                                                mandatoryAttributeIds;
  protected List<String>                                                shouldAttributeIds;
  protected List<String>                                                mandatoryTagIds;
  protected List<String>                                                shouldTagIds;
  protected Boolean                                                     isLanguageHierarchyPresent = false;
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    this.referencedKlasses = klasses;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Integer getNumberOfVersionsToMaintain()
  {
    return numberOfVersionsToMaintain;
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain)
  {
    this.numberOfVersionsToMaintain = numberOfVersionsToMaintain;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public List<String> getReferencedLifeCycleStatusTags()
  {
    if (referencedLifeCycleStatusTags == null) {
      referencedLifeCycleStatusTags = new ArrayList<>();
    }
    return referencedLifeCycleStatusTags;
  }
  
  @Override
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags)
  {
    this.referencedLifeCycleStatusTags = referencedLifeCycleStatusTags;
  }
  
  @Override
  public List<IDataRuleModel> getReferencedDataRules()
  {
    if (referencedDataRules == null) {
      referencedDataRules = new ArrayList<>();
    }
    return referencedDataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    return technicalImageVariantWithIsAutoCreate;
  }
  
  @Override
  @JsonDeserialize(contentAs = TechnicalImageVariantWithAutoCreateEnableModel.class)
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantWithIsAutoCreate)
  {
    this.technicalImageVariantWithIsAutoCreate = technicalImageVariantWithIsAutoCreate;
  }
  
  @Override
  public IReferencedContextModel getReferencedVariantContexts()
  {
    return referencedVariantContexts;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedContextModel.class)
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts)
  {
    this.referencedVariantContexts = referencedVariantContexts;
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
  
  @Override
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships()
  {
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetReferencedNatureRelationshipModel.class)
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipPropertiesModel.class)
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
  }
  
  @Override
  public Map<String, IReferencedRelationshipModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipModel.class)
  public void setReferencedRelationships(
      Map<String, IReferencedRelationshipModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantWithIsAutoCreate()
  {
    return technicalImageVariantWithIsAutoCreate;
  }
  
  @Override
  @JsonDeserialize(contentAs = TechnicalImageVariantWithAutoCreateEnableModel.class)
  public void setTechnicalImageVariantWithIsAutoCreate(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantWithIsAutoCreate)
  {
    this.technicalImageVariantWithIsAutoCreate = technicalImageVariantWithIsAutoCreate;
  }
  
  @Override
  public Map<String, Map<String, IPropertyCouplingInformationModel>> getReferencedPropertyCouplingInformation()
  {
    return referencedPropertyCouplingInformation;
  }
  
  @Override
  @JsonDeserialize(contentUsing = CustomDeserializerForVersionRollback.class)
  public void setReferencedPropertyCouplingInformation(
      Map<String, Map<String, IPropertyCouplingInformationModel>> referencedPropertyCouplingInformation)
  {
    this.referencedPropertyCouplingInformation = referencedPropertyCouplingInformation;
  }
  
  @Override
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages()
  {
    if (referencedLanguages == null) {
      referencedLanguages = new HashMap<String, IGetLanguagesInfoModel>();
    }
    return referencedLanguages;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetLanguagesInfoModel.class)
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages)
  {
    this.referencedLanguages = referencedLanguages;
  }
  
  @Override
  public List<String> getVersionableAttributes()
  {
    if (versionableAttributes == null) {
      versionableAttributes = new ArrayList<>();
    }
    return versionableAttributes;
  }
  
  @Override
  public void setVersionableAttributes(List<String> versionableAttributes)
  {
    this.versionableAttributes = versionableAttributes;
  }
  
  @Override
  public List<String> getVersionableTags()
  {
    if (versionableTags == null) {
      versionableTags = new ArrayList<>();
    }
    return versionableTags;
  }
  
  @Override
  public void setVersionableTags(List<String> versionableTags)
  {
    this.versionableTags = versionableTags;
  }
  
  @Override
  public List<String> getMandatoryAttributeIds()
  {
    if (mandatoryAttributeIds == null) {
      mandatoryAttributeIds = new ArrayList<>();
    }
    return mandatoryAttributeIds;
  }
  
  @Override
  public void setMandatoryAttributeIds(List<String> mandatoryAttributeIds)
  {
    this.mandatoryAttributeIds = mandatoryAttributeIds;
  }
  
  @Override
  public List<String> getMandatoryTagIds()
  {
    if (mandatoryTagIds == null) {
      mandatoryTagIds = new ArrayList<>();
    }
    return mandatoryTagIds;
  }
  
  @Override
  public void setMandatoryTagIds(List<String> mandatoryTagIds)
  {
    this.mandatoryTagIds = mandatoryTagIds;
  }
  
  @Override
  public List<String> getShouldAttributeIds()
  {
    if (shouldAttributeIds == null) {
      shouldAttributeIds = new ArrayList<>();
    }
    return shouldAttributeIds;
  }
  
  @Override
  public void setShouldAttributeIds(List<String> shouldAttributeIds)
  {
    this.shouldAttributeIds = shouldAttributeIds;
  }
  
  @Override
  public List<String> getShouldTagIds()
  {
    if (shouldTagIds == null) {
      shouldTagIds = new ArrayList<>();
    }
    return shouldTagIds;
  }
  
  @Override
  public void setShouldTagIds(List<String> shouldTagIds)
  {
    this.shouldTagIds = shouldTagIds;
  }
  
  @Override
  public Boolean getIsLanguageHierarchyPresent()
  {
    return isLanguageHierarchyPresent;
  }
  
  @Override
  public void setIsLanguageHierarchyPresent(Boolean isLanguageHierarchyPresent)
  {
    this.isLanguageHierarchyPresent = isLanguageHierarchyPresent;
  }
}
