package com.cs.core.runtime.strategy.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.template.ICustomTemplateTab;
import com.cs.core.config.interactor.entity.template.ITabPermission;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplateTab;
import com.cs.core.config.interactor.entity.template.TabPermission;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionForSingleEntityModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForRuntimeEntitiesResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForSingleEntityModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.GetGlobalPermissionModel;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedRolePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.strategy.usecase.attribute.IGetRolesStrategy;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionForRuntimeEntitiesStrategy;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionForSingleEntityStrategy;
import com.cs.core.config.strategy.usecase.role.IGetRoleByUserStrategy;
import com.cs.core.config.strategy.usecase.user.IGetFunctionPermissionByUserIdStrategy;
import com.cs.core.config.strategy.usecase.user.IGetUsersByRoleStrategy;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplateConfigDetails;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplatePermissionModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridContentInstanceModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridInstanceConfigDetailsModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridInstanceReferencedPermission;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewModel;
@Component
public class PermissionUtils {
  
  @Autowired
  protected ISessionContext                                context;
  
  @Autowired
  protected IGetGlobalPermissionForRuntimeEntitiesStrategy getGlobalPermissionForRuntimeEntitiesStrategy;
  
  @Autowired
  protected IGetGlobalPermissionForSingleEntityStrategy    getGlobalPermissionForSingleEntityStrategy;
  
  @Autowired
  protected IGetGlobalPermissionForSingleEntityStrategy    getGlobalPermissionForSingleEntityForRoleStrategy;
  
  @Autowired
  IGetUsersByRoleStrategy                                  getUsersByRoleStrategy;
  
  @Autowired
  IGetRoleByUserStrategy                                   getRoleByUserStrategy;
  
  @Autowired
  protected IGetRolesStrategy                    getRolesByIdsStrategy;
  
  @Autowired
  IGetFunctionPermissionByUserIdStrategy getFunctionPermissionByUserIdStrategy;

  public IFunctionPermissionModel getFunctionPermissionByUserId () throws Exception {
    String loginUserId = context.getUserId();
    IIdParameterModel idParameterModel = new IdParameterModel();
    idParameterModel.setCurrentUserId(loginUserId);
    return getFunctionPermissionByUserIdStrategy.execute(idParameterModel);

  }

  public IGlobalPermissionWithAllowedModuleEntitiesModel getGlobalPermissionForSingleEntity(String taskId, String type, List<String> roleIds) throws Exception
  {
    String loginUserId = context.getUserId();
    IGetGlobalPermissionForSingleEntityModel getGlobalPermissionModel = new GetGlobalPermissionForSingleEntityModel();
    getGlobalPermissionModel.setEntityId(taskId);
    getGlobalPermissionModel.setType(type);
    getGlobalPermissionModel.setRoleIdsContainingLoginUser(roleIds);
    getGlobalPermissionModel.setUserId(loginUserId);
    
    return getGlobalPermissionForSingleEntityStrategy.execute(getGlobalPermissionModel);
  }

  public List<String> getRoleIdsWithCurrentUser(List<String> listOfUsers, List<String> listOfRoles)throws Exception
  {
    String currentUserId = context.getUserId();
    List<String> idsToReturn = new ArrayList<>();
    for (String userId : listOfUsers) {
      if (userId.equals(currentUserId)) {
        // getRolesByUserId
        IIdParameterModel model = new IdParameterModel();
        model.setId(currentUserId);
        IIdsListParameterModel roleModel = getRoleByUserStrategy.execute(model);
        for (String roleId : roleModel.getIds()) {
          idsToReturn.add(roleId);
        }
        break;
      }
    }
    for (String roleId : listOfRoles) {
      // getUsers for role
      IIdParameterModel model = new IdParameterModel();
      model.setId(roleId);
      IGetGridUsersResponseModel getGridUsersResponseModel = getUsersByRoleStrategy.execute(model);
      for (IUser user : getGridUsersResponseModel.getUsersList()) {
        if (user.getId()
            .equals(currentUserId)) {
          idsToReturn.add(roleId);
          break;
        }
      }
    }
    return idsToReturn;
  }
  
  public IGlobalPermission getCompleteRightGlobalPermission()
  {
    IGlobalPermission globalPermission = new GlobalPermission();
    globalPermission.setCanCreate(true);
    globalPermission.setCanDelete(true);
    globalPermission.setCanRead(true);
    globalPermission.setCanEdit(true);
    return globalPermission;
  }

