package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

import java.util.List;
import java.util.Map;

public interface IConfigDetailsForGetKlassInstancesToMergeModel extends IModel {
  
  public static final String REFRENCED_KLASSES                   = "referencedKlasses";
  public static final String REFERENCED_ELEMENTS                 = "referencedElements";
  public static final String REFERENCED_ATTRIBUTES               = "referencedAttributes";
  public static final String REFERENCED_TAGS                     = "referencedTags";
  public static final String REFERENCED_TAXONOMIES               = "referencedTaxonomies";
  public static final String REFERENCED_NATURE_RELATIONSHIPS     = "referencedNatureRelationships";
  public static final String REFERENCED_RELATIONSHIPS            = "referencedRelationships";
  public static final String REFERENCED_PROPERTY_COLLECTIONS     = "referencedPropertyCollections";
  public static final String REFERENCED_VARIANT_CONTEXTS         = "referencedVariantContexts";
  public static final String GOLDEN_RECORD_RULE                  = "goldenRecordRule";
  public static final String REFERENCED_RELATIONSHIP_SIDES       = "referencedRelationshipSides";
  public static final String DEPENDEND_ATTRIBUTE_IDS             = "dependendAttributeIds";
  public static final String REFERENCED_RELATIONSHIPS_PROPERTIES = "referencedRelationshipProperties";
  public static final String REFERENCED_LANGUAGES                = "referencedLanguages";
  
  // key:klassId
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  
  // key:propertyId[attributeId, tagId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  // key:attributeId
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public Map<String, IReferencedRelationshipModel> getReferencedRelationships();
  
  public void setReferencedRelationships(
      Map<String, IReferencedRelationshipModel> referencedRelationships);
  
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections);
  
  public IGoldenRecordRuleModel getGoldenRecordRule();
  
  public void setGoldenRecordRule(IGoldenRecordRuleModel goldenRecordRule);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  public Map<String, List<String>> getReferencedRelationshipSides();
  
  public void setReferencedRelationshipSides(Map<String, List<String>> referencedRelationshipSides);
  
  public List<String> getDependendAttributeIds();
  
  public void setDependendAttributeIds(List<String> dependendAttributeIds);
  
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages);
}
