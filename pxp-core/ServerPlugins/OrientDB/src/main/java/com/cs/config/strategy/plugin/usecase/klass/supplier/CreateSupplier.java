package com.cs.config.strategy.plugin.usecase.klass.supplier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.supplier.util.SupplierUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.supplier.ISupplier;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class CreateSupplier extends AbstractOrientPlugin {
  
  public CreateSupplier(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> returnKlassMap = new HashMap<>();
    HashMap<String, Object> supplierMap = new HashMap<String, Object>();
    boolean inheritenceFlag = true;
    supplierMap = (HashMap<String, Object>) map.get("supplier");
    inheritenceFlag = map.get("shouldInherit") != null ? (boolean) map.get("shouldInherit") : true;
    
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setSectionElementIdMap(new HashMap<>());
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_SUPPLIER, CommonConstants.CODE_PROPERTY);
    
    Vertex supplierNode = createSupplierNode(supplierMap, vertexType);
    CreateKlassUtils.inheritTreeTypeOption(supplierMap, supplierNode,
        VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
    HashMap<String, Object> parentKlassMap = (HashMap<String, Object>) supplierMap
        .get(ISupplier.PARENT);
    String parentId = parentKlassMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    KlassUtils.createSectionNodes(supplierMap, supplierNode,
        VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
    KlassUtils.createParentChildLink(supplierNode, VertexLabelConstants.ENTITY_TYPE_SUPPLIER,
        supplierMap, inheritenceFlag);
    VariantContextUtils.manageContextKlasses(supplierMap, supplierNode);
    if (!parentId.equals("-1")) {
      CreateKlassUtils.inheritKlassRelationshipNodesInChildKlasses(supplierNode, parentId);
    }
    
    graph.commit();
    
    Map<String, Object> klassMap = SupplierUtils.getSupplierEntityMap(supplierNode, null);
    returnKlassMap.put(IGetKlassEntityWithoutKPModel.ENTITY, klassMap);
    KlassGetUtils.fillReferencedConfigDetails(returnKlassMap, supplierNode);
    AuditLogUtils.fillAuditLoginfo(returnKlassMap, supplierNode, Entities.CLASSES, Elements.SUPPLIER);

    return returnKlassMap;
  }
  
  private Vertex createSupplierNode(HashMap<String, Object> supplierMap,
      OrientVertexType vertexType) throws Exception
  {
    Vertex supplierNode = null;
    if (supplierMap.get(CommonConstants.ID_PROPERTY) != null) {
      String supplierId = supplierMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      supplierMap.remove(ISupplierModel.REFERENCED_TAGS);
      supplierNode = UtilClass.createNode(supplierMap, vertexType, propertiesToExclude);
      supplierNode.setProperty(CommonConstants.CODE_PROPERTY, supplierId);
    }
    else {
      supplierNode = UtilClass.createNode(supplierMap, vertexType, propertiesToExclude);
    }
    return supplierNode;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateSupplier/*" };
  }
  
  public static List<String> propertiesToExclude = Arrays.asList(IAssetModel.REFERENCED_CONTEXTS,
      "parent", "children", "permission", CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      IAssetModel.EMBEDDED_KLASS_IDS);
}