  public Boolean isUserHasPermissionToCreate(String taskId, String type, String moduleEntityType) throws Exception
  {
    String loginUserId = context.getUserId();
    IGetGlobalPermissionForSingleEntityModel getGlobalPermissionModel = new GetGlobalPermissionForSingleEntityModel();
    getGlobalPermissionModel.setEntityId(taskId);
    getGlobalPermissionModel.setType(type);
    getGlobalPermissionModel.setUserId(loginUserId);
    
    IGlobalPermissionWithAllowedModuleEntitiesModel response = getGlobalPermissionForSingleEntityStrategy.execute(getGlobalPermissionModel);

    if(moduleEntityType!= null && !response.getAllowedEntities().contains(moduleEntityType)) {
      return false;
    }
    return response.getGlobalPermission().getCanCreate();
  }
  public IGlobalPermissionWithAllowedModuleEntitiesModel getGlobalPermissionForSingleEntityForUser(String taskId, String type, String userId) throws Exception
  {
   
    IGetGlobalPermissionForSingleEntityModel getGlobalPermissionModel = new GetGlobalPermissionForSingleEntityModel();
    getGlobalPermissionModel.setEntityId(taskId);
    getGlobalPermissionModel.setType(type);
    getGlobalPermissionModel.setUserId(userId);
    IGlobalPermissionWithAllowedModuleEntitiesModel response = getGlobalPermissionForSingleEntityStrategy.execute(getGlobalPermissionModel);
    return response;
  }
  
  public IGlobalPermissionWithAllowedModuleEntitiesModel getGlobalPermissionForSingleEntityForRole(String taskId, String type, String roleId) throws Exception
  {
   
    IGetGlobalPermissionForSingleEntityModel getGlobalPermissionModel = new GetGlobalPermissionForSingleEntityModel();
    getGlobalPermissionModel.setEntityId(taskId);
    getGlobalPermissionModel.setType(type);
    getGlobalPermissionModel.setUserId(roleId);
    IGlobalPermissionWithAllowedModuleEntitiesModel response = getGlobalPermissionForSingleEntityForRoleStrategy.execute(getGlobalPermissionModel);
    return response;
  }
  
  public IListModel<IConfigEntityInformationModel> getRolesByIds(List<String> roleIds)
      throws Exception
  {
    IIdsListParameterModel idsListModel = new IdsListParameterModel();
    idsListModel.setIds(roleIds);
    return getRolesByIdsStrategy.execute(idsListModel);
  }
  @Deprecated
  public void manageKlassInstancePermissions(List<? extends IRoleInstance> roleInstances,
      IGetMultiClassificationKlassDetailsModel multiClassificationKlassDetails) throws Exception
  {

    IReferencedRolePermissionModel mergeRolePermission = multiClassificationKlassDetails.getReferencedPermission();

    Set<String> mergedEditablePropertyCollectionIds = getMergedEditablePropertyCollectionIds(multiClassificationKlassDetails);

    Set<String> editablePropertyIds = mergeRolePermission.getEnabledSectionElementIds(); //contains all editable property id's for current user
    Set<String> mergedEditablePropertyIds = new HashSet<>(); //contains editable property id's only if its containing property collection is also editable
    Set<String> propertyIdsLinkedToPropertyCollections = new HashSet<>();
    
    Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollections = multiClassificationKlassDetails.getReferencedPropertyCollections();
    Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollectionsClone = new HashMap<>(referencedPropertyCollections);
    for (Map.Entry<String, IReferencedPropertyCollectionModel> entry : referencedPropertyCollectionsClone.entrySet()) {
      String key = entry.getKey();
      IReferencedPropertyCollectionModel referencedPropertyCollection = entry.getValue();
      for (IReferencedPropertyCollectionElementModel propertyCollectionElement : referencedPropertyCollection.getElements()) {
        String propertyId = propertyCollectionElement.getId();
        propertyIdsLinkedToPropertyCollections.add(propertyId);
        //add editable property id's to mergedEditablePropertyIds only if its containing property collection is also editable
        if (mergedEditablePropertyCollectionIds.contains(key) && editablePropertyIds.contains(propertyId)) {
          mergedEditablePropertyIds.add(propertyId);
        }
      }
      
      if(!mergeRolePermission.getVisiblePropertyCollectionIds().contains(key)) {
        referencedPropertyCollections.remove(key);
        continue;
      }
      
      if(!mergeRolePermission.getOpenedPropertyCollectionIds().contains(key)) {
        IReferencedPropertyCollectionModel value = referencedPropertyCollections.get(key);
        value.setIsCollapsed(true);
      }
    }
    
    //remove all the property id's linked to the collections 
    //after that enabledSectionElementIds will contain property id's for which permission is not configured
    //e.g context property collection elements or nature relationship  
    editablePropertyIds.removeAll(propertyIdsLinkedToPropertyCollections);
    mergedEditablePropertyIds.addAll(editablePropertyIds);
    
    if(!multiClassificationKlassDetails.getGlobalPermission().getCanEdit()) {
      mergedEditablePropertyCollectionIds.clear();
    }
    
    Map<String, IReferencedSectionElementModel> referencedElements = multiClassificationKlassDetails.getReferencedElements();
    for (IReferencedSectionElementModel referencedElement : referencedElements.values()) {
      String elementId = referencedElement.getId();
      if (mergedEditablePropertyIds.contains(elementId)) {
        referencedElement.setIsDisabled(false);
      }
      else {
        referencedElement.setIsDisabled(true);
      }
  }
    multiClassificationKlassDetails.setCanCreateNature(true);
  }

