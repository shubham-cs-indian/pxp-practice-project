package com.cs.core.runtime.interactor.usecase.taskexecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.relationship.GetRelationshipDataTransferModel;
import com.cs.core.runtime.interactor.model.relationship.IEntityRelationshipInfoModel;
import com.cs.core.runtime.interactor.model.relationship.IGetRelationshipDataTransferModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferPropertyModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipSidePropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferPropertyModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IPrepareDataForRelationshipDataTransferTask;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForPrepareDataForRelationshipDataTransferStrategy;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;

@Component
public class PrepareDataForRelationshipDataTransferTask
    extends AbstractRuntimeInteractor<IRelationshipDataTransferInputModel, IModel>
    implements IPrepareDataForRelationshipDataTransferTask {
  
  @Autowired
  protected IGetConfigDetailsForPrepareDataForRelationshipDataTransferStrategy getConfigDetailsForPrepareDataForRelationshipDataTransferStrategy;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy                                      getInstanceTypeStrategy;
    
  /*@Autowired
  protected IPrepareDataForRelationshipDataTransferStrategy                    prepareDataForRelationshipDataTransferStrategy;*/
  
  @Override
  protected IModel executeInternal(IRelationshipDataTransferInputModel inputModel) throws Exception
  {
    IKlassInstanceTypeModel klassInstanceTypeModel = getKlassInstanceType(
        inputModel.getDataTransfer()
            .getContentId());
    IMulticlassificationRequestModel configRequestModel = new MulticlassificationRequestModel();
    
    configRequestModel.setKlassIds((List<String>) klassInstanceTypeModel.getTypes());
    configRequestModel.setSelectedTaxonomyIds(klassInstanceTypeModel.getSelectedTaxonomyIds());
    configRequestModel.setUserId(context.getUserId());
    
    IGetConfigDetailsForCustomTabModel configModel = getConfigDetailsForPrepareDataForRelationshipDataTransferStrategy
        .execute(configRequestModel);
    
    IGetRelationshipDataTransferModel requestModel = prepareRequestModel(inputModel);
    
    // key: relationship
    Map<String, IEntityRelationshipInfoModel> modifiedRelationships = inputModel
        .getModifiedRelationships();
    
    Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipPropertMap = configModel
        .getReferencedRelationshipProperties();
    
    if (referencedRelationshipPropertMap.isEmpty()) {
      return null;
    }
    
    for (String relationshipId : referencedRelationshipPropertMap.keySet()) {
      IReferencedRelationshipPropertiesModel referencedRelationshipProperties = referencedRelationshipPropertMap
          .get(relationshipId);
      IEntityRelationshipInfoModel entityRelationshipInfo = modifiedRelationships
          .get(relationshipId);
      
      String sideId = null;
      // entityRelationshipInfo can be null, when attributes and tags are
      // modified but not
      // relationship
      if (entityRelationshipInfo != null) {
        sideId = entityRelationshipInfo.getSideId();
      }
      IRelationshipSidePropertiesModel side1 = referencedRelationshipProperties.getSide1();
      IRelationshipSidePropertiesModel side2 = referencedRelationshipProperties.getSide2();
      
      IRelationshipSidePropertiesModel transferSide = null;
      IRelationshipSidePropertiesModel inheritanceSide = null;
      
      if (sideId != null) {
        if (side1.getSideId()
            .equals(sideId)) {
          transferSide = side1;
          inheritanceSide = side2;
        }
        else {
          transferSide = side2;
          inheritanceSide = side1;
        }
      }
      
      fillDataForRelationshipDataTransfer(inputModel, entityRelationshipInfo, requestModel,
          transferSide, side1, side2, relationshipId);
      fillDataForRelationshipDataInheritance(inputModel, entityRelationshipInfo, requestModel,
          inheritanceSide, side1, side2, relationshipId);
    }
    
    /*IListModel<IContentsPropertyDiffModel> contentDiffListModel = prepareDataForRelationshipDataTransferStrategy.execute(requestModel);
    Collection<? extends IContentsPropertyDiffModel> contentDiffList = contentDiffListModel.getList();
    
    kafkaUtils.prepareMessageData((List<? extends IModel>) contentDiffList, PropagateValuesHandlerTask.class.getName(), IContentsPropertyDiffModel.CONTENT_ID);
    */
    return null;
  }
  
  private IKlassInstanceTypeModel getKlassInstanceType(String contentId) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(contentId);
    return getInstanceTypeStrategy.execute(idParameterModel);
  }
  
  private IGetRelationshipDataTransferModel prepareRequestModel(
      IRelationshipDataTransferInputModel inputModel)
  {
    IGetRelationshipDataTransferModel requestModel = new GetRelationshipDataTransferModel();
    requestModel.setContentId(inputModel.getDataTransfer()
        .getContentId());
    requestModel.setBaseType(inputModel.getDataTransfer()
        .getBaseType());
    requestModel.setModifiedLanguageCodes(inputModel.getDataTransfer()
        .getModifiedLanguageCodes());
    
    return requestModel;
  }
  
  private void fillDataForRelationshipDataInheritance(
      IRelationshipDataTransferInputModel inputModel,
      IEntityRelationshipInfoModel entityRelationshipInfo,
      IGetRelationshipDataTransferModel requestModel,
      IRelationshipSidePropertiesModel sideForAddedEntities, IRelationshipSidePropertiesModel side1,
      IRelationshipSidePropertiesModel side2, String relationshipId)
  {
    fillDataForRelationshipDataInheritanceOnAdditionOfProperties(inputModel, requestModel, side1,
        side2, relationshipId);
    fillDataForRelationshipDataInheritanceOnAdditionOfProperties(inputModel, requestModel, side2,
        side1, relationshipId);
    
    if (entityRelationshipInfo != null && sideForAddedEntities != null) {
      fillDataForRelationshipDataInheritanceOnAdditionOrRemovalOfEntity(inputModel,
          entityRelationshipInfo, requestModel, sideForAddedEntities, relationshipId);
    }
  }
  
  private void fillDataForRelationshipDataInheritanceOnAdditionOfProperties(
      IRelationshipDataTransferInputModel inputModel,
      IGetRelationshipDataTransferModel requestModel,
      IRelationshipSidePropertiesModel sideForTransfer,
      IRelationshipSidePropertiesModel sideForInheritance, String relationshipId)
  {
    List<IReferencedRelationshipProperty> tags = sideForTransfer.getTags();
    List<IReferencedRelationshipProperty> attributes = sideForTransfer.getAttributes();
    List<IReferencedRelationshipProperty> dependentAttributes = sideForTransfer
        .getDependentAttributes();
    
    if (tags.isEmpty() && attributes.isEmpty() && dependentAttributes.isEmpty()) {
      return;
    }
    
    IRelationshipDataTransferPropertyModel relationshipDataInheritanceDueToModificationOfProperties = getRelationshipDataInheritanceDueToAdditionOfProperties(
        inputModel, sideForTransfer);
    if (relationshipDataInheritanceDueToModificationOfProperties != null) {
      relationshipDataInheritanceDueToModificationOfProperties
          .setContentId(inputModel.getDataTransfer()
              .getContentId());
      relationshipDataInheritanceDueToModificationOfProperties.setRelationshipId(relationshipId);
      relationshipDataInheritanceDueToModificationOfProperties
          .setSideId(sideForInheritance.getSideId());
      String relationshipSideKey = relationshipId + Seperators.RELATIONSHIP_SIDE_SPERATOR
          + sideForInheritance.getSideId();
      requestModel.getRelationshipDataInheritanceDueToAddedProperty()
          .put(relationshipSideKey, relationshipDataInheritanceDueToModificationOfProperties);
      fillPropertyIds(relationshipDataInheritanceDueToModificationOfProperties, requestModel);
    }
  }
  
  private void fillDataForRelationshipDataInheritanceOnAdditionOrRemovalOfEntity(
      IRelationshipDataTransferInputModel inputModel,
      IEntityRelationshipInfoModel entityRelationshipInfo,
      IGetRelationshipDataTransferModel requestModel,
      IRelationshipSidePropertiesModel sideForAddedEntities, String relationshipId)
  {
    if (sideForAddedEntities == null) {
      return;
    }
    
    List<IReferencedRelationshipProperty> tags = sideForAddedEntities.getTags();
    List<IReferencedRelationshipProperty> attributes = sideForAddedEntities.getAttributes();
    List<IReferencedRelationshipProperty> dependentAttributes = sideForAddedEntities
        .getDependentAttributes();
    
    if (tags.isEmpty() && attributes.isEmpty() && dependentAttributes.isEmpty()) {
      return;
    }
    
    IRelationshipDataTransferPropertyModel relationshipDataInheritanceDueToAddedOfEntities = getRelationshipDataInheritanceDueToAddedOrRemovalOfEntities(
        inputModel, sideForAddedEntities, entityRelationshipInfo);
    
    if (relationshipDataInheritanceDueToAddedOfEntities != null) {
      relationshipDataInheritanceDueToAddedOfEntities.setContentId(inputModel.getDataTransfer()
          .getContentId());
      relationshipDataInheritanceDueToAddedOfEntities.setRelationshipId(relationshipId);
      relationshipDataInheritanceDueToAddedOfEntities.setSideId(sideForAddedEntities.getSideId());
      String relationshipSideKey = relationshipId + Seperators.RELATIONSHIP_SIDE_SPERATOR
          + sideForAddedEntities.getSideId();
      requestModel.getRelationshipDataInheritanceDueToAddedEntity()
          .put(relationshipSideKey, relationshipDataInheritanceDueToAddedOfEntities);
      fillPropertyIds(relationshipDataInheritanceDueToAddedOfEntities, requestModel);
    }
  }
  
  private IRelationshipDataTransferPropertyModel getRelationshipDataInheritanceDueToAddedOrRemovalOfEntities(
      IRelationshipDataTransferInputModel inputModel, IRelationshipSidePropertiesModel side2,
      IEntityRelationshipInfoModel entityRelationshipInfo)
  {
    if (entityRelationshipInfo == null) {
      return null;
    }
    
    List<String> languageCodes = inputModel.getDataTransfer()
        .getModifiedLanguageCodes();
    
    List<IReferencedRelationshipProperty> tags = side2.getTags();
    List<IReferencedRelationshipProperty> attributes = side2.getAttributes();
    List<IReferencedRelationshipProperty> dependentAttributes = side2.getDependentAttributes();
    
    List<IIdAndBaseType> addedEntities = entityRelationshipInfo.getAddedEntities();
    List<IIdAndBaseType> removedEntities = entityRelationshipInfo.getRemovedEntities();
    
    if (addedEntities.isEmpty() && removedEntities.isEmpty()) {
      return null;
    }
    
    IRelationshipDataTransferPropertyModel relationshipDataTransferProperty = new RelationshipDataTransferPropertyModel();
    relationshipDataTransferProperty.setAddedEntities(addedEntities);
    relationshipDataTransferProperty.setRemovedEntities(removedEntities);
    relationshipDataTransferProperty.setTransferIndependentAttributes(attributes);
    relationshipDataTransferProperty.setTransferDependentAttributes(
        getDependentAttributeAsPerLanguage(dependentAttributes, languageCodes));
    relationshipDataTransferProperty.setTransferTags(tags);
    
    return relationshipDataTransferProperty;
  }
  
  private void fillDataForRelationshipDataTransfer(IRelationshipDataTransferInputModel inputModel,
      IEntityRelationshipInfoModel entityRelationshipInfo,
      IGetRelationshipDataTransferModel requestModel, IRelationshipSidePropertiesModel transferSide,
      IRelationshipSidePropertiesModel side1, IRelationshipSidePropertiesModel side2,
      String relationshipId)
  {
    
    fillDataForRelationshipDataTransferForModificationOfProperties(inputModel, requestModel, side1,
        relationshipId);
    fillDataForRelationshipDataTransferForModificationOfProperties(inputModel, requestModel, side2,
        relationshipId);
    
    if (entityRelationshipInfo != null && transferSide != null) {
      fillDataForRelationshipDataTransferOnAdditionOrRemovalOfEntities(inputModel,
          entityRelationshipInfo, requestModel, transferSide, relationshipId);
    }
  }
  
  private void fillDataForRelationshipDataTransferOnAdditionOrRemovalOfEntities(
      IRelationshipDataTransferInputModel inputModel,
      IEntityRelationshipInfoModel entityRelationshipInfo,
      IGetRelationshipDataTransferModel requestModel,
      IRelationshipSidePropertiesModel sideForAddedEntities, String relationshipId)
  {
    List<String> languageCodes = inputModel.getDataTransfer()
        .getModifiedLanguageCodes();
    List<IReferencedRelationshipProperty> tags = sideForAddedEntities.getTags();
    List<IReferencedRelationshipProperty> independentAttributes = sideForAddedEntities
        .getAttributes();
    List<IReferencedRelationshipProperty> dependentAttributes = sideForAddedEntities
        .getDependentAttributes();
    
    // if data transfer is not defined return
    if (tags.isEmpty() && independentAttributes.isEmpty()
        && dependentAttributes.isEmpty() /*&& !shouldEvaluateForRemovedElements*/) {
      return;
    }
    
    // fill data transfer due to addition of entity
    IRelationshipDataTransferPropertyModel relationshipDataTransferDueToAdditionOfEntity = getRelationshipDataTransferDueToAdditionOrRemovalOfEntity(
        entityRelationshipInfo, sideForAddedEntities, languageCodes);
    if (relationshipDataTransferDueToAdditionOfEntity != null) {
      relationshipDataTransferDueToAdditionOfEntity.setContentId(inputModel.getDataTransfer()
          .getContentId());
      relationshipDataTransferDueToAdditionOfEntity.setRelationshipId(relationshipId);
      relationshipDataTransferDueToAdditionOfEntity.setSideId(sideForAddedEntities.getSideId());
      String relationshipSideKey = relationshipId + Seperators.RELATIONSHIP_SIDE_SPERATOR
          + sideForAddedEntities.getSideId();
      requestModel.getRelationshipDataTransferDueToAddedEntity()
          .put(relationshipSideKey, relationshipDataTransferDueToAdditionOfEntity);
      fillPropertyIds(relationshipDataTransferDueToAdditionOfEntity, requestModel);
    }
  }
  
  private void fillDataForRelationshipDataTransferForModificationOfProperties(
      IRelationshipDataTransferInputModel inputModel,
      IGetRelationshipDataTransferModel requestModel,
      IRelationshipSidePropertiesModel sideForModifiedProperties, String relationshipId)
  {
    List<IReferencedRelationshipProperty> tags = sideForModifiedProperties.getTags();
    List<IReferencedRelationshipProperty> independentAttributes = sideForModifiedProperties
        .getAttributes();
    List<IReferencedRelationshipProperty> dependentAttributes = sideForModifiedProperties
        .getDependentAttributes();
    
    if (tags.isEmpty() && independentAttributes.isEmpty() && dependentAttributes.isEmpty()) {
      return;
    }
    
    if (inputModel.getDataTransfer()
        .getChangedAttributeIds()
        .isEmpty()
        && inputModel.getDataTransfer()
            .getChangedTagsIds()
            .isEmpty()
        && inputModel.getDataTransfer()
            .getChangedDependentAttributeIdsMap()
            .isEmpty()) {
      return;
    }
    
    // fill data transfer due to modification properties
    IRelationshipDataTransferPropertyModel relationshipDataTransferDueToModificationOfProperties = getRelationshipDataTransfereDueToModificationOfProperties(
        inputModel, sideForModifiedProperties);
    if (relationshipDataTransferDueToModificationOfProperties != null) {
      relationshipDataTransferDueToModificationOfProperties
          .setContentId(inputModel.getDataTransfer()
              .getContentId());
      relationshipDataTransferDueToModificationOfProperties.setRelationshipId(relationshipId);
      relationshipDataTransferDueToModificationOfProperties
          .setSideId(sideForModifiedProperties.getSideId());
      String relationshipSideKey = relationshipId + Seperators.RELATIONSHIP_SIDE_SPERATOR
          + sideForModifiedProperties.getSideId();
      requestModel.getRelationshipDataTransferDueToModifiedProperty()
          .put(relationshipSideKey, relationshipDataTransferDueToModificationOfProperties);
      fillPropertyIds(relationshipDataTransferDueToModificationOfProperties, requestModel);
    }
  }
  
  /**
   * @author Shubham.Jangir
   * @param entityRelationshipInfo
   * @param side2
   * @param languageCodes
   * @return
   */
  private IRelationshipDataTransferPropertyModel getRelationshipDataTransferDueToAdditionOrRemovalOfEntity(
      IEntityRelationshipInfoModel entityRelationshipInfo, IRelationshipSidePropertiesModel side2,
      List<String> languageCodes)
  {
    List<IReferencedRelationshipProperty> tags = side2.getTags();
    List<IReferencedRelationshipProperty> independentAttributes = side2.getAttributes();
    List<IReferencedRelationshipProperty> dependentAttributes = side2.getDependentAttributes();
    
    if (entityRelationshipInfo == null || (entityRelationshipInfo.getAddedEntities()
        .isEmpty()
        && entityRelationshipInfo.getRemovedEntities()
            .isEmpty())) {
      return null;
    }
    IRelationshipDataTransferPropertyModel relationshipDataTransferProperty = new RelationshipDataTransferPropertyModel();
    relationshipDataTransferProperty.setAddedEntities(entityRelationshipInfo.getAddedEntities());
    relationshipDataTransferProperty
        .setRemovedEntities(entityRelationshipInfo.getRemovedEntities());
    relationshipDataTransferProperty.setTransferDependentAttributes(
        getDependentAttributeAsPerLanguage(dependentAttributes, languageCodes));
    relationshipDataTransferProperty.setTransferIndependentAttributes(independentAttributes);
    relationshipDataTransferProperty.setTransferTags(tags);
    
    return relationshipDataTransferProperty;
  }
  
  private Map<String, List<IReferencedRelationshipProperty>> getDependentAttributeAsPerLanguage(
      List<IReferencedRelationshipProperty> dependentAttributes, List<String> languageCodes)
  {
    Map<String, List<IReferencedRelationshipProperty>> dependentAttributeAsPerLanguage = new HashMap<>();
    languageCodes.forEach(languageCode -> {
      dependentAttributeAsPerLanguage.put(languageCode, dependentAttributes);
    });
    return dependentAttributeAsPerLanguage;
  }
  
  /**
   * @author Shubham.Jangir
   * @param entityRelationshipInfo
   * @param side2
   * @return
   */
  private IRelationshipDataTransferPropertyModel getRelationshipDataTransfereDueToModificationOfProperties(
      IRelationshipDataTransferInputModel inputModel, IRelationshipSidePropertiesModel side2)
  {
    List<String> changedAttributeIds = inputModel.getDataTransfer()
        .getChangedAttributeIds();
    Map<String, List<String>> changedDependentAttributeIdsMap = inputModel.getDataTransfer()
        .getChangedDependentAttributeIdsMap();
    List<String> changedTagsIds = inputModel.getDataTransfer()
        .getChangedTagsIds();
    
    IRelationshipDataTransferPropertyModel relationshipDataTransferProperty = getRelationshipDataDueToModificationOfProperties(
        side2, changedAttributeIds, changedTagsIds, changedDependentAttributeIdsMap);
    
    return relationshipDataTransferProperty;
  }
  
  /**
   * @author Shubham.Jangir
   * @param inputModel
   * @param side2
   * @return
   */
  private IRelationshipDataTransferPropertyModel getRelationshipDataInheritanceDueToAdditionOfProperties(
      IRelationshipDataTransferInputModel inputModel, IRelationshipSidePropertiesModel side2)
  {
    List<String> addedAttributeIds = inputModel.getDataTransfer()
        .getAddedAttributeIds();
    List<String> addedTagsIds = inputModel.getDataTransfer()
        .getAddedTagIds();
    Map<String, List<String>> addedDependentAttributeIdsMap = inputModel.getDataTransfer()
        .getAddedDependentAttributeIdsMap();
    
    if (addedAttributeIds.isEmpty() && addedTagsIds.isEmpty()
        && addedDependentAttributeIdsMap.isEmpty()) {
      return null;
    }
    IRelationshipDataTransferPropertyModel relationshipDataTransferProperty = getRelationshipDataDueToModificationOfProperties(
        side2, addedAttributeIds, addedTagsIds, addedDependentAttributeIdsMap);
    
    return relationshipDataTransferProperty;
  }
  
  /**
   * @author Shubham.Jangir
   * @param side2
   * @param changedAttributeIds
   * @param changedTagsIds
   * @param changedDependentAttributeIdsMap
   * @return
   */
  private IRelationshipDataTransferPropertyModel getRelationshipDataDueToModificationOfProperties(
      IRelationshipSidePropertiesModel side2, List<String> changedAttributeIds,
      List<String> changedTagsIds, Map<String, List<String>> changedDependentAttributeIdsMap)
  {
    List<IReferencedRelationshipProperty> tags = side2.getTags();
    List<IReferencedRelationshipProperty> independentAttributes = side2.getAttributes();
    List<IReferencedRelationshipProperty> dependentAttributes = side2.getDependentAttributes();
    
    List<IReferencedRelationshipProperty> referencedModifiedLanguageIndependentAttributesToTransfer = getReferencedModifiedPropertiesToTransfer(
        independentAttributes, changedAttributeIds);
    Map<String, List<IReferencedRelationshipProperty>> referencedModifiedLanguageDependentAttributesToTransfer = getDependentAttributeToTransfer(
        dependentAttributes, changedDependentAttributeIdsMap);
    List<IReferencedRelationshipProperty> referencedModifiedTagToTransfer = getReferencedModifiedPropertiesToTransfer(
        tags, changedTagsIds);
    
    if (referencedModifiedLanguageIndependentAttributesToTransfer.isEmpty()
        && referencedModifiedLanguageDependentAttributesToTransfer.isEmpty()
        && referencedModifiedTagToTransfer.isEmpty()) {
      return null;
    }
    
    IRelationshipDataTransferPropertyModel relationshipDataTransferProperty = new RelationshipDataTransferPropertyModel();
    relationshipDataTransferProperty
        .setTransferDependentAttributes(referencedModifiedLanguageDependentAttributesToTransfer);
    relationshipDataTransferProperty.setTransferIndependentAttributes(
        referencedModifiedLanguageIndependentAttributesToTransfer);
    relationshipDataTransferProperty.setTransferTags(referencedModifiedTagToTransfer);
    
    if (!referencedModifiedLanguageIndependentAttributesToTransfer.isEmpty()) {
      // setModifiedLanguageCodes
    }
    return relationshipDataTransferProperty;
  }
  
  private Map<String, List<IReferencedRelationshipProperty>> getDependentAttributeToTransfer(
      List<IReferencedRelationshipProperty> dependentAttributes,
      Map<String, List<String>> changedDependentAttributeIdsMap)
  {
    Map<String, List<IReferencedRelationshipProperty>> changedAttributesAsPerLanguage = new HashMap<>();
    
    changedDependentAttributeIdsMap.forEach((languageCode, changedAttributeIds) -> {
      Stream<IReferencedRelationshipProperty> dependentAttributeStream = dependentAttributes
          .stream();
      Stream<IReferencedRelationshipProperty> filterSteam = dependentAttributeStream
          .filter(dependentAttribute -> changedAttributeIds.contains(dependentAttribute.getId()));
      List<IReferencedRelationshipProperty> filterDependentAttribute = filterSteam
          .collect(Collectors.toList());
      if (!filterDependentAttribute.isEmpty()) {
        changedAttributesAsPerLanguage.put(languageCode, filterDependentAttribute);
      }
    });
    
    return changedAttributesAsPerLanguage;
  }
  
  private void fillPropertyIds(
      IRelationshipDataTransferPropertyModel relationshipDataTransferProperty,
      IGetRelationshipDataTransferModel requestModel)
  {
    Map<String, List<IReferencedRelationshipProperty>> transferDependentAttributes = relationshipDataTransferProperty
        .getTransferDependentAttributes();
    List<IReferencedRelationshipProperty> transferInependentAttributeIds = relationshipDataTransferProperty
        .getTransferInependentAttributes();
    List<IReferencedRelationshipProperty> gettransferTagIds = relationshipDataTransferProperty
        .getTransferTags();
    
    requestModel.getDependentAttributeIdsToTransfer()
        .addAll(collectDependentAttributeIds(transferDependentAttributes));
    requestModel.getIndependentAttributeIdsToTransfer()
        .addAll(collectIds(transferInependentAttributeIds));
    requestModel.getTagIdsToTransfer()
        .addAll(collectIds(gettransferTagIds));
  }
  
  private List<String> collectDependentAttributeIds(
      Map<String, List<IReferencedRelationshipProperty>> transferDependentAttributes)
  {
    Stream<List<IReferencedRelationshipProperty>> internalStream = transferDependentAttributes
        .values()
        .stream();
    Stream<IReferencedRelationshipProperty> flatMap = internalStream.flatMap(x -> x.stream());
    List<String> dependentAttributeList = flatMap.map(attribute -> attribute.getId())
        .distinct()
        .collect(Collectors.toList());
    return dependentAttributeList;
  }
  
  private List<String> collectIds(
      List<IReferencedRelationshipProperty> referencedRelationshipProperties)
  {
    return referencedRelationshipProperties.stream()
        .map(IReferencedRelationshipProperty::getId)
        .distinct()
        .collect(Collectors.toList());
  }
  
  private List<IReferencedRelationshipProperty> getReferencedModifiedPropertiesToTransfer(
      List<IReferencedRelationshipProperty> properties, List<String> changedPropertyIds)
  {
    return properties.stream()
        .filter(property -> changedPropertyIds.contains(property.getId()))
        .collect(Collectors.toList());
  }
}
