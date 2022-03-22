package com.cs.config.strategy.plugin.usecase.smartdocument.preset;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteSmartDocumentPreset extends AbstractOrientPlugin {
  
  public DeleteSmartDocumentPreset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteSmartDocumentPreset/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<>();
    OrientBaseGraph graph = UtilClass.getGraph();
    String smartDocumentPresetIdToDelete = (String) requestMap.get(IIdParameterModel.ID);
    List<String> deletedIds = new ArrayList<>();
    
    List<String> smartDocumentPresetIdsToDelete = Arrays.asList(smartDocumentPresetIdToDelete);
    
    for (String smartDocumentPresetId : smartDocumentPresetIdsToDelete) {
      Vertex presetNode = UtilClass.getVertexByIndexedId(smartDocumentPresetId,
          VertexLabelConstants.SMART_DOCUMENT_PRESET);
      if (presetNode != null) {
        presetNode.remove();
        deletedIds.add(smartDocumentPresetId);
      }
    }
    UtilClass.getGraph()
        .commit();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, deletedIds);
    return responseMap;
  }
}