  private Set<String> getMergedEditablePropertyCollectionIds(
      IGetMultiClassificationKlassDetailsModel multiClassificationKlassDetailsModel)
  {
    if(!multiClassificationKlassDetailsModel.getGlobalPermission().getCanEdit()) {
      return new HashSet<>();
    }
    IReferencedRolePermissionModel mergeRolePermission = multiClassificationKlassDetailsModel.getReferencedPermission();
    Set<String> editableKlassAndTaxonomyIds = mergeRolePermission.getEditableKlassAndTaxonomyIds();
    
    Set<String> mergedEditablePropertyCollectionIds = new HashSet<>(); //contains editable property collection id's only if its containing class or taxonomy is also editable
    Set<String> editablePropertyCollectionIds = mergeRolePermission.getEditablePropertyCollectionIds(); //contains all editable property collection id's for current user 
    Set<String> propertyCollectionIdsLinkedToKlassesOrTaxonomies = new HashSet<>();
    
    Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = multiClassificationKlassDetailsModel.getReferencedKlasses();
    for (Map.Entry<String, IReferencedKlassDetailStrategyModel> entry : referencedKlasses.entrySet()) {
      String entityId = entry.getKey();
      IReferencedKlassDetailStrategyModel klassDetails = entry.getValue();
      List<String> propertyCollections = klassDetails.getPropertyCollections();
      for (String propertyCollectionId : propertyCollections) {
        propertyCollectionIdsLinkedToKlassesOrTaxonomies.add(propertyCollectionId);
        if(editablePropertyCollectionIds.contains(propertyCollectionId) && editableKlassAndTaxonomyIds.contains(entityId)) {
          mergedEditablePropertyCollectionIds.add(propertyCollectionId);
        }
      }
    }
    
    Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies = multiClassificationKlassDetailsModel.getReferencedTaxonomies();
    for (Map.Entry<String, IReferencedArticleTaxonomyModel> entry : referencedTaxonomies.entrySet()) {
      String entityId = entry.getKey();
      IReferencedArticleTaxonomyModel taxonomyDetails = entry.getValue();
      List<String> propertyCollections = taxonomyDetails.getPropertyCollections();
      for (String propertyCollectionId : propertyCollections) {
        propertyCollectionIdsLinkedToKlassesOrTaxonomies.add(propertyCollectionId);
        if(editablePropertyCollectionIds.contains(propertyCollectionId) && editableKlassAndTaxonomyIds.contains(entityId)) {
          mergedEditablePropertyCollectionIds.add(propertyCollectionId);
        }
      }
    }
    
    editablePropertyCollectionIds.removeAll(propertyCollectionIdsLinkedToKlassesOrTaxonomies);
    mergedEditablePropertyCollectionIds.addAll(editablePropertyCollectionIds);
    
    return mergedEditablePropertyCollectionIds;
  }

  public void manageKlassInstancePermissions(IGetKlassInstanceModel returnModel)
  {
    IGetConfigDetailsModel configDetails = returnModel.getConfigDetails();
    manageKlassInstancePermissions(configDetails, null);
    
    Set<String> taskIdsHavingRP = configDetails.getReferencedPermissions().getTaskIdsHavingReadPermissions();
    
    Integer tasksCount = returnModel.getTasksCount();
    if(tasksCount==0 && taskIdsHavingRP.isEmpty())
    {
      configDetails.getReferencedTasks().clear();
    }
  }
  
  public void manageKlassInstancePermissions( IGetConfigDetailsModel configDetails)
  {
    manageKlassInstancePermissions(configDetails, null);
  }
  
  /**
   * manage Permissions using referencedPermission (Orient)
   * 
   * @param configDetails
   * @throws Exception
   */
  public void manageKlassInstancePermissions(IGetConfigDetailsModel configDetails, String tabType)
  {
    IReferencedTemplatePermissionModel permission = configDetails.getReferencedPermissions();

    Set<String> visiblePCIds = permission.getVisiblePropertyCollectionIds();
    Set<String> visibleRelationshipIds = permission.getVisibleRelationshipIds();
    Set<String> visiblePropertyIds = permission.getVisiblePropertyIds();
    
    Set<String> editablePCIds = permission.getEditablePropertyCollectionIds();
    Set<String> pCIdsClone = new HashSet<>(visiblePCIds);
    pCIdsClone.addAll(editablePCIds);
    Set<String> editablePropertyIds = permission.getEditablePropertyIds(); //contains all editable property id's for current user
    Set<String> mergedEditablePropertyIds = new HashSet<>();               //contains editable property id's only if its containing property collection is also editable
    Set<String> mergedVisiblePropertyIds = new HashSet<>();

    Set<String> canAddRelationshipIds = permission.getCanAddRelationshipIds();
    Set<String> canDeleteRelationshipIds = permission.getCanDeleteRelationshipIds();
    ITabPermission tabPermission = permission.getTabPermission();
    if(tabPermission == null) {
      tabPermission = new TabPermission();
      permission.setTabPermission(tabPermission);
    }

    if (!tabPermission.getCanEdit()) {
      editablePCIds.clear();
      editablePropertyIds.clear();
      canAddRelationshipIds.clear();
      canDeleteRelationshipIds.clear();
    }
    
    Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();
    Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies = configDetails.getReferencedTaxonomies();
    
    Set<String> referencedElementsToRemoved = new HashSet<>();
    
    if (configDetails instanceof GetConfigDetailsForCustomTabModel) {
      Set<String> propertyCollectionIdsAssociatedByType = new HashSet<>();
      referencedKlasses.forEach((klassId, referencedKlass) -> {
        propertyCollectionIdsAssociatedByType.addAll(referencedKlass.getPropertyCollections());
      });
      referencedTaxonomies.forEach((klassId, referencedTaxonomy) -> {
        propertyCollectionIdsAssociatedByType.addAll(referencedTaxonomy.getPropertyCollections());
      });
      propertyCollectionIdsAssociatedByType.addAll(pCIdsClone);
      Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollections = null;
      referencedPropertyCollections = ((IGetConfigDetailsForCustomTabModel) configDetails).getReferencedPropertyCollections();

      Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollectionsClone = new HashMap<>(referencedPropertyCollections);
      
      for (Map.Entry<String, IReferencedPropertyCollectionModel> entry : referencedPropertyCollectionsClone.entrySet()) {
        String pCId = entry.getKey();
        if (!propertyCollectionIdsAssociatedByType.contains(pCId)) {
          continue;
        }
        if (!visiblePCIds.contains(pCId)) {
          referencedPropertyCollections.remove(pCId);
          for (IReferencedPropertyCollectionElementModel element : entry.getValue().getElements()) {
            String elementId = element.getId();
            referencedElementsToRemoved.add(elementId);
          }
          continue;
        }
        
        // add editable property id's to mergedEditablePropertyIds only if its
        // containing property collection is also editable
        IReferencedPropertyCollectionModel referencedPropertyCollection = entry.getValue();
        for (IReferencedPropertyCollectionElementModel propertyCollectionElement : referencedPropertyCollection.getElements()) {
          String propertyId = propertyCollectionElement.getId();
          if (editablePropertyIds.contains(propertyId) && editablePCIds.contains(pCId)) {
            mergedEditablePropertyIds.add(propertyId);
          }
          if (visiblePropertyIds.contains(propertyId)) {
            mergedVisiblePropertyIds.add(propertyId);
          }
        }
      }
    }
    else{
      mergedEditablePropertyIds = editablePropertyIds;
    }
    
    //retains only visible propertyIds (this is done to handle usecase if property is present in more than 2 PCs, 1 visible other is not)
    referencedElementsToRemoved.removeAll(mergedVisiblePropertyIds);
    visiblePropertyIds.removeAll(referencedElementsToRemoved);
    
    Set<String> alltaxonomyIdsHavingRP = permission.getAllTaxonomyIdsHavingRP();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    for(IReferencedSectionElementModel element:referencedElements.values())
    {
      String id = element.getId();
      String elementType = element.getType();
      String propertyId = element.getPropertyId();
      if(visiblePropertyIds.contains(id) || visibleRelationshipIds.contains(propertyId))
      {
        element.setCanRead(true);
      }
      else {
        //temp fix to check if relationship, dont remove from referenced elements..
        if(!elementType.equals(CommonConstants.RELATIONSHIP)){
          referencedElementsToRemoved.add(id);
        }
      }
      if (elementType.equals(CommonConstants.TAXONOMY) && !alltaxonomyIdsHavingRP.contains(id)) {
        element.setCanRead(false);
      }
    }

    referencedElements.keySet().removeAll(referencedElementsToRemoved);
    
    manageReferencedElementsAndRelationshipForPermissions(configDetails, visibleRelationshipIds, mergedEditablePropertyIds, canAddRelationshipIds, canDeleteRelationshipIds);
    
    //retain referenced Taxonomies having read permissions
    //Set<String> allTaxonomyIdsHavingRP = permission.getAllTaxonomyIdsHavingRP();
    //referencedTaxonomies.keySet().retainAll(allTaxonomyIdsHavingRP);
    
    //retain only those klasses having read permission
    Set<String> klassIdsHavingReadPermission = permission.getKlassIdsHavingRP();
    if(!klassIdsHavingReadPermission.isEmpty())
    {
      //referencedKlasses.keySet().retainAll(klassIdsHavingReadPermission);
      retainReferencedTemplatesHavingRP(configDetails, permission, klassIdsHavingReadPermission);
    }
    
    Map<String, IReferencedVariantContextModel> embeddedVariantContexts = configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts();
    if(embeddedVariantContexts!=null)
    {
      manageContextsPermissions(configDetails, embeddedVariantContexts);
    }
    
    filterPropertysForCustomTemplate(configDetails);
    
    filterPropertySequenceForSelectedTab(configDetails);
    
    filterLinkedEntities(configDetails);
    
    filterOutArticleAndGoldenArticleRelationship(configDetails);
  }

