package com.cs.config;

import com.cs.config.dto.*;
import com.cs.config.idto.*;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IStandardConfig;
import com.cs.config.standard.IStandardConfig.*;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.json.JSONContent;
import com.cs.core.printer.QuickPrinter;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author vallee
 */
public class ConfigDTOTests extends QuickPrinter {
 
  @Test
  public void propertyDTOTests() throws CSFormatException
  {
    printTestTitle("propertyDTOTests");
    ConfigAttributeDTO attr1 = new ConfigAttributeDTO();
    attr1.fromPXON(
        "{\n" + "    \"csid\": \"[dtpminimumslotwidthattribute $iid=214 $type=MEASUREMENT]\",\n"
            + "    \"valueashtml\": \"\",\n" + "    \"defaultunit\": \"mm\",\n"
            + "    \"isfilterable\": false,\n" + "    \"isversionable\": true,\n"
            + "    \"$istranslatable\": false,\n" + "    \"defaultvalue\": \"10\",\n"
            + "    \"$isstandard\": true,\n" + "    \"precision\": 2,\n"
            + "    \"issortable\": false,\n" + "    \"label\": \"Minimum Slot Width\",\n"
            + "    \"_subtype\": \"LENGTH_ATTRIBUTE_TYPE\",\n" + "    \"isgrideditable\": false\n"
            + "  }");
    printJSON(attr1);
    printf("PXON DTO: %s\n", attr1.toPXONBuffer());
    printf("Config DTO: %s\n", attr1.toJSON());

    attr1.fromJSON(
        "{\"propertyiid\":123,\"code\":\"Code1\", \"label\":\"test\", \"type\": \"com.cs.core.config.interactor.entity.attribute.NumberAttribute\", \"isdisabled\": false}");
    printf("PXON DTO: %s\n", attr1.toPXONBuffer());
    printf("Config DTO: %s\n", attr1.toJSON());
  }

  @Test
  public void tagValueDTOTests() throws CSFormatException
  {
    printTestTitle("tagValueDTOTests");
    ConfigTagValueDTO tagValue = new ConfigTagValueDTO();
    tagValue.fromPXON(
        "{\"csid\":\"[T>enrichmenttag]\",\"color\":null,\"label\":\"Enrichment__de_DE\"}");
    printJSON(tagValue);
    printf("PXON DTO: %s\n", tagValue.toPXONBuffer());
    printf("Config DTO: %s\n", tagValue.toJSON());

    IConfigTagValueDTO tagValueDTO = new ConfigTagValueDTO();
    tagValueDTO.setTagValueDTO("enrichmenttag");
    tagValueDTO.setLabel("Enrichment__de_DE");
    tagValueDTO.setColor("RED");
    tagValueDTO.setLinkedMasterTag("Master");
    printf("PXON DTO: %s\n", tagValueDTO.toPXON());
  }

  @Test
  public void tagDTOTests() throws CSFormatException
  {
    printTestTitle("tagDTOTests");

    IConfigTagDTO configTagDTO = new ConfigTagDTO();
    configTagDTO.setIsFilterable(false);
    configTagDTO.setColor("RED");
    configTagDTO.setIsGridEditable(false);
    configTagDTO.setIsStandard(true);
    configTagDTO.setDefaultValue("defaultTag");
    configTagDTO.setDescription("add description");
    configTagDTO.setIsVersionable(false);
    configTagDTO.setLabel("Status");

    IConfigTagValueDTO configTagValueDTO = new ConfigTagValueDTO();
    configTagValueDTO.setColor("GREEN");
    configTagValueDTO.setLabel("Approval");
    configTagValueDTO.setTagValueDTO(StandardTagValue_statustag.approvaltag.name());
    configTagDTO.getChildren().add(configTagValueDTO);

    IConfigTagValueDTO configTagValueDTO1 = new ConfigTagValueDTO();
    configTagValueDTO1.setColor("GREEN");
    configTagValueDTO1.setLabel("Enrichment");
    configTagValueDTO1.setTagValueDTO(StandardTagValue_statustag.enrichmenttag.name());
    configTagDTO.getChildren().add(configTagValueDTO1);

    configTagDTO.setPropertyDTO(StandardProperty.statustag.name(), PropertyType.TAG);
    configTagDTO.setTagType("com.cs.core.config.interactor.entity.tag.Tag");
    printf("PXON DTO: %s\n", configTagDTO.toPXON());
  }

