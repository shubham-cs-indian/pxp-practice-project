package com.cs.config.strategy.plugin.usecase.template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.template.util.CustomTemplateUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateCustomTemplate extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToExclude = Arrays.asList(
      ICustomTemplateModel.NATURE_RELATIONSHIP_IDS, ICustomTemplateModel.RELATIONSHIP_IDS,
      ICustomTemplateModel.PROPERTY_COLLECTION_IDS, ICustomTemplateModel.CONTEXT_IDS);
  
  public CreateCustomTemplate(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateCustomTemplate/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.TEMPLATE,
        CommonConstants.CODE_PROPERTY);
    Map<String, Object> returnMap = new HashMap<String, Object>();

    Vertex templateNode = UtilClass.createNode(requestMap, vertexType, fieldsToExclude);
    
    UtilClass.getGraph()
        .commit();
    returnMap = CustomTemplateUtil.prepareTemplateResponseMap(templateNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, templateNode, Entities.TEMPLATES, Elements.UNDEFINED);

    return returnMap;
  }
}
