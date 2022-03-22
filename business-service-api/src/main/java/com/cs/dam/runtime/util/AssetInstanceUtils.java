package com.cs.dam.runtime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassPermission;
import com.cs.core.config.interactor.entity.klass.IKlassRoleSetting;
import com.cs.core.config.interactor.entity.klass.IKlassRoleViewSetting;
import com.cs.core.config.interactor.entity.klass.IKlassStructureEditorSetting;
import com.cs.core.config.interactor.entity.klass.IKlassStructureTreeSetting;
import com.cs.core.config.interactor.entity.klass.IKlassViewSetting;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.klass.KlassRoleSetting;
import com.cs.core.config.interactor.entity.klass.KlassViewSetting;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionPermission;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRole;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.propertycollection.Section;
import com.cs.core.config.interactor.entity.propertycollection.SectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.SectionRole;
import com.cs.core.config.interactor.entity.propertycollection.SectionTag;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.IRolePermission;
import com.cs.core.config.interactor.entity.standard.attribute.ImageCoverflowAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.NameAttribute;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.klass.IProjectKlassModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetStrategy;
import com.cs.core.config.strategy.usecase.user.IGetUserStrategy;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ImageAttributeInstance;
import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class AssetInstanceUtils {
  
  public static final String YES_NEUTRAL_TAG_TYPE    = "tag_type_yes_neutral";
  public static final String YES_NEUTRAL_NO_TAG_TYPE = "tag_type_yes_neutral_no";
  public static final String RANGE_TAG_TYPE          = "tag_type_range";
  public static final String CUSTOM_TAG_TYPE         = "tag_type_custom";
  @Autowired
  IGetAssetStrategy          neo4jGetAssetStrategy;
  @Autowired
  ISessionContext            context;
  @Autowired
  IGetUserStrategy           getUserStrategy;
  
  @Resource(name = "languageContentLabelMap")
  Map<String, String>        languageContentLabelMap;
  
  /**
   * Description: The method sets flat level properties for image attribute
   * instance. (lastModifiedBy, createdBy, createdOn, lastModifiedBy,
   * versionTimestamp, versionId)
   *
   * @param imageAttributeInstance
   *          - content attribute instance
   * @param userId
   *          - current user Id
   */
  public static void setFlatPropertyForImageAttributeInstance(
      IContentAttributeInstance imageAttributeInstance, String userId)
  {
    imageAttributeInstance.setLastModifiedBy(userId);
    imageAttributeInstance.setCreatedBy(userId);
    
    Long currentTimeMillis = System.currentTimeMillis();
    imageAttributeInstance.setCreatedOn(currentTimeMillis);
    imageAttributeInstance.setLastModified(currentTimeMillis);
    imageAttributeInstance.setVersionTimestamp(currentTimeMillis);
    imageAttributeInstance.setVersionId(0L);
  }
  
  /**
   * Description: The method returns the intersection of the two lists.
   *
   * @param list1
   * @param list2
   * @return intersection of list1 and list2
   */
  public static List<String> intersection(final List<String> list1, final List<String> list2)
  {
    final ArrayList<String> result = new ArrayList<String>();
    final Iterator<String> iterator = list2.iterator();
    while (iterator.hasNext()) {
      final Object o = iterator.next();
      
      if (list1.contains(o)) {
        result.add((String) o);
      }
    }
    
    return result;
  }
  
  /**
   * This method fills event schedule and asset file information in passed IGetKlassInstanceModel model.
   * 
   * @param executeGetKlassInstance
   * @param baseEntityDAO
   * @throws Exception
   */
  public static void fillAssetInformation(IGetKlassInstanceModel executeGetKlassInstance,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IAssetInstance assetInstance = (IAssetInstance) executeGetKlassInstance.getKlassInstance();
    IEventInstanceSchedule eventSchedule = assetInstance.getEventSchedule() == null
        ? new EventInstanceSchedule() : assetInstance.getEventSchedule();
    
    ConfigurationDAO configDAO = ConfigurationDAO.instance();
    List<IPropertyDTO> propertyDTO = new ArrayList<>();
    propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.START_DATE_ATTRIBUTE));
    propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.END_DATE_ATTRIBUTE));
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords(propertyDTO.toArray(new IPropertyDTO[0]));
    for (IPropertyRecordDTO iPropertyDTO : baseEntityDTO.getPropertyRecords()) {
      IPropertyDTO property = iPropertyDTO.getProperty();
      IValueRecordDTO value = (IValueRecordDTO) iPropertyDTO;
      if ((property.getCode()).equals(SystemLevelIds.START_DATE_ATTRIBUTE)) {
        eventSchedule.setStartTime((long) value.getAsNumber());
      }
      else {
        eventSchedule.setEndTime((long) value.getAsNumber());
      }
    }
    assetInstance.setEventSchedule(eventSchedule);
    fillEntityExtensionInAssetCoverFlowAttribute(assetInstance, baseEntityDTO, baseEntityDTO.getEntityExtension());
  }
  
  public static void fillEntityExtensionInAssetCoverFlowAttribute(
      IAssetInstance assetInstance, IBaseEntityDTO baseEntityDTO,
      IJSONContent entityExtension) throws Exception
  {
    assetInstance.setAssetInformation(getAssetInformationModel(baseEntityDTO, entityExtension));
  }

  public static IAssetInformationModel getAssetInformationModel(IBaseEntityDTO baseEntityDTO,
      IJSONContent entityExtension) throws Exception
  {
    entityExtension.deleteField(IEventInstanceSchedule.START_TIME);
    entityExtension.deleteField(IEventInstanceSchedule.END_TIME);
    entityExtension.deleteField(IPropertyInstance.BASE_TYPE);
    String entityExtensionAsString = entityExtension.toString();
    IAssetInformationModel assetInformationModel = (IAssetInformationModel) ObjectMapperUtil.readValue(entityExtensionAsString,
        AssetInformationModel.class);
    assetInformationModel.setHash(baseEntityDTO.getHashCode());
    return assetInformationModel;
  }
  
  public static IImageAttributeInstance fillImageAttributeInstance(IBaseEntityDTO baseEntityDTO)
      throws Exception
  {
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    if (entityExtension.isEmpty()) {
      return null;
    }
    entityExtension.setField(IImageAttributeInstance.BASE_TYPE,
        Constants.IMAGE_ATTRIBUTE_INSTANCE_TYPE);
    String string = entityExtension.toString();
    IImageAttributeInstance imageAttributeInstance = ObjectMapperUtil.readValue(string,
        ImageAttributeInstance.class);
    imageAttributeInstance
        .setAttributeId(IStandardConfig.StandardProperty.assetcoverflowattribute.toString());
    imageAttributeInstance.setId(IStandardConfig.StandardProperty.assetcoverflowattribute.toString());
    imageAttributeInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    imageAttributeInstance.setIsDefault(true);
    imageAttributeInstance.setHash(baseEntityDTO.getHashCode());
    return imageAttributeInstance;
  }
  
  public static void fillEntityExtension(IBaseEntityDAO baseEntityDAO,
      IPropertyInstance modifiedContentAttributeInstance)
      throws JsonProcessingException, RDBMSException
  {
    String assetObjectKey = ((IImageAttributeInstance) modifiedContentAttributeInstance)
        .getAssetObjectKey();
    String fileName = ((IImageAttributeInstance) modifiedContentAttributeInstance).getFileName();
    String hash = ((IImageAttributeInstance) modifiedContentAttributeInstance).getHash();
    HashMap<String, String> properties = ((IImageAttributeInstance) modifiedContentAttributeInstance)
        .getProperties();
    String thumbKey = ((IImageAttributeInstance) modifiedContentAttributeInstance).getThumbKey();
    String type = ((IImageAttributeInstance) modifiedContentAttributeInstance).getType();
    
    Map<String, Object> imageAttr = new HashMap<>();
    imageAttr.put(IImageAttributeInstance.ASSET_OBJECT_KEY, assetObjectKey);
    imageAttr.put(IImageAttributeInstance.FILENAME, fileName);
    imageAttr.put(IImageAttributeInstance.PROPERTIES, properties);
    imageAttr.put(IImageAttributeInstance.THUMB_KEY, thumbKey);
    imageAttr.put(IImageAttributeInstance.TYPE, type);
    imageAttr.put(IImageAttributeInstance.PREVIEW_IMAGE_KEY,
        ((IImageAttributeInstance) modifiedContentAttributeInstance).getPreviewImageKey());
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    try {
      baseEntityDTO.setHashCode(hash);
      baseEntityDTO.setEntityExtension(ObjectMapperUtil.writeValueAsString(imageAttr));
      baseEntityDAO.updatePropertyRecords(new IPropertyRecordDTO[] {});
    }
    catch (CSFormatException e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  public void manageKlassDetailsAccordingToPermissions(String userId,
      IArticleInstanceModel klassInstanceModel, IProjectKlassModel klassModel)
  {
    // get roles for user
    List<? extends IRoleInstance> roleInstances = klassInstanceModel.getRoles();
    List<String> roleIds = getRoleIdsForUserFromKlassInstances(userId, roleInstances);
    
    // role id will not exist in klassInstance if it is deleted from config
    // klass
    roleIds = getRoleIdsExistInKlassPermission(roleIds, klassModel);
    
    manageSectionsOfKlassAccordingToUserRole(klassModel, roleIds);
    
    IKlassViewSetting klassViewSettings = (KlassViewSetting) klassModel.getKlassViewSetting();
    // List<KlassViewSetting> limitedKlassViewSettings = new ArrayList<>();
    
    /*
     * Map<String, IRolePermission> rolePermissions =
     * klassModel.getPermission().getRolePermission(); for (String roleId :
     * roleIds) { IRolePermission rolePermission = rolePermissions.get(roleId);
     * List<String> visibleViewTypes =
     * rolePermission.getVisibleStructureViews(); for (KlassViewSetting
     * klassViewSetting : allKlassViewSettings) { if
     * (visibleViewTypes.contains(klassViewSetting.getId())) {
     * limitedKlassViewSettings.add(klassViewSetting); } } }
     */
    klassModel.setKlassViewSetting(klassViewSettings);
  }
  
  /**
   * Description: The method gets the role ids of the given klass instance if
   * the user exists in a role.
   *
   * @param userId
   * @param roleInstances
   * @return list of roleIds
   */
  private List<String> getRoleIdsForUserFromKlassInstances(String userId,
      List<? extends IRoleInstance> roleInstances)
  {
    List<String> roleIds = new ArrayList<String>();
    for (IRoleInstance roleInstance : roleInstances) {
      List<? extends IRoleCandidate> roleCandidates = roleInstance.getCandidates();
      Boolean isUserExist = checkUserExistInARole(userId, roleCandidates);
      if (isUserExist) {
        roleIds.add(roleInstance.getRoleId());
      }
    }
    
    return roleIds;
  }
  
  private List<String> getRoleIdsExistInKlassPermission(List<String> roleIds, IKlass klassModel)
  {
    List<String> roleIdsClone = new ArrayList<String>();
    roleIdsClone.addAll(roleIds);
    for (String roleId : roleIds) {
      IKlassPermission klassPermission = klassModel.getPermission();
      Map<String, IRolePermission> rolePermissionMap = klassPermission.getRolePermission();
      IRolePermission rolePermission = rolePermissionMap.get(roleId);
      // case: when role is deleted from klass
      if (rolePermission == null) {
        roleIdsClone.remove(roleId);
      }
    }
    return roleIdsClone;
  }
  
  private void manageSectionsOfKlassAccordingToUserRole(IKlassModel klassModel,
      List<String> roleIds)
  {
    List<ISection> sections = null;
    if (roleIds.size() == 0) {
      sections = new ArrayList<ISection>();
    }
    else if (roleIds.size() == 1) {
      sections = getSectionsForSingleRoleUser(klassModel, roleIds.get(0));
    }
    else {
      sections = getSectionsForMultiRoleUser(klassModel, roleIds);
    }
    klassModel.setSections(sections);
  }
  
  private List<ISection> getSectionsForSingleRoleUser(IKlassModel klassModel, String roleId)
  {
    List<ISection> sections = new ArrayList<ISection>();
    sections.addAll(klassModel.getSections());
    
    IKlassPermission klassPermission = klassModel.getPermission();
    Map<String, IRolePermission> rolePermissionMap = klassPermission.getRolePermission();
    IRolePermission rolePermission = rolePermissionMap.get(roleId);
    Map<String, ISectionPermission> sectionPermissionMap = rolePermission.getSectionPermission();
    for (Map.Entry<String, ISectionPermission> sectionPermissionEntry : sectionPermissionMap
        .entrySet()) {
      String sectionId = sectionPermissionEntry.getKey();
      ISectionPermission sectionPermission = sectionPermissionEntry.getValue();
      manageSectionPermission(sections, sectionId, sectionPermission);
    }
    
    return sections;
  }
  
  private List<ISection> getSectionsForMultiRoleUser(IKlassModel klassModel, List<String> roleIds)
  {
    List<ISection> filteredSections = new ArrayList<ISection>();
    filteredSections.addAll(klassModel.getSections());
    IKlassPermission klassPermission = klassModel.getPermission();
    
    List<ISection> sectionToBeRemoved = new ArrayList<ISection>();
    for (int iSectionCount = 0; iSectionCount < klassModel.getSections()
        .size(); iSectionCount++) {
      ISection sectionFromKlass = klassModel.getSections()
          .get(iSectionCount);
      Map<String, Object> sectionPermissionMap = getSectionPermission(klassPermission,
          sectionFromKlass.getId(), roleIds);
      ISection section = filteredSections.get(iSectionCount);
      if ((Boolean) sectionPermissionMap.get("isHidden")) {
        sectionToBeRemoved.add(section);
        // filteredSections.remove(section);
      }
      else {
        section.setIsCollapsed((Boolean) sectionPermissionMap.get("isCollapsed"));
        List<String> disabledElements = (List<String>) sectionPermissionMap.get("disabledElements");
        for (ISectionElement sectionElement : section.getElements()) {
          // set sections elements isDesabled true if it is there in
          // disabledElements
          if (disabledElements.contains(sectionElement.getId())) {
            sectionElement.setIsDisabled(true);
          }
        }
      }
    }
    
    filteredSections.removeAll(sectionToBeRemoved);
    
    return filteredSections;
  }
  
  private Boolean checkUserExistInARole(String userId,
      List<? extends IRoleCandidate> roleCandidates)
  {
    for (IRoleCandidate roleCandidate : roleCandidates) {
      if (roleCandidate instanceof User) {
        if (roleCandidate.getId()
            .equals(userId)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private void manageSectionPermission(List<ISection> sections, String sectionId,
      ISectionPermission sectionPermission)
  {
    ISection tempSection = new Section();
    tempSection.setId(sectionId);
    int sectionIndex = sections.indexOf(tempSection);
    if (sectionPermission.getIsHidden()) {
      sections.remove(sectionIndex);
    }
    else {
      ISection section = sections.get(sectionIndex);
      section.setIsCollapsed(sectionPermission.getIsCollapsed());
      List<String> disabledElements = sectionPermission.getDisabledElements();
      for (ISectionElement sectionElement : section.getElements()) {
        if (disabledElements.contains(sectionElement.getId())) {
          sectionElement.setIsDisabled(true);
        }
      }
    }
  }
  
  private Map<String, Object> getSectionPermission(IKlassPermission klassPermission,
      String sectionId, List<String> roleIds)
  {
    Map<String, Object> sectionPermissionMap = new HashMap<String, Object>();
    sectionPermissionMap.put("isHidden", true);
    sectionPermissionMap.put("isCollapsed", true);
    for (Map.Entry<String, IRolePermission> entry : klassPermission.getRolePermission()
        .entrySet()) {
      String roleId = entry.getKey();
      IRolePermission rolePermission = entry.getValue();
      if (!roleIds.contains(roleId)) {
        continue;
      }
      
      ISectionPermission sectionPermission = rolePermission.getSectionPermission()
          .get(sectionId);
      Boolean isHidden = sectionPermission.getIsHidden();
      if (!isHidden) {
        sectionPermissionMap.put("isHidden", false);
        if (!sectionPermission.getIsCollapsed()) {
          sectionPermissionMap.put("isCollapsed", false);
        }
        List<String> disabledElements = (List<String>) sectionPermissionMap.get("disabledElements");
        if (disabledElements != null) {
          disabledElements.retainAll(sectionPermission.getDisabledElements());
        }
        else {
          sectionPermissionMap.put("disabledElements", sectionPermission.getDisabledElements());
        }
      }
    }
    return sectionPermissionMap;
  }
  
  public IKlassRoleSetting mergeRoleSettings(String userId,
      IArticleInstanceModel klassInstanceModel, IProjectKlass klassModel, boolean isUserOwner)
      throws Exception
  {
    IKlassRoleSetting roleSetting = null;
    Map<String, IKlassRoleSetting> roleViewSettingsMap = klassModel.getKlassViewSetting()
        .getRoles();
    if (isUserOwner) {
      roleSetting = ObjectMapperUtil.readValue(
          ObjectMapperUtil.writeValueAsString(roleViewSettingsMap.get(SystemLevelIds.OWNER_ROLE)),
          KlassRoleSetting.class);
    }
    
    List<? extends IRoleInstance> roleInstances = klassInstanceModel.getRoles();
    List<String> roleIds = getRoleIdsForUserFromKlassInstances(userId, roleInstances);
    roleIds = getRoleIdsExistInKlassPermission(roleIds, klassModel);
    
    for (String roleId : roleIds) {
      IKlassRoleSetting roleSettingsById = roleViewSettingsMap.get(roleId);
      if (roleSetting == null) {
        roleSetting = ObjectMapperUtil.readValue(
            ObjectMapperUtil.writeValueAsString(roleSettingsById), KlassRoleSetting.class);
        continue;
      }
      
      Map<String, IKlassRoleViewSetting> viewSettingsMap = roleSetting.getViews();
      Map<String, IKlassRoleViewSetting> viewSettingsMapToCompare = roleSettingsById.getViews();
      
      for (Entry<String, IKlassRoleViewSetting> viewSettingEntry : viewSettingsMapToCompare
          .entrySet()) {
        if (viewSettingsMap.get(viewSettingEntry.getKey()) == null) {
          viewSettingsMap.put(viewSettingEntry.getKey(), viewSettingEntry.getValue());
        }
      }
      
      mergeStructureSettings(roleSettingsById, roleSetting);
      
      for (Entry<String, IKlassRoleViewSetting> viewSetting : viewSettingsMap.entrySet()) {
        /*
         * if (!viewSetting.getValue().getIsInstructionVisible() &&
         * viewSettingsMapToCompare
         * .get(viewSetting.getKey()).getIsInstructionVisible()) {
         * viewSetting.getValue().setIsInstructionVisible(true); }
         *
         * if (!viewSetting.getValue().getIsCommentVisible() &&
         * viewSettingsMapToCompare
         * .get(viewSetting.getKey()).getIsCommentVisible()) {
         * viewSetting.getValue().setIsCommentVisible(true); }
         */
        
        // mergeStructureViewSettings(viewSettingsMapToCompare, viewSetting);
      }
    }
    return roleSetting;
  }
  
  private void mergeStructureSettings(IKlassRoleSetting roleSettingsById,
      IKlassRoleSetting roleSetting)
  {
    Map<String, IKlassRoleSetting> structureSettingsMapToCompare = roleSettingsById.getStructures();
    Map<String, IKlassRoleSetting> structureSettingsMap = roleSetting.getStructures();
    for (Entry<String, IKlassRoleSetting> structureViewSettingEntry : structureSettingsMap
        .entrySet()) {
      IKlassRoleSetting structureViewSetting = structureViewSettingEntry.getValue();
      IKlassRoleSetting structureViewSettingToCampare = structureSettingsMapToCompare
          .get(structureViewSettingEntry.getKey());
      IKlassStructureTreeSetting treeSettings = structureViewSetting.getTree();
      IKlassStructureTreeSetting treeSettingsToCampare = structureViewSettingToCampare.getTree();
      IKlassStructureEditorSetting editorSettings = structureViewSetting.getEditor();
      IKlassStructureEditorSetting editorSettingsToCampare = structureViewSettingToCampare
          .getEditor();
      
      if (treeSettingsToCampare.getCanAddChildren() != null
          && treeSettingsToCampare.getCanAddChildren()) {
        treeSettings.setCanAddChildren(true);
      }
      
      if (treeSettingsToCampare.getCanAddSiblings() != null
          && treeSettingsToCampare.getCanAddSiblings()) {
        treeSettings.setCanAddSiblings(true);
      }
      
      if (treeSettingsToCampare.getCanDelete() != null && treeSettingsToCampare.getCanDelete()) {
        treeSettings.setCanDelete(true);
      }
      
      if (treeSettingsToCampare.getCanMove() != null && treeSettingsToCampare.getCanMove()) {
        treeSettings.setCanMove(true);
      }
      
      if (treeSettingsToCampare.getIsHeaderEditable() != null
          && treeSettingsToCampare.getIsHeaderEditable()) {
        treeSettings.setIsHeaderEditable(true);
      }
      
      if (editorSettingsToCampare.getCanAddChildren() != null
          && editorSettingsToCampare.getCanAddChildren()) {
        editorSettings.setCanAddChildren(true);
      }
      
      if (editorSettingsToCampare.getCanAddSiblings() != null
          && editorSettingsToCampare.getCanAddSiblings()) {
        editorSettings.setCanAddSiblings(true);
      }
      
      if (editorSettingsToCampare.getCanDelete() != null
          && editorSettingsToCampare.getCanDelete()) {
        editorSettings.setCanDelete(true);
      }
      
      if (editorSettingsToCampare.getCanMove() != null && editorSettingsToCampare.getCanMove()) {
        editorSettings.setCanMove(true);
      }
      
      if (editorSettingsToCampare.getIsHeaderEditable() != null
          && editorSettingsToCampare.getIsHeaderEditable()) {
        editorSettings.setIsHeaderEditable(true);
      }
      
      if (editorSettingsToCampare.getIsHeaderVisible() != null
          && editorSettingsToCampare.getIsHeaderVisible()) {
        editorSettings.setIsHeaderVisible(true);
      }
    }
  }
  
  public void addStructureSettingsToKlassViewSettings(IProjectKlass klassModel)
  {
    IKlassViewSetting klassViewSettingsMap = klassModel.getKlassViewSetting();
    Map<String, IKlassRoleSetting> klassRoleSettingMap = klassViewSettingsMap.getRoles();
    
    recursivelyAddStructureSettings(klassModel, klassRoleSettingMap);
  }
  
  private void recursivelyAddStructureSettings(IStructure parentStructure,
      Map<String, IKlassRoleSetting> klassRoleSettingMap)
  {
    for (IStructure structure : parentStructure.getStructureChildren()) {
      Map<String, IKlassRoleSetting> structureRoleSettingMapping = structure.getViewSettings()
          .getRoles();
      for (Entry<String, IKlassRoleSetting> structureRoleSettingEntry : structureRoleSettingMapping
          .entrySet()) {
        IKlassRoleSetting klassRoleSetting = klassRoleSettingMap
            .get(structureRoleSettingEntry.getKey());
        if (klassRoleSetting == null) {
          klassRoleSetting = new KlassRoleSetting();
          klassRoleSettingMap.put(structureRoleSettingEntry.getKey(), klassRoleSetting);
        }
        Map<String, IKlassRoleSetting> structureRoleSettingMap = klassRoleSetting.getStructures();
        if (structureRoleSettingMap == null) {
          structureRoleSettingMap = new HashMap<>();
          klassRoleSetting.setStructures(structureRoleSettingMap);
        }
        structureRoleSettingMap.put(structure.getId(), structureRoleSettingEntry.getValue());
      }
      recursivelyAddStructureSettings(structure, klassRoleSettingMap);
    }
  }
  
  public boolean isUserAssignedInKlassInstance(String userId, IAssetInstance klassInstance)
  {
    boolean isUserAssignedToKlassInstance = false;
    for (IRoleInstance roleInstance : klassInstance.getRoles()) {
      for (IRoleCandidate roleCandidate : roleInstance.getCandidates()) {
        if (roleCandidate.getId()
            .equals(userId)) {
          isUserAssignedToKlassInstance = true;
          break;
        }
      }
    }
    return isUserAssignedToKlassInstance;
  }
  
  public IAssetModel getTypeKlassFromStandardKlass(String id) throws Exception
  {
    IAssetModel assetKlassModel = null;
    IIdParameterModel idParameterModel = new IdParameterModel(id);
    IAssetModel standardKlassModel = neo4jGetAssetStrategy.execute(idParameterModel);
    IKlass standardKlass = (IKlass) standardKlassModel.getEntity();
    
    String defaultTypeValue = "";
    if (standardKlass != null) {
      List<String> attributeIdsToFetch = new ArrayList<>();
    }
    if (!defaultTypeValue.isEmpty()) {
      IIdParameterModel typeKlassIdModel = new IdParameterModel(defaultTypeValue);
      IAssetModel getKlassModel = neo4jGetAssetStrategy.execute(typeKlassIdModel);
      if (getKlassModel.getEntity() != null) {
        assetKlassModel = getKlassModel;
      }
    }
    else {
      assetKlassModel = standardKlassModel;
    }
    
    return assetKlassModel;
  }
  
  public List<ISectionAttribute> getSectionAttributes(IKlass klass, List<String> attributeIds)
  {
    List<ISectionAttribute> sectionAttributes = new ArrayList<>();
    for (ISection section : klass.getSections()) {
      for (ISectionElement sectionElement : section.getElements()) {
        if (sectionElement instanceof ISectionAttribute) {
          IAttribute masterAttribute = ((ISectionAttribute) sectionElement).getAttribute();
          if (attributeIds.contains(masterAttribute.getId())) {
            sectionAttributes.add((ISectionAttribute) sectionElement);
            attributeIds.remove(masterAttribute.getId());
            if (attributeIds.size() == 0) {
              return sectionAttributes;
            }
          }
        }
      }
    }
    return sectionAttributes;
  }
  
  /**
   * Description: The method returns the name of default instance in the given
   * language.
   *
   * @param typeKlass
   * @param lang
   *          - language in which the name is required.
   * @return name of default instance if exists otherwise "Neues".
   */
  public String getDefaultInstanceName(IKlass typeKlass, String lang)
  {
    String newInstanceName = languageContentLabelMap.get(lang);
    if (newInstanceName == null) {
      newInstanceName = "Neues";
    }
    List<String> attributeIdsToFetch = new ArrayList<>();
    attributeIdsToFetch.add(IStandardConfig.StandardProperty.nameattribute.toString());
    List<ISectionAttribute> defaultSectionElements = getSectionAttributes(typeKlass,
        attributeIdsToFetch);
    for (ISectionAttribute sectionAttributeElement : defaultSectionElements) {
      if (sectionAttributeElement.getAttribute() instanceof NameAttribute) {
        String defaultNameValue = sectionAttributeElement.getDefaultValue();
        if (!defaultNameValue.isEmpty()) {
          newInstanceName = defaultNameValue;
        }
        break;
      }
    }
    return newInstanceName;
  }
  
  /**
   * Description: The method creates proeperty instances on the basis of their
   * section type (attribute, tag, role).
   *
   * @param typeKlass
   * @param attributesList-
   *          list of attributes
   * @param tagsList
   *          - list of tags
   * @param rolesList
   *          - list of roles
   * @param entityName
   *          - name of current entity
   * @throws Exception
   */
  public void createPropertyInstances(IKlass typeKlass,
      List<IContentAttributeInstance> attributesList, List<ITagInstance> tagsList,
      List<IRoleInstance> rolesList, String entityName) throws Exception
  {
    for (ISection section : typeKlass.getSections()) {
      for (ISectionElement sectionElement : section.getElements()) {
        if (sectionElement instanceof ISectionAttribute) {
          ISectionAttribute sectionAttribute = ((SectionAttribute) sectionElement);
          IAttribute masterAttribute = sectionAttribute.getAttribute();
          if (!(masterAttribute instanceof ImageCoverflowAttribute)) {
            if (!doesAttributeInstanceAlreadyExist(attributesList, masterAttribute.getId())) {
              String defaultValue = sectionAttribute.getDefaultValue();
              if (masterAttribute instanceof NameAttribute && entityName != null) {
                defaultValue = entityName;
                
              }
              else if (defaultValue == null) {
                defaultValue = masterAttribute.getDefaultValue();
              }
              
              IAttributeInstance newAttributeInstance = new AttributeInstance();
              newAttributeInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
              // newAttributeInstance.setMappingId(sectionAttribute.getId());
              newAttributeInstance.setAttributeId(masterAttribute.getId());
              newAttributeInstance.setValue(defaultValue != null ? defaultValue : "");
              attributesList.add(newAttributeInstance);
            }
          }
        }
        else if (sectionElement instanceof SectionTag) {
          ISectionTag sectionTag = ((SectionTag) sectionElement);
          ITag masterTag = sectionTag.getTag();
          if (!doesAttributeInstanceAlreadyExist(tagsList, masterTag.getId())) {
            ITagInstance tagInstance = new TagInstance();
            tagInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
            tagInstance.setTagId(masterTag.getId());
            // tagInstance.setMappingId(sectionTag.getId());
            tagsList.add(tagInstance);
            addTagsInContentBasedOnTagType(masterTag, tagInstance);
          }
        }
        else if (sectionElement instanceof SectionRole) {
          ISectionRole sectionRole = ((SectionRole) sectionElement);
          IRole masterRole = sectionRole.getRole();
          if (!doesAttributeInstanceAlreadyExist(rolesList, masterRole.getId())) {
            IRoleInstance roleInstance = new RoleInstance();
            roleInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ROLE.getPrefix()));
            roleInstance.setRoleId(masterRole.getId());
            // roleInstance.setMappingId(sectionRole.getId());
            if (masterRole.getId()
                .equals(SystemLevelIds.OWNER_ROLE)) {
              String userId = context.getUserId();
              IIdParameterModel idModel = new IdParameterModel(userId);
              IUserModel owner = getUserStrategy.execute(idModel);
              List<IRoleCandidate> candidates = new ArrayList<>();
              candidates.add((IRoleCandidate) owner.getEntity());
              roleInstance.setCandidates(candidates);
            }
            rolesList.add(roleInstance);
          }
        }
      }
    }
  }
  
  /**
   * Description: The method checks if propertyInstance already exists.
   *
   * @param propertyInstancesArray
   * @param propertyId
   * @return false if it does not exists otherwise true.
   */
  public boolean doesAttributeInstanceAlreadyExist(
      List<? extends IPropertyInstance> propertyInstancesArray, String propertyId)
  {
    boolean doesPropertyInstanceExist = false;
    for (IPropertyInstance propertyInstance : propertyInstancesArray) {
      if (propertyInstance instanceof ITagInstance) {
        doesPropertyInstanceExist = ((ITagInstance) propertyInstance).getTagId()
            .equals(propertyId);
      }
      else if (propertyInstance instanceof IContentAttributeInstance) {
        doesPropertyInstanceExist = ((IContentAttributeInstance) propertyInstance).getAttributeId()
            .equals(propertyId);
      }
      else if (propertyInstance instanceof IRoleInstance) {
        doesPropertyInstanceExist = ((IRoleInstance) propertyInstance).getRoleId()
            .equals(propertyId);
      }
      if (doesPropertyInstanceExist) {
        break;
      }
    }
    return doesPropertyInstanceExist;
  }
  
  /**
   * Description: The method add tags in content based on tag type. If the tag
   * type is tag_type_yes_neutral then, it adds in the tag instance otherwise in
   * the master tag.
   *
   * @param masterTag
   * @param tagInstance
   * @throws Exception 
   */
  public void addTagsInContentBasedOnTagType(ITag masterTag, ITagInstance tagInstance) throws Exception
  {
    String tagType = masterTag.getTagType();
    if (tagType.equals(YES_NEUTRAL_TAG_TYPE)) {
      ITag defaultTag = masterTag.getDefaultValue();
      if (defaultTag != null && defaultTag.getId() != null) {
        tagInstance.getTagValues()
            .add(getTagGroupValue(defaultTag, 100));
      }
    }
    else {
      addLeafTagsToTagValues((List<ITag>) masterTag.getChildren(), tagInstance);
    }
  }
  
  /**
   * Description: The method gets the tagvalueInstance and sets properties such
   * as id, relevance, tagId, timestamp to it.
   *
   * @param tagValue
   * @param relevance
   * @return
   * @throws Exception 
   * @throws  
   */
  public ITagInstanceValue getTagGroupValue(ITag tagValue, Integer relevance) throws Exception
  {
    ITagInstanceValue tagValueInstance = new TagInstanceValue();
    tagValueInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagValueInstance.setRelevance(relevance != null ? relevance : 0);
    tagValueInstance.setTagId(tagValue.getId());
    tagValueInstance.setTimestamp(Long.toString(System.currentTimeMillis()));
    return tagValueInstance;
  }
  
  /**
   * Description: The method adds leaf tags (children of tag) in the tag
   * instance if exists.
   *
   * @param tags
   *          - list of tags
   * @param tagInstance
   * @return list of leaf tags
   * @throws Exception 
   */
  public List<ITag> addLeafTagsToTagValues(List<ITag> tags, ITagInstance tagInstance) throws Exception
  {
    List<ITag> leafTags = new ArrayList<>();
    for (ITag tagValue : tags) {
      List<ITag> childTags = (List<ITag>) tagValue.getChildren();
      if (childTags.size() > 0) {
        addLeafTagsToTagValues(childTags, tagInstance);
      }
      else {
        tagInstance.getTagValues()
            .add(getTagGroupValue(tagValue, 0));
      }
    }
    return leafTags;
  }
  
  public String removeRestrictedFileNameChars(String str)
  {
    return str.replaceAll("[\\\\/:*?\"<>|]", "");
  }
}