  @Test
  public void propertyCollectionDTOTests() throws CSFormatException
  {
    printTestTitle("propertyCollectionDTOTests");

    ConfigPropertyCollectionDTO configPCDTO = new ConfigPropertyCollectionDTO();
    configPCDTO.setIsDefaultForXRay(false);
    String code = "articlegeneralInformationPropertyCollection";
    configPCDTO.setCode(code);
    configPCDTO.setIsDefaultForXRay(false);
    configPCDTO.setIsForXRay(true);
    configPCDTO.setIsStandard(true);
    configPCDTO.setLabel(code);
    configPCDTO.setTab(CommonConstants.PROPERTY_COLLECTION);

    Collection<IJSONContent> jsonElements = new ArrayList<>();
    IJSONContent json = new JSONContent();
    json.setField(ConfigTag.code.toString(), IStandardConfig.StandardProperty.pid_attribute.toString());
    json.setField(ConfigTag.index.toString(), 1);
    json.setField(ConfigTag.type.toString(), CommonConstants.ATTRIBUTE);
    json.setField(ConfigTag.id.toString(), IStandardConfig.StandardProperty.pid_attribute.toString());
    
    IJSONContent json1 = new JSONContent();
    json1.setField(ConfigTag.code.toString(), IStandardConfig.StandardProperty.sellingpriceattribute.toString());
    json1.setField(ConfigTag.index.toString(), 1);
    json1.setField(ConfigTag.type.toString(), CommonConstants.ATTRIBUTE);
    json1.setField(ConfigTag.id.toString(), IStandardConfig.StandardProperty.sellingpriceattribute.toString());
    jsonElements.add(json);
    jsonElements.add(json1);
    
    configPCDTO.setElements(jsonElements);
    String pxon = configPCDTO.toPXON();
    println("PXON : " + pxon);
    println("JSON : " + configPCDTO.toJSON());
    configPCDTO = new ConfigPropertyCollectionDTO();
    configPCDTO.fromPXON(pxon);
    println("JSON : " + configPCDTO.toJSON());
    println("PXON : " + configPCDTO.toPXON());
  }
  
  @Test
  public void contextDTOTests() throws CSFormatException
  {
    printTestTitle("contextDTOTests");
    IConfigContextDTO configContextDTO = new ConfigContextDTO();
    configContextDTO.setIsAutoCreate(false);
    configContextDTO.setIsCurrentTime(true);
    configContextDTO.setIsLimitedObject(false);
    configContextDTO.setTab("ContextTab");
    configContextDTO.setIsDuplicateVariantAllowed(true);
    configContextDTO.setDefaultStartTime(158243764568l);
    configContextDTO.setDefaultEndTime(75364533438l);
    configContextDTO.setIsLimitedObject(true);
    configContextDTO.setContextDTO("Context123", ContextType.ATTRIBUTE_CONTEXT);

    IJSONContent jsonContent = new JSONContent();
    jsonContent.setStringArrayField(StandardProperty.imageextensiontag.name(), Arrays.asList(StandardTagValue_imageextensiontag.image_extension_format_jpg.name(),
        StandardTagValue_imageextensiontag.image_extension_format_original.name()));
    jsonContent.setStringArrayField(StandardProperty.isbaseunittag.name(), Arrays.asList(StandardTagValue_isordertag.isbaseunittagvalue.name()));
    configContextDTO.setTagCodes(jsonContent);
    String pxon = configContextDTO.toPXON();
    printf("PXON DTO: %s\n", pxon);
    
    configContextDTO = new ConfigContextDTO();
    configContextDTO.fromPXON(pxon);
    printf("Config DTO: %s\n", configContextDTO.toJSON());

  }

  @Test
  public void configUserDTOTests() throws CSFormatException {
    printTestTitle("configUserDTOTests");

    IConfigUserDTO configUserDTO = new ConfigUserDTO();
    configUserDTO.setUserName("xxxLutz123.surname");
    configUserDTO.setFirstName("xxxLutz");
    configUserDTO.setCode("xxxLutz123");
    configUserDTO.setIsBackgroundUser(false);
    configUserDTO.setEmail("xxxLutz123surname@gmail.com");
    configUserDTO.setLastName("surname");
    configUserDTO.setGender("male");
    configUserDTO.setContact("35453445487");
    configUserDTO.setPassword("hsdhsg");
    configUserDTO.setIcon("icon");
    String pxon = configUserDTO.toPXON();
    printf("PXON DTO: %s\n", pxon);
    printf("Config DTO: %s\n", configUserDTO.toJSON());
    
    IConfigUserDTO configUserDTO1 = new ConfigUserDTO();
    configUserDTO1.fromPXON(pxon);
    printf("Config DTO 1: %s\n", configUserDTO1.toJSON());
  }



