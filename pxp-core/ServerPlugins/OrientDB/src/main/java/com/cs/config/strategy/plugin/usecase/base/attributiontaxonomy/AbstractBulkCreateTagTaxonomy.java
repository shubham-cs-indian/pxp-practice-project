package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.tag.BulkCreateTaxonomyFailedException;
import com.cs.core.config.interactor.exception.taxonomy.ParentKlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.IBulkCreateTaxonomyListResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractBulkCreateTagTaxonomy extends AbstractOrientPlugin {
  
  public AbstractBulkCreateTagTaxonomy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
  }
  
  public abstract String getTaxonomyLevelType();
  
  public abstract String getVertexType();
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfRequestMap = (List<Map<String, Object>>) requestMap.get("list");
    List<Map<String, Object>> createdKlassList = new ArrayList<>();
    Map<String, Object> result = new HashMap<>();
    
    Map<String, Object> referencedTaxonomies = new HashMap<String, Object>();
    Map<String, Integer> isPermissionFromRoleOrOrganization = new HashMap<String, Integer>();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);
    
    IExceptionModel failure = new ExceptionModel();
    for (Map<String, Object> taxonomyMap : listOfRequestMap) {
      try {
        String parentId = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.PARENT_TAXONOMY_ID);
        if (parentId == null || parentId.equals("-1")) {
          String taxonomyType = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.TAXONOMY_TYPE);
          if (taxonomyType == null) {
            taxonomyMap.put(IMasterTaxonomy.TAXONOMY_TYPE, CommonConstants.MAJOR_TAXONOMY);
          }
          taxonomyMap.put(ITaxonomy.IS_ROOT, true);
        }
        String tagValueId = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.TAG_VALUE_ID);
        Boolean isNewlyCreated = (Boolean) taxonomyMap
            .get(ICreateMasterTaxonomyModel.IS_NEWLY_CREATED);
        Vertex taxonomyNode;
        if ((isNewlyCreated == null || !isNewlyCreated) && tagValueId != null) {
          taxonomyNode = UtilClass.getVertexById(tagValueId, VertexLabelConstants.ENTITY_TAG);
        }
        else {
          taxonomyNode = AttributionTaxonomyUtil.createAttributionTaxonomyNode(taxonomyMap,
              getVertexType());
        }
        taxonomyNode.setProperty(IMasterTaxonomy.IS_TAXONOMY, true);
        taxonomyNode.setProperty(IMasterTaxonomy.CHILD_COUNT, 0);
        taxonomyNode.setProperty(IMasterTaxonomy.CLASSIFIER_IID, taxonomyMap.get(IMasterTaxonomy.CLASSIFIER_IID));
        Vertex parent = null;
        String taxonomyType = null;
        if (parentId != null && !parentId.equals("-1")) {
          try {
            parent = UtilClass.getVertexById(parentId, getVertexType());
            Integer childCount = parent.getProperty(IMasterTaxonomy.CHILD_COUNT);
            parent.setProperty(IMasterTaxonomy.CHILD_COUNT, ++childCount);
            taxonomyType = parent.getProperty(ITaxonomy.TAXONOMY_TYPE);
            if (taxonomyType != null) {
              taxonomyNode.setProperty(ITaxonomy.TAXONOMY_TYPE,
                  parent.getProperty(ITaxonomy.TAXONOMY_TYPE));
            }
          }
          catch (NotFoundException e) {
            throw new ParentKlassTaxonomyNotFoundException(e);
          }
          
          taxonomyNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parent);
          KlassUtils.inheritParentKlassData(taxonomyNode, parent);
          if (taxonomyType.equals(CommonConstants.MAJOR_TAXONOMY)) {
            KlassUtils.inheritParentEmbeddedKlasses(taxonomyNode, parent); 
          }
          TaxonomyUtil.fillParentIdAndConfigDetails(new HashMap<String, Object>(),
              referencedTaxonomies, taxonomyNode, isPermissionFromRoleOrOrganization,
              new ArrayList<String>());
        }        
        AuditLogUtils.fillAuditLoginfo(result, taxonomyNode, Entities.TAXONOMIES, Elements.MASTER_TAXONOMY_CONFIGURATION_TITLE);
        UtilClass.getGraph()
            .commit();
        Map<String, Object> mapOfDataEachTaxonomy = AttributionTaxonomyUtil.getAttributionTaxonomy(taxonomyNode, getTaxonomyLevelType());
        KlassUtils.addSectionsToKlassEntityMap(taxonomyNode, mapOfDataEachTaxonomy);
        mapOfDataEachTaxonomy.remove(IKlass.PERMISSIONS);
        createdKlassList.add(mapOfDataEachTaxonomy);
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) taxonomyMap.get(IKlassModel.LABEL));
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) taxonomyMap.get(IKlassModel.LABEL));
      }
    }
    if (createdKlassList.size() == 0) {
      throw new BulkCreateTaxonomyFailedException();
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkCreateTaxonomyListResponseModel.LIST, createdKlassList);
    responseMap.put(IBulkCreateTaxonomyListResponseModel.REFERENCED_TAXONOMIES,
        referencedTaxonomies);
    result.put(IPluginSummaryModel.SUCCESS, responseMap);
    result.put(IPluginSummaryModel.FAILURE, failure);
    return result;
  }
}