  private void filterOutArticleAndGoldenArticleRelationship(IGetConfigDetailsModel configDetails)
  {
    ITemplate referencedTemplate = configDetails.getReferencedTemplate();
    List<ITemplateTab> tabs = referencedTemplate.getTabs();
    for (ITemplateTab iTemplateTab : tabs) {
      List<String> propertySequenceList = iTemplateTab.getPropertySequenceList();
      propertySequenceList.remove(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID);
    }
  }

  private void filterLinkedEntities(IGetConfigDetailsModel configDetails)
  {
    IReferencedTemplatePermissionModel referencedPermissions = configDetails.getReferencedPermissions();
    Set<String> entitiesHavingRP = referencedPermissions.getEntitiesHavingRP();
    IReferencedContextModel referencedVariantContexts = configDetails.getReferencedVariantContexts();
    Map<String, IReferencedVariantContextModel> embeddedVariantContexts = referencedVariantContexts.getEmbeddedVariantContexts();
    embeddedVariantContexts.forEach((contextId, embeddedVariantContext) -> {
      embeddedVariantContext.getEntities().retainAll(entitiesHavingRP);
    });
  }

  private void filterPropertySequenceForSelectedTab(IGetConfigDetailsModel configDetails)
  {
    if(!(configDetails instanceof IGetConfigDetailsForCustomTabModel)) {
      return;
    }
    Set<String> propertyIdsToRetain = new HashSet<>();
    IReferencedTemplatePermissionModel referencedPermissions = configDetails.getReferencedPermissions();
    propertyIdsToRetain.addAll(referencedPermissions.getVisibleRelationshipIds());
    propertyIdsToRetain.addAll(((IGetConfigDetailsForCustomTabModel)configDetails).getReferencedPropertyCollections().keySet());
    Set<String> klassIdsHavingReadPermission = referencedPermissions.getKlassIdsHavingRP();
    IReferencedContextModel referencedVariantContexts = configDetails.getReferencedVariantContexts();
    Map<String, IReferencedVariantContextModel> embeddedVariantContexts = referencedVariantContexts.getEmbeddedVariantContexts();
    propertyIdsToRetain.addAll(embeddedVariantContexts.keySet());
    Set<String> embeddedVariantToRemove = new HashSet<>();
    if (!klassIdsHavingReadPermission.isEmpty()) {
      Collection<IReferencedVariantContextModel> listOfAssociatedEmbeddedContexts = embeddedVariantContexts.values();
      for (IReferencedVariantContextModel iReferencedVariantContextModel : listOfAssociatedEmbeddedContexts) {
        String contextKlassId = iReferencedVariantContextModel.getContextKlassId();
        if(contextKlassId != null && !klassIdsHavingReadPermission.contains(contextKlassId)) {
          embeddedVariantToRemove.add(iReferencedVariantContextModel.getId());
        }
      }
    }
    propertyIdsToRetain.removeAll(embeddedVariantToRemove);
    propertyIdsToRetain.addAll(((IGetConfigDetailsForCustomTabModel)configDetails).getReferencedRelationships().keySet());
    propertyIdsToRetain.addAll(((IGetConfigDetailsForCustomTabModel)configDetails).getReferencedNatureRelationships().keySet());
    ITemplate referencedTemplate = configDetails.getReferencedTemplate();
    List<ITemplateTab> tabs = referencedTemplate.getTabs();
    for (ITemplateTab iTemplateTab : tabs) {
      Boolean isSelected = iTemplateTab.getIsSelected();
      if(!isSelected || !(iTemplateTab instanceof ICustomTemplateTab) || !(configDetails instanceof IGetConfigDetailsForCustomTabModel)) {
        continue;
      }
      List<String> propertySequenceList = ((ICustomTemplateTab)iTemplateTab).getPropertySequenceList();
      propertySequenceList.retainAll(propertyIdsToRetain);
    }
  }

