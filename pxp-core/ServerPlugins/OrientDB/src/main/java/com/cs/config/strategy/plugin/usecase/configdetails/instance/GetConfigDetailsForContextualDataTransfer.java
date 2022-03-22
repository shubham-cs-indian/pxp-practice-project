package com.cs.config.strategy.plugin.usecase.configdetails.instance;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.model.klass.IContextKlassModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IGetConfigDetailsForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeInfoModel;
import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;
import com.cs.core.runtime.strategy.model.couplingtype.IPropertiesIdCodeCouplingTypeModel;
import com.cs.repository.klass.context.KlassContextRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForContextualDataTransfer extends AbstractOrientPlugin {
  
  public GetConfigDetailsForContextualDataTransfer(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForContextualDataTransfer/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap.get(IKlassInstanceTypeInfoModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IKlassInstanceTypeInfoModel.TAXONOMY_IDS);
    klassIds.addAll(taxonomyIds);
    
    Map<String, Object> returnMap = prepareDataForResponse();
    
    fillPropertiesForContextualDataTransfer(klassIds, returnMap);
    String parentId = (String) requestMap.get(IKlassInstanceTypeInfoModel.PARENT_ID);
    
    if (parentId != null) {
      List<String> parentKlassIds = (List<String>) requestMap
          .get(IKlassInstanceTypeInfoModel.PARENT_KLASS_IDS);
      List<String> parentTaxonomyIds = (List<String>) requestMap
          .get(IKlassInstanceTypeInfoModel.PARENT_TAXONOMY_IDS);
      parentKlassIds.addAll(parentTaxonomyIds);
      
      fillPropertiesForContextualDataInheritance(klassIds, returnMap, parentKlassIds);
    }
    return returnMap;
  }
  
  private void fillPropertiesForContextualDataTransfer(List<String> klassIds,
      Map<String, Object> returnMap)
  {
    Iterable<Vertex> klassContextualPropagablePropertyVertices = KlassContextRepository
        .getKlassContextPropagablePropertyVerticesTransfer(klassIds);
    
    List<String> tagsIdsToTransfer = (List<String>) returnMap
        .get(IGetConfigDetailsForContextualDataTransferModel.TAG_IDS_TO_TRANSFER);
    List<String> dependentAttributesIdsToTransfer = (List<String>) returnMap
        .get(IGetConfigDetailsForContextualDataTransferModel.DEPENDENT_ATTRIBUTE_IDS_TO_TRANSFER);
    List<String> independentAttributesIdsToTransfer = (List<String>) returnMap
        .get(IGetConfigDetailsForContextualDataTransferModel.INDEPENDENT_ATTRIBUTE_IDS_TO_TRANSFER);
    Map<String, Object> contextualDataTransfer = (Map<String, Object>) returnMap
        .get(IGetConfigDetailsForContextualDataTransferModel.CONTEXTUAL_DATA_TRANSFER);
    
    for (Vertex klassContextualPropagablePropertyVertex : klassContextualPropagablePropertyVertices) {
      
      Iterable<Edge> contextualPropagablePropertyEdges = klassContextualPropagablePropertyVertex
          .getEdges(Direction.OUT,
              RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK);
      String contextKlassId = (String) klassContextualPropagablePropertyVertex
          .getProperty(IContextKlassModel.CONTEXT_KLASS_ID);
      
      Map<String, Object> propertiesMap = fillProperties(tagsIdsToTransfer,
          dependentAttributesIdsToTransfer, independentAttributesIdsToTransfer,
          contextualPropagablePropertyEdges);
      
      contextualDataTransfer.put(contextKlassId, propertiesMap);
      returnMap.put(IGetConfigDetailsForContextualDataTransferModel.CONTEXT_KLASS_ID,
          contextKlassId);
    }
  }
  
  private void fillPropertiesForContextualDataInheritance(List<String> klassIds,
      Map<String, Object> returnMap, List<String> parentKlassIds)
  {
    Iterable<Vertex> klassContextualPropagablePropertyVertices1 = KlassContextRepository
        .klassContextPropagablePropertyVerticesInherit(klassIds, parentKlassIds);
    
    List<String> tagIdsToInherit = (List<String>) returnMap
        .get(IGetConfigDetailsForContextualDataTransferModel.TAG_IDS_TO_INHERITANCE);
    List<String> dependentAttributeIdsToInherit = (List<String>) returnMap.get(
        IGetConfigDetailsForContextualDataTransferModel.DEPENDENT_ATTRIBUTE_IDS_TO_INHERITANCE);
    List<String> indenpendentAttributeIdsToInherit = (List<String>) returnMap.get(
        IGetConfigDetailsForContextualDataTransferModel.INDEPENDENT_ATTRIBUTE_IDS_TO_INHERITANCE);
    
    for (Vertex klassContextualPropagablePropertyVertex : klassContextualPropagablePropertyVertices1) {
      
      Iterable<Edge> contextualPropagablePropertyEdges = klassContextualPropagablePropertyVertex
          .getEdges(Direction.OUT,
              RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK);
      
      returnMap.put(IGetConfigDetailsForContextualDataTransferModel.CONTEXTUAL_DATA_INHERITANCE,
          fillProperties(tagIdsToInherit, dependentAttributeIdsToInherit,
              indenpendentAttributeIdsToInherit, contextualPropagablePropertyEdges));
      String contextKlassId = (String) klassContextualPropagablePropertyVertex
          .getProperty(IContextKlassModel.CONTEXT_KLASS_ID);
      returnMap.put(IGetConfigDetailsForContextualDataTransferModel.CONTEXT_KLASS_ID,
          contextKlassId);
    }
  }
  
  private Map<String, Object> fillProperties(List<String> tagsIdsToTransfer,
      List<String> dependentAttributesIdsToTransfer,
      List<String> independentAttributesIdsToTransfer,
      Iterable<Edge> contextualPropagablePropertyEdges)
  {
    List<Map<String, Object>> tags = new ArrayList<>();
    List<Map<String, Object>> dependentAttributes = new ArrayList<>();
    List<Map<String, Object>> independentAttributes = new ArrayList<>();
    Map<String, Object> propertiesMap = new HashMap<>();
    
    for (Edge contextualPropagablePropertyEdge : contextualPropagablePropertyEdges) {
      
      Vertex propertyVertex = contextualPropagablePropertyEdge.getVertex(Direction.IN);
      String code = UtilClass.getCodeNew(propertyVertex);
      Map<String, Object> idCodeCouplingTypeModel = new HashMap<>();
      idCodeCouplingTypeModel.put(IIdCodeCouplingTypeModel.ID, code);
      idCodeCouplingTypeModel.put(IIdCodeCouplingTypeModel.COUPLING_TYPE,
          contextualPropagablePropertyEdge.getProperty(ISectionElement.COUPLING_TYPE));
      
      String vertexType = propertyVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
      if (vertexType.equals(VertexLabelConstants.ENTITY_TAG)) {
        tags.add(idCodeCouplingTypeModel);
        tagsIdsToTransfer.add(code);
      }
      else {
        Boolean istranslatable = propertyVertex.getProperty(IAttribute.IS_TRANSLATABLE);
        if (istranslatable) {
          dependentAttributes.add(idCodeCouplingTypeModel);
          dependentAttributesIdsToTransfer.add(code);
        }
        else {
          independentAttributes.add(idCodeCouplingTypeModel);
          independentAttributesIdsToTransfer.add(code);
        }
      }
    }
    propertiesMap.put(IPropertiesIdCodeCouplingTypeModel.TAGS, tags);
    propertiesMap.put(IPropertiesIdCodeCouplingTypeModel.INDEPENDENT_ATTRIBUTES,
        independentAttributes);
    propertiesMap.put(IPropertiesIdCodeCouplingTypeModel.DEPENDENT_ATTRIBUTES, dependentAttributes);
    return propertiesMap;
  }
  
  private Map<String, Object> prepareDataForResponse()
  {
    Map<String, Object> returnMap = new HashMap<>();
    
    returnMap.put(IGetConfigDetailsForContextualDataTransferModel.CONTEXTUAL_DATA_TRANSFER,
        new HashMap<>());
    returnMap.put(IGetConfigDetailsForContextualDataTransferModel.CONTEXTUAL_DATA_INHERITANCE,
        new HashMap<>());
    returnMap.put(
        IGetConfigDetailsForContextualDataTransferModel.DEPENDENT_ATTRIBUTE_IDS_TO_TRANSFER,
        new ArrayList<>());
    returnMap.put(
        IGetConfigDetailsForContextualDataTransferModel.INDEPENDENT_ATTRIBUTE_IDS_TO_TRANSFER,
        new ArrayList<>());
    returnMap.put(IGetConfigDetailsForContextualDataTransferModel.TAG_IDS_TO_TRANSFER,
        new ArrayList<>());
    returnMap.put(
        IGetConfigDetailsForContextualDataTransferModel.DEPENDENT_ATTRIBUTE_IDS_TO_INHERITANCE,
        new ArrayList<>());
    returnMap.put(
        IGetConfigDetailsForContextualDataTransferModel.INDEPENDENT_ATTRIBUTE_IDS_TO_INHERITANCE,
        new ArrayList<>());
    returnMap.put(IGetConfigDetailsForContextualDataTransferModel.TAG_IDS_TO_INHERITANCE,
        new ArrayList<>());
    return returnMap;
  }
}
