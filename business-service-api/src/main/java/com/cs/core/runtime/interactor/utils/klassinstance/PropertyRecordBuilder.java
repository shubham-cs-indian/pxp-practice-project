package com.cs.core.runtime.interactor.utils.klassinstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttributeOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedHtmlOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTOBuilder;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.datarule.ContextConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.IContextConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflictingValueSource;
import com.cs.core.runtime.interactor.entity.language.ILanguageConflictingValueSource;
import com.cs.core.runtime.interactor.entity.language.LanguageConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.relationship.IRelationshipConflictingValueSource;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipConflictingValueSource;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.exception.tags.InvalidLifeCycleStateAddedException;
import com.cs.core.runtime.interactor.model.attribute.IModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class PropertyRecordBuilder {
  
  public enum PropertyRecordType
  {
    
    DEFAULT, CREATE, UPDATE, DEFAULT_COUPLED;
    
    private static final PropertyRecordType[] values = values();
    
    public static PropertyRecordType valueOf(int ordinal)
    {
      return values[ordinal];
    }
  };
  
  private final ILocaleCatalogDAO                           localeCatalogDAO;
  private final IBaseEntityDAO                              entityDAO;
  private final Map<String, IAttribute>                     attributes;
  private final Map<String, ITag>                           tags;
  private final PropertyRecordType                          propertyRecordType;
  private final Map<String, IReferencedSectionElementModel> referencedSectionElements;
  private final Map<String, IReferencedVariantContextModel> embeddedVariantContexts;
  private Set<IPropertyDTO>                                 couplingNotificationProperty       = new HashSet<IPropertyDTO>();
  private Set<IPropertyRecordDTO>                           couplingNotificationPropertyRecord = new HashSet<IPropertyRecordDTO>();
  
  public PropertyRecordBuilder(IBaseEntityDAO entityDAO, IGetConfigDetailsModel configDetails,
      PropertyRecordType propertyRecordType, ILocaleCatalogDAO localeCatalogDAO)
  {
    this.localeCatalogDAO = localeCatalogDAO;
    this.entityDAO = entityDAO;
    this.attributes = configDetails.getReferencedAttributes();
    this.tags = configDetails.getReferencedTags();
    this.referencedSectionElements = configDetails.getReferencedElements();
    this.embeddedVariantContexts = configDetails.getReferencedVariantContexts()
        .getEmbeddedVariantContexts();
    this.propertyRecordType = propertyRecordType;
  }
  
  public PropertyRecordBuilder(IBaseEntityDAO entityDAO, Map<String, IAttribute> attributes, Map<String, ITag> tags,
      Map<String, IReferencedSectionElementModel> referencedSectionElements, PropertyRecordType propertyRecordType,
      ILocaleCatalogDAO localeCatalogDAO)
  {
    this.localeCatalogDAO = localeCatalogDAO;
    this.entityDAO = entityDAO;
    this.attributes = attributes;
    this.tags = tags;
    this.referencedSectionElements = referencedSectionElements;
    this.propertyRecordType = propertyRecordType;
    this.embeddedVariantContexts = null;
  }  
  
  public Set<IPropertyDTO> getCouplingNotificationProperty()
  {
    return couplingNotificationProperty;
  }

  
  public Set<IPropertyRecordDTO> getCouplingNotificationPropertyRecord()
  {
    return couplingNotificationPropertyRecord;
  }

  /**
   * ************************************************************
   *  Attribute Record 
   * ************************************************************
   */
  public IPropertyRecordDTO createValueRecord(IAttribute attributeConfig) throws Exception
  {
    String attributeId = attributeConfig.getId();
    IReferencedSectionAttributeModel attributeElement = (IReferencedSectionAttributeModel) this.referencedSectionElements
        .get(attributeId);
    
    IPropertyRecordDTO buildValueRecord = null;
    // If attribute contain attribute context then avoid to create value record
    // for default value
    if (attributeElement != null) {
      if (this.propertyRecordType == PropertyRecordType.DEFAULT) {
        IReferencedVariantContextModel attributeContextConfig = null;
        if (attributeElement.getAttributeVariantContext() != null && (!attributeElement.getAttributeVariantContext().trim().equals(""))) {
          attributeContextConfig = this.embeddedVariantContexts.get(attributeElement.getAttributeVariantContext());
        }
        buildValueRecord = this.buildValueRecord(attributeElement, attributeConfig, attributeContextConfig, null);
      }
      else if (this.propertyRecordType == PropertyRecordType.DEFAULT_COUPLED) {
        
        // If attribute is loosely coupled or default value is empth or contain
          // attribute context then avoid to create value record
        if (attributeElement.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)
            || attributeElement.getAttributeVariantContext() != null
            || attributeConfig.getType().equals(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE)
            || attributeConfig.getType().equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
          return buildValueRecord;
        }
          buildValueRecord = this.buildValueRecord(attributeElement, attributeConfig, null, null);
      }
    }
    return buildValueRecord;
  }
  
  public IPropertyRecordDTO updateValueRecord(IPropertyInstance attributeInstance) throws Exception
  {
    String attributeId = ((IContentAttributeInstance) attributeInstance).getAttributeId();
    IAttribute attributeConfig = this.attributes.get(attributeId);
    IReferencedSectionAttributeModel attributeElement = (IReferencedSectionAttributeModel) this.referencedSectionElements
        .get(attributeId);
    IReferencedVariantContextModel attributeContextConfig = null;
    
    //In case of relationship extension object no embedded attribute support
    if (this.embeddedVariantContexts != null && attributeElement != null) { 
      attributeContextConfig = this.embeddedVariantContexts
          .get(attributeElement.getAttributeVariantContext());
    }
    
     IPropertyRecordDTO valueRecord = this.buildValueRecord(attributeElement, attributeConfig, attributeContextConfig,
        attributeInstance);

      if(valueRecord == null){
        valueRecord = new ValueRecordDTO.ValueRecordDTOBuilder( Long.parseLong(attributeInstance.getKlassInstanceId()), ConfigurationDAO.instance().getPropertyByCode(attributeId), "").build();
      }
    if (attributeContextConfig != null) {
      ((IValueRecordDTO) valueRecord).getContextualObject().setAllowDuplicate(attributeContextConfig.getIsDuplicateVariantAllowed());
    }
     this.resolveCoupledConflictForAttributes(valueRecord, attributeInstance, attributeElement, attributeConfig);
     return valueRecord;
  }
  
  private IPropertyRecordDTO buildValueRecord(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IPropertyInstance attributeInstance) throws Exception
  {
    PropertyType propertyType = null;
    IPropertyRecordDTO propertyRecordDTO = null;
    if (attributeConfig != null) {
      switch (attributeConfig.getType()) {
      case CommonConstants.TEXT_ATTRIBUTE_TYPE:
        propertyType = PropertyType.TEXT;
        propertyRecordDTO = createTextAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case CommonConstants.NUMBER_ATTRIBUTE_TYPE:
        propertyType = PropertyType.NUMBER;
        propertyRecordDTO = createNumberAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case CommonConstants.PRICE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.PRICE;
        propertyRecordDTO = createPriceAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case Constants.NAME_ATTRIBUTE_TYPE:
        propertyType = PropertyType.TEXT;
        propertyRecordDTO = createNameAttribute(attributeElement, attributeConfig, attributeInstance, propertyType);
        break;
      
      case CommonConstants.CALCULATED_ATTRIBUTE_TYPE:
        propertyType = PropertyType.CALCULATED;
        propertyRecordDTO = createCalculatedAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case CommonConstants.CONCATENATED_ATTRIBUTE_TYPE:
        propertyType = PropertyType.CONCATENATED;
        propertyRecordDTO = createConcatenatedAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case CommonConstants.DATE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.DATE;
        propertyRecordDTO = createDateAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case CommonConstants.HTML_TYPE_ATTRIBUTE:
        propertyType = PropertyType.HTML;
        propertyRecordDTO = createHTMLAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case CommonConstants.LENGTH_ATTRIBUTE_TYPE:
      case CommonConstants.AREA_ATTRIBUTE_TYPE:
      case CommonConstants.AREA_PER_VOLUME_ATTRIBUTE_TYPE:
      case CommonConstants.CAPACITANCE_ATTRIBUTE_TYPE:
      case CommonConstants.ACCELERATION_ATTRIBUTE_TYPE:
      case CommonConstants.CHARGE_ATTRIBUTE_TYPE:
      case CommonConstants.CONDUCTANCE_ATTRIBUTE_TYPE:
      case CommonConstants.CURRENT_ATTRIBUTE_TYPE:
      case CommonConstants.CURRENCY_ATTRIBUTE_TYPE:
      case CommonConstants.CUSTOM_UNIT_ATTRIBUTE_TYPE:
      case CommonConstants.DIGITAL_STORAGE_ATTRIBUTE_TYPE:
      case CommonConstants.ENERGY_ATTRIBUTE_TYPE:
      case CommonConstants.FREQUENCY_ATTRIBUTE_TYPE:
      case CommonConstants.HEATING_RATE_ATTRIBUTE_TYPE:
      case CommonConstants.ILLUMINANCE_ATTRIBUTE_TYPE:
      case CommonConstants.INDUCTANCE_ATTRIBUTE_TYPE:
      case CommonConstants.FORCE_ATTRIBUTE_TYPE:
      case CommonConstants.LUMINOSITY_ATTRIBUTE_TYPE:
      case CommonConstants.MAGNETISM_ATTRIBUTE_TYPE:
      case CommonConstants.MASS_ATTRIBUTE_TYPE:
      case CommonConstants.POTENTIAL_ATTRIBUTE_TYPE:
      case CommonConstants.POWER_ATTRIBUTE_TYPE:
      case CommonConstants.PROPORTION_ATTRIBUTE_TYPE:
      case CommonConstants.RADIATION_ATTRIBUTE_TYPE:
      case CommonConstants.ROTATION_FREQUENCY_ATTRIBUTE_TYPE:
      case CommonConstants.SPEED_ATTRIBUTE_TYPE:
      case CommonConstants.SUBSTANCE_ATTRIBUTE_TYPE:
      case CommonConstants.TEMPERATURE_ATTRIBUTE_TYPE:
      case CommonConstants.THERMAL_INSULATION_ATTRIBUTE_TYPE:
      case CommonConstants.VISCOCITY_ATTRIBUTE_TYPE:
      case CommonConstants.VOLUME_ATTRIBUTE_TYPE:
      case CommonConstants.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE:
      case CommonConstants.WEIGHT_PER_TIME_ATTRIBUTE_TYPE:
      case CommonConstants.WEIGHT_PER_AREA_ATTRIBUTE_TYPE:
      case CommonConstants.RESISTANCE_ATTRIBUTE_TYPE:
      case CommonConstants.PRESSURE_ATTRIBUTE_TYPE:
      case CommonConstants.PLANE_ANGLE_ATTRIBUTE_TYPE:
      case CommonConstants.DENSITY_ATTRIBUTE_TYPE:
      case CommonConstants.TIME_ATTRIBUTE_TYPE:  
        propertyType = PropertyType.MEASUREMENT;
        propertyRecordDTO = createMeasurementAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case CommonConstants.ASSET_METADATA_ATTRIBUTE_TYPE:
        propertyType = propertyType.ASSET_ATTRIBUTE;
        propertyRecordDTO = createAssetAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      case Constants.IMAGE_COVER_FLOW_ATTRIBUTE_BASE_TYPE:
        propertyType = propertyType.ASSET_ATTRIBUTE;
        propertyRecordDTO = createAssetAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;
      
      default:
        propertyRecordDTO = null;
        break;
    }
   } 
    return propertyRecordDTO;
  }
  
  
  
  private IPropertyDTO newAttributeProperty(IAttribute attributeConfig,
      IPropertyDTO.PropertyType propertyType) throws Exception
  {
    long starTime1 = System.currentTimeMillis();
    IPropertyDTO propertyDTO = this.entityDAO.newPropertyDTO(attributeConfig.getPropertyIID(),
        attributeConfig.getCode(), propertyType);
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|newAttributeProperty|newPropertyDTO| %d ms",
            System.currentTimeMillis() - starTime1);
    return propertyDTO;
  }
  
  private IValueRecordDTO newValueRecordDTO(long propertyRecordIID, long valueIID, String value, String localeID, IContextDTO context,
      IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig, PropertyType propertyType) throws Exception
  {
    IPropertyDTO propertyDTO = this.newAttributeProperty(attributeConfig, propertyType);
    long starTime1 = System.currentTimeMillis();
    IValueRecordDTOBuilder valueRecordDTOBuilder = this.entityDAO.newValueRecordDTOBuilder(propertyDTO, value).valueIID(valueIID)
        .localeID(localeID)
        .isVersionable(attributeElement == null ? attributeConfig.getIsVersionable() : attributeElement.getIsVersionable());
    if (context != null)
      valueRecordDTOBuilder.contextDTO(context);
    
    IValueRecordDTO valueRecordDTO = valueRecordDTOBuilder.build();
    RDBMSLogger.instance().debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|newValueRecordDTO|newValueRecordDTO| %d ms",
        System.currentTimeMillis() - starTime1);
    return valueRecordDTO;
  }
  
  public IValueRecordDTO buildValueRecord(long propertyRecordIID, long valueRecordIID, String value,
      String localeID, IContextDTO contextDTO, IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.newValueRecordDTO(propertyRecordIID, valueRecordIID,
        value, localeID, contextDTO, attributeElement, attributeConfig, propertyType);
    return valueRecordDTO;
  }
  
  public static long getValueIIDFromAttributeID(String prpertyRecordId) throws Exception
  {
    String[] prpertyRecordIdArr = prpertyRecordId
        .split(KlassInstanceBuilder.ATTRIBUTE_ID_SEPARATOR);
    long propRec = 0l;
    if (prpertyRecordIdArr.length == 3) {
      propRec = Long.parseLong(prpertyRecordIdArr[2]);
    }
    return propRec;
  }
  
  public static long getEntityIIDFromAttributeID(String prpertyRecordId) throws Exception
  {
    String[] prpertyRecordIdArr = prpertyRecordId
        .split(KlassInstanceBuilder.ATTRIBUTE_ID_SEPARATOR);
    long propRec = 0l;
    if (prpertyRecordIdArr.length == 3) {
      propRec = Long.parseLong(prpertyRecordIdArr[0]);
    }
    return propRec;
  }
  
  public static long getPropertyIIDFromAttributeID(String prpertyRecordId) throws Exception
  {
    String[] prpertyRecordIdArr = prpertyRecordId
        .split(KlassInstanceBuilder.ATTRIBUTE_ID_SEPARATOR);
    long propRec = 0l;
    if (prpertyRecordIdArr.length == 3) {
      propRec = Long.parseLong(prpertyRecordIdArr[1]);
    }
    return propRec;
  }
  
  private IValueRecordDTO buildValueRecord(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IPropertyInstance attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = null;
    long propertyRecordIID = 0L;
    long valueRecordIID = 0L;
    String value = null;
    String localeID = "";
    if (this.propertyRecordType == PropertyRecordType.DEFAULT
        && StringUtils.isNoneEmpty(attributeElement.getDefaultValue())) {
      propertyRecordIID = 0L;
      valueRecordIID = 0L;
      value = attributeElement.getDefaultValue();
      if (attributeConfig.getIsTranslatable()) {
        localeID = this.localeCatalogDAO.getLocaleCatalogDTO().getLocaleID();
      }
      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, value, localeID,
          null, attributeElement, attributeConfig, propertyType);
    } 
    else if (this.propertyRecordType == PropertyRecordType.DEFAULT_COUPLED) {
      propertyRecordIID = 0L;
      valueRecordIID = 0L;
      value = attributeElement.getDefaultValue();
      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, value, localeID,
          null, attributeElement, attributeConfig, propertyType);

      
    }
    else if (this.propertyRecordType == PropertyRecordType.UPDATE) {
      IModifiedAttributeInstanceModel modifiedAttributeInstanceModel = (IModifiedAttributeInstanceModel) attributeInstance;
      propertyRecordIID = 0l;
      valueRecordIID = PropertyRecordBuilder.getValueIIDFromAttributeID(modifiedAttributeInstanceModel.getId());
      value = modifiedAttributeInstanceModel.getValue();
      if (attributeConfig.getIsTranslatable()) {
        localeID = modifiedAttributeInstanceModel.getLanguage();
      }
      if (modifiedAttributeInstanceModel.getModifiedContext() != null
          && attributeContextConfig != null) {
        valueRecordDTO = this.updateAttributeContextInstance(propertyRecordIID, valueRecordIID,
            value, localeID, modifiedAttributeInstanceModel, attributeElement, attributeConfig,
            attributeContextConfig, propertyType);
      } else if(attributeElement != null && attributeElement.getCouplingType() != null 
          && ! attributeElement.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)) {
        valueRecordDTO = this.updateCoupledAttributeInstance(propertyRecordIID, valueRecordIID, value, localeID, 
            modifiedAttributeInstanceModel, attributeElement, attributeConfig, propertyType);
      } else {
        valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, value, localeID,
            null, attributeElement, attributeConfig, propertyType);
        
        if(attributeConfig.getIsTranslatable()) {
          
          ICouplingDAO couplingDAO = localeCatalogDAO.openCouplingDAO();
          
          ILanguageConfigDTO languageConfig = ConfigurationDAO.instance().getLanguageConfig(localeCatalogDAO.getLocaleCatalogDTO().getLocaleID());
          
          List<ICouplingDTO> coupledRecord = couplingDAO.getCoupledRecord(entityDAO.getBaseEntityDTO().getBaseEntityIID(), 
              attributeConfig.getPropertyIID(), languageConfig.getLanguageIID());
          
          if(!coupledRecord.isEmpty()) {
            ((PropertyRecordDTO) valueRecordDTO).setRecordStatus(RecordStatus.COUPLED);
            ((PropertyRecordDTO) valueRecordDTO).setCouplingType(CouplingType.LANG_INHERITANCE);
          }
        }
      }
    }
    else if (this.propertyRecordType == PropertyRecordType.CREATE) {
      IAttributeInstance attrInstance = (IAttributeInstance) attributeInstance;
      propertyRecordIID = 0L;
      valueRecordIID = 0L;
      value = attrInstance.getValue();
      if (attributeConfig.getIsTranslatable()) {
        localeID = attrInstance.getLanguage();
      }
      IContextDTO contextDTO = this.getContextDTO(attributeElement, attributeConfig,
          attributeContextConfig, attrInstance, propertyType);
      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, value, localeID,
          contextDTO, attributeElement, attributeConfig, propertyType);
      this.createAttributeContextInstance(valueRecordDTO.getContextualObject(), attributeElement,
          attributeConfig, attributeContextConfig, attrInstance, propertyType);
    }
    return valueRecordDTO;
  }
  
  private IContextDTO getContextDTO(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IAttributeInstance attrInstance, PropertyType propertyType) throws Exception
  {
    IContextDTO contextDTO = null;
    if (attrInstance.getContext() != null && attributeContextConfig != null) {
      contextDTO = RDBMSUtils.getContextDTO(attributeContextConfig);
    }
    return contextDTO;
  }
  
  private void createAttributeContextInstance(IContextualDataDTO contextualObject,
      IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig,
      IReferencedVariantContextModel attributeContextConfig, IAttributeInstance attrInstance,
      PropertyType propertyType) throws Exception
  {
    if (attrInstance.getContext() != null && attributeContextConfig != null
        && contextualObject != null) {
      this.createContextTags((List<ITagInstance>) attrInstance.getTags(), contextualObject);
      this.setContextTimeRange(attrInstance.getContext()
          .getTimeRange(), contextualObject);
    }
  }
  
  public void createContextTags(List<? extends IContentTagInstance> tagInstances,
      IContextualDataDTO contextualDataDTO) throws Exception
  {
    tagInstances.forEach(tagInstance -> {
      try {
        long contextInstanceId =  (tagInstance instanceof ITagInstance) && ((ITagInstance) tagInstance).getContextInstanceId() != null ? Long.parseLong(((ITagInstance) tagInstance).getContextInstanceId()) : 0;
        if(contextualDataDTO.getContextualObjectIID() == contextInstanceId) {
          ITag tagConfig = this.tags.get(tagInstance.getTagId());
          List<ITagDTO> TagRecords = this.getAddedTagValues(tagInstance.getTagValues(), tagConfig);
          contextualDataDTO.getContextTagValues()
          .addAll(TagRecords);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
  
  public boolean setContextTimeRange(IInstanceTimeRange timeRange,
      IContextualDataDTO contextualDataDTO) throws Exception
  {
    if (timeRange != null) {
      Long from = timeRange.getFrom() == null ? 0 : timeRange.getFrom();
      Long to = timeRange.getTo() == null ? 0 : timeRange.getTo();
      if (from.equals(contextualDataDTO.getContextStartTime())
          && to.equals(contextualDataDTO.getContextEndTime())) {
        return false;
      }
      contextualDataDTO.setContextStartTime(from);
      contextualDataDTO.setContextEndTime(to);
      return true;
    }
    return false;
  }
  
  private IValueRecordDTO updateAttributeContextInstance(long propertyRecordIID,
      long valueRecordIID, String value, String localeID,
      IModifiedAttributeInstanceModel modifiedAttributeInstanceModel,
      IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig,
      IReferencedVariantContextModel attributeContextConfig, PropertyType propertyType)
      throws Exception
  {
    IValueRecordDTO valueRecordDTO = loadValueRecordByValueIID(valueRecordIID,
        this.entityDAO, attributeConfig);
    if (valueRecordDTO != null) {
      valueRecordDTO.setValue(value);
      //In case of numeric attribute, correct number value will be set in respective create attribute method.
      valueRecordDTO.setAsNumber(0);
      this.updateAttributeContextTags(modifiedAttributeInstanceModel,
          valueRecordDTO.getContextualObject());
      this.setContextTimeRange(modifiedAttributeInstanceModel.getModifiedContext()
          .getTimeRange(), valueRecordDTO.getContextualObject());
    }
    return valueRecordDTO;
  }
  
  public void updateAttributeContextTags(
      IModifiedAttributeInstanceModel modifiedAttributeInstanceModel,
      IContextualDataDTO contextualDataDTO) throws Exception
  {
    // Handle modified tag
    this.updateModifiedContextTags(modifiedAttributeInstanceModel.getModifiedTags(),
        contextualDataDTO);
    
    // Handle added tag
    this.createContextTags(modifiedAttributeInstanceModel.getAddedTags(), contextualDataDTO);
  }
  
  public void updateModifiedContextTags(List<IModifiedContentTagInstanceModel> modifiedTags,
      IContextualDataDTO contextualDataDTO) throws Exception
  {
    Set<ITagDTO> contextTagValues = contextualDataDTO.getContextTagValues();
    modifiedTags.forEach(modifiedTag -> {
      try {
        long contextInstanceId =  (modifiedTag instanceof ITagInstance) && ((ITagInstance) modifiedTag).getContextInstanceId() != null ? Long.parseLong(((ITagInstance) modifiedTag).getContextInstanceId()) : 0;
        if(contextualDataDTO.getContextualObjectIID() == contextInstanceId) {
          ITag tagConfig = this.tags.get(modifiedTag.getTagId());
          
          // Handle deleted tag values
          this.deleteTagValues(modifiedTag.getDeletedTagValues(), contextTagValues);
          
          // Handle modified tag values
          List<ITagDTO> modifiedTagValues = this
              .getmodifiedTagValues(modifiedTag.getModifiedTagValues(), tagConfig);
          modifiedTagValues.forEach(modifiedTagValue -> {
            contextTagValues.remove(modifiedTagValue);
            contextTagValues.add(modifiedTagValue);
          });
          
          // Handle added tag values
          List<ITagDTO> addedTagValues = this.getAddedTagValues(modifiedTag.getAddedTagValues(),
              tagConfig);
          contextTagValues.addAll(addedTagValues);
          ITagDTO[] tagDtos = contextTagValues.toArray(new ITagDTO[contextTagValues.size()]);
          contextualDataDTO.setContextTagValues(tagDtos);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
  
  private IValueRecordDTO updateCoupledAttributeInstance(long propertyRecordIID,
      long valueRecordIID, String value, String localeID,
      IModifiedAttributeInstanceModel modifiedAttributeInstanceModel,
      IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig,PropertyType propertyType)
      throws Exception
  {
    IValueRecordDTO valueRecordDTO = loadValueRecordByValueIID(valueRecordIID,
        this.entityDAO, attributeConfig);
    if (valueRecordDTO != null) {
      valueRecordDTO.setValue(value);
    }
    return valueRecordDTO;
  }
  
  private IValueRecordDTO createTextAttribute(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IPropertyInstance attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    // do any Attribute specific settings
    if (valueRecordDTO != null) {
      
    }
    return valueRecordDTO;
  }
  
  public IValueRecordDTO createCalculatedAttribute(
      IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig,
      IReferencedVariantContextModel attributeContextConfig, IPropertyInstance attributeInstance,
      PropertyType propertyType) throws Exception
  {
    long propertyRecordIID = 0L;
    long valueRecordIID = 0L;
    String localeID = "";
    ICalculatedAttribute calculatedAttribute = ((ICalculatedAttribute) attributeConfig);
    List<IAttributeOperator> attributeOperatorList = calculatedAttribute.getAttributeOperatorList();
    
    StringBuilder mathExpression = new StringBuilder();
    if (!attributeOperatorList.isEmpty()) {
      mathExpression.append("= ");
    }
    
    for (IAttributeOperator attributeOperator : attributeOperatorList) {
      
      String attributeOperatorType = attributeOperator.getType();
      
      switch (attributeOperatorType) {
        
        case "ATTRIBUTE":
          if (referencedSectionElements.containsKey(attributeOperator.getAttributeId())) {
            IAttribute iAttribute = attributes.get(attributeOperator.getAttributeId());
            mathExpression.append("[" + iAttribute.getCode() + "]");
          }
          else {
            mathExpression.append("0");
          }
          break;
        
        case "ADD":
          mathExpression.append(" +");
          break;
        
        case "SUBTRACT":
          mathExpression.append(" -");
          break;
        
        case "MULTIPLY":
          mathExpression.append(" *");
          break;
        
        case "DIVIDE":
          mathExpression.append(" /");
          break;
        
        case "VALUE":
          mathExpression.append(" " + attributeOperator.getValue());
          break;
        
        case "OPENING_BRACKET":
          mathExpression.append(" (");
          break;
        
        case "CLOSING_BRACKET":
          mathExpression.append(" )");
          break;
      }
    }
    
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, "",
        localeID, null, attributeElement, attributeConfig, propertyType);
    
    if (valueRecordDTO != null && mathExpression.length() != 0) {
      try {
        valueRecordDTO.addCalculation(mathExpression.toString());
      }
      catch (CSFormatException e) {
        RDBMSLogger.instance().exception(e);
      }
    }
    return valueRecordDTO;
  }
  
  private IValueRecordDTO createConcatenatedAttribute(
      IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig,
      IReferencedVariantContextModel attributeContextConfig, IPropertyInstance attributeInstance,
      PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = null;
    long propertyRecordIID = 0L;
    long valueRecordIID = 0L;
    String localeID = null;
    if (propertyRecordType == PropertyRecordType.DEFAULT) {
      
      localeID = this.entityDAO.getBaseEntityDTO()
          .getBaseLocaleID();
      
      if (attributeConfig.getIsTranslatable()) {
        localeID = this.localeCatalogDAO.getLocaleCatalogDTO().getLocaleID();
      }
      
      IConcatenatedAttribute concatenatedAttribute = (IConcatenatedAttribute) attributeConfig;
      List<IConcatenatedOperator> attributeConcatenatedList = concatenatedAttribute
          .getAttributeConcatenatedList();
      
      StringBuilder mathExpression = createExpression(attributeConcatenatedList);
      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, "", localeID, null,
          attributeElement, attributeConfig, propertyType);
      if (valueRecordDTO != null && mathExpression.length() != 0)
        try {
          valueRecordDTO.addCalculation(mathExpression.toString());
        }
        catch (CSFormatException e) {
          RDBMSLogger.instance().exception(e);
        }
    }
    else if (propertyRecordType == PropertyRecordType.UPDATE) {
      IModifiedAttributeInstanceModel modifiedAttributeInstanceModel = (IModifiedAttributeInstanceModel) attributeInstance;
      propertyRecordIID = 0l;
      valueRecordIID = PropertyRecordBuilder.getValueIIDFromAttributeID(modifiedAttributeInstanceModel.getId());
      localeID = modifiedAttributeInstanceModel.getLanguage() == null
          ? this.entityDAO.getBaseEntityDTO()
              .getBaseLocaleID()
          : modifiedAttributeInstanceModel.getLanguage();
      
      String refereshedCalculation = refreshCalculation(attributeConfig.getPropertyIID(),
          modifiedAttributeInstanceModel.getValueAsExpression(), attributeConfig.getCode());
      
      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, "", localeID, null,
          attributeElement, attributeConfig, propertyType);
      
      try {
        valueRecordDTO.addCalculation(refereshedCalculation);
      }
      catch (CSFormatException e) {
        RDBMSLogger.instance().exception(e);
      }
    }
    return valueRecordDTO;
  }
  
  private String refreshCalculation(long propertyIID, List<IConcatenatedOperator> expressions,
      String code) throws RDBMSException, CSFormatException
  {
    IPropertyDTO propertyDTO = new PropertyDTO(propertyIID, code, PropertyType.CONCATENATED);
    
    IBaseEntityDTO loadPropertyRecords = this.entityDAO.loadPropertyRecords(propertyDTO);
    IValueRecordDTO valueRecord = (IValueRecordDTO) loadPropertyRecords
        .getPropertyRecord(propertyIID);
    
    String calculation = valueRecord.getCalculation();
    String replace = calculation.replace("=", "");
    String[] split = replace.split("\\|\\|");
    
    for (IConcatenatedOperator expression : expressions) {
      if (!(expression instanceof IConcatenatedHtmlOperator)) {
        continue;
      }
      IConcatenatedHtmlOperator valueExpression = (IConcatenatedHtmlOperator) expression;
      if(!valueExpression.getValueAsHtml().isEmpty()) {
        String valueAsHTML = valueExpression.getValueAsHtml();
        valueExpression.setValueAsHtml( StringEscapeUtils.escapeHtml4(valueAsHTML));
      }
      split[valueExpression.getOrder()] = "'" + valueExpression.getValueAsHtml() + "'";
    }
    String refereshedCalculation = "=".concat(String.join("||", split));
    return refereshedCalculation;
  }
  
  public StringBuilder createExpression(List<IConcatenatedOperator> attributeConcatenatedList)
  {
    
    StringBuilder mathExpression = new StringBuilder();
    for (IConcatenatedOperator concatenatedAttributeOperator : attributeConcatenatedList) {
      
      if (mathExpression.length() == 0) {
        mathExpression.append("=");
      }
      
      String type = concatenatedAttributeOperator.getType();
      switch (type) {
        
        case "attribute":
          IConcatenatedAttributeOperator attributeOperator = (IConcatenatedAttributeOperator) concatenatedAttributeOperator;
          if (referencedSectionElements.containsKey(attributeOperator.getAttributeId())) {
            IAttribute configSourceAttribute = attributes.get(attributeOperator.getAttributeId());
            mathExpression.append("[" + configSourceAttribute.getCode() + "]||");
          }
          break;
        
        case "html":
          IConcatenatedHtmlOperator attributeOperatorForHtml = (IConcatenatedHtmlOperator) concatenatedAttributeOperator;
          String valueAsHtml = attributeOperatorForHtml.getValueAsHtml() == null ? "" : attributeOperatorForHtml.getValueAsHtml();
          mathExpression.append("'" + StringEscapeUtils.escapeHtml4(valueAsHtml) + "'||");
          break;
        
        case "tag":
          IConcatenatedTagOperator tagOperator = (IConcatenatedTagOperator) concatenatedAttributeOperator;
          if (referencedSectionElements.containsKey(tagOperator.getTagId())) {
            ITag tag = tags.get(tagOperator.getTagId());
            /*IReferencedSectionTagModel iReferencedSectionElementModel = (IReferencedSectionTagModel) referencedSectionElements.get(tagOperator.getTagId());
            List<IIdRelevance> defaultValue = iReferencedSectionElementModel.getDefaultValue();
            for(IIdRelevance relevance: defaultValue) {
              mathExpression.append("[" + relevance.getTagId()+ "]");
            }*/
            mathExpression.append("[" + tag.getCode() + "]||");
          }
          break;
      }
    }
    mathExpression.setLength(mathExpression.length() > 1 ? mathExpression.length() - 2 : 0);
    return mathExpression;
  }
  
  private IValueRecordDTO createPriceAttribute(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IPropertyInstance attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    // do any Attribute specific settings
    if (valueRecordDTO != null) {
      String unitSymbol = attributeElement.getDefaultUnit() != null
          ? attributeElement.getDefaultUnit()
          : ((IUnitAttribute) attributeConfig).getDefaultUnit();
      valueRecordDTO.setUnitSymbol(unitSymbol);
      if (!StringUtils.isEmpty(valueRecordDTO.getValue())) {
        valueRecordDTO.setAsNumber(Double.parseDouble(valueRecordDTO.getValue()));
      }
    }
    return valueRecordDTO;
  }
  
  private IValueRecordDTO createDateAttribute(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IPropertyInstance attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    // do any Attribute specific settings
    if (valueRecordDTO != null) {
      if (!StringUtils.isEmpty(valueRecordDTO.getValue())) {
        valueRecordDTO.setAsNumber(Double.parseDouble(valueRecordDTO.getValue()));
      }
    }
    return valueRecordDTO;
  }
  
  private IValueRecordDTO createMeasurementAttribute(
      IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig,
      IReferencedVariantContextModel attributeContextConfig, IPropertyInstance attributeInstance,
      PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    if (valueRecordDTO != null) {
      // Do any Measurement Attribute specific settings
      String unitSymbol = attributeElement.getDefaultUnit() != null
          ? attributeElement.getDefaultUnit()
          : ((IUnitAttribute) attributeConfig).getDefaultUnit();
      valueRecordDTO.setUnitSymbol(unitSymbol);
      if (!StringUtils.isEmpty(valueRecordDTO.getValue())) {
        valueRecordDTO.setAsNumber(Double.parseDouble(valueRecordDTO.getValue()));
      }
    }
    return valueRecordDTO;
  }
  
  private IValueRecordDTO createNumberAttribute(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IPropertyInstance attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    if (valueRecordDTO != null) {
      // Do any Number Attribute specific settings
      if (!StringUtils.isEmpty(valueRecordDTO.getValue())) {
        valueRecordDTO.setAsNumber(Double.parseDouble(valueRecordDTO.getValue()));
      }
    }
    return valueRecordDTO;
  }
  
  private IValueRecordDTO createHTMLAttribute(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IPropertyInstance attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    if (valueRecordDTO != null) {
      if (this.propertyRecordType == PropertyRecordType.DEFAULT || this.propertyRecordType == PropertyRecordType.DEFAULT_COUPLED ) {
        valueRecordDTO.setAsHTML(attributeElement.getValueAsHtml());
      }
      else if (this.propertyRecordType == PropertyRecordType.UPDATE) {
        valueRecordDTO
            .setAsHTML(((IModifiedAttributeInstanceModel) attributeInstance).getValueAsHtml());
      }
      else if (this.propertyRecordType == PropertyRecordType.CREATE) {
        valueRecordDTO.setAsHTML(((IAttributeInstance) attributeInstance).getValueAsHtml());
      }
    }
    return valueRecordDTO;
  }
  
  private IValueRecordDTO createNameAttribute(IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig,
      IPropertyInstance attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = null;
    if (this.propertyRecordType == PropertyRecordType.UPDATE) {
      valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig, null, attributeInstance,
          propertyType);
    }
    else if (this.propertyRecordType == PropertyRecordType.CREATE) {
      valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig, null, attributeInstance,
          propertyType);
    }
    return valueRecordDTO;
  }
  
  private IValueRecordDTO createAssetAttribute(IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig, IReferencedVariantContextModel attributeContextConfig,
      IPropertyInstance attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    
    if (valueRecordDTO != null) {
      // Do any Asset Attribute specific settings
    }
    return valueRecordDTO;
  }
  
  public IPropertyRecordDTO createStandardNameAttribute(String name) throws Exception
  {
    String baseLocaleID = "";
    if(localeCatalogDAO== null) {
      baseLocaleID = this.entityDAO.getBaseEntityDTO().getBaseLocaleID();
    }
    else {
      baseLocaleID = localeCatalogDAO.getLocaleCatalogDTO().getLocaleID();
    }
    IAttribute attributeConfig = this.attributes.get(CommonConstants.NAME_ATTRIBUTE);
    IReferencedSectionAttributeModel attributeElement = (IReferencedSectionAttributeModel) referencedSectionElements.get(CommonConstants.NAME_ATTRIBUTE);
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(0l, 0L, name,
        baseLocaleID, null, attributeElement, attributeConfig, PropertyType.TEXT);
    return valueRecordDTO;
  }
  
  // TODO refactor method for multiple valueRecord
  @SuppressWarnings("unused")
  public IPropertyRecordDTO resolveCoupledConflictForAttributes(IPropertyRecordDTO valueRecordDTO,
      IPropertyInstance attributeInstance, IReferencedSectionAttributeModel attributeElement,
      IAttribute attributeConfig) throws Exception
  {
    if (attributeElement != null && this.propertyRecordType == PropertyRecordType.UPDATE) {
      ICouplingDAO couplingDAO = localeCatalogDAO.openCouplingDAO();
      IModifiedAttributeInstanceModel modifiedAttributeInstanceModel = (IModifiedAttributeInstanceModel) attributeInstance;
      IConflictingValueSource source = modifiedAttributeInstanceModel.getSource();
      String contentId = "";
      
      String dataLanguage = localeCatalogDAO.getLocaleCatalogDTO().getLocaleID();
      ILanguageConfigDTO languageConfig = ConfigurationDAO.instance().getLanguageConfig(dataLanguage);
      Long localeIID = 0L;
      if (attributeConfig.getIsTranslatable()) {
        localeIID = languageConfig.getLanguageIID();
      }
      if(source != null) {
        
        if(source.getType().equals(ContextConflictingValueSource.class.getName())) {
          IContextConflictingValueSource contextualSource = (IContextConflictingValueSource) source;
          contentId = contextualSource.getContentId();
        }
        else if(source.getType().equals(RelationshipConflictingValueSource.class.getName())) {
          IRelationshipConflictingValueSource relationshipSource = (IRelationshipConflictingValueSource) source;
          contentId = relationshipSource.getContentId();
        }
        else if (source.getType().equals(TaxonomyConflictingValueSource.class.getName())) {
          ITaxonomyConflictingValueSource classificationSource = (ITaxonomyConflictingValueSource) source;
          contentId = String.valueOf(RDBMSUtils.getDefaultLocaleCatalogDAO().getEntityByID(classificationSource.getId()).getBaseEntityIID());
        }
        
        else if (source.getType().equals(LanguageConflictingValueSource.class.getName())) {
          ILanguageConflictingValueSource contextualSource = (ILanguageConflictingValueSource) source;
          contentId = contextualSource.getContentId();
        }
        
        String couplingType = "";
        
        couplingType = getCouplingTypeForModifiedAttribute(modifiedAttributeInstanceModel, contentId, couplingType);
        ICouplingDTO couplingDTO = localeCatalogDAO.newCouplingDTOBuilder().build();
        prepareCouplingDTOToResolveConflict(valueRecordDTO, source, contentId, couplingType, couplingDTO, localeIID);
        couplingDAO.resolvedConflicts(couplingDTO);
        
      }else if(!valueRecordDTO.isCoupled()){
        
        couplingDAO.updateTargetConflictingValues(valueRecordDTO.getProperty().getPropertyIID(), 
            entityDAO.getBaseEntityDTO().getBaseEntityIID(), localeIID);
      }
    }
    return valueRecordDTO;
  }


  private void prepareCouplingDTOToResolveConflict(IPropertyRecordDTO valueRecordDTO, IConflictingValueSource source, String contentId,
      String couplingType, ICouplingDTO couplingDTO, Long languageIID) throws RDBMSException
  {
    couplingDTO.setSourceEntityIID(Long.parseLong(contentId));
    couplingDTO.setTargetEntityIID(entityDAO.getBaseEntityDTO().getBaseEntityIID());
    couplingDTO.setPropertyIID(valueRecordDTO.getProperty().getPropertyIID());
    couplingDTO.setLocaleIID(languageIID);
    
    if(source.getType().equals(ContextConflictingValueSource.class.getName())) {
      
      if(couplingType.equals(CouplingBehavior.TIGHTLY.toString())) {
        couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
        couplingDTO.setCouplingSourceType(CouplingType.TIGHT_CONTEXTUAL);
      }
      if(couplingType.equals(CouplingBehavior.DYNAMIC.toString())) {
        couplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
        couplingDTO.setCouplingSourceType(CouplingType.DYN_CONTEXTUAL);
      }
      couplingDTO.setCouplingSourceIID(ConfigurationDAO.instance().getClassifierByCode(source.getId()).getClassifierIID());
      
    }else if(source.getType().equals(RelationshipConflictingValueSource.class.getName())) {
      
      if(couplingType.equals(CouplingBehavior.TIGHTLY.toString())) {
        couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
        couplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
      }
      if(couplingType.equals(CouplingBehavior.DYNAMIC.toString())) {
        couplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
        couplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP);
      }
      couplingDTO.setCouplingSourceIID(ConfigurationDAO.instance().getPropertyByCode(source.getId()).getPropertyIID());
    } else if(source.getType().equals(TaxonomyConflictingValueSource.class.getName())) {
      
      if(couplingType.equals(CouplingBehavior.TIGHTLY.toString())) {
        couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
        couplingDTO.setCouplingSourceType(CouplingType.TIGHT_CLASSIFICATION);
      }
      if(couplingType.equals(CouplingBehavior.DYNAMIC.toString())) {
        couplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
        couplingDTO.setCouplingSourceType(CouplingType.DYN_CLASSIFICATION);
      }
      couplingDTO.setCouplingSourceIID(ConfigurationDAO.instance().getClassifierByCode(source.getId()).getClassifierIID());
    }
    else if (source.getType().equals(LanguageConflictingValueSource.class.getName())) {
      
      if (couplingType.equals(CouplingBehavior.TIGHTLY.toString())) {
        couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
        couplingDTO.setCouplingSourceType(CouplingType.LANG_INHERITANCE);
        String sourceLanguageCode = source.getId();
        ILanguageConfigDTO languageConfig = ConfigurationDAO.instance().getLanguageConfig(sourceLanguageCode);
        couplingDTO.setCouplingSourceIID(languageConfig.getLanguageIID());
      }
    }
  }


  private String getCouplingTypeForModifiedAttribute(IModifiedAttributeInstanceModel modifiedAttributeInstanceModel, String contentId,
      String couplingType)
  {
    List<IAttributeConflictingValue> conflictingValues = modifiedAttributeInstanceModel.getConflictingValues();
    for(IAttributeConflictingValue conflictingValue: conflictingValues) {
       IConflictingValueSource conflictingValueSource = conflictingValue.getSource();
       if(conflictingValueSource.getType().equals(ContextConflictingValueSource.class.getName())) {
         IContextConflictingValueSource contextualSource = (IContextConflictingValueSource) conflictingValueSource;
         if(contextualSource.getContentId().equals(contentId)) {
           couplingType = conflictingValue.getCouplingType();
           break;
         }
       }
       else if(conflictingValueSource.getType().equals(RelationshipConflictingValueSource.class.getName())) {
         IRelationshipConflictingValueSource relationshipSource = (RelationshipConflictingValueSource) conflictingValueSource;
         if(relationshipSource.getContentId().equals(contentId)) {
           couplingType = conflictingValue.getCouplingType();
           break;
         }
      }
      else if (conflictingValueSource.getType().equals(TaxonomyConflictingValueSource.class.getName())) {
        ITaxonomyConflictingValueSource classifierSource = (ITaxonomyConflictingValueSource) conflictingValueSource;
          couplingType = conflictingValue.getCouplingType();
          break;
      }
      
       else if(conflictingValueSource.getType().equals(LanguageConflictingValueSource.class.getName())) {
         ILanguageConflictingValueSource languageSource = (LanguageConflictingValueSource) conflictingValueSource;
         if(languageSource.getContentId().equals(contentId)) {
           couplingType = conflictingValue.getCouplingType();
           break;
         }
       }
    }
    return couplingType;
  }

  
  /**
   * ************************************************************ 
   * Tag Record
   * ************************************************************
   */
  public IPropertyRecordDTO createTagsRecord(ITag tagConfig) throws Exception
  {
    String tagId = tagConfig.getId();
    IReferencedSectionTagModel tagElement = (IReferencedSectionTagModel) this.referencedSectionElements
        .get(tagId);
    // Below check of null is added to handle the case related to mandatory/Default tags, because those tags 
    // are not present in referenced elements.
    if ((tagElement == null && Constants.DEFAULT_TAG_IDS.contains(tagId)) || tagElement.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)) {
      return this.buildTagsRecord(tagElement, tagConfig, null);
    }
    // in case of tag record is coupled
    return null;
  }
  
  
  public IPropertyRecordDTO createTagsRecordForDefaultInstance(ITag tagConfig) throws Exception
  {
    String tagId = tagConfig.getId();
    IReferencedSectionTagModel tagElement = (IReferencedSectionTagModel) this.referencedSectionElements
        .get(tagId);
    if(tagElement != null) {
      return this.buildTagsRecordForDefaultInstance(tagElement, tagConfig, null);
    }
    return null;
  }
  
  public IPropertyRecordDTO updateTagsRecord(IPropertyInstance tagInstance) throws Exception
  {
    String tagId = ((IContentTagInstance) tagInstance).getTagId();
    ITag tagConfig = this.tags.get(tagId);
    IReferencedSectionTagModel tagElement = (IReferencedSectionTagModel) this.referencedSectionElements
        .get(tagId);
    IPropertyRecordDTO tagsRecord = this.buildTagsRecord(tagElement, tagConfig, tagInstance);

    this.resolveCoupledConflictForTags(tagsRecord, tagInstance, tagElement, tagConfig);
    return tagsRecord;
  }
  
  private IPropertyRecordDTO buildTagsRecord(IReferencedSectionTagModel tagElement, ITag tagConfig,
      IPropertyInstance tagInstance) throws Exception
  {
    IPropertyRecordDTO recordDTO = null;
    PropertyType propertyType = PropertyType.TAG;
    switch (tagConfig.getTagType()) {
      case CommonConstants.YES_NEUTRAL_TAG_TYPE_ID:
        recordDTO = createYesNeutralTag(tagElement, tagConfig, tagInstance, propertyType);
        break;
      case SystemLevelIds.LIFECYCLE_STATUS_TAG_TYPE_ID:
        recordDTO = createLifeStatusTag(tagElement, tagConfig, tagInstance, propertyType);
        break;
      case SystemLevelIds.LISTING_STATUS_TAG_TYPE_ID:
        recordDTO = createListingStatusTag(tagElement, tagConfig, tagInstance, propertyType);
        break;
      case SystemLevelIds.BOOLEAN_TAG_TYPE_ID:
        recordDTO = createBooleanTag(tagElement, tagConfig, tagInstance, PropertyType.BOOLEAN);
      default:
        recordDTO = buildTagsRecordDTO(tagElement, tagConfig, tagInstance, propertyType);
        break;
    }
    return recordDTO;
  }
  
  private IPropertyRecordDTO buildTagsRecordForDefaultInstance(IReferencedSectionTagModel tagElement, ITag tagConfig,
      IPropertyInstance tagInstance) throws Exception
  {
    
    ITagsRecordDTO recordDTO = null;
    if (this.propertyRecordType != PropertyRecordType.DEFAULT_COUPLED || tagElement.getDefaultValue() == null
        || tagElement.getDefaultValue().isEmpty() || tagElement.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)) {
      return recordDTO;
    }
    PropertyType propertyType = PropertyType.TAG;
    long propertyRecordIID = 0L;
    switch (tagConfig.getTagType()) {
      case CommonConstants.YES_NEUTRAL_TAG_TYPE_ID:
        propertyRecordIID = 0L;
        List<IIdRelevance> tagDefaultvalues = tagElement.getDefaultValue();
        if (tagDefaultvalues != null && !tagDefaultvalues.isEmpty()) {
          recordDTO = buildTagsRecord(propertyRecordIID, tagElement, tagConfig, propertyType);
          Set<ITagDTO> tagRecords = recordDTO.getTags();
          for (IIdRelevance tagDefaultvalue : tagDefaultvalues) {
            if (tagDefaultvalue.getRelevance() != 0) {
              ITagDTO newTagRecordDTO = this.newTagRecordDTO(tagDefaultvalue.getTagId(), tagDefaultvalue.getRelevance());
              tagRecords.add(newTagRecordDTO);
            }
          }
        }
        
        break;
      case SystemLevelIds.LIFECYCLE_STATUS_TAG_TYPE_ID:
        recordDTO = this.buildStandardTagsRecordWithDefaultChild(propertyRecordIID, tagElement, tagConfig, propertyType,
            Constants.LIFE_STATUS_INBOX);
        break;
      case SystemLevelIds.LISTING_STATUS_TAG_TYPE_ID:
        recordDTO = this.buildStandardTagsRecordWithDefaultChild(propertyRecordIID, tagElement, tagConfig, propertyType,
            SystemLevelIds.LISTING_STATUS_CATLOG);
        break;
      case SystemLevelIds.BOOLEAN_TAG_TYPE_ID:
        recordDTO = this.newTagsRecordDTO(propertyRecordIID, tagConfig, propertyType);
        
      default:
        recordDTO = buildTagsRecordDTO(tagElement, tagConfig, tagInstance, propertyType);
        break;
    }
    return recordDTO;
  }
  
  private IPropertyDTO newTagProperty(ITag tagConfig, IPropertyDTO.PropertyType propertyType)
      throws Exception
  {
    long starTime1 = System.currentTimeMillis();
    IPropertyDTO propertyDTO = this.entityDAO.newPropertyDTO(tagConfig.getPropertyIID(),
        tagConfig.getCode(), propertyType);
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|newTagProperty|newPropertyDTO| %d ms",
            System.currentTimeMillis() - starTime1);
    return propertyDTO;
  }
  
  private ITagsRecordDTO newTagsRecordDTO(long propertyRecordIID, ITag tagConfig,
      PropertyType propertyType) throws Exception
  {
    IPropertyDTO newPropertyDTO = this.newTagProperty(tagConfig, propertyType);
    long starTime1 = System.currentTimeMillis();
    ITagsRecordDTO tagsRecordDTO = this.entityDAO.newTagsRecordDTOBuilder(newPropertyDTO).isVersionable(tagConfig.getIsVersionable()).build();
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|newTagProperty|newPropertyDTO| %d ms",
            System.currentTimeMillis() - starTime1);
    return tagsRecordDTO;
  }
  
  private ITagDTO newTagRecordDTO(String tagValueCode, int relevance) throws Exception
  {
    long starTime1 = System.currentTimeMillis();
    ITagDTO tagRecordDTO = this.entityDAO.newTagDTO(relevance, tagValueCode);
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|newTagRecordDTO|newTagDTO| %d ms",
            System.currentTimeMillis() - starTime1);
    return tagRecordDTO;
  }
  
  private ITagsRecordDTO buildTagsRecord(long propertyRecordIID,
      IReferencedSectionTagModel tagElement, ITag tagConfig, PropertyType propertyType)
      throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = this.newTagsRecordDTO(propertyRecordIID, tagConfig,
        propertyType);
    return tagsRecordDTO;
  }
  
  private IValueRecordDTO newBooleanValueRecordDTO(long propertyRecordIID, long valueIID,
      String value, String localeID, IContextDTO context, ITag tagConfig, PropertyType propertyType)
      throws Exception
  {
    IPropertyDTO propertyDTO = this.newTagProperty(tagConfig, propertyType);
    long starTime1 = System.currentTimeMillis();
    IValueRecordDTO valueRecordDTO = this.entityDAO.newValueRecordDTOBuilder(propertyDTO, value)
        .valueIID(valueIID)
        .localeID(localeID)
        .contextDTO(context)
        .build();
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|newBooleanValueRecordDTO|newValueRecordDTO| %d ms",
            System.currentTimeMillis() - starTime1);
    return valueRecordDTO;
  }
  
  private IValueRecordDTO buildBooleanRecord(long propertyRecordIID, long valueRecordIID,
      String value, String localeID, IReferencedSectionTagModel tagElement, ITag tagConfig,
      PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.newBooleanValueRecordDTO(propertyRecordIID,
        valueRecordIID, value, localeID, null, tagConfig, propertyType);
    return valueRecordDTO;
  }
  
  private ITagsRecordDTO buildStandardTagsRecordWithDefaultChild(long propertyRecordIID,
      IReferencedSectionTagModel tagElement, ITag tagConfig, PropertyType propertyType,
      String childId) throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = this.newTagsRecordDTO(propertyRecordIID, tagConfig,
        propertyType);
    Optional<? extends ITreeEntity> defaultTagValueOption = tagConfig.getChildren()
        .stream()
        .filter(tagChildern -> tagChildern.getId()
            .equals(childId))
        .findFirst();
    ITag defaultTagValue = (ITag) defaultTagValueOption.get();
    tagsRecordDTO.getTags()
        .add(this.newTagRecordDTO(defaultTagValue.getCode(), 100));
    return tagsRecordDTO;
  }
  
  private ITagsRecordDTO buildTagsRecordDTO(IReferencedSectionTagModel tagElement, ITag tagConfig,
      IPropertyInstance tagInstance, PropertyType propertyType) throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = null;
    long propertyRecordIID = 0L;
    if (this.propertyRecordType == PropertyRecordType.UPDATE) {
      IModifiedContentTagInstanceModel modifiedContentTagInstanceModel = (IModifiedContentTagInstanceModel) tagInstance;
      tagsRecordDTO = this.loadTagsRecord(tagConfig, propertyType);
      // manage deleted tag values
      this.deleteTagValues(modifiedContentTagInstanceModel.getDeletedTagValues(),
          tagsRecordDTO.getTags());
      // manage modified tag values
      List<ITagDTO> modifiedTagValues = this
          .getmodifiedTagValues(modifiedContentTagInstanceModel.getModifiedTagValues(), tagConfig);
      // manage added tag values
      modifiedTagValues.addAll(
          this.getAddedTagValues(modifiedContentTagInstanceModel.getAddedTagValues(), tagConfig));
      tagsRecordDTO.mergeTags((modifiedTagValues.toArray(new ITagDTO[modifiedTagValues.size()])));
    }
    else if (this.propertyRecordType == PropertyRecordType.CREATE) {
      ITagInstance tagInst = (ITagInstance) tagInstance;
      propertyRecordIID = 0L;
      tagsRecordDTO = this.buildTagsRecord(propertyRecordIID, tagElement, tagConfig, propertyType);
      List<ITagDTO> addedTagValues = this.getAddedTagValues(tagInst.getTagValues(), tagConfig);
      tagsRecordDTO.setTags((addedTagValues.toArray(new ITagDTO[addedTagValues.size()])));
    }
    return tagsRecordDTO;
  }
  
  private ITagsRecordDTO loadTagsRecord(ITag referencedTag, PropertyType propertyType)
      throws Exception
  {
    IPropertyDTO propertyDTO = this.newTagProperty(referencedTag, propertyType);
    long entityIID = entityDAO.getBaseEntityDTO().getBaseEntityIID();
    Set<IPropertyRecordDTO> loadPropertyRecords = localeCatalogDAO.getPropertyRecordsForEntities(Set.of(entityIID),
        propertyDTO).get(entityIID);

    return (ITagsRecordDTO) loadPropertyRecords.stream().findFirst().orElse(new TagsRecordDTO.
        TagsRecordDTOBuilder(entityDAO.getBaseEntityDTO().getBaseEntityIID(), propertyDTO).build());
  }
  
  private void deleteTagValues(List<String> deletedTagValueIds, Set<ITagDTO> tagRecords)
      throws Exception
  {
    /*for(ITagDTO tagRecord : tagRecords) {
      if(deletedTagValueIds.contains(tagRecord.getTagValueCode())) {
        tagRecord.setRange(0);
      }
    }*/
    tagRecords.removeIf(
        tagRecord -> deletedTagValueIds.contains(String.valueOf(tagRecord.getTagValueCode())));
  }
  
  private List<ITagDTO> getmodifiedTagValues(List<ITagInstanceValue> modifiedTagValuesList,
      ITag referencedTag)
  {
    if (modifiedTagValuesList == null || modifiedTagValuesList.isEmpty())
      return new ArrayList<ITagDTO>();
    List<ITagDTO> modifiedTagRecords = new ArrayList<>();

    for (ITagInstanceValue modifiedTagValue : modifiedTagValuesList) {
      try {
        if (modifiedTagValue.getRelevance() != 0) {
          modifiedTagRecords
              .add(this.newTagRecordDTO(modifiedTagValue.getCode(), modifiedTagValue.getRelevance()));
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    /*List<ITagDTO> modifiedTagValues = modifiedTagValuesList.stream()
        .map(tagInstanceValue -> {
          try {
            return this.newTagRecordDTO(tagInstanceValue.getCode(),
                tagInstanceValue.getRelevance());
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toList());*/
    return modifiedTagRecords;
  }

  private List<ITagDTO> getAddedTagValues(List<ITagInstanceValue> addedTagValues, ITag tagConfig)
  {
    if (addedTagValues == null || addedTagValues.isEmpty())
      return new ArrayList<ITagDTO>();
    
    // FIXME Temp fix for getting tagValueIID. Ideally it come from UI
    Map<String, ITag> tagIdTagMap = this.getTagIdTagMap((List<ITag>) tagConfig.getChildren());
    List<ITagDTO> addedTagRecords = new ArrayList<>();
    for (ITagInstanceValue addedTagValue : addedTagValues) {
      try {
        if (addedTagValue.getRelevance() != 0) {
          ITag tagValueReference = tagIdTagMap.get(addedTagValue.getTagId());
          addedTagRecords
              .add(this.newTagRecordDTO(tagValueReference.getCode(), addedTagValue.getRelevance()));
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    /*List<ITagDTO> addedTagRecords = addedTagValues.stream()
        .map(tagInstanceValue -> {
          ITag tagValueReference = tagIdTagMap.get(tagInstanceValue.getTagId());
          try {
            if(tagInstanceValue.getRelevance() != 0) {
              return this.newTagRecordDTO(tagValueReference.getCode(),
                  tagInstanceValue.getRelevance());
            }
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toList());*/
    return addedTagRecords;
  }
  
  private Map<String, ITag> getTagIdTagMap(List<ITag> childrenTags)
  {
    return childrenTags.stream()
        .collect(Collectors.toMap(tag -> tag.getId(), tag -> tag));
  }
  
  private ITagsRecordDTO createYesNeutralTag(IReferencedSectionTagModel tagElement, ITag tagConfig,
      IPropertyInstance tagInstance, PropertyType propertyType) throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = null;
    long propertyRecordIID = 0L;
    if (this.propertyRecordType == PropertyRecordType.DEFAULT) {
      propertyRecordIID = 0L;
      List<IIdRelevance> tagDefaultvalues = tagElement.getDefaultValue();
      if (tagDefaultvalues != null && !tagDefaultvalues.isEmpty()) {
        tagsRecordDTO = buildTagsRecord(propertyRecordIID, tagElement, tagConfig, propertyType);
        Set<ITagDTO> tagRecords = tagsRecordDTO.getTags();
        for (IIdRelevance tagDefaultvalue : tagDefaultvalues) {
          if(tagDefaultvalue.getRelevance() != 0) {
            ITagDTO newTagRecordDTO = this.newTagRecordDTO(tagDefaultvalue.getTagId(),
                tagDefaultvalue.getRelevance());
            tagRecords.add(newTagRecordDTO);
          }
        }
      }
    }
    else {
      tagsRecordDTO = this.buildTagsRecordDTO(tagElement, tagConfig, tagInstance, propertyType);
    }
    // do any Yes Neutral Tag specific settings
    if (tagsRecordDTO != null) {
    }
    return tagsRecordDTO;
  }
  
  private ITagsRecordDTO createLifeStatusTag(IReferencedSectionTagModel tagElement, ITag tagConfig,
      IPropertyInstance tagInstance, PropertyType propertyType) throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = null;
    long propertyRecordIID = 0L;
    if (this.propertyRecordType == PropertyRecordType.DEFAULT) {
      propertyRecordIID = 0L;
      tagsRecordDTO = this.buildStandardTagsRecordWithDefaultChild(propertyRecordIID, tagElement,
          tagConfig, propertyType, Constants.LIFE_STATUS_INBOX);
    }
    else {
      tagsRecordDTO = this.buildTagsRecordDTO(tagElement, tagConfig, tagInstance, propertyType);
    }
    // doTag specific settings
    if (tagsRecordDTO != null) {
    }
    return tagsRecordDTO;
  }
  
  private ITagsRecordDTO createListingStatusTag(IReferencedSectionTagModel tagElement,
      ITag tagConfig, IPropertyInstance tagInstance, PropertyType propertyType) throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = null;
    long propertyRecordIID = 0L;
    if (this.propertyRecordType == PropertyRecordType.DEFAULT) {
      propertyRecordIID = 0L;
      tagsRecordDTO = this.buildStandardTagsRecordWithDefaultChild(propertyRecordIID, tagElement,
          tagConfig, propertyType, SystemLevelIds.LISTING_STATUS_CATLOG);
    } 
    else {
      tagsRecordDTO = this.buildTagsRecordDTO(tagElement, tagConfig, tagInstance, propertyType);
    }
    // doTag specific settings
    if (tagsRecordDTO != null) {
    }
    return tagsRecordDTO;
  }
  
  private ITagsRecordDTO createBooleanTag(IReferencedSectionTagModel tagElement, ITag tagConfig,
      IPropertyInstance tagInstance, PropertyType propertyType) throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = null;
    long propertyRecordIID = 0L;
    if (this.propertyRecordType == PropertyRecordType.DEFAULT) {
      propertyRecordIID = 0L;
      tagsRecordDTO = this.newTagsRecordDTO(propertyRecordIID, tagConfig, propertyType);
    }
    else {
      tagsRecordDTO = this.buildTagsRecordDTO(tagElement, tagConfig, tagInstance, propertyType);
    }
    return tagsRecordDTO;
  }
  
  private String getCouplingTypeForModifiedTag(ModifiedTagInstanceModel modifiedAttributeInstanceModel, String contentId,
      String couplingType)
  {
    List<ITagConflictingValue> conflictingValues = modifiedAttributeInstanceModel.getConflictingValues();
    for (ITagConflictingValue conflictingValue : conflictingValues) {
      IConflictingValueSource conflictingValueSource = conflictingValue.getSource();
      if (conflictingValueSource.getType().equals(ContextConflictingValueSource.class.getName())) {
        IContextConflictingValueSource contextualSource = (IContextConflictingValueSource) conflictingValueSource;
        if (contextualSource.getContentId().equals(contentId)) {
          couplingType = conflictingValue.getCouplingType();
          break;
        }
      }
      else if (conflictingValueSource.getType().equals(RelationshipConflictingValueSource.class.getName())) {
        IRelationshipConflictingValueSource relationshipSource = (RelationshipConflictingValueSource) conflictingValueSource;
        if (relationshipSource.getContentId().equals(contentId)) {
          couplingType = conflictingValue.getCouplingType();
          break;
        }
      }
      else if (conflictingValueSource.getType().equals(TaxonomyConflictingValueSource.class.getName())) {
        ITaxonomyConflictingValueSource relationshipSource = (ITaxonomyConflictingValueSource) conflictingValueSource;
        couplingType = conflictingValue.getCouplingType();
        break;
      }
      else if (conflictingValueSource.getType().equals(LanguageConflictingValueSource.class.getName())) {
        ILanguageConflictingValueSource languageSource = (LanguageConflictingValueSource) conflictingValueSource;
        if (languageSource.getContentId().equals(contentId)) {
          couplingType = conflictingValue.getCouplingType();
          break;
        }
      }
    }
    return couplingType;
  }


  @SuppressWarnings("unused")
  public IPropertyRecordDTO resolveCoupledConflictForTags(IPropertyRecordDTO tagsRecord,
      IPropertyInstance tagInstance, IReferencedSectionTagModel tagElement, ITag tagConfig)
      throws Exception
  {
    
    if (tagElement != null && tagElement.getCouplingType() != null && !tagElement.getCouplingType()
        .equals(CommonConstants.LOOSELY_COUPLED)
        && this.propertyRecordType == PropertyRecordType.UPDATE) {
      
      ICouplingDAO couplingDAO = localeCatalogDAO.openCouplingDAO();
      
      ModifiedTagInstanceModel modifiedContentTagInstanceModel = (ModifiedTagInstanceModel) tagInstance;
      IConflictingValueSource source = modifiedContentTagInstanceModel.getSource();
      String contentId = "";
      
      
      if (source != null) {
        if (source.getType().equals(ContextConflictingValueSource.class.getName())) {
          IContextConflictingValueSource contextualSource = (IContextConflictingValueSource) source;
          contentId = contextualSource.getContentId();
        }
        else if (source.getType().equals(RelationshipConflictingValueSource.class.getName())) {
          IRelationshipConflictingValueSource relationshipSource = (IRelationshipConflictingValueSource) source;
          contentId = relationshipSource.getContentId();
        }
        else if (source.getType().equals(TaxonomyConflictingValueSource.class.getName())) {
          ITaxonomyConflictingValueSource relationshipSource = (ITaxonomyConflictingValueSource) source;
          contentId = String.valueOf(RDBMSUtils.getDefaultLocaleCatalogDAO().getEntityByID(source.getId()).getBaseEntityIID());
        }
        
        String couplingType = "";
        ICouplingDTO couplingDTO = localeCatalogDAO.newCouplingDTOBuilder().build();
        couplingType = getCouplingTypeForModifiedTag(modifiedContentTagInstanceModel, contentId, couplingType);
        prepareCouplingDTOToResolveConflict(tagsRecord, source, contentId, couplingType, couplingDTO, 0l);
        couplingDAO.resolvedConflicts(couplingDTO);
        
      }
      else if (!tagsRecord.isCoupled()) {
        
        couplingDAO.updateTargetConflictingValues(tagsRecord.getProperty().getPropertyIID(),
            entityDAO.getBaseEntityDTO().getBaseEntityIID(), 0L);
      }
      
    }
    return tagsRecord;
  }

  
  private IValueRecordDTO loadValueRecordByValueIID(long valueIID,
      IBaseEntityDAO baseEntityDAO, IAttribute referencedAttribute) throws Exception
  {
    IValueRecordDTO valueRecordDTO = null;
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords( RDBMSUtils.newPropertyDTO(referencedAttribute));
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
    if (propertyRecords != null) {
      for (IPropertyRecordDTO propertyRecord : propertyRecords) {
        if (propertyRecord instanceof IValueRecordDTO) {
          valueRecordDTO = (IValueRecordDTO) propertyRecord;
          if (valueRecordDTO.getValueIID() == valueIID) {
            return valueRecordDTO;
          }
        }
      }
    }
    return valueRecordDTO;
  }
  
  public void validateLifeStatusTag(IModifiedContentTagInstanceModel modifiedTag) throws Exception
  {
    PropertyType propertyType = PropertyType.TAG;
    String tagId = modifiedTag.getTagId();
    ITag tagConfig = this.tags.get(tagId);
    ITagsRecordDTO tagsRecordDTO = this.loadTagsRecord(tagConfig, propertyType);
    
    Set<ITagDTO> tagsDTO = tagsRecordDTO.getTags();
    for (ITagDTO tagDTO : tagsDTO) {
      String tagValueCode = tagDTO.getTagValueCode();
      ITag tagValueChild = (ITag) tagConfig.getChildren().stream()
          .filter(tagChildern -> tagChildern.getId().equals(tagValueCode))
          .findFirst().get();
      if (tagValueChild == null)
        continue;
      
      List<String> allowedTags = tagValueChild.getAllowedTags();
      
      for (ITagInstanceValue addedTagValue : modifiedTag.getAddedTagValues()) {
        if (!allowedTags.contains(addedTagValue.getTagId())) {
          throw new InvalidLifeCycleStateAddedException();
        }
      }
    }
  }
  
}