  @SuppressWarnings("unchecked")
  private void filterPropertysForCustomTemplate(IGetConfigDetailsModel configDetails)
  {
    ITemplate referencedTemplate = configDetails.getReferencedTemplate();
    List<ITemplateTab> tabs = referencedTemplate.getTabs();
    for (ITemplateTab tab : tabs) {
      Boolean isSelected = tab.getIsSelected();
      if(!isSelected || !(tab instanceof ICustomTemplateTab) || !(configDetails instanceof IGetConfigDetailsForCustomTabModel)) {
        continue;
      }
      Set<String> propertyCollectionToRetain = new HashSet<>();
      Set<String> propertyCollectionIdsToRemove = new HashSet<>();
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies = configDetails.getReferencedTaxonomies();
      referencedTaxonomies.forEach((taxonomyId, referencedTaxonomy) -> {
        Map<String, Object> taxonomyMap = ObjectMapperUtil.convertValue(referencedTaxonomy, Map.class);
        Map<String, Object> rootLevelParent = new HashMap<>(taxonomyMap);
        Map<String, Object> parent = (Map<String, Object>)(rootLevelParent.get(IReferencedArticleTaxonomyModel.PARENT));
        while (!(parent.get(IReferencedTaxonomyParentModel.ID)).equals("-1")) {
          rootLevelParent.clear();
          rootLevelParent.putAll(parent);
          parent = (Map<String, Object>)parent.get(IReferencedArticleTaxonomyModel.PARENT);
        }
        String taxonomyType = (String)rootLevelParent.get(IReferencedArticleTaxonomyModel.TAXONOMY_TYPE);
        if (taxonomyType.equals(CommonConstants.MINOR_TAXONOMY)) {
          propertyCollectionIdsToRemove.addAll(referencedTaxonomy.getPropertyCollections());
        }
        else
        {
          propertyCollectionToRetain.addAll(referencedTaxonomy.getPropertyCollections());
        }
      });
      Set<String> propertySequenceList = new HashSet<>(((ICustomTemplateTab)tab).getPropertySequenceList());
      propertySequenceList.addAll(propertyCollectionIdsToRemove);
      Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollections = ((IGetConfigDetailsForCustomTabModel) configDetails).getReferencedPropertyCollections();
      referencedPropertyCollections.keySet().retainAll(propertySequenceList); 
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();
      referencedKlasses.forEach((klassId, referencedKlass) -> {
        List<String> propertyCollections = referencedKlass.getPropertyCollections();
        //property Collection Ids is null when embedded klass is in referenced klass
        if(propertyCollections != null) {
          propertyCollectionToRetain.addAll(propertyCollections);
        }
      });
      propertyCollectionToRetain.retainAll(tab.getPropertySequenceList());
      propertyCollectionIdsToRemove.removeAll(propertyCollectionToRetain);
      tab.getPropertySequenceList().removeAll(propertyCollectionIdsToRemove);
    }
  }

  /**
   * Only those allowed templates are retained whose klass has RP
   * 
   * @param configDetails
   * @param permission
   * @param referencedKlasses
   *
   * @author Kshitij
   */
  private void retainReferencedTemplatesHavingRP(IGetConfigDetailsModel configDetails,
      IReferencedTemplatePermissionModel permission,
      Set<String> klassIdsHavingReadPermission)
  {
    permission.getKlassIdVsTemplateIdsMap().keySet().retainAll(klassIdsHavingReadPermission);
    Set<String> templateIdsToRetain = new HashSet<>();
    for (Set<String> templateIdsSet : permission.getKlassIdVsTemplateIdsMap().values()) {
      templateIdsToRetain.addAll(templateIdsSet);
    }
    List<IConfigEntityInformationModel> referencedTemplatesToRetain = new ArrayList<>();
    for (IConfigEntityInformationModel referencedTemplate : configDetails.getReferencedTemplates()) {
      if (templateIdsToRetain.contains(referencedTemplate.getId())) {
        referencedTemplatesToRetain.add(referencedTemplate);
      }
    }
    configDetails.setReferencedTemplates(referencedTemplatesToRetain);
  }


  /**
   * update referenced Elements according to mergedEditablePropertyIds and also update relationship add, remove and visiblity  
   * 
   * @param configDetails
   * @param visibleRelationshipIds
   * @param mergedEditablePropertyIds
   * @param canAddRelationshipIds
   * @param canDeleteRelationshipIds
   * @param referencedElements
   */
  private void manageReferencedElementsAndRelationshipForPermissions(IGetConfigDetailsModel configDetails,
      Set<String> visibleRelationshipIds, Set<String> mergedEditablePropertyIds,
      Set<String> canAddRelationshipIds, Set<String> canDeleteRelationshipIds)
  {
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Set<String> referenecedPropertyIds = new HashSet<String>(referencedElements.keySet());
    
    for (String elementId : referenecedPropertyIds) {
      IReferencedSectionElementModel referencedElement = referencedElements.get(elementId);
      //For relationship
      if(referencedElement instanceof ReferencedSectionRelationshipModel){
        String propertyId = referencedElement.getPropertyId();
        if(!visibleRelationshipIds.contains(propertyId)){
          if(configDetails instanceof GetConfigDetailsForCustomTabModel){
            Map<String,IReferencedRelationshipModel> referencedRelationship = ((IGetConfigDetailsForCustomTabModel)configDetails).getReferencedRelationships();
            referencedRelationship.remove(propertyId);
            Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationship = ((IGetConfigDetailsForCustomTabModel)configDetails).getReferencedNatureRelationships();
            referencedNatureRelationship.remove(propertyId);
            Map<String, String> referencedRelationshipMapping = ((IGetConfigDetailsForCustomTabModel)configDetails).getReferencedRelationshipsMapping();
            referencedRelationshipMapping.remove(propertyId);
          }
          referencedElements.remove(elementId);
          continue;
        }
        if(!canAddRelationshipIds.contains(propertyId)){
          ((IReferencedSectionRelationshipModel) referencedElement).setCanAdd(false);
        }
        if(!canDeleteRelationshipIds.contains(propertyId)){
          ((IReferencedSectionRelationshipModel) referencedElement).setCanDelete(false);
        }
        continue;
      }
      
      //For attributes, tags and roles
      if (mergedEditablePropertyIds.contains(elementId)) {
        referencedElement.setIsDisabled(false);
      }
      else {
        referencedElement.setIsDisabled(true);
      }
    }
  }
  
