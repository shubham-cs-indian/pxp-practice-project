package com.cs.core.runtime.interactor.utils.klassinstance;

import com.cs.config.standard.IConfigMap;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.*;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionTagModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.process.idto.IRuleViolationDTO;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.datarule.*;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndVersionId;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndVersionId;
import com.cs.core.runtime.interactor.entity.klassinstance.*;
import com.cs.core.runtime.interactor.entity.language.ILanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.language.ILanguageConflictingValueSource;
import com.cs.core.runtime.interactor.entity.language.LanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.language.LanguageConflictingValueSource;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.*;
import com.cs.core.runtime.interactor.entity.relationship.IRelationshipConflictingValueSource;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipConflictingValueSource;
import com.cs.core.runtime.interactor.entity.tag.*;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.attribute.AttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;

import java.util.*;
import java.util.stream.Collectors;

public class KlassInstanceBuilder {

  public static final String ATTRIBUTE_ID_SEPARATOR = "-";

  private final IBaseEntityDAO baseEntityDAO;

  private final IGetConfigDetailsModel configDetails;

  private final IBaseEntityDTO baseEntityDTO;

  private final Map<String, IReferencedSectionElementModel> referencedSectionElements;

  private final Map<String, IAttribute> referencedAttributes;

  private final Map<String, ITag> referencedTags;

  private IRuleCatalogDAO ruleCatalogDAO;

  protected RDBMSComponentUtils rdbmsComponentUtils;

  protected KlassInstanceUtils klassInstanceUtils;

  @SuppressWarnings("unused") private final Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies;

  public KlassInstanceBuilder(IBaseEntityDAO baseEntityDAO, IGetConfigDetailsModel configDetails, RDBMSComponentUtils rdbmsComponentUtils)
      throws Exception
  {
    super();
    this.baseEntityDAO = baseEntityDAO;
    this.configDetails = configDetails;
    this.referencedSectionElements = configDetails.getReferencedElements();
    this.referencedAttributes = configDetails.getReferencedAttributes();
    this.referencedTags = configDetails.getReferencedTags();
    this.referencedTaxonomies = configDetails.getReferencedTaxonomies();
    this.rdbmsComponentUtils = rdbmsComponentUtils;
    this.loadPropertyRecords();
    this.baseEntityDTO = this.loadCouplingNotificationStatus();
    loadRuleViolations(this.configDetails);
  }

  /**
   * This constructor used from grid view edit To fetch klassinstance without
   * attribute and tag instance
   *
   * @param baseEntityDAO
   * @param referencedAttributes
   * @param referencedTags
   * @param referencedSectionElements
   * @throws Exception
   */
  public KlassInstanceBuilder(IBaseEntityDAO baseEntityDAO, Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags,
      Map<String, IReferencedSectionElementModel> referencedSectionElements, RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    super();
    this.baseEntityDAO = baseEntityDAO;
    this.configDetails = null;
    this.referencedSectionElements = referencedSectionElements;
    this.referencedAttributes = referencedAttributes;
    this.referencedTags = referencedTags;
    this.referencedTaxonomies = new HashMap<>();
    this.rdbmsComponentUtils = rdbmsComponentUtils;
    this.baseEntityDTO = this.loadPropertyRecords();
  }

  public IBaseEntityDTO getBaseEntityDTO()
  {
    return baseEntityDTO;
  }

  /**
   * This constructor used from x-ray view
   *
   * @param baseEntityDAO
   * @param referencedAttributes
   * @param referencedTags
   * @param propertyDTOs
   * @throws Exception
   */
  public KlassInstanceBuilder(IBaseEntityDAO baseEntityDAO, Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags,
      RDBMSComponentUtils rdbmsComponentUtils, IPropertyDTO... propertyDTOs) throws Exception
  {
    super();
    this.baseEntityDAO = baseEntityDAO;
    this.configDetails = null;
    this.referencedSectionElements = new HashMap<>();
    ;
    this.referencedAttributes = referencedAttributes;
    this.referencedTags = referencedTags;
    this.referencedTaxonomies = new HashMap<>();
    this.baseEntityDTO = this.baseEntityDAO.loadPropertyRecords(propertyDTOs);
    this.rdbmsComponentUtils = rdbmsComponentUtils;
  }
  
  public KlassInstanceBuilder(IBaseEntityDAO baseEntityDAO, Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags,
      RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    super();
    this.baseEntityDAO = baseEntityDAO;
    this.configDetails = null;
    this.referencedSectionElements = new HashMap<>();
    ;
    this.referencedAttributes = referencedAttributes;
    this.referencedTags = referencedTags;
    this.referencedTaxonomies = new HashMap<>();
    this.baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    this.rdbmsComponentUtils = rdbmsComponentUtils;
  }

  /**
   * This constructor used from x-ray view
   *
   * @throws Exception
   */
  public KlassInstanceBuilder(IBaseEntityDTO baseEntityDTO, RDBMSComponentUtils rdbmsComponentUtils)
  {
    this.baseEntityDAO = null;
    this.configDetails = null;
    this.referencedAttributes = null;
    this.referencedTags = null;
    this.referencedSectionElements = new HashMap<>();
    this.referencedTaxonomies = new HashMap<>();
    this.baseEntityDTO = baseEntityDTO;
    this.rdbmsComponentUtils = rdbmsComponentUtils;
  }

  private IBaseEntityDTO loadPropertyRecords() throws Exception
  {
    List<IPropertyDTO> referenceAttributesTags = BaseEntityUtils.getReferenceAttributesTags(referencedAttributes, referencedTags,
        baseEntityDAO);
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Map<Long, Set<IPropertyRecordDTO>> propertyRecordsForEntities = rdbmsComponentUtils.getLocaleCatlogDAO().getPropertyRecordsForEntities(Set.of(baseEntityDTO.getBaseEntityIID()), 
        referenceAttributesTags.toArray(new IPropertyDTO[referenceAttributesTags.size()]));
    Set<IPropertyRecordDTO> propertRecords = propertyRecordsForEntities.get(baseEntityDTO.getBaseEntityIID());
    if(propertRecords != null) {
      baseEntityDTO.setPropertyRecords(propertRecords
          .toArray(new IPropertyRecordDTO[propertRecords.size()]));
    }
    /*IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords(
        referenceAttributesTags.toArray(new IPropertyDTO[referenceAttributesTags.size()]));*/

    //Return name in created language if it doesn'e exists in the localeCatalogHeirarchy
    /* long namePropertyIID = IStandardConfig.StandardProperty.nameattribute.getIID();
    IPropertyRecordDTO nameAttribute = baseEntityDTO.getPropertyRecord(namePropertyIID);
    if (nameAttribute == null) {
      List<String> localeIDs = new ArrayList<>();
      String localeID = baseEntityDTO.getBaseLocaleID();
      localeIDs.add(localeID);
      IBaseEntityDAO baseEntityDAOByIID = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO.getBaseEntityIID(), localeIDs);
      IBaseEntityDTO propertyRecord = baseEntityDAOByIID.loadPropertyRecords(
          new PropertyDTO(namePropertyIID, IStandardConfig.StandardProperty.nameattribute.name(), PropertyType.TEXT));
      //for extension entity name attribute is null 
      if (propertyRecord.getPropertyRecord(namePropertyIID) != null)
        baseEntityDTO.getPropertyRecords().add(propertyRecord.getPropertyRecord(namePropertyIID));
    }*/
    return baseEntityDTO;
  }

  private void loadRuleViolations(IGetConfigDetailsModel configDetails) throws Exception
  {

    if (configDetails.getReferencedDataRules() != null || !configDetails.getReferencedDataRules().isEmpty()) {
      this.ruleCatalogDAO = rdbmsComponentUtils.openRuleDAO();
    }
  }