  @Test
  public void configClassifierDTOTests() throws CSFormatException
  {
    printTestTitle("configClassifierDTOTests");

    IConfigClassifierDTO configClassifierDTO = new ConfigClassifierDTO();
    // Taxonomy DTO
    Collection<String> events = new ArrayList<>();
    events.add("R_Events");
    events.add("RR_events");
    configClassifierDTO.setEvents(events);

    Collection<String> tasks = new ArrayList<>();
    tasks.add("R_Task");
    tasks.add("RR_tasks");
    configClassifierDTO.setTasks(tasks);

    configClassifierDTO.setClassifierDTO("R_Taxonomy", ClassifierType.TAXONOMY);
    configClassifierDTO.setSubType(IConfigClass.ClassifierClass.TAXONOMY_KLASS_TYPE.name());

    configClassifierDTO.setLabel("R_Taxonomy");
    Collection<IConfigSectionElementDTO> sections = new ArrayList<>();
    IConfigSectionElementDTO sectionElementDTO = new ConfigSectionElementDTO();
    sectionElementDTO.setIsMandatory(false);
    sectionElementDTO.setCouplingType(CommonConstants.LOOSELY_COUPLED);
    sectionElementDTO.setIsDisabled(false);
    sectionElementDTO.setCode(StandardProperty.longdescriptionattribute.toString());
    sectionElementDTO.setIsShould(false);
    sectionElementDTO.setIsInherited(false);
    sectionElementDTO.setType(CommonConstants.ATTRIBUTE);
    sections.add(sectionElementDTO);
    configClassifierDTO.getSections().put("articlegeneralInformationPropertyCollection", sections);

    Collection<String> statusTags = new ArrayList<>();
    statusTags.add(StandardProperty.listingstatustag.name());
    statusTags.add(StandardProperty.statustag.name());
    configClassifierDTO.setStatusTag(statusTags);

    IConfigEmbeddedRelationDTO embeddedDTO = new ConfigEmbeddedRelationDTO();
    embeddedDTO.setLabel("R_EMB");
    embeddedDTO.setType("Embedded");
    embeddedDTO.setCode("R_EMB");
    embeddedDTO.setCouplings(new ArrayList<IJSONContent>());
    configClassifierDTO.getEmbeddedClasses().put("R_EMB", embeddedDTO);
    configClassifierDTO.getLevelCodes().add("level1");
    configClassifierDTO.setMasterParentTag("parent1");
    configClassifierDTO.setParentCode("parentCode");
    configClassifierDTO.setContextCode("contextCode");

    IConfigRelationshipDTO relationshipDTO = new ConfigRelationshipDTO();
    relationshipDTO.setPropertyDTO("relatioship", PropertyType.RELATIONSHIP);
    relationshipDTO.setIsNature(true);
    relationshipDTO.setIsStandard(false);
    relationshipDTO.setLabel("relationship");
    relationshipDTO.setRelationshipType("relationshipType");
    relationshipDTO.setTab("RelationshipTab");

    IConfigSideRelationshipDTO side1 = new ConfigSideRelationshipDTO();
    side1.setCardinality(CommonConstants.CARDINALITY_ONE);
    side1.setClassCode(CommonConstants.ARTICLE);
    side1.setContextCode("contextCode1");
    side1.setIsVisible(true);
    side1.setLabel("ArticleLabel");
    relationshipDTO.getSide1().fromPXON(side1.toPXON());

    IConfigSideRelationshipDTO side2 = new ConfigSideRelationshipDTO();
    side2.setCardinality(CommonConstants.CARDINALITY_MANY);
    side2.setClassCode(CommonConstants.ASSET);
    side2.setContextCode("contextCode2");
    side2.setIsVisible(true);
    side2.setLabel("AssetLabel");
    relationshipDTO.getSide2().fromPXON(side2.toPXON());
    configClassifierDTO.setConfigTaxonomyType(CommonConstants.MAJOR_TAXONOMY);
    
    configClassifierDTO.setProductVariantContextCode(Arrays.asList("PVC1", "PVC2"));
    configClassifierDTO.setPromotionalVersionContextCode(Arrays.asList("PROVC1", "PROVC2"));
    configClassifierDTO.setLevelIndex(2);
    configClassifierDTO.setIsNewlyCreated(Arrays.asList(false, true));

    configClassifierDTO.getRelationships().add(relationshipDTO);
    String pxon = configClassifierDTO.toPXON();
    printf("PXON DTO: %s\n", pxon);
    configClassifierDTO = new ConfigClassifierDTO();
    configClassifierDTO.fromPXON(pxon);
    printf("JSON DTO: %s\n", configClassifierDTO.toJSON());
  }

