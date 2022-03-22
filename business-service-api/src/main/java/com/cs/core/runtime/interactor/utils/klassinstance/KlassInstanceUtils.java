package com.cs.core.runtime.interactor.utils.klassinstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.HTMLAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRole;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.propertycollection.SectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.SectionRole;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.standard.attribute.CreatedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.ImageCoverflowAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.NameAttribute;
import com.cs.core.config.interactor.entity.standard.role.OwnerRole;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.attribute.standard.CoverflowAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.DateAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ImageAttributeModel;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.GetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionForSwitchTypeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionTagModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.config.strategy.usecase.klass.IGetAllowedTypesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetDefaultKlassesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassWithReferencedKlassesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesStrategy;
import com.cs.core.config.strategy.usecase.target.IGetTargetStrategy;
import com.cs.core.config.strategy.usecase.user.IGetUserStrategy;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IProductDeleteDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.relationship.dto.RelationCoupleRecordDTO.RelationCoupleRecordDTOBuilder;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflictingValueSource;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassConflictingValueSource;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceStructure;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassConflictingValueSource;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceStructure;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ImageAttributeInstance;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflict;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflictingSource;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IResolveRelationshipConflictsRequestModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.RelationshipConflict;
import com.cs.core.runtime.interactor.entity.relationshipinstance.RelationshipConflictingSource;
import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.attribute.IAttributeDiffModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.ModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestStrategyModelForTasksTab;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedTagInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceDiffModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.languageinstance.LanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.DevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.searchable.IInstanceSearchStrategyModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceRequestModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceRequestModel;
import com.cs.core.runtime.interactor.model.tag.ITagDiffModel;
import com.cs.core.runtime.interactor.model.tag.ITagDiffValueModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.typeswitch.GetAllowedTypesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.variants.IVariantReferencedInstancesModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.interactor.usecase.klassinstance.KlassInstanceNotificationTask;
import com.cs.core.runtime.interactor.usecase.logger.CSLogUtil;
import com.cs.core.runtime.interactor.usecase.threadlocal.ApplicationThreadLocal;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;

/**
 * @author CS11
 */

/** @author CS11 */
@Component("klassInstanceUtils")
@SuppressWarnings("unchecked")
public class KlassInstanceUtils {
  
  public static final String                       YES_NEUTRAL_TAG_TYPE    = "tag_type_yes_neutral";
  
  public static final String                       YES_NEUTRAL_NO_TAG_TYPE = "tag_type_yes_neutral_no";
  
  public static final String                       RANGE_TAG_TYPE          = "tag_type_range";
  
  public static final String                       CUSTOM_TAG_TYPE         = "tag_type_custom";
  
  public static final List<String>                 mandatoryAttributes     = new ArrayList<>(
      Arrays.asList(IStandardConfig.StandardProperty.createdonattribute.toString(),
          IStandardConfig.StandardProperty.createdbyattribute.toString(),
          IStandardConfig.StandardProperty.lastmodifiedattribute.toString(),
          IStandardConfig.StandardProperty.lastmodifiedbyattribute.toString(),
          IStandardConfig.StandardProperty.nameattribute.toString()));
  
  @Autowired
  protected IGetUserStrategy                       getUserStrategy;
  
  @Autowired
  protected ISessionContext                        context;
  
  @Autowired
  protected IGetKlassWithReferencedKlassesStrategy getKlassWithReferencedKlassesStrategy;
  
  @Autowired
  protected PermissionUtils                        permissionUtils;
  
  @Autowired
  protected ApplicationThreadLocal                 threadLocal;
  
  @Autowired
  protected ApplicationContext                     applicationContext;
  
  @Autowired
  protected IGetKlassesStrategy                    neo4jGetKlassesByIdsStrategy;
  
  @Autowired
  protected IGetTargetStrategy                     neo4jGetTargetStrategy;
  
  @Autowired
  protected IGetDefaultKlassesStrategy             getDefaultKlassesStrategy;
  
  @Autowired
  protected IGetAllowedTypesStrategy               getAllowedTypesStrategy;
  
  @Resource(name = "languageContentLabelMap")
  protected Map<String, String>                    languageContentLabelMap;
  
  @Autowired
  protected TransactionThreadData                  transactionThreadData;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy          postConfigDetailsForRelationshipsStrategy;
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy  getConfigDetailsForCustomTabStrategy;
  
  /*
  @Autowired
  protected IGetKlassInstanceTypeStrategy          getArticleInstanceTypeStrategy;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy          getAssetInstanceTypeStrategy;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy          getMarketInstanceTypeStrategy;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy          getPromotionInstanceTypeStrategy;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy          getSupplierInstanceTypeStrategy;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy          getTextAssetInstanceTypeStrategy;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy          getVirtualCatalogInstanceTypeStrategy;*/
  
  @Autowired
  protected TransactionThreadData                   controllerThread;
  
  @Autowired
  protected RDBMSComponentUtils                     rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                              configUtil;
  
  @Autowired
  RelationshipInstanceUtil                          relationshipInstanceUtil;
  
  @Autowired
  protected VariantInstanceUtils                    variantInstanceUtils;
  
  public IKlass getReferencedKlass(String referenceId, List<? extends IKlass> referencedKlasses)
  {
    for (IKlass klass : referencedKlasses) {
      if (klass.getId()
          .equals(referenceId)) {
        return klass;
      }
    }
    return null;
  }
  
  public IKlassInstanceStructure createStructureInstance(IStructure newStructure)
  {
    IKlassInstanceStructure structrueInstance = new KlassInstanceStructure();
    structrueInstance.setId(newStructure.getId());
    structrueInstance.setStructureId(newStructure.getStructureId());
    return structrueInstance;
  }
  
  public String getDefaultInstanceNameByConfigdetails(
      IGetConfigDetailsForCustomTabModel configDetails, String typeId)
  {
    /*  String newInstanceName = languageContentLabelMap.get(lang);
    if (newInstanceName == null) {
      newInstanceName = "Neues";
    }
    
    IReferencedSectionElementModel nameAttribute = configDetails.getReferencedElements().get(IStandardConfig.StandardProperty.nameattribute.toString());
    if (nameAttribute != null) {
      newInstanceName = ((ISectionAttribute) nameAttribute).getDefaultValue();
    }*/
    // String newInstanceName = null;
    IReferencedKlassDetailStrategyModel referencedKlass = configDetails.getReferencedKlasses()
        .get(typeId);
    // return getInstanceNameWithKlassName(newInstanceName,
    // referencedKlass.getLabel());
    return referencedKlass.getLabel();
  }
  
  
  public void createPropertyInstances(IGetConfigDetailsForCustomTabModel configDetails,
      IKlassInstance klassInstance, List<String> attributeIdsToExclude, String currentUser,
      String language) throws Exception
  {
    createPropertyInstances(configDetails.getReferencedElements(),
        configDetails.getReferencedAttributes(), configDetails.getReferencedTags(), klassInstance,
        attributeIdsToExclude, currentUser, language);
  }
  
  public void createPropertyInstances(
      Map<String, IReferencedSectionElementModel> referencedElements,
      Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags,
      IKlassInstance klassInstance, List<String> attributeIdsToExclude, String currentUser,
      String language) throws Exception
  {
    List<ITagInstance> tagsToBeMaintainedList = (List<ITagInstance>) klassInstance.getTags();
    List<String> attributeIdsList = new ArrayList<>();
    List<String> tagIdsList = new ArrayList<>();
    for (ITagInstance tagInstance : tagsToBeMaintainedList) {
      tagIdsList.add(tagInstance.getTagId());
    }
    
    Set<Entry<String, IReferencedSectionElementModel>> referencedElementEntrySet = referencedElements
        .entrySet();
    for (Entry<String, IReferencedSectionElementModel> referencedSectionElementMap : referencedElementEntrySet) {
      IReferencedSectionElementModel referencedSectionElement = referencedSectionElementMap
          .getValue();
      String referencedSectionElementId = referencedSectionElementMap.getKey();
      
      if (CommonConstants.READ_ONLY_COUPLED.equals(referencedSectionElement.getCouplingType())) {
        continue;
      }
      
      // 2nd condition checking if attribute context is assign the dont create
      // attribute instance
      if (referencedSectionElement.getType()
          .equals(CommonConstants.ATTRIBUTE)
          && referencedSectionElement.getAttributeVariantContext() == null
          && !attributeIdsToExclude.contains(referencedSectionElementId)) {
        IAttribute referencedAttribute = referencedAttributes.get(referencedSectionElementId);
        
        String type = referencedAttribute.getType();
        Boolean isSpecialAttribute = AttributeInstanceUtils.isSpecialAttribute(type);
        
        IContentAttributeInstance attributeInstance = null;
        if (!isSpecialAttribute) {
          attributeInstance = AttributeInstanceUtils.createNewAttributeInstance(
              klassInstance.getId(), referencedSectionElement, referencedAttribute, language,
              currentUser);
          if (attributeInstance != null) {
            attributeIdsList.add(attributeInstance.getAttributeId());
            List<IContentAttributeInstance> attributeInstances = (List<IContentAttributeInstance>) klassInstance
                .getAttributes();
            attributeInstances.add(attributeInstance);
          }
        }
      }
      else if (referencedSectionElement.getType()
          .equals(CommonConstants.TAG)) {
        ITag referencedTag = referencedTags.get(referencedSectionElementId);
        String tagId = referencedTag.getId();
        if (tagIdsList.contains(tagId)) {
          continue;
        }
        ITagInstance tagInstance = TagInstanceUtils.createTagInstance(klassInstance,
            referencedSectionElement, referencedTag, currentUser);
        if (tagInstance != null) {
          tagsToBeMaintainedList.add(tagInstance);
          tagIdsList.add(tagId);
        }
      }
    }
    
    AttributeInstanceUtils.checkAndAddMissingMandatoryAttributes(klassInstance, attributeIdsList,
        referencedAttributes, currentUser, language);
    
    createMandatoryTagInstances(referencedTags, tagsToBeMaintainedList, currentUser, tagIdsList,
        klassInstance.getId());
  }
  
  private void createMandatoryTagInstances(Map<String, ITag> referencedTags,
      List<ITagInstance> tagsToBeMaintainedList, String currentUser, List<String> tagIdsList,
      String klassInstanceId) throws Exception
  {
    Long currentTimeMillis = System.currentTimeMillis();
    for (String tagId : IStandardConfig.StandardProperty.DefaultTagCodes) {
      ITag referencedTag = referencedTags.get(tagId);
      if (referencedTag == null || tagIdsList.contains(tagId)) {
        continue;
      }
      ITagInstance tagInstance = createTagInstance(referencedTag, currentTimeMillis,
          klassInstanceId);
      if (tagInstance == null) {
        continue;
      }
      tagsToBeMaintainedList.add(tagInstance);
    }
  }
  
  /*  public IContentAttributeInstance createNewAttributeInstanceOrRemoveIfExists(String  referencedKlassId,
      List<IContentAttributeInstance> attributesList, String entityName,
      ISectionElement sectionElement, boolean isRemove)
  {
    // IAttributeInstance attributeInstanceToReturn = null;
    ISectionAttribute sectionAttribute = ((SectionAttribute) sectionElement);
    IAttribute masterAttribute = sectionAttribute.getAttribute();
    if (!(masterAttribute instanceof ImageCoverflowAttribute)
        && !(masterAttribute instanceof CoverflowAttributeModel)
        && !(masterAttribute instanceof ImageAttributeModel)) {
      List<IPropertyInstance> foundPropertyInstances = getPropertyInstancesForPropertyIdWithUpdatedMappingId(attributesList, masterAttribute.getId());
      Boolean isSkipped = sectionAttribute.getIsSkipped();
      if (!isSkipped && (foundPropertyInstances == null || foundPropertyInstances.size() == 0)) {
        String couplingType = sectionAttribute.getCouplingType();
        String defaultValue = sectionAttribute.getDefaultValue();
        IAttributeInstance newAttributeInstance = createAttributeInstance(referencedKlassId, entityName, sectionAttribute, defaultValue, couplingType, CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
          return newAttributeInstance;
      }
      else if (isRemove) {
        attributesList.removeAll(foundPropertyInstances);
      }
    }
    return null;
  }*/
  
  public void createPropertyInstances(IKlass typeKlass, String entityName,
      List<? extends ITag> tags, IKlassInstance klassInstance) throws Exception
  {
  }
  
  public IRoleInstance createRoleInstanceOrRemoveIfExists(List<IRoleInstance> rolesList,
      ISectionElement sectionElement, boolean isRemove) throws Exception
  {
    String userId = null;
    ISectionRole sectionRole = ((SectionRole) sectionElement);
    IRole masterRole = sectionRole.getRole();
    List<IPropertyInstance> foundPropertyInstances = getPropertyInstancesForPropertyIdWithUpdatedMappingId(
        rolesList, masterRole.getId());
    if (foundPropertyInstances == null || foundPropertyInstances.size() == 0) {
      return createNewRoleInstance(userId, masterRole);
    }
    else if (isRemove) {
      rolesList.removeAll(foundPropertyInstances);
    }
    return null;
  }
  
