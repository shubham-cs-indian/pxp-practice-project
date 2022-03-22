package com.cs.config.strategy.plugin.usecase.base.organization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.exception.organization.OrganizationNotFoundException;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.interactor.model.organization.ISaveOrganizationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public abstract class AbstractSaveOrganization extends AbstractOrientPlugin {
  
  public static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList(
      ISaveOrganizationModel.TAXONOMY_IDS, ISaveOrganizationModel.ROLE_IDS,
      ISaveOrganizationModel.ADDED_TAXONOMY_IDS, ISaveOrganizationModel.DELETED_TAXONOMY_IDS,
      ISaveOrganizationModel.ADDED_KLASS_IDS, ISaveOrganizationModel.DELETED_KLASS_IDS,
      ISaveOrganizationModel.ENDPOINT_IDS, ISaveOrganizationModel.ADDED_ENDPOINT_IDS,
      ISaveOrganizationModel.DELETED_ENDPOINT_IDS, ISaveOrganizationModel.ADDED_SYSTEM_IDS,
      ISaveOrganizationModel.DELETED_SYSTEM_IDS, ISaveOrganizationModel.IS_STANDARD);
  
  public AbstractSaveOrganization(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String organizationId = (String) requestMap.get(IOrganizationModel.ID);
    Map<String, Object> returnMap = new HashMap<>();
    Vertex organizationVertex = null;
    try {
      organizationVertex = UtilClass.getVertexById(organizationId,
          VertexLabelConstants.ORGANIZATION);
    }
    catch (NotFoundException e) {
      throw new OrganizationNotFoundException();
    }
    
    List<String> newPhysicalCatalogs = (List<String>) requestMap
        .get(ISaveOrganizationModel.PHYSICAL_CATALOGS);
    List<String> oldPhysicalCatalogs = (List<String>) organizationVertex
        .getProperty(IOrganizationModel.PHYSICAL_CATALOGS);
    if (!newPhysicalCatalogs.equals(oldPhysicalCatalogs)) {
      OrganizationUtil.updatePhysicalCatalogsInRole(organizationVertex, newPhysicalCatalogs);
    }
    
    UtilClass.saveNode(requestMap, organizationVertex, FIELDS_TO_EXCLUDE);
    List<String> addedTaxonomyIds = (List<String>) requestMap
        .get(ISaveOrganizationModel.ADDED_TAXONOMY_IDS);
    OrganizationUtil.manageAddedTaxonomies(addedTaxonomyIds, organizationVertex);
    
    List<String> deletedTaxonomyIds = (List<String>) requestMap
        .get(ISaveOrganizationModel.DELETED_TAXONOMY_IDS);
    OrganizationUtil.manageDeletedTaxonomies(deletedTaxonomyIds, organizationVertex);
    
    List<String> addedKlassIds = (List<String>) requestMap
        .get(ISaveOrganizationModel.ADDED_KLASS_IDS);
    OrganizationUtil.manageAddedKlasses(addedKlassIds, organizationVertex);
    
    List<String> deletedKlassIds = (List<String>) requestMap
        .get(ISaveOrganizationModel.DELETED_KLASS_IDS);
    OrganizationUtil.manageDeletedKlasses(deletedKlassIds, organizationVertex);
    
    manageEndpoints(requestMap, organizationVertex);
    manageSystemIds(requestMap, organizationVertex);
    
    UtilClass.getGraph()
        .commit();
    returnMap = OrganizationUtil.getOrganizationMapToReturnWithReferencedData(organizationVertex);
    AuditLogUtils.fillAuditLoginfo(returnMap, organizationVertex, Entities.PARTNERS, Elements.UNDEFINED);
    return returnMap;
  }
  
  private void manageSystemIds(Map<String, Object> requestMap, Vertex organizationVertex)
      throws Exception
  {
    List<String> deletedSystemIds = (List<String>) requestMap
        .get(ISaveOrganizationModel.DELETED_SYSTEM_IDS);
    List<String> addedSystemIds = (List<String>) requestMap
        .get(ISaveOrganizationModel.ADDED_SYSTEM_IDS);
    if (deletedSystemIds.isEmpty() && addedSystemIds.isEmpty()) {
      return;
    }
    
    Iterable<Vertex> organizationSystems = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION);
    List<String> exsitingOrganizationSystemIds = new ArrayList<>();
    for (Vertex organizationEndPoint : organizationSystems) {
      exsitingOrganizationSystemIds.add(UtilClass.getCodeNew(organizationEndPoint));
    }
    
    OrganizationUtil.manageDeletedSystemIds(deletedSystemIds, organizationVertex, exsitingOrganizationSystemIds);

    OrganizationUtil.manageAddedSystemIds(organizationVertex, addedSystemIds, exsitingOrganizationSystemIds);
  }

  private void manageEndpoints(Map<String, Object> requestMap, Vertex organizationVertex)
      throws Exception
  {
    List<String> addedEndpointIds = (List<String>) requestMap
        .get(ISaveOrganizationModel.ADDED_ENDPOINT_IDS);
    OrganizationUtil.manageAddedEndpoints(organizationVertex, addedEndpointIds);
    
    List<String> deletedEndpointIds = (List<String>) requestMap
        .get(ISaveOrganizationModel.DELETED_ENDPOINT_IDS);
    OrganizationUtil.manageDeletedEndpoints(deletedEndpointIds, organizationVertex);
  }
}
