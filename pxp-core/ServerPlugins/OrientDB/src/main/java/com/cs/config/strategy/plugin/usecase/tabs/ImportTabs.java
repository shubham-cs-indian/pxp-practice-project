package com.cs.config.strategy.plugin.usecase.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.IterableUtils;

import com.cs.config.idto.IConfigTabDTO;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.InvalidTabSequenceException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ImportTabs extends AbstractOrientPlugin{

  public ImportTabs(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  public static final List<String> fieldToExclude = Arrays.asList(IConfigTabDTO.SEQUENCE);
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ImportTabs/*" };
  }

  @Override protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> tabs = (List<Map<String, Object>>) requestMap.get("list");

    List<Map<String, Object>> createdTabs = new ArrayList<>();
    List<Map<String, Object>> failedTabs = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    for (Map<String, Object> tab : tabs) {
      try {
        upsertTab(tab);
        Map<String, Object> createdTab = new HashMap<>();
        createdTab.put(ISummaryInformationModel.ID, tab.get(IConfigTabDTO.CODE));
        createdTab.put(ISummaryInformationModel.LABEL, tab.get(IConfigTabDTO.LABEL));
        createdTabs.add(createdTab);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, (String) tab.get(IConfigTabDTO.CODE), (String) tab.get(IConfigTabDTO.LABEL));
        addToFailureIds(failedTabs, tab);
      }
    }
    UtilClass.getGraph().commit();

    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdTabs);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedTabs);
    return result;
  }

  private void upsertTab(Map<String, Object> tab) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String code = (String) tab.get(ITab.CODE);
    Iterable<Vertex> vertices = graph.getVertices(VertexLabelConstants.TAB, new String[] { CommonConstants.CODE_PROPERTY }, new String[] { code });
    Iterator<Vertex> iterator = vertices.iterator();
    //validate icon
    UtilClass.validateIconExistsForImport(tab);
    if (iterator.hasNext()) {
      Vertex tabNode = iterator.next();
      updateTabSequence(tab);
      UtilClass.saveNode(tab, tabNode, fieldToExclude);
      manageHasTabEdges(tabNode);
    }
    else {
      Vertex tabNode = TabUtils.createTabNode(tab, fieldToExclude);
      manageHasTabEdges(tabNode);
    }
  }

  private void manageHasTabEdges(Vertex tabNode)
  {
    List<String> propertySequence = tabNode.getProperty(IConfigTabDTO.PROPERTY_SEQUENCE_LIST);
    List<String> edgesToCreate = new ArrayList<>(propertySequence);
    Iterable<Edge> tabEdges = tabNode.getEdges(Direction.IN, RelationshipLabelConstants.HAS_TAB);

    //remove unrequired edges
    for(Edge tabEdge  : tabEdges) {
      Vertex vertex = tabEdge.getVertex(Direction.OUT);
      String code = UtilClass.getCode(vertex);
      if(!propertySequence.contains(code)){
        propertySequence.add(code);
      }
      else{
        edgesToCreate.remove(code);
      }
    }
    propertySequence.removeAll(edgesToCreate);
/*    //create edges
    List<Vertex> entities = IterableUtils.toList(UtilClass.getVerticesByIds(edgesToCreate, VertexLabelConstants.VARIANT_CONTEXT));
    entities.addAll(IterableUtils.toList(UtilClass.getVerticesByIds(edgesToCreate, VertexLabelConstants.PROPERTY_COLLECTION)));
    entities.addAll(IterableUtils.toList(UtilClass.getVerticesByIds(edgesToCreate, VertexLabelConstants.ROOT_RELATIONSHIP)));
    List<String> presentPropertyEntities = new ArrayList<>();
    for(Vertex entity : entities){
      Iterable<Edge> edges = entity.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TAB);
      //if this element already has a tab, then it should be removed.
      for(Edge edge : edges){
        edge.remove();
      }
      entity.addEdge(RelationshipLabelConstants.HAS_TAB, tabNode);
      presentPropertyEntities.add(entity.getProperty(ITab.CODE));
    }

    propertySequence.retainAll(presentPropertyEntities);*/
    tabNode.setProperty(ITab.PROPERTY_SEQUENCE_LIST, propertySequence);
  }

  public void addToFailureIds(List<Map<String, Object>> failedTabs, Map<String, Object> tab)
  {
    Map<String, Object> failedtaskMap = new HashMap<>();
    failedtaskMap.put(ISummaryInformationModel.ID, tab.get(IConfigTabDTO.CODE));
    failedtaskMap.put(ISummaryInformationModel.LABEL, tab.get(IConfigTabDTO.LABEL));
    failedTabs.add(failedtaskMap);
  }

  private void updateTabSequence(Map<String, Object> tab) throws Exception
  {
    String tabId = (String) tab.get(IConfigTabDTO.CODE);
    Integer newTabSequence = (Integer) tab.get(IConfigTabDTO.SEQUENCE);
    if (newTabSequence == 0) {
      throw new InvalidTabSequenceException();
    }
    Integer tabSequence = TabUtils.getTabSequence(tabId);
    if (newTabSequence != null && !newTabSequence.equals(tabSequence)) {
      TabUtils.updateTabSequence(tabId, newTabSequence);
      tabSequence = newTabSequence;
    }
  }
}
