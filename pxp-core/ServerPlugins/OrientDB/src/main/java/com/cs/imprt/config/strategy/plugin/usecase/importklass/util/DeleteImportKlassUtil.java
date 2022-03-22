package com.cs.imprt.config.strategy.plugin.usecase.importklass.util;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.ImportVertexLabelConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DeleteImportKlassUtil {
  
  public static void deleteKlassNode(Vertex klassNode, List<String> selfAndChildKlassIds,
      Set<Vertex> nodesToDelete, Set<Edge> relationshipsToDelete) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    
    Iterable<Edge> sectionOfRelationships = klassNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    
    for (Edge sectionRelation : sectionOfRelationships) {
      
      Vertex sectionNode = sectionRelation.getVertex(Direction.OUT);
      KlassUtils.deleteSectionFromKlass(sectionRelation, sectionNode, selfAndChildKlassIds, klassId,
          nodesToDelete, relationshipsToDelete, true,
          ImportVertexLabelConstants.ENTITY_TYPE_IMPORT_KLASS, new HashMap<>());
    }
    
    Iterable<Edge> hasGlobalPermissionRelationships = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
    for (Edge globalPermissionRelationship : hasGlobalPermissionRelationships) {
      Vertex globalPermissionNode = globalPermissionRelationship.getVertex(Direction.IN);
      Iterator<Edge> iterator = globalPermissionNode
          .getEdges(Direction.OUT,
              RelationshipLabelConstants.RELATIONSHIPLABEL_GLOBAL_PERMISSION_FOR)
          .iterator();
      Edge globalPermissionForRelationship = null;
      while (iterator.hasNext()) {
        globalPermissionForRelationship = iterator.next();
      }
      
      relationshipsToDelete.add(globalPermissionForRelationship);
      relationshipsToDelete.add(globalPermissionRelationship);
      nodesToDelete.add(globalPermissionNode);
    }
    
    // taxonomy node deletion
    Iterator<Edge> iterator = klassNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_TAXONOMY_SETTING_OF)
        .iterator();
    Edge taxonomySettingOf = null;
    while (iterator.hasNext()) {
      taxonomySettingOf = iterator.next();
      relationshipsToDelete.add(taxonomySettingOf);
    }
    
    ImportKlassUtils.deleteAllowedTypeRelationships(klassNode);
    
    nodesToDelete.add(klassNode);
  }
}
