package com.cs.core.runtime.interactor.usecase.goldenrecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffectType;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesComparisonModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstancesForComparisonModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

public class GoldenRecordUtil {
  
  @SuppressWarnings("unchecked")
  public static void fillAllAttributesAndTags(
      IGoldenRecordRuleKlassInstancesComparisonModel klassInstancesDetail,
      List<IAttributeInstance> attributes, List<ITagInstance> tags)
  {
    Map<String, IKlassInstancesForComparisonModel> klassInstancesDetails = klassInstancesDetail
        .getKlassInstancesDetails();
    List<String> klassInstanceIds = klassInstancesDetail.getKlassInstanceIds();
    klassInstanceIds.forEach(klassInstanceId -> {
      IKlassInstancesForComparisonModel klassInstanceDetail = klassInstancesDetails
          .get(klassInstanceId);
      IKlassInstanceInformationModel klassInstance = klassInstanceDetail.getKlassInstance();
      tags.addAll((Collection<? extends ITagInstance>) klassInstance.getTags());
      attributes.addAll((Collection<? extends IAttributeInstance>) klassInstance.getAttributes());
    });
  }
  
  public static void fillRecommendationForRelationship(
      Map<String, List<String>> supplierContentsMap, Map<String, List<String>> recommendation,
      IMergeEffect mergeEffect)
  {
    List<IMergeEffectType> relationships = mergeEffect.getRelationships();
    relationships.forEach(relationship -> {
      String relationshipId = relationship.getEntityId();
      fillCompratorObjectForSupplierPriority(supplierContentsMap, recommendation, relationship,
          relationshipId);
    });
  }
  
  public static void fillRecommendationForNatureRelationship(
      Map<String, List<String>> supplierContentsMap, Map<String, List<String>> recommendation,
      IMergeEffect mergeEffect)
  {
    List<IMergeEffectType> natureRelationships = mergeEffect.getNatureRelationships();
    natureRelationships.forEach(natureRelationship -> {
      String natureRelationshipId = natureRelationship.getEntityId();
      fillCompratorObjectForSupplierPriority(supplierContentsMap, recommendation,
          natureRelationship, natureRelationshipId);
    });
  }
  
  public static void fillRecommendationForTags(Map<String, List<String>> supplierContentsMap,
      List<ITagInstance> tags, Map<String, List<String>> recommendation, IMergeEffect mergeEffect)
  {
    List<IMergeEffectType> goldenRuleTags = mergeEffect.getTags();
    goldenRuleTags.forEach(goldenRuleTag -> {
      String tagId = goldenRuleTag.getEntityId();
      if (goldenRuleTag.getType()
          .equals(CommonConstants.SUPPLIER_PRIORITY)) {
        fillCompratorObjectForSupplierPriority(supplierContentsMap, recommendation, goldenRuleTag,
            tagId);
      }
      else {
        Optional<ITagInstance> max = tags.stream()
            .filter(tag -> tag.getTagId()
                .equals(tagId))
            .filter(tag -> tag.getLastModified() != null)
            .max(Comparator.comparingLong(ITagInstance::getLastModified));
        
        if (max.isPresent()) {
          Long lastModified = max.get()
              .getLastModified();
          
          List<String> matchedklassInstanceIds = tags.stream()
              .filter(tag -> tag.getTagId()
                  .equals(tagId))
              .filter(tag -> tag.getLastModified() != null)
              .filter(tag -> tag.getLastModified()
                  .equals(lastModified))
              .map(tag -> tag.getKlassInstanceId())
              .collect(Collectors.toList());
          
          recommendation.put(tagId, matchedklassInstanceIds);
        }
      }
    });
  }
  
  public static void fillRecommendationForAttribute(Map<String, List<String>> supplierContentsMap,
      List<IAttributeInstance> attributes, Map<String, List<String>> recommendation,
      IMergeEffect mergeEffect)
  {
    List<IMergeEffectType> goldenRuleAttributes = mergeEffect.getAttributes();
    goldenRuleAttributes.forEach(goldenRuleAttribute -> {
      String attributeId = goldenRuleAttribute.getEntityId();
      if (goldenRuleAttribute.getType()
          .equals(CommonConstants.SUPPLIER_PRIORITY)) {
        fillCompratorObjectForSupplierPriority(supplierContentsMap, recommendation,
            goldenRuleAttribute, attributeId);
      }
      else {
        Optional<IAttributeInstance> max = attributes.stream()
            .filter(attribute -> attribute.getAttributeId()
                .equals(attributeId))
            .filter(attribute -> attribute.getLastModified() != null)
            .max(Comparator.comparingLong(IAttributeInstance::getLastModified));
        
        if (max.isPresent()) {
          Long lastModified = max.get()
              .getLastModified();
          
          List<String> matchedklassInstanceIds = attributes.stream()
              .filter(attribute -> attribute.getAttributeId()
                  .equals(attributeId))
              .filter(attribute -> attribute.getLastModified() != null)
              .filter(attribute -> attribute.getLastModified()
                  .equals(lastModified))
              .map(attribute -> attribute.getKlassInstanceId())
              .collect(Collectors.toList());
          recommendation.put(attributeId, matchedklassInstanceIds);
        }
      }
    });
  }
  
  public static void fillCompratorObjectForSupplierPriority(
      Map<String, List<String>> supplierContentsMap, Map<String, List<String>> recommendation,
      IMergeEffectType goldenRuleEntity, String entityId)
  {
    List<String> supplierIds = goldenRuleEntity.getSupplierIds();
    List<String> klassInstanceIdsBySupplier = new ArrayList<>();
    supplierIds.forEach(supplierId -> {
      if (supplierContentsMap.containsKey(supplierId)) {
        klassInstanceIdsBySupplier.addAll(supplierContentsMap.get(supplierId));
      }
      if (!klassInstanceIdsBySupplier.isEmpty()) {
        recommendation.put(entityId, klassInstanceIdsBySupplier);
      }
    });
  }
  
  public static List<String> getDependentAttributeIds(
      IGetConfigDetailsForCustomTabModel configDetails)
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    
    List<String> dependentAttributeIds = referencedAttributes.values()
        .stream()
        .filter(attribute -> attribute.getIsTranslatable())
        .map(attribute -> attribute.getId())
        .collect(Collectors.toList());
    return dependentAttributeIds;
  }
  
  public static List<String> getTaxonomyIds(Map<String, List<String>> taxonomiesHierarchies)
  {
    Collection<List<String>> values = taxonomiesHierarchies.values();
    List<String> taxonomyIds = values.stream()
        .flatMap(td -> td.stream())
        .distinct()
        .collect(Collectors.toList());
    return taxonomyIds;
  }
}
