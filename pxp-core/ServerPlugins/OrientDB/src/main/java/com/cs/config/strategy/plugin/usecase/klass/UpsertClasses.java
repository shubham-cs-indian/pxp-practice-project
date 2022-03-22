package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableBoolean;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IConfigClass;
import com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy.AbstractSaveKlassAndTaxonomy;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.relationship.ImportRelationshipPXONParser;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IModifiedNatureRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/**
 * @author tauseef
 */
public class UpsertClasses extends AbstractSaveKlassAndTaxonomy {
  
  private List<String>              fieldsToExclude              = Arrays.asList(REFERENCED_CLASS_IDS, CommonConstants.VALIDATOR_PROPERTY,
      REFERENCED_ATTRIBUTE_IDS, IKlass.PERMISSIONS, DELETED_STRUCTURES, IKlass.NOTIFICATION_SETTINGS, STRUCTURE_CHILDREN, IKlass.CHILDREN,
      KLASS_VIEW_SETTING, MODIFIED_STRUCTURES, ADDED_STRUCTURES, IKlass.IS_ENFORCED_TAXONOMY, CommonConstants.PARENT_PROPERTY, PARENT_CODE,
      STATUS_TAGS, TASKS, ADDED_TASKS, EMBEDDED_CLASSES, COUPLINGS, DELETED_TASKS, ADDED_RELATIONSHIPS, VERSION_CONTEXT_CODE,
      ADDED_CONTEXTS, VERSION_CONTEXTS, PRODUCT_VARIANT_CONTEXT_CODE, CommonConstants.SECTIONS_PROPERTY,
      CommonConstants.TREE_TYPE_OPTION_PROPERTY, LEVEL_CODE, MASTER_PARENT_TAG, LEVEL_LABELS, ConfigTag.subType.name(),
      ConfigTag.relationships.toString(), ConfigTag.isNewlyCreatedLevel.name(), ConfigTag.levelIndex.name(),
      ConfigTag.promotionalVersionContextCode.toString());
  
  private List<String>              saveFieldsToExclude          = Arrays.asList(ConfigTag.isStandard.toString(),
      ConfigTag.levelCode.toString(), ConfigTag.levelLabels.toString(), ConfigTag.subType.toString(), ConfigTag.masterParentTag.toString(),
      ConfigTag.linkedMasterTagId.toString(), ConfigTag.sections.toString(), ConfigTag.extensionConfiguration.toString(),
      ConfigTag.taxonomyType.toString(), ConfigTag.tasks.toString(),
      ConfigTag.statusTags.toString(), ConfigTag.relationships.toString(), ConfigTag.promotionalVersionContextCode.toString(),
      ConfigTag.productVariantContextCode.toString(), ConfigTag.isNature.toString(), ConfigTag.isDefaultChild.toString(),
      ConfigTag.isAbstract.toString(), ConfigTag.events.toString(), ConfigTag.embeddedClasses.toString(), ConfigTag.contextCode.toString(),
      ConfigTag.parentCode.toString(), ConfigTag.natureType.toString(), ConfigTag.isNature.toString(), ConfigTag.type.toString(),
      ConfigTag.classifierIID.toString(), ConfigTag.isNewlyCreatedLevel.name(), ConfigTag.levelIndex.name());
  
  private IExceptionModel           failure;
  
  private final String              productVariantRelationship    = CommonConstants.PRODUCT_VARIANT_RELATIONSHIP;

