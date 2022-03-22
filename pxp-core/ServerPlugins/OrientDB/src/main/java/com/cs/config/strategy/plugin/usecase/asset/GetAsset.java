package com.cs.config.strategy.plugin.usecase.asset;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetAsset extends AbstractOrientPlugin {
  
  public GetAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    String endpointId = (String) map.get(IMulticlassificationRequestModel.ENDPOINT_ID);
    String organisationId = (String) map.get(IMulticlassificationRequestModel.ORAGANIZATION_ID);
    String physicalCatalogId = (String) map
        .get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID);
    
    OrientGraph graph = UtilClass.getGraph();
    String id = "";
    Map<String, Object> rootNode = new HashMap<>();
    id = (String) map.get("id");
    
    if (id.equals("-1")) {
      
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.ASSET_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      // TODO handle first time error when klass doesnot present
      Iterable<Vertex> i = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ASSET
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex assetNode : i) {
        childKlasses.add(AssetUtils.getAssetEntityMap(assetNode, null));
      }
      
    }
    else {
      Vertex assetNode = null;
      try {
        assetNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TYPE_ASSET);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      rootNode = AssetUtils.getAssetEntityMap(assetNode, null);
      rootNode.put(IAssetModel.DATA_RULES_OF_KLASS,
          KlassUtils.getDataRulesOfKlass(assetNode, endpointId, organisationId, physicalCatalogId));
      
      List<Map<String, Object>> referencedContexts = new ArrayList<>();
      Iterator<Vertex> contextIterator = assetNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
          .iterator();
      while (contextIterator.hasNext()) {
        List<String> attributeIds = new ArrayList<>();
        Vertex contextNode = contextIterator.next();
        Map<String, Object> contextMap = VariantContextUtils.getContext(contextNode,
            new HashMap<>());
        String type = (String) contextMap.get(IVariantContext.TYPE);
        Boolean isAutoCreate = (Boolean) contextMap.get(IVariantContext.IS_AUTO_CREATE);
        contextMap.put(IGetVariantContextModel.ATTRIBUTE_IDS, attributeIds);
        if (type.equals(CommonConstants.IMAGE_VARIANT) && isAutoCreate) {
          getMetaDataAttributeIds(contextNode, attributeIds);
          referencedContexts.add(contextMap);
        }
      }
      rootNode.put(IAssetModel.REFERENCED_CONTEXTS, referencedContexts);
      
      List<String> lifeCycleTags = (List<String>) rootNode.get(IKlass.LIFE_CYCLE_STATUS_TAGS);
      List<Map<String, Object>> referencedTags = new ArrayList<>();
      
      for (String lifeCycleTagId : lifeCycleTags) {
        Vertex linkedTagNode = UtilClass.getVertexById(lifeCycleTagId,
            VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
        referencedTags.add(referencedTag);
      }
      rootNode.put(IAssetModel.REFERENCED_TAGS, referencedTags);
      List<Map<String, Object>> variantsList = new ArrayList<>();
      KlassGetUtils.fillTechnicalImageVariantWithAutoCreateEnabled(assetNode, variantsList);
      rootNode.put(IAssetModel.TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE, variantsList);
      KlassUtils.fillContextKlassesDetails(new HashMap<>(), rootNode, assetNode);
    }
    
    return rootNode;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAsset/*" };
  }
  
  /**
   * @author Arshad
   * @description This method gets all metadata attributeids associated with
   *              context
   */
  public void getMetaDataAttributeIds(Vertex contextNode, List<String> attributeIds)
  {
    Iterator<Vertex> kpIterator = contextNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_PROPERTY)
        .iterator();
    while (kpIterator.hasNext()) {
      Vertex kpNode = kpIterator.next();
      String type = kpNode.getProperty("type");
      if (type.equals(Constants.ATTRIBUTE)) {
        Iterator<Vertex> propertyIterator = kpNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        if (propertyIterator.hasNext()) {
          Vertex propertyNode = propertyIterator.next();
          String attributeType = propertyNode.getProperty(IAttribute.TYPE);
          if (attributeType.equals(CommonConstants.ASSET_METADATA_ATTRIBUTE_TYPE)) {
            attributeIds.add(propertyNode.getProperty(CommonConstants.CODE_PROPERTY));
          }
        }
      }
    }
  }
}
