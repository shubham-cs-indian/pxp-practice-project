package com.cs.pim.runtime.strategy.usecase.articleinstance.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipInheritanceInfoModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesBaseTypeModel;
import com.cs.core.runtime.interactor.model.relationship.IEntityRelationshipInfoModel;

public class RelationshipInheritanceUtil {
  
  public static void fillBaseTypeVsContentIdsMap(IRelationshipInheritanceInfoModel dataModel,
      Map<String, List<String>> baseTypeVsContentIds)
  {
    String sourceContentId = dataModel.getSourceContentId();
    String sourceContentBaeType = dataModel.getSourceContentBaseType();
    
    mangeContentListWithRespectToBaseType(baseTypeVsContentIds, sourceContentId,
        sourceContentBaeType);
    for (IEntityRelationshipInfoModel relationshipInfo : dataModel.getRelationshipInfo()) {
      fillBaseTypeVsContentIdsMapWithEntities(baseTypeVsContentIds,
          relationshipInfo.getAddedEntities());
      fillBaseTypeVsContentIdsMapWithEntities(baseTypeVsContentIds,
          relationshipInfo.getRemovedEntities());
    }
  }
  
  public static void mangeContentListWithRespectToBaseType(
      Map<String, List<String>> baseTypeVsContentIds, String contentId, String baseType)
  {
    List<String> contentIds = baseTypeVsContentIds.get(baseType);
    if (contentIds == null) {
      contentIds = new ArrayList<>();
      baseTypeVsContentIds.put(baseType, contentIds);
    }
    contentIds.add(contentId);
  }
  
  public static void fillBaseTypeVsContentIdsMapWithEntities(
      Map<String, List<String>> baseTypeVsContentIds, List<IIdAndBaseType> entities)
  {
    entities.forEach(entity -> {
      String contentId = entity.getId();
      String baseType = entity.getBaseType();
      mangeContentListWithRespectToBaseType(baseTypeVsContentIds, contentId, baseType);
    });
  }
  
  public static Boolean isSideAplicable(Collection<String> sourceTypes, List<String> klassIds,
      List<String> sourceTaxonomyIds, List<String> taxonomyIds)
  {
    Boolean present = isTypeMatch(sourceTypes, klassIds);
    if (!present) {
      return isTypeMatch(sourceTaxonomyIds, taxonomyIds);
    }
    return present;
  }
  
  public static Boolean isTypeMatch(Collection<String> sourceTypes, List<String> klassIds)
  {
    Boolean present = sourceTypes.stream()
        .filter(sourceTypeId -> (klassIds.contains(sourceTypeId)))
        .findFirst()
        .isPresent();
    return present;
  }
  
  public static Boolean isTypeMatch(ITypesTaxonomiesBaseTypeModel typesTaxonomiesModel,
      Set<String> typeAndTaxonomyIds)
  {
    Boolean present = typesTaxonomiesModel.getTypes()
        .stream()
        .filter(targetklassId -> (typeAndTaxonomyIds.contains(targetklassId)))
        .findFirst()
        .isPresent();
    if (!present) {
      return typesTaxonomiesModel.getTaxonomyIds()
          .stream()
          .filter(targetklassId -> (typeAndTaxonomyIds.contains(targetklassId)))
          .findFirst()
          .isPresent();
    }
    return present;
  }
}
