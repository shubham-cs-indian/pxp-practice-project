package com.cs.config.strategy.plugin.usecase.role.abstrct;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.config.interactor.model.role.IGetRoleModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.SystemLabels;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGetRole extends AbstractOrientPlugin {
  
  public AbstractGetRole(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public HashMap<String, Object> getRole(Map<String, Object> requestMap, String roleId,
      String label, String entityName) throws Exception
  {
    HashMap<String, Object> roleMap;
    HashMap<String, Object> roleMapToReturn = new HashMap<String, Object>();
    
    OrientGraph graph = UtilClass.getGraph();
    
    try {
      Vertex roleNode = UtilClass.getVertexByIndexedId(roleId, label);
      roleMap = RoleUtils.getRoleEntityMap(roleNode);
      /*
      List<Map<String, Object>> referencedKlasses = new ArrayList<Map<String, Object>>();
      Iterable<Edge> hasTargetKlasses = roleNode.getEdges(com.tinkerpop.blueprints.Direction.OUT,
          RelationshipLabelConstants.HAS_TARGET_KLASSES);
      
      for (Edge relationship : hasTargetKlasses) {
        Vertex targetKlass = relationship.getVertex(com.tinkerpop.blueprints.Direction.IN);
        referencedKlasses.add(UtilClass.getMapFromNode(targetKlass));
      }
      List<Map<String, Object>> referencedTaxonomies = new ArrayList<Map<String, Object>>();
      Iterable<Edge> hasTargetTaxonomies = roleNode.getEdges(com.tinkerpop.blueprints.Direction.OUT,
          RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
      
      for (Edge relationship : hasTargetTaxonomies) {
        Vertex targetTaxonomy = relationship.getVertex(com.tinkerpop.blueprints.Direction.IN);
        referencedTaxonomies.add(UtilClass.getMapFromNode(targetTaxonomy));
      }
      
      roleMap.put(IGetRoleModel.REFERENCED_KLASSES, referencedKlasses);
      roleMap.put(IGetRoleModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
      */
      RoleUtils.addTargetKlasses(roleMap, roleNode, roleMapToReturn);
      RoleUtils.addTargetTaxonomies(roleMap, roleNode, roleMapToReturn);
      RoleUtils.addReferencedEndPoints(roleMap, roleNode, roleMapToReturn);
      RoleUtils.addReferencedKPIs(roleMap, roleNode, roleMapToReturn);
      RoleUtils.addReferencedSystems(roleMap, roleNode, roleMapToReturn);
      roleMapToReturn.put(IGetRoleModel.ROLE, roleMap);
      roleMapToReturn.put("articles", KlassGetUtils.getKlassesList(
          SystemLabels.STANDARD_ARTICLE_KLASS_LABEL, VertexLabelConstants.ENTITY_TYPE_KLASS));
      roleMapToReturn.put("collections",
          KlassGetUtils.getKlassesList("collection", VertexLabelConstants.ENTITY_TYPE_KLASS));
      roleMapToReturn.put("sets",
          KlassGetUtils.getKlassesList("set", VertexLabelConstants.ENTITY_TYPE_KLASS));
      roleMapToReturn.put("assets",
          KlassGetUtils.getKlassesList("asset_asset", VertexLabelConstants.ENTITY_TYPE_ASSET));
      roleMapToReturn.put("collectionAssets",
          KlassGetUtils.getKlassesList("collection_asset", VertexLabelConstants.ENTITY_TYPE_ASSET));
      roleMapToReturn.put("markets",
          KlassGetUtils.getKlassesList(SystemLabels.STANDARD_TARGET_KLASS_MARKET_LABEL,
              VertexLabelConstants.ENTITY_TYPE_TARGET));
      roleMapToReturn.put("collectionTargets", KlassGetUtils.getKlassesList("collection_target",
          VertexLabelConstants.ENTITY_TYPE_TARGET));
    }
    catch (NotFoundException e) {
      throw new RoleNotFoundException();
    }
    
    return roleMapToReturn;
  }
}