  @Test
  public void configAttributDTOTest() throws CSFormatException {
    printTestTitle("configAttributDTOTest");

    IConfigAttributeDTO attributeDTO = new ConfigAttributeDTO();

    Collection<String> allowedStyles = new ArrayList<>();
    allowedStyles.add("Italic");
    attributeDTO.setAllowedStyles(allowedStyles);

    Collection<String> availability = new ArrayList<>();
    availability.add(CommonConstants.ARTICLE);
    attributeDTO.setAvailability(availability);

    attributeDTO.setDefaultUnit("unit");
    attributeDTO.setDefaultValue("value");
    attributeDTO.setDefaultValueAsHTML("html");
    attributeDTO.setDescription("description");
    attributeDTO.setIsDisabled(false);
    attributeDTO.setIsFilterable(true);
    attributeDTO.setIsGridEditable(false);
    attributeDTO.setIsMandatory(false);
    attributeDTO.setIsSearchable(false);
    attributeDTO.setIsSortable(true);
    attributeDTO.setIsStandard(false);
    attributeDTO.setIsTranslatable(true);
    attributeDTO.SetIsVersionable(true);
    attributeDTO.setLabel("LongDescription");
    attributeDTO.setPlaceHolder("add description");
    attributeDTO.setPrecision(0);
    attributeDTO.setToolTip("toolTip");
    attributeDTO.setPropertyDTO(StandardProperty.longdescriptionattribute.toString(), PropertyType.NUMBER);
    attributeDTO.setSubType(IPropertyDTO.SuperType.ATTRIBUTE.name());
    
    List<IJSONContent> attributeConcatenatedList = prepareConcatinatedAttributeList();
    attributeDTO.setAttributeConcatenatedList(attributeConcatenatedList);
    
    List<IJSONContent> attributeCalculatedList = prepareCalculatedAttributeList();
    attributeDTO.setAttributeOperatorList(attributeCalculatedList);
    
    attributeDTO.setHideSeparator(false);
    
    printf("PXON DTO: %s\n", attributeDTO.toPXON());
    printf("JSON DTO: %s\n", attributeDTO.toJSON());
  }

  private List<IJSONContent> prepareCalculatedAttributeList()
  {
    IJSONContent calculatedAttribute = new JSONContent();
    calculatedAttribute.setField(ConfigTag.attributeId.toString(), StandardProperty.gtin_attribute.toString());
    calculatedAttribute.setField(ConfigTag.order.toString(), 0);
    calculatedAttribute.setField(ConfigTag.type.toString(), CommonConstants.ATTRIBUTE);
    
    IJSONContent operater = new JSONContent();
    operater.setField(ConfigTag.order.toString(), 1);
    operater.setField(ConfigTag.type.toString(), "MULTIPLY");
    operater.setField(ConfigTag.operator.toString(), "MULTIPLY");
    
    IJSONContent calculatedAttribute2 = new JSONContent();
    calculatedAttribute2.setField(ConfigTag.order.toString(), 2);
    calculatedAttribute2.setField(ConfigTag.type.toString(), CommonConstants.TAG);
    calculatedAttribute2.setField(ConfigTag.attributeId.toString(), StandardProperty.gtin_attribute.toString());
    List<IJSONContent> attributeCalculatedList = Arrays.asList(calculatedAttribute, operater, calculatedAttribute2);
    return attributeCalculatedList;
  }

