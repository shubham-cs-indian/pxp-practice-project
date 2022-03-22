package com.cs.config.strategy.plugin.usecase.template;

import com.cs.config.strategy.plugin.usecase.template.util.CustomTemplateUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.exception.template.TemplateNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class GetTemplate extends AbstractOrientPlugin {
  
  public GetTemplate(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get(ITemplate.ID);
    Vertex templateNode = null;
    try {
      templateNode = UtilClass.getVertexById(id, VertexLabelConstants.TEMPLATE);
    }
    catch (NotFoundException e) {
      throw new TemplateNotFoundException(e);
    }
    return CustomTemplateUtil.prepareTemplateResponseMap(templateNode);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTemplate/*" };
  }
}
