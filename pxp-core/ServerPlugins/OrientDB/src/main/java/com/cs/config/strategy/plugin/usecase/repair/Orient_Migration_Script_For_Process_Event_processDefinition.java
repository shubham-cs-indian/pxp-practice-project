package com.cs.config.strategy.plugin.usecase.repair;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class Orient_Migration_Script_For_Process_Event_processDefinition
    extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_For_Process_Event_processDefinition(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_For_Process_Event_processDefinition/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, List<String>> result = new HashMap<String, List<String>>();
    List<String> successIds = new ArrayList<String>();
    List<String> failureIds = new ArrayList<String>();
    String query = "select from " + VertexLabelConstants.PROCESS_EVENT;
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex processNode : searchResults) {
      try {
        String processDefinition = processNode.getProperty(IProcessEvent.PROCESS_DEFINITION);
        // Replace all old input parameters with new input parameters
        processDefinition = StringUtils.replace(processDefinition, "\"tagsMap\"",
            "\"Input_tagsMap\"");
        processDefinition = StringUtils.replace(processDefinition, "\"attributesTypeMap\"",
            "\"Input_attributesTypeMap\"");
        processDefinition = StringUtils.replace(processDefinition, "\"attributesValueMap\"",
            "\"Input_attributesValueMap\"");
        processDefinition = StringUtils.replace(processDefinition, "\"setDataLanguage\"",
            "\"Input_setDataLanguage\"");
        processDefinition = StringUtils.replace(processDefinition, "\"attributesValuesToFetch\"",
            "\"Input_attributesValuesToFetch\"");
        processDefinition = StringUtils.replace(processDefinition, "\"tagsValuesToFetch\"",
            "\"Input_tagsValuesToFetch\"");
        processDefinition = StringUtils.replace(processDefinition, "\"instanceId\"",
            "\"Input_instanceId\"");
        processDefinition = StringUtils.replace(processDefinition, "\"relationshipId\"",
            "\"Input_relationshipId\"");
        processDefinition = StringUtils.replace(processDefinition, "\"oppositeInstanceIds\"",
            "\"Input_oppositeInstanceIds\"");
        processDefinition = StringUtils.replace(processDefinition, "\"context\"",
            "\"Input_context\"");
        processDefinition = StringUtils.replace(processDefinition, "\"sourceType\"",
            "\"Input_sourceType\"");
        processDefinition = StringUtils.replace(processDefinition, "\"properties\"",
            "\"Input_properties\"");
        processDefinition = StringUtils.replace(processDefinition, "\"filterModel\"",
            "\"Input_filterModel\"");
        processDefinition = StringUtils.replace(processDefinition, "\"moduleId\"",
            "\"Input_moduleId\"");
        processDefinition = StringUtils.replace(processDefinition, "\"dataLanguage\"",
            "\"Input_dataLanguage\"");
        // add new output parameters
        processDefinition = addOutputParameters(processDefinition);
        // Remove attribute "standalone" which got added while executing above
        // function
        processDefinition = StringUtils.replace(processDefinition, " standalone=\"no\"", "");
        processNode.setProperty(IProcessEvent.PROCESS_DEFINITION, processDefinition);
        successIds.add(processNode.getProperty(IProcessEvent.CODE));
      }
      catch (Exception e) {
        failureIds.add(processNode.getProperty(IProcessEvent.CODE));
      }
    }
    UtilClass.getGraph()
        .commit();
    result.put("successIds", successIds);
    result.put("failureIds", failureIds);
    return result;
  }
  
  private String addOutputParameters(String processDefinition) throws Exception
  {
    Map<String, String[]> outputParams = createOutputAttributeMap();
    // Use method to convert XML string content to XML Document object
    Document doc = convertStringToXMLDocument(processDefinition);
    NodeList bpmnServiceTaskNodes = doc.getElementsByTagName("bpmn:serviceTask");
    for (int count = 0; count < bpmnServiceTaskNodes.getLength(); count++) {
      Node node = bpmnServiceTaskNodes.item(count);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        String delegateExpression = element.getAttribute("camunda:delegateExpression");
        String componentControllerName = delegateExpression
            .substring(delegateExpression.indexOf("{") + 1, delegateExpression.lastIndexOf("}"));
        String[] params = outputParams.get(componentControllerName);
        if (params != null) {
          for (String param : params) {
            addNewXMLElement(param, element, doc);
          }
        }
      }
    }
    // Code to convert xml document back into xml string
    DOMSource domSource = new DOMSource(doc);
    StringWriter writer = new StringWriter();
    StreamResult result = new StreamResult(writer);
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.transform(domSource, result);
    return writer.toString();
  }
  
  private static void addNewXMLElement(String parameter, Element element, Document document)
  {
    NodeList elementsByTagName = element.getElementsByTagName("camunda:inputParameter");
    boolean isNodePresent = false;
    for (int i = 0; i < elementsByTagName.getLength(); i++) {
      Element node = (Element) elementsByTagName.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE && node.hasAttribute("name")
          && node.getAttribute("name")
              .equals(parameter)) {
        isNodePresent = true;
        break;
      }
    }
    if (!isNodePresent) {
      Element newChild = document.createElement("camunda:inputParameter");
      newChild.setAttribute("name", parameter);
      if (elementsByTagName.item(0) != null) {
        elementsByTagName.item(0)
            .getParentNode()
            .appendChild(document.createTextNode("\n"));
        elementsByTagName.item(0)
            .getParentNode()
            .appendChild(newChild);
      }
    }
  }
  
  private static Document convertStringToXMLDocument(String xmlString)
  {
    // Parser that produces DOM object trees from XML content
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
    // API to obtain DOM Document instance
    DocumentBuilder builder = null;
    try {
      // Create DocumentBuilder with default configuration
      builder = factory.newDocumentBuilder();
      
      // Parse the content to Document object
      Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
      return doc;
    }
    catch (Exception e) {
      e.printStackTrace();
      //RDBMSLogger.instance().exception(e);
    }
    return null;
  }
  
  private Map<String, String[]> createOutputAttributeMap()
  {
    Map<String, String[]> outputParams = new HashMap<>();
    outputParams.put("getAttributesAndTagsValueController",
        new String[] { "Output_attributesAndTags", "Output_executionStatus" });
    outputParams.put("getAttributesAndTagsValueForAssetController",
        new String[] { "Output_attributesAndTags", "Output_executionStatus" });
    outputParams.put("setAttributesAndTagsController", new String[] { "Output_executionStatus" });
    outputParams.put("setAttributesAndTagsForAssetController",
        new String[] { "Output_executionStatus" });
    outputParams.put("relationshipInstanceReadController",
        new String[] { "Output_instanceRelationships", "Output_executionStatus" });
    outputParams.put("relationshipInstanceCreateController",
        new String[] { "Output_executionStatus" });
    outputParams.put("relationshipInstanceDeleteController",
        new String[] { "Output_executionStatus" });
    outputParams.put("relationshipInstanceUpdateController",
        new String[] { "Output_executionStatus" });
    outputParams.put("searchContentController",
        new String[] { "Output_contentIds", "Output_executionStatus" });
    return outputParams;
  }
}