  private List<IJSONContent> prepareConcatinatedAttributeList()
  {
    IJSONContent concatinatedAttribute = new JSONContent();
    concatinatedAttribute.setField(ConfigTag.attributeId.toString(), StandardProperty.addressattribute.toString());
    concatinatedAttribute.setField(ConfigTag.order.toString(), 0);
    concatinatedAttribute.setField(ConfigTag.type.toString(), CommonConstants.ATTRIBUTE);
    
    IJSONContent userDefineValue = new JSONContent();
    userDefineValue.setField(ConfigTag.order.toString(), 1);
    userDefineValue.setField(ConfigTag.type.toString(), IPropertyDTO.PropertyType.HTML.toString());
    userDefineValue.setField(ConfigTag.value.toString(), "userdefineValue");
    
    IJSONContent concatinatedTag = new JSONContent();
    concatinatedTag.setField(ConfigTag.order.toString(), 2);
    concatinatedTag.setField(ConfigTag.type.toString(), CommonConstants.TAG);
    concatinatedTag.setField(ConfigTag.tagId.toString(), StandardProperty.statustag.toString());
    List<IJSONContent> attributeConcatenatedList = Arrays.asList(concatinatedAttribute, userDefineValue, concatinatedTag);
    return attributeConcatenatedList;
  }

  @Test
  public void configTaskDTOTest() throws CSFormatException {
    printTestTitle("configTaskDTOTest");
    IConfigTaskDTO taskDTO = new ConfigTaskDTO();
    taskDTO.setColor("#A07400");
    taskDTO.setIcon("");
    taskDTO.setLabel("R_TaskLabel");
    taskDTO.setStatusTag(StandardProperty.taskstatustag.name());
    taskDTO.setPriorityTag(StandardProperty.availabilitytag.name());
    taskDTO.setTaskDTO("R_Task", TaskType.SHARED);
    printf("PXON DTO: %s\n", taskDTO.toPXON());
    printf("JSON DTO: %s\n", taskDTO.toJSON());
  }
  
  @Test
  public void configDataRuleDTOTest() throws CSFormatException {
    printTestTitle("configDataRuleDTOTest");
    IConfigDataRuleDTO dataRule = new ConfigDataRuleDTO();
    dataRule.setCode("R_Rules");
    dataRule.setType(Constants.STANDARD_AND_NORMALIZATION);
    dataRule.setIsLanguageDependent(true);
    dataRule.setIsStandard(false);
    dataRule.setLabel("R_Rules_Label");
    dataRule.setLanguages(Arrays.asList(Constants.ENGLISH_US, Constants.FRENCH));
    dataRule.setOrganizations(Arrays.asList(IStandardConfig.STANDARD_ORGANIZATION_CODE));
    dataRule.setPhysicalCatalogIds(Arrays.asList(Constants.PIM, Constants.DATA_INTEGRATION_CATALOG_IDS));
    dataRule.setTaxonomyCodes(Arrays.asList("R_Taxonomy1","R_taxonomy2"));
    dataRule.setTypes(Arrays.asList("R_non_nature"));
    
    IConfigDataRuleIntermediateEntitysDTO attribute1 = new ConfigDataRuleIntermediateEntitysDTO();
    attribute1.setEntityId(IStandardConfig.StandardProperty.descriptionattribute.name());
    IConfigRuleEntityDTO attributeEntityDTO1 = new ConfigRuleEntityDTO();
    attributeEntityDTO1.setType(CommonConstants.VALUE_PROPERTY);
    attributeEntityDTO1.setShouldCompareWithSystemDate(false);
    attributeEntityDTO1.setValues(Arrays.asList("abcd","pqr"));
    attribute1.getRules().add(attributeEntityDTO1);
    
    IConfigDataRuleIntermediateEntitysDTO attribute2 = new ConfigDataRuleIntermediateEntitysDTO();
    attribute2.setEntityId(IStandardConfig.StandardProperty.shortdescriptionattribute.name());
    IConfigRuleEntityDTO attributeEntityDTO2 = new ConfigRuleEntityDTO();
    attributeEntityDTO2.setType(CommonConstants.VALUE_PROPERTY);
    attributeEntityDTO2.setShouldCompareWithSystemDate(false);
    attributeEntityDTO2.setValues(Arrays.asList("abcd","pqr"));
    attribute2.getRules().add(attributeEntityDTO2);
    
    dataRule.getAttributes().addAll(Arrays.asList(attribute1,attribute2));
    
    IConfigDataRuleTagsDTO tagDTO1 = new ConfigDataRuleTagsDTO();
    tagDTO1.setEntityId(IStandardConfig.StandardProperty.availabilitytag.name());
    IConfigDataRuleTagRuleDTO tagRuleDTO = new ConfigDataRuleTagRuleDTO();
    tagRuleDTO.setType(CommonConstants.EXACT);
    JSONContent json1 = new JSONContent();
    json1.setField(ConfigTag.to.toString(), 100);
    json1.setField(ConfigTag.from.toString(), 100);
    json1.setField("id", StandardTagValue_availabilitytag.regularlyavailable.name());
    JSONContent json2 = new JSONContent();
    json2.setField(ConfigTag.to.toString(), 0);
    json2.setField(ConfigTag.from.toString(), 0);
    json2.setField("id", StandardTagValue_availabilitytag.onstock.name());
    tagRuleDTO.setTagValues(Arrays.asList(json1, json2));
    tagDTO1.getRules().add(tagRuleDTO);
    dataRule.getTags().add(tagDTO1);
    
    dataRule.getNormalizations();
    
    IConfigNormalizationDTO normalizaton = new ConfigNormalizationDTO();
    normalizaton.setEntityId(IStandardConfig.StandardProperty.shortdescriptionattribute.name());
    normalizaton.setType(CommonConstants.ATTRIBUTE);
    normalizaton.setTransformationType("value");
    normalizaton.setValueAsHTML("<p> abcd</p>");
    normalizaton.setValues(Arrays.asList("xxxLutz","jpg"));
    normalizaton.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
    
    IConfigNormalizationDTO normalizaton1 = new ConfigNormalizationDTO();
    normalizaton1.setType(CommonConstants.TYPE_PROPERTY);
    normalizaton1.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
    normalizaton1.setValues(Arrays.asList("R_non_nature"));
    
    dataRule.getNormalizations().addAll(Arrays.asList(normalizaton, normalizaton1));
    String pxon = dataRule.toPXON();
    printf("PXON DTO: %s\n", pxon);
    printf("JSON DTO: %s\n", dataRule.toJSON());
    
    IConfigDataRuleDTO configdataRule = new ConfigDataRuleDTO();
    configdataRule.fromPXON(pxon);
    printf("PXON DTO: %s\n", configdataRule.toJSON());
  }

