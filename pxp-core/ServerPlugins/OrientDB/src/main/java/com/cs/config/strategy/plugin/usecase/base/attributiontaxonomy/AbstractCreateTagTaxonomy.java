package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.validationontype.InvalidTaxonomyTypeException;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public abstract class AbstractCreateTagTaxonomy extends AbstractOrientPlugin {
  
  public AbstractCreateTagTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getVertexType();
  public abstract String getTaxonomyLevelType();

  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> taxonomyMap = (HashMap<String, Object>) requestMap.get("klassTaxonomy");
    Map<String, Object> mapToReturn = new HashMap<>();

    
    try {
      UtilClass.validateOnType(Constants.TAXONOMY_TYPE_LIST_FOR_TAXONOMY, (String)taxonomyMap.get(ICreateMasterTaxonomyModel.TAXONOMY_TYPE),true);
    }
    catch(InvalidTypeException e) {
      throw new InvalidTaxonomyTypeException(e);
    }
    String parentId = (String)taxonomyMap.get(ICreateMasterTaxonomyModel.PARENT_TAXONOMY_ID);

    if (parentId == null || parentId.equals("-1")) {
      String taxonomyType = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.TAXONOMY_TYPE);
      if (taxonomyType == null) {
        taxonomyType = CommonConstants.MAJOR_TAXONOMY;
        taxonomyMap.put(IMasterTaxonomy.TAXONOMY_TYPE, taxonomyType);
      }
      taxonomyMap.put(ITaxonomy.IS_ROOT, true);
    }
    String tagValueId = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.TAG_VALUE_ID);
    Boolean isNewlyCreated = (Boolean) taxonomyMap.get(ICreateMasterTaxonomyModel.IS_NEWLY_CREATED);
    Vertex taxonomyNode;
    if((isNewlyCreated==null || !isNewlyCreated) && tagValueId != null) {
      taxonomyNode = UtilClass.getVertexByIndexedId(tagValueId, VertexLabelConstants.ENTITY_TAG);
    } else {
      taxonomyNode = AttributionTaxonomyUtil.createAttributionTaxonomyNode(taxonomyMap, getVertexType());
      taxonomyNode.setProperty(IMasterTaxonomy.IS_TAXONOMY, true);
    }
    taxonomyNode.setProperty(IMasterTaxonomy.CHILD_COUNT, 0);

//    CreateKlassUtils.createDefaultTemplateNode(klassTaxonomyMap, klassTaxonomy,parentId, CommonConstants.TAXONOMY_TEMPLATE);

    Map<String, Object> attributionTaxonomy = AttributionTaxonomyUtil.getAttributionTaxonomy(taxonomyNode, getTaxonomyLevelType());
    AuditLogUtils.fillAuditLoginfo(mapToReturn, taxonomyNode, Entities.TAXONOMIES, Elements.MASTER_TAXONOMY_CONFIGURATION_TITLE);
    
    UtilClass.getGraph().commit();
    
    Map<String, Object> configDetails = new HashMap<>();
    mapToReturn.put(IGetMasterTaxonomyWithoutKPModel.ENTITY, attributionTaxonomy);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPModel.CONFIG_DETAILS, configDetails);
    
    KlassUtils.addSectionsToKlassEntityMap(taxonomyNode, attributionTaxonomy, false);
    TaxonomyUtil.fillReferencedAttributeContextDetails(attributionTaxonomy, configDetails);
    TaxonomyUtil.fillReferencedTaxonomies(attributionTaxonomy, configDetails);
    mapToReturn.remove(IKlass.PERMISSIONS);
    
    return mapToReturn;
  }
  
  protected static final List<String> fieldsToExclude = Arrays.asList(
      IMasterTaxonomy.APPLIED_KLASSES, IMasterTaxonomy.CHILDREN,
      IMasterTaxonomy.PARENT, IMasterTaxonomy.SECTIONS,
      ITaxonomy.LINKED_MASTER_TAG_ID, IMasterTaxonomy.LINKED_LEVELS,
      IMasterTaxonomy.TAG_LEVELS, IMasterTaxonomy.DATA_RULES,
      IMasterTaxonomy.EMBEDDED_KLASS_IDS, IMasterTaxonomy.TASKS,
      IMasterTaxonomy.DEFAULT_VALUE, IMasterTaxonomy.TAG_VALUES,
      IMasterTaxonomy.ALLOWED_TAGS, ICreateMasterTaxonomyModel.IS_NEWLY_CREATED,
      ICreateMasterTaxonomyModel.TAG_VALUE_ID, ICreateMasterTaxonomyModel.TAXONOMY_ID,
      ICreateMasterTaxonomyModel.PARENT_TAG_ID,
      ICreateMasterTaxonomyModel.PARENT_TAXONOMY_ID);
}