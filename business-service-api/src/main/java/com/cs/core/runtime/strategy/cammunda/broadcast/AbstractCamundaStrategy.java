package com.cs.core.runtime.strategy.cammunda.broadcast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.application.impl.EmbeddedProcessApplication;
import org.camunda.bpm.application.impl.EmbeddedProcessApplicationReferenceImpl;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;



public class AbstractCamundaStrategy {
  
  @Autowired
  protected RepositoryService                repositoryService;
  
  @Autowired
  protected ProcessEngine                    processEngine;
  
  @Autowired
  protected IDeleteProcessDefinationStrategy deleteProcessDefinationStrategy;
  
  public static final String                 MODEL_NS = "http://www.omg.org/spec/BPMN/20100524/MODEL";
  public static final String                 DI_NS    = "http://www.omg.org/spec/BPMN/20100524/DI";
  public static final String                 PROCESS  = "process";
  
  protected String getResponseString(InputStream in) throws IOException
  {
    StringBuilder content = new StringBuilder();
    DataInputStream input = new DataInputStream(in);
    BufferedReader d = new BufferedReader(new InputStreamReader(input, "UTF-8"));
    String str;
    while ((str = d.readLine()) != null) {
      content.append(str);
    }
    
    return content.toString();
  }
  
  /**
   * @param bpmnXml
   *          - .bpmn xml file
   * @return BpmnModelInstance
   */
  protected BpmnModelInstance convertBpmnXmlToBpmnModel(String bpmnXml)
  {
    return Bpmn.readModelFromStream(new ByteArrayInputStream(bpmnXml.getBytes()));
  }
  
  /**
   * @param bpmnModel
   *          - Container for DOM objects which is coverted from .bpmn xml
   * @param processName
   *          - Process label
   * @return ProcessDefinition which is a deployment descriptor
   */
  protected ProcessDefinition deployProcessInCamunda(BpmnModelInstance bpmnModel,
      String processName)
  {
    Deployment deployment = repositoryService.createDeployment()
        .addModelInstance(processName + ".bpmn", bpmnModel)
        .deploy();
    ProcessApplicationReference par = new EmbeddedProcessApplicationReferenceImpl(
        new EmbeddedProcessApplication());
    processEngine.getManagementService()
        .registerProcessApplication(deployment.getId(), par);
    return repositoryService.createProcessDefinitionQuery()
        .deploymentId(deployment.getId())
        .singleResult();
  }
  
  /**
   * @param root
   *          - root Dom element of xml
   * @param elementName
   *          - Element label
   * @return DemElement with name as "process"
   * @throws Exception
   */
  protected DomElement parseProcessElement(DomElement root, String elementName) throws Exception
  {
    List<DomElement> elements = root.getChildElementsByNameNs(MODEL_NS, elementName);
    if (elements.isEmpty()) {
      throw new Exception("No BPMN Process found in the Workflow : " + elementName);
    }
    if (elements.size() > 1) {
      throw new Exception("Multiple BPMN Processes/Pools found in the Workflow : " + elementName);
    }
    return elements.get(0);
  }
  
  /**
   * @param root
   *          - root DomElement of .bpmn xml
   * @param processElement
   */
  protected void updateProcessNameAndIdForClone(DomElement root, DomElement processElement)
  {
    String newProcessId = "Process_" + UUID.randomUUID()
        .toString();
    String oldProcessId = processElement.getAttribute("id");
    processElement.setAttribute("id", newProcessId);
    if (processElement.getAttribute("name") != null) {
      String cloneProcessName = "Copy_" + processElement.getAttribute("name");
      processElement.setAttribute("name", cloneProcessName);
    }
    List<DomElement> diagramElements = root.getChildElementsByNameNs(DI_NS, "BPMNDiagram");
    List<DomElement> planeElements;
    if (!diagramElements.isEmpty()) {
      planeElements = diagramElements.get(0)
          .getChildElementsByNameNs(DI_NS, "BPMNPlane");
      for (DomElement planeElement : planeElements) {
        if (planeElement.getAttribute("bpmnElement")
            .equals(oldProcessId)) {
          planeElement.setAttribute("bpmnElement", newProcessId);
        }
      }
    }
  }
  
  /**
   * @param process
   * @param processDefinition
   * @throws Exception
   */
  protected void deleteOldProcessDefinition(IProcessEventModel process,
      ProcessDefinition processDefinition) throws Exception
  {
    // ProcessDefination is null when isExecutable property in .bpmn xml toggled
    // to false
    if (processDefinition != null) {
      String oldProcessDefinationId = process.getProcessDefinitionId();
      String deployedProcessDefinitionId = processDefinition.getId();
      if (oldProcessDefinationId != null && oldProcessDefinationId != deployedProcessDefinitionId) {
        List<String> idsList = new ArrayList<>();
        idsList.add(oldProcessDefinationId);
        IIdsListParameterModel idsListModel = new IdsListParameterModel();
        idsListModel.setIds(idsList);
        deleteProcessDefinationStrategy.execute(idsListModel);
      }
      process.setProcessDefinitionId(deployedProcessDefinitionId);
    }
  }
  
}