  @Test
  public void configTabTest() throws CSFormatException {
    printTestTitle("configTabTest");
    IConfigTabDTO tab = new ConfigTabDTO();
    tab.setCode("xxxLutz");
    tab.setIcon("/123wasdqw/chair.jpg");
    tab.setIsStandard(false);
    tab.setLabel("Lutz Furniture Tab");
    tab.setSequence(13);
    tab.setPropertySequenceList(List.of("cxt_Color", "cxt_Size", "dimensions", "marketing_info", "furniture-image-relationship", "furniture-market-relationship"));

    String pxon = tab.toPXON();
    printf("PXON DTO: %s\n", pxon);
    printf("JSON DTO: %s\n", tab.toJSON());

    tab = new ConfigTabDTO();
    tab.fromPXON(pxon);
    printf("PXON DTO: %s\n", tab.toJSON());
  }
  
  @Test
  public void configGoldenRecordRule() throws CSFormatException {
    printTestTitle("configGoldenRecordRule");
    IConfigGoldenRecordRuleDTO goldenRecordDTO = new ConfigGoldenRecordRuleDTO();
    goldenRecordDTO.setCode("GOLDEN_RECORD");
    goldenRecordDTO.setLabel("GOLDEN_RECORD");
    goldenRecordDTO.setTags(Arrays.asList(StandardProperty.availabilitytag.name(), StandardProperty.imageextensiontag.name()));
    goldenRecordDTO.setAttributes(Arrays.asList(StandardProperty.addressattribute.name(), StandardProperty.createdonattribute.name()
    , StandardProperty.descriptionattribute.name()));
    goldenRecordDTO.setKlassIds(Arrays.asList(StandardClassifier.single_article.name(), StandardClassifier.attribute_classes.name()));
    goldenRecordDTO.setTaxonomyIds(Arrays.asList("taxonomy_1", "taxonomy_2"));
    goldenRecordDTO.setIsAutoCreate(true);
    goldenRecordDTO.setOrganizations(Arrays.asList(IStandardConfig.STANDARD_ORGANIZATION_CODE));
    goldenRecordDTO.setPhysicalCatalogIds(Arrays.asList(Constants.PIM));
    IConfigMergeEffectDTO configMergeEffectDTO = new ConfigMergeEffectDTO();
    IConfigMergeEffectTypeDTO attributeEffectTypeDTO = new ConfigMergeEffectTypeDTO();
    attributeEffectTypeDTO.setType(CommonConstants.SUPPLIER_PRIORITY);
    attributeEffectTypeDTO.setEntityType(CommonConstants.ATTRIBUTE);
    attributeEffectTypeDTO.setEntityId( StandardProperty.createdonattribute.name());
    attributeEffectTypeDTO.setSupplierIds(Arrays.asList(IStandardConfig.STANDARD_ORGANIZATION_CODE));
    List<IConfigMergeEffectTypeDTO> attributes = configMergeEffectDTO.getAttributes();
    attributes.add(attributeEffectTypeDTO);
    
    IConfigMergeEffectTypeDTO tagEffectTypeDTO = new ConfigMergeEffectTypeDTO();
    tagEffectTypeDTO.setType(CommonConstants.SUPPLIER_PRIORITY);
    tagEffectTypeDTO.setEntityType(CommonConstants.TAGS);
    tagEffectTypeDTO.setEntityId(StandardProperty.availabilitytag.name());
    tagEffectTypeDTO.setSupplierIds(Arrays.asList(IStandardConfig.STANDARD_ORGANIZATION_CODE));
    List<IConfigMergeEffectTypeDTO> tags = configMergeEffectDTO.getTags();
    tags.add(tagEffectTypeDTO);
    
    IConfigMergeEffectTypeDTO relatinshipEffectTypeDTO = new ConfigMergeEffectTypeDTO();
    relatinshipEffectTypeDTO.setType(CommonConstants.SUPPLIER_PRIORITY);
    relatinshipEffectTypeDTO.setEntityType(CommonConstants.RELATIONSHIP);
    relatinshipEffectTypeDTO.setEntityId(StandardProperty.standardArticleAssetRelationship.name());
    relatinshipEffectTypeDTO.setSupplierIds(Arrays.asList(IStandardConfig.STANDARD_ORGANIZATION_CODE));
    List<IConfigMergeEffectTypeDTO> relationships = configMergeEffectDTO.getRelationships();
    relationships.add(relatinshipEffectTypeDTO);
    
    IConfigMergeEffectTypeDTO naturerRlatinshipEffectTypeDTO = new ConfigMergeEffectTypeDTO();
    naturerRlatinshipEffectTypeDTO.setType(CommonConstants.SUPPLIER_PRIORITY);
    naturerRlatinshipEffectTypeDTO.setEntityType(CommonConstants.NATURE_RELATIONSHIPS);
    naturerRlatinshipEffectTypeDTO.setEntityId(CommonConstants.FIXED_BUNDLE);
    naturerRlatinshipEffectTypeDTO.setSupplierIds(Arrays.asList(IStandardConfig.STANDARD_ORGANIZATION_CODE));
    List<IConfigMergeEffectTypeDTO> natureRelationships = configMergeEffectDTO.getNatureRelationships();
    natureRelationships.add(naturerRlatinshipEffectTypeDTO);
    goldenRecordDTO.setMergeEffect(configMergeEffectDTO);
    
    String pxon = goldenRecordDTO.toPXON();
    printf("PXON DTO: %s\n", pxon);
    printf("JSON DTO: %s\n", goldenRecordDTO.toJSON());
    
    IConfigGoldenRecordRuleDTO configGoldenRecordDTO = new ConfigGoldenRecordRuleDTO();
    configGoldenRecordDTO.fromPXON(pxon);
    printf("JSON DTO: %s\n", configGoldenRecordDTO);
    
  }
  
