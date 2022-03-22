package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigAttributeDTO;
import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.dto.ConfigEmbeddedRelationDTO;
import com.cs.config.idto.IConfigEmbeddedRelationDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.idto.IConfigNatureRelationshipDTO;
import com.cs.config.idto.IConfigSectionElementDTO;
import com.cs.config.standard.IConfigClass;
import com.cs.constants.CommonConstants;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.workflow.constants.CalculatedAttributeTypes;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.model.WorkflowTaskModel;

@SuppressWarnings("unchecked")
public abstract class AbstractConfigFromPXONTask extends AbstractTransformationTask {
  
  public static final String FAILED_FILES               = "FAILED_FILES";
  public static final String TRANSFORMED_DATA           = "TRANSFORMED_DATA";
  public static final String PXON_FILE_PATH             = "PXON_FILE_PATH";
  public static final String EXCEL                      = "EXCEL";
  public static final String HEADERS                    = "headers";
  public static final String DATA                       = "data";
  public static final String XLSX_FILE_EXTENSION        = ".xlsx";
  public static final String EXPORT_FILE_PREFIX         = "OutboundPXON_";
  
  public static final String PC_SHEET_PREFIX            = "PC_";
  public static final String VARIANTS_SHEET_PREFIX      = "Variants_";
  public static final String RELATIONSHIPS_SHEET_PREFIX = "Relationships_";
  public static final String PXON_SCOPE                 = "PXON SCOPE";
  private String             SEPARATOR                  = ";";
  protected int              propCollectinCount         = 1;
  protected int              variantCount               = 1;
  protected int              relationshipCount          = 1;
  protected String           parentCode                 = "-1";
  protected String           EMPTY_STRING               = "";
  protected String           ATTRIBUTE                  = "ATTRIBUTE";
  protected String           TAG_ID                     = "tagId";
  private String             VALUE                      = "VALUE";
  
  protected abstract void generateFromPXON(WorkflowTaskModel model);
  protected abstract void fillLevelInfo(Map<String, Object> transformedToxnomyMap,
      ConfigClassifierDTO configClassifierDTO);
  protected abstract Object getSepratedValues(Collection<String> collection);
  protected abstract Object getDefaultValue(IConfigSectionElementDTO obj);

  
  @Override
  public void transform(WorkflowTaskModel model)
  {
    generateFromPXON(model);
  }
  
  protected void prepareArticleKlassSheet(Map<String, Object> classifierExcelMap)
  {
    LinkedHashSet<String> articleKlassHeader = new LinkedHashSet<>();
    articleKlassHeader.add(CODE_COLUMN_CONFIG);
    articleKlassHeader.add(LABEL_COLUMN_CONFIG);
    articleKlassHeader.add(PARENT_CODE_COLUMN);
    articleKlassHeader.add(IS_NATURE);
    articleKlassHeader.add(NATURE_TYPE);
    articleKlassHeader.add(MAX_VERSION);
    articleKlassHeader.add(LIFE_CYCLE_STATUS_TAG);
    articleKlassHeader.add(IS_ABSTRACT);
    articleKlassHeader.add(IS_DEFAULT);
    articleKlassHeader.add(ICON_COLUMN);
    articleKlassHeader.add(PREVIEW_IMAGE);
  //  articleKlassHeader.add(EVENT_CODES);
    articleKlassHeader.add(TASK_CODES);
    articleKlassHeader.add(SECTION_SHEET_NAME);
    articleKlassHeader.add(VARIANT_SHEET_NAME_FOR_KLASS);
    articleKlassHeader.add(RELATIONSHIP_SHEET_NAME_FOR_KLASS);
    articleKlassHeader.add(RELATIONSHIP_FILTER);
    articleKlassHeader.add(ATTRIBUTE_FILTER);
    articleKlassHeader.add(TAG_FILTER); 
    
    classifierExcelMap.put(KLASS_SHEET_NAME, new HashMap<>());
    Map<String, Object> articleklassMap = (Map<String, Object>) classifierExcelMap.get(KLASS_SHEET_NAME);//KLASS Sheet
    articleklassMap.put(HEADERS, articleKlassHeader);
    articleklassMap.put(DATA, new ArrayList<>());
  }
   