  public IRoleInstance createNewRoleInstance(String userId, IRole masterRole) throws Exception
  {
    IRoleInstance roleInstance = new RoleInstance();
    roleInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ROLE.getPrefix()));
    roleInstance.setRoleId(masterRole.getId());
    // roleInstance.setMappingId(sectionRole.getId());
    if (masterRole.getId()
        .equals(SystemLevelIds.OWNER_ROLE)) {
      if (userId == null) {
        userId = context.getUserId();
      }
      IIdParameterModel idModel = new IdParameterModel(userId);
      IUserModel owner = getUserStrategy.execute(idModel);
      List<IRoleCandidate> candidates = new ArrayList<>();
      candidates.add((IRoleCandidate) owner.getEntity());
      roleInstance.setCandidates(candidates);
    }
    // rolesList.add(roleInstance);
    return roleInstance;
  }
  
  public ITagInstance createTagInstanceOrRemoveIfExists(IKlassInstance klassInstance,
      List<ITagInstance> tagsList, IReferencedSectionElementModel sectionElement,
      ITag referencedTag, boolean isRemove, String typeKlassId) throws Exception
  {
    IReferencedSectionTagModel sectionTagElement = (ReferencedSectionTagModel) sectionElement;
    List<IPropertyInstance> foundPropertyInstances = getPropertyInstancesForPropertyIdWithUpdatedMappingId(
        tagsList, referencedTag.getId());
    Boolean isSkipped = sectionTagElement.getIsSkipped();
    if (!isSkipped && foundPropertyInstances.isEmpty()) {
      ITagInstance tagInstance = createNewTagInstance(klassInstance, typeKlassId, sectionTagElement,
          referencedTag);
      return tagInstance;
    }
    else if (isRemove) {
      tagsList.removeAll(foundPropertyInstances);
    }
    return null;
  }
  
  public ITagInstance createNewTagInstance(IKlassInstance klassInstance, String typeKlassId,
      IReferencedSectionTagModel sectionElement, ITag referencedTag) throws Exception
  {
    ITagInstance tagInstance = getTagInstanceAndSetIsShouldAndIsMandatoryFields(sectionElement);
    if (tagInstance == null) {
      return null;
    }
    else {
      setConflictingValue(typeKlassId, sectionElement, tagInstance);
      setFlatProperty(klassInstance, referencedTag, tagInstance);
      // addTagsInContentBasedOnTagType(referencedTag, tagInstance,
      // sectionElement);
      return tagInstance;
    }
  }
  
  public ITagInstance createNewTagInstance(String klassInstanceId, String typeKlassId,
      IReferencedSectionTagModel sectionElement, ITag referencedTag) throws Exception
  {
    ITagInstance tagInstance = getTagInstanceAndSetIsShouldAndIsMandatoryFields(sectionElement);
    if (tagInstance == null) {
      return null;
    }
    else {
      setConflictingValue(typeKlassId, sectionElement, tagInstance);
      setFlatProperty(klassInstanceId, referencedTag, tagInstance);
      // addTagsInContentBasedOnTagType(referencedTag, tagInstance,
      // sectionElement);
      return tagInstance;
    }
  }
  
  private void setConflictingValue(String typeKlassId, IReferencedSectionTagModel sectionElement,
      ITagInstance tagInstance)
  {
    if (sectionElement.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED)
        || (sectionElement.getDefaultValue() != null && !sectionElement.getDefaultValue()
            .isEmpty())) {
      ITagConflictingValue conflictingValue = new TagConflictingValue();
      conflictingValue.setCouplingType(sectionElement.getCouplingType());
      conflictingValue.setTagValues(sectionElement.getDefaultValue());
      
      IKlassConflictingValueSource conflictingValueSource = new KlassConflictingValueSource();
      conflictingValueSource.setId(typeKlassId);
      conflictingValueSource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
      conflictingValue.setSource(conflictingValueSource);
      List<ITagConflictingValue> listOfConflictingValues = tagInstance.getConflictingValues();
      if (listOfConflictingValues == null) {
        listOfConflictingValues = new ArrayList<>();
      }
      listOfConflictingValues.add(conflictingValue);
    }
  }
  
  private void setFlatProperty(IKlassInstance klassInstance, ITag referencedTag,
      ITagInstance tagInstance) throws RDBMSException, Exception
  {
    IContextInstance context = ((IContentInstance) klassInstance).getContext();
    String klassInstanceId = null;
    String variantInstanceId = null;
    if (context == null) {
      klassInstanceId = klassInstance.getId();
    }
    else {
      klassInstanceId = ((IContentInstance) klassInstance).getKlassInstanceId();
      variantInstanceId = ((IContentInstance) klassInstance).getId();
    }
    
    tagInstance.setVariantInstanceId(variantInstanceId);
    tagInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagInstance.setTagId(referencedTag.getId());
    tagInstance.setKlassInstanceId(klassInstanceId);
    Long timestamp = System.currentTimeMillis();
    tagInstance.setCreatedOn(timestamp);
    tagInstance.setLastModified(timestamp);
    tagInstance.setVersionTimestamp(timestamp);
    tagInstance.setVersionId(0l);
    tagInstance.setLastModifiedBy(transactionThreadData.getTransactionData()
        .getUserId());
    tagInstance.setCreatedBy(transactionThreadData.getTransactionData()
        .getUserId());
  }
  
  private void setFlatProperty(String klassInstanceId, ITag referencedTag, ITagInstance tagInstance) throws Exception
  {
    tagInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagInstance.setTagId(referencedTag.getId());
    tagInstance.setKlassInstanceId(klassInstanceId);
    Long timestamp = System.currentTimeMillis();
    tagInstance.setCreatedOn(timestamp);
    tagInstance.setLastModified(timestamp);
    tagInstance.setVersionTimestamp(timestamp);
    tagInstance.setVersionId(0l);
    tagInstance.setLastModifiedBy(transactionThreadData.getTransactionData()
        .getUserId());
    tagInstance.setCreatedBy(transactionThreadData.getTransactionData()
        .getUserId());
  }
  
  public ITagInstance createTagInstanceOrRemoveIfExists(List<ITagInstance> tagsList, ITag tag,
      Long currentTimeMillis) throws Exception
  {
    List<IPropertyInstance> foundPropertyInstances = getPropertyInstancesForPropertyIdWithUpdatedMappingId(
        tagsList, tag.getId());
    if (foundPropertyInstances == null || foundPropertyInstances.size() == 0) {
      return createTagInstance(tag, currentTimeMillis, null);
    }
    return null;
  }
  
  public ITagInstance createTagInstance(ITag tag, Long currentTimeMillis, String klassInstanceId) throws Exception
  {
    ITagInstance tagInstance = new TagInstance();
    tagInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagInstance.setTagId(tag.getId());
    tagInstance.setVersionId(0l);
    tagInstance.setVersionTimestamp(currentTimeMillis);
    tagInstance.setLastModified(currentTimeMillis);
    tagInstance.setLastModifiedBy(context.getUserId());
    tagInstance.setCreatedBy(context.getUserId());
    tagInstance.setCreatedOn(currentTimeMillis);
    tagInstance.setKlassInstanceId(klassInstanceId);
    addTagsInContentBasedOnTagType(tag, tagInstance);
    
    String tagId = tagInstance.getTagId();
    if (tagId.equals(SystemLevelIds.LIFE_STATUS_TAG_ID)) {
      for (ITagInstanceValue tagValues : tagInstance.getTagValues()) {
        if (tagValues.getTagId()
            .equals(SystemLevelIds.LIFE_STATUS_INBOX)) {
          tagValues.setRelevance(100);
        }
      }
    }
    else if (tagId.equals(SystemLevelIds.LISTING_STATUS_TAG_ID)
        && !transactionThreadData.getTransactionData()
            .getPhysicalCatalogId()
            .equals(Constants.PIM)) {
      // only in case of onBoarding and central staging
      for (ITagInstanceValue tagValues : tagInstance.getTagValues()) {
        if (tagValues.getTagId()
            .equals(SystemLevelIds.LISTING_STATUS_CATLOG)) {
          tagValues.setRelevance(100);
        }
      }
    }
    
    return tagInstance;
  }
  
  private void addSectionTagDefaultValues(ITagInstance tagInstance, List<IIdRelevance> defaultValue) throws Exception
  {
    Map<String, Integer> defaultValueMap = new HashMap<>();
    for (IIdRelevance iIdRelevance : defaultValue) {
      defaultValueMap.put(iIdRelevance.getTagId(), iIdRelevance.getRelevance());
    }
    for (ITagInstanceValue value : tagInstance.getTagValues()) {
      Integer relevance = defaultValueMap.remove(value.getTagId());
      if (relevance != null) {
        value.setRelevance(relevance);
      }
    }
    
    Set<Entry<String, Integer>> defaultValueMapEntrySet = defaultValueMap.entrySet();
    for(Entry<String, Integer> relevanceMap : defaultValueMapEntrySet) {
      String tagId = relevanceMap.getKey();
      Integer relevance = relevanceMap.getValue();
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
      tagInstanceValue.setRelevance(relevance);
      tagInstanceValue.setTagId(tagId);
      tagInstance.getTagValues()
          .add(tagInstanceValue);
    }
  }
  
  public IContentAttributeInstance createNewAttributeInstanceOrRemoveIfExists(IKlass typeKlass,
      List<IContentAttributeInstance> attributesList, String entityName,
      ISectionElement sectionElement, boolean isRemove) throws Exception
  {
    // IAttributeInstance attributeInstanceToReturn = null;
    ISectionAttribute sectionAttribute = ((SectionAttribute) sectionElement);
    IAttribute masterAttribute = sectionAttribute.getAttribute();
    if (!(masterAttribute instanceof ImageCoverflowAttribute)
        && !(masterAttribute instanceof CoverflowAttributeModel)
        && !(masterAttribute instanceof ImageAttributeModel)) {
      List<IPropertyInstance> foundPropertyInstances = getPropertyInstancesForPropertyIdWithUpdatedMappingId(
          attributesList, masterAttribute.getId());
      Boolean isSkipped = sectionAttribute.getIsSkipped();
      if (!isSkipped && (foundPropertyInstances == null || foundPropertyInstances.size() == 0)) {
        String couplingType = sectionAttribute.getCouplingType();
        String defaultValue = sectionAttribute.getDefaultValue();
        String defaultValueAsHtml = sectionAttribute.getValueAsHtml();
        IAttributeInstance newAttributeInstance = createAttributeInstance(typeKlass.getId(),
            entityName, masterAttribute, defaultValue, couplingType,
            CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE, defaultValueAsHtml);
        return newAttributeInstance;
      }
      else if (isRemove) {
        attributesList.removeAll(foundPropertyInstances);
      }
    }
    return null;
  }
  
  public IAttributeInstance createAttributeInstance(IKlassInstance klassInstance, String typeId,
      String entityName, IReferencedSectionAttributeModel sectionElement,
      IAttribute referencedAttribute, String defaultValue, String couplingType,
      String conflictingType, String defaultValueAsHtml) throws Exception
  {
    Boolean isConflictingValueForName = true;
    IAttributeInstance newAttributeInstance = getAttributeInstanceAndSetIsShouldAndIsMandatoryFields(
        sectionElement);
    if (newAttributeInstance == null) {
      return newAttributeInstance;
    }
    long currentTimeMillis = System.currentTimeMillis();
    
    if (referencedAttribute.getType()
        .equals(NameAttribute.class.getName()) && entityName != null) {
      if (defaultValue == null || defaultValue.equals("")) {
        isConflictingValueForName = false;
      }
      defaultValue = entityName;
    }
    else if (referencedAttribute.getType()
        .equals(CreatedOnAttribute.class.getName())) {
      defaultValue = String.valueOf(currentTimeMillis);
      klassInstance.setCreatedOn(currentTimeMillis);
    }
    else if (referencedAttribute.getType()
        .equals(LastModifiedAttribute.class.getName())) {
      defaultValue = String.valueOf(currentTimeMillis);
      klassInstance.setLastModified(currentTimeMillis);
    }
    else if (referencedAttribute.getType()
        .equals(CreatedByAttribute.class.getName())) {
      defaultValue = context.getUserId();
      klassInstance.setCreatedBy(defaultValue);
    }
    else if (referencedAttribute.getType()
        .equals(LastModifiedByAttribute.class.getName())) {
      defaultValue = context.getUserId();
      klassInstance.setLastModifiedBy(defaultValue);
    }
    else if (defaultValue == null || defaultValue.equals("")) {
      defaultValue = sectionElement.getDefaultValue();
    }
    
    setConflictingValue(typeId, defaultValue, couplingType, conflictingType,
        isConflictingValueForName, newAttributeInstance, defaultValueAsHtml);
    setFlatPropertyForAttributeInstance(referencedAttribute, defaultValue, newAttributeInstance,
        currentTimeMillis, klassInstance.getId());
    
    return newAttributeInstance;
  }
  
  public void setFlatPropertyForAttributeInstance(IAttribute referencedAttribute,
      String defaultValue, IAttributeInstance newAttributeInstance, long currentTimeMillis,
      String klassInstanceId) throws Exception
  {
    newAttributeInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
    String attributeId = referencedAttribute.getId();
    newAttributeInstance.setAttributeId(attributeId);
    newAttributeInstance.setCode(referencedAttribute.getCode());
    
    if (referencedAttribute.getType()
        .equals(HTMLAttribute.class.getName())) {
      newAttributeInstance.setValueAsHtml(defaultValue != null ? defaultValue : "");
    }
    newAttributeInstance.setLastModified(currentTimeMillis);
    newAttributeInstance.setValue(defaultValue != null ? defaultValue : "");
    
    TransactionData transactionData = transactionThreadData.getTransactionData();
    newAttributeInstance.setCreatedOn(currentTimeMillis);
    newAttributeInstance.setLastModified(currentTimeMillis);
    newAttributeInstance.setVersionTimestamp(currentTimeMillis);
    newAttributeInstance.setVersionId(0l);
    newAttributeInstance.setLastModifiedBy(transactionData.getUserId());
    newAttributeInstance.setCreatedBy(transactionData.getUserId());
    newAttributeInstance.setKlassInstanceId(klassInstanceId);
    if (referencedAttribute.getIsTranslatable()) {
      newAttributeInstance.setLanguage(transactionData.getDataLanguage());
    }
  }
  
  public void setConflictingValue(String typeId, String defaultValue, String couplingType,
      String conflictingType, Boolean isConflictingValueForName,
      IAttributeInstance newAttributeInstance, String defaultValueAsHtml)
  {
    if (isConflictingValueForName && (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)
        || ((defaultValue != null && !defaultValue.equals(""))
            || (defaultValueAsHtml != null && !defaultValueAsHtml.equals(""))))) {
      IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
      conflictingValue.setValue(defaultValue);
      conflictingValue.setValueAsHtml(defaultValueAsHtml);
      conflictingValue.setCouplingType(couplingType);
      IConflictingValueSource conflictingValueSource;
      if (conflictingType.equals(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE)) {
        conflictingValueSource = new KlassConflictingValueSource();
      }
      else {
        conflictingValueSource = new TaxonomyConflictingValueSource();
      }
      conflictingValueSource.setId(typeId);
      conflictingValueSource.setType(conflictingType);
      conflictingValue.setSource(conflictingValueSource);
      List<IAttributeConflictingValue> listOfConflictingValues = newAttributeInstance
          .getConflictingValues();
      if (listOfConflictingValues == null) {
        listOfConflictingValues = new ArrayList<>();
      }
      listOfConflictingValues.add(conflictingValue);
    }
  }
  
  public IAttributeInstance getAttributeInstanceAndSetIsShouldAndIsMandatoryFields(
      IReferencedSectionAttributeModel sectionElement)
  {
    IAttributeInstance newAttributeInstance = null;
    
    Boolean isValueExist = (sectionElement.getDefaultValue() == null
        || sectionElement.getDefaultValue()
            .equals("")) ? false : true;
    Boolean isDynamicCouple = sectionElement.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED);
    Boolean isMandatoryViolated = sectionElement.getIsMandatory();
    Boolean isShouldViolated = sectionElement.getIsShould();
    
    isMandatoryViolated = (isMandatoryViolated == null) ? false : isMandatoryViolated;
    isShouldViolated = (isShouldViolated == null) ? false : isShouldViolated;
    
    if (isValueExist) {
      newAttributeInstance = new AttributeInstance();
      newAttributeInstance.setIsMandatoryViolated(false);
      newAttributeInstance.setIsShouldViolated(false);
    }
    else if (isDynamicCouple || isMandatoryViolated || isShouldViolated) {
      newAttributeInstance = new AttributeInstance();
      newAttributeInstance.setIsMandatoryViolated(isMandatoryViolated);
      newAttributeInstance.setIsShouldViolated(isShouldViolated);
    }
    
    return newAttributeInstance;
  }
  
  private ITagInstance getTagInstanceAndSetIsShouldAndIsMandatoryFields(
      IReferencedSectionTagModel sectionElement) throws Exception
  {
    ITagInstance newTagInstance = new TagInstance();
    
    Boolean isValueExist = false;
    List<IIdRelevance> tagValues = sectionElement.getDefaultValue();
    for (IIdRelevance tagValue : tagValues) {
      Integer relevance = tagValue.getRelevance();
      if (relevance != null && !relevance.equals(0)) {
        newTagInstance.getTagValues()
            .add(getTagGroupValue(tagValue));
        isValueExist = true;
      }
    }
    
    Boolean isDynamicCouple = sectionElement.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED);
    Boolean isMandatoryViolated = sectionElement.getIsMandatory();
    Boolean isShouldViolated = sectionElement.getIsShould();
    
    isMandatoryViolated = (isMandatoryViolated == null) ? false : isMandatoryViolated;
    isShouldViolated = (isShouldViolated == null) ? false : isShouldViolated;
    
    if (isValueExist) {
      newTagInstance.setIsMandatoryViolated(false);
      newTagInstance.setIsShouldViolated(false);
      return newTagInstance;
    }
    else if (isDynamicCouple || isMandatoryViolated || isShouldViolated) {
      newTagInstance.setIsMandatoryViolated(isMandatoryViolated);
      newTagInstance.setIsShouldViolated(isShouldViolated);
      return newTagInstance;
    }
    
    return null;
  }
  
  public ITagInstanceValue getTagGroupValue(IIdRelevance tagValue) throws Exception
  {
    ITagInstanceValue tagValueInstance = new TagInstanceValue();
    tagValueInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagValueInstance.setRelevance(tagValue.getRelevance());
    tagValueInstance.setTagId(tagValue.getTagId());
    tagValueInstance.setTimestamp(Long.toString(System.currentTimeMillis()));
    tagValueInstance.setVersionId(0l);
    return tagValueInstance;
  }
  
  @Deprecated
  public IAttributeInstance createAttributeInstance(String typeId, String entityName,
      IAttribute masterAttribute, String defaultValue, String couplingType, String conflictingType,
      String defaultValueAsHtml) throws Exception
  {
    Boolean isConflictingValueForName = true;
    IAttributeInstance newAttributeInstance = new AttributeInstance();
    if (masterAttribute instanceof NameAttribute && entityName != null) {
      if (defaultValue == null || defaultValue.equals("")) {
        isConflictingValueForName = false;
      }
      defaultValue = entityName;
    }
    else if (masterAttribute instanceof CreatedOnAttribute) {
      defaultValue = String.valueOf(System.currentTimeMillis());
    }
    else if (masterAttribute instanceof LastModifiedAttribute) {
      defaultValue = String.valueOf(System.currentTimeMillis());
    }
    else if (masterAttribute instanceof CreatedByAttribute) {
      defaultValue = context.getUserId();
    }
    else if (masterAttribute instanceof LastModifiedByAttribute) {
      defaultValue = context.getUserId();
    }
    else if (defaultValue == null || defaultValue.equals("")) {
      defaultValue = masterAttribute.getDefaultValue();
    }
    setConflictingValue(typeId, defaultValue, couplingType, conflictingType,
        isConflictingValueForName, newAttributeInstance, defaultValueAsHtml);
    
    newAttributeInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
    newAttributeInstance.setAttributeId(masterAttribute.getId());
    newAttributeInstance.setCode(masterAttribute.getCode());
    
    if (masterAttribute.getType()
        .equals(HTMLAttribute.class.getName())) {
      newAttributeInstance.setValueAsHtml(defaultValueAsHtml != null ? defaultValueAsHtml : "");
    }
    newAttributeInstance.setLastModified(System.currentTimeMillis());
    newAttributeInstance.setValue(defaultValue != null ? defaultValue : "");
    
    TransactionData transactionData = transactionThreadData.getTransactionData();
    Long timestamp = System.currentTimeMillis();
    newAttributeInstance.setCreatedOn(timestamp);
    newAttributeInstance.setLastModified(timestamp);
    newAttributeInstance.setVersionTimestamp(timestamp);
    newAttributeInstance.setLastModifiedBy(transactionData.getUserId());
    newAttributeInstance.setCreatedBy(transactionData.getUserId());
    if (masterAttribute.getIsTranslatable()) {
      newAttributeInstance.setLanguage(transactionData.getDataLanguage());
    }
    
    return newAttributeInstance;
  }
  
  public List<IPropertyInstance> getPropertyInstancesForPropertyIdWithUpdatedMappingId(
      List<? extends IPropertyInstance> propertyInstancesArray, String propertyId)
  {
    
    List<IPropertyInstance> propertyInstances = new ArrayList<>();
    
    for (IPropertyInstance propertyInstance : propertyInstancesArray) {
      boolean doesPropertyInstanceExist = false;
      
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
        propertyInstances.add(propertyInstance);
      }
    }
    
    return propertyInstances;
  }
  
  public void addTagsInContentBasedOnTagType(ITag masterTag, ITagInstance tagInstance,
      ISectionTag sectionTag) throws Exception
  {
    String tagType = masterTag.getTagType();
    if (sectionTag != null) {
      List<IIdRelevance> defaultValue = sectionTag.getDefaultValue();
      if (defaultValue != null && defaultValue.size() > 0) {
        addLeafTagsToTagValues((List<ITag>) masterTag.getChildren(), tagInstance, 0);
        addSectionTagDefaultValues(tagInstance, defaultValue);
        return;
      }
    }
    
    if (tagType.equals(YES_NEUTRAL_TAG_TYPE)) {
      ITag defaultTag = masterTag.getDefaultValue();
      if (defaultTag != null && defaultTag.getId() != null) {
        tagInstance.getTagValues()
            .add(getTagGroupValue(defaultTag, 100));
      }
      else {
        addLeafTagsToTagValues((List<ITag>) masterTag.getChildren(), tagInstance, 0);
      }
    }
    else {
      addLeafTagsToTagValues((List<ITag>) masterTag.getChildren(), tagInstance, 0);
    }
  }
  
  public void addTagsInContentBasedOnTagType(ITag masterTag, ITagInstance tagInstance) throws Exception
  {
    String tagType = masterTag.getTagType();
    if (tagType.equals(YES_NEUTRAL_TAG_TYPE)) {
      ITag defaultTag = masterTag.getDefaultValue();
      if (defaultTag != null && defaultTag.getId() != null) {
        tagInstance.getTagValues()
            .add(getTagGroupValue(defaultTag, 100));
      }
      else {
        addLeafTagsToTagValues((List<ITag>) masterTag.getChildren(), tagInstance, 0);
      }
    }
    else {
      addLeafTagsToTagValues((List<ITag>) masterTag.getChildren(), tagInstance, 0);
    }
  }
  
  public ITagInstanceValue getTagGroupValue(ITag tagValue, Integer relevance) throws Exception
  {
    ITagInstanceValue tagValueInstance = new TagInstanceValue();
    tagValueInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagValueInstance.setRelevance(relevance != null ? relevance : 0);
    tagValueInstance.setTagId(tagValue.getId());
    tagValueInstance.setTimestamp(Long.toString(System.currentTimeMillis()));
    tagValueInstance.setVersionId(0l);
    return tagValueInstance;
  }
  
  public List<ITag> addLeafTagsToTagValues(List<ITag> tags, ITagInstance tagInstance,
      Integer relevance) throws Exception
  {
    List<ITag> leafTags = new ArrayList<>();
    for (ITag tagValue : tags) {
      List<ITag> childTags = (List<ITag>) tagValue.getChildren();
      if (childTags.size() > 0) {
        addLeafTagsToTagValues(childTags, tagInstance, relevance);
      }
      else {
        tagInstance.getTagValues()
            .add(getTagGroupValue(tagValue, relevance));
      }
    }
    return leafTags;
  }
  
  /* public IArticleInstanceModel createNewKlassInstance(IProjectKlass typeKlass,
      List<? extends IKlass> referencedKlasses,
      String newInstanceName, List<? extends ITag> tags)
      throws Exception
  {
    IArticleInstance newKlassInstance = new ArticleInstance();
    newKlassInstance.setName(newInstanceName);
    List<String> types = new ArrayList<>();
    types.add(typeKlass.getId());
    newKlassInstance.setTypes(types);
  
    createPropertyInstances(typeKlass,
        (List<IContentAttributeInstance>) newKlassInstance.getAttributes(),
        (List<ITagInstance>) newKlassInstance.getTags(),
        (List<IRoleInstance>) newKlassInstance.getRoles(), newInstanceName, tags);
  
    IArticleInstanceModel newKlassInstanceModel = new ArticleInstanceModel(newKlassInstance);
    return newKlassInstanceModel;
  }*/
  
  public IGetKlassModel getTypeKlassFromStandardKlass(String standardKlassId) throws Exception
  {
    IGetKlassModel typeKlassModel = null;
    IIdParameterModel idParameterModel = new IdParameterModel(standardKlassId);
    IGetKlassModel standardKlassModel = getKlassWithReferencedKlassesStrategy
        .execute(idParameterModel);
    IProjectKlass standardProductKlass = (IProjectKlass) standardKlassModel.getKlass();
    
    String defaultTypeValue = "";
    if (standardProductKlass != null) {
      List<String> attributeIdsToFetch = new ArrayList<>();
    }
    if (!defaultTypeValue.isEmpty()) {
      IIdParameterModel typeKlassIdModel = new IdParameterModel(defaultTypeValue);
      IGetKlassModel getKlassModel = getKlassWithReferencedKlassesStrategy
          .execute(typeKlassIdModel);
      if (getKlassModel.getKlass() != null) {
        typeKlassModel = getKlassModel;
      }
    }
    else {
      typeKlassModel = standardKlassModel;
    }
    
    return typeKlassModel;
  }
  
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
        if (defaultNameValue != null && !defaultNameValue.equalsIgnoreCase("null")
            && !defaultNameValue.isEmpty()) {
          newInstanceName = defaultNameValue;
        }
        break;
      }
    }
    return getInstanceNameWithKlassName(newInstanceName, typeKlass.getLabel());
  }
  
  public String getInstanceNameWithKlassName(String newInstanceName, String label)
  {
    return newInstanceName + " " + label;
  }
  
  public String getDefaultInstanceName(String lang)
  {
    String newInstanceName = languageContentLabelMap.get(lang);
    if (newInstanceName == null) {
      newInstanceName = "Neues";
    }
    return newInstanceName;
  }
  
  public String getInstanceNameWithKlassLabel(String lang, String label)
  {
    String newInstanceName = languageContentLabelMap.get(lang);
    if (newInstanceName == null) {
      newInstanceName = "Neues";
    }
    return newInstanceName + " " + label;
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
  
  public ITargetModel getTypeKlassFromStandardTargetKlass(String id) throws Exception
  {
    ITargetModel targetKlassModel = null;
    IIdParameterModel idParameterModel = new IdParameterModel(id);
    ITargetModel standardKlassModel = neo4jGetTargetStrategy.execute(idParameterModel);
    IKlass standardKlass = (IKlass) standardKlassModel.getEntity();
    
    String defaultTypeValue = "";
    if (standardKlass != null) {
      List<String> attributeIdsToFetch = new ArrayList<>();
    }
    if (!defaultTypeValue.isEmpty()) {
      IIdParameterModel typeKlassIdModel = new IdParameterModel(defaultTypeValue);
      ITargetModel getKlassModel = neo4jGetTargetStrategy.execute(typeKlassIdModel);
      if (getKlassModel.getEntity() != null) {
        targetKlassModel = getKlassModel;
      }
    }
    else {
      targetKlassModel = standardKlassModel;
    }
    
    return targetKlassModel;
  }
  
  @Deprecated
  public IGlobalPermission getCompleteRightGlobalPermission()
  {
    IGlobalPermission globalPermission = new GlobalPermission();
    globalPermission.setCanCreate(true);
    globalPermission.setCanDelete(true);
    globalPermission.setCanRead(true);
    globalPermission.setCanEdit(true);
    return globalPermission;
  }
  
  public Map<String, List<Map<String, Object>>> getRolesIdsToNotifyFieldChanges(
      IKlassInstanceDiffModel iKlassInstanceDiffModel,
      IGetMultiClassificationKlassDetailsModel multiClassificationDetails) throws Exception
  {
    Map<String, List<Map<String, Object>>> roleIdsToBeNotified = new HashMap<>();
    List<String> modifiedPropertyIds = new ArrayList<>();
    Map<String, Map<String, Object>> modifiedPropertiesMap = new HashMap<>();
    Map<String, Set<String>> notificationSettings = multiClassificationDetails
        .getNotificationSettings();
    List<IAttributeDiffModel> attributes = iKlassInstanceDiffModel.getAttributes();
    setModifiedAttributesInfo(modifiedPropertyIds, modifiedPropertiesMap, attributes,
        multiClassificationDetails);
    
    List<ITagDiffModel> tags = iKlassInstanceDiffModel.getTags();
    setModifiedTagsInfo(modifiedPropertyIds, modifiedPropertiesMap, tags,
        multiClassificationDetails);
    
    for (String roleId : notificationSettings.keySet()) {
      Set<String> notificationSettingForRole = notificationSettings.get(roleId);
      List<Map<String, Object>> modifiedFieldsForRole = new ArrayList<>();
      for (String propertyIdToNotify : notificationSettingForRole) {
        Map<String, Object> modifiedProperty = modifiedPropertiesMap.get(propertyIdToNotify);
        if (modifiedProperty != null) {
          modifiedFieldsForRole.add(modifiedProperty);
        }
      }
      if (modifiedFieldsForRole.size() > 0)
        roleIdsToBeNotified.put(roleId, modifiedFieldsForRole);
    }
    
    return roleIdsToBeNotified;
  }
  
  private void setModifiedAttributesInfo(List<String> modifiedPropertyIds,
      Map<String, Map<String, Object>> modifiedFieldsMap, List<IAttributeDiffModel> attributes,
      IGetMultiClassificationKlassDetailsModel multiClassificationDetails) throws Exception
  {
    
    for (IAttributeDiffModel diffAttribute : attributes) {
      Map<String, Object> labelValue = new HashMap<>();
      String attributeId = diffAttribute.getAttributeId();
      modifiedPropertyIds.add(attributeId);
      String label = null;
      Map<String, IAttribute> referencedAttributes = multiClassificationDetails
          .getReferencedAttributes();
      IAttribute attribute = referencedAttributes.get(attributeId);
      if (attribute == null) {
        continue;
      }
      
      label = attribute.getLabel();
      labelValue.put("id", attributeId);
      labelValue.put("label", label);
      String newValue = diffAttribute.getNewValue();
      String oldValue = diffAttribute.getOldValue();
      
      if (attribute instanceof DateAttributeModel) {
        SimpleDateFormat ft = new SimpleDateFormat("d.M.yyyy");
        if (newValue != null && newValue.length() > 0) {
          labelValue.put("value", ft.format(new Date(Long.valueOf(newValue))));
        }
        else {
          labelValue.put("oldValue", "\"\"");
        }
        if (oldValue != null && oldValue.length() > 0) {
          labelValue.put("oldValue", ft.format(new Date(Long.valueOf(oldValue))));
        }
        else {
          labelValue.put("oldValue", "\"\"");
        }
        
      }
      else if (attribute instanceof OwnerRole) {
        IIdParameterModel userIdModel = new IdParameterModel(newValue);
        IUserModel ownerModel = getUserStrategy.execute(userIdModel);
        
        userIdModel.setId(oldValue);
        IUserModel previousOwnerModel = getUserStrategy.execute(userIdModel);
        
        labelValue.put("value", ownerModel.getFirstName() + " " + ownerModel.getLastName());
        labelValue.put("oldValue",
            previousOwnerModel.getFirstName() + " " + previousOwnerModel.getLastName());
      }
      else {
        labelValue.put("value", newValue);
        labelValue.put("oldValue", oldValue);
      }
      
      modifiedFieldsMap.put(attributeId, labelValue);
    }
  }
  
  // TODO: Section Element is null when a dimentaional tag is applied on a
  // attribute. Diff is Not created correctly
  private void setModifiedTagsInfo(List<String> mappingIdOfModifiedFields,
      Map<String, Map<String, Object>> modifiedFieldsMap, List<ITagDiffModel> tags,
      IGetMultiClassificationKlassDetailsModel multiClassificationDetails) throws Exception
  {
    for (ITagDiffModel iTagDiffModel : tags) {
      String tagId = iTagDiffModel.getTagId();
      Map<String, ITag> referencedTags = multiClassificationDetails.getReferencedTags();
      ITag tagGroup = referencedTags.get(tagId);
      Map<String, Object> labelValue = new HashMap<>();
      
      if (tagGroup == null) {
        continue;
      }
      mappingIdOfModifiedFields.add(tagId);
      
      String label = tagGroup.getLabel();
      Object value = null;
      Object oldValue = null;
      List<ITagDiffValueModel> addedValues = iTagDiffModel.getAddedValues();
      List<ITagDiffValueModel> oldValues = iTagDiffModel.getOldValues();
      List<ITagDiffValueModel> deletedValues = iTagDiffModel.getDeletedValues();
      List<ITagDiffValueModel> modifiedValues = iTagDiffModel.getModifiedValues();
      
      if (tagGroup.getTagType()
          .equals(SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID) && !tagGroup.getIsMultiselect()) {
        HashMap<String, List<String>> modificationInfo = new HashMap<>();
        if (addedValues.size() > 0) {
          List<String> addedList = new ArrayList<>();
          modificationInfo.put("added", addedList);
          ITag tag = addedValues.get(0)
              .getTag();
          if (tag != null) {
            addedList.add(tag.getLabel());
          }
        }
        if (oldValues.size() > 0) {
          if (oldValues.get(0)
              .getRelevance()
              .equals("100"))
            oldValue = oldValues.get(0)
                .getTag()
                .getLabel();
        }
        if (modifiedValues.size() > 0) {
          List<String> modifiedList = new ArrayList<>();
          modificationInfo.put("modified", modifiedList);
          for (ITagDiffValueModel modifiedValue : modifiedValues) {
            modifiedList.add(modifiedValue.getTag()
                .getLabel());
          }
          value = modificationInfo;
        }
      }
      else if (tagGroup.getTagType()
          .equals(SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID) && tagGroup.getIsMultiselect()) {
        HashMap<String, List<String>> modificationInfo = new HashMap<>();
        List<String> oldList = new ArrayList<>();
        
        if (addedValues.size() > 0) {
          List<String> addedList = new ArrayList<>();
          modificationInfo.put("added", addedList);
          for (ITagDiffValueModel addedValue : addedValues) {
            addedList.add(addedValue.getTag()
                .getLabel());
          }
        }
        
        List<String> removedTagValues = new ArrayList<>();
        modificationInfo.put("removed", removedTagValues);
        for (ITagDiffValueModel tagValue : deletedValues) {
          removedTagValues.add(tagValue.getTag()
              .getLabel());
        }
        
        if (modifiedValues.size() > 0) {
          List<String> modifiedList = new ArrayList<>();
          modificationInfo.put("modified", modifiedList);
          for (ITagDiffValueModel modifiedValue : modifiedValues) {
            modifiedList.add(modifiedValue.getTag()
                .getLabel());
          }
        }
        for (ITagDiffValueModel tagValue : oldValues) {
          if (tagValue.getRelevance()
              .equals("100")) {
            oldList.add(tagValue.getTag()
                .getLabel());
          }
        }
        value = modificationInfo;
        oldValue = oldList;
      }
      else if (tagGroup.getTagType()
          .equals(SystemLevelIds.RANGE_TAG_TYPE_ID)) {
        HashMap<String, List<String>> modificationInfo = new HashMap<>();
        List<String> oldList = new ArrayList<>();
        
        List<String> modifiedList = new ArrayList<>();
        modificationInfo.put("modified", modifiedList);
        for (ITagDiffValueModel modifiedValue : modifiedValues) {
          modifiedList.add(modifiedValue.getTag()
              .getLabel() + ":" + modifiedValue.getRelevance());
        }
        
        for (ITagDiffValueModel tagValue : oldValues) {
          oldList.add(tagValue.getTag()
              .getLabel() + ":" + tagValue.getRelevance());
        }
        
        value = modificationInfo;
        oldValue = oldList;
        
      }
      else if (tagGroup.getTagType()
          .equals(SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID)) {
        HashMap<String, List<String>> modificationInfo = new HashMap<>();
        List<String> oldList = new ArrayList<>();
        
        List<String> modifiedList = new ArrayList<>();
        modificationInfo.put("modified", modifiedList);
        for (ITagDiffValueModel modifiedValue : modifiedValues) {
          String tagValue = modifiedValue.getRelevance()
              .equals("0") ? "Neutral"
                  : modifiedValue.getRelevance()
                      .equals("100") ? "Yes" : "No";
          modifiedList.add(modifiedValue.getTag()
              .getLabel() + ":" + tagValue);
        }
        
        for (ITagDiffValueModel tagValue : oldValues) {
          String tagRelevance = tagValue.getRelevance()
              .equals("0") ? "Neutral"
                  : tagValue.getRelevance()
                      .equals("100") ? "Yes" : "No";
          oldList.add(tagValue.getTag()
              .getLabel() + ":" + tagRelevance);
        }
        
        value = modificationInfo;
        oldValue = oldList;
      }
      labelValue.put("id", tagId);
      labelValue.put("label", label);
      labelValue.put("value", value);
      labelValue.put("oldValue", oldValue);
      
      modifiedFieldsMap.put(tagId, labelValue);
    }
  }
  
  public IKlass getKlassFromList(List<IKlass> klasses, String klassId)
  {
    for (IKlass klass : klasses) {
      String id = klass.getId();
      if (id.equals(klassId)) {
        return klass;
      }
    }
    return null;
  }
  
  public void notifyRolesOfEachKlassInstance(
      IListModel<? extends IKlassInstance> savedKlassInstancesModel,
      Map<String, Map<String, List<Map<String, Object>>>> klassInstanceRolesMapping)
      throws Exception
  {
    try {
      // infoLog(klassInstanceRolesMapping);
      IIdParameterModel idParameterModel = new IdParameterModel(context.getUserId());
      IUser currentUser = getUserStrategy.execute(idParameterModel);
      KlassInstanceNotificationTask notificationTask = (KlassInstanceNotificationTask) applicationContext
          .getBean("klassInstanceNotificationTask");
      String requestId = "";
      String sessionId = "";
      if (threadLocal.getValue() != null) {
        requestId = threadLocal.getValue()
            .getRequestId();
        sessionId = threadLocal.getValue()
            .getSessionId();
      }
      notificationTask.setData(savedKlassInstancesModel, klassInstanceRolesMapping, currentUser,
          requestId, sessionId);
      // notificationTaskExecutor.execute(notificationTask);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      String stackTrace = CSLogUtil.getStackTrace(e);
      // errorLog(e.getMessage(), stackTrace);
    }
  }
  
  /*private void infoLog(
      Map<String, Map<String, List<Map<String, Object>>>> klassInstanceRolesMapping)
  {
    String functionName = "notifyRolesOfEachKlassInstance";
    String className = "SaveKlassInstance";
    try {
      if (klassInstanceRolesMapping != null) {
        String modifiedFieldInfo = ObjectMapperUtil.writeValueAsString(klassInstanceRolesMapping);
        logger.info("Interactor_" + className, functionName, "request", modifiedFieldInfo);
      }
      else {
        logger.info("Interactor_" + className, functionName, "request", null);
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      String errorStackTrace = CSLogUtil.getStackTrace(e);
      errorLog(e.getMessage(), errorStackTrace);
    }
  
  };*/
  
  /* private void errorLog(String errorMessage, Object errorStackTrace)
  {
    String functionName = "notifyRolesOfEachKlassInstance";
    String className = "SaveKlassInstance";
    if (errorStackTrace != null) {
      logger.error("Interactor_" + className, functionName, errorMessage, errorStackTrace);
    }
    else {
      logger.error("Interactor_" + className, functionName, errorMessage, null);
    }
  };*/
  
  public IListModel<IKlass> fetchKlassesFromNeo4j(
      IListModel<? extends IKlassInstance> listOfKlassInstancesToReturn) throws Exception
  {
    Set<String> setOfKlasses = new HashSet<>();
    
    for (IKlassInstance klassInstance : listOfKlassInstancesToReturn.getList()) {
      setOfKlasses.addAll(klassInstance.getTypes());
    }
    
    List<String> listOfKlassesToFetch = new ArrayList<>(setOfKlasses);
    
    IIdsListParameterModel listOfParameterModelKlassesToFetch = new IdsListParameterModel();
    listOfParameterModelKlassesToFetch.setIds(listOfKlassesToFetch);
    
    return neo4jGetKlassesByIdsStrategy.execute(listOfParameterModelKlassesToFetch);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, Map<String, List>> createKlassAndItsContainingAttributesTagsRolesMapping(
      IListModel<IKlass> listOfKlasses)
  {
    Map<String, Map<String, List>> outerMap = new HashMap<>();
    
    for (IKlass klassModel : listOfKlasses.getList()) {
      List<String> listOfAttributesIds = new ArrayList<>();
      List<String> listOfTagIds = new ArrayList<>();
      List<String> listOfRoleIds = new ArrayList<>();
      Map<String, List> mappingOfKlassIdsAntThereRespectives = new HashMap<>();
      
      List<ISection> listOfSections = (List<ISection>) klassModel.getSections();
      for (ISection section : listOfSections) {
        for (ISectionElement sectionElement : section.getElements()) {
          if (sectionElement instanceof ISectionAttribute) {
            listOfAttributesIds.add(((ISectionAttribute) sectionElement).getAttribute()
                .getId());
          }
          else if (sectionElement instanceof ISectionTag) {
            listOfTagIds.add(((ISectionTag) sectionElement).getTag()
                .getId());
          }
          else if (sectionElement instanceof ISectionRole) {
            listOfRoleIds.add(((ISectionRole) sectionElement).getRole()
                .getId());
          }
        }
      }
      mappingOfKlassIdsAntThereRespectives.put("Attributes", listOfAttributesIds);
      mappingOfKlassIdsAntThereRespectives.put("Tags", listOfTagIds);
      mappingOfKlassIdsAntThereRespectives.put("Roles", listOfRoleIds);
      outerMap.put(klassModel.getId(), mappingOfKlassIdsAntThereRespectives);
    }
    return outerMap;
  }
  
  public void removeUnncecessaryAttributesTagsRolesFromKlassInstance(
      IListModel<? extends IKlassInstance> listOfKlassInstancesToReturn,
      Map<String, Map<String, List>> outerMap)
  {
    
    for (IKlassInstance klassInstance : listOfKlassInstancesToReturn.getList()) {
      // TGP
      Map<String, List> retieveMapOfLists = outerMap.get(klassInstance.getTypes()
          .get(0));
      
      List<IContentAttributeInstance> listOfFinalizedAttributes = new ArrayList<>();
      List<String> listOfAttributesIds = retieveMapOfLists.get("Attributes");
      for (IContentAttributeInstance iContentAttributeInstance : klassInstance.getAttributes()) {
        if (listOfAttributesIds.contains(iContentAttributeInstance.getAttributeId())) {
          listOfFinalizedAttributes.add(iContentAttributeInstance);
        }
      }
      
      List<IContentTagInstance> listOfFinalizedTags = new ArrayList<>();
      List<String> listOfTagIds = retieveMapOfLists.get("Tags");
      for (IContentTagInstance iTagInstance : klassInstance.getTags()) {
        
        if (listOfTagIds.contains(iTagInstance.getTagId())) {
          listOfFinalizedTags.add(iTagInstance);
        }
      }
      
      List<IRoleInstance> listOfFinalizedRoles = new ArrayList<>();
      List<String> listOfRoleIds = retieveMapOfLists.get("Roles");
      
      for (IRoleInstance iRoleInstance : klassInstance.getRoles()) {
        
        if (listOfRoleIds.contains(iRoleInstance.getRoleId())) {
          listOfFinalizedRoles.add(iRoleInstance);
        }
      }
      
      klassInstance.setAttributes(listOfFinalizedAttributes);
      klassInstance.setTags(listOfFinalizedTags);
      klassInstance.setRoles(listOfFinalizedRoles);
    }
  }
  
  @Deprecated
  @SuppressWarnings("unchecked")
  public void addDeltaOfPropertiesForTypeChange(IKlassInstanceSaveModel klassInstanceSaveModel,
      IKlassInstance articleInstance, IKlass typeKlass) throws Exception
  {
  }
  
  private static Map<String, String> getPropertyIdToMappingId(List<ISection> sections)
  {
    Map<String, String> mapToReturn = new HashMap<String, String>();
    for (ISection section : sections) {
      List<ISectionElement> elements = section.getElements();
      for (ISectionElement element : elements) {
        if (element instanceof ISectionAttribute) {
          IAttribute attribute = ((ISectionAttribute) element).getAttribute();
          mapToReturn.put(attribute.getId(), element.getId());
        }
        else if (element instanceof ISectionTag) {
          ITag tag = ((ISectionTag) element).getTag();
          mapToReturn.put(tag.getId(), element.getId());
        }
      }
    }
    return mapToReturn;
  }
  
  private static List<String> getMappingIdsForIds(List<String> mappingIdOfModifiedFields,
      Map<String, String> idMappingIdMap)
  {
    List<String> returnList = new ArrayList<>();
    for (String mappingIdOfModifiedField : mappingIdOfModifiedFields) {
      String mappingId = idMappingIdMap.get(mappingIdOfModifiedField);
      if (mappingId != null) {
        returnList.add(mappingId);
      }
    }
    return returnList;
  }
  
  /**
   * @param klassInstance
   *          -
   * @param mode
   *          - PIM, MAM, TARGET
   * @param standardKlassId
   *          - SystemLevelIds for standard classes
   * @return
   * @throws Exception
   */
  public IGetDefaultKlassesModel getDefaultKlasses(IKlassInstance klassInstance, String mode,
      String standardKlassId) throws Exception
  {
    String typeKlassId = "-1";
    /*if (klassInstance != null && klassInstance.getType() != null && !klassInstance.getType()
        .equals("")) {
      typeKlassId = klassInstance.getType();
    }*/
    
    IGetAllowedTypesModel allowedTypesModel = new GetAllowedTypesModel();
    allowedTypesModel.setMode(mode);
    allowedTypesModel.setStandardKlassId(standardKlassId);
    allowedTypesModel.setId(typeKlassId);
    IGetDefaultKlassesModel defaultTypes = getDefaultKlassesStrategy.execute(allowedTypesModel);
    return defaultTypes;
  }
  
  /**
   * @param klassInstance
   *          -
   * @param mode
   *          - PIM, MAM, TARGET
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<String> getAllowedTypes(String mode, String standardKlassId) throws Exception
  {
    IGetAllowedTypesModel allowedTypesModel = new GetAllowedTypesModel();
    allowedTypesModel.setMode(mode);
    allowedTypesModel.setStandardKlassId(standardKlassId);
    
    IListModel<String> allowedTypes = getAllowedTypesStrategy.execute(allowedTypesModel);
    return (List<String>) allowedTypes.getList();
  }
  
  public static List<String> getStandardKlassIds(List<String> entities)
  {
    List<String> returnList = new ArrayList<>();
    for (String entity : entities) {
      switch (entity) {
        case Constants.ARTICLE_INSTANCE_MODULE_ENTITY:
          returnList.add(SystemLevelIds.ARTICLE);
          break;
        case Constants.ASSET_INSTANCE_MODULE_ENTITY:
          returnList.add(SystemLevelIds.ASSET);
          break;
        case Constants.MARKET_INSTANCE_MODULE_ENTITY:
          returnList.add(SystemLevelIds.MARKET);
          break;
        case Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY:
          returnList.add(SystemLevelIds.TEXT_ASSET);
          break;
        case Constants.SUPPLIER_INSTANCE_MODULE_ENTITY:
          returnList.add(SystemLevelIds.SUPPLIER);
          break;
        case Constants.FILE_INSTANCE_MODULE_ENTITY:
          returnList.add(SystemLevelIds.FILE);
          break;
      }
    }
    
    return returnList;
  }
  
  public static String getModuleForKlassType(String klassType)
  {
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
      case Constants.PROJECT_SET_KLASS_TYPE:
      case Constants.ASSET_KLASS_TYPE:
      case Constants.MARKET_KLASS_TYPE:
    }
    return null;
  }
  
  public void addDeltaOfPropertiesForTypeChange(IKlassInstanceSaveModel klassInstanceSaveModel,
      IKlassInstance articleInstance, IGetConfigDetailsModel multiClassificationDetails,
      List<String> removedTypeIds, String addedTypeId, Boolean isTaxonomyAddedOrRemoved)
      throws Exception
  {
    List<IContentAttributeInstance> attributeInstancesToAdd = new ArrayList<>();
    List<IModifiedContentAttributeInstanceModel> modifiedAttributeInstances = new ArrayList<>();
    List<IRoleInstance> roleInstancesToAdd = new ArrayList<>();
    List<ITagInstance> tagInstancesToAdd = new ArrayList<>();
    List<IModifiedContentTagInstanceModel> modifiedTagInstances = new ArrayList<>();
    
    List<String> deletedTagInstances = new ArrayList<>();
    List<String> deletedRoleInstances = new ArrayList<>();
    List<String> deletedAttributeInstances = new ArrayList<>();
    
    Map<String, ITag> referencedTags = multiClassificationDetails.getReferencedTags();
    Map<String, IRole> referencedRoles = multiClassificationDetails.getReferencedRoles();
    Map<String, IAttribute> referencedAttributes = multiClassificationDetails
        .getReferencedAttributes();
    Map<String, List<IReferencedSectionElementModel>> referencedElements = ((IConfigDetailsForSwitchTypeResponseModel) multiClassificationDetails)
        .getReferencedElementsForSwitchType();
    Map<String, List<IReferencedSectionElementModel>> referencedElementsCopy = new HashMap<>(
        referencedElements);
    
    /*
    IDerivablePropertiesModel derivableProperties = multiClassificationDetails.getDerivableProperties();
    List<String> derivableAttributeIds = derivableProperties.getDerivableAttributeIds();
    List<String> derivableTagIds = derivableProperties.getDerivableTagIds();
    */
    
    // Set<String> addedTagIds = new HashSet<>(referencedTags.keySet());
    Set<String> addedRoleIds = new HashSet<>(referencedRoles.keySet());
    // Set<String> addedAttributeIds = new
    // HashSet<>(referencedAttributes.keySet());
    
    for (IContentAttributeInstance iContentAttributeInstance : articleInstance.getAttributes()) {
      if (iContentAttributeInstance instanceof ImageAttributeInstance) {
        continue;
      }
      Boolean isAttributeInstanceModified = false;
      String attributeId = iContentAttributeInstance.getAttributeId();
      
      IAttribute iAttribute = referencedAttributes.get(attributeId);
      String attributeInstanceId = iContentAttributeInstance.getId();
      if (iAttribute == null && mandatoryAttributes.contains(attributeId)) {
        deletedAttributeInstances.add(attributeInstanceId);
      }
      else if (iAttribute != null) {
        // addedAttributeIds.remove(attributeId);
        IAttributeInstance attributeInstance = (IAttributeInstance) iContentAttributeInstance;
        List<IReferencedSectionElementModel> referencedElement = referencedElementsCopy
            .remove(attributeId);
        if (addedTypeId != null && !addedTypeId.isEmpty() && referencedElement != null) {
          for (IReferencedSectionElementModel element : referencedElement) {
            String sourceType = ((IReferencedSectionForSwitchTypeModel) element).getSourceType();
            String couplingType = element.getCouplingType();
            String defaultValue = ((ISectionAttribute) element).getDefaultValue();
            String defaultValueAsHtml = ((ISectionAttribute) element).getValueAsHtml();
            if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)
                || ((defaultValue != null && !defaultValue.isEmpty())
                    || (defaultValueAsHtml != null && !defaultValueAsHtml.isEmpty()))) {
              IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
              conflictingValue.setCouplingType(couplingType);
              conflictingValue.setValue(defaultValue);
              conflictingValue.setValueAsHtml(defaultValueAsHtml);
              IConflictingValueSource conflictingValueSource = new KlassConflictingValueSource();
              if (sourceType.equals(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE)) {
                conflictingValueSource = new KlassConflictingValueSource();
              }
              else {
                conflictingValueSource = new TaxonomyConflictingValueSource();
              }
              conflictingValueSource
                  .setId(((IReferencedSectionForSwitchTypeModel) element).getSourceId());
              conflictingValueSource.setType(sourceType);
              conflictingValue.setSource(conflictingValueSource);
              attributeInstance.getConflictingValues()
                  .add(conflictingValue);
              attributeInstance.setLastModified(System.currentTimeMillis());
              isAttributeInstanceModified = true;
            }
          }
        }
      }
      if (!removedTypeIds.isEmpty()) {
        List<IAttributeConflictingValue> attributeConflictingValues = iContentAttributeInstance
            .getConflictingValues();
        List<IAttributeConflictingValue> attributeConflictingValuesToRemove = new ArrayList<>();
        for (IAttributeConflictingValue iAttributeConflictingValue : attributeConflictingValues) {
          IConflictingValueSource attributeValueSource = iAttributeConflictingValue.getSource();
          String sourceId = attributeValueSource.getId();
          if (removedTypeIds.contains(sourceId)) {
            attributeConflictingValuesToRemove.add(iAttributeConflictingValue);
            isAttributeInstanceModified = true;
          }
        }
        if (!attributeConflictingValuesToRemove.isEmpty()) {
          attributeConflictingValues.removeAll(attributeConflictingValuesToRemove);
          iContentAttributeInstance.setNotification(new HashMap<>());
        }
      }
      if (isAttributeInstanceModified) {
        IModifiedAttributeInstanceModel modifiedAttributeInstanceModel = new ModifiedAttributeInstanceModel(
            (IAttributeInstance) iContentAttributeInstance);
        modifiedAttributeInstanceModel.setIsConflictResolved(false);
        modifiedAttributeInstances.add(modifiedAttributeInstanceModel);
      }
    }
    
    for (IContentTagInstance iContentTagInstance : articleInstance.getTags()) {
      Boolean isTagInstanceModified = false;
      String tagId = iContentTagInstance.getTagId();
      ITag iTag = referencedTags.get(tagId);
      if (iTag == null) {
        deletedTagInstances.add(iContentTagInstance.getId());
      }
      else {
        // addedTagIds.remove(tagId);
        ITagInstance tagInstance = (ITagInstance) iContentTagInstance;
        List<IReferencedSectionElementModel> referencedElement = referencedElementsCopy
            .remove(tagId);
        if (addedTypeId != null && !addedTypeId.isEmpty() && referencedElement != null) {
          for (IReferencedSectionElementModel element : referencedElement) {
            String sourceType = ((IReferencedSectionForSwitchTypeModel) element).getSourceType();
            String couplingType = element.getCouplingType();
            List<IIdRelevance> defaultValues = ((ISectionTag) element).getDefaultValue();
            if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)
                || (defaultValues != null && !defaultValues.isEmpty())) {
              ITagConflictingValue conflictingValue = new TagConflictingValue();
              conflictingValue.setCouplingType(couplingType);
              conflictingValue.setTagValues(defaultValues);
              IConflictingValueSource conflictingValueSource = new KlassConflictingValueSource();
              if (sourceType.equals(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE)) {
                conflictingValueSource = new KlassConflictingValueSource();
              }
              else {
                conflictingValueSource = new TaxonomyConflictingValueSource();
              }
              conflictingValueSource
                  .setId(((IReferencedSectionForSwitchTypeModel) element).getSourceId());
              conflictingValueSource.setType(sourceType);
              conflictingValue.setSource(conflictingValueSource);
              tagInstance.getConflictingValues()
                  .add(conflictingValue);
              isTagInstanceModified = true;
            }
          }
        }
      }
      if (!removedTypeIds.isEmpty()) {
        List<ITagConflictingValue> tagConflictingValues = iContentTagInstance
            .getConflictingValues();
        List<ITagConflictingValue> tagConflictingValuesToRemove = new ArrayList<>();
        for (ITagConflictingValue iTagConflictingValue : tagConflictingValues) {
          IConflictingValueSource tagValueSource = iTagConflictingValue.getSource();
          String sourceId = tagValueSource.getId();
          if (removedTypeIds.contains(sourceId)) {
            tagConflictingValuesToRemove.add(iTagConflictingValue);
            isTagInstanceModified = true;
          }
        }
        if (!tagConflictingValuesToRemove.isEmpty()) {
          tagConflictingValues.removeAll(tagConflictingValuesToRemove);
          iContentTagInstance.setNotification(new HashMap<>());
        }
      }
      if (isTagInstanceModified) {
        IModifiedTagInstanceModel modifiedTagInstanceModel = new ModifiedTagInstanceModel(
            (ITagInstance) iContentTagInstance);
        modifiedTagInstanceModel.setIsConflictResolved(false);
        modifiedTagInstances.add(modifiedTagInstanceModel);
      }
    }
    
    for (Map.Entry<String, List<IReferencedSectionElementModel>> entry : referencedElementsCopy
        .entrySet()) {
      String entityId = entry.getKey();
      List<IReferencedSectionElementModel> elementList = entry.getValue();
      String type = null;
      if (elementList.isEmpty()) {
        continue;
      }
      
      type = elementList.get(0)
          .getType();
      
      if (type.equals("attribute")) {
        if (entityId.equals(IStandardConfig.StandardProperty.assetcoverflowattribute.toString())) {
          continue;
        }
        
        IAttribute iAttribute = referencedAttributes.get(entityId);
        IContentAttributeInstance attributeInstance = null;
        for (IReferencedSectionElementModel element : elementList) {
          String typeId = ((IReferencedSectionForSwitchTypeModel) element).getSourceId();
          if (removedTypeIds.contains(typeId)) {
            continue;
          }
          String sourceType = ((IReferencedSectionForSwitchTypeModel) element).getSourceType();
          
          if (attributeInstance == null) {
            String attributeContextId = element.getAttributeVariantContext();
            if (attributeContextId == null) {
              attributeInstance = createAttributeInstance(typeId, null, iAttribute,
                  ((ISectionAttribute) element).getDefaultValue(), element.getCouplingType(),
                  sourceType, ((ISectionAttribute) element).getValueAsHtml());
              attributeInstancesToAdd.add(attributeInstance);
            }
          }
          else {
            String couplingType = element.getCouplingType();
            String defaultValue = ((ISectionAttribute) element).getDefaultValue();
            String defaultValueAsHtml = ((ISectionAttribute) element).getValueAsHtml();
            if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)
                || ((defaultValue != null && !defaultValue.isEmpty())
                    || (defaultValueAsHtml != null && !defaultValueAsHtml.isEmpty()))) {
              IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
              conflictingValue.setCouplingType(couplingType);
              conflictingValue.setValue(defaultValue);
              conflictingValue.setValueAsHtml(defaultValueAsHtml);
              IConflictingValueSource conflictingValueSource = new KlassConflictingValueSource();
              if (sourceType.equals(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE)) {
                conflictingValueSource = new KlassConflictingValueSource();
              }
              else {
                conflictingValueSource = new TaxonomyConflictingValueSource();
              }
              conflictingValueSource.setId(typeId);
              conflictingValueSource.setType(sourceType);
              conflictingValue.setSource(conflictingValueSource);
              attributeInstance.getConflictingValues()
                  .add(conflictingValue);
            }
          }
        }
      }
      else if (type.equals("tag")) {
        ITag iTag = referencedTags.get(entityId);
        ITagInstance tagInstance = null;
        
        for (IReferencedSectionElementModel element : elementList) {
          String typeId = ((IReferencedSectionForSwitchTypeModel) element).getSourceId();
          if (removedTypeIds.contains(typeId)) {
            continue;
          }
          String sourceType = ((IReferencedSectionForSwitchTypeModel) element).getSourceType();
          if (element.getCouplingType()
              .equals(CommonConstants.DYNAMIC_COUPLED)
              || (((ISectionTag) element).getDefaultValue() != null)) {
            ITagConflictingValue conflictingValue = new TagConflictingValue();
            conflictingValue.setCouplingType(element.getCouplingType());
            conflictingValue.setTagValues(((ISectionTag) element).getDefaultValue());
            IConflictingValueSource conflictingValueSource = new KlassConflictingValueSource();
            if (sourceType.equals(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE)) {
              conflictingValueSource = new KlassConflictingValueSource();
            }
            else {
              conflictingValueSource = new TaxonomyConflictingValueSource();
            }
            conflictingValueSource.setId(typeId);
            conflictingValueSource.setType(sourceType);
            conflictingValue.setSource(conflictingValueSource);
            if (tagInstance == null) {
              tagInstance = new TagInstance();
              tagInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
              tagInstance.setTagId(iTag.getId());
              addTagsInContentBasedOnTagType(iTag, tagInstance, (ISectionTag) element);
              tagInstancesToAdd.add(tagInstance);
            }
            List<ITagConflictingValue> listOfConflictingValues = tagInstance.getConflictingValues();
            if (listOfConflictingValues == null) {
              listOfConflictingValues = new ArrayList<>();
            }
            listOfConflictingValues.add(conflictingValue);
          }
        }
      }
    }
    
    for (IRoleInstance iContentRoleInstance : articleInstance.getRoles()) {
      String roleId = iContentRoleInstance.getRoleId();
      IRole iRole = referencedRoles.get(roleId);
      if (iRole == null) {
        deletedRoleInstances.add(iContentRoleInstance.getId());
      }
      else {
        addedRoleIds.remove(roleId);
      }
    }
    
    for (String roleId : addedRoleIds) {
      IRole iRole = referencedRoles.get(roleId);
      IRoleInstance roleInstance = new RoleInstance();
      roleInstance.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ROLE.getPrefix()));
      roleInstance.setRoleId(iRole.getId());
      if (iRole.getId()
          .equals(SystemLevelIds.OWNER_ROLE)) {
        IIdParameterModel idModel = new IdParameterModel(context.getUserId());
        IUserModel owner = getUserStrategy.execute(idModel);
        List<IRoleCandidate> candidates = new ArrayList<>();
        candidates.add((IRoleCandidate) owner.getEntity());
        roleInstance.setCandidates(candidates);
      }
      roleInstancesToAdd.add(roleInstance);
    }
    
    klassInstanceSaveModel.setAddedAttributes(attributeInstancesToAdd);
    klassInstanceSaveModel.setAddedRoles(roleInstancesToAdd);
    klassInstanceSaveModel.setAddedTags(tagInstancesToAdd);
    klassInstanceSaveModel.setModifiedAttributes(modifiedAttributeInstances);
    klassInstanceSaveModel.setModifiedTags(modifiedTagInstances);
    // klassInstanceSaveModel.setDeletedAttributes(deletedAttributeInstances);
    // klassInstanceSaveModel.setDeletedRoles(deletedRoleInstances);
    // klassInstanceSaveModel.setDeletedTags(deletedTagInstances);
  }
  
  public static String getStandardKlassIds(String klassType)
  {
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
        return SystemLevelIds.ARTICLE;
      case Constants.ASSET_KLASS_TYPE:
        return SystemLevelIds.ASSET;
      case Constants.MARKET_KLASS_TYPE:
        return SystemLevelIds.MARKET;
      case Constants.TEXT_ASSET_KLASS_TYPE:
        return SystemLevelIds.TEXT_ASSET;
      case Constants.SUPPLIER_KLASS_TYPE:
        return SystemLevelIds.SUPPLIER;
    }
    
    return null;
  }
  
  public static void removeLinkedOrUnlinkedRelationships(
      IGetMultiClassificationKlassDetailsModel multiClassficationDetails, Boolean isLinked)
  {
    if (!isLinked) {
      return;
    }
    Map<String, IRelationship> referencedRelationships = multiClassficationDetails
        .getReferencedRelationships();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = multiClassficationDetails
        .getReferencedNatureRelationships();
    Map<String, IReferencedSectionElementModel> referencedElements = multiClassficationDetails
        .getReferencedElements();
    Map<String, String> referencedRelationshipsMapping = multiClassficationDetails
        .getReferencedRelationshipsMapping();
    List<String> keySet = new ArrayList<String>(referencedRelationships.keySet());
    
    for (String key : keySet) {
      IReferencedSectionRelationshipModel element = (IReferencedSectionRelationshipModel) referencedElements
          .get(key);
      if (!isLinked.equals(element.getIsLinked())) {
        referencedRelationships.remove(key);
        referencedElements.remove(key);
        referencedRelationshipsMapping.remove(key);
      }
    }
    keySet = new ArrayList<String>(referencedNatureRelationships.keySet());
    for (String key : keySet) {
      IReferencedSectionRelationshipModel element = (IReferencedSectionRelationshipModel) referencedElements
          .get(key);
      if (!isLinked.equals(element.getIsLinked())) {
        referencedNatureRelationships.remove(key);
        referencedElements.remove(key);
      }
    }
  }
  
  public static void setStandardKlassInstanceProperties(IKlassInstance klassInstance)
  {
    List<? extends IContentAttributeInstance> attributes = klassInstance.getAttributes();
    for (IContentAttributeInstance iContentAttributeInstance : attributes) {
      if (iContentAttributeInstance.getAttributeId()
          .equals(IStandardConfig.StandardProperty.createdonattribute.toString())) {
        String createdOn = "";
        if (iContentAttributeInstance instanceof IAttributeInstance) {
          createdOn = ((IAttributeInstance) iContentAttributeInstance).getValue();
        }
        klassInstance.setCreatedOn(Long.valueOf(createdOn));
      }
      else if (iContentAttributeInstance.getAttributeId()
          .equals(IStandardConfig.StandardProperty.lastmodifiedattribute.toString())) {
        String lastModifiedOn = "";
        if (iContentAttributeInstance instanceof IAttributeInstance) {
          lastModifiedOn = ((IAttributeInstance) iContentAttributeInstance).getValue();
        }
        klassInstance.setLastModified(Long.valueOf(lastModifiedOn));
      }
      else if (iContentAttributeInstance.getAttributeId()
          .equals(IStandardConfig.StandardProperty.lastmodifiedbyattribute.toString())) {
        String lastModifiedBy = "";
        if (iContentAttributeInstance instanceof IAttributeInstance) {
          lastModifiedBy = ((IAttributeInstance) iContentAttributeInstance).getValue();
        }
        klassInstance.setLastModifiedBy(lastModifiedBy);
      }
      else if (iContentAttributeInstance.getAttributeId()
          .equals(IStandardConfig.StandardProperty.createdbyattribute.toString())) {
        String createdBy = "";
        if (iContentAttributeInstance instanceof IAttributeInstance) {
          createdBy = ((IAttributeInstance) iContentAttributeInstance).getValue();
        }
        klassInstance.setCreatedBy(createdBy);
      }
    }
  }
  
  public static IGetInstanceRequestStrategyModel getInstanceRequestStrategyModel(String tabType,
      String id, String templateId, IGetConfigDetailsModel configDetails, Integer from,
      Integer size, List<String> languagesToCompare)
  {
    String contextId = null;
    IGetInstanceRequestStrategyModel getInstanceRequestStrategymodel = null;
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = null;
    
    switch (tabType) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        if (languagesToCompare.isEmpty()) {
          getInstanceRequestStrategymodel = new GetInstanceRequestStrategyModelForCustomTab();
        }
        else {
          getInstanceRequestStrategymodel = new LanguageComparisonRequestModel();
        }
        
        contextId = ((IGetConfigDetailsForCustomTabModel) configDetails).getContextId();
        getInstanceRequestStrategymodel.setChildContextId(contextId);
        referencedNatureRelationships = ((IGetConfigDetailsForCustomTabModel) configDetails)
            .getReferencedNatureRelationships();
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedNatureRelationships(referencedNatureRelationships);
        
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedVariantContexts(((IGetConfigDetailsForCustomTabModel) configDetails)
                .getReferencedVariantContexts());
        for (IGetReferencedNatureRelationshipModel referencedNatureRelationshipModel : referencedNatureRelationships
            .values()) {
          String relationshipType = referencedNatureRelationshipModel.getRelationshipType();
          if (relationshipType != null
              && relationshipType.equals(CommonConstants.TARGET_PROMOTION_RELATIONSHIP)) {
            ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
                .setContextTagsForTargetRelationship(
                    referencedNatureRelationshipModel.getContextTags());
            break;
          }
        }
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedNatureRelationshipsIds(
                new ArrayList<>(referencedNatureRelationships.keySet()));
        Map<String, IReferencedRelationshipModel> referencedRelationships = ((IGetConfigDetailsForCustomTabModel) configDetails)
            .getReferencedRelationships();
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedRelationshipsIds(new ArrayList<>(referencedRelationships.keySet()));
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedLifeCycleStatusTags(((IGetConfigDetailsForCustomTabModel) configDetails)
                .getReferencedLifeCycleStatusTags());
        getInstanceRequestStrategymodel.setChildContextId(contextId);
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedVariantContexts(((IGetConfigDetailsForCustomTabModel) configDetails)
                .getReferencedVariantContexts());
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedAttributes(configDetails.getReferencedAttributes());
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedTags(configDetails.getReferencedTags());
        ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
            .setReferencedElements(configDetails.getReferencedElements());
        
        
        return fillMandatoryInformationToStrategyModel(id, templateId, configDetails, from, size,
            getInstanceRequestStrategymodel);
      
      case CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE:
        getInstanceRequestStrategymodel = new GetInstanceRequestStrategyModelForTasksTab();
        return fillMandatoryInformationToStrategyModel(id, templateId, configDetails, from, size,
            getInstanceRequestStrategymodel);
      
      case CommonConstants.TEMPLATE_TIME_LINE_TAB_BASETYPE:
        getInstanceRequestStrategymodel = new GetInstanceRequestStrategyModelForCustomTab();
        return fillMandatoryInformationToStrategyModel(id, templateId, configDetails, from, size,
            getInstanceRequestStrategymodel);
    }
    return getInstanceRequestStrategymodel;
  }
  
  private static IGetInstanceRequestStrategyModel fillMandatoryInformationToStrategyModel(String id,
      String templateId, IGetConfigDetailsModel configDetails, Integer from, Integer size,
      IGetInstanceRequestStrategyModel getInstanceRequestStrategymodel)
  {
    getInstanceRequestStrategymodel.setId(id);
    getInstanceRequestStrategymodel.setFrom(from);
    getInstanceRequestStrategymodel.setSize(size);
    getInstanceRequestStrategymodel.setReferencedKlasses(configDetails.getReferencedKlasses());
    getInstanceRequestStrategymodel.setTemplateId(templateId);
    
    return getInstanceRequestStrategymodel;
  }
  
  /**
   * @param ruleViolations
   * @param referencedAttributes
   * @author Kshitij
   *         <p>
   *         Temporary implementation for showing overridden Calculated
   *         Attribute Unit in Normalization
   */
  public void replaceCalculatedAttributeUnitInReferencedCustomCalculatedAttributes(
      List<IRuleViolation> ruleViolations, Map<String, IAttribute> referencedAttributes)
  {
    List<IRuleViolation> ruleViolationsToDelete = new ArrayList<>();
    for (IRuleViolation ruleViolation : ruleViolations) {
      String calculatedAttributeUnit = ruleViolation.getCalculatedAttributeUnit();
      if (calculatedAttributeUnit != null && !calculatedAttributeUnit.isEmpty()) {
        ICalculatedAttribute referencedAttribute = (ICalculatedAttribute) referencedAttributes
            .get(ruleViolation.getEntityId());
        referencedAttribute.setCalculatedAttributeUnit(calculatedAttributeUnit);
        referencedAttribute
            .setCalculatedAttributeUnitAsHTML(ruleViolation.getCalculatedAttributeUnitAsHTML());
        ruleViolationsToDelete.add(ruleViolation);
      }
    }
    ruleViolations.removeAll(ruleViolationsToDelete);
  }
  
  public ITagInstance createTagInstance(String typeKlassId, ISectionTag sectionTag, ITag masterTag)
  {
    ITagInstance tagInstance = new TagInstance();
    return tagInstance;
  }
  
  public void checkAndCreateFileNameAttributeInstance(IKlassInstance klassInstance) throws Exception
  {
    List<IContentAttributeInstance> attributes = (List<IContentAttributeInstance>) klassInstance
        .getAttributes();
    Boolean isFileNameAttributeCreated = false;
    for (IContentAttributeInstance attribute : attributes) {
      if (attribute.getAttributeId()
          .equals(IStandardConfig.StandardProperty.filenameattribute.toString())) {
        isFileNameAttributeCreated = true;
        break;
      }
    }
    
    if (!isFileNameAttributeCreated) {
      IAttributeInstance fileNameAttribute = new AttributeInstance();
      fileNameAttribute.setId( RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
      fileNameAttribute.setBaseType(Constants.ATTRIBUTE_INSTANCE_PROPERTY_TYPE);
      fileNameAttribute
          .setAttributeId(IStandardConfig.StandardProperty.filenameattribute.toString());
      fileNameAttribute.setValue("");
      fileNameAttribute.setConflictingValues(new ArrayList<>());
      attributes.add(fileNameAttribute);
    }
  }
  
  /**
   * This function returns attributeInstances of all parametered attributeIds
   *
   * @author Lokesh
   * @param attributeIds
   * @param referencedAttributes
   * @param referencedElement
   * @param klassId
   * @return
   * @throws Exception 
   */
  public List<IContentAttributeInstance> prepareAttributeInstances(List<String> attributeIds,
      Map<String, IAttribute> referencedAttributes,
      Map<String, IReferencedSectionElementModel> referencedElement, String klassId) throws Exception
  {
    List<IContentAttributeInstance> returnList = new ArrayList<>();
    for (String attributeId : attributeIds) {
      IAttribute attribute = referencedAttributes.get(attributeId);
      IReferencedSectionAttributeModel element = (IReferencedSectionAttributeModel) referencedElement
          .get(attributeId);
      String defaultValue = element.getDefaultValue();
      String couplingType = element.getCouplingType();
      IContentAttributeInstance attributeInstance = createAttributeInstance(klassId, null,
          attribute, defaultValue, couplingType, CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE,
          element.getValueAsHtml());
      
      returnList.add(attributeInstance);
    }
    return returnList;
  }
  
  /**
   * This function returns tagInstances of all parametered tagIds
   *
   * @author Lokesh
   * @param tagIds
   * @param referencedTags
   * @param referencedElement
   * @param klassId
   * @return
   * @throws Exception 
   */
  public List<ITagInstance> prepareTagsInstances(List<String> tagIds,
      Map<String, ITag> referencedTags,
      Map<String, IReferencedSectionElementModel> referencedElement, String klassId) throws Exception
  {
    List<ITagInstance> returnList = new ArrayList<>();
    for (String tagId : tagIds) {
      ITag tag = referencedTags.get(tagId);
      IReferencedSectionTagModel element = (IReferencedSectionTagModel) referencedElement
          .get(tagId);
      
      ITagInstance tagInstance = createNewTagInstance("", klassId, element, tag);
      returnList.add(tagInstance);
    }
    return returnList;
  }
  
  @SuppressWarnings("rawtypes")
  public static IGetFilterAndSortDataRequestModel prepareSortAndFilterDataRequestModel(
      IInstanceSearchStrategyModel getKlassInstanceTreeStrategyModel)
  {
    IGetFilterAndSortDataRequestModel filterAndSortDataRequestModel = new GetFilterAndSortDataRequestModel();
    filterAndSortDataRequestModel.setModuleId(getKlassInstanceTreeStrategyModel.getModuleId());
    List<? extends IPropertyInstanceFilterModel> attributes = getKlassInstanceTreeStrategyModel
        .getAttributes();
    List<String> attributeIds = new ArrayList<>();
    for (IPropertyInstanceFilterModel attribute : attributes) {
      attributeIds.add(attribute.getId());
    }
    filterAndSortDataRequestModel.setAttributeIds(attributeIds);
    List<? extends IPropertyInstanceFilterModel> tags = getKlassInstanceTreeStrategyModel.getTags();
    List<String> tagIds = new ArrayList<>();
    for (IPropertyInstanceFilterModel tag : tags) {
      tagIds.add(tag.getId());
    }
    filterAndSortDataRequestModel.setTagIds(tagIds);
    List<IRoleModel> roles = getKlassInstanceTreeStrategyModel.getRoles();
    List<String> roleIds = new ArrayList<>();
    for (IRoleModel role : roles) {
      roleIds.add(role.getId());
    }
    filterAndSortDataRequestModel.setRoleIds(roleIds);
    return filterAndSortDataRequestModel;
  }
  
  /**
   * @author Ajit This method adds referenced klass data for all relationship
   *         elements
   */
  public void fillPostConfigDataForReferencedRelationshipElements(
      IGetKlassInstanceCustomTabModel model) throws Exception
  {
    Set<String> klassIdsSet = new HashSet<>();
    Map<String, List<IKlassInstanceInformationModel>> referenceNatureRelationshipInstanceElements = model
        .getReferenceNatureRelationshipInstanceElements();
    List<IKlassInstanceInformationModel> referencedElementsMasterList = new ArrayList<>();
    for (List<IKlassInstanceInformationModel> referencedElements : referenceNatureRelationshipInstanceElements
        .values()) {
      referencedElementsMasterList.addAll(referencedElements);
    }
    
    Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements = model
        .getReferenceRelationshipInstanceElements();
    for (List<IKlassInstanceInformationModel> referencedElements : referenceRelationshipInstanceElements
        .values()) {
      referencedElementsMasterList.addAll(referencedElements);
    }
    
    for (IKlassInstanceInformationModel referencedElement : referencedElementsMasterList) {
      klassIdsSet.addAll(referencedElement.getTypes());
    }
    
    Map<String, IVariantReferencedInstancesModel> referencedInstances = model
        .getReferencedInstances();
    for (IVariantReferencedInstancesModel referencedInstance : referencedInstances.values()) {
      klassIdsSet.addAll(referencedInstance.getTypes());
    }
    
    for (IKlassInstanceInformationModel referencedElement : model.getReferencedElementInstances()
        .values()) {
      klassIdsSet.addAll(referencedElement.getTypes());
    }
    if (!klassIdsSet.isEmpty()) {
      List<String> klassIdsList = new ArrayList<>(klassIdsSet);
      IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
      requestModel.setKlassIds(klassIdsList);
      requestModel.setShouldGetNonNature(false);
      IGetPostConfigDetailsResponseModel responseModel = postConfigDetailsForRelationshipsStrategy
          .execute(requestModel);
      IGetConfigDetailsModel configDetails = model.getConfigDetails();
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails
          .getReferencedKlasses();
      referencedKlasses.putAll(responseModel.getReferencedKlasses());
    }
  }
  
  public IGetKlassInstanceTypeStrategy getKlassInstanceTypeStrategy(String baseType)
  {
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
      case Constants.ASSET_INSTANCE_BASE_TYPE:
      case Constants.MARKET_INSTANCE_BASE_TYPE:
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return (IGetKlassInstanceTypeStrategy) new KlassInstanceTypeModel();
      
      default:
        return null;
    }
  }
  
  /**
   * @author Lokesh
   * @param requestTime
   * @return
   */
  public Long removeSecondsFromDate(Long requestTime)
  {
    if (requestTime == null) {
      return requestTime;
    }
    Date d1 = new Date(requestTime);
    Calendar calender1 = Calendar.getInstance();
    calender1.setTime(d1);
    calender1.set(Calendar.MILLISECOND, 0);
    calender1.set(Calendar.SECOND, 0);
    return calender1.getTime()
        .getTime();
  }
  
  public void updateAssociatedSearchableInstances(
      IUpdateSearchableInstanceModel updateSearchableInstanceModel) throws Exception
  {
    List<String> searchableInstanceIds = updateSearchableInstanceModel.getSearchableInstanceIds();
    for (String searchableInstanceId : searchableInstanceIds) {
      IUpdateSearchableInstanceRequestModel updateSearchableInstanceRequestModel = new UpdateSearchableInstanceRequestModel();
      updateSearchableInstanceRequestModel.setSearchablePropertyInstancesInformation(
          updateSearchableInstanceModel.getSearchablePropertyInstancesInformation());
      updateSearchableInstanceRequestModel.setSearchableInstanceId(searchableInstanceId);
      //TODO: BGP
    }
  }
  
  public void removeTagValuesWithZeroRelevanceFromConfigDetails(IGetKlassInstanceModel returnModel,
      IGetConfigDetailsModel configDetails)
  {
    Set<String> tagGroupIdsToIgnore = new HashSet<>();
    IReferencedContextModel referencedVariantContexts = configDetails
        .getReferencedVariantContexts();
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getEmbeddedVariantContexts()
            .values());
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getLanguageVariantContexts()
            .values());
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getProductVariantContexts()
            .values());
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getPromotionalVersionContexts()
            .values());
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getRelationshipVariantContexts()
            .values());
    
    if (configDetails instanceof IGetConfigDetailsForCustomTabModel) {
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = ((IGetConfigDetailsForCustomTabModel) configDetails)
          .getReferencedNatureRelationships();
      for (IGetReferencedNatureRelationshipModel natureRelationship : referencedNatureRelationships
          .values()) {
        List<String> contextTags = natureRelationship.getContextTags();
        if (contextTags != null && !contextTags.isEmpty()) {
          tagGroupIdsToIgnore.addAll(contextTags);
        }
      }
    }
    
    Map<String, Set<String>> tagIdsVsTagValueIdsToRetainMap = new HashMap<>();
    
    List<? extends IContentTagInstance> tagInstances = returnModel.getKlassInstance()
        .getTags();
    for (IContentTagInstance tagInstance : tagInstances) {
      Set<String> tagValueIdsToRetain = new HashSet<>();
      List<ITagInstanceValue> tagValues = tagInstance.getTagValues();
      for (ITagInstanceValue tagValue : tagValues) {
        if (!tagValue.getRelevance()
            .equals(0)) {
          tagValueIdsToRetain.add(tagValue.getTagId());
        }
      }
      List<ITagConflictingValue> conflictingValues = tagInstance.getConflictingValues();
      for (ITagConflictingValue conflictingValue : conflictingValues) {
        List<IIdRelevance> conflictingTagValues = conflictingValue.getTagValues();
        for (IIdRelevance conflictingTagValue : conflictingTagValues) {
          tagValueIdsToRetain.add(conflictingTagValue.getTagId());
        }
      }
      tagIdsVsTagValueIdsToRetainMap.put(tagInstance.getTagId(), tagValueIdsToRetain);
    }
    
    // Only filter tag values for types mentioned in list below
    List<String> tagTypes = Arrays.asList(SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID,
        SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID, SystemLevelIds.RANGE_TAG_TYPE_ID);
    
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    List<String> referencedTagsToRemove = new ArrayList<>();
    Set<String> tagsToIterate = new HashSet<>(referencedTags.keySet());
    tagsToIterate.removeAll(tagGroupIdsToIgnore);
    for (String tagGroupId : tagsToIterate) {
      
      ITag referencedTag = referencedTags.get(tagGroupId);
      if (!tagTypes.contains(referencedTag.getTagType())) {
        continue;
      }
      
      Set<String> tagValuesToRetainList = tagIdsVsTagValueIdsToRetainMap.get(tagGroupId);
      if (tagValuesToRetainList == null) {
        referencedTagsToRemove.add(tagGroupId);
        continue;
      }
      
      List<ITag> referencedTagValues = (List<ITag>) referencedTag.getChildren();
      List<ITag> referencedTagValuesToRemove = new ArrayList<>();
      for (ITag referencedTagValue : referencedTagValues) {
        if (!tagValuesToRetainList.contains(referencedTagValue.getId())) {
          referencedTagValuesToRemove.add(referencedTagValue);
        }
      }
      referencedTagValues.removeAll(referencedTagValuesToRemove);
    }
    
    /*for (String tagId : referencedTagsToRemove) {
      referencedTags.remove(tagId);
    }*/
    
  }
  
  private void fillTagGroupIdsToIgnore(Set<String> tagGroupIdsToIgnore,
      Collection<IReferencedVariantContextModel> values)
  {
    for (IReferencedVariantContextModel contextModel : values) {
      List<IReferencedVariantContextTagsModel> tags = contextModel.getTags();
      for (IReferencedVariantContextTagsModel tag : tags) {
        tagGroupIdsToIgnore.add(tag.getTagId());
      }
    }
  }
  
  public String getVariantOfLabel(IBaseEntityDAO baseEntityDAO,
      List<String> linkedVariantCodes)
      throws RDBMSException, Exception
  {
    String variantOfLabel = null;
    if(linkedVariantCodes.isEmpty()) {
      return variantOfLabel;
    }
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Map<Long, Long> variantIdParentIdMap = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getLinkedVariantIds(Collections.singletonList(baseEntityDTO), linkedVariantCodes);
    Long baseEntityIID = baseEntityDTO.getBaseEntityIID();
    if (variantIdParentIdMap.containsKey(baseEntityIID)) {
      Long parentId = variantIdParentIdMap.get(baseEntityIID);
      variantOfLabel = getName(parentId);
    }
    return variantOfLabel;
  }
  
  private String getName(Long baseEntityIID) throws Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
      IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
      return baseEntityDTO.getBaseEntityName();
  }
  
  public static String getBranchOfLabel(IBaseEntityDTO baseEntityDTO, RDBMSComponentUtils rdbmsComponentUtilsParam) throws Exception
  {
    long parentId = baseEntityDTO.getOriginBaseEntityIID();
    String branchOfLabel = "";
    if (parentId != 0 && baseEntityDTO.isClone()) {
      IBaseEntityDTO parentEntityDTO = rdbmsComponentUtilsParam.getBaseEntityDTO(parentId);
      branchOfLabel = parentEntityDTO.getBaseEntityName();
    }
    return branchOfLabel;
  }

  public IGetKlassInstanceModel prepareDataForResponse(IResolveRelationshipConflictsRequestModel dataModel ) throws InterruptedException, Exception
  {
    return  prepareDataForResponse(dataModel.getTabId(), dataModel.getTabType(), dataModel.getTypeId(), dataModel.getTemplateId(), dataModel.getTargetId());
  }
  
  private IGetKlassInstanceModel prepareDataForResponse(String tabId, String tabType, String typeId, String templateId, String entityId)
      throws Exception, InterruptedException
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(entityId));
    IGetInstanceRequestModel instanceRequestModel = new GetInstanceRequestModel();
    instanceRequestModel.setTabId(tabId);
    instanceRequestModel.setTemplateId(templateId);
    instanceRequestModel.setTypeId(typeId);
    IMulticlassificationRequestModel multiclassificationRequestModel = configUtil
        .getConfigRequestModelForCustomTab(instanceRequestModel, baseEntityDAO);
  
    IGetConfigDetailsModel configDetails =  getConfigDetails(multiclassificationRequestModel , tabType);
    
    IGetKlassInstanceCustomTabModel returnModel = new GetKlassInstanceForCustomTabModel();
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    returnModel.setKlassInstance((IContentInstance) klassInstance);
    returnModel.setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
    
    returnModel.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    returnModel.setBranchOfLabel(KlassInstanceUtils.getBranchOfLabel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils));
    returnModel.setConfigDetails(configDetails);
    getConflictAndCoupleType(baseEntityDAO, returnModel,(IGetConfigDetailsForCustomTabModel) configDetails);
    relationshipInstanceUtil.executeGetRelationshipInstance(returnModel, (IGetConfigDetailsForCustomTabModel)configDetails, baseEntityDAO, rdbmsComponentUtils);
    permissionUtils.manageKlassInstancePermissions(returnModel);
    returnModel.setGlobalPermission(configDetails.getReferencedPermissions().getGlobalPermission());
    
    return returnModel;
  }
 
  public IMulticlassificationRequestModel fillMulticlassificationRequestModel(String tabId,
      String typeId, String templateId, TransactionData transactionData,
      IKlassInstanceTypeModel klassInstanceTypes)
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(new ArrayList<String>(klassInstanceTypes.getTypes()));
    multiclassificationRequestModel.setSelectedTaxonomyIds(klassInstanceTypes.getSelectedTaxonomyIds());
    multiclassificationRequestModel.setTemplateId(templateId);
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    multiclassificationRequestModel.setTabId(tabId);
    multiclassificationRequestModel.setTypeId(typeId);
    multiclassificationRequestModel.setParentKlassIds(klassInstanceTypes.getParentKlassIds());
    multiclassificationRequestModel.setParentTaxonomyIds(klassInstanceTypes.getParentTaxonomyIds());
    multiclassificationRequestModel.setLanguageCodes(klassInstanceTypes.getLanguageCodes());
    
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    return multiclassificationRequestModel;
  }

  public IGetConfigDetailsModel getConfigDetails(IMulticlassificationRequestModel model, String tabType) throws Exception
  {
    switch(tabType){
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE :
        return getConfigDetailsForCustomTabStrategy.execute(model);
      default : return  null;
    }
  }
  
  public void getConflictAndCoupleType(IBaseEntityDAO baseEntityDAO, IGetKlassInstanceCustomTabModel returnModel,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    ILocaleCatalogDAO catalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    IRelationCoupleRecordDAO relationCoupleRecordDao = catalogDao.openRelationCoupleRecordDAO();
    IRelationCoupleRecordDTO exsitingRelationCoupleRecord = new RelationCoupleRecordDTOBuilder().targetEntityId(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID()).build();
    StringBuilder filterQuery = relationCoupleRecordDao.getFilterQuery(exsitingRelationCoupleRecord);
    List<IRelationCoupleRecordDTO> relationCoupleRecordDtos = relationCoupleRecordDao.fetchRelationCoupleRecord(filterQuery);
    
    IConfigurationDAO configurationDao = RDBMSAppDriverManager.getDriver().newConfigurationDAO();
    for (IRelationCoupleRecordDTO relationCoupleRecordDto : relationCoupleRecordDtos) {
      IPropertyDTO propogableRelationshipPropertyDTO = configurationDao.getPropertyByIID(relationCoupleRecordDto.getPropagableeRelationshipId());
      IPropertyDTO natureRelationPropertyDto = configurationDao.getPropertyByIID(relationCoupleRecordDto.getNatureRelationshipId());
      IRelationshipConflict relationshipconflict = new RelationshipConflict();
      relationshipconflict.setIsResolved(relationCoupleRecordDto.getIsResolved());
      relationshipconflict.setPropagableRelationshipId(propogableRelationshipPropertyDTO.getPropertyCode());
      relationshipconflict.setPropagableRelationshipSideId(relationCoupleRecordDto.getPropagableeRelationshipSideId());
      IRelationshipConflictingSource relationshipconflictingSource = new RelationshipConflictingSource();
      relationshipconflictingSource.setCouplingType(relationCoupleRecordDto.getCouplingType().toString());
      relationshipconflictingSource.setSourceContentId(String.valueOf(relationCoupleRecordDto.getSourceEntityId()));
      relationshipconflictingSource.setRelationshipId(String.valueOf(natureRelationPropertyDto.getPropertyCode()));
      relationshipconflictingSource.setRelationshipSideId(natureRelationPropertyDto.getCode());
      relationshipconflict.getConflicts().add(relationshipconflictingSource);
      returnModel.getKlassInstance().getRelationshipConflictingValues().add(relationshipconflict);
     
      IReferencedSectionElementModel referenceElement = configDetails.getReferencedElements().get(relationCoupleRecordDto.getPropagableeRelationshipSideId());
      if(referenceElement!=null) {
          referenceElement.setCouplingType(relationCoupleRecordDto.getCouplingType().toString());
      }
    }
   
  }
  
  /** If an Article doesn't have any Default Image, it will assign a new one.
   * @param entityDAO DAO of article whose default image needs to be set.
   * @throws RDBMSException
   */
  public static void handleDefaultImage(IBaseEntityDAO entityDAO)
      throws RDBMSException
  {
    IBaseEntityDTO entityDTO = entityDAO.getBaseEntityDTO();
    if(entityDTO.getDefaultImageIID() == 0l && entityDTO.getBaseType().equals(BaseType.ARTICLE)) {
      Long newDefaultImage = entityDAO.getNewDefaultImage();
      if(newDefaultImage != 0l) {
        entityDTO.setDefaultImageIID(newDefaultImage);
        entityDAO.updatePropertyRecords();
      }
    }
  }
  
  public void deleteCoupledRecord(Long baseEntityIID) throws Exception
  {
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
    
    ICouplingDAO couplingDAO = localeCatalogDAO.openCouplingDAO();
    List<ICouplingDTO> conflictingValues = couplingDAO.getConflictingRecord(baseEntityIID);
    
    for (ICouplingDTO couplingDTO : conflictingValues) {
      if (couplingDTO.getCouplingSourceType() == CouplingType.TIGHT_RELATIONSHIP
          || couplingDTO.getCouplingSourceType() == CouplingType.DYN_RELATIONSHIP) {
        deleteRelationshipCoupledRecord(baseEntityIID, baseEntityDAO, couplingDAO, couplingDTO.getTargetEntityIID());
      }
      else if ((couplingDTO.getCouplingSourceType() == CouplingType.TIGHT_CONTEXTUAL
          || couplingDTO.getCouplingSourceType() == CouplingType.DYN_CONTEXTUAL)) {
        deleteContextualCoupledRecord(baseEntityIID, couplingDAO);
      }
    }
    couplingDAO.deleteCoupledRecord(baseEntityIID, localeCatalogDAO);
  }
  
  private void deleteContextualCoupledRecord(Long baseEntityIID, ICouplingDAO couplingDAO) throws Exception
  {
    List<Long> deletedEntities = new ArrayList<Long>();
    couplingDAO.getEntitiesForDelete(baseEntityIID, deletedEntities);
    for (Long deleteId : deletedEntities) {
      couplingDAO.deleteCoupledRecord(deleteId, rdbmsComponentUtils.getLocaleCatlogDAO());
    }
  }
  
  private void deleteRelationshipCoupledRecord(Long baseEntityIID, IBaseEntityDAO baseEntityDAO, ICouplingDAO couplingDAO,
      Long targetEnitityIID) throws RDBMSException, Exception, CSFormatException
  {
    List<ICouplingDTO> coupledRecords = couplingDAO.getCoupledRecords(baseEntityIID, targetEnitityIID);
    
    if (coupledRecords.size() > 0) {
      
      ConfigurationDAO configurationInstance = ConfigurationDAO.instance();
      
      for (ICouplingDTO couplingDTO : coupledRecords) {
        ILanguageConfigDTO languageConfigDTO = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(couplingDTO.getLocaleIID());
        
        IPropertyDTO propertyDTO = configurationInstance.getPropertyByIID(couplingDTO.getPropertyIID());
        IBaseEntityDAO targetBaseEntityDAO = null;
        if (languageConfigDTO != null) {
          IBaseEntityDAO sourceEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, languageConfigDTO.getLanguageCode());
          targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetEnitityIID, languageConfigDTO.getLanguageCode());
          IBaseEntityDTO loadPropertyRecords = sourceEntityDAO.loadPropertyRecords(propertyDTO);
          IPropertyRecordDTO propertyRecord = loadPropertyRecords.getPropertyRecord(couplingDTO.getPropertyIID());
          if (propertyRecord instanceof IValueRecordDTO) {
            IValueRecordDTO valueRecordDTO = targetBaseEntityDAO
                .newValueRecordDTOBuilder(propertyRecord.getProperty(), ((IValueRecordDTO) propertyRecord).getValue())
                .localeID(((IValueRecordDTO) propertyRecord).getLocaleID()).build();
            
            targetBaseEntityDAO.createPropertyRecords(valueRecordDTO);
          }
        }
        else {
          
          targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetEnitityIID);
          IBaseEntityDTO loadPropertyRecords = baseEntityDAO.loadPropertyRecords(propertyDTO);
          IPropertyRecordDTO propertyRecord = loadPropertyRecords.getPropertyRecord(couplingDTO.getPropertyIID());
          
          if (propertyRecord instanceof IValueRecordDTO) {
            IValueRecordDTO valueRecordDTO = targetBaseEntityDAO
                .newValueRecordDTOBuilder(propertyRecord.getProperty(), ((IValueRecordDTO) propertyRecord).getValue()).build();
            
            targetBaseEntityDAO.createPropertyRecords(valueRecordDTO);
          }
          else if (propertyRecord instanceof ITagsRecordDTO) {
            ITagsRecordDTO tagRecordDTO = targetBaseEntityDAO.newTagsRecordDTOBuilder(propertyRecord.getProperty()).build();
            Set<ITagDTO> tagsDTO = ((ITagsRecordDTO) propertyRecord).getTags();
            tagRecordDTO.setTags(tagsDTO.toArray(new ITagDTO[tagsDTO.size()]));
            targetBaseEntityDAO.createPropertyRecords(tagRecordDTO);
          }
        }
      }

    }
    //couplingDAO.deleteCoupledRecord(baseEntityIID);
  }

  public void fillBranchOfCloneOfLabel(IBaseEntityDTO baseEntity,
      IGetKlassInstanceModel returnModel, List<String> linkedVariantCodes)
      throws RDBMSException
  {
    String variantOfLabel = null;
    String branchOfLabel = "";
    Set<Long> baseEntityIIds = new HashSet<>();

    long variantOfIId = 0;
    if(!linkedVariantCodes.isEmpty()) {
      Map<Long, Long> variantIdParentIdMap = rdbmsComponentUtils.getLocaleCatlogDAO()
          .getLinkedVariantIds(Collections.singletonList(baseEntity), linkedVariantCodes);
      Long baseEntityIID = baseEntity.getBaseEntityIID();
      if (variantIdParentIdMap.containsKey(baseEntityIID)) {
        variantOfIId = variantIdParentIdMap.get(baseEntityIID);
        baseEntityIIds.add(variantOfIId);
      }
    }
    long branchOfIId = baseEntity.getOriginBaseEntityIID();
    if (branchOfIId != 0 && baseEntity.isClone()) {
      baseEntityIIds.add(branchOfIId);
    }

    Map<Long, String> baseEntityNamesByIID = rdbmsComponentUtils.getLocaleCatlogDAO().getBaseEntityNamesByIID(baseEntityIIds);
    if(variantOfIId != 0) {
      variantOfLabel = baseEntityNamesByIID.get(variantOfIId);
    }
    if(branchOfIId != 0) {
      branchOfLabel = baseEntityNamesByIID.get(branchOfIId);
    }

    returnModel.setBranchOfLabel(branchOfLabel);
    returnModel.setVariantOfLabel(variantOfLabel);
  }
  
}
