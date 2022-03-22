package com.cs.core.asset.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTOBuilder;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@SuppressWarnings("unchecked")
public class PropertyRecordBuilder {

  public static final String ATTRIBUTE_ID_SEPARATOR = "-";

  public enum PropertyRecordType
  {

    DEFAULT, CREATE, UPDATE;

    private static final PropertyRecordType[] values = values();

    public static PropertyRecordType valueOf(int ordinal)
    {
      return values[ordinal];
    }
  };

  private final IBaseEntityDAO      entityDAO;
  private final Map<String, Object> attributes;
  private final Map<String, Object> tags;
  private final PropertyRecordType  propertyRecordType;
  private final Map<String, Object> referencedSectionElements;
  private final Map<String, Object> embeddedVariantContexts;

  public PropertyRecordBuilder(IBaseEntityDAO entityDAO, Map<String, Object> configDetails,
      PropertyRecordType propertyRecordType)
  {
    this.entityDAO = entityDAO;
    this.attributes = (Map<String, Object>) configDetails.get("referencedAttributes");
    this.tags = (Map<String, Object>) configDetails.get("referencedTags");
    this.referencedSectionElements = (Map<String, Object>) configDetails.get("referencedElements");
    this.embeddedVariantContexts = (Map<String, Object>) ((Map<String, Object>) configDetails
        .get("referencedVariantContexts")).get("embeddedVariantContexts");
    this.propertyRecordType = propertyRecordType;
  }

  /**
   * ************************************************************ Attribute
   * Record **********************************************************
   */
  public IPropertyRecordDTO createValueRecord(Map<String, Object> attributeConfig) throws Exception
  {
    String attributeId = (String) attributeConfig.get("id");
    Map<String, Object> attributeElement = (Map<String, Object>) this.referencedSectionElements
        .get(attributeId);
    // If attribute contain attribute context then avoid to create value record
    // for default value

    // FOR MANDATORY ATTRIBUTES
    if (attributeElement == null) {
      return null;
    }

    // FOR ATTRIBUTE VARIANT
    String attributeVariantContext = (String) attributeElement.get("attributeVariantContext");
    if (!StringUtils.isBlank(attributeVariantContext)
        && this.propertyRecordType == PropertyRecordType.DEFAULT)
      return null;

    Map<String, Object> attributeContextConfig = (Map<String, Object>) embeddedVariantContexts
        .get(attributeVariantContext);
    return this.buildValueRecord(attributeElement, attributeConfig, attributeContextConfig, null);
  }

  public IPropertyRecordDTO updateValueRecord(Map<String, Object> attributeInstance)
      throws Exception
  {
    String attributeId = (String) attributeInstance.get("attributeId");
    Map<String, Object> attributeConfig = (Map<String, Object>) this.attributes.get(attributeId);
    Map<String, Object> attributeElement = (Map<String, Object>) this.referencedSectionElements
        .get(attributeId);
    Map<String, Object> attributeContextConfig = null;
    // In case of relationship extension object no embedded attribute support
    if (this.embeddedVariantContexts != null && attributeElement != null) {
      attributeContextConfig = (Map<String, Object>) this.embeddedVariantContexts
          .get(attributeElement.get("attributeVariantContext"));
    }
    return this.buildValueRecord(attributeElement, attributeConfig, attributeContextConfig,
        attributeInstance);
  }

  @SuppressWarnings("static-access")
  private IPropertyRecordDTO buildValueRecord(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance) throws Exception
  {
    PropertyType propertyType = null;
    IPropertyRecordDTO propertyRecordDTO = null;
    String type = (String) attributeConfig.get("type");
    switch (type) {
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
        propertyType = PropertyType.MEASUREMENT;
        propertyRecordDTO = createMeasurementAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;

      case Constants.ASSET_METADATA_ATTRIBUTE_TYPE:
        propertyRecordDTO = createAssetAttribute(attributeElement, attributeConfig,
            attributeContextConfig, attributeInstance, propertyType);
        break;

      default:
        propertyRecordDTO = null;
        break;
    }
    return propertyRecordDTO;
  }

  private void setCommonPropertyForDefaultRecord(IPropertyRecordDTO propertyRecordDTO,
      Map<String, Object> referencedSectionElement)
  {
    // Set common property for attribute and tag
  }

  private IPropertyDTO newAttributeProperty(Map<String, Object> attributeConfig,
      IPropertyDTO.PropertyType propertyType) throws Exception
  {
    IPropertyDTO propertyDTO = this.entityDAO.newPropertyDTO(
        (Long) attributeConfig.get("propertyIID"), (String) attributeConfig.get("code"),
        propertyType);
    return propertyDTO;
  }

  private IValueRecordDTO newValueRecordDTO(long propertyRecordIID, long valueIID, String value,
      String localeID, IContextDTO context, Map<String, Object> attributeConfig,
      PropertyType propertyType) throws Exception
  {
    IPropertyDTO propertyDTO = this.newAttributeProperty(attributeConfig, propertyType);
    
    IValueRecordDTOBuilder valueRecordDTOBuilder = entityDAO
        .newValueRecordDTOBuilder(propertyDTO, value)
        .valueIID(valueIID)
        .localeID(localeID);
    if (context != null)
      valueRecordDTOBuilder.contextDTO(context);
    return valueRecordDTOBuilder.build();
  }
  