  protected void prepareAssetKlassSheet(Map<String, Object> classifierExcelMap)
  {
    LinkedHashSet<String> assetKlassHeader = new LinkedHashSet<>();
    assetKlassHeader.add(CODE_COLUMN_CONFIG);
    assetKlassHeader.add(LABEL_COLUMN_CONFIG);
    assetKlassHeader.add(PARENT_CODE_COLUMN);
    assetKlassHeader.add(IS_NATURE);
    assetKlassHeader.add(NATURE_TYPE);
    assetKlassHeader.add(MAX_VERSION);
    assetKlassHeader.add(LIFE_CYCLE_STATUS_TAG);
    assetKlassHeader.add(IS_ABSTRACT);
    assetKlassHeader.add(IS_DEFAULT);
    assetKlassHeader.add(ICON_COLUMN);
    assetKlassHeader.add(PREVIEW_IMAGE);
    //assetKlassHeader.add(EVENT_CODES);
    assetKlassHeader.add(TASK_CODES);
    assetKlassHeader.add(TECHNICAL_IMAGE_CLASSES);
    assetKlassHeader.add(DETECT_DUPLICATE);
    assetKlassHeader.add(EXTRACT_ZIP);
    assetKlassHeader.add(ITransformationTask.TRACKDOWNLOAD);
    assetKlassHeader.add(SECTION_SHEET_NAME);
    assetKlassHeader.add(VARIANT_SHEET_NAME_FOR_KLASS);
    assetKlassHeader.add(RELATIONSHIP_SHEET_NAME_FOR_KLASS);
    assetKlassHeader.add(EXTENSION);
    assetKlassHeader.add(EXTRACT_METADATA);
    assetKlassHeader.add(EXTRACT_RENDITIONS);
    assetKlassHeader.add(RELATIONSHIP_FILTER);
    assetKlassHeader.add(ATTRIBUTE_FILTER);
    assetKlassHeader.add(TAG_FILTER);
    
    classifierExcelMap.put(KLASS_SHEET_NAME, new HashMap<>());
    Map<String, Object> assetKlassMap = (Map<String, Object>) classifierExcelMap.get(KLASS_SHEET_NAME);//KLASS Sheet
    assetKlassMap.put(HEADERS, assetKlassHeader);
    assetKlassMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareSupplierKlassSheet(Map<String, Object> classifierExcelMap)
  {
    LinkedHashSet<String> supplierKlassHeader = new LinkedHashSet<>();
    supplierKlassHeader.add(CODE_COLUMN_CONFIG);
    supplierKlassHeader.add(LABEL_COLUMN_CONFIG);
    supplierKlassHeader.add(PARENT_CODE_COLUMN);
    supplierKlassHeader.add(MAX_VERSION);
    supplierKlassHeader.add(LIFE_CYCLE_STATUS_TAG);
    supplierKlassHeader.add(ICON_COLUMN);
    supplierKlassHeader.add(PREVIEW_IMAGE);
    //supplierKlassHeader.add(EVENT_CODES);
    supplierKlassHeader.add(TASK_CODES);
    supplierKlassHeader.add(SECTION_SHEET_NAME);
    supplierKlassHeader.add(VARIANT_SHEET_NAME_FOR_KLASS);
    supplierKlassHeader.add(RELATIONSHIP_SHEET_NAME_FOR_KLASS);
    
    classifierExcelMap.put(KLASS_SHEET_NAME, new HashMap<>());
    Map<String, Object> assetKlassMap = (Map<String, Object>) classifierExcelMap.get(KLASS_SHEET_NAME);//KLASS Sheet
    assetKlassMap.put(HEADERS, supplierKlassHeader);
    assetKlassMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareTextAssetKlassSheet(Map<String, Object> classifierExcelMap)
  {
    LinkedHashSet<String> textAssetKlassHeader = new LinkedHashSet<>();
    textAssetKlassHeader.add(CODE_COLUMN_CONFIG);
    textAssetKlassHeader.add(LABEL_COLUMN_CONFIG);
    textAssetKlassHeader.add(PARENT_CODE_COLUMN);
    textAssetKlassHeader.add(IS_NATURE);
    textAssetKlassHeader.add(NATURE_TYPE);
    textAssetKlassHeader.add(MAX_VERSION);
    textAssetKlassHeader.add(LIFE_CYCLE_STATUS_TAG);
    textAssetKlassHeader.add(IS_ABSTRACT);
    textAssetKlassHeader.add(IS_DEFAULT);
    textAssetKlassHeader.add(ICON_COLUMN);
    textAssetKlassHeader.add(PREVIEW_IMAGE);
   // textAssetKlassHeader.add(EVENT_CODES);
    textAssetKlassHeader.add(TASK_CODES);
    textAssetKlassHeader.add(SECTION_SHEET_NAME);
    textAssetKlassHeader.add(VARIANT_SHEET_NAME_FOR_KLASS);
    textAssetKlassHeader.add(RELATIONSHIP_SHEET_NAME_FOR_KLASS);
    
    classifierExcelMap.put(KLASS_SHEET_NAME, new HashMap<>());
    Map<String, Object> textAssetklassMap = (Map<String, Object>) classifierExcelMap.get(KLASS_SHEET_NAME);//KLASS Sheet
    textAssetklassMap.put(HEADERS, textAssetKlassHeader);
    textAssetklassMap.put(DATA, new ArrayList<>());
  }

  protected void prepareVirtualCatlogKlassSheet(Map<String, Object> classifierExcelMap)
  {
    LinkedHashSet<String> virCatlogKlassHeader = new LinkedHashSet<>();
    virCatlogKlassHeader.add(CODE_COLUMN_CONFIG);
    virCatlogKlassHeader.add(LABEL_COLUMN_CONFIG);
    virCatlogKlassHeader.add(PARENT_CODE_COLUMN);
    virCatlogKlassHeader.add(NATURE_TYPE);
    virCatlogKlassHeader.add(MAX_VERSION);
    virCatlogKlassHeader.add(ICON_COLUMN);
    virCatlogKlassHeader.add(PREVIEW_IMAGE);
    virCatlogKlassHeader.add(SECTION_SHEET_NAME);
    virCatlogKlassHeader.add(VARIANT_SHEET_NAME_FOR_KLASS);
    virCatlogKlassHeader.add(RELATIONSHIP_SHEET_NAME_FOR_KLASS);
    virCatlogKlassHeader.add(IS_DEFAULT);
    virCatlogKlassHeader.add(IS_ABSTRACT);
    classifierExcelMap.put(KLASS_SHEET_NAME, new HashMap<>());
    Map<String, Object> virCatlogklassMap = (Map<String, Object>) classifierExcelMap.get(KLASS_SHEET_NAME);//KLASS Sheet
    virCatlogklassMap.put(HEADERS, virCatlogKlassHeader);
    virCatlogklassMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareKlassPropertyCollectionSheet(Map<String, Object> classifierExcelMap,String pcSheetName)
  {
    LinkedHashSet<String> propCollHeader = new LinkedHashSet<>();
    propCollHeader.add(PROPERTY_COLLECTION_CODE);
    propCollHeader.add(PROPERTY_CODE);
    propCollHeader.add(PROPERTY_TYPE);
    propCollHeader.add(ALLOWED_VALUES_COLUMN);
    propCollHeader.add(IS_INHERTIED);
    propCollHeader.add(PRECISION_COLUMN);
    propCollHeader.add(DEFAULT_VALUE);
    propCollHeader.add(COUPLING_COLUMN);
    propCollHeader.add(MANDATORY_COLUMN);
    propCollHeader.add(SKIPPED_COLUMN);
    propCollHeader.add(TYPE_COLUMN);
    propCollHeader.add(MULTISELECT_COLUMN);
    propCollHeader.add(ATTRIBUTE_CONTEXT_COLUMN);
    propCollHeader.add(PREFIX_COLUMN);
    propCollHeader.add(SUFFIX_COLUMN);
    propCollHeader.add(PRODUCT_IDENTIFIER_COLUMN);
    propCollHeader.add(LANGUAGE_DEPENDENT_COLUMN);
    propCollHeader.add(REVISIONABLE_COLUMN);
    String sheetName = pcSheetName!=null?pcSheetName:PC_SHEET_PREFIX;
    classifierExcelMap.put(sheetName, new HashMap<>());
    Map<String, Object> propCollMap = (Map<String, Object>) classifierExcelMap.get(sheetName);//PC Sheet
    propCollMap.put(HEADERS, propCollHeader);
    propCollMap.put(DATA, new ArrayList<>());
  }
    
  protected void prepareVarientSheet(Map<String, Object> classifierExcelMap, String variantsSheetName)
  {
    LinkedHashSet<String> variantHeaders = new LinkedHashSet<>();
    variantHeaders.add(CLASS_TYPE);
    variantHeaders.add(CLASS_CODE);
    variantHeaders.add(PROPERTY_TYPE);
    variantHeaders.add(PROPERTY_CODE);
    variantHeaders.add(COUPLING_COLUMN);
    classifierExcelMap.put(variantsSheetName, new HashMap<>());
    Map<String, Object> variantsMap = (Map<String, Object>) classifierExcelMap.get(variantsSheetName);//Variant Sheet
    variantsMap.put(HEADERS, variantHeaders);
    variantsMap.put(DATA, new ArrayList<>());
  }
    
  protected void prepareKlassRelationshipSheet(Map<String, Object> classifierExcelMap, String sheetName)
  {
    LinkedHashSet<String> relationshipHeaders = new LinkedHashSet<>();
    relationshipHeaders.add(RELATIONSHIP_TYPE);
    relationshipHeaders.add(SIDE2_CLASS_CODE);
    relationshipHeaders.add(TAB_CODE);
    relationshipHeaders.add(CONTEXT_CODE);
    relationshipHeaders.add(ATTRIBUTES_SIDE1);
    relationshipHeaders.add(ATTRIBUTESCOUPLING_SIDE1);
    relationshipHeaders.add(TAGS_SIDE1);
    relationshipHeaders.add(TAGSCOUPLING_SIDE1);
    relationshipHeaders.add(ATTRIBUTES_SIDE2);
    relationshipHeaders.add(ATTRIBUTESCOUPLING_SIDE2);
    relationshipHeaders.add(TAGS_SIDE2);
    relationshipHeaders.add(TAGSCOUPLING_SIDE2);
    relationshipHeaders.add(AUTOCREATESETTING);
    relationshipHeaders.add(RHYTHM);
    relationshipHeaders.add(CONTEXTTAGS_CODES);
    relationshipHeaders.add(MAXNOOFVERSION);
    relationshipHeaders.add(RELATIONSHIPCODES_TO_INHERIT);
    relationshipHeaders.add(RELATIONSHIP_INHERITANCE_COUPLING);
    relationshipHeaders.add(ALLOW_AFTERSAVE_EVENT_TO_BE_TRIGGERED_FOR_CHANGES);
    relationshipHeaders.add(TAXONOMY_INHERITANCE);
    classifierExcelMap.put(sheetName, new HashMap<>());
    Map<String, Object> relationshipMap = (Map<String, Object>) classifierExcelMap.get(sheetName);
    relationshipMap.put(HEADERS, relationshipHeaders);
    relationshipMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareAttributeSheet(Map<String, Object> attributeExcelMap)
  {
    LinkedHashSet<String> attributeHeader = new LinkedHashSet<>();
    attributeHeader.add(CODE);
    attributeHeader.add(NAME);
    attributeHeader.add(TYPE);
    attributeHeader.add(SUB_TYPE);
    attributeHeader.add(DESCRIPTION);
    attributeHeader.add(TOOLTIP);
    attributeHeader.add(DEFAULT_VALUE);
    attributeHeader.add(ICON);
    attributeHeader.add(PLACEHOLDER);
    attributeHeader.add(SEARCHABLE);
    attributeHeader.add(DEFAULT_UNIT);
    attributeHeader.add(PRECISION);
    attributeHeader.add(ALLOWED_STYLES);
    attributeHeader.add(FILTERABLE);
    attributeHeader.add(SORTABLE);
    attributeHeader.add(AVAILABILITY);
    attributeHeader.add(GRID_EDITABLE);
    attributeHeader.add(TYPE_OF_PROPERTY_CONCATENATED);
    attributeHeader.add(ATTRIBUTE_CODE_OF_PROPERTY_CONCATENATED);
    attributeHeader.add(VALUE_OF_PROPERTY_CONCATENATED);
    attributeHeader.add(CALCULATED_ATTRIBUTE_TYPE);
    attributeHeader.add(CALCULATED_ATTRIBUTE_UNIT);
    attributeHeader.add(TYPE_OF_PROPERTY_CALCULATED);
    attributeHeader.add(ATTRIBUTE_CODE_OF_PROPERTY_CALCULATED);
    attributeHeader.add(VALUE_OF_PROPERTY_CALCULATED);
    attributeHeader.add(READONLY);
    attributeHeader.add(SHOWCODETAG);
    attributeHeader.add(TRANSLATABLE);
    attributeHeader.add(REVISIONABLE);
    attributeHeader.add(STANDARD);
    attributeHeader.add(HIDE_SEPARATOR);
    attributeExcelMap.put(ATTRIBUTE_SHEET_NAME, new HashMap<>());
    Map<String, Object> attributeMap = (Map<String, Object>) attributeExcelMap.get(ATTRIBUTE_SHEET_NAME);//ATTRIBUTE Sheet
    attributeMap.put(HEADERS, attributeHeader);
    attributeMap.put(DATA, new ArrayList<>());
  }

  protected void preparePropertyCollectionSheet(Map<String, Object> propertyCollectionExcelMap)
  {
    LinkedHashSet<String> propertyCollectionHeaders = new LinkedHashSet<>();
    propertyCollectionHeaders.add(CODE_COLUMN_CONFIG);
    propertyCollectionHeaders.add(LABEL_COLUMN_CONFIG);
    propertyCollectionHeaders.add(TAB_COLUMN);
    propertyCollectionHeaders.add(ENTITY_CODE_COLUMN);
    propertyCollectionHeaders.add(ENTITY_TYPE_COLUMN);
    propertyCollectionHeaders.add(INDEX_COLUMN);
    propertyCollectionHeaders.add(ICON_COLUMN);
    propertyCollectionHeaders.add(IS_FOR_XRAY_COLUMN);
    propertyCollectionHeaders.add(IS_DEFAULT_FOR_XRAY_COLUMN);
    propertyCollectionHeaders.add(IS_STANDARD_COLUMN);
    
    propertyCollectionExcelMap.put(PROPERTY_COLLECTION_SHEET_NAME, new HashMap<>());
    Map<String, Object> propertyCollectionMap = (Map<String, Object>) propertyCollectionExcelMap.get(PROPERTY_COLLECTION_SHEET_NAME);
    propertyCollectionMap.put(HEADERS, propertyCollectionHeaders);
    propertyCollectionMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareContextSheet(Map<String, Object> contextExcelMap)
  {
    LinkedHashSet<String> contextHeaders = new LinkedHashSet<>();
    contextHeaders.add(CODE_COLUMN_CONFIG);
    contextHeaders.add(LABEL_COLUMN_CONFIG);
    contextHeaders.add(TYPE_COLUMN);
    contextHeaders.add(ICON_COLUMN);
    contextHeaders.add(IS_TIME_ENABLED);
    contextHeaders.add(ENABLE_TIME_FROM);
    contextHeaders.add(ENABLE_TIME_TO);
    contextHeaders.add(USE_CURRENT_TIME);
    contextHeaders.add(ALLOW_DUPLICATES);
    contextHeaders.add(SELECTED_TAGS);
    contextHeaders.add(SELECTED_TAG_VALUES);
    contextHeaders.add(ENTITIES);
    
    contextExcelMap.put(CONTEXT_SHEET_NAME, new HashMap<>());
    Map<String, Object> contextMap = (Map<String, Object>) contextExcelMap.get(CONTEXT_SHEET_NAME);
    contextMap.put(HEADERS, contextHeaders);
    contextMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareRelationshipSheet(Map<String, Object> relationshipExcelMap)
  {
    LinkedHashSet<String> relationshipHeaders = new LinkedHashSet<>();
    relationshipHeaders.add(CODE);
    relationshipHeaders.add(LABEL_COLUMN);
    relationshipHeaders.add(ICON);
    relationshipHeaders.add(IS_LITE);
    relationshipHeaders.add(TAB_CODE_COLUMN);
    relationshipHeaders.add(S1_CLASS_CODE);
    relationshipHeaders.add(S1_LABEL);
    relationshipHeaders.add(S1_CARDINALITY);
    relationshipHeaders.add(S1_EDITABLE);
    relationshipHeaders.add(S1_CONTEXT_CODE);
    relationshipHeaders.add(S2_CLASS_CODE);
    relationshipHeaders.add(S2_LABEL);
    relationshipHeaders.add(S2_CARDINALITY);
    relationshipHeaders.add(S2_EDITABLE);
    relationshipHeaders.add(S2_CONTEXT_CODE);
    relationshipHeaders.add(S1_PROPERTY_TYPE);
    relationshipHeaders.add(S1_PROPERTY_CODE);
    relationshipHeaders.add(S1_COUPLING_TYPE);
    relationshipHeaders.add(S2_PROPERTY_TYPE);
    relationshipHeaders.add(S2_PROPERTY_CODE);
    relationshipHeaders.add(S2_COUPLING_TYPE);
    relationshipHeaders.add(ALLOW_AFTERSAVE_EVENT);
    
    relationshipExcelMap.put(RELATIONSHIP_SHEET_NAME, new HashMap<>());
    Map<String, Object> relationshipMap = (Map<String, Object>) relationshipExcelMap.get(RELATIONSHIP_SHEET_NAME);
    relationshipMap.put(HEADERS, relationshipHeaders);
    relationshipMap.put(DATA, new ArrayList<>());   
  }

  public void prepareTagSheet(Map<String, Object> tagExcelMap)
  {
    LinkedHashSet<String> tagHeader = new LinkedHashSet<>();
    tagHeader.add(CODE);
    tagHeader.add(NAME);
    tagHeader.add(PARENT_CODE);
    tagHeader.add(TYPE);
    tagHeader.add(DESCRIPTION);
    tagHeader.add(COLOR);
    tagHeader.add(LINKED_MASTER_TAG_CODE);
    tagHeader.add(DEFAULT_VALUE);
    tagHeader.add(MULTISELECT);
    tagHeader.add(TOOLTIP);
    tagHeader.add(FILTERABLE);
    tagHeader.add(AVAILABILITY);
    tagHeader.add(GRID_EDITABLE);
    tagHeader.add(READ_ONLY_COLUMN);
    tagHeader.add(ICON);
    tagHeader.add(IMAGE_EXTENSION);
    tagHeader.add(IMAGE_RESOLUTION);
    tagHeader.add(REVISIONABLE);
    tagHeader.add(STANDARD);
    tagHeader.add(ACTION);
    tagExcelMap.put(TAG_SHEET_NAME, new HashMap<>());
    Map<String, Object> tagMap = (Map<String, Object>) tagExcelMap.get(TAG_SHEET_NAME);// TAG
                                                                                       // Sheet
    tagMap.put(HEADERS, tagHeader);
    tagMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareMasterTaxonomySheet(Map<String, Object> taxonomyExcelMap)
  {
    LinkedHashSet<String> taxonomyHeaders = new LinkedHashSet<>();
    taxonomyHeaders.add(CODE_COLUMN_CONFIG);
    taxonomyHeaders.add(LABEL_COLUMN_CONFIG);
    taxonomyHeaders.add(ICON_COLUMN);
    taxonomyHeaders.add(PARENT_CODE_COLUMN);
    taxonomyHeaders.add(TYPE_COLUMN);
    taxonomyHeaders.add(LEVEL_CODES_COLUMN);
    taxonomyHeaders.add(LEVEL_LABEL_COLUMN);
    taxonomyHeaders.add(IS_NEWLY_CREATED_LEVEL_COLUMN);
    taxonomyHeaders.add(LEVEL_INDEX_COLUMN);
    taxonomyHeaders.add(MASTER_TAG_PARENT_CODE_COLUMN);
    taxonomyHeaders.add(EVENTS_COLUMN);
    taxonomyHeaders.add(TASK_COLUMN);
    taxonomyHeaders.add(PROPERTY_COLLECTION_COLUMN);
    taxonomyHeaders.add(EMBEDDED_CLASS_COLUMN);    
    taxonomyExcelMap.put(MASTER_TAXONOMY_SHEET_NAME, new HashMap<>());
    Map<String, Object> masterTaxonomyMap = (Map<String, Object>) taxonomyExcelMap.get(MASTER_TAXONOMY_SHEET_NAME);
    masterTaxonomyMap.put(HEADERS, taxonomyHeaders);
    masterTaxonomyMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareTaxonomyHierarchySheet(Map<String, Object> taxonomyExcelMap)
  {
    LinkedHashSet<String> taxonomyHeaders = new LinkedHashSet<>();
    taxonomyHeaders.add(CODE_COLUMN_CONFIG);
    taxonomyHeaders.add(LABEL_COLUMN_CONFIG);
    taxonomyHeaders.add(PARENT_CODE_COLUMN);
    taxonomyHeaders.add(TYPE_COLUMN);
    taxonomyHeaders.add(LEVEL_CODES_COLUMN);
    taxonomyHeaders.add(LEVEL_LABEL_COLUMN);
    taxonomyHeaders.add(IS_NEWLY_CREATED_LEVEL_COLUMN);
    taxonomyHeaders.add(LEVEL_INDEX_COLUMN);
    taxonomyHeaders.add(MASTER_TAG_PARENT_CODE_COLUMN);
    taxonomyExcelMap.put(HIERARCHY_SHEET_NAME, new HashMap<>());
    Map<String, Object> hierarchyTaxonomyMap = (Map<String, Object>) taxonomyExcelMap.get(HIERARCHY_SHEET_NAME);
    hierarchyTaxonomyMap.put(HEADERS, taxonomyHeaders);
    hierarchyTaxonomyMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareUserSheet(Map<String, Object> userExcelMap)
  {
    LinkedHashSet<String> userHeaders = new LinkedHashSet<>();
    userHeaders.add(CODE);
    userHeaders.add(USERNAME);
    userHeaders.add(PASSWORD);
    userHeaders.add(FIRSTNAME);
    userHeaders.add(LASTNAME);
    userHeaders.add(GENDER);
    userHeaders.add(EMAIL);
    userHeaders.add(CONTACT);
    userHeaders.add(ICON);
    userHeaders.add(EMAIL_LOG_FILE);
    userHeaders.add(UI_LANGUAGE);
    userHeaders.add(DATA_LANGUAGE);
    
    
    userExcelMap.put(USER_SHEET_NAME, new HashMap<>());
    Map<String, Object> contextMap = (Map<String, Object>) userExcelMap.get(USER_SHEET_NAME);
    contextMap.put(HEADERS, userHeaders);
    contextMap.put(DATA, new ArrayList<>());
  }
  
  protected void prepareReferenceSheet(Map<String, Object> referenceExcelMap)
  {
    LinkedHashSet<String> referenceHeaders = new LinkedHashSet<>();
    referenceHeaders.add(CODE_COLUMN_CONFIG);
    referenceHeaders.add(LABEL_COLUMN_CONFIG);
    referenceHeaders.add(ICON_COLUMN);
    referenceHeaders.add(TAB_COLUMN);
    referenceHeaders.add(IS_STANDARD);
    referenceHeaders.add(S1_CLASS_TYPE_COLUMN);
    referenceHeaders.add(S1_CLASS_CODE_COlUMN);
    referenceHeaders.add(S1_CLASS_LABEL_COlUMN);
    referenceHeaders.add(S1_CLASS_CARDINALITY_COlUMN);
    referenceHeaders.add(S2_CLASS_TYPE_COLUMN);
    referenceHeaders.add(S2_CLASS_CODE_COLUMN);
    referenceHeaders.add(S2_CLASS_LABEL_COLUMN);
    referenceHeaders.add(S2_CLASS_CARDINALITY_COLUMN);
    
    referenceExcelMap.put(REFERENCE_SHEET_NAME, new HashMap<>());
    Map<String, Object> referenceMap = (Map<String, Object>) referenceExcelMap.get(REFERENCE_SHEET_NAME);
    referenceMap.put(HEADERS, referenceHeaders);
    referenceMap.put(DATA, new ArrayList<>());   
  }
  
  protected void prepareGoldenRecordRuleSheet(Map<String, Object> goldenRecordRuleExcelMap)
  {
    LinkedHashSet<String> goldenRuleHeaders = new LinkedHashSet<>();
    goldenRuleHeaders.add(CODE_COLUMN_CONFIG);
    goldenRuleHeaders.add(LABEL_COLUMN_CONFIG);
    goldenRuleHeaders.add(NATURE_TYPE);
    goldenRuleHeaders.add(PARTNERS);
    goldenRuleHeaders.add(PHYSICALCATALOGS);
    goldenRuleHeaders.add(MATCH_ATTRIBUTES);
    goldenRuleHeaders.add(MATCH_TAGS);
    goldenRuleHeaders.add(MATCH_NON_NATURE_CLASSES);
    goldenRuleHeaders.add(MATCH_TAXONOMIES);
    goldenRuleHeaders.add(AUTOCREATE);
    goldenRuleHeaders.add(MERGE_ATTRIBUTES);
    goldenRuleHeaders.add(MERGE_ATTRIBUTES_LATEST);
    goldenRuleHeaders.add(MERGE_ATTRIBUTES_SUPPLIERS);
    goldenRuleHeaders.add(MERGE_TAGS);
    goldenRuleHeaders.add(MERGE_TAGS_LATEST);
    goldenRuleHeaders.add(MERGE_TAGS_SUPPLIERS);
    goldenRuleHeaders.add(MERGE_RELATIONSHIPS);
    goldenRuleHeaders.add(MERGE_RELATIONSHIPS_SUPPLIERS);
    goldenRuleHeaders.add(MERGE_NATURE_RELATIONSHIPS);
    goldenRuleHeaders.add(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS);

    goldenRecordRuleExcelMap.put(GOLDEN_RECORD_RULES_SHEET_NAME, new HashMap<>());
    Map<String, Object> goldenRuleMap = (Map<String, Object>) goldenRecordRuleExcelMap.get(GOLDEN_RECORD_RULES_SHEET_NAME);
    goldenRuleMap.put(HEADERS, goldenRuleHeaders);
    goldenRuleMap.put(DATA, new ArrayList<>());   
  }
  
  protected void prepareOrganizationSheet(Map<String, Object> organizationExcelMap)
  {
    LinkedHashSet<String> organizationHeaders = new LinkedHashSet<>();
    organizationHeaders.add(CODE_COLUMN_CONFIG);
    organizationHeaders.add(LABEL_COLUMN_CONFIG);
    organizationHeaders.add(TYPE_COLUMN);
    organizationHeaders.add(ICON_COLUMN);
    organizationHeaders.add(PHYSICAL_CATALOG);
    organizationHeaders.add(PORTALS);
    organizationHeaders.add(TAXONOMY_CODE);
    organizationHeaders.add(TARGET_CLASS_CODE);
    organizationHeaders.add(SYSTEM_CODE);
    organizationHeaders.add(ENDPOINT_CODE);
    organizationHeaders.add(ROLE_CODE);
    organizationHeaders.add(ROLE_LABEL);
    organizationHeaders.add(ROLE_TYPE);
    organizationHeaders.add(ROLE_PHYSICAL_CATALOG);
    organizationHeaders.add(ROLE_PORTALS);
    organizationHeaders.add(ROLE_TAXONOMY_CODE);
    organizationHeaders.add(ROLE_TARGET_CLASS_CODE);
    organizationHeaders.add(ROLE_USERS);
    organizationHeaders.add(ROLE_ENABLED_DASHBOARD);
    organizationHeaders.add(ROLE_KPI);
    organizationHeaders.add(ROLE_ENTITIES);
    organizationHeaders.add(ROLE_SYSTEM_CODE);
    organizationHeaders.add(ROLE_ENDPOINT_CODE);
    organizationHeaders.add(ROLE_READONLY_USER);
    organizationExcelMap.put(PARTNER_SHEET_NAME, new HashMap<>());
    Map<String, Object> organizationMap = (Map<String, Object>) organizationExcelMap.get(PARTNER_SHEET_NAME);
    organizationMap.put(HEADERS, organizationHeaders);
    organizationMap.put(DATA, new ArrayList<>());   
  }
  
  protected void prepareTranslationSheet(Map<String, Object> translationExcelMap, String sheetName)
  {
    LinkedHashSet<String> translationHeaders = new LinkedHashSet<>();
    translationHeaders.add(ENTITY_TYPE_COLUMN);
    translationHeaders.add(ENTITY_CODE_COLUMN);
    translationHeaders.add(LABEL_COLUMN_CONFIG);
    translationHeaders.add(DESCRIPTION_COLUMN_CONFIG);
    translationHeaders.add(PLACEHOLDER_COLUMN_CONFIG);
    translationHeaders.add(TOOLTIP_COLUMN_CONFIG);
    translationHeaders.add(SIDE1_LABEL);
    translationHeaders.add(SIDE2_LABEL);
    
    translationExcelMap.put(sheetName, new HashMap<>());
    Map<String, Object> translationMap = (Map<String, Object>) translationExcelMap.get(sheetName);
    translationMap.put(HEADERS, translationHeaders);
    translationMap.put(DATA, new ArrayList<>());   
  }
  
  /**
   * @param userExcelMap
   */
  protected void prepareLanguageSheet(Map<String, Object> userExcelMap)
  {
    LinkedHashSet<String> userHeaders = new LinkedHashSet<>();
    userHeaders.add(CODE);
    userHeaders.add(LABEL_COLUMN);
    userHeaders.add(PARENTCODE);
    userHeaders.add(ABBREVIATION);
    userHeaders.add(LOCALE_ID);
    userHeaders.add(NUMBER_FORMAT);
    userHeaders.add(DATE_FORMAT);
    userHeaders.add(ICON);
    userHeaders.add(IS_DATA_LANGUAGE);
    userHeaders.add(IS_UI_LANGUAGE);
    userHeaders.add(IS_DEFULT_LANGUAGE);

    userExcelMap.put(LANGUAGE_SHEET_NAME, new HashMap<>());
    Map<String, Object> contextMap = (Map<String, Object>) userExcelMap.get(LANGUAGE_SHEET_NAME);
    contextMap.put(HEADERS, userHeaders);
    contextMap.put(DATA, new ArrayList<>());
  }
  
  /**
   * Read the CSID from give PXON
   * and get the Property Type
   * @param blockInfoData
   * @return
   * @throws CSFormatException
   */
  public ICSEObject parseCSEElement(String blockInfoData) throws CSFormatException
  {
    JSONObject pxonInput = JSONContent.StringToJSON(blockInfoData);
    ICSEElement cseElement = (new CSEParser()).parseDefinition((String) pxonInput.get(PXONTag.csid.toTag()));
    return (ICSEObject) cseElement;
  } 
  
  /**
   * Prepare map for flat level field of attributes, 
   * 
   * @param transformedAttributeMap
   * @param configAttributeDTO
   */
  protected void transformAttribute(Map<String, String> transformedAttributeMap,
      ConfigAttributeDTO configAttributeDTO)
  {
    transformedAttributeMap.put(ALLOWED_STYLES, String.join(SEPARATOR, configAttributeDTO.getAllowedStyles()));
    transformedAttributeMap.put(AVAILABILITY,String.join(SEPARATOR, configAttributeDTO.getAvailability()));
    transformedAttributeMap.put(CODE, configAttributeDTO.getCode());
    // boolean
    transformedAttributeMap.put(TRANSLATABLE, configAttributeDTO.isTranslatable() ? "1" : "0");
    transformedAttributeMap.put(DISABLED, configAttributeDTO.isDisabled() ? "1" : "0");
    transformedAttributeMap.put(FILTERABLE, configAttributeDTO.isFilterable() ? "1" : "0");
    transformedAttributeMap.put(GRID_EDITABLE, configAttributeDTO.isGridEditable() ? "1" : "0");
    transformedAttributeMap.put(MANDATORY, configAttributeDTO.isMandatory() ? "1" : "0");
    transformedAttributeMap.put(SEARCHABLE, configAttributeDTO.isSearchable() ? "1" : "0");
    transformedAttributeMap.put(SORTABLE, configAttributeDTO.isSortable() ? "1" : "0");
    transformedAttributeMap.put(STANDARD, configAttributeDTO.isStandard() ? "1" : "0");
    transformedAttributeMap.put(REVISIONABLE, configAttributeDTO.isVersionable() ? "1" : "0");
    transformedAttributeMap.put(HIDE_SEPARATOR, configAttributeDTO.hideSeparator() ? "1" : "0");
    transformedAttributeMap.put(READONLY, configAttributeDTO.isDisabled() ? "1" : "0");
    transformedAttributeMap.put(ICON, configAttributeDTO.getIcon());//PXPFDEV-16987
    transformedAttributeMap.put(DEFAULT_UNIT, configAttributeDTO.getDefaultUnit());
    transformedAttributeMap.put(DEFAULT_VALUE, configAttributeDTO.getDefaultValue());
    transformedAttributeMap.put(DEFAULT_VALUE_AS_HTML, configAttributeDTO.getDefaultValueAsHTML());
    transformedAttributeMap.put(DESCRIPTION, configAttributeDTO.getDescription());
    //transformedAttributeMap.put(FORMULA, configAttributeDTO.getFormula());
    transformedAttributeMap.put(NAME, configAttributeDTO.getLabel());
    transformedAttributeMap.put(PLACEHOLDER, configAttributeDTO.getPlaceHolder());
    transformedAttributeMap.put(PRECISION, String.valueOf(configAttributeDTO.getPrecision()));
    transformedAttributeMap.put(TOOLTIP, configAttributeDTO.getToolTip());
    transformedAttributeMap.put(DEFAULT_UNIT, configAttributeDTO.getDefaultUnit());
    String type = configAttributeDTO.getPropertyDTO().getPropertyType().name();
    transformedAttributeMap.put(TYPE, type);
    transformedAttributeMap.put(SUB_TYPE, configAttributeDTO.getString(ConfigTag.subType));
    if (PropertyType.DATE.name()
        .equalsIgnoreCase(type) && configAttributeDTO.getDefaultValue() != null
        && !configAttributeDTO.getDefaultValue().isEmpty()) {
      long longAttributeValue = Long.parseLong(configAttributeDTO.getDefaultValue());
      transformedAttributeMap.put(DEFAULT_VALUE, DiTransformationUtils.getTimeStampForFormat(
          longAttributeValue != 0 ? longAttributeValue : null, DiConstants.DATE_FORMAT));
    }
    else {
      transformedAttributeMap.put(DEFAULT_VALUE, configAttributeDTO.getDefaultValue());
    }
    if (PropertyType.CONCATENATED.name().equalsIgnoreCase(type)) {
      transformedAttributeMap.put(SHOWCODETAG, configAttributeDTO.isCodeVisible() ? "1" : "0");
      if (!configAttributeDTO.getAttributeConcatenatedList().isEmpty()) {
        formulaForConcatenatedAttributeType(transformedAttributeMap, configAttributeDTO);
      }
    }
    if (PropertyType.CALCULATED.name().equalsIgnoreCase(type)) {
      if (configAttributeDTO.getCalculatedAttributeType() != null) {
        transformedAttributeMap.put(CALCULATED_ATTRIBUTE_TYPE,
            CalculatedAttributeTypes
                .valueOf(IConfigClass.PropertyClass.valueOfClassPath(configAttributeDTO.getCalculatedAttributeType().toString()).name())
                .toString());
      }
      if (configAttributeDTO.getCalculatedAttributeUnit() != null) {
        transformedAttributeMap.put(CALCULATED_ATTRIBUTE_UNIT, configAttributeDTO.getCalculatedAttributeUnit().toString());
      }
      if (!configAttributeDTO.getAttributeOperatorList().isEmpty()) {
        formulaForCalculatedAttributeType(transformedAttributeMap, configAttributeDTO);
      }
    }
  }


/**
 * Implementation of formula for Attribute Type-Concatenated
 * 
 * @param transformedAttributeMap
 * @param configAttributeDTO
 */
private void formulaForConcatenatedAttributeType(Map<String, String> transformedAttributeMap, ConfigAttributeDTO configAttributeDTO)
{
  List<String> typesOfPropertyConcat = new ArrayList<String>();
  List<String> entityCodesOfPropertyConcat = new ArrayList<String>();
  List<String> valuesOfPropertyConcat = new ArrayList<String>();
  List<IJSONContent> attributeConcatenatedList = configAttributeDTO.getAttributeConcatenatedList();
  for (int i = 0; i < attributeConcatenatedList.size(); i++) {
    JSONObject jsonElement = (JSONObject) attributeConcatenatedList.get(i);
    typesOfPropertyConcat.add((String) jsonElement.get(CommonConstants.TYPE));
    if (jsonElement.containsKey(ATTRIBUTE_ID)) {
      entityCodesOfPropertyConcat.add((String) jsonElement.get(ATTRIBUTE_ID));
    }
    else if (jsonElement.containsKey(TAG_ID)) {
      entityCodesOfPropertyConcat.add((String) jsonElement.get(TAG_ID));
    }
    else {
      entityCodesOfPropertyConcat.add(EMPTY_STRING);
    }
    if (jsonElement.containsKey(CommonConstants.VALUE_PROPERTY)) {
      valuesOfPropertyConcat.add((String) jsonElement.get(CommonConstants.VALUE_PROPERTY));
    }
    else {
      valuesOfPropertyConcat.add(EMPTY_STRING);
    }
  }
  transformedAttributeMap.put(TYPE_OF_PROPERTY_CONCATENATED, String.join(SEPARATOR, typesOfPropertyConcat));
  transformedAttributeMap.put(ATTRIBUTE_CODE_OF_PROPERTY_CONCATENATED, String.join(SEPARATOR, entityCodesOfPropertyConcat));
  transformedAttributeMap.put(VALUE_OF_PROPERTY_CONCATENATED, String.join(SEPARATOR, valuesOfPropertyConcat));
}


/**
 * Implementation of formula for Attribute Type-Calculated
 * 
 * @param transformedAttributeMap
 * @param configAttributeDTO
 */
private void formulaForCalculatedAttributeType(Map<String, String> transformedAttributeMap, ConfigAttributeDTO configAttributeDTO)
{
  List<IJSONContent> attributeCalculatedList = configAttributeDTO.getAttributeOperatorList();
  List<String> typesOfPropertyCalculated = new ArrayList<String>();
  List<String> entityCodesOfPropertyCalculated = new ArrayList<String>();
  List<String> valuesOfPropertyCalculated = new ArrayList<String>();
  
  for (int i = 0; i < attributeCalculatedList.size(); i++) {
    JSONObject jsonElement = (JSONObject) attributeCalculatedList.get(i);
    String typeKey = (String) jsonElement.get(CommonConstants.TYPE);
    typesOfPropertyCalculated.add(typeKey);
    if (typeKey.equals(ATTRIBUTE)) {
      entityCodesOfPropertyCalculated.add((String) jsonElement.get(ATTRIBUTE_ID));
      valuesOfPropertyCalculated.add(EMPTY_STRING);
    }
    else if (typeKey.equals(VALUE)) {
      valuesOfPropertyCalculated.add((String) jsonElement.get(CommonConstants.VALUE_PROPERTY));
      entityCodesOfPropertyCalculated.add(EMPTY_STRING);
    }
    else {
      // any of the operators
      entityCodesOfPropertyCalculated.add(EMPTY_STRING);
      valuesOfPropertyCalculated.add(EMPTY_STRING);
    }
  }
  transformedAttributeMap.put(TYPE_OF_PROPERTY_CALCULATED, String.join(SEPARATOR, typesOfPropertyCalculated));
  transformedAttributeMap.put(ATTRIBUTE_CODE_OF_PROPERTY_CALCULATED, String.join(SEPARATOR, entityCodesOfPropertyCalculated));
  transformedAttributeMap.put(VALUE_OF_PROPERTY_CALCULATED, String.join(SEPARATOR, valuesOfPropertyCalculated));
}


protected void fillSide2Coupling(Map<String, String> transformedRelationshipMap, JSONObject side2Coupling)
{
  transformedRelationshipMap.put(S2_PROPERTY_TYPE, (String) side2Coupling.getOrDefault(TYPE_PROPERTY, new String()));
  transformedRelationshipMap.put(S2_PROPERTY_CODE, (String) side2Coupling.getOrDefault(ID_PROPERTY, new String()));
    transformedRelationshipMap.put(S2_COUPLING_TYPE,
        DiTransformationUtils.getCouplingType((String) side2Coupling.getOrDefault(COUPLING_TYPE, new String())));
  }

protected void fillSide1Coupling(Map<String, String> transformedRelationshipMap, JSONObject side1Coupling)
{
  transformedRelationshipMap.put(S1_PROPERTY_TYPE, (String) side1Coupling.getOrDefault(TYPE_PROPERTY, new String()));
  transformedRelationshipMap.put(S1_PROPERTY_CODE, (String) side1Coupling.getOrDefault(ID_PROPERTY, new String()));
    transformedRelationshipMap.put(S1_COUPLING_TYPE,
        DiTransformationUtils.getCouplingType((String) side1Coupling.getOrDefault(COUPLING_TYPE, new String())));
}


/**
 * Prepare map of common field of klass entity.
 * 
 * @param transformedKlassMap
 * @param configClassDTO
 */
protected void transformCommonFieldOfKlassEntity(Map<String, Object> transformedKlassMap, ConfigClassifierDTO configClassDTO)
{
  IClassifierDTO classifierDTO = configClassDTO.getClassifierDTO();
  transformedKlassMap.put(CODE_COLUMN_CONFIG, classifierDTO.getCode());
  transformedKlassMap.put(LABEL_COLUMN_CONFIG, configClassDTO.getLabel());
  transformedKlassMap.put(PARENT_CODE_COLUMN,  configClassDTO.getParentCode().isEmpty() ? "-1" : configClassDTO.getParentCode());
  transformedKlassMap.put(NATURE_TYPE, configClassDTO.getNatureType());
  transformedKlassMap.put(MAX_VERSION, String.valueOf(configClassDTO.getNumberOfVersionsToMaintain()));
  transformedKlassMap.put(LIFE_CYCLE_STATUS_TAG,  getSepratedValues(configClassDTO.getStatusTags()));
  transformedKlassMap.put(ICON_COLUMN, configClassDTO.getIcon());//PXPFDEV-16987
  transformedKlassMap.put(PREVIEW_IMAGE, "");//TODO: Mapping not found
 // transformedKlassMap.put(EVENT_CODES, String.join(separator, configClassDTO.getEvents()));
  transformedKlassMap.put(TASK_CODES, getSepratedValues(configClassDTO.getTasks()));
}

/**
 * prepare map for variants.
 * 
 * @param transformedExcelMap
 * @param configClassDTO
 * @param variantsList
 */
protected void prepareKlassifierVariants(ConfigClassifierDTO configClassDTO,
    List<Map<String, String>> variantsList)
{
  Map<String, IConfigEmbeddedRelationDTO> embeddedClasses = configClassDTO.getEmbeddedClasses();
  Iterator<String> keySet = embeddedClasses.keySet().iterator();
  while (keySet.hasNext()) {
    String key = keySet.next();
    ConfigEmbeddedRelationDTO configEmbeddedRelationDTO = (ConfigEmbeddedRelationDTO) embeddedClasses.get(key);
    Collection<IJSONContent> couplings = configEmbeddedRelationDTO.getCouplings();
    Iterator<IJSONContent> iterator = couplings.iterator();
    do {
      Map<String, String> variantMap = new HashMap<>();
      variantMap.put(CLASS_TYPE, EMBEDDED); // TODO: Need to change by RDBMS
      variantMap.put(CLASS_CODE, key);
      if (iterator.hasNext()) {
        JSONObject coupling = (JSONObject) iterator.next();
        variantMap.put(PROPERTY_TYPE, (String) coupling.get(CommonConstants.TYPE));
        variantMap.put(PROPERTY_CODE, (String) coupling.get(CommonConstants.CODE_PROPERTY));
        variantMap.put(COUPLING, DiTransformationUtils.getCouplingType((String) coupling.get(COUPLING_TYPE)));
      }
      variantsList.add(variantMap);
    }while (iterator.hasNext());
  }
}

/**
 * prepare map for relationship sheet.
 * 
 * @param transformedExcelMap
 * @param configClassDTO
 * @param relationshipList
 */
protected void prepareKlassifirerRelationhips(ConfigClassifierDTO configClassDTO,
    List<Map<String, String>> relationshipList)
{
  Collection<IConfigNatureRelationshipDTO> relationships = configClassDTO.getRelationships();
  Iterator<IConfigNatureRelationshipDTO> relationshipIterator = relationships.iterator();
  while (relationshipIterator.hasNext()) {
    IConfigNatureRelationshipDTO relationship = relationshipIterator.next();
    Map<String, String> relationshipMap = new HashMap<>();
    String type = relationship.getRelationshipType();
    relationshipMap.put(RELATIONSHIP_TYPE, type);
    if (type.equals(PRODUCTVARIANTRELATIONSIP)) {
      configClassDTO.getProductVariantContextCode().forEach(action -> relationshipMap.put(CONTEXT_CODE, action));
    }
    else if (type.equals(PROMOTIONALVERSIONCONTEXTCODE)) {
      configClassDTO.getPromotionalVersionContextCode().forEach(action -> relationshipMap.put(CONTEXT_CODE, action));
    }
    else {
      relationshipMap.put(CONTEXT_CODE, relationship.getSide2().getContextCode());
    } 
    relationshipMap.put(SIDE2_CLASS_CODE,relationship.getSide2().getClassCode());
    relationshipMap.put(TAB_CODE, relationship.getTab());
    
    
    Collection<IJSONContent> side1Couplings = relationship.getSide1().getCouplings();
    Map<String, List<String>> side1CouplingInfo =  getCouplingInfo(side1Couplings);
    relationshipMap.put(ATTRIBUTES_SIDE1,String.join(";", side1CouplingInfo.get(ATTRIBUTES)));
    relationshipMap.put(ATTRIBUTESCOUPLING_SIDE1, String.join(";", side1CouplingInfo.get(ATTRIBUTES_COUPLING)));
    relationshipMap.put(TAGS_SIDE1, String.join(";", side1CouplingInfo.get(TAGS)));
    relationshipMap.put(TAGSCOUPLING_SIDE1, String.join(";", side1CouplingInfo.get(TAGS_COUPLING)));
    
    Collection<IJSONContent> side2Couplings = relationship.getSide2().getCouplings();
    Map<String, List<String>> side2CouplingInfo =  getCouplingInfo(side2Couplings);
    relationshipMap.put(ATTRIBUTES_SIDE2,String.join(";", side2CouplingInfo.get(ATTRIBUTES)));
    relationshipMap.put(ATTRIBUTESCOUPLING_SIDE2, String.join(";", side2CouplingInfo.get(ATTRIBUTES_COUPLING)));
    relationshipMap.put(TAGS_SIDE2, String.join(";", side2CouplingInfo.get(TAGS)));
    relationshipMap.put(TAGSCOUPLING_SIDE2, String.join(";", side2CouplingInfo.get(TAGS_COUPLING)));
    
    relationshipMap.put(AUTOCREATESETTING, "");
    relationshipMap.put(RHYTHM, "");
    relationshipMap.put(CONTEXTTAGS_CODES, "");
    relationshipMap.put(MAXNOOFVERSION, "");
    relationshipMap.put(RELATIONSHIPCODES_TO_INHERIT, "");
    relationshipMap.put(RELATIONSHIP_INHERITANCE_COUPLING, "");
    relationshipMap.put(ALLOW_AFTERSAVE_EVENT_TO_BE_TRIGGERED_FOR_CHANGES, relationship.isEnableAfterSave() ? "1" : "0");
    relationshipMap.put(TAXONOMY_INHERITANCE, relationship.getTaxonomyInheritanceSetting());
  
    relationshipList.add(relationshipMap);
  }
}


/**
 * @param side1Couplings
 * @return the map of coupling information.
 */
private Map<String, List<String>> getCouplingInfo(Collection<IJSONContent> side1Couplings)
{
  Iterator<IJSONContent> iterator = side1Couplings.iterator();
  Map<String, List<String>> couplingInfoMap = new HashMap<>();
  List<String> attributes = new ArrayList<>();
  List<String> tags = new ArrayList<>();
  List<String> attributesCoupling = new ArrayList<>();
  List<String> tagsCoupling = new ArrayList<>();
  while(iterator.hasNext()) {
    JSONObject coupling = (JSONObject) iterator.next();
    if(coupling.get(CommonConstants.TYPE).equals(CommonConstants.ATTRIBUTE)) {
     // attributes.add((String) coupling.get(CommonConstants.CODE_PROPERTY));
      attributes.add((String) coupling.get(CommonConstants.ID_PROPERTY));
      attributesCoupling.add(DiTransformationUtils.getCouplingType((String) coupling.get(COUPLING_TYPE)));
    }else if(coupling.get(CommonConstants.TYPE).equals(CommonConstants.TAG)) {
      //tags.add((String) coupling.get(CommonConstants.CODE_PROPERTY));
      tags.add((String) coupling.get(CommonConstants.ID_PROPERTY));
      tagsCoupling.add(DiTransformationUtils.getCouplingType((String) coupling.get(COUPLING_TYPE)));
    }
  }
  
  couplingInfoMap.put(ATTRIBUTES, attributes);
  couplingInfoMap.put(TAGS, tags);
  couplingInfoMap.put(ATTRIBUTES_COUPLING, attributesCoupling);
  couplingInfoMap.put(TAGS_COUPLING, tagsCoupling);
  
  return couplingInfoMap;
} 

/**
 * Map boolean flag in Excel 
 * as and when required
 * @param configClassDTO
 * @param transformedKlassMap
 */
protected void prepareBooleanValues(ConfigClassifierDTO configClassDTO, Map<String, Object> transformedKlassMap)
{
  transformedKlassMap.put(IS_NATURE, configClassDTO.isNature() ? "1":"0");
  transformedKlassMap.put(IS_ABSTRACT, configClassDTO.isAbstract() ? "1" : "0");
  transformedKlassMap.put(IS_DEFAULT, configClassDTO.isDefault() ? "1" : "0");
  transformedKlassMap.put((DETECT_DUPLICATE), configClassDTO.isDetectDuplicate() ? "1" : "0");
  transformedKlassMap.put((EXTRACT_ZIP), configClassDTO.isUploadZip() ? "1" : "0");
  transformedKlassMap.put(ITransformationTask.TRACKDOWNLOAD, configClassDTO.isTrackDownloads() ? "1" : "0");
}


/**
 * Prepare Property Collection Sheet 
 * for Classes
 * @param transformedExcelMap
 * @param configClassDTO
 * @param propCollList
 */
protected void prepareKlassifierPropertyCollections(ConfigClassifierDTO configClassDTO,
    List<Map<String, Object>> propCollList)
{
  Map<String, Collection<IConfigSectionElementDTO>> sectionMap = configClassDTO.getSections();
  Iterator<String> keySet = sectionMap.keySet().iterator();
  
  while (keySet.hasNext()) {
    String key = keySet.next();
    Collection<IConfigSectionElementDTO> values = sectionMap.get(key);
    values.stream().filter(obj -> obj instanceof IConfigSectionElementDTO).map(obj -> (IConfigSectionElementDTO) obj).forEach(obj -> {
      Map<String, Object> propCollEntity = new HashMap<String, Object>();
      propCollEntity.put(PROPERTY_COLLECTION_CODE, key);
      propCollEntity.put(PROPERTY_CODE, obj.getCode());
      propCollEntity.put(PROPERTY_TYPE, obj.getType());
      propCollEntity.put(ALLOWED_VALUES_COLUMN, getSepratedValues(obj.getSelectedTagValues()));
      propCollEntity.put(IS_INHERTIED, String.valueOf(obj.isInherited()));
      propCollEntity.put(PRECISION_COLUMN, String.valueOf(obj.getPrecision()));
      propCollEntity.put(DEFAULT_VALUE, getDefaultValue(obj));
      propCollEntity.put(COUPLING_COLUMN, DiTransformationUtils.getCouplingType(obj.getCouplingType()));
      propCollEntity.put(MANDATORY_COLUMN, obj.isMandatory() ? "must" : (obj.isShould() ? "should" : "can"));
      propCollEntity.put(SKIPPED_COLUMN, obj.isSkipped() ? "1" : "0");
      propCollEntity.put(TYPE_COLUMN, obj.getType().equals(CommonConstants.TAG_PROPERTY) ? obj.getTagType() : obj.getRendereType());
      propCollEntity.put(MULTISELECT_COLUMN, obj.getType().equals(CommonConstants.TAG_PROPERTY) ? (obj.isMultiSelect() ? "1" : "0") : "");
      propCollEntity.put(ATTRIBUTE_CONTEXT_COLUMN, obj.getAttributeVariantContextCode());
      propCollEntity.put(PREFIX_COLUMN, obj.getPrefix());
      propCollEntity.put(SUFFIX_COLUMN, obj.getSuffix());
      propCollEntity.put(PRODUCT_IDENTIFIER_COLUMN, obj.isIdentifier() ? "1" : "0");
      propCollEntity.put(LANGUAGE_DEPENDENT_COLUMN, obj.isTranslatable() ? "1" : "0");
      propCollEntity.put(REVISIONABLE_COLUMN, obj.isVersionable() ? "1" : "0");
      propCollList.add(propCollEntity);
    });
  }
}

/**
 * Prepare map for flat level field of attributes, 
 * 
 * @param transformedAttributeMap
 * @param configAttributeDTO
 */
protected void transformTaxonomy(Map<String, Object> transformedToxnomyMap, ConfigClassifierDTO configClassifierDTO, String type)
{
  IClassifierDTO classifierDTO = configClassifierDTO.getClassifierDTO();
  transformedToxnomyMap.put(CODE_COLUMN_CONFIG, classifierDTO.getCode());
  transformedToxnomyMap.put(LABEL_COLUMN_CONFIG, configClassifierDTO.getLabel());
  transformedToxnomyMap.put(PARENT_CODE_COLUMN, configClassifierDTO.getParentCode());
  fillLevelInfo(transformedToxnomyMap, configClassifierDTO);
  Integer levelIndex = configClassifierDTO.getLevelIndex();
  if (levelIndex != 0) {
    transformedToxnomyMap.put(LEVEL_INDEX_COLUMN, String.valueOf(levelIndex));
  }
  transformedToxnomyMap.put(MASTER_TAG_PARENT_CODE_COLUMN, configClassifierDTO.getMasterParentTag());
  if (parentCode.equals(configClassifierDTO.getParentCode())) {
    transformedToxnomyMap.put(TYPE_COLUMN, configClassifierDTO.getConfigTaxonomyType());
  }
  if (MASTER_TAXONOMY_SHEET_NAME.equals(type)) {
    transformedToxnomyMap.put(ICON_COLUMN, configClassifierDTO.getIcon());
 //   transformedToxnomyMap.put(EVENTS_COLUMN, getSepratedValues(configClassifierDTO.getEvents()));
    transformedToxnomyMap.put(TASK_COLUMN, getSepratedValues(configClassifierDTO.getTasks()));
  }
}

protected void fillTranslationDetails(Map<String, String> transformedTranslationMap, Map<String, String> languageObject)
{
  transformedTranslationMap.put(LABEL_COLUMN_CONFIG, languageObject.get(LABEL));
  transformedTranslationMap.put(SIDE1_LABEL, languageObject.get(SIDE1_LABEL_VAL));
  transformedTranslationMap.put(SIDE2_LABEL, languageObject.get(SIDE2_LABEL_VAL));
  transformedTranslationMap.put(DESCRIPTION_COLUMN_CONFIG, languageObject.get(DESCRIPTION_VAL));
  transformedTranslationMap.put(PLACEHOLDER_COLUMN_CONFIG, languageObject.get(PLACEHOLDER_VAL));
  transformedTranslationMap.put(TOOLTIP_COLUMN_CONFIG, languageObject.get(TOOLTIP_VAL)); 
}

protected void prepareEntitiesAndTypeMapForContext(Map<String, String> entitiesToAddMap, Map<ContextType, String> typeOfContextMap)
{
  // entities Map
  entitiesToAddMap.put(ARTICLE_INSTANCE, ARTICLE_KEY);
  entitiesToAddMap.put(ASSET_INSTANCE, ASSET_KEY);
  entitiesToAddMap.put(MARKET_INSTANCE, MARKET_KEY);
  entitiesToAddMap.put(SUPPLIER_INSTANCE, SUPPLIER_KEY);
  entitiesToAddMap.put(TEXT_ASSET_INSTANCE, TEXT_ASSET_KEY);
  
  // type of Context Map
  typeOfContextMap.put(ContextType.ATTRIBUTE_CONTEXT, ATTRIBUTE_VARIANT_CONTEXT);
  typeOfContextMap.put(ContextType.RELATIONSHIP_VARIANT, RELATIONSHIP_VARIANT_CONTEXT);
  typeOfContextMap.put(ContextType.LINKED_VARIANT, PRODUCT_VARIANT_CONTEXT);
  
}

protected void prepareMainPermissionSheet(Map<String, Object> mainPermissionExcelMap)
{
  LinkedHashSet<String> mainPermissionHeaders = new LinkedHashSet<>();
  mainPermissionHeaders.add(PERMISSION_TYPE);
  mainPermissionHeaders.add(KLASS_CODE);
  mainPermissionHeaders.add(PERMISSION_CREATE);
  mainPermissionHeaders.add(PERMISSION_DELETE);
  mainPermissionHeaders.add(PERMISSION_DOWNLOAD);
  mainPermissionHeaders.add(PERMISSION_READ);
  mainPermissionHeaders.add(PERMISSION_UPDATE);
 
  mainPermissionExcelMap.put(MAIN_PERMISSIONS_SHEET_NAME, new HashMap<>());
  Map<String, Object> mainPermissionMap = (Map<String, Object>) mainPermissionExcelMap.get(MAIN_PERMISSIONS_SHEET_NAME);
  mainPermissionMap.put(HEADERS, mainPermissionHeaders);
  mainPermissionMap.put(DATA, new ArrayList<>());   
}

protected void prepareGeneralInfoPermissionSheet(Map<String, Object> generalInfoPermissionExcelMap)
{
  LinkedHashSet<String> generalInfoPermissionHeaders = new LinkedHashSet<>();
  generalInfoPermissionHeaders.add(KLASS_CODE);
  generalInfoPermissionHeaders.add(GENERAL_INFO_LABEL);
  generalInfoPermissionHeaders.add(VISIBLE);
  generalInfoPermissionHeaders.add(CAN_EDIT);
  generalInfoPermissionHeaders.add(CAN_ADD);
  generalInfoPermissionHeaders.add(CAN_REMOVE);
  
  generalInfoPermissionExcelMap.put(GENERALI_NFORMATION_PERMISSIONS_SHEET_NAME, new HashMap<>());
  Map<String, Object> generalInfoPermissionMap = (Map<String, Object>) generalInfoPermissionExcelMap
      .get(GENERALI_NFORMATION_PERMISSIONS_SHEET_NAME);
  generalInfoPermissionMap.put(HEADERS, generalInfoPermissionHeaders);
  generalInfoPermissionMap.put(DATA, new ArrayList<>());
}

protected void preparePropertyPermissionSheet(Map<String, Object> propertyPermissionExcelMap)
{
  LinkedHashSet<String> propertyPermissionHeaders = new LinkedHashSet<>();
  propertyPermissionHeaders.add(KLASS_CODE);
  propertyPermissionHeaders.add(ATTRIBUTE_TAG_CODE);
  propertyPermissionHeaders.add(VISIBLE);
  propertyPermissionHeaders.add(CAN_EDIT);
  propertyPermissionHeaders.add(PROPERTY_TYPE);

  propertyPermissionExcelMap.put(PROPERTY_PERMISSIONS_SHEET_NAME, new HashMap<>());
  Map<String, Object> propertyPermissionMap = (Map<String, Object>) propertyPermissionExcelMap
      .get(PROPERTY_PERMISSIONS_SHEET_NAME);
  propertyPermissionMap.put(HEADERS, propertyPermissionHeaders);
  propertyPermissionMap.put(DATA, new ArrayList<>());
}

protected void prepareRelationShipPermissionSheet(Map<String, Object> relationshipPermissionExcelMap)
{
  LinkedHashSet<String> relationshipPermissionHeaders = new LinkedHashSet<>();
  relationshipPermissionHeaders.add(KLASS_CODE);
  relationshipPermissionHeaders.add(PERMISSION_RELATIONSHIPS_COLOUMN);
  relationshipPermissionHeaders.add(VISIBLE);
  relationshipPermissionHeaders.add(CAN_ADD);
  relationshipPermissionHeaders.add(CAN_REMOVE);
  
  relationshipPermissionExcelMap.put(RELATIONSHIP_PERMISSIONS_SHEET_NAME, new HashMap<>());
  Map<String, Object> relationshipPermissionMap = (Map<String, Object>) relationshipPermissionExcelMap
      .get(RELATIONSHIP_PERMISSIONS_SHEET_NAME);
  relationshipPermissionMap.put(HEADERS, relationshipPermissionHeaders);
  relationshipPermissionMap.put(DATA, new ArrayList<>());
  
}

}
