package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.config.interactor.model.language.GetLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.relationship.ReferencedRelationshipModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.runtime.interactor.model.relationship.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigDetailsForGetKlassInstancesToMergeModel
    implements IConfigDetailsForGetKlassInstancesToMergeModel {
  
  private static final long                                     serialVersionUID = 1L;
  
  protected Map<String, IReferencedKlassDetailStrategyModel>    referencedKlasses;
  protected Map<String, IReferencedSectionElementModel>         referencedElements;
  protected Map<String, IAttribute>                             referencedAttributes;
  protected Map<String, ITag>                                   referencedTags;
  protected Map<String, IReferencedArticleTaxonomyModel>        referencedTaxonomies;
  protected Map<String, IGetReferencedNatureRelationshipModel>  referencedNatureRelationships;
  protected Map<String, IReferencedRelationshipModel>           referencedRelationships;
  protected Map<String, IReferencedPropertyCollectionModel>     referencedPropertyCollections;
  protected IReferencedContextModel                             referencedVariantContexts;
  protected IGoldenRecordRuleModel                              goldenRecordRule;
  protected Map<String, List<String>>                           referencedRelationshipSides;
  protected List<String>                                        dependendAttributeIds;
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  protected Map<String, IGetLanguagesInfoModel>                 referencedLanguages;
  
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
  public IGoldenRecordRuleModel getGoldenRecordRule()
  {
    return goldenRecordRule;
  }
  
  @JsonDeserialize(as = GoldenRecordRuleModel.class)
  @Override
  public void setGoldenRecordRule(IGoldenRecordRuleModel goldenRecordRule)
  {
    this.goldenRecordRule = goldenRecordRule;
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
  public Map<String, List<String>> getReferencedRelationshipSides()
  {
    return referencedRelationshipSides;
  }
  
  @Override
  public void setReferencedRelationshipSides(Map<String, List<String>> referencedRelationshipSides)
  {
    this.referencedRelationshipSides = referencedRelationshipSides;
  }
  
  @Override
  public List<String> getDependendAttributeIds()
  {
    if (dependendAttributeIds == null) {
      dependendAttributeIds = new ArrayList<>();
    }
    return dependendAttributeIds;
  }
  
  @Override
  public void setDependendAttributeIds(List<String> dependendAttributeIds)
  {
    this.dependendAttributeIds = dependendAttributeIds;
  }
  
  @Override
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections()
  {
    return referencedPropertyCollections;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedPropertyCollectionModel.class)
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollections)
  {
    this.referencedPropertyCollections = referencedPropertyCollections;
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
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages()
  {
    return referencedLanguages;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetLanguagesInfoModel.class)
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages)
  {
    this.referencedLanguages = referencedLanguages;
  }
}