  /*  @Test
  public void configReferenceDTO() throws CSFormatException {
    printTestTitle("configReferenceDTO");
    IConfigReferenceDTO referanceDTO = new ConfigReferenceDTO();
    referanceDTO.setIsNature(true);
    referanceDTO.setPropertyDTO("xxLutz_referance", PropertyType.REFERENCE);
    referanceDTO.setIsStandard(true);
    referanceDTO.setTab("relationship_tab");
    referanceDTO.setLabel("xxLutz_referance");
    IConfigReferenceSideDTO side1 = referanceDTO.getSide1();
    side1.setCardinality(CommonConstants.CARDINALITY_MANY);
    side1.setLabel("R_side1");
    side1.setClassCode(StandardClassifier.single_article.name());
    side1.setType(IConfigClass.ClassifierClass.PROJECT_KLASS_TYPE.name());
    
    IConfigReferenceSideDTO side2 = referanceDTO.getSide2();
    side2.setCardinality(CommonConstants.CARDINALITY_ONE);
    side2.setLabel("R_side2");
    side2.setClassCode(StandardClassifier.image_asset.name());
    side2.setType(IConfigClass.ClassifierClass.ASSET_KLASS_TYPE.name());
    
    referanceDTO.setIcon("");
    
    String pxon = referanceDTO.toPXON();
    printf("PXON DTO: %s\n", pxon);
    
    IConfigReferenceDTO configreferanceDTODTO = new ConfigReferenceDTO();
    configreferanceDTODTO.fromPXON(pxon);
    printf("JSON DTO: %s\n", configreferanceDTODTO);
  }*/