  public UpsertClasses(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    failure = new ExceptionModel();
    List<Map<String, Object>> klassList = (List<Map<String, Object>>) requestMap.get(CommonConstants.LIST_PROPERTY);
    List<Map<String, Object>> createdKlassList = new ArrayList<>();
    List<Map<String, Object>> failedKlassList = new ArrayList<>();
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    
    for (Map<String, Object> klass : klassList) {
      try {
        Map<String, Object> klassMap = upsertClass(klass);
        Map<String, Object> createdKlass = new HashMap<>();
        createdKlass.put(CommonConstants.CODE_PROPERTY, klassMap.get(CommonConstants.CODE_PROPERTY));
        createdKlass.put(ISummaryInformationModel.LABEL, klassMap.get(IAttributeModel.LABEL));
        createdKlassList.add(createdKlass);
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) klass.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klass);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) klass.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klass);
      }
    }

    for (Map<String, Object> klass : klassList) {
      try {
        String type = (String) klass.get(CommonConstants.TYPE_PROPERTY);
        String vertexLabel = getVertextType(type);
        String code = (String) klass.get(CommonConstants.CODE_PROPERTY);
        Vertex klassNode = UtilClass.getVertexByCode(code, vertexLabel);
        handleEmbeddedClasses(klass, klassNode, failure);
        UtilClass.getGraph().commit();
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) klass.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klass);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) klass.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klass);
      }
    }

    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdKlassList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedKlassList);
    return result;
  }
  
  private void addEmbeddedClasses(List<Map<String, Object>> failedKlassList, IExceptionModel failure,
      List<Map<String, Object>> embeddedClassesList)
  {
    for (Map<String, Object> klassMap : embeddedClassesList) {
      String type = (String) klassMap.get(CommonConstants.TYPE_PROPERTY);
      String vertexLabel = getVertextType(type);
      String code = (String) klassMap.get(CommonConstants.CODE_PROPERTY);
      try {
        Vertex klassNode = UtilClass.getVertexByCode(code, vertexLabel);
        handleEmbeddedClasses(klassMap, klassNode, failure);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) klassMap.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klassMap);
      }
    }
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedKlassList, Map<String, Object> klass)
  {
    Map<String, Object> failedKlassMap = new HashMap<>();
    failedKlassMap.put(ISummaryInformationModel.ID, klass.get(IKlassModel.ID));
    failedKlassMap.put(ISummaryInformationModel.LABEL, klass.get(IKlassModel.LABEL));
    failedKlassList.add(failedKlassMap);
  }

  private Map<String, Object> upsertClass(Map<String, Object> klassMap) throws Exception
  {
    String type = (String) klassMap.get(CommonConstants.TYPE_PROPERTY);
    String vertexLabel = getVertextType(type);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(vertexLabel, CommonConstants.CODE_PROPERTY);
    Vertex klassNode;

    UtilClass.validateIconExistsForImport(klassMap);
    String previewImage = (String) klassMap.get(IKlassModel.PREVIEW_IMAGE);
    previewImage = StringUtils.isEmpty(previewImage) ? "" : previewImage;
    klassMap.put(IKlassModel.PREVIEW_IMAGE, previewImage);
    Integer maxVersion = (Integer) klassMap.get(ConfigTag.numberOfVersionsToMaintain.name());
    klassMap.put(IKlassModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, Math.abs(maxVersion));

    try {
      String code = (String) klassMap.get(CommonConstants.CODE_PROPERTY);
      klassNode = UtilClass.getVertexByCode(code, vertexLabel);
      UtilClass.saveNode(klassMap, klassNode, saveFieldsToExclude);
      checkForInvalidParentCode(klassMap, vertexLabel);
    }
    catch (NotFoundException e) {
      checkForInvalidParentCode(klassMap, vertexLabel);
      if (vertexLabel.equals(VertexLabelConstants.ENTITY_TYPE_SUPPLIER))
        throw new Exception("Can not create Supplier class");
      String parentCode = (String) klassMap.get(PARENT_CODE);
      if(StringUtils.isNotEmpty(parentCode) && !parentCode.equals("-1")) {
        // if parent not found then it will throw Not found exception and hence child klass is not getting created
        UtilClass.getVertexByCode(parentCode, vertexLabel);
      }
      UtilClass.validateIconExistsForImport(klassMap);
      // if user pass negative number in version
      Integer maxversion = (Integer) klassMap.get(ConfigTag.numberOfVersionsToMaintain.name());
      klassMap.put(IKlassModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, Math.abs(maxversion));
      
      String natureType = (String) klassMap.get(ConfigTag.natureType.toString());
      if(StringUtils.isNotEmpty(natureType)) {
        klassMap.put(ConfigTag.isNature.toString(), true);
      }

      klassNode = UtilClass.createNode(klassMap, vertexType, fieldsToExclude);
      prepareParentMap(klassMap);
      CreateKlassUtils.inheritTreeTypeOption(klassMap, klassNode, vertexLabel);
      
      if (CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE.equals(natureType)) {
        CreateKlassUtils.createDefaultContextNodeForTechnicalImageType(klassNode, klassMap);
      }
      UtilClass.getGraph().commit();
    }
    checkForInvalidParentCode(klassMap, vertexLabel);

    // Process parent
    Boolean isParentChanged = processParent(klassMap, klassNode);

    // Validator property creation
    processValidator(klassMap, klassNode);

    // Status Tag
    manageStatusTags(klassMap, klassNode);

    // Task
    manageTasks(klassMap, klassNode, failure);

    // Nature Relationship Processing
    processRelationships(klassMap, klassNode, isParentChanged);

    //default value of section element handle
    processSections(klassMap, klassNode, vertexLabel, failure);

    // only inherited section need to create
    klassMap.put(CommonConstants.SECTIONS_PROPERTY, new ArrayList<>());
    processContextClasses(klassMap, klassNode);
    
    UtilClass.getGraph().commit();

    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(CommonConstants.CODE_PROPERTY, klassMap.get(CommonConstants.CODE_PROPERTY));
    returnMap.put(CommonConstants.LABEL_PROPERTY, klassMap.get(CommonConstants.LABEL_PROPERTY));
    return returnMap;
  }

  private void checkForInvalidParentCode(Map<String, Object> klassMap, String vertexLabel)
      throws Exception
  {
    String parentCode = (String) klassMap.get(PARENT_CODE);
    if (StringUtils.isNotEmpty(parentCode) && !parentCode.equals("-1")) {
      // if parent not found then it will throw Not found exception and hence child klass is not getting created
      UtilClass.getVertexByCode(parentCode, vertexLabel);
    }
  }
  
  private void processRelationships(Map<String, Object> klassMap, Vertex klassNode, Boolean isParentChanged) throws Exception
  {
    List<String> productVariantContexts = (List<String>) klassMap.get(PRODUCT_VARIANT_CONTEXT_CODE);
    List<String> validProductVC = validateProductVariantContexts(klassNode, productVariantContexts);

    // Nature Relationships
    Map<String, Object> existingRelationships = KlassUtils.getNatureRelationships(klassNode);
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) klassMap.remove(CommonConstants.RELATIONSHIPS);
    if (relationships == null) {
      relationships = new ArrayList<>();
    }
    Map<String, Object> relationsToImport = relationships.stream()
        .collect(Collectors.toMap(x -> (String)x.get(CommonConstants.CODE_PROPERTY), y -> y));

    List<Map<String, Object>> addedRelationship = new ArrayList<>();
    List<String> deletedRelationship = new ArrayList<>(SetUtils.difference(existingRelationships.keySet(), relationsToImport.keySet()));
    List<Map<String, Object>> modifiedRelationship = new ArrayList<>();

    List<String> relationshipTypes = new ArrayList<>();
    String klassCode = klassNode.getProperty(IKlass.CODE);

    for (Map<String, Object> relationshipMap : relationships) {
      try {
        Map<String, Object> side1Entity = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
        side1Entity.put(ConfigTag.classCode.toString(), klassCode);
        Vertex rNode = ImportRelationshipPXONParser.prepareADMForImport(relationshipMap, true, deletedRelationship);
        if (rNode == null) {
            addedRelationship.add(relationshipMap);
        }else {
            modifiedRelationship.add(relationshipMap);
        }
        relationshipTypes.add((String) relationshipMap.get(ConfigTag.relationshipType.toString()));
      }
      catch (NotFoundException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, klassCode);
      }
    }

    if(!validProductVC.isEmpty()) {
      addRelationshipForVariant(addedRelationship, relationshipTypes, klassCode, productVariantRelationship, deletedRelationship);
    }else {
      removeRelationship(addedRelationship, relationshipTypes, productVariantRelationship);
    }
    klassMap.put(IKlassSaveModel.ADDED_RELATIONSHIPS, addedRelationship);
    klassMap.put(IKlassSaveModel.DELETED_RELATIONSHIPS, deletedRelationship);
    klassMap.put(IKlassSaveModel.MODIFIED_RELATIONSHIPS, modifiedRelationship);

    try {
      SaveKlassUtil.manageKlassNatureInKlass(klassNode, klassMap, validProductVC, new ArrayList<>(), new HashMap<>(), new HashMap<>(),
          new MutableBoolean());
    }
    catch (Exception e) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, klassCode);
    }

    removeRelationshipUnknownProperty(addedRelationship);
    klassNode.setProperty(IKlass.RELATIONSHIPS, addedRelationship);
  }


  private List<String> validateProductVariantContexts(Vertex klassNode, List<String> productVariantContexts)
  {
    List<String> validCodes = new ArrayList<>();
    if(productVariantContexts == null) {
      productVariantContexts = new ArrayList<>();
    }
    for (String code : productVariantContexts) {
      try {
        Vertex context = UtilClass.getVertexByCode(code, VertexLabelConstants.VARIANT_CONTEXT);
        String property = context.getProperty(CommonConstants.TYPE);
        if(property.equals(CommonConstants.PRODUCT_VARIANT)){
          validCodes.add(code);
        }
        else{
          throw new InvalidTypeException();
        }
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) klassNode.getProperty(IKlass.CODE));
      }
    }
    return validCodes;
  }

  private void removeRelationshipUnknownProperty(List<Map<String, Object>> addedRelationship)
  {
    for(Map<String, Object> relationship: addedRelationship) {
      relationship.remove(CommonConstants.TAB);
      relationship.remove(ISaveRelationshipModel.ADDED_ATTRIBUTES);
      relationship.remove(ISaveRelationshipModel.ADDED_TAGS);
      relationship.remove(ISaveRelationshipModel.MODIFIED_ATTRIBUTES);
      relationship.remove(ISaveRelationshipModel.MODIFIED_TAGS);
      relationship.remove(ISaveRelationshipModel.DELETED_ATTRIBUTES);
      relationship.remove(ISaveRelationshipModel.DELETED_TAGS);
      relationship.remove(IModifiedNatureRelationshipModel.ADDED_RELATIONSHIP_INHERITANCE);
      relationship.remove(IModifiedNatureRelationshipModel.ADDED_RELATIONSHIP_INHERITANCE);
      relationship.remove(ISaveRelationshipModel.DELETED_TAGS);
      relationship.put(IRelationship.TAB_ID, null);
    }
  }

  private void removeRelationship(List<Map<String, Object>> addedRelationship, List<String> relationshipTypes,
      String versionContextRelationship)
  {
    if(relationshipTypes.contains(versionContextRelationship)) {
      int index = relationshipTypes.indexOf(versionContextRelationship);
      relationshipTypes.remove(index);
      addedRelationship.remove(index);
    }
  }

  private void addRelationshipForVariant(List<Map<String, Object>> addedRelationship, List<String> realtionshipTypes, String klassCode,
      String relationshipType, List<String> deletedRelationship) throws Exception
  {
    if (!realtionshipTypes.contains(relationshipType)) {
      Map<String, Object> relationship = prepareRelationshipMap(klassCode, relationshipType, deletedRelationship);
      addedRelationship.add(relationship);
      realtionshipTypes.add(relationshipType);
    }
  }

  private Map<String, Object> prepareRelationshipMap(String klassCode, String versionContextRelationship, List<String> deletedRelationships) throws Exception
  {
    Map<String, Object> relationship = new HashMap<>();
    relationship.put(IRelationship.IS_NATURE, true);
    relationship.put(ConfigTag.relationshipType.toString(), versionContextRelationship);
    Map<String, Object> sideMap = new HashMap<>();
    sideMap.put(ConfigTag.classCode.toString(), klassCode);
    sideMap.put(IRelationshipSide.LABEL, klassCode);
    sideMap.put(ConfigTag.couplings.toString(), new ArrayList<>());
    relationship.put(IRelationship.SIDE1, sideMap);
    relationship.put(IRelationship.SIDE2, sideMap);
    ImportRelationshipPXONParser.prepareADMForImport(relationship, true, deletedRelationships);
    return relationship;
  }

  private List<String> setOnlyOneContext(List<String> validVersionContextCodes)
  {
    if(validVersionContextCodes.size()>1) {
      String versionContextCode = validVersionContextCodes.get(0);
      validVersionContextCodes = new ArrayList<>();
      // at time only one will be allowed
      validVersionContextCodes.add(versionContextCode);
    }
    return validVersionContextCodes;
  }
  
  private List<String> getValidCodes(Vertex klassNode, List<String> codelist, String entityLabel) throws Exception
  {
    List<String> validCodes = new ArrayList<>();
    if(codelist == null) {
      codelist = new ArrayList<>();
    }
    for (String code : codelist) {
      try {
        UtilClass.getVertexByCode(code, entityLabel);
        validCodes.add(code);
        return validCodes;
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) klassNode.getProperty(IKlass.CODE));
      }
    }
    return validCodes;
  }
  
  private void manageStatusTags(Map<String, Object> klassMap, Vertex klassNode) throws Exception
  {
    List<String> statusTagIds = (List<String>) klassMap.get(STATUS_TAGS);

    Iterable<Edge> klassLifeCycleTagLinks = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK);

    for(Edge klassLifeCycleTagLink : klassLifeCycleTagLinks) {
      Vertex vertex = klassLifeCycleTagLink.getVertex(Direction.IN);
      String code = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      if (!statusTagIds.contains(code)) {
        klassLifeCycleTagLink.remove();
      }
      else {
        statusTagIds.remove(code);
      }
    }
    for (String id : statusTagIds) {
      Vertex tagToLink = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
      klassNode.addEdge(RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK, tagToLink);
    }
  }

  private Boolean processParent(Map<String, Object> klassMap, Vertex klassNode) throws Exception
  {
    Boolean isParentChanged = false;
    String parentCode = prepareParentMap(klassMap);
    Map<String, Edge> parentEdges = TaxonomyUtil.getParentEdges(klassNode);

    if (!parentEdges.containsKey(parentCode)) {
      KlassUtils.removeParentChildLink(klassNode, parentEdges);
      KlassUtils.createParentChildLink(klassNode, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, klassMap, true);
      isParentChanged = true;
    }
    else {

    }
    return isParentChanged;
  }

  private String prepareParentMap(Map<String, Object> klassMap)
  {
    String parentCode = (String) klassMap.get(PARENT_CODE);
    Map<String, Object> parentInfo = new HashMap<>();
    parentInfo.put(CommonConstants.ID_PROPERTY, parentCode);
    klassMap.put(CommonConstants.PARENT_PROPERTY, parentInfo);
    return parentCode;
  }
  
  private void processValidator(Map<String, Object> klassMap, Vertex klassNode) throws Exception
  {
    HashMap<String, Object> validatorMap = (HashMap<String, Object>) klassMap.get(CommonConstants.VALIDATOR_PROPERTY);
    
    if (validatorMap == null) {
      validatorMap = new HashMap<String, Object>();
      klassMap.put(CommonConstants.VALIDATOR_PROPERTY, validatorMap);
    }
    
    OrientVertexType vType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_KLASS_VALIDATOR,
        CommonConstants.CODE_PROPERTY);
    Vertex validatorNode = UtilClass.createNode(validatorMap, vType, new ArrayList<>());
    validatorNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF, klassNode);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UpsertClasses/*" };
  }
  
  private String getVertextType(String type)
  {
    switch (IConfigClass.ClassifierClass.valueOfClassPath(type)) {
      case PROJECT_KLASS_TYPE:
      case PROJECT_SET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      
      case ASSET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      
      case MARKET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      
      case TEXT_ASSET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
      
      case SUPPLIER_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
    }
    return null;
  }
  
  private void processContextClasses(Map<String, Object> klassMap, Vertex klassNode) throws Exception
  {
    Vertex contextNode = VariantContextUtils.getContextNodeFromKlassNode(klassNode);
    if(contextNode == null){
      VariantContextUtils.manageContextKlasses(klassMap, klassNode);
    }
  }

  public static List<Edge> getKlassRelationshipEdges(Vertex entityNode)
  {
    Iterable<Edge> hasKlassProperties = entityNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    List<Edge> klassRelationshipEdges = new ArrayList<>();
    for (Edge hasKlassProperty : hasKlassProperties) {
      Vertex propertyNode = hasKlassProperty.getVertex(Direction.OUT);
      if (propertyNode.getProperty(ISectionElement.TYPE).equals(CommonConstants.RELATIONSHIP)) {
        klassRelationshipEdges.add(hasKlassProperty);
      }
    }
    return klassRelationshipEdges;
  }
}