package com.cs.config.strategy.plugin.usecase.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class IdentifierAttributeUtils {
  
  public static void fillIdentifierAttributesForKlasses(Map<String, Object> returnMap,
      List<String> klassIds) throws Exception
  {
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexById(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        fillIdentifierAttributesForType(returnMap, klassVertex);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
  }
  
  public static void fillIdentifierAttributesForTaxonomies(Map<String, Object> returnMap,
      List<String> taxonomyIds) throws Exception
  {
    for (String taxonomyId : taxonomyIds) {
      try {
        Vertex taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        fillIdentifierAttributesForType(returnMap, taxonomyVertex);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
    }
  }
  
  public static void fillIdentifierAttributesForType(Map<String, Object> mapToReturn,
      Vertex klassNode) throws Exception
  {
    Map<String, Object> typeIdIdentifierAttributeIds = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS);
    List<String> identifierAttributesForKlass = new ArrayList<>();
    typeIdIdentifierAttributeIds.put(UtilClass.getCodeNew(klassNode), identifierAttributesForKlass);
    Iterable<Vertex> kPNodesIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassPropertyNode : kPNodesIterable) {
      Iterator<Vertex> entityIterator = klassPropertyNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex entityNode = entityIterator.next();
      String entityId = UtilClass.getCodeNew(entityNode);
      String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE)) {
        Boolean isIdentifierAttribute = klassPropertyNode
            .getProperty(ISectionAttribute.IS_IDENTIFIER);
        if (isIdentifierAttribute != null && isIdentifierAttribute) {
          identifierAttributesForKlass.add(entityId);
        }
      }
    }
  }
}
