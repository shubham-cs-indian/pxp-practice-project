package com.cs.config.strategy.plugin.usecase.smartdocument.template;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DeleteSmartDocumentTemplate extends AbstractOrientPlugin {
  
  public DeleteSmartDocumentTemplate(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteSmartDocumentTemplate/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String templateIdToDelete = (String) requestMap.get(IIdParameterModel.ID);
    List<String> idsToDelete = Arrays.asList(templateIdToDelete);
    List<String> successIds = new ArrayList<String>();
    for (String id : idsToDelete) {
      successIds.add(id);
      Vertex smartDocTemplate = UtilClass.getVertexById(id,
          VertexLabelConstants.SMART_DOCUMENT_TEMPLATE);
      Iterator<Vertex> iterator = smartDocTemplate
          .getVertices(Direction.OUT,
              RelationshipLabelConstants.SMART_DOCUMENT_TEMPLATE_PRESET_LINK)
          .iterator();
      Vertex childOfTemplate = null;
      while (iterator.hasNext()) {
        childOfTemplate = iterator.next();
        childOfTemplate.remove();
      }
      smartDocTemplate.remove();
    }
    UtilClass.getGraph()
        .commit();
    HashMap<String, Object> response = new HashMap<>();
    response.put(IBulkDeleteReturnModel.SUCCESS, successIds);
    return response;
  }
}