  public IGetGlobalPermissionForRuntimeEntitiesResponseModel getGlobalPermissionsForRuntimeEntities(
      String loginUserId, List<String> taxonomyIds, List<String> klassIds) throws Exception
  {
    IGetGlobalPermissionModel model = new GetGlobalPermissionModel();
    model.setLoginUserId(loginUserId);
    model.setTaxonomyIds(taxonomyIds);
    model.setKlassIds(klassIds);
    return getGlobalPermissionForRuntimeEntitiesStrategy.execute(model);
  }

  /**
   * manage Permissions using referencedPermission (Orient)
   *
   * @param configDetails
   * @throws Exception
   */
  public void manageVariantInstancePermissions(IBaseKlassTemplateConfigDetails configDetails)
  {
    IBaseKlassTemplatePermissionModel permission = configDetails.getReferencedPermissions();

    Set<String> visiblePCIds = permission.getVisiblePropertyCollectionIds();
    Set<String> visiblePropertyIds = permission.getVisiblePropertyIds();

    Set<String> editablePCIds = permission.getEditablePropertyCollectionIds();
    Set<String> editablePropertyIds = permission.getEditablePropertyIds(); //contains all editable property id's for current user
    Set<String> mergedEditablePropertyIds = new HashSet<>();               //contains editable property id's only if its containing property collection is also editable
    Set<String> mergedVisiblePropertyIds = new HashSet<>();               //contains visible property id's only if its containing property collection is also visible

    Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollections = configDetails.getReferencedPropertyCollections();
    Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollectionsClone = new HashMap<>(referencedPropertyCollections);
    Set<String> referencedElementsToRemoved = new HashSet<>();

    for (Map.Entry<String, IReferencedPropertyCollectionModel> entry : referencedPropertyCollectionsClone.entrySet()) {
      String pCId = entry.getKey();

      if (!visiblePCIds.contains(pCId)) {
        referencedPropertyCollections.remove(pCId);
        for (IReferencedPropertyCollectionElementModel element : entry.getValue()
            .getElements()) {
          String elementId = element.getId();
          visiblePropertyIds.remove(elementId);
          referencedElementsToRemoved.add(elementId);
        }
        continue;
      }


      /*if (!editablePCIds.contains(pCId)) {
        continue;
      }*/
      // add editable property id's to mergedEditablePropertyIds only if its
      // containing property collection is also editable
      IReferencedPropertyCollectionModel referencedPropertyCollection = entry.getValue();
      for (IReferencedPropertyCollectionElementModel propertyCollectionElement : referencedPropertyCollection
          .getElements()) {
        String propertyId = propertyCollectionElement.getId();
        if (editablePropertyIds.contains(propertyId) && editablePCIds.contains(pCId)) {
          mergedEditablePropertyIds.add(propertyId);
        }

        if (visiblePropertyIds.contains(propertyId)) {
          mergedVisiblePropertyIds.add(propertyId);
        }
      }
    }

    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    for(IReferencedSectionElementModel element:referencedElements.values())
    {
      String id = element.getId();
      if(visiblePropertyIds.contains(id))
      {
        element.setCanRead(true);
      }
      else {
        referencedElementsToRemoved.add(id);
      }

      if (mergedEditablePropertyIds.contains(id)) {
        element.setIsDisabled(false);
      }
      else {
        element.setIsDisabled(true);
      }
    }
    
    Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements = null;
    
    if(configDetails instanceof IConfigDetailsForGetVariantInstancesInTableViewModel) {
      instanceIdVsReferencedElements = ((IConfigDetailsForGetVariantInstancesInTableViewModel) configDetails)
          .getInstanceIdVsReferencedElements();
    }
    
    if(instanceIdVsReferencedElements == null) {
      for (String referencedElementId : referencedElementsToRemoved) {
        referencedElements.remove(referencedElementId);
      }
    }
    else {
      for (String referencedElementId : referencedElementsToRemoved) {
        referencedElements.remove(referencedElementId);
        for(Map<String, IReferencedSectionElementModel> map : instanceIdVsReferencedElements.values()) {
          map.remove(referencedElementId);
        }
      }
    }
   
    configDetails.setVisiblePropertyIds(mergedVisiblePropertyIds);
  }