  private IBaseEntityDTO loadCouplingNotificationStatus() throws Exception
  {
    Set<IPropertyDTO> coupleProperty = this.referencedSectionElements.values().stream().map(referencedSectionElement -> {
      try {
        boolean isCouple = referencedSectionElement.getCouplingType() != null && (referencedSectionElement.getCouplingType()
            .equals(CommonConstants.DYNAMIC_COUPLED) || referencedSectionElement.getCouplingType().equals(CommonConstants.TIGHTLY_COUPLED));
        IPropertyDTO propertyDTO = null;
        if (isCouple) {
          if (referencedSectionElement.getType().equals(CommonConstants.ATTRIBUTE)) {
            propertyDTO = RDBMSUtils.newPropertyDTO(this.referencedAttributes.get(referencedSectionElement.getId()));
          }
          else if (referencedSectionElement.getType().equals(CommonConstants.TAG)) {
            propertyDTO = RDBMSUtils.newPropertyDTO(this.referencedTags.get(referencedSectionElement.getId()));
          }
        }
        return propertyDTO;
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).filter(propertyDTO -> propertyDTO != null).collect(Collectors.toSet());

    IPropertyDTO[] coupleProperties = coupleProperty.toArray(new IPropertyDTO[coupleProperty.size()]);
    IBaseEntityDTO baseEntityDTO = this.baseEntityDAO.loadCouplingNotifications(coupleProperties);
    return baseEntityDTO;
  }

  public static String getAttributeID(IValueRecordDTO valueRecord)
  {
    String attributeId = null;
    if (valueRecord.getValueIID() == -1) {
      attributeId = Long.toString(valueRecord.getEntityIID()) + ATTRIBUTE_ID_SEPARATOR + valueRecord.getProperty()
          .getPropertyIID() + valueRecord.getValueIID();
    }
    else {
      attributeId = Long.toString(valueRecord.getEntityIID()) + ATTRIBUTE_ID_SEPARATOR + valueRecord.getProperty()
          .getPropertyIID() + ATTRIBUTE_ID_SEPARATOR + Long.toString(valueRecord.getValueIID());
    }
    return attributeId;
  }

  public IKlassInstance getKlassInstance() throws Exception
  {
    IKlassInstance klassInstance = this.getKlassInstance(baseEntityDTO.getBaseType());
    if (klassInstance instanceof IContentInstance)
      setRuleViolation(klassInstance);

    setElementsInstance(klassInstance);
    return klassInstance;
  }

  public IKlassInstance getKlassInstanceForTimelineTab() throws Exception
  {
    IKlassInstance klassInstance = this.getKlassInstance(baseEntityDTO.getBaseType());
    ((IContentInstance) klassInstance).setDefaultAssetInstanceId(String.valueOf(baseEntityDTO.getDefaultImageIID()));
    this.setElementsInstance(klassInstance);
    if (baseEntityDTO.getBaseType().equals(BaseType.ASSET)) {
      fillEntityExtensionInAssetCoverFlowAttribute((List<IContentAttributeInstance>) klassInstance.getAttributes(), baseEntityDTO);
    }
    return klassInstance;
  }

  private void setRuleViolation(IKlassInstance klassInstance) throws RDBMSException
  {

    IContentInstance contentInstance = (IContentInstance) klassInstance;

    Set<IRuleViolationDTO> loadViolations = new HashSet<IRuleViolationDTO>();

    if (ruleCatalogDAO != null) {
      loadViolations = this.ruleCatalogDAO.loadViolations(baseEntityDTO.getBaseEntityIID());
    }
    List<IDataRuleModel> referencedDataRules = new ArrayList<IDataRuleModel>();

    if (configDetails != null) {
      referencedDataRules = configDetails.getReferencedDataRules();
    }
    Map<String, IDataRuleModel> dataRules = new HashMap<>();
    if (referencedDataRules != null && !referencedDataRules.isEmpty()) {
      for (IDataRuleModel dataRule : referencedDataRules) {
        dataRules.put(dataRule.getCode(), dataRule);
      }
    }
    List<IRuleViolation> ruleViolations = new ArrayList<>();
    for (IRuleViolationDTO ruleViolationDTO : loadViolations) {
      IRuleViolation ruleViolation = new RuleViolation();
      IDataRuleModel dataRuleConfigModel = dataRules.getOrDefault(ruleViolationDTO.getRuleCode(), new DataRuleModel());

      IPropertyDTO propertyDTO = ruleViolationDTO.getPropertyDTO();
      ruleViolation.setEntityId(propertyDTO.getCode());
      ruleViolation.setColor(ruleViolationDTO.getColor());
      ruleViolation.setDescription(ruleViolationDTO.getDescription());
      ruleViolation.setRuleId(ruleViolationDTO.getRuleCode());
      ruleViolation.setRuleLabel(dataRuleConfigModel.getLabel());
      ruleViolation.setType(propertyDTO.getSuperType().name().toLowerCase());
      ruleViolation.setId(Long.toString(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID()));

      ruleViolations.add(ruleViolation);
    }

    contentInstance.setRuleViolation(ruleViolations);

  }

  private IKlassInstance getKlassInstance(BaseType baseTypeEnum) throws Exception
  {
    IKlassInstance klassInstance = null;
    switch (baseTypeEnum) {
      case UNDEFINED:
        break;
      case ARTICLE:
        klassInstance = this.createArticleInstance();
        break;
      case ASSET:
        klassInstance = this.createAssetInstance();
        break;
      default:
        klassInstance = this.createArticleInstance();
        break;
    }
    return klassInstance;
  }

  public void setKlassInstanceProperty(IKlassInstance klassInstance) throws Exception
  {
    klassInstance.setId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    klassInstance.setOriginalInstanceId(baseEntityDTO.getBaseEntityID());
    klassInstance.setName(baseEntityDTO.getBaseEntityName());
    // Handling done for Target Instances
    if ((baseEntityDTO.getEntityExtension()).containsField(IAttribute.SUB_TYPE)) {
      klassInstance.setBaseType(baseEntityDTO.getEntityExtension().getInitField(IAttribute.SUB_TYPE, ""));
    }
    else {
      klassInstance.setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDTO.getBaseType()));
    }
    List<String> taxonomy = new ArrayList<String>();
    // baseEntityDao is null means BaseEntityDTO is already loaded so take classifiers from baseEntityDTO 
    if (baseEntityDAO != null) {
      klassInstance.setTypes(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO));
      taxonomy = BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers());
    }
    else {
      List<String> classifierCodes = new ArrayList<String>();
      classifierCodes.add(baseEntityDTO.getNatureClassifier().getClassifierCode());
      for (IClassifierDTO classifier : baseEntityDTO.getOtherClassifiers()) {
        ClassifierType classifierType = classifier.getClassifierType();
        if (classifierType.equals(ClassifierType.CLASS)) {
          classifierCodes.add(classifier.getClassifierCode());
        }
        else if (classifierType.equals(ClassifierType.TAXONOMY) || classifierType.equals(ClassifierType.MINOR_TAXONOMY)) {
          taxonomy.add(classifier.getClassifierCode());
        }
      }
      klassInstance.setTypes(classifierCodes);
    }
    klassInstance.setTaxonomyIds(taxonomy);
    klassInstance.setSelectedTaxonomyIds(taxonomy);
    klassInstance.setLastModified(baseEntityDTO.getLastModifiedTrack().getWhen());
    klassInstance.setLastModifiedBy(baseEntityDTO.getLastModifiedTrack().getWho());
    klassInstance.setCreatedOn(baseEntityDTO.getCreatedTrack().getWhen());
    klassInstance.setCreatedBy(baseEntityDTO.getCreatedTrack().getWho());
  }

  private void setContentInstanceProperty(IContentInstance contentInstance) throws Exception
  {
    this.setKlassInstanceProperty(contentInstance);
    contentInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getTopParentIID()));
    long defaultImageIID = baseEntityDTO.getDefaultImageIID();
    contentInstance.setDefaultAssetInstanceId(defaultImageIID == 0 ? null : String.valueOf(defaultImageIID));
    if (baseEntityDTO.getTopParentIID() == 0l) {
      contentInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    }
    contentInstance.setParentId(String.valueOf(baseEntityDTO.getParentIID()));
    if (baseEntityDTO.getParentIID() == 0l) {
      contentInstance.setParentId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    }
    contentInstance.setCreationLanguage(baseEntityDTO.getBaseLocaleID());
    List<String> localeIds = baseEntityDTO.getLocaleIds();
    contentInstance.setLanguageCodes(localeIds);
    List<ILanguageAndVersionId> languageInstances = new ArrayList<>();

    localeIds.forEach(localeId -> {
      ILanguageAndVersionId languageInstance = new LanguageAndVersionId();
      languageInstance.setLanguageCode(localeId);
      languageInstance.setVersionId(0l); //TODO: need to change versionId as per versioning.
      languageInstances.add(languageInstance);
    });
    contentInstance.setLanguageInstances(languageInstances);

    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    if (!contextualObject.isNull()) {
      IContextInstance contextInstance = new ContextInstance();
      contextInstance.setContextId(contextualObject.getContextCode());
      contextInstance.setId(Long.toString(contextualObject.getContextualObjectIID()));
      contentInstance.setContext(contextInstance);
    }
    if (contentInstance instanceof IAssetInstance) {
      ((IAssetInstance) contentInstance).setIsExpired(baseEntityDTO.isExpired());
    }

  }

  private IKlassInstance createArticleInstance() throws Exception
  {
    IArticleInstance articleInstance = new ArticleInstance();
    this.setContentInstanceProperty(articleInstance);
    // Article Instance specific task
    return articleInstance;
  }

  private IKlassInstance createAssetInstance() throws Exception
  {
    IAssetInstance assetInstance = new AssetInstance();
    this.setContentInstanceProperty(assetInstance);
    // Asset Instance specific task
    return assetInstance;
  }

  @SuppressWarnings("unchecked")
  private void setElementsInstance(IKlassInstance klassInstance) throws Exception
  {
    List<IContentAttributeInstance> attributeInstances = (List<IContentAttributeInstance>) klassInstance.getAttributes();
    List<IContentTagInstance> tagInstances = (List<IContentTagInstance>) klassInstance.getTags();
    TreeSet<IPropertyRecordDTO> propertyRecords = (TreeSet<IPropertyRecordDTO>) baseEntityDTO.getPropertyRecords();
    TreeSet<IPropertyRecordDTO> properties = new TreeSet<>(propertyRecords);
    List<IPropertyRecordDTO> conflictingProperties = baseEntityDAO.getConflictingProperties();
    Set<String> propertyCodes = propertyRecords.stream()
        .map(propertyRecord -> propertyRecord.getProperty().getPropertyCode())
        .collect(Collectors.toSet());

    for (IPropertyRecordDTO conflictingProperty : conflictingProperties) {
      if (!propertyCodes.contains(conflictingProperty.getProperty().getPropertyCode())) {
        properties.add(conflictingProperty);
      }
    }

    fillAttributesAndTags(klassInstance, attributeInstances, tagInstances);
    //language inheritance notification
    /*if(!propertyIIDs.isEmpty()) {
      handleLanguageInheritanceNotificationForValueRecord(attributeInstances, propertyIIDs);
    }*/
  }

  private void fillAttributesAndTags(IKlassInstance klassInstance, List<IContentAttributeInstance> attributeInstances,
      List<IContentTagInstance> tagInstances) throws Exception
  {
    TreeSet<IPropertyRecordDTO> propertyRecords = (TreeSet<IPropertyRecordDTO>) baseEntityDTO.getPropertyRecords();

    if (propertyRecords.isEmpty()) {
      return;
    }

    Map<Long, List<ICouplingDTO>> couplingDTOs = baseEntityDAO.loadCouplingConflicts(
        propertyRecords.stream().map(x -> x.getProperty().getPropertyIID()).collect(Collectors.toList()));

    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      IReferencedSectionElementModel referencedSectionElement = referencedSectionElements.get(
          propertyRecord.getProperty().getPropertyCode());
      if (propertyRecord instanceof IValueRecordDTO) {
        switch (propertyRecord.getProperty().getPropertyType()) {
          case BOOLEAN:
            ITag referencedTag = this.referencedTags.get(propertyRecord.getProperty().getPropertyCode());
            IContentTagInstance tagInstance = createBooleanAttributeInstance(referencedTag,
                (ReferencedSectionTagModel) referencedSectionElement, (IValueRecordDTO) propertyRecord);
            tagInstances.add(tagInstance);
            break;

          default:
            IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;

            // Attribute variant
            if (valueRecordDTO.getContextualObject() != null && valueRecordDTO.getContextualObject().getContextualObjectIID() != 0) {
              IIdAndVersionId attributeContext = this.getAttributeContext(valueRecordDTO);
              if (klassInstance != null && klassInstance instanceof IContentInstance) {
                ((IContentInstance) klassInstance).getAttributeVariants().add(attributeContext);
              }
            }
            // Normal attributes
            else {
              IAttribute referencedAttribute = this.referencedAttributes.get(propertyRecord.getProperty().getPropertyCode());
              ReferencedSectionAttributeModel referencedElement = (ReferencedSectionAttributeModel) referencedSectionElement;
              if (valueRecordDTO.getProperty().getCode().equals(CommonConstants.NAME_ATTRIBUTE)) {
                List<String> localeIds = baseEntityDAO.getBaseEntityDTO().getLocaleIds();
                if (localeIds.contains(rdbmsComponentUtils.getDataLanguage())) {
                  IContentAttributeInstance attributeInstance = getAttributeInstance(klassInstance, referencedAttribute, referencedElement,
                      valueRecordDTO, couplingDTOs.get(propertyRecord.getProperty().getPropertyIID()));
                  attributeInstances.add(attributeInstance);
                }
              }
              else {
                IContentAttributeInstance attributeInstance = getAttributeInstance(klassInstance, referencedAttribute, referencedElement,
                    valueRecordDTO, couplingDTOs.get(propertyRecord.getProperty().getPropertyIID()));
                attributeInstances.add(attributeInstance);
              }
            }
        }
      }
      else if (propertyRecord instanceof ITagsRecordDTO) {
        ITag referencedTag = this.referencedTags.get(propertyRecord.getProperty().getPropertyCode());
        IContentTagInstance tagInstance = getTagInstance(referencedTag, (ReferencedSectionTagModel) referencedSectionElement,
            (ITagsRecordDTO) propertyRecord, couplingDTOs.get(propertyRecord.getProperty().getPropertyIID()));
        tagInstances.add(tagInstance);
      }
    }
  }

  private IIdAndVersionId getAttributeContext(IValueRecordDTO valueRecordDTO)
  {
    IIdAndVersionId attributeContext = new IdAndVersionId();
    attributeContext.setId(KlassInstanceBuilder.getAttributeID(valueRecordDTO));
    return attributeContext;
  }

  private IContentAttributeInstance getAttributeInstance(IKlassInstance klassInstance, IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord, List<ICouplingDTO> couplingDTOs) throws Exception
  {
    IContentAttributeInstance contentAttributeInstance = null;
    PropertyType type = valueRecord.getProperty().getPropertyType();
    switch (type) {
      case MEASUREMENT:
        contentAttributeInstance = createMeasurementAttributeInstance(referencedAttribute, referencedSectionAttribute, valueRecord, couplingDTOs);
        break;
      case HTML:
        contentAttributeInstance = createHTMLAttributeInstance(referencedAttribute, referencedSectionAttribute, valueRecord, couplingDTOs);
        break;
      case NUMBER:
        contentAttributeInstance = createNumberAttributeInstance(referencedAttribute, referencedSectionAttribute, valueRecord, couplingDTOs);
        break;
      case DATE:
        contentAttributeInstance = createDateAttributeInstance(referencedAttribute, referencedSectionAttribute, valueRecord, couplingDTOs);
        break;
      case PRICE:
        contentAttributeInstance = createPriceAttributeInstance(referencedAttribute, referencedSectionAttribute, valueRecord, couplingDTOs);
        break;
      case TEXT:
        contentAttributeInstance = createTextAttributeInstance(referencedAttribute, referencedSectionAttribute, valueRecord, couplingDTOs);
        break;

      case CALCULATED:
        contentAttributeInstance = createCalculatedAttribute(referencedAttribute, referencedSectionAttribute, valueRecord);
        break;
      case ASSET_ATTRIBUTE:
        contentAttributeInstance = createAssetAttributeInstance(referencedAttribute, referencedSectionAttribute, valueRecord);
        break;

      case CONCATENATED:
        contentAttributeInstance = createConcatenatedAttribute(referencedAttribute, referencedSectionAttribute, valueRecord);
        break;

      default:
        // TODO throw exception for method missing
        break;
    }
    return contentAttributeInstance;
  }

  public void setContentAttributeInstanceProperty(IContentAttributeInstance contentAttributeInstance, IValueRecordDTO valueRecord)
  {
    contentAttributeInstance.setId(KlassInstanceBuilder.getAttributeID(valueRecord));
    contentAttributeInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    contentAttributeInstance.setBaseType(AttributeInstance.class.getName());
  }

  public void setAttributeInstanceProperty(IAttributeInstance attributeInstance, IValueRecordDTO valueRecord)
  {
    attributeInstance.setLanguage(valueRecord.getLocaleID());
    attributeInstance.setValue(valueRecord.getValue());
    attributeInstance.setOriginalInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    attributeInstance.setAttributeId(valueRecord.getProperty().getPropertyCode());
  }

  private IContentAttributeInstance createMeasurementAttributeInstance(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord,  List<ICouplingDTO> couplingDTOs) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    this.handleNotificationConflictValueForAttributes(attributeInstance, valueRecord, referencedSectionAttribute, couplingDTOs);
    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);

    // Measurement attribute specific task
    return attributeInstance;
  }

  private IContentAttributeInstance createNumberAttributeInstance(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord,  List<ICouplingDTO> couplingDTOs) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    this.handleNotificationConflictValueForAttributes(attributeInstance, valueRecord, referencedSectionAttribute, couplingDTOs);
    // Number attribute specific task
    attributeInstance.setValueAsNumber(valueRecord.getAsNumber());

    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);
    return attributeInstance;
  }

  private void fillProductIdentifierInfo(ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord,
      IAttributeInstance attributeInstance) throws Exception
  {

    if (referencedSectionAttribute != null && referencedSectionAttribute.getIsIdentifier()) {
      ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
      IUniquenessViolationDAO uniquenessDAO = localeCatlogDAO.openUniquenessDAO();

      Set<IClassifierDTO> otherClassifiers = baseEntityDTO.getOtherClassifiers();
      IUniquenessViolationDTO uniquenessViolationDTO = localeCatlogDAO.newUniquenessViolationBuilder(valueRecord.getEntityIID(),
          valueRecord.getEntityIID(), valueRecord.getProperty().getPropertyIID(), baseEntityDTO.getNatureClassifier().getClassifierIID())
          .build();

      List<Long> targetIIDS = uniquenessDAO.isUniqueRecord(uniquenessViolationDTO, otherClassifiers);

      if (targetIIDS.size() > 0) {

        IDuplicateTypeAndInstanceIds duplicateTypeAndInstanceIds = new DuplicateTypeAndInstanceIds();
        List<String> duplicateKlassInstanceIds = new ArrayList<>();

        for (Long targetIID : targetIIDS) {
          duplicateKlassInstanceIds.add(Long.toString(targetIID));
        }

        duplicateTypeAndInstanceIds.setTypeId(baseEntityDTO.getNatureClassifier().getClassifierCode());
        duplicateTypeAndInstanceIds.setDuplicateKlassInstanceIds(duplicateKlassInstanceIds);
        attributeInstance.getDuplicateStatus().add(duplicateTypeAndInstanceIds);
        attributeInstance.setIsUnique(0);
      }

      if (valueRecord.getValue().isEmpty()) {
        attributeInstance.setIsMandatoryViolated(true);
      }
    }

  }

  private IContentAttributeInstance createDateAttributeInstance(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord,  List<ICouplingDTO> couplingDTOs) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    this.handleNotificationConflictValueForAttributes(attributeInstance, valueRecord, referencedSectionAttribute, couplingDTOs);
    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);

    // Date attribute specific task
    attributeInstance.setValueAsNumber(valueRecord.getAsNumber());
    return attributeInstance;
  }

  private IContentAttributeInstance createHTMLAttributeInstance(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord,  List<ICouplingDTO> couplingDTOs) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    this.handleNotificationConflictValueForAttributes(attributeInstance, valueRecord, referencedSectionAttribute, couplingDTOs);
    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);

    // HTML attribute specific task
    attributeInstance.setValueAsHtml(valueRecord.getAsHTML());
    return attributeInstance;
  }

  private IContentAttributeInstance createPriceAttributeInstance(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord,  List<ICouplingDTO> couplingDTOs) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    this.handleNotificationConflictValueForAttributes(attributeInstance, valueRecord, referencedSectionAttribute, couplingDTOs);
    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);

    // Price attribute specific task
    attributeInstance.setValueAsNumber(valueRecord.getAsNumber());
    return attributeInstance;
  }

  private IContentAttributeInstance createTextAttributeInstance(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord,  List<ICouplingDTO> couplingDTOs) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    this.handleNotificationConflictValueForAttributes(attributeInstance, valueRecord, referencedSectionAttribute, couplingDTOs);
    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);

    // Text attribute specific task
    return attributeInstance;
  }

  private void handleNotificationConflictValueForAttributes(IAttributeInstance attributeInstance, IValueRecordDTO valueRecord,
      ReferencedSectionAttributeModel referencedSectionAttribute, List<ICouplingDTO> loadCouplingConflicts) throws Exception
  {
    if (referencedSectionAttribute != null) {


      if (loadCouplingConflicts == null || loadCouplingConflicts.isEmpty()) {
        return;
      }

      String dataLanguage = rdbmsComponentUtils.getDataLanguage();
      ILanguageConfigDTO languageConfig = ConfigurationDAO.instance().getLanguageConfig(dataLanguage);

      for (ICouplingDTO couplingDTO : loadCouplingConflicts) {

        if (couplingDTO.getLocaleIID() == 0l || couplingDTO.getLocaleIID() == languageConfig.getLanguageIID()) {

          Long couplingSourceIID = couplingDTO.getCouplingSourceIID();
          long sourceEntityIID = couplingDTO.getSourceEntityIID();

          IBaseEntityDAO baseEntityDAO = null;
          ILanguageConfigDTO languageConfigByLanguageIID = null;

          ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
          IBaseEntityDTO entity = localeCatalogDAO.getEntityByIID(sourceEntityIID);

          if (couplingDTO.getCouplingSourceType().equals(CouplingType.LANG_INHERITANCE)) {
            languageConfigByLanguageIID = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(couplingDTO.getCouplingSourceIID());
            if (entity.getCatalog().getCatalogCode().contains(CommonConstants.DEFAULT_KLASS_INSTANCE_CATALOG)) {
              localeCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAOWithLocale(languageConfigByLanguageIID.getLanguageCode());
            }
            else {
              List<String> dataLanguageCodes = new ArrayList<>();
              dataLanguageCodes.add(languageConfigByLanguageIID.getLanguageCode());
              localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO(dataLanguageCodes);
            }
            
          }
          else if (entity.getCatalog().getCatalogCode().contains(CommonConstants.DEFAULT_KLASS_INSTANCE_CATALOG)) {
              localeCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
          }
          baseEntityDAO = localeCatalogDAO.openBaseEntity(entity);

          IPropertyRecordDTO propertyRecord = loadSinglePropertyRecords(baseEntityDAO, valueRecord.getProperty());
          if (propertyRecord instanceof IValueRecordDTO) {
            IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;

            if (couplingDTO.getRecordStatus().equals(RecordStatus.NOTIFIED)) {
              attributeInstance.getNotification().put("attributeValue", valueRecordDTO.getValue());
            }

            IAttributeConflictingValue attributeConflictingValue = new AttributeConflictingValue();
            attributeConflictingValue.setCouplingType(couplingDTO.getCouplingType().toString());
            attributeConflictingValue.setValue(valueRecordDTO.getValue());
            attributeConflictingValue.setValueAsHtml(valueRecordDTO.getAsHTML());

            if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_CONTEXTUAL) || couplingDTO.getCouplingSourceType()
                .equals(CouplingType.DYN_CONTEXTUAL)) {

              prepareResponseModelForContextualDataTransfer(couplingSourceIID, sourceEntityIID, attributeConflictingValue);
            }
            else if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_RELATIONSHIP) || couplingDTO.getCouplingSourceType()
                .equals(CouplingType.DYN_RELATIONSHIP)) {
              prepareResponseModelForRelationshipDataTransfer(couplingSourceIID, sourceEntityIID, attributeConflictingValue);
            }
            else if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_CLASSIFICATION) || couplingDTO.getCouplingSourceType()
                .equals(CouplingType.DYN_CLASSIFICATION)) {
              prepareResponseModelForClassificationDataTransfer(couplingSourceIID, sourceEntityIID, attributeConflictingValue);
            }
            else if (couplingDTO.getCouplingSourceType().equals(CouplingType.LANG_INHERITANCE)) {
              prepareResponseModelForLanguageInheritance(attributeInstance, sourceEntityIID, languageConfigByLanguageIID,
                  attributeConflictingValue);
            }
            attributeInstance.getConflictingValues().add(attributeConflictingValue);
          }
        }
      }
    }
  }

  private void prepareResponseModelForLanguageInheritance(IAttributeInstance attributeInstance, long sourceEntityIID,
      ILanguageConfigDTO languageConfigByLanguageIID, IAttributeConflictingValue attributeConflictingValue)
  {
    ILanguageConflictingValueSource conflictingValueSource = new LanguageConflictingValueSource();
    conflictingValueSource.setId(languageConfigByLanguageIID.getLanguageCode());
    conflictingValueSource.setType(CommonConstants.LANGUAGE_CONFLICTING_SOURCE_TYPE);
    conflictingValueSource.setLastModifiedBy(attributeInstance.getLastModifiedBy());
    conflictingValueSource.setContentId(Long.toString(sourceEntityIID));
    attributeConflictingValue.setSource(conflictingValueSource);
  }

  private void prepareResponseModelForRelationshipDataTransfer(Long couplingSourceIID, long sourceEntityIID,
      IAttributeConflictingValue attributeConflictingValue) throws RDBMSException
  {
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(couplingSourceIID);
    IRelationshipConflictingValueSource conflictingValueSource = new RelationshipConflictingValueSource();
    conflictingValueSource.setType(RelationshipConflictingValueSource.class.getName());
    conflictingValueSource.setId(propertyDTO.getCode());
    conflictingValueSource.setContentId(Long.toString(sourceEntityIID));
    attributeConflictingValue.setSource(conflictingValueSource);
  }

  private void prepareResponseModelForContextualDataTransfer(Long couplingSourceIID, long sourceEntityIID,
      IAttributeConflictingValue attributeConflictingValue) throws RDBMSException
  {
    IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByIID(couplingSourceIID);
    IContextConflictingValueSource conflictingValueSource = new ContextConflictingValueSource();
    conflictingValueSource.setType(ContextConflictingValueSource.class.getName());
    conflictingValueSource.setId(classifierDTO.getCode());
    conflictingValueSource.setContentId(Long.toString(sourceEntityIID));
    attributeConflictingValue.setSource(conflictingValueSource);
  }

  private void prepareResponseModelForClassificationDataTransfer(Long couplingSourceIID, long sourceEntityIID,
      IAttributeConflictingValue attributeConflictingValue) throws RDBMSException
  {
    IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByIID(couplingSourceIID);
    ITaxonomyConflictingValueSource conflictingValueSource = new TaxonomyConflictingValueSource();
    conflictingValueSource.setType(TaxonomyConflictingValueSource.class.getName());
    conflictingValueSource.setId(classifierDTO.getCode());
    // conflictingValueSource.setContentId(Long.toString(sourceEntityIID));
    attributeConflictingValue.setSource(conflictingValueSource);
  }

  private IContentAttributeInstance createCalculatedAttribute(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);

    return attributeInstance;
  }

  private IContentAttributeInstance createConcatenatedAttribute(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord) throws Exception
  {
    List<IConcatenatedOperator> attributeConcatenatedList = ((IConcatenatedAttribute) referencedAttribute).getAttributeConcatenatedList();

    Map<Integer, String> userTextMap = new HashMap<>();
    for (IConcatenatedOperator attributeConcatenated : attributeConcatenatedList) {
      if (attributeConcatenated.getType().equals("html")) {
        userTextMap.put(attributeConcatenated.getOrder(), attributeConcatenated.getCode());
      }
    }
    IAttributeInstance attributeInstance = new AttributeInstance();

    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    //set the html value
    attributeInstance.setValueAsHtml(StringEscapeUtils.unescapeHtml4(valueRecord.getAsHTML()));

    String expression = valueRecord.getCalculation();

    String expressionIfExist = expression.replace("{", "").replace("}", "");

    if (!expressionIfExist.isEmpty()) {
      String[] expressionSplit = expressionIfExist.split("=", 2);
      String expressionAfterEqual = expressionSplit[1];
      String expressionRemovalOfSpecialChar = null;
      int order = 0;

      if (expressionAfterEqual.contains("[") || expressionAfterEqual.contains("'") || expressionAfterEqual.contains("}")) {
        expressionRemovalOfSpecialChar = expressionAfterEqual.replace("[", "").replace("]", "").replace("\"", "").replace("}", "");
      }
      expressionRemovalOfSpecialChar = StringEscapeUtils.unescapeHtml4(expressionRemovalOfSpecialChar);
      String[] expressionSubStrings = expressionRemovalOfSpecialChar.split("\\|\\|");
      List<IConcatenatedOperator> valueAsExpression = new ArrayList<>();
      for (String expressionSubString : expressionSubStrings) {
        long propertyIID = 0;
        IAttribute attributeValue = null;
        ITag iTag = null;
        expressionSubString = expressionSubString.replace("'", "");

        if (referencedAttributes.containsKey(expressionSubString)) {
          attributeValue = referencedAttributes.get(expressionSubString);
          propertyIID = attributeValue.getPropertyIID();
        }
        else if (referencedTags.containsKey(expressionSubString)) {
          iTag = referencedTags.get(expressionSubString);
          propertyIID = iTag.getPropertyIID();
        }

        if (propertyIID != 0) {
          ConcatenatedAttributeOperator concatenatedOperator = new ConcatenatedAttributeOperator();
          IPropertyRecordDTO propertyRecord = baseEntityDTO.getPropertyRecord(propertyIID);

          if (propertyRecord != null && propertyRecord instanceof IValueRecordDTO) {
            IValueRecordDTO ValueRecordDto = (IValueRecordDTO) propertyRecord;
            concatenatedOperator.setCode(expressionSubString);
            concatenatedOperator.setId(Long.toString(ValueRecordDto.getProperty().getPropertyIID()));
            concatenatedOperator.setAttributeId(attributeValue.getId());
            concatenatedOperator.setType("attribute");
            concatenatedOperator.setOrder(order);
            valueAsExpression.add(concatenatedOperator);
          }
          else if (propertyRecord != null && propertyRecord instanceof ITagsRecordDTO) {
            ConcatenatedTagOperator concatenatedTagOperator = new ConcatenatedTagOperator();
            ITagsRecordDTO tagRecordDto = (ITagsRecordDTO) propertyRecord;
            concatenatedTagOperator.setCode(expressionSubString);
            concatenatedTagOperator.setId(tagRecordDto.getNodeID());
            concatenatedTagOperator.setTagId(iTag.getId());
            concatenatedTagOperator.setType("tag");
            concatenatedTagOperator.setOrder(order);
            valueAsExpression.add(concatenatedTagOperator);
          }
        }
        else {
          IConcatenatedHtmlOperator concatenatedOperator = new ConcatenatedHtmlOperator();
          concatenatedOperator.setValue(Jsoup.parse(expressionSubString).text());
          concatenatedOperator.setType("html");
          concatenatedOperator.setValueAsHtml(expressionSubString);
          concatenatedOperator.setOrder(order);
          String id = userTextMap.get(order);
          concatenatedOperator.setId(id);
          concatenatedOperator.setCode(id);

          valueAsExpression.add(concatenatedOperator);
        }
        order++;
      }

      attributeInstance.setValueAsExpression(valueAsExpression);

      fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);
    }
    return attributeInstance;
  }

  private IContentAttributeInstance createStandardAttributeInstance(IKlassInstance klassInstance, IAttribute referencedAttribute,
      IValueRecordDTO valueRecord, ReferencedSectionAttributeModel referencedSectionAttribute) throws Exception
  {
    IContentAttributeInstance attributeInstance = null;
    String type = referencedAttribute.getType();
    switch (type) {
      case Constants.CREATED_BY_ATTRIBUTE_BASE_TYPE:
        attributeInstance = this.createCreateByAttributeInstance(referencedAttribute, valueRecord);
        if (klassInstance != null)
          klassInstance.setCreatedBy(valueRecord.getValue());
        break;
      case Constants.CREATED_ON_ATTRIBUTE_BASE_TYPE:
        attributeInstance = this.createCreateOnAttributeInstance(referencedAttribute, valueRecord);
        if (klassInstance != null)
          klassInstance.setCreatedOn((long) valueRecord.getAsNumber());
        break;
      case Constants.LAST_MODIFIED_BY_ATTRIBUTE_BASE_TYPE:
        attributeInstance = this.createLastModifiedByAttributeInstance(referencedAttribute, valueRecord);
        if (klassInstance != null)
          klassInstance.setLastModifiedBy(valueRecord.getValue());
        break;
      case Constants.LAST_MODIFIED_ATTRIBUTE_BASE_TYPE:
        attributeInstance = this.createLastModifiedAttributeInstance(referencedAttribute, valueRecord);
        if (klassInstance != null)
          klassInstance.setLastModified((long) valueRecord.getAsNumber());
        break;
      case Constants.NAME_ATTRIBUTE_TYPE:
        attributeInstance = this.createNameAttributeInstance(referencedAttribute, valueRecord);
        break;
      default:
        attributeInstance = this.createStandardAttributeInstance(referencedAttribute, valueRecord, referencedSectionAttribute);
    }

    return attributeInstance;
  }

  private IContentAttributeInstance createCreateByAttributeInstance(IAttribute referencedAttribute, IValueRecordDTO valueRecord)
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    // Create By attribute specific task
    return attributeInstance;
  }

  private IContentAttributeInstance createCreateOnAttributeInstance(IAttribute referencedAttribute, IValueRecordDTO valueRecord)
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    // Create On attribute specific task
    attributeInstance.setValueAsNumber(valueRecord.getAsNumber());
    return attributeInstance;
  }

  private IContentAttributeInstance createLastModifiedByAttributeInstance(IAttribute referencedAttribute, IValueRecordDTO valueRecord)
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    // Last Modified By attribute specific task
    return attributeInstance;
  }

  private IContentAttributeInstance createLastModifiedAttributeInstance(IAttribute referencedAttribute, IValueRecordDTO valueRecord)
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    // Last Modified attribute specific task
    attributeInstance.setValueAsNumber(valueRecord.getAsNumber());
    return attributeInstance;
  }

  private IContentAttributeInstance createNameAttributeInstance(IAttribute referencedAttribute, IValueRecordDTO valueRecord)
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    // Last Modified attribute specific task
    attributeInstance.setValueAsNumber(valueRecord.getAsNumber());
    return attributeInstance;
  }

  private IContentAttributeInstance createStandardAttributeInstance(IAttribute referencedAttribute, IValueRecordDTO valueRecord,
      ReferencedSectionAttributeModel referencedSectionAttribute) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);

    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);

    // Create On attribute specific task
    return attributeInstance;
  }

  private IContentAttributeInstance createAssetAttributeInstance(IAttribute referencedAttribute,
      ReferencedSectionAttributeModel referencedSectionAttribute, IValueRecordDTO valueRecord) throws Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    this.setContentAttributeInstanceProperty(attributeInstance, valueRecord);
    this.setAttributeInstanceProperty(attributeInstance, valueRecord);
    fillProductIdentifierInfo(referencedSectionAttribute, valueRecord, attributeInstance);
    // Asset attribute specific task
    return attributeInstance;
  }

  private IContentTagInstance createBooleanAttributeInstance(ITag referencedTag, ReferencedSectionTagModel referencedSectionTag,
      IValueRecordDTO valueRecord)
  {
    ITagInstance tagInstance = new TagInstance();
    tagInstance.setId(Long.toString(valueRecord.getValueIID()));
    tagInstance.setKlassInstanceId(baseEntityDTO.getNatureClassifier().getClassifierCode());
    tagInstance.setBaseType(TagInstance.class.getName());
    tagInstance.setTagId(valueRecord.getProperty().getPropertyCode());

    // Tag values
    List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
    ITagInstanceValue tagInstanceValue = new TagInstanceValue();
    tagInstanceValue.setId(Long.toString(valueRecord.getValueIID()));
    tagInstanceValue.setCode(Long.toString(valueRecord.getValueIID()));
    if (referencedTag.getChildren() != null && referencedTag.getChildren().size() >= 1) {
      ITreeEntity treeEntity = referencedTag.getChildren().get(0);
      tagInstanceValue.setTagId(treeEntity.getId());
    }

    int relevance = 0;
    if (valueRecord.getValue().equals(IStandardConfig.TRUE) && valueRecord.getAsNumber() == 1) {
      relevance = 100;
    }
    tagInstanceValue.setRelevance(relevance);
    tagInstanceValues.add(tagInstanceValue);
    tagInstance.setTagValues(tagInstanceValues);
    return tagInstance;
  }

  /**
   * **************************************************** Tag Instance
   * ****************************************************
   */
  public static String getTagValueID(ITagDTO tagDTO)
  {
    String tagValueId = tagDTO.getTagValueCode();
    return tagValueId;
  }

  private IContentTagInstance getTagInstance(ITag referencedTag, ReferencedSectionTagModel referencedSectionTag,
      ITagsRecordDTO tagsRecordDTO, List<ICouplingDTO> couplingDTOS) throws Exception
  {
    IContentTagInstance tagInstance = this.getTagInstance(tagsRecordDTO);
    if (couplingDTOS!=null && !couplingDTOS.isEmpty()) {
      this.handleNotificationConflictValueForTags(tagInstance, tagsRecordDTO, referencedTag, referencedSectionTag, couplingDTOS);
    }
    return tagInstance;
  }

  private IContentTagInstance getTagInstance(ITagsRecordDTO tagsRecordDTO)
  {
    ITagInstance tagInstance = new TagInstance();
    tagInstance.setId(tagsRecordDTO.getNodeID());
    tagInstance.setKlassInstanceId(baseEntityDTO.getNatureClassifier().getClassifierCode());
    tagInstance.setBaseType(TagInstance.class.getName());
    tagInstance.setTagId(tagsRecordDTO.getProperty().getPropertyCode());

    // Tag values
    List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
    tagsRecordDTO.getTags().forEach(tagRecordDTO -> {
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setTagId(tagRecordDTO.getTagValueCode());
      tagInstanceValue.setId(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
      tagInstanceValue.setCode(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
      tagInstanceValue.setRelevance(tagRecordDTO.getRange());
      tagInstanceValues.add(tagInstanceValue);
    });
    tagInstance.setTagValues(tagInstanceValues);
    return tagInstance;
  }

  private void handleNotificationConflictValueForTags(IContentTagInstance tagInstance, ITagsRecordDTO tagsRecordDTO, ITag referencedTag,
      ReferencedSectionTagModel referencedSectionTag, List<ICouplingDTO> loadCouplingConflicts) throws Exception
  {
    if (referencedSectionTag != null && referencedSectionTag.getCouplingType() != null) {
      // TODO No need to fetch baseEntityDAO, Conflict values will come from
      // ITagsRecordDTO (if record on coupling state)

      for (ICouplingDTO couplingDTO : loadCouplingConflicts) {

        Long couplingSourceIID = couplingDTO.getCouplingSourceIID();
        long sourceEntityIID = couplingDTO.getSourceEntityIID();

        ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
        IBaseEntityDTO entity = localeCatalogDAO.getEntityByIID(sourceEntityIID);

        if (entity.getCatalog().getCatalogCode().contains(CommonConstants.DEFAULT_KLASS_INSTANCE_CATALOG)) {
            localeCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
        }

        IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(entity);
        IPropertyRecordDTO propertyRecord = loadSinglePropertyRecords(baseEntityDAO, tagsRecordDTO.getProperty());
        if (propertyRecord instanceof ITagsRecordDTO) {
          ITagsRecordDTO notifySourcetagsRecord = (ITagsRecordDTO) propertyRecord;

          // Coupling Tag values
          List<IIdRelevance> notifySourceTagInstanceValues = new ArrayList<IIdRelevance>();
          notifySourcetagsRecord.getTags().forEach(tagRecordDTO -> {
            IIdRelevance idRelevance = new IdRelevance();
            idRelevance.setTagId(tagRecordDTO.getTagValueCode());
            idRelevance.setId(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
            idRelevance.setCode(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
            idRelevance.setRelevance(tagRecordDTO.getRange());
            notifySourceTagInstanceValues.add(idRelevance);
          });

          if (couplingDTO.getRecordStatus().equals(RecordStatus.NOTIFIED)) {
            tagInstance.getNotification().put(IContentTagInstance.TAG_VALUES, notifySourceTagInstanceValues);
          }

          ITagConflictingValue tagConflictingValue = new TagConflictingValue();
          tagConflictingValue.setCouplingType(couplingDTO.getCouplingType().toString());
          tagConflictingValue.setTagValues(notifySourceTagInstanceValues);

          if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_CONTEXTUAL) || couplingDTO.getCouplingSourceType()
              .equals(CouplingType.DYN_CONTEXTUAL)) {

            IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByIID(couplingSourceIID);
            IContextConflictingValueSource conflictingValueSource = new ContextConflictingValueSource();
            conflictingValueSource.setType(ContextConflictingValueSource.class.getName());
            conflictingValueSource.setId(classifierDTO.getCode());
            conflictingValueSource.setContentId(Long.toString(sourceEntityIID));
            tagConflictingValue.setSource(conflictingValueSource);
          }
          else if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_RELATIONSHIP) || couplingDTO.getCouplingSourceType()
              .equals(CouplingType.DYN_RELATIONSHIP)) {

            IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(couplingSourceIID);
            IRelationshipConflictingValueSource conflictingValueSource = null;
            conflictingValueSource = new RelationshipConflictingValueSource();
            conflictingValueSource.setType(RelationshipConflictingValueSource.class.getName());
            conflictingValueSource.setId(propertyDTO.getCode());
            conflictingValueSource.setContentId(Long.toString(sourceEntityIID));
            tagConflictingValue.setSource(conflictingValueSource);

          }
          else if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_CLASSIFICATION) || couplingDTO.getCouplingSourceType()
              .equals(CouplingType.DYN_CLASSIFICATION)) {

            IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByIID(couplingSourceIID);
            ITaxonomyConflictingValueSource conflictingValueSource = new TaxonomyConflictingValueSource();
            conflictingValueSource.setType(TaxonomyConflictingValueSource.class.getName());
            conflictingValueSource.setId(classifierDTO.getCode());
            tagConflictingValue.setSource(conflictingValueSource);
          }

          tagInstance.getConflictingValues().add(tagConflictingValue);

        }
      }
    }
  }

  /**
   * **************************************************** Klass Instance
   * Information Model
   *
   * @throws Exception ****************************************************
   */
  public static IKlassInstanceInformationModel getKlassInstanceInformationModel(IBaseEntityDTO baseEntityDTO,
      RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {

    IKlassInstanceInformationModel klassInstanceInformationModel = new KlassInstanceInformationModel();
    klassInstanceInformationModel.setId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    klassInstanceInformationModel.setOriginalInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    // for preview icon
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();

    if (!entityExtension.isEmpty() && entityExtension.containsField("subType")) {
      klassInstanceInformationModel.setBaseType(entityExtension.getInitField("subType", ""));
    }
    else {
      klassInstanceInformationModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDTO.getBaseType()));
    }
    // for name
    klassInstanceInformationModel.setName(baseEntityDTO.getBaseEntityName());
    // for creation language icon
    klassInstanceInformationModel.setCreationLanguage(baseEntityDTO.getBaseLocaleID());
    // for new icon
    klassInstanceInformationModel.setCreatedOn(baseEntityDTO.getCreatedTrack().getWhen());
    // for modified icon
    klassInstanceInformationModel.setLastModified(baseEntityDTO.getLastModifiedTrack().getWhen());
    klassInstanceInformationModel.setLastModifiedBy(baseEntityDTO.getLastModifiedTrack().getWho());

    long parentId = baseEntityDTO.getOriginBaseEntityIID();
    String branchOfLabel = "";
    if (parentId != 0 && baseEntityDTO.isClone()) {
      HashSet<Long> baseEntityIIds = new HashSet<>();
      baseEntityIIds.add(parentId);
      Map<Long, String> baseEntityNamesByIID = rdbmsComponentUtils.getLocaleCatlogDAO().getBaseEntityNamesByIID(baseEntityIIds);
      branchOfLabel = baseEntityNamesByIID.get(parentId);
    }
    klassInstanceInformationModel.setBranchOfLabel(branchOfLabel);
    //Set<String> types = new TreeSet<>();
    List<String> types = new ArrayList();
    types.add(baseEntityDTO.getNatureClassifier().getClassifierCode());
    List<String> selectedTaxonomyIds = new ArrayList<>();
    baseEntityDTO.getOtherClassifiers().forEach(otherClassifier -> {
      if (otherClassifier.getClassifierType().equals(ClassifierType.CLASS)) {
        types.add(otherClassifier.getClassifierCode());
      }
      else {
        selectedTaxonomyIds.add(otherClassifier.getClassifierCode());
      }
    });

    //klassInstanceInformationModel.setTypes(new ArrayList<>(types));
    klassInstanceInformationModel.setTypes(types);
    klassInstanceInformationModel.setSelectedTaxonomyIds(new ArrayList<>(selectedTaxonomyIds));

    List<String> languages = baseEntityDTO.getLocaleIds();
    klassInstanceInformationModel.setLanguageCodes(languages);

    fillAssetInformation(baseEntityDTO, rdbmsComponentUtils, klassInstanceInformationModel, entityExtension);

    IRuleCatalogDAO openRuleDAO = rdbmsComponentUtils.openRuleDAO();
    Set<IRuleViolationDTO> loadViolations = openRuleDAO.loadViolations(baseEntityDTO.getBaseEntityIID());
    IMessageInformation messages = klassInstanceInformationModel.getMessages();
    if (klassInstanceInformationModel.getBaseType().equals(Constants.ASSET_INSTANCE_BASE_TYPE)) {
      klassInstanceInformationModel.setIsAssetExpired(baseEntityDTO.isExpired());
      klassInstanceInformationModel.setIsDuplicate(baseEntityDTO.isDuplicate());
    }
    if (messages == null) {
      klassInstanceInformationModel.setMessages(new MessageInformation());
    }
    messages = klassInstanceInformationModel.getMessages();

    IUniquenessViolationDAO uniquenessDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openUniquenessDAO();
    Integer isUniqueCount = uniquenessDAO.isUniqueRecordForTileView(baseEntityDTO.getBaseEntityIID());
    messages.setIsUniqueViolationCount(isUniqueCount);
    klassInstanceInformationModel.setIsUniqueViolationCount(isUniqueCount);

    for (IRuleViolationDTO violation : loadViolations) {
      Long ruleExpressionIID = Long.valueOf(violation.getRuleExpressionIID());
      String color = violation.getColor();
      if (ruleExpressionIID.equals(-1l)) {
        if (color.equals("red")) {
          messages.setMandatoryViolationCount(messages.getMandatoryViolationCount() + 1);
          klassInstanceInformationModel.setMandatoryViolationCount(messages.getMandatoryViolationCount());
          messages.setIsRed(true);
        }
        else {
          messages.setShouldViolationCount(messages.getShouldViolationCount() + 1);
          klassInstanceInformationModel.setShouldViolationCount(messages.getShouldViolationCount());
          messages.setIsOrange(true);
        }
      }
      else if (violation.getRuleCode().isEmpty()) {
        IClassifierDTO classifier = ConfigurationDAO.instance().getClassifierByIID(ruleExpressionIID);
        if (types.contains(classifier.getCode()) || selectedTaxonomyIds.contains(classifier.getCode())) {
          messages.setIsRed(true);
          messages.setIsUniqueViolationCount(messages.getIsUniqueViolationCount() + 1);
          klassInstanceInformationModel.setIsUniqueViolationCount(klassInstanceInformationModel.getIsUniqueViolationCount() + 1);
        }
      }
      else {
        if (color.equals("red")) {
          messages.setRedCount(messages.getRedCount() + 1);
          messages.setIsRed(true);
        }
        else if (color.equals("yellow")) {
          messages.setYellowCount(messages.getYellowCount() + 1);
          messages.setIsYellow(true);
        }
        else if (color.equals("orange")) {
          messages.setOrangeCount(messages.getOrangeCount() + 1);
          messages.setIsOrange(true);
        }
      }
    }
    return klassInstanceInformationModel;
  }

  private static void fillAssetInformation(IBaseEntityDTO baseEntityDTO, RDBMSComponentUtils rdbmsComponentUtils,
      IKlassInstanceInformationModel klassInstanceInformationModel, IJSONContent entityExtension) throws Exception
  {
    if (baseEntityDTO.getBaseType().equals(BaseType.ASSET) || baseEntityDTO.getBaseType().equals(BaseType.ATTACHMENT)) {
      if (!entityExtension.isEmpty()) {
        entityExtension = baseEntityDTO.getEntityExtension();
        IEventInstanceSchedule eventSchedule = klassInstanceInformationModel.getEventSchedule();
        List<IContentAttributeInstance> attributes = (List<IContentAttributeInstance>) klassInstanceInformationModel.getAttributes();
        if (attributes == null) {
          attributes = new ArrayList<>();
          klassInstanceInformationModel.setAttributes(attributes);
        }
        fillEntityExtensionInAssetCoverFlowAttribute(baseEntityDTO, klassInstanceInformationModel);
      }
    }
    else {
      // TODO: Refactor the code.
      long defaultImageIID = baseEntityDTO.getDefaultImageIID();
      if (defaultImageIID != 0l) {
        klassInstanceInformationModel.setDefaultAssetInstanceId(String.valueOf(defaultImageIID));
      }
    }
  }

  private static void fillEntityExtensionInAssetCoverFlowAttribute(IBaseEntityDTO baseEntityDTO,
      IKlassInstanceInformationModel iKlassInstanceInformationModel) throws Exception
  {
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    if (entityExtension.isEmpty()) {
      return;
    }
    String string = entityExtension.toString();

    IAssetInformationModel assetInformation = ObjectMapperUtil.readValue(string, AssetInformationModel.class);
    iKlassInstanceInformationModel.setAssetInformation(assetInformation);

  }

  @SuppressWarnings("unchecked") public IKlassInstance getKlassInstanceForOverviewTab() throws Exception
  {
    IKlassInstance klassInstance = this.getKlassInstance(baseEntityDTO.getBaseType());
    List<IContentAttributeInstance> attributeInstances = (List<IContentAttributeInstance>) klassInstance.getAttributes();
    List<IContentTagInstance> tagInstances = (List<IContentTagInstance>) klassInstance.getTags();
    ((IContentInstance) klassInstance).setDefaultAssetInstanceId(String.valueOf(baseEntityDTO.getDefaultImageIID()));
    fillAttributesAndTags(klassInstance, attributeInstances, tagInstances);
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();

    if (!entityExtension.isEmpty() && entityExtension.containsField("subType")) {
      klassInstance.setBaseType(entityExtension.getInitField("subType", ""));
    }
    return klassInstance;
  }

  public Map<String, IAttributeVariantsStatsModel> getAttributeVariantsStats()
  {
    Map<String, IAttributeVariantsStatsModel> attributeVariantsStats = new HashMap<>();

    List<IAttribute> referencedAttributes = this.filterAttributeVariant();

    referencedAttributes.forEach(referencedAttribute -> {
      try {
        IAttributeVariantsStatsModel attributeVariantStats = this.getAttributeVariantStats(referencedAttribute);
        if (attributeVariantStats != null) {
          attributeVariantsStats.put(referencedAttribute.getId(), attributeVariantStats);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    return attributeVariantsStats;
  }

  private List<IAttribute> filterAttributeVariant()
  {
    return this.configDetails.getReferencedElements().values().stream().filter(referencedElement -> {
      return (referencedElement.getType()
          .equals(
              CommonConstants.ATTRIBUTE) && (referencedElement.getAttributeVariantContext() != null) && (!referencedElement.getAttributeVariantContext()
          .trim()
          .equals("")));
    }).map(referencedElement -> {
      return this.configDetails.getReferencedAttributes().get(referencedElement.getId());
    }).collect(Collectors.toList());
  }

  protected IAttributeVariantsStatsModel getAttributeVariantStats(IAttribute referencedAttribute) throws Exception
  {
    IAttributeVariantsStatsModel attributeVariantStats = null;
    IPropertyDTO propertyDTO = RDBMSUtils.newPropertyDTO(referencedAttribute);
    boolean isNumeric = (propertyDTO.getPropertyType().equals(PropertyType.MEASUREMENT) || propertyDTO.getPropertyType()
        .equals(PropertyType.NUMBER) || propertyDTO.getPropertyType().equals(PropertyType.PRICE));
    if (isNumeric) {
      long starTime = System.currentTimeMillis();
      IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords(propertyDTO);
      Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
      RDBMSLogger.instance()
          .debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|getAttributeVariantStats|loadPropertyRecords| %d ms",
              System.currentTimeMillis() - starTime);
      if (propertyRecords != null && (!propertyRecords.isEmpty())) {

        attributeVariantStats = new AttributeVariantsStatsModel();

        DoubleSummaryStatistics doubleSummaryStatistics = propertyRecords.stream().mapToDouble(propertyRecord -> {
          if (propertyRecord instanceof IValueRecordDTO) {
            IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
            double numberValue = valueRecord.getAsNumber();
            if (numberValue == 0) {
              numberValue = Double.parseDouble(valueRecord.getValue());
            }
            return numberValue;
          }
          return 0;
        }).summaryStatistics();

        attributeVariantStats.setAttributeId(referencedAttribute.getId());
        attributeVariantStats.setAvg(doubleSummaryStatistics.getAverage());
        attributeVariantStats.setMax(doubleSummaryStatistics.getMax());
        attributeVariantStats.setMin(doubleSummaryStatistics.getMin());
        attributeVariantStats.setCount(doubleSummaryStatistics.getCount());
        attributeVariantStats.setSum(doubleSummaryStatistics.getSum());
      }
    }
    return attributeVariantStats;
  }

  public void fillAttributeTagProperties(IKlassInstanceInformationModel klassInstanceInformationModel) throws Exception
  {
    List<IContentAttributeInstance> attributeInstances = new ArrayList<>();
    List<IContentTagInstance> tagInstances = new ArrayList<>();

    TreeSet<IPropertyRecordDTO> propertyRecords = (TreeSet<IPropertyRecordDTO>) baseEntityDTO.getPropertyRecords();
    fillAttributesAndTags(null, attributeInstances, tagInstances);
    klassInstanceInformationModel.setAttributes(attributeInstances);
    klassInstanceInformationModel.setTags(tagInstances);
  }
  
  public void fillAttributeTagPropertiesForInstanceTree(IKlassInstanceInformationModel klassInstanceInformationModel, Set<IPropertyRecordDTO> propertyRecords) throws Exception
  {
    List<IContentAttributeInstance> attributeInstances = new ArrayList<>();
    List<IContentTagInstance> tagInstances = new ArrayList<>();
    fillAttributesAndTags(null, attributeInstances, tagInstances);

    klassInstanceInformationModel.setAttributes(attributeInstances);
    klassInstanceInformationModel.setTags(tagInstances);
  }

  public static void fillEntityExtensionInAssetCoverFlowAttribute(List<IContentAttributeInstance> attributes, IBaseEntityDTO baseEntityDTO)
      throws Exception
  {
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    if (entityExtension.isEmpty()) {
      return;
    }
    entityExtension.setField(IImageAttributeInstance.BASE_TYPE, Constants.IMAGE_ATTRIBUTE_INSTANCE_TYPE);
    String string = entityExtension.toString();

    IImageAttributeInstance imageAttributeInstance = ObjectMapperUtil.readValue(string, ImageAttributeInstance.class);
    imageAttributeInstance.setAttributeId(IStandardConfig.StandardProperty.assetcoverflowattribute.toString());
    imageAttributeInstance.setId(IStandardConfig.StandardProperty.assetcoverflowattribute.toString());
    imageAttributeInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    imageAttributeInstance.setIsDefault(true);
    attributes.add(imageAttributeInstance);
  }

  private void handleLanguageInheritanceNotificationForValueRecord(List<IContentAttributeInstance> attributeInstances,
      Set<Long> propertyIIDs) throws RDBMSException
  {
    Map<Long, IValueRecordNotificationDTO> languageNotifiedProperties = baseEntityDAO.getLanguageNotifiedProperties(propertyIIDs);
    for (IContentAttributeInstance attributeInstanceModel : attributeInstances) {
      AttributeInstance attributeInstance = (AttributeInstance) attributeInstanceModel;
      // split the combine id
      String id = attributeInstance.getId();
      String[] split = id.split("-");
      Long propertyIID = new Long(split[1]);

      if (propertyIIDs.contains(propertyIID)) {
        IValueRecordNotificationDTO valueRecordNotificationDTO = languageNotifiedProperties.get(propertyIID);
        if (valueRecordNotificationDTO != null && !StringUtils.isEmpty(valueRecordNotificationDTO.getValue())) {
          Map<String, Object> notification = new HashMap<>();
          notification.put("attributeValue", valueRecordNotificationDTO.getValue());
          attributeInstance.setNotification(notification);

          IAttributeConflictingValue attributeConflictingValue = new AttributeConflictingValue();
          attributeConflictingValue.setValue(valueRecordNotificationDTO.getValue());
          attributeConflictingValue.setId(baseEntityDAO.getBaseEntityDTO().getBaseEntityID());
          attributeConflictingValue.setValueAsHtml(valueRecordNotificationDTO.getAsHTML());

          // prepare source of conflicting values
          IConflictingValueSource source = new LanguageConflictingValueSource();
          source.setId(valueRecordNotificationDTO.getLocaleID());
          source.setType(CommonConstants.LANGUAGE_CONFLICTING_SOURCE_TYPE);
          source.setLastModifiedBy(attributeInstance.getLastModifiedBy());

          attributeConflictingValue.setSource(source);
          attributeInstance.getConflictingValues().add(attributeConflictingValue);
        }
      }
    }
  }

  private void getAllPropertyIIDsNeedsToNotified(String dataLanguage, Set<Long> propertyIIDs, IPropertyRecordDTO propertyRecord)
  {
    if (propertyRecord instanceof IValueRecordDTO) {
      IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;
      if (!StringUtils.isEmpty(valueRecordDTO.getLocaleID()) && valueRecordDTO.getLocaleID().contains(dataLanguage)) {
        propertyIIDs.add(valueRecordDTO.getProperty().getPropertyIID());
      }
    }
  }

  private IPropertyRecordDTO loadSinglePropertyRecords(IBaseEntityDAO baseEntityDAO, IPropertyDTO propertyDTO) throws Exception
  {
    IPropertyRecordDTO propertyRecordDTO = null;
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords(propertyDTO);
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
    if (propertyRecords != null && propertyRecords.size() == 1) {
      propertyRecordDTO = propertyRecords.iterator().next();
    }
    return propertyRecordDTO;
  }
  
  public static IKlassInstanceInformationModel getKlassInstanceInformationModelWithoutViolations(IBaseEntityDTO baseEntityDTO,
      RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {

    IKlassInstanceInformationModel klassInstanceInformationModel = new KlassInstanceInformationModel();
    klassInstanceInformationModel.setId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    klassInstanceInformationModel.setOriginalInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    // for preview icon
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();

    if (!entityExtension.isEmpty() && entityExtension.containsField("subType")) {
      klassInstanceInformationModel.setBaseType(entityExtension.getInitField("subType", ""));
    }
    else {
      klassInstanceInformationModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDTO.getBaseType()));
    }
    // for name
    klassInstanceInformationModel.setName(baseEntityDTO.getBaseEntityName());
    // for creation language icon
    klassInstanceInformationModel.setCreationLanguage(baseEntityDTO.getBaseLocaleID());
    // for new icon
    klassInstanceInformationModel.setCreatedOn(baseEntityDTO.getCreatedTrack().getWhen());
    // for modified icon
    klassInstanceInformationModel.setLastModified(baseEntityDTO.getLastModifiedTrack().getWhen());
    klassInstanceInformationModel.setLastModifiedBy(baseEntityDTO.getLastModifiedTrack().getWho());
    Set<String> types = new TreeSet<>();
    types.add(baseEntityDTO.getNatureClassifier().getClassifierCode());
    List<String> selectedTaxonomyIds = new ArrayList<>();
    baseEntityDTO.getOtherClassifiers().forEach(otherClassifier -> {
      if (otherClassifier.getClassifierType().equals(ClassifierType.CLASS)) {
        types.add(otherClassifier.getClassifierCode());
      }
      else {
        selectedTaxonomyIds.add(otherClassifier.getClassifierCode());
      }
    });

    klassInstanceInformationModel.setTypes(new ArrayList<>(types));
    klassInstanceInformationModel.setSelectedTaxonomyIds(new ArrayList<>(selectedTaxonomyIds));

    List<String> languages = baseEntityDTO.getLocaleIds();
    klassInstanceInformationModel.setLanguageCodes(languages);

    fillAssetInformation(baseEntityDTO, rdbmsComponentUtils, klassInstanceInformationModel, entityExtension);

    if (klassInstanceInformationModel.getBaseType().equals(Constants.ASSET_INSTANCE_BASE_TYPE)) {
      klassInstanceInformationModel.setIsAssetExpired(baseEntityDTO.isExpired());
      klassInstanceInformationModel.setIsDuplicate(baseEntityDTO.isDuplicate());
    }
    return klassInstanceInformationModel;
  }

  public static IAttributeVariantsStatsModel getAttributeVariantStatsForEntity(IAttribute referencedAttribute, Set<IPropertyRecordDTO> propertyRecords) throws Exception
  {
    IAttributeVariantsStatsModel attributeVariantStats = null;
    IPropertyDTO propertyDTO = RDBMSUtils.newPropertyDTO(referencedAttribute);
    boolean isNumeric = (propertyDTO.getPropertyType().equals(PropertyType.MEASUREMENT) || propertyDTO.getPropertyType()
        .equals(PropertyType.NUMBER) || propertyDTO.getPropertyType().equals(PropertyType.PRICE));
    if (isNumeric) {
      if (propertyRecords != null && (!propertyRecords.isEmpty())) {

        attributeVariantStats = new AttributeVariantsStatsModel();

        DoubleSummaryStatistics doubleSummaryStatistics = propertyRecords.stream().mapToDouble(propertyRecord -> {
          if (propertyRecord instanceof IValueRecordDTO) {
            IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
            double numberValue = valueRecord.getAsNumber();
            if (numberValue == 0) {
              numberValue = Double.parseDouble(valueRecord.getValue());
            }
            return numberValue;
          }
          return 0;
        }).summaryStatistics();

        attributeVariantStats.setAttributeId(referencedAttribute.getId());
        attributeVariantStats.setAvg(doubleSummaryStatistics.getAverage());
        attributeVariantStats.setMax(doubleSummaryStatistics.getMax());
        attributeVariantStats.setMin(doubleSummaryStatistics.getMin());
        attributeVariantStats.setCount(doubleSummaryStatistics.getCount());
        attributeVariantStats.setSum(doubleSummaryStatistics.getSum());
      }
    }
    return attributeVariantStats;
  }
}
