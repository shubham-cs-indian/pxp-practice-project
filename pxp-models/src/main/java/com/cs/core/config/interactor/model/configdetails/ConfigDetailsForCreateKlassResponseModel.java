package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.ReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.datarule.ElementConflictingValuesCustomDeserializer;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.ReferencedRelationshipPropertiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForCreateKlassResponseModel
    implements IConfigDetailsForCreateKlassResponseModel {
  
  private static final long                                       serialVersionUID                                 = 1L;
  
  protected Map<String, IReferencedRelationshipPropertiesModel>   referencedRelationshipProperties;
  protected List<IDataRuleModel>                                  referencedDataRules                              = new ArrayList<>();
  protected Map<String, IAttribute>                               referencedAttributes;
  protected Map<String, ITag>                                     referencedTags;
  protected Map<String, IReferencedSectionElementModel>           referencedElements;
  protected Map<String, List<IElementConflictingValuesModel>>     elementsConflictingValues;
  protected List<String>                                          eventIds                                         = new ArrayList<>();
  protected Map<String, List<String>>                             typeIdIdentifierAttributeIds;
  protected Map<String, IReferencedKlassDetailStrategyModel>      referencedKlasses;
  protected Map<String, IReferencedNatureRelationshipModel>       referencedNatureRelationship;
  protected String                                                variantContextId;
  protected IReferencedContextModel                               referencedVariantContexts;
  protected Map<String, IReferencedArticleTaxonomyModel>          referencedTaxonomies;
  protected Map<String, List<String>>                             taxonomiesHierarchies;
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable = new ArrayList<>();
  protected List<String>                                          taxonomyIdsToTransfer;
  protected Integer                                               numberOfVersionToMaintain;
  
  @Override
  public List<IDataRuleModel> getReferencedDataRules()
  {
    if (referencedDataRules == null) {
      referencedDataRules = new ArrayList<>();
    }
    return referencedDataRules;
  }
  
  @JsonDeserialize(contentAs = DataRuleModel.class)
  @Override
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
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
  public Map<String, List<IElementConflictingValuesModel>> getElementsConflictingValues()
  {
    return elementsConflictingValues;
  }
  
  @Override
  @JsonDeserialize(contentUsing = ElementConflictingValuesCustomDeserializer.class)
  public void setElementsConflictingValues(
      Map<String, List<IElementConflictingValuesModel>> elementsConflictingValues)
  {
    this.elementsConflictingValues = elementsConflictingValues;
  }
  
  @Override
  public List<String> getEventIds()
  {
    return eventIds;
  }
  
  @Override
  public void setEventIds(List<String> eventIds)
  {
    this.eventIds = eventIds;
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
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipPropertiesModel.class)
  @Override
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
  }
  
  @Override
  public Map<String, IReferencedNatureRelationshipModel> getReferencedNatureRelationship()
  {
    return referencedNatureRelationship;
  }
  
  @JsonDeserialize(contentAs = ReferencedNatureRelationshipModel.class)
  @Override
  public void setReferencedNatureRelationship(
      Map<String, IReferencedNatureRelationshipModel> referencedNatureRelationship)
  {
    this.referencedNatureRelationship = referencedNatureRelationship;
  }
  
  @Override
  public String getVariantContextId()
  {
    return variantContextId;
  }
  
  @Override
  public void setVariantContextId(String variantContextId)
  {
    this.variantContextId = variantContextId;
  }
  
  @Override
  public IReferencedContextModel getReferencedVariantContexts()
  {
    return referencedVariantContexts;
  }
  
  @JsonDeserialize(as = ReferencedContextModel.class)
  @Override
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts)
  {
    this.referencedVariantContexts = referencedVariantContexts;
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
  public Map<String, List<String>> getTaxonomiesHierarchies()
  {
    return taxonomiesHierarchies;
  }
  
  @Override
  public void setTaxonomiesHierarchies(Map<String, List<String>> taxonomiesHierarchies)
  {
    this.taxonomiesHierarchies = taxonomiesHierarchies;
  }
  
  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    return technicalImageVariantContextWithAutoCreateEnable;
  }
  
  @JsonDeserialize(contentAs = TechnicalImageVariantWithAutoCreateEnableModel.class)
  @Override
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable)
  {
    this.technicalImageVariantContextWithAutoCreateEnable = technicalImageVariantContextWithAutoCreateEnable;
  }
  
  @Override
  public List<String> getTaxonomyIdsToTransfer()
  {
    return taxonomyIdsToTransfer;
  }
  
  @Override
  public void setTaxonomyIdsToTransfer(List<String> taxonomyIdsToTransfer)
  {
    this.taxonomyIdsToTransfer = taxonomyIdsToTransfer;
  }
  
  @Override
  public Integer getNumberOfVersionToMaintain()
  {
    return numberOfVersionToMaintain;
  }
  
  @Override
  public void setNumberOfVersionToMaintain(Integer numberOfVersionToMaintain)
  {
    this.numberOfVersionToMaintain = numberOfVersionToMaintain;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  @Override
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
}
