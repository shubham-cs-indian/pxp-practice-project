package com.cs.config.strategy.plugin.usecase.vertextypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.sso.ISSOConfiguration;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.entity.template.IHeaderPermission;
import com.cs.core.config.interactor.entity.template.IPropertyCollectionPermission;
import com.cs.core.config.interactor.entity.template.IPropertyPermission;
import com.cs.core.config.interactor.entity.template.IRelationshipPermission;
import com.cs.core.config.interactor.entity.template.ITemplatePermission;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentEmbedded;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateVertexTypes extends AbstractOrientPlugin {
  
  public CreateVertexTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    ODatabaseDocumentEmbedded database = UtilClass.getDatabase();
    OrientGraph graph = UtilClass.getGraph();
    try {
      createTypes(database, graph);
    }
    catch (Exception e) {
      e.printStackTrace();
      //RDBMSLogger.instance().exception(e);
      throw e;
    }
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("success", "vertex types created");
    return returnMap;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void createTypes(ODatabaseDocumentEmbedded database, OrientGraph graph)
  {
    database.commit();
    // create all vertex types
    // String[] fieldsToIndex = {CommonConstants.CODE_PROPERTY};
    
    OrientVertexType rootKlassVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(rootKlassVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    UtilClass.createProperty(rootKlassVertexType, IKlass.CLASSIFIER_IID);

    
    // UtilClass.getOrCreateVertexType( EntityConstants.ENTITY_TYPE_KLASS,
    // CommonConstants.CODE_PROPERTY);
      
    OrientVertexType propertyVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_PROPERTY, new String[] {});
    
    OrientVertexType attributeVertexType = graph
        .getVertexType(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    if (attributeVertexType == null) {
      attributeVertexType = graph.createVertexType(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", attributeVertexType));
      
      UtilClass.createVertexPropertyAndApplyFulLTextIndex(attributeVertexType,
          new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
      
      attributeVertexType.createProperty(IAttribute.IS_FILTERABLE, OType.BOOLEAN);
      attributeVertexType.createProperty(IAttribute.IS_SORTABLE, OType.BOOLEAN);
      attributeVertexType.createProperty(IAttribute.IS_TRANSLATABLE, OType.BOOLEAN);
      attributeVertexType.createProperty(IAttribute.IS_SEARCHABLE, OType.BOOLEAN);
      attributeVertexType.createProperty(IAttribute.PROPERTY_IID, OType.LONG);
      
      attributeVertexType.createIndex(
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + "." + IAttribute.IS_FILTERABLE,
          OClass.INDEX_TYPE.NOTUNIQUE, IAttribute.IS_FILTERABLE);
      attributeVertexType.createIndex(
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + "." + IAttribute.IS_TRANSLATABLE,
          OClass.INDEX_TYPE.NOTUNIQUE, IAttribute.IS_TRANSLATABLE);
      attributeVertexType.createIndex(
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + "." + IAttribute.IS_SORTABLE,
          OClass.INDEX_TYPE.NOTUNIQUE, IAttribute.IS_SORTABLE);
      attributeVertexType.createIndex(
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + "." + IAttribute.IS_SEARCHABLE,
          OClass.INDEX_TYPE.NOTUNIQUE, IAttribute.IS_SEARCHABLE);
     
      
      List<String> attributeVertexSuperKlasses = attributeVertexType.getSuperClassesNames();
      if (!attributeVertexSuperKlasses.contains(propertyVertexType.getName())) {
        attributeVertexType.addSuperClass(propertyVertexType);
      }
    }
        
    OrientVertexType tagVertexType = graph.getVertexType(VertexLabelConstants.ENTITY_TAG);

    if (tagVertexType == null) {
      tagVertexType = graph.createVertexType(VertexLabelConstants.ENTITY_TAG);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", tagVertexType));
      
      UtilClass.createVertexPropertyAndApplyFulLTextIndex(tagVertexType,
          new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
      
      tagVertexType.createProperty(ITag.IS_FILTERABLE, OType.BOOLEAN);
      tagVertexType.createProperty(ITag.IS_ROOT, OType.BOOLEAN);
      tagVertexType.createProperty(ITag.PROPERTY_IID, OType.LONG);
      
      tagVertexType.createIndex(VertexLabelConstants.ENTITY_TAG + "." + ITag.IS_FILTERABLE,
          OClass.INDEX_TYPE.NOTUNIQUE, ITag.IS_FILTERABLE);
      tagVertexType.createIndex(VertexLabelConstants.ENTITY_TAG + "." + ITag.IS_ROOT,
          OClass.INDEX_TYPE.NOTUNIQUE, ITag.IS_ROOT);
      
      List<String> tagVertexSuperKlasses = tagVertexType.getSuperClassesNames();
      if (!tagVertexSuperKlasses.contains(propertyVertexType.getName())) {
        tagVertexType.addSuperClass(propertyVertexType);
      }
    }
    
    OrientVertexType roleVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ROLE, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(roleVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    // UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_ROLE,
    // CommonConstants.CODE_PROPERTY);
    OrientVertexType userVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_USER, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(userVertexType, new String[] {
        IUser.LAST_NAME, IUser.FIRST_NAME, IUser.USER_NAME, IUser.CODE, IUser.EMAIL });
    UtilClass.createProperty(userVertexType, IUser.USER_IID);   
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_MANDATORY,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_MANDATORY_ROLE,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TAXONOMY_SETTING,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_KLASS_VALIDATOR,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_KLASS_SECTION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_SECTION_ELEMENT_POSITION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_SECTION_ELEMENT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TAG_TYPE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TAG_VALUE,
        CommonConstants.CODE_PROPERTY);
    // UtilClass.getOrCreateVertexType( EntityConstants.ENTITY_TYPE_ASSET,
    // CommonConstants.CODE_PROPERTY);
    OrientVertexType taskVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_TASK, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(taskVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    // UtilClass.getOrCreateVertexType( EntityConstants.ENTITY_TYPE_TARGET,
    // CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_STANDARD,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_SECTION_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    
    OrientVertexType dataRuleVertexType = UtilClass
        .getOrCreateVertexType(CommonConstants.DATA_RULES, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(dataRuleVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    UtilClass.getOrCreateVertexType(CommonConstants.RULE_LIST, CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(CommonConstants.CONDITION, CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(CommonConstants.STATE, CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(CommonConstants.CAUSE_EFFECT_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(CommonConstants.RETAILER, CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TREE_TYPE,
        CommonConstants.CODE_PROPERTY);
    
    OrientVertexType propertyCollectionVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROPERTY_COLLECTION, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(propertyCollectionVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.RELATIONSHIP_RULE_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TAG_RULE_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ATTRIBUTE_RULE_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ROLE_RULE_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.KLASS_RULE_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.RULE_VIOLATION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.RULE_NODE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.NORMALIZATION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CALCULATED_ATTRIBUTE_OPERATOR,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CONCATENATED_NODE_ATTRIBUTE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CONCATENATED_NODE,
        CommonConstants.CODE_PROPERTY);
    OrientVertexType variantContextVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.VARIANT_CONTEXT, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(variantContextVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    OrientVertexType permissionVertexType = graph
        .getVertexType(VertexLabelConstants.KLASS_TAXONOMY_GLOBAL_PERMISSIONS);
    
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.KLASS_TAXONOMY_GLOBAL_PERMISSIONS);
      
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      
      permissionVertexType.createProperty(IGlobalPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IGlobalPermission.ENTITY_ID, OType.STRING);
      
      permissionVertexType.createIndex(Constants.GLOBAL_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IGlobalPermission.ENTITY_ID, IGlobalPermission.ROLE_ID);
    }
    UtilClass.getOrCreateVertexType(VertexLabelConstants.PROPERTY_COLLECTION_GLOBAL_PERMISSIONS,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.PROPERTY_ENTITY_GLOBAL_PERMISSIONS,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ATTRIBUTE_TAG,
        CommonConstants.CODE_PROPERTY);
    OrientVertexType rootRelationshipVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ROOT_RELATIONSHIP, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(rootRelationshipVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.COLLECTION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TEMPLATE_HEADER,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.UNIQUE_SELECTOR,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULES,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULE_ATTR_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULE_TAG_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULE_ROLE_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULE_REL_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    OrientVertexType kpiVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE_KPI, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(kpiVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULE_TASK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULE_BLOCK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.KPI_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.DRILL_DOWN,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.DRILL_DOWN_LEVEL,
        CommonConstants.CODE_PROPERTY);
    
    OrientVertexType tabVertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.TAB,
        CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(tabVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TAB_SEQUENCE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GOLDEN_RECORD_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.MERGE_EFFECT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.EFFECT_TYPE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CONFIG_DETAIL_CACHE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.INDESIGN_SERVER_INSTANCE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.INDESIGN_LOAD_BALANCER_INSTANCE,
        CommonConstants.CODE_PROPERTY);
    
    
    createSmartDocumentVertexTypes();
    
    OrientVertexType dashboardType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.DASHBOARD_TAB, new String[] { CommonConstants.CODE_PROPERTY });
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(dashboardType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    OrientVertexType vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_TYPE_KLASS);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_KLASS,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(rootKlassVertexType);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_TYPE_ASSET);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_ASSET,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(rootKlassVertexType);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_TYPE_TARGET);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_TARGET,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(rootKlassVertexType);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(rootKlassVertexType);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_SUPPLIER,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(rootKlassVertexType);
    }
    
    vertexType = null;
    vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE);
    if (vertexType == null) {
      vertexType = graph.createVertexType(VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE,
          VertexLabelConstants.ENTITY_STANDARD);
      OClass attributeclass = database.getMetadata()
          .getSchema()
          .getClass(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      
      vertexType.addSuperClass(attributeclass);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", vertexType));
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_STANDARD_ROLE);
    if (vertexType == null) {
      vertexType = graph.createVertexType(VertexLabelConstants.ENTITY_STANDARD_ROLE,
          VertexLabelConstants.ENTITY_STANDARD);
      OClass roleclass = database.getMetadata()
          .getSchema()
          .getClass(VertexLabelConstants.ENTITY_TYPE_ROLE);
      
      vertexType.addSuperClass(roleclass);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", vertexType));
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_STANDARD_USER);
    if (vertexType == null) {
      OrientVertexType entityTypeUser = UtilClass
          .getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_USER);
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_STANDARD_USER,
          CommonConstants.CODE_PROPERTY);
      //vertexType.createProperty(IUser.USER_IID, OType.LONG);
      vertexType.addSuperClass(entityTypeUser);
    }
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_KLASS_PROPERTY);
    OrientVertexType entityKlassProperty = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.ENTITY_KLASS_PROPERTY);
    
    vertexType = graph.getVertexType(VertexLabelConstants.KLASS_ATTRIBUTE);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.KLASS_ATTRIBUTE,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(entityKlassProperty);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.KLASS_TAG);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.KLASS_TAG,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(entityKlassProperty);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.KLASS_ROLE);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.KLASS_ROLE,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(entityKlassProperty);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.KLASS_RELATIONSHIP);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.KLASS_RELATIONSHIP,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(entityKlassProperty);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.KLASS_TAXONOMY_ENTITY);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.KLASS_TAXONOMY_ENTITY,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(entityKlassProperty);
    }
    OrientVertexType rootRelationship = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.ROOT_RELATIONSHIP);
    UtilClass.createProperty(rootRelationship, CommonConstants.PROPERTY_IID);
    
    vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(rootRelationship);
    }
    
    vertexType = graph.getVertexType(VertexLabelConstants.NATURE_RELATIONSHIP);
    if (vertexType == null) {
      vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.NATURE_RELATIONSHIP,
          CommonConstants.CODE_PROPERTY);
      vertexType.addSuperClass(rootRelationship);
    }
    
    OrientVertexType rootTaxonomyVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ROOT_KLASS_TAXONOMY, CommonConstants.CODE_PROPERTY);
    // UtilClass.createVertexProperty(vertexType);
    UtilClass.getOrCreateVertexProperty(rootTaxonomyVertexType, ITaxonomy.IS_ROOT, OType.BOOLEAN);
    UtilClass.getOrCreateVertexProperty(rootTaxonomyVertexType, ITaxonomy.TAXONOMY_TYPE, OType.STRING);
    UtilClass.createOrRebuildIndex(rootTaxonomyVertexType,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY + ".isRootMajorTaxonomy",
        OClass.INDEX_TYPE.NOTUNIQUE, ITaxonomy.IS_ROOT, ITaxonomy.TAXONOMY_TYPE);
    
    // UtilClass.createVertexProperty(vertexType);
    vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.DEFAULT_FILTER,
        CommonConstants.CODE_PROPERTY);
    vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.FILTERABLE_TAG,
        CommonConstants.CODE_PROPERTY);
    vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.SORTABLE_ATTRIBUTE,
        CommonConstants.CODE_PROPERTY);
    
    vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.SUPPLIER_KLASS_MAPPING,
        CommonConstants.CODE_PROPERTY);
    // vertexType =
    // UtilClass.getOrCreateVertexType(VertexLabelConstants.COLUMN_MAPPING,
    // CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TEMPLATE, CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TEMPLATE_TAB);
    
    permissionVertexType = graph
        .getVertexType(VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      
      permissionVertexType.createProperty(IPropertyCollectionPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyCollectionPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyCollectionPermission.PROPERTY_COLLECTION_ID,
          OType.STRING);
      
      permissionVertexType.createIndex(Constants.PROPERTY_COLLECTION_CAN_READ_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IPropertyCollectionPermission.ROLE_ID,
          IPropertyCollectionPermission.ENTITY_ID,
          IPropertyCollectionPermission.PROPERTY_COLLECTION_ID);
    }
    
    permissionVertexType = graph
        .getVertexType(VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      
      permissionVertexType.createProperty(IRelationshipPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IRelationshipPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createProperty(IRelationshipPermission.RELATIONSHIP_ID, OType.STRING);
      
      permissionVertexType.createIndex(Constants.RELATIONSHIP_CAN_READ_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IRelationshipPermission.ROLE_ID,
          IRelationshipPermission.ENTITY_ID, IRelationshipPermission.RELATIONSHIP_ID);
    }
    
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      
      permissionVertexType.createProperty(IPropertyPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyPermission.PROPERTY_ID, OType.STRING);
      
      permissionVertexType.createIndex(Constants.PROPERTY_CAN_READ_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IPropertyPermission.ROLE_ID, IPropertyPermission.ENTITY_ID,
          IPropertyPermission.PROPERTY_ID);
    }
    
    permissionVertexType = graph
        .getVertexType(VertexLabelConstants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IPropertyCollectionPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyCollectionPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyCollectionPermission.PROPERTY_COLLECTION_ID,
          OType.STRING);
      
      permissionVertexType.createIndex(Constants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IPropertyCollectionPermission.ROLE_ID,
          IPropertyCollectionPermission.ENTITY_ID,
          IPropertyCollectionPermission.PROPERTY_COLLECTION_ID);
    }
    
    permissionVertexType = graph
        .getVertexType(VertexLabelConstants.RELATIONSHIP_CAN_ADD_PERMISSION);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.RELATIONSHIP_CAN_ADD_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IRelationshipPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IRelationshipPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createProperty(IRelationshipPermission.RELATIONSHIP_ID, OType.STRING);
      
      permissionVertexType.createIndex(Constants.RELATIONSHIP_CAN_ADD_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IRelationshipPermission.ROLE_ID,
          IRelationshipPermission.ENTITY_ID, IRelationshipPermission.RELATIONSHIP_ID);
    }
    
    permissionVertexType = graph
        .getVertexType(VertexLabelConstants.RELATIONSHIP_CAN_DELETE_PERMISSION);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.RELATIONSHIP_CAN_DELETE_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IRelationshipPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IRelationshipPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createProperty(IRelationshipPermission.RELATIONSHIP_ID, OType.STRING);
      
      permissionVertexType.createIndex(Constants.RELATIONSHIP_CAN_DELETE_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IRelationshipPermission.ROLE_ID,
          IRelationshipPermission.ENTITY_ID, IRelationshipPermission.RELATIONSHIP_ID);
    }
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION);
    if(permissionVertexType == null) {
      permissionVertexType = graph.createVertexType(VertexLabelConstants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IRelationshipPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IRelationshipPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createProperty(IRelationshipPermission.RELATIONSHIP_ID, OType.STRING);
      
      permissionVertexType.createIndex(Constants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IRelationshipPermission.ROLE_ID,
          IRelationshipPermission.ENTITY_ID, IRelationshipPermission.RELATIONSHIP_ID);
    }
    
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IPropertyPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyPermission.PROPERTY_ID, OType.STRING);
      
      permissionVertexType.createIndex(Constants.PROPERTY_CAN_EDIT_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IPropertyPermission.ROLE_ID, IPropertyPermission.ENTITY_ID,
          IPropertyPermission.PROPERTY_ID);
    }
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.HEADER_PERMISSION);
    if (permissionVertexType == null) {
      permissionVertexType = graph.createVertexType(VertexLabelConstants.HEADER_PERMISSION);
      permissionVertexType.createProperty(IPropertyPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IPropertyPermission.ENTITY_ID, OType.STRING);
      
      permissionVertexType.createIndex(Constants.HEADER_PERMISSION_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IHeaderPermission.ENTITY_ID, IHeaderPermission.ROLE_ID);
    }
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TAB_PERMISSION);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.VARIANT_CONTEXT_TAG,
        CommonConstants.CODE_PROPERTY);
    
    // mutiple inheritance
    vertexType = graph.getVertexType(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    if (vertexType == null) {
      vertexType = graph.createVertexType(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
      vertexType.addSuperClass(tagVertexType);
      vertexType.addSuperClass(rootTaxonomyVertexType);
      OrientVertexType klassVertexType = graph
          .getVertexType(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      vertexType.addSuperClass(klassVertexType);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", vertexType));
      UtilClass.createVertexPropertyAndApplyFulLTextIndex(vertexType,
          new String[] { CommonConstants.LABEL_PROPERTY });
    }
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL);
    
    vertexType = graph.getVertexType(VertexLabelConstants.LANGUAGE);
    if (vertexType == null) {
      vertexType = graph.createVertexType(VertexLabelConstants.LANGUAGE);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", vertexType));
      UtilClass.createVertexPropertyAndApplyFulLTextIndex(vertexType,
          new String[] { CommonConstants.LABEL_PROPERTY });
    }
    UtilClass.getOrCreateVertexType(VertexLabelConstants.LANGUAGE_TAXONOMY_LEVEL,
        CommonConstants.CODE_PROPERTY);
    
    OrientVertexType organizationVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.ORGANIZATION, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(organizationVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY });
    
    vertexType = graph.getVertexType(VertexLabelConstants.TEMPLATE_PERMISSION);
    if (vertexType == null) {
      vertexType = graph.createVertexType(VertexLabelConstants.TEMPLATE_PERMISSION);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", vertexType));
      vertexType.createProperty(ITemplatePermission.ENTITY_ID, OType.STRING);
      vertexType.createProperty(CommonConstants.TEMPLATE_ID_PROPERY, OType.STRING);
      vertexType.createProperty(CommonConstants.ROLE_ID_PROPERY, OType.STRING);
      vertexType.createIndex(Constants.TEMPLATE_PERMISSION_INDEX, OClass.INDEX_TYPE.NOTUNIQUE,
          ITemplatePermission.ENTITY_ID, CommonConstants.ROLE_ID_PROPERY,
          CommonConstants.TEMPLATE_ID_PROPERY);
    }
    
    OrientVertexType systemVertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.SYSTEM,
        CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(systemVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    OrientVertexType translationsVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.UI_TRANSLATIONS, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(translationsVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IGlobalPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IGlobalPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createIndex(Constants.GLOBAL_CAN_CREATE_PERMISSIONS_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IGlobalPermission.ENTITY_ID, IGlobalPermission.ROLE_ID);
    }
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.GLOBAL_CAN_DELETE_PERMISSIONS);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.GLOBAL_CAN_DELETE_PERMISSIONS);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IGlobalPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IGlobalPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createIndex(Constants.GLOBAL_CAN_DELETE_PERMISSIONS_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IGlobalPermission.ENTITY_ID, IGlobalPermission.ROLE_ID);
    }
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.GLOBAL_CAN_READ_PERMISSIONS);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.GLOBAL_CAN_READ_PERMISSIONS);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IGlobalPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IGlobalPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createIndex(Constants.GLOBAL_CAN_READ_PERMISSIONS_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IGlobalPermission.ENTITY_ID, IGlobalPermission.ROLE_ID);
    }
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.GLOBAL_CAN_EDIT_PERMISSIONS);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.GLOBAL_CAN_EDIT_PERMISSIONS);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IGlobalPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IGlobalPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createIndex(Constants.GLOBAL_CAN_EDIT_PERMISSIONS_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IGlobalPermission.ENTITY_ID, IGlobalPermission.ROLE_ID);
    }
    
    permissionVertexType = graph.getVertexType(VertexLabelConstants.ASSET_CAN_DOWNLOAD_PERMISSIONS);
    if (permissionVertexType == null) {
      permissionVertexType = graph
          .createVertexType(VertexLabelConstants.ASSET_CAN_DOWNLOAD_PERMISSIONS);
      graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
          new Parameter("type", "UNIQUE"), new Parameter("class", permissionVertexType));
      permissionVertexType.createProperty(IGlobalPermission.ROLE_ID, OType.STRING);
      permissionVertexType.createProperty(IGlobalPermission.ENTITY_ID, OType.STRING);
      permissionVertexType.createIndex(Constants.ASSET_CAN_DOWNLOAD_PERMISSIONS_INDEX,
          OClass.INDEX_TYPE.NOTUNIQUE, IGlobalPermission.ENTITY_ID, IGlobalPermission.ROLE_ID);
    }
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.UNIQUE_TAG_PROPERTY,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.MIGRATION,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.LOGO_CONFIGURATION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.VIEW_CONFIGURATION,
            CommonConstants.CODE_PROPERTY);
        
    OrientVertexType mappingVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROPERTY_MAPPING, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(mappingVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    OrientVertexType authorizationVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.AUTHORIZATION_MAPPING, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(authorizationVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    UtilClass.getOrCreateVertexType(VertexLabelConstants.PROMOTION_PRICE_CONTEXTUAL_INTERMIDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.GRID_EDIT_SEQUENCE,
        CommonConstants.CODE_PROPERTY);
    OrientVertexType ssoConfigVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SSO_CONFIGURATION, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(ssoConfigVertexType,
        new String[] { ISSOConfiguration.DOMAIN });
    
    OrientVertexType iconVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ICON, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(iconVertexType,
        new String[] { CommonConstants.CODE_PROPERTY });
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CAN_CLONE_PERMISSION, 
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CAN_GRID_EDIT_PERMISSION, 
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CAN_BULK_EDIT_PERMISSION, 
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CAN_TRANSFER_PERMISSION, 
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CAN_EXPORT_PERMISSION, 
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CAN_SHARE_PERMISSION, 
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CAN_IMPORT_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    
    // now create edge types
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TAG_CONCATENATED_NODE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HTML_CONCATENATED_NODE_LINK,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_POSITION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.RELATIONSHIPLABEL_NOTIFICATION_SETTING_FOR,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_TYPE_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_VALUE_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_ALLOWED_TAGS,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.RELATIONSHIPLABEL_GLOBAL_PERMISSION_FOR,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.KLASS_STRUCTURES_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_SETTING_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_EDITOR_SETTING_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_TREE_SETTING_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_VIEW_SETTING_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_VIEW_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.STRUCTURE_COLLECTION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.STRUCTURE_KLASS_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.STRUCTURE_CHILD_AT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.KLASS_STRUCTURE_CHILD_POSITION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.STRUCTURE_ATTRIBUTE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.STRUCTURE_SETTING_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.STRUCTURE_SETTING_FOR,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_TAXONOMY_SETTING_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_BELONGS_TO,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIP_INSTANCE_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIPLABEL_RELATION_TO,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.BRANCH_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.VARIANT_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TREE_TYPE_OPTION_LINK,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_FILTERABLE_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_SORTABLE_ATTRIBUTE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_DEFAULT_FILTER,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TAG_VALUE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.KLASS_TAXONOMY_LINK,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_MAPPING,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.MAPPED_TO_CLASS,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_COLUMN_MAPPING,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_AUTHORIZATION_TO_ATTRIBUTE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_AUTHORIZATION_TO_KLASS,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAXONOMIES,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_AUTHORIZATION_TO_CONTEXT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_AUTHORIZATION_TO_RELATIONSHIP,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.DATA_RULES,
        CommonConstants.CODE_PROPERTY);
    OrientEdgeType dataRuleLinkEdgeType = UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.DATA_RULES, CommonConstants.CODE_PROPERTY);
    OrientEdgeType ruleEdgeType = graph.getEdgeType(RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
    if (ruleEdgeType == null) {
      ruleEdgeType = UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_KLASS_RULE_LINK,
          CommonConstants.CODE_PROPERTY);
      ruleEdgeType.addSuperClass(dataRuleLinkEdgeType);
    }
    
    ruleEdgeType = graph.getEdgeType(RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK);
    if (ruleEdgeType == null) {
      ruleEdgeType = UtilClass.getOrCreateEdgeType(
          RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK, CommonConstants.CODE_PROPERTY);
      ruleEdgeType.addSuperClass(dataRuleLinkEdgeType);
    }
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TAG_DATA_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.ATTRIBUTE_DATA_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.ROLE_DATA_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TYPE_DATA_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIP_DATA_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RULE_VIOLATION_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RULE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIP_DATA_RULE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TAG_DATA_RULE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.ATTRIBUTE_DATA_RULE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.ROLE_DATA_RULE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.KLASS_DATA_RULE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RULE_TAG_VALUE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_RULE_LIST,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ATTRIBUTE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RULE_USER_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.NORMALIZATION_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.NORMALIZATION_USER_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK,
        CommonConstants.CODE_PROPERTY);
    
    OrientEdgeType nolmalizationLinkEdgeType = UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.NORMALIZATION_LINK, CommonConstants.CODE_PROPERTY);
    OrientEdgeType edgeType = graph
        .getEdgeType(RelationshipLabelConstants.TAXONOMY_NORMALIZATION_LINK);
    if (edgeType == null) {
      edgeType = UtilClass.getOrCreateEdgeType(
          RelationshipLabelConstants.TAXONOMY_NORMALIZATION_LINK,
          CommonConstants.CODE_PROPERTY);
      edgeType.addSuperClass(nolmalizationLinkEdgeType);
    }
    
    edgeType = graph.getEdgeType(RelationshipLabelConstants.TYPE_NORMALIZATION_LINK);
    if (edgeType == null) {
      edgeType = UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TYPE_NORMALIZATION_LINK,
          CommonConstants.CODE_PROPERTY);
      edgeType.addSuperClass(nolmalizationLinkEdgeType);
    }
    
    OrientEdgeType hasKlassProperty = UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.HAS_KLASS_PROPERTY, CommonConstants.CODE_PROPERTY);
    OrientEdgeType hasProperty = UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.HAS_PROPERTY, CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.PREVIOUS_SECTION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.PROPERTY_COLLECTION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.ATTRIBUTE_OPERATOR_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.OPERATOR_ATTRIBUTE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_CONTEXT_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.VARIANT_CONTEXT_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SUB_CONTEXT_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ATTRIBUTE_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ATTRIBUTE_TAG_PROPERTY,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ATTRIBUTE_TAG_VALUE,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_GLOBAL_PERMISSIONS,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_PROPERTY_ENTITY_GLOBAL_PERMISSIONS,
        CommonConstants.CODE_PROPERTY);
    OrientEdgeType klassNatureRelationshipOfEdgeType = graph
        .getEdgeType(RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    if (klassNatureRelationshipOfEdgeType == null) {
      OrientEdgeType klassNatureRelationshipOf = UtilClass.getOrCreateEdgeType(
          RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF,
          CommonConstants.CODE_PROPERTY);
      klassNatureRelationshipOf.addSuperClass(hasKlassProperty);
    }
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_PROPERTY_COLLECTION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.STATUS_TAG_TYPE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE_RELATIONSHIP,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE_CONTEXT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE_TAB,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_RELATIONSHIP_SEQUENCE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TASK,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_RELATIONSHIP_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_PROPERTY_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_HEADER_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TAB_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.IS_PROPERTY_COLLECTION_PERMISSION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.IS_PROPERTY_PERMISSION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.IS_RELATIONSHIP_PERMISSION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.IS_HEADER_PERMISSION_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.IS_TAB_PERMISSION_OF,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.HAS_ROLE_PROPERTY_COLLECTION_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ROLE_RELATIONSHIP_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ROLE_PROPERTY_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ROLE_HEADER_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ROLE_TAB_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE_ROLE,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE_HEADER,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_RELATIONSHIP_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_PRIORITY_TAG,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_STATUS_TAG,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.MASTER_TAG_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_MASTER_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TAXONOMY_LEVEL,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_CONTEXT_KLASS,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.LEVEL_TAGGROUP_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TAXONOMY_LEVEL,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_ATTRIBUTE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_ROLE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_RELATIONSHIP,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_ATTR_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_TAG_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_ROLE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_REL_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOVERNANCE_RULE_USER_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.LINK_KLASS,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.LINK_TAXONOMY,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_KPI,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_GOVERNANCE_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_RACIVS_ROLE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_CANDIDATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_KPI_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_KPI_TAG_VALUE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_LEVEL_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_AVAILABLE_KLASSES,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.ORGANIZATION_ROLE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TARGET_KLASSES,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TARGET_TAXONOMIES,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_SYSTEM,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_DRILL_DOWN,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TAXONOMY_RULE_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_KLASS_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TAXONOMY_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TAXONOMY_DATA_RULE,
        CommonConstants.CODE_PROPERTY);
    // Onboarding
    OrientVertexType endpointVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.ENDPOINT, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(endpointVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.COLUMN_MAPPING,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.VALUE, CommonConstants.CODE_PROPERTY);
    
    OrientVertexType processEventVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROCESS_EVENT, CommonConstants.CODE_PROPERTY);
    UtilClass.createVertexPropertyAndApplyFulLTextIndex(processEventVertexType,
        new String[] { CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY });
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.STEP, CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.COMPONENT,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TAG_CONFIG_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_VALUE_MAPPING,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.NEXT_STEP,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.COMPONENT_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_DATARULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TAB,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TEMPLATE_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ROLE_TEMPLATE_PERMISSION,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ALLOWED_TEMPLATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_KLASS_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOLDEN_RECORD_RULE_ATTRIBUTE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAG_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOLDEN_RECORD_RULE_ENDPOINT_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.EFFECT_TYPE_ENTITY_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.EFFECT_TYPE_ORGANIZATION_LINK,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_UNIQUE_SELECTOR,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_UNIQUE_TAG_PROPERTY,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_UNIQUE_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_UNIQUE_TAG_VALUE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.CONTEXT_CLONE_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TAXONOMY_TO_CLONE,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_LINKED_AUTHORIZATION_MAPPING,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateSequence();
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_CLONE_PERMISSION,
        "");
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_GRID_EDIT_PERMISSION,
        "");
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_BULK_EDIT_PERMISSION,
        "");
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TRANSFER_PERMISSION,
        "");
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_EXPORT_PERMISSION,
        "");
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_SHARE_PERMISSION,
        "");
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_IMPORT_PERMISSION,
        "");
    UtilClass.getOrCreateVertexType(VertexLabelConstants.VARIANT_CONFIGURATION,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateSequence();
  }
  
  private void createSmartDocumentVertexTypes()
  {
    UtilClass.getOrCreateVertexType(VertexLabelConstants.SMART_DOCUMENT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.SMART_DOCUMENT_TEMPLATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.SMART_DOCUMENT_PRESET,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.SMART_DOCUMENT_PRESET_ATTR_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.SMART_DOCUMENT_PRESET_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.SMART_DOCUMENT_PRESET_TAG_INTERMEDIATE,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_TEMPLATE_PRESET_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_ATTRIBUTE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAG_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_KLASS_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_TAXONOMY_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_ATTR_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_ATTRIBUTE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_LINK,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(
        RelationshipLabelConstants.SMART_DOCUMENT_PRESET_RULE_TAG_VALUE_LINK,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.PROMOTION_PRICE_CONTEXTUAL_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_PROMOTION_PRICE_CONTEXTUAL_TAG,
        CommonConstants.CODE_PROPERTY);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateVertexTypes/*" };
  }
}