  public IValueRecordDTO buildValueRecord(long propertyRecordIID, long valueRecordIID, String value,
      String localeID, IContextDTO contextDTO, Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.newValueRecordDTO(propertyRecordIID, valueRecordIID,
        value, localeID, contextDTO, attributeConfig, propertyType);
    setCommonPropertyForDefaultRecord(valueRecordDTO, attributeElement);
    return valueRecordDTO;
  }

  public static long[] getPropertyID(String prpertyRecordId) throws Exception
  {
    String[] prpertyRecordIdArr = prpertyRecordId.split(ATTRIBUTE_ID_SEPARATOR);
    long[] propRec = null;
    if (prpertyRecordIdArr.length == 2) {
      propRec = new long[2];
      propRec[0] = Long.parseLong(prpertyRecordIdArr[0]);
      propRec[1] = Long.parseLong(prpertyRecordIdArr[1]);
    }
    return propRec;
  }

  private IValueRecordDTO buildValueRecord(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = null;
    long propertyRecordIID = 0L;
    long valueRecordIID = 0L;
    String value = null;
    String localeID = null;
    if (this.propertyRecordType == PropertyRecordType.DEFAULT
        && (StringUtils.isNoneEmpty((String) attributeElement.get("defaultValue")))) {
      propertyRecordIID = 0L;
      valueRecordIID = 0L;
      value = (String) attributeElement.get("defaultValue");
      localeID = this.entityDAO.getBaseEntityDTO()
          .getBaseLocaleID();
      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, value, localeID,
          null, attributeElement, attributeConfig, propertyType);
    }
    else if (this.propertyRecordType == PropertyRecordType.UPDATE) {
      long[] propertyRecordIds = PropertyRecordBuilder
          .getPropertyID((String) attributeInstance.get("id"));
      propertyRecordIID = propertyRecordIds[0];
      valueRecordIID = propertyRecordIds[1];
      value = (String) attributeInstance.get("value");
      localeID = attributeInstance.get("language") == null ? this.entityDAO.getBaseEntityDTO()
          .getBaseLocaleID() : (String) attributeInstance.get("language");
      if (attributeInstance.get("modifiedContext") != null && attributeContextConfig != null) {
        try {
          valueRecordDTO = this.updateAttributeContextInstance(propertyRecordIID, valueRecordIID,
              value, localeID, attributeInstance, attributeElement, attributeConfig,
              attributeContextConfig, propertyType);
        }
        catch (CSFormatException e) {
          // TODO Auto-generated catch block
          throw new RDBMSException(0, "", "");
        }
      }
      else if (attributeElement != null && (!attributeElement.get("couplingType")
          .equals(CommonConstants.LOOSELY_COUPLED))) {
        valueRecordDTO = this.updateCoupledAttributeInstance(propertyRecordIID, valueRecordIID,
            value, localeID, attributeInstance, attributeElement, attributeConfig,
            attributeContextConfig, propertyType);
      }
      else {
        valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, value, localeID,
            null, attributeElement, attributeConfig, propertyType);
      }
    }
    else if (this.propertyRecordType == PropertyRecordType.CREATE) {
      propertyRecordIID = 0L;
      valueRecordIID = 0L;
      value = (String) attributeInstance.get("value");
      localeID = attributeInstance.get("language") == null ? this.entityDAO.getBaseEntityDTO()
          .getBaseLocaleID() : (String) attributeInstance.get("language");
      IContextDTO contextDTO = this.getContextDTO(attributeElement, attributeConfig,
          attributeContextConfig, attributeInstance, propertyType);
      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, value, localeID,
          contextDTO, attributeElement, attributeConfig, propertyType);
      this.createAttributeContextInstance(valueRecordDTO.getContextualObject(), attributeElement,
          attributeConfig, attributeContextConfig, attributeInstance, propertyType);
    }
    return valueRecordDTO;
  }

  private IValueRecordDTO updateCoupledAttributeInstance(long propertyRecordIID,
      long valueRecordIID, String value, String localeID, Map<String, Object> attributeInstance,
      Map<String, Object> attributeElement, Map<String, Object> attributeConfig,
      Map<String, Object> attributeContextConfig, PropertyType propertyType)
  {
    // TODO Auto-generated method stub
    return null;
  }

  private IContextDTO getContextDTO(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attrInstance, PropertyType propertyType) throws Exception
  {
    IContextDTO contextDTO = null;
    if (attrInstance.get("context") != null && attributeContextConfig != null) {
      contextDTO = getContextDTO(attributeContextConfig);
    }
    return contextDTO;
  }

  @SuppressWarnings("unchecked")
  private void createAttributeContextInstance(IContextualDataDTO contextualObject,
      Map<String, Object> attributeElement, Map<String, Object> attributeConfig,
      Map<String, Object> attributeContextConfig, Map<String, Object> attrInstance,
      PropertyType propertyType) throws Exception
  {
    Map<String, Object> context = (Map<String, Object>) attrInstance.get("context");
    if (context != null && attributeContextConfig != null && contextualObject != null) {
      this.createContextTags((List<Map<String, Object>>) attrInstance.get("tags"),
          contextualObject);
      this.setContextTimeRange((Map<String, Object>) context.get("timeRange"), contextualObject);
    }
  }

  public void createContextTags(List<Map<String, Object>> tagInstances,
      IContextualDataDTO contextualDataDTO) throws Exception
  {
    tagInstances.forEach(tagInstance -> {
      try {
        Map<String, Object> tagConfig = (Map<String, Object>) this.tags
            .get(tagInstance.get("tagId"));
        List<ITagDTO> TagRecords = this
            .getAddedTagValues((List<Map<String, Object>>) tagInstance.get("tagValues"), tagConfig);
        contextualDataDTO.getContextTagValues()
            .addAll(TagRecords);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  public boolean setContextTimeRange(Map<String, Object> timeRange,
      IContextualDataDTO contextualDataDTO) throws Exception
  {
    if (timeRange != null) {
      Long from = timeRange.get("from") == null ? 0 : (Long) timeRange.get("from");
      Long to = timeRange.get("to") == null ? 0 : (Long) timeRange.get("to");
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

  public IValueRecordDTO loadValueRecordByValueIID(long valueIID,
      IBaseEntityDAO baseEntityDAO, Map<String, Object> referencedAttribute)
      throws Exception, CSFormatException
  {
    IPropertyRecordDTO propertyRecord = loadSinglePropertyRecords(baseEntityDAO,
        newPropertyDTO(referencedAttribute));
    return (IValueRecordDTO) propertyRecord;
  }

  private IValueRecordDTO updateAttributeContextInstance(long propertyRecordIID,
      long valueRecordIID, String value, String localeID,
      Map<String, Object> modifiedAttributeInstanceModel, Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      PropertyType propertyType) throws Exception, CSFormatException
  {

    IValueRecordDTO valueRecordDTO = loadValueRecordByValueIID(valueRecordIID, this.entityDAO,
        attributeConfig);
    if (valueRecordDTO != null) {
      valueRecordDTO.setValue(value);
      this.updateAttributeContextTags(modifiedAttributeInstanceModel,
          valueRecordDTO.getContextualObject());
      Map<String, Object> modifiedAttributeInstance = (Map<String, Object>) modifiedAttributeInstanceModel
          .get("modifiedContext");
      this.setContextTimeRange((Map<String, Object>) modifiedAttributeInstance.get("timeRange"),
          valueRecordDTO.getContextualObject());
    }
    return valueRecordDTO;
  }

  public void updateAttributeContextTags(Map<String, Object> modifiedAttributeInstanceModel,
      IContextualDataDTO contextualDataDTO) throws Exception
  {
    // Handle modified tag
    this.updateModifiedContextTags(
        (List<Map<String, Object>>) modifiedAttributeInstanceModel.get("modifiedTags"),
        contextualDataDTO);

    // Handle added tag
    this.createContextTags(
        (List<Map<String, Object>>) modifiedAttributeInstanceModel.get("addedTags"),
        contextualDataDTO);
  }

  public void updateModifiedContextTags(List<Map<String, Object>> modifiedTags,
      IContextualDataDTO contextualDataDTO) throws Exception
  {
    Set<ITagDTO> contextTagValues = contextualDataDTO.getContextTagValues();
    modifiedTags.forEach(modifiedTag -> {
      try {

        Map<String, Object> tagConfig = (Map<String, Object>) this.tags
            .get(modifiedTag.get("tagId"));

        // Handle deleted tag values
        this.deleteTagValues((List<String>) modifiedTag.get("deletedTags"), contextTagValues);

        // Handle modified tag values
        List<ITagDTO> modifiedTagValues = this.getmodifiedTagValues(
            (List<Map<String, Object>>) modifiedTag.get("modifiedTagValues"), tagConfig);
        modifiedTagValues.forEach(modifiedTagValue -> {
          contextTagValues.remove(modifiedTagValue);
          contextTagValues.add(modifiedTagValue);
        });

        // Handle added tag values
        List<ITagDTO> addedTagValues = this.getAddedTagValues(
            (List<Map<String, Object>>) modifiedTag.get("modifiedTagValues"), tagConfig);
        contextTagValues.addAll(addedTagValues);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private IValueRecordDTO createTextAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
  {

    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    // do any Attribute specific settings
    if (valueRecordDTO != null) {
    }

    return valueRecordDTO;
  }

  private IValueRecordDTO createCalculatedAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
  {
    long propertyRecordIID = 0L;
    long valueRecordIID = 0L;
    String localeID = "";
    List<Map<String, Object>> attributeOperatorList = (List<Map<String, Object>>) attributeConfig
        .get("attributeOperatorList");

    StringBuilder mathExpression = new StringBuilder();
    if (!attributeOperatorList.isEmpty()) {
      mathExpression.append("= ");
    }

    for (Map<String, Object> attributeOperator : attributeOperatorList) {

      String attributeOperatorType = (String) attributeOperator.get("type");

      switch (attributeOperatorType) {
        case "ATTRIBUTE":
          String attributeId = (String) attributeOperator.get("attributeId");
          if (referencedSectionElements.containsKey(attributeId)) {
            Map<String, Object> iAttribute = (Map<String, Object>) attributes.get(attributeId);
            mathExpression.append("[" + ((String) iAttribute.get("code")) + "]");
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
          mathExpression.append(" " + attributeOperator.get("value"));
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
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    }
    return valueRecordDTO;
  }

  private IValueRecordDTO createConcatenatedAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = null;
    long propertyRecordIID = 0L;
    long valueRecordIID = 0L;
    String localeID = null;
    if (propertyRecordType == PropertyRecordType.DEFAULT) {

      localeID = this.entityDAO.getBaseEntityDTO()
          .getBaseLocaleID();

      List<Map<String, Object>> attributeConcatenatedList = (List<Map<String, Object>>) attributeConfig
          .get("attributeConcatenatedList");

      StringBuilder mathExpression = createExpression(attributeConcatenatedList);

      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, "", localeID, null,
          attributeElement, attributeConfig, propertyType);
      if (valueRecordDTO != null && mathExpression.length() != 0)
        try {
          valueRecordDTO.addCalculation(mathExpression.toString());
        }
        catch (CSFormatException e) {
          e.printStackTrace();
          RDBMSLogger.instance().exception(e);
        }
    }
    else if (propertyRecordType == PropertyRecordType.UPDATE) {

      long[] propertyRecordIds = PropertyRecordBuilder
          .getPropertyID((String) attributeInstance.get("id"));
      propertyRecordIID = propertyRecordIds[0];
      valueRecordIID = propertyRecordIds[1];
      localeID = attributeInstance.get("language") == null ? this.entityDAO.getBaseEntityDTO()
          .getBaseLocaleID() : (String) attributeInstance.get("language");

      List<Map<String, Object>> valueAsExpression = (List<Map<String, Object>>) attributeInstance
          .get("valueAsExpression");

      StringBuilder mathExpression = createExpression(valueAsExpression);

      valueRecordDTO = this.buildValueRecord(propertyRecordIID, valueRecordIID, "", localeID, null,
          attributeElement, attributeConfig, propertyType);

      try {
        valueRecordDTO.addCalculation(mathExpression.toString());
      }
      catch (CSFormatException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    }

    return valueRecordDTO;
  }

  public StringBuilder createExpression(List<Map<String, Object>> attributeConcatenatedList)
  {

    StringBuilder mathExpression = new StringBuilder();
    for (Map<String, Object> concatenatedAttributeOperator : attributeConcatenatedList) {

      if (mathExpression.length() == 0) {
        mathExpression.append("=");
      }
      else {
        mathExpression.append("||");
      }

      String type = (String) concatenatedAttributeOperator.get("type");
      switch (type) {
        case "attribute":
          Map<String, Object> attributeOperator = concatenatedAttributeOperator;
          if (referencedSectionElements.containsKey(attributeOperator.get("attributeId"))) {
            Map<String, Object> configSourceAttribute = (Map<String, Object>) attributes
                .get(attributeOperator.get("attributeId"));
            mathExpression.append("[" + configSourceAttribute.get("code") + "]");
          }
          break;

        case "html":
          mathExpression.append("'" + concatenatedAttributeOperator.get("value") + "'");
          break;

        case "tag":
          if (referencedSectionElements.containsKey(concatenatedAttributeOperator.get("tagId"))) {
            Map<String, Object> tag = (Map<String, Object>) tags
                .get(concatenatedAttributeOperator.get("tagId"));
            mathExpression.append("[" + tag.get("code") + "]");
          }
          break;
      }
    }
    return mathExpression;
  }

  private IValueRecordDTO createPriceAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    // do any Attribute specific settings
    if (valueRecordDTO != null) {
      String unitSymbol = attributeElement.get("defaultUnit") != null
          ? (String) attributeElement.get("defaultUnit")
          : (String) attributeConfig.get("defaultUnit");
      valueRecordDTO.setUnitSymbol(unitSymbol);
      if (!StringUtils.isEmpty(valueRecordDTO.getValue())) {
        valueRecordDTO.setAsNumber(Double.parseDouble(valueRecordDTO.getValue()));
      }
    }
    return valueRecordDTO;
  }

  private IValueRecordDTO createDateAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
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

  private IValueRecordDTO createMeasurementAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    if (valueRecordDTO != null) {
      // Do any Measurement Attribute specific settings
      String unitSymbol = attributeElement.get("defaultUnit") != null
          ? (String) attributeElement.get("defaultUnit")
          : (String) attributeConfig.get("defaultUnit");
      valueRecordDTO.setUnitSymbol(unitSymbol);
    }
    return valueRecordDTO;
  }

  private IValueRecordDTO createNumberAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
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

  private IValueRecordDTO createHTMLAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig,
        attributeContextConfig, attributeInstance, propertyType);
    if (valueRecordDTO != null) {
      if (this.propertyRecordType == PropertyRecordType.DEFAULT) {
        valueRecordDTO.setAsHTML((String) attributeElement.get("valueAsHtml"));
      }
      else if (this.propertyRecordType == PropertyRecordType.UPDATE
          || this.propertyRecordType == PropertyRecordType.CREATE) {
        valueRecordDTO.setAsHTML((String) attributeInstance.get("valueAsHtml"));
      }
    }
    return valueRecordDTO;
  }

  private IValueRecordDTO createNameAttribute(Map<String, Object> attributeElement, Map<String, Object> attributeConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = null;
    valueRecordDTO = this.buildValueRecord(attributeElement, attributeConfig, null, attributeInstance,
        propertyType);
    return valueRecordDTO;
  }

  private IValueRecordDTO createAssetAttribute(Map<String, Object> attributeElement,
      Map<String, Object> attributeConfig, Map<String, Object> attributeContextConfig,
      Map<String, Object> attributeInstance, PropertyType propertyType) throws Exception
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
    Map<String, Object> attributeConfig = (Map<String, Object>) this.attributes
        .get(CommonConstants.NAME_ATTRIBUTE);
    IValueRecordDTO valueRecordDTO = this.buildValueRecord(0l, 0L, name,
        this.entityDAO.getBaseEntityDTO()
            .getBaseLocaleID(),
        null, null, attributeConfig, PropertyType.TEXT);
    return valueRecordDTO;
  }

  /**
   * ************************************************************ Tag Record
   *
   * @throws CSFormatException
   *           **********************************************************
   */
  public IPropertyRecordDTO createTagsRecord(Map<String, Object> tagConfig)
      throws Exception, CSFormatException
  {
    String tagId = (String) tagConfig.get("id");
    ;
    Map<String, Object> tagElement = (Map<String, Object>) this.referencedSectionElements
        .get(tagId);
    return this.buildTagsRecord(tagElement, tagConfig, null);
  }

  public IPropertyRecordDTO updateTagsRecord(Map<String, Object> tagInstance)
      throws Exception, CSFormatException
  {
    String tagId = (String) tagInstance.get("tagId");
    Map<String, Object> tagConfig = (Map<String, Object>) this.tags.get(tagId);
    Map<String, Object> tagElement = (Map<String, Object>) this.referencedSectionElements
        .get(tagId);
    return this.buildTagsRecord(tagElement, tagConfig, tagInstance);
  }

  private IPropertyRecordDTO buildTagsRecord(Map<String, Object> tagElement,
      Map<String, Object> tagConfig, Map<String, Object> tagInstance)
      throws Exception, CSFormatException
  {
    IPropertyRecordDTO recordDTO = null;
    PropertyType propertyType = PropertyType.TAG;
    String tagType = (String) tagConfig.get("tagType");
    switch (tagType) {
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
        propertyType = PropertyType.BOOLEAN;
        recordDTO = createBooleanAttribute(tagElement, tagConfig, tagInstance, propertyType);
        break;
      default:
        recordDTO = buildTagsRecordDTO(tagElement, tagConfig, tagInstance, propertyType);
        break;
    }
    return recordDTO;
  }

  private IPropertyDTO newTagProperty(Map<String, Object> tagConfig,
      IPropertyDTO.PropertyType propertyType) throws Exception
  {
    IPropertyDTO propertyDTO = this.entityDAO.newPropertyDTO((Long) tagConfig.get("propertyIID"),
        (String) tagConfig.get("code"), propertyType);
    return propertyDTO;
  }

  private ITagsRecordDTO newTagsRecordDTO(long propertyRecordIID, Map<String, Object> tagConfig,
      PropertyType propertyType) throws Exception
  {
    IPropertyDTO newPropertyDTO = this.newTagProperty(tagConfig, propertyType);
    return this.entityDAO.newTagsRecordDTOBuilder(newPropertyDTO).build();
  }

  private ITagDTO newTagDTO(String tagValueCode, int relevance) throws Exception
  {
    ITagDTO tagRecordDTO = this.entityDAO.newTagDTO(relevance, tagValueCode);
    return tagRecordDTO;
  }

  private ITagsRecordDTO buildTagsRecord(long propertyRecordIID, Map<String, Object> tagElement,
      Map<String, Object> tagConfig, PropertyType propertyType) throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = this.newTagsRecordDTO(propertyRecordIID, tagConfig,
        propertyType);
    setCommonPropertyForDefaultRecord(tagsRecordDTO, tagElement);
    return tagsRecordDTO;
  }

  private IValueRecordDTO newBooleanValueRecordDTO(long propertyRecordIID, long valueIID,
      String value, String localeID, IContextDTO context, Map<String, Object> tagConfig,
      PropertyType propertyType) throws Exception
  {
    IPropertyDTO propertyDTO = this.newTagProperty(tagConfig, propertyType);
    return entityDAO.newValueRecordDTOBuilder(propertyDTO, value)
        .valueIID(valueIID)
        .contextDTO(context)
        .localeID(localeID)
        .build();
  }

  private IValueRecordDTO buildBooleanRecord(long propertyRecordIID, long valueRecordIID,
      String value, String localeID, Map<String, Object> tagElement, Map<String, Object> tagConfig,
      PropertyType propertyType) throws Exception
  {
    IValueRecordDTO valueRecordDTO = this.newBooleanValueRecordDTO(propertyRecordIID,
        valueRecordIID, value, localeID, null, tagConfig, propertyType);
    setCommonPropertyForDefaultRecord(valueRecordDTO, tagElement);
    return valueRecordDTO;
  }

  private ITagsRecordDTO buildStandardTagsRecordWithDefaultChild(long propertyRecordIID,
      Map<String, Object> tagElement, Map<String, Object> tagConfig, PropertyType propertyType,
      String childId) throws Exception
  {
    ITagsRecordDTO tagsRecordDTO = this.newTagsRecordDTO(propertyRecordIID, tagConfig,
        propertyType);
    setCommonPropertyForDefaultRecord(tagsRecordDTO, tagElement);
    List<Map<String, Object>> children = (List<Map<String, Object>>) tagConfig.get("children");
    Optional<Map<String, Object>> findFirst = children.stream()
        .filter(tagChildern -> tagChildern.get("id")
            .equals(childId))
        .findFirst();
    Map<String, Object> defaultTagValue = (Map<String, Object>) findFirst.get();
    tagsRecordDTO.getTags()
        .add(this.newTagDTO((String) defaultTagValue.get("code"), 100));
    return tagsRecordDTO;
  }

  private ITagsRecordDTO buildTagsRecordDTO(Map<String, Object> tagElement,
      Map<String, Object> tagConfig, Map<String, Object> tagInstance, PropertyType propertyType)
      throws Exception, CSFormatException
  {
    ITagsRecordDTO tagsRecordDTO = null;
    long propertyRecordIID = 0L;
    if (this.propertyRecordType == PropertyRecordType.UPDATE) {
      tagsRecordDTO = this.loadTagsRecord(tagConfig, propertyType);
      // manage deleted tag values
      this.deleteTagValues((List<String>) tagInstance.get("deletedTagValues"),
          tagsRecordDTO.getTags());
      // manage modified tag values
      List<ITagDTO> modifiedTagValues = this.getmodifiedTagValues(
          (List<Map<String, Object>>) tagInstance.get("modifiedTagValues"), tagConfig);
      // manage added tag values
      modifiedTagValues.addAll(this.getAddedTagValues(
          (List<Map<String, Object>>) tagInstance.get("addedTagValues"), tagConfig));
      tagsRecordDTO.mergeTags((modifiedTagValues.toArray(new ITagDTO[0])));
    }
    else if (this.propertyRecordType == PropertyRecordType.CREATE) {
      Map<String, Object> tagInst = (Map<String, Object>) tagInstance;
      propertyRecordIID = 0L;
      tagsRecordDTO = this.buildTagsRecord(propertyRecordIID, tagElement, tagConfig, propertyType);
      List<ITagDTO> addedTagValues = this
          .getAddedTagValues((List<Map<String, Object>>) tagInst.get("tagValues"), tagConfig);
      tagsRecordDTO.setTags((addedTagValues.toArray(new ITagDTO[0])));
    }
    return tagsRecordDTO;
  }

  private ITagsRecordDTO loadTagsRecord(Map<String, Object> referencedTag,
      PropertyType propertyType) throws Exception, CSFormatException
  {
    IPropertyDTO propertyDTO = this.newTagProperty(referencedTag, propertyType);
    IBaseEntityDTO baseEntityDTO = this.entityDAO.loadPropertyRecords(propertyDTO);
    Set<IPropertyRecordDTO> loadPropertyRecords = baseEntityDTO.getPropertyRecords();
    ITagsRecordDTO tagsRecordDTO = null;
    if (loadPropertyRecords != null && loadPropertyRecords.size() != 1) {
      throw new Exception("Tag record not present.");
    }
    else {
      tagsRecordDTO = (ITagsRecordDTO) loadPropertyRecords.iterator()
          .next();
    }
    return tagsRecordDTO;
  }

  private void deleteTagValues(List<String> deletedTagValueIds, Set<ITagDTO> tagRecords)
      throws Exception
  {
    tagRecords.removeIf(
        tagRecord -> deletedTagValueIds.contains(String.valueOf(tagRecord.getTagValueCode())));
  }

  private List<ITagDTO> getmodifiedTagValues(List<Map<String, Object>> modifiedTagValuesList,
      Map<String, Object> referencedTag) throws Exception
  {
    List<ITagDTO> modifiedTagValues = new ArrayList<>();

    if (modifiedTagValuesList == null || modifiedTagValuesList.isEmpty())
      return modifiedTagValues;

    for (Map<String, Object> tagInstanceValue : modifiedTagValuesList) {
      modifiedTagValues.add(this.newTagDTO((String) tagInstanceValue.get("code"),
          (Integer) tagInstanceValue.get("relevance")));
    }
    return modifiedTagValues;
  }

  private List<ITagDTO> getAddedTagValues(List<Map<String, Object>> addedTagValues,
      Map<String, Object> tagConfig) throws Exception
  {
    if (addedTagValues == null || addedTagValues.isEmpty())
      return new ArrayList<ITagDTO>();

    // FIXME Temp fix for getting tagValueIID. Ideally it come from UI
    Map<String, Map<String, Object>> tagIdTagMap = this
        .getTagIdTagMap((List<Map<String, Object>>) tagConfig.get("children"));

    List<ITagDTO> addedTagRecords = new ArrayList<>();
    for (Map<String, Object> addedTagValue : addedTagValues) {
      Map<String, Object> tagValueReference = tagIdTagMap.get(addedTagValue.get("tagId"));
      addedTagRecords.add(this.newTagDTO((String) tagValueReference.get("code"),
          (Integer) addedTagValue.get("relevance")));
    }
    return addedTagRecords;
  }

  private Map<String, Map<String, Object>> getTagIdTagMap(List<Map<String, Object>> childrenTags)
  {
    return childrenTags.stream()
        .collect(Collectors.toMap(tag -> (String) tag.get("id"), tag -> (Map<String, Object>) tag));
  }

  private ITagsRecordDTO createYesNeutralTag(Map<String, Object> tagElement,
      Map<String, Object> tagConfig, Map<String, Object> tagInstance, PropertyType propertyType)
      throws Exception, CSFormatException
  {
    ITagsRecordDTO tagsRecordDTO = null;
    long propertyRecordIID = 0L;
    if (this.propertyRecordType == PropertyRecordType.DEFAULT) {
      propertyRecordIID = 0L;
      List<Map<String, Object>> tagDefaultvalues = (List<Map<String, Object>>) tagElement
          .get("defaultValue");
      if (tagDefaultvalues != null && !tagDefaultvalues.isEmpty()) {
        tagsRecordDTO = buildTagsRecord(propertyRecordIID, tagElement, tagConfig, propertyType);
        Set<ITagDTO> tagRecords = tagsRecordDTO.getTags();
        for (Map<String, Object> tagDefaultvalue : tagDefaultvalues) {
          ITagDTO newTagRecordDTO = this.newTagDTO((String) tagDefaultvalue.get("code"),
              (Integer) tagDefaultvalue.get("relevance"));
          tagRecords.add(newTagRecordDTO);
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

  private ITagsRecordDTO createLifeStatusTag(Map<String, Object> tagElement,
      Map<String, Object> tagConfig, Map<String, Object> tagInstance, PropertyType propertyType)
      throws Exception, CSFormatException
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

  private ITagsRecordDTO createListingStatusTag(Map<String, Object> tagElement,
      Map<String, Object> tagConfig, Map<String, Object> tagInstance, PropertyType propertyType)
      throws Exception, CSFormatException
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

  private IValueRecordDTO createBooleanAttribute(Map<String, Object> tagElement,
      Map<String, Object> tagConfig, Map<String, Object> tagInstance, PropertyType propertyType)
      throws Exception
  {
    IValueRecordDTO valueRecordDTO = null;
    long propertyRecordIID = 0L;
    long valueRecordIID = 0L;
    String value = IStandardConfig.FALSE;
    int valeAsNumber = 0;
    if (this.propertyRecordType == PropertyRecordType.UPDATE) {
      List<Map<String, Object>> modifiedTagvalues = (List<Map<String, Object>>) tagInstance
          .get("modifiedTagValues");
      if (modifiedTagvalues != null && modifiedTagvalues.size() == 1) {
        Map<String, Object> tagInstanceValue = modifiedTagvalues.get(0);
        propertyRecordIID = Long.parseLong((String) tagInstance.get("id"));
        valueRecordIID = Long.parseLong((String) tagInstanceValue.get("id"));
        Integer relevance = (Integer) tagInstanceValue.get("relevance");
        if (relevance == 100) {
          value = IStandardConfig.TRUE;
          valeAsNumber = 1;
        }
      }
      valueRecordDTO = this.buildBooleanRecord(propertyRecordIID, valueRecordIID, value, "",
          tagElement, tagConfig, propertyType);
    }
    else if (this.propertyRecordType == PropertyRecordType.CREATE) {
      propertyRecordIID = 0L;
      valueRecordIID = 0L;
      value = IStandardConfig.FALSE;
      valeAsNumber = 0;
      List<Map<String, Object>> tagValues = (List<Map<String, Object>>) tagInstance
          .get("tagValues");
      if (tagValues != null && tagValues.size() == 1) {
        Map<String, Object> tagInstanceValue = tagValues.get(0);
        Integer relevance = (Integer) tagInstanceValue.get("relevance");
        if (relevance == 100) {
          value = IStandardConfig.TRUE;
          valeAsNumber = 1;
        }
      }
      valueRecordDTO = this.buildBooleanRecord(propertyRecordIID, valueRecordIID, value, "",
          tagElement, tagConfig, propertyType);
    }
    if (valueRecordDTO != null) {
      valueRecordDTO.setAsNumber(valeAsNumber);
    }
    return valueRecordDTO;
  }

  public IPropertyDTO newPropertyDTO(Map<String, Object> referencedAttribute)
      throws Exception
  {
    long propertyIID = (long) referencedAttribute.get("propertyIID");
    String propertyID = (String) referencedAttribute.get("id");
    String propertyCode = (String) referencedAttribute.get("code");
    String type = (String) referencedAttribute.get("type");
    IPropertyDTO propertyDTO = newPropertyDTO(propertyIID, propertyID, propertyCode, type);
    return propertyDTO;
  }

  public IPropertyDTO newPropertyDTO(long propertyIID, String propertyID,
      String propertyCode, String type) throws Exception
  {
    PropertyType propertyType = getPropertyType(type);
    return entityDAO.newPropertyDTO(propertyIID, propertyCode, propertyType);
  }

  public static PropertyType getPropertyType(String type) throws Exception
  {
    PropertyType propertyType = null;

    switch (type) {
      case CommonConstants.TAG_TYPE:
      case SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID:
      case SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID:
      case SystemLevelIds.RULER_TAG_TYPE_ID:
      case SystemLevelIds.RANGE_TAG_TYPE_ID:
      case SystemLevelIds.CUSTOM_TAG_TYPE_ID:
      case SystemLevelIds.LIFECYCLE_STATUS_TAG_TYPE_ID:
      case SystemLevelIds.LISTING_STATUS_TAG_TYPE_ID:
      case SystemLevelIds.STATUS_TAG_TYPE_ID:
      case SystemLevelIds.MASTER_TAG_TYPE_ID:
      case SystemLevelIds.LANGUAGE_TAG_TYPE_ID:
        propertyType = PropertyType.TAG;
        break;

      case CommonConstants.BOOLEAN_TAG_TYPE_ID:
        propertyType = PropertyType.BOOLEAN;
        break;

      case CommonConstants.CALCULATED_ATTRIBUTE_TYPE:
        propertyType = PropertyType.CALCULATED;
        break;

      case CommonConstants.CONCATENATED_ATTRIBUTE_TYPE:
        propertyType = PropertyType.CONCATENATED;
        break;

      case CommonConstants.DATE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.DATE;
        break;

      case CommonConstants.HTML_TYPE_ATTRIBUTE:
        propertyType = PropertyType.HTML;
        break;

      case CommonConstants.LENGTH_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.AREA_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.AREA_PER_VOLUME_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.CAPACITANCE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.ACCELERATION_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.CHARGE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.CONDUCTANCE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.CURRENCY_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.CUSTOM_UNIT_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.DIGITAL_STORAGE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.ENERGY_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.FREQUENCY_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.HEATING_RATE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.ILLUMINANCE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.INDUCTANCE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.FORCE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.LUMINOSITY_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.MAGNETISM_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.MASS_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.POTENTIAL_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.POWER_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.PROPORTION_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.RADIATION_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.ROTATION_FREQUENCY_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.SPEED_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.SUBSTANCE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.TEMPERATURE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.THERMAL_INSULATION_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.VISCOCITY_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.VOLUME_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.WEIGHT_PER_TIME_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.WEIGHT_PER_AREA_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.RESISTANCE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.PRESSURE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.PLANE_ANGLE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;
      case CommonConstants.DENSITY_ATTRIBUTE_TYPE:
        propertyType = PropertyType.MEASUREMENT;
        break;

      case CommonConstants.NUMBER_ATTRIBUTE_TYPE:
        propertyType = PropertyType.NUMBER;
        break;

      case CommonConstants.PRICE_ATTRIBUTE_TYPE:
        propertyType = PropertyType.PRICE;
        break;

      case CommonConstants.TEXT_ATTRIBUTE_TYPE:
        propertyType = PropertyType.TEXT;
        break;

      case Constants.NAME_ATTRIBUTE_TYPE:
        propertyType = PropertyType.TEXT;
        break;

      case Constants.ASSET_METADATA_ATTRIBUTE_TYPE:
        propertyType = propertyType.ASSET_ATTRIBUTE;
        break;

      case Constants.IMAGE_COVER_FLOW_ATTRIBUTE_BASE_TYPE:
        propertyType = propertyType.ASSET_ATTRIBUTE;
        break;
      default:
        // throw new Exception("RDBMS property type not avialble for " + type);

    }
    return propertyType;
  }

  public IPropertyRecordDTO loadSinglePropertyRecords(IBaseEntityDAO baseEntityDAO,
      IPropertyDTO propertyDTO) throws Exception, CSFormatException
  {
    IPropertyRecordDTO propertyRecordDTO = null;
    IBaseEntityDTO loadPropertyRecords = baseEntityDAO.loadPropertyRecords(propertyDTO);
    Set<IPropertyRecordDTO> propertyRecords = loadPropertyRecords.getPropertyRecords();
    if (propertyRecords != null && propertyRecords.size() == 1) {
      propertyRecordDTO = propertyRecords.iterator()
          .next();
    }
    return propertyRecordDTO;
  }

  public  IContextDTO getContextDTO(Map<String, Object> variantContext) throws Exception
  {
    ContextType contextType = getContextTypeByType((String) variantContext.get("type"));
    IContextDTO contextDTO = entityDAO.newContextDTO((String) variantContext.get("code"), contextType);
    return contextDTO;
  }

  public static ContextType getContextTypeByType(String type)
  {
    ContextType contextType = ContextType.UNDEFINED;

    switch (type) {
      case CommonConstants.ATTRIBUTE_VARIANT_CONTEXT:
        contextType = ContextType.ATTRIBUTE_CONTEXT;
        break;

      case CommonConstants.PRODUCT_VARIANT:
        contextType = ContextType.LINKED_VARIANT;
        break;

      case CommonConstants.RELATIONSHIP_VARIANT:
        contextType = ContextType.RELATIONSHIP_VARIANT;
        break;

      case CommonConstants.CONTEXTUAL_VARIANT:
        contextType = ContextType.EMBEDDED_VARIANT;
        break;

      case CommonConstants.GTIN_VARIANT:
        contextType = ContextType.GTIN_VARIANT;
        break;

      default:
        contextType = ContextType.UNDEFINED;
    }
    return contextType;
  }
}
