package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetailsForTaskAndEventTab;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForTasksTab extends AbstractConfigDetailsForTaskAndEventTab {
  
  public GetConfigDetailsForTasksTab(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForTasksTab/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    return getConfigDetails(requestMap, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
  }
  
  protected Map<String, Object> getConfigDetails(Map<String, Object> requestMap, String nodeLabel)
      throws Exception
  {
    String userId = (String) requestMap.get("userId");
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setUserId(userId);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Map<String, Object> mapToReturn = getMapToReturn(referencedDataRuleMap);
    fillEntityIdsHavingReadPermission(userInRole, mapToReturn);
    
    Map<String, List<String>> tagIdTagValueIdsMap = (Map<String, List<String>>) requestMap
        .get(IMulticlassificationRequestModel.TAG_ID_TAG_VALUE_IDS_MAP);
    helperModel.setTagIdTagValueIdsMap(tagIdTagValueIdsMap);
    Boolean shouldUseTagIdTagValueIdsMap = (Boolean) requestMap
        .get(IMulticlassificationRequestModel.SHOULD_USE_TAG_ID_TAG_VALUE_IDS_MAP);
    helperModel.setShouldUseTagIdTagValueIdsMap(shouldUseTagIdTagValueIdsMap);
    
    String requestedTypeId = (String) requestMap.get(IMulticlassificationRequestModel.TYPE_ID);
    requestedTypeId = requestedTypeId == null ? CommonConstants.ALL_PROPERTY : requestedTypeId;
    
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    helperModel.setInstanceKlassIds(new HashSet<>(klassIds));
    fillKlassDetails(mapToReturn, klassIds, referencedDataRuleMap, helperModel, requestedTypeId,
        UtilClass.getCodeNew(userInRole));
    ConfigDetailsUtils.fillLinkedVariantsConfigInfo(mapToReturn, klassIds);
    
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    fillTaxonomyDetails(mapToReturn, taxonomyIds, referencedDataRuleMap, helperModel,
        requestedTypeId);
    
    List<String> collectionIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.COLLECTION_IDS);
    fillCollectionDetails(collectionIds, helperModel, requestedTypeId);
    
    fillReferencedPermission(mapToReturn, CommonConstants.CUSTOM_TEMPLATE, userInRole, helperModel);
    String requestedTemplateId = (String) requestMap
        .get(IMulticlassificationRequestModel.TEMPLATE_ID);
    if (requestedTemplateId == null) {
      fillTemplateDetails(mapToReturn, helperModel, requestedTypeId, SystemLevelIds.TASK_TAB);
    }
    else {
      fillTemplateDetailsForCustomTemplate(mapToReturn, helperModel, SystemLevelIds.TASK_TAB,
          requestedTemplateId);
    }
    fillReferencedContextLinkedToKlass(helperModel, mapToReturn);
    fillPersonalTaskIds(mapToReturn);
    fillMandatoryReferencedAttributes(mapToReturn);
    fillReferencedRACIVSRoles(mapToReturn);
    fillReferencedLanguages(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    mapToReturn.put(IGetConfigDetailsForTasksTabModel.ROLE_ID_OF_CURRENT_USER,
        UtilClass.getCodeNew(userInRole));
    return mapToReturn;
  }
  
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRulesMap)
  {
    
    Map<String, Object> mapToReturn = super.getMapToReturn(referencedDataRulesMap);
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedRoles = new HashMap<>();
    
    mapToReturn.put(IGetConfigDetailsForTasksTabModel.REFERENCED_TAGS, referencedTags);
    mapToReturn.put(IGetConfigDetailsForTasksTabModel.REFERENCED_ROLES, referencedRoles);
    
    return mapToReturn;
  }
  
  private void fillReferencedContextLinkedToKlass(IGetConfigDetailsHelperModel helperModel,
      Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    
    fillReferencedContextLinkedToKlass(embeddedContexts, referencedTags,
        helperModel);
  }
  
  protected void fillKlassDetails(Map<String, Object> mapToReturn, List<String> klassIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel,
      String requestedTypeId, String roleId) throws Exception
  {
    List<Map<String, Object>> referencedTemplates = (List<Map<String, Object>>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_TEMPLATES);
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexById(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
        if (klassId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(klassVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        }
        helperModel.setKlassType(klassVertex.getProperty(IKlass.TYPE));
        if (isNature != null && isNature) {
          helperModel.setNatureNode(klassVertex);
        }
        else {
          helperModel.getNonNatureKlassNodes()
              .add(klassVertex);
        }
        
        List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
            IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
            IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.PREVIEW_IMAGE, IKlass.CODE);
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
        
        Integer numberOfVersionsToMaintain = (Integer) klassMap
            .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
            .get(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
          mapToReturn.put(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
              numberOfVersionsToMaintain);
        }
        fillReferencedTagsAndLifeCycleStatusTags(mapToReturn, klassVertex);
        fillReferencedTasks(klassVertex, mapToReturn,roleId);
        fillPropertyCollectionsForKlass(helperModel, klassVertex);
        fillEmbeddedContextForKlass(klassVertex, helperModel);
        fillRelationshipsForKlass(helperModel, klassVertex);
        referencedKlassMap.put(klassId, klassMap);
        if (isNature != null && isNature) {
          fillNatureRelationshipForKlass(helperModel, klassVertex);
        }
        fillReferencedTemplates(referencedTemplates, roleId, klassId, referencedPermissions);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
  }
  
  /**
   * fills referencedTasks for the associated klassIds
   *
   * @author Arshad
   * @param klassIds
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillReferencedTasks(Vertex klassVertex, Map<String, Object> mapToReturn, String roleId)
      throws Exception
  {
    Map<String, Object> referencedTasks = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TASKS);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    
    Iterable<Vertex> taskVertices = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TASK);
    for (Vertex taskVertex : taskVertices) {
      String taskId = UtilClass.getCodeNew(taskVertex);
      if (referencedTasks.get(taskId) != null) {
        continue;
      }
      
      Map<String, Object> taskMap = TasksUtil.getTaskMapFromNode(taskVertex);
     if(TasksUtil.isAnyPermissionsAvailableForTaskInstanceToUser(roleId,taskId)) {
      referencedTasks.put(taskId, taskMap);
     }
      String statusTagId = (String) taskMap.get(ITask.STATUS_TAG);
      if (statusTagId != null && !referencedTags.containsKey(statusTagId)) {
        Vertex statusTag = UtilClass.getVertexById(statusTagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(statusTag, true);
        referencedTags.put(statusTagId, referencedTag);
      }
      String priorityTagId = (String) taskMap.get(ITask.PRIORITY_TAG);
      if (priorityTagId != null && !referencedTags.containsKey(priorityTagId)) {
        Vertex priorityTag = UtilClass.getVertexById(priorityTagId,
            VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(priorityTag, true);
        referencedTags.put(priorityTagId, referencedTag);
      }
    }
  }
 }