  /**
   * Manage contexts permissions
   *
   * @author Santosh
   *
   * @param configDetails
   *
   */
  public void manageContextsPermissions(IGetConfigDetailsModel configDetails, Map<String, IReferencedVariantContextModel> embeddedVariantContexts)
  {
    List<String> contextIdsToRemove = new ArrayList<>();
    IReferencedTemplatePermissionModel referencedPermissions = configDetails.getReferencedPermissions();
    Set<String> klassIdsHavingReadPermissions = referencedPermissions.getKlassIdsHavingRP();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Set<String> referencedElementIds = referencedElements.keySet();
    for (String contextId : embeddedVariantContexts.keySet()) {
      IReferencedVariantContextModel embeddedVariantContext = embeddedVariantContexts.get(contextId);
      switch (embeddedVariantContext.getType()) {
        case CommonConstants.CONTEXTUAL_VARIANT:
        case CommonConstants.LANGUAGE_VARIANT:
        case CommonConstants.GTIN_VARIANT:
        case CommonConstants.IMAGE_VARIANT:
          String contextKlassId = embeddedVariantContext.getContextKlassId();
          if (contextKlassId != null && !klassIdsHavingReadPermissions.contains(contextKlassId) && !klassIdsHavingReadPermissions.isEmpty()) {
            contextIdsToRemove.add(contextId);
          }
          break;
        case CommonConstants.ATTRIBUTE_VARIANT_CONTEXT:
          List<String> entityIds = embeddedVariantContext.getEntityIds();
          List<String> referencedElementsClone = new ArrayList<>(referencedElementIds);
          referencedElementsClone.retainAll(entityIds);
          if (referencedElementsClone.isEmpty()) {
            contextIdsToRemove.add(contextId);
          }
          break;
      }
    }
    for (String contextId : contextIdsToRemove) {
      embeddedVariantContexts.remove(contextId);
    }

  }

  public void manageGridPermission(IGridInstanceConfigDetailsModel instanceConfigDetails,
      IGridContentInstanceModel responseInstance)
  {
    Map<String, IReferencedSectionElementModel> referencedElements = instanceConfigDetails.getReferencedElements();
    IGridInstanceReferencedPermission referencedPermissions = instanceConfigDetails.getReferencedPermissions();
    
    Set<String> editablePropertyIds = referencedPermissions.getEditablePropertyIds();
    Set<String> visiblePropertyIds = referencedPermissions.getVisiblePropertyIds();
    
    referencedElements.keySet().retainAll(visiblePropertyIds);
    
    List<String> nonEditablePropertyIds = new ArrayList<String>(referencedElements.keySet());
    nonEditablePropertyIds.removeAll(editablePropertyIds);
    
    for (String propertyId : nonEditablePropertyIds) {
      IReferencedSectionElementModel element = referencedElements.get(propertyId);
      element.setIsDisabled(true);
    }
    
    responseInstance.setIsNameEditable(referencedPermissions.getIsNameEditable());
    responseInstance.setIsNameVisible(referencedPermissions.getIsNameVisible());
  }
  
  public void retainDefaultTypesAccordingToPermission(IGetKlassInstanceTreeModel returnModel,
      Set<String> klassIdsHavingCP)
  {
    IGetDefaultKlassesModel defaultTypes = returnModel.getDefaultTypes();
    List<IKlassInformationModel> childrenToRemove = new ArrayList<>();
    List<IKlassInformationModel> children = defaultTypes.getChildren();
    for (IKlassInformationModel child : children) {
      if (!klassIdsHavingCP.contains(child.getId())) {
        childrenToRemove.add(child);
      }
    }
    children.removeAll(childrenToRemove);
  }
  
  public IGlobalPermission getGlobalPermissionForEntity(String taskId, String type, String moduleEntityType) throws Exception
  {
    String loginUserId = context.getUserId();
    IGetGlobalPermissionForSingleEntityModel getGlobalPermissionModel = new GetGlobalPermissionForSingleEntityModel();
    getGlobalPermissionModel.setEntityId(taskId);
    getGlobalPermissionModel.setType(type);
    getGlobalPermissionModel.setUserId(loginUserId);
    
    IGlobalPermissionWithAllowedModuleEntitiesModel response = getGlobalPermissionForSingleEntityStrategy.execute(getGlobalPermissionModel);

    if(moduleEntityType!= null && !response.getAllowedEntities().contains(moduleEntityType)) {
      return null;
    }

    return response.getGlobalPermission();

  }

  public List<String> getRoleIdsWithCurrentUser() throws Exception
  {
    {  String currentUserId = context.getUserId();
      IIdParameterModel model = new IdParameterModel();
      model.setId(currentUserId);
      IIdsListParameterModel roleModel = getRoleByUserStrategy.execute(model);
    return roleModel.getIds();
  }
  }
}