  @Test
  public void configOrganizationDTO() throws CSFormatException {
    printTestTitle("configOrganizationDTO");
    ConfigOrganizationDTO organization = new ConfigOrganizationDTO();
    organization.setCode("Xcode");
    organization.setIcon("icon");
    organization.setLabel("organization");
    organization.setType("internal");
    organization.setEndpointIds(List.of("E1","E2"));
    organization.setKlassIds(List.of("K1","K2"));
    organization.setTaxonomyIds(List.of("TAX1","TAX2"));
    organization.setPhysicalCatalogs(List.of(Constants.PIM, Constants.ONBOARDING_CATALOG_IDS));
    organization.setPortals(List.of());
    organization.setSystems(List.of("Sys1"));
    ConfigRoleDTO role = new ConfigRoleDTO();

    organization.getRoles().add(role);
    role.setCode("xsxs");
    role.setIsDashboardEnable(true);
    role.setLabel("Role 1");
    role.setType("com.cs.core.config.interactor.entity.standard.role.ApproverRole");
    role.getKpis().addAll(List.of("KP1","KP2"));
    role.getUsers().addAll(List.of("User2","User1"));
    role.getTargetKlasses().addAll(List.of("K1"));
    role.getTargetTaxonomies().addAll(List.of("TAX2"));
    role.getPhysicalCatalogs().add(Constants.PIM);
    role.getSystems().add("Sys1");
    role.getEndpoints().add("E1");
    role.setRoleType("admin");
    role.getEntities().add("Entity1");

    String pxon = organization.toPXON();
    System.out.println("PXON : " + pxon);
    System.out.println("JSON : " + organization.toJSON());
    organization = new ConfigOrganizationDTO();
    organization.fromPXON(pxon);
    System.out.println("JSON : " + organization.toJSON());
    System.out.println("PXON : " + organization.toPXON());
  }
  
  @Test
  public void configTranslationDTO() throws CSFormatException {
    printTestTitle("configTranslationDTO");
    ConfigTranslationDTO translationDTO = new ConfigTranslationDTO();
    translationDTO.setCode(StandardProperty.createdbyattribute.name());
    translationDTO.setType(EntityType.ATTRIBUTE);
    
    IJSONContent translations = new JSONContent();
    getAttributeTranskation(translations, Constants.ENGLISH_US);
    getAttributeTranskation(translations, Constants.SPANISH);
    translationDTO.setTranslations(translations);
    String pxon = translationDTO.toPXON();
    System.out.println("PXON : " + pxon);
    System.out.println("JSON : " + translationDTO.toJSON());
    translationDTO = new ConfigTranslationDTO();
    translationDTO.fromPXON(pxon);
    IJSONContent language = translationDTO.getTranslations();
    System.out.println("Language Translation : "+ language);
    System.out.println("JSON : " + translationDTO.toJSON());
    System.out.println("PXON : " + translationDTO.toPXON());
    
  }

  private void getAttributeTranskation(IJSONContent translations, String language)
  {
    IJSONContent attribute = new JSONContent();
    attribute.setField(ConfigTag.tooltip.name(), "tool tip in"+ language);
    attribute.setField(ConfigTag.description.name(), "description in" + language);
    attribute.setField(ConfigTag.placeholder.name(), "placeholder in" + language);
    attribute.setField(ConfigTag.label.name(), "label in" + language);
    
    translations.setField(language, attribute);
  }
  
}
