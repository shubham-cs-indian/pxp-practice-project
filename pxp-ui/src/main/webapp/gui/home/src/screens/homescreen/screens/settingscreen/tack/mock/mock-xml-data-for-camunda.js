import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';

let sXMLProcessId = UniqueIdentifierGenerator.generateUUID();
let sDelegateExpression = '${camundaTask}';
export default function () {
  return [
    {
      id: "forCreate",
      xmlData:  `<?xml version="1.0" encoding="UTF-8"?>
                  <bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" id="Definitions_${sXMLProcessId}" targetNamespace="http://bpmn.io/schema/bpmn" exporter="Camunda Modeler" exporterVersion="1.11.2">
                    <bpmn:process id="Process_${sXMLProcessId}" isExecutable="false">
                      <bpmn:startEvent id="StartEvent_${sXMLProcessId}" camunda:asyncAfter="true">
                      <bpmn:extensionElements>
                        <camunda:executionListener event="start" delegateExpression="${sDelegateExpression}"/>
                      </bpmn:extensionElements>
                     </bpmn:startEvent>
                    </bpmn:process>
                    <bpmndi:BPMNDiagram id="BPMNDiagram_${sXMLProcessId}">
                      <bpmndi:BPMNPlane id="BPMNPlane_${sXMLProcessId}" bpmnElement="Process_${sXMLProcessId}">
                        <bpmndi:BPMNShape id="_BPMNShape_StartEvent_${sXMLProcessId}" bpmnElement="StartEvent_${sXMLProcessId}">
                          <dc:Bounds x="299" y="91" width="36" height="36" />
                          <bpmndi:BPMNLabel>
                            <dc:Bounds x="272" y="127" width="90" height="20" />
                          </bpmndi:BPMNLabel>
                        </bpmndi:BPMNShape>
                      </bpmndi:BPMNPlane>
                    </bpmndi:BPMNDiagram>
                  </bpmn:definitions>`
    },
    {
      id: "forScheduledWorkflows",
      xmlData: `<?xml version="1.0" encoding="UTF-8"?>
                <bpmn:definitions
                    xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL"
                    xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
                    xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xmlns:camunda="http://camunda.org/schema/1.0/bpmn" id="Definitions_${sXMLProcessId}" targetNamespace="http://bpmn.io/schema/bpmn" exporter="Camunda Modeler" exporterVersion="1.11.2">
                    <bpmn:process id="Process_${sXMLProcessId}" isExecutable="false">
                        <bpmn:startEvent id="StartEvent_${sXMLProcessId}" camunda:asyncAfter="true">
                            <bpmn:extensionElements>
                              <camunda:executionListener event="start" delegateExpression="${sDelegateExpression}"/>
                            </bpmn:extensionElements>
                            <bpmn:timerEventDefinition />
                        </bpmn:startEvent>
                    </bpmn:process>
                    <bpmndi:BPMNDiagram id="BPMNDiagram_${sXMLProcessId}">
                        <bpmndi:BPMNPlane id="BPMNPlane_${sXMLProcessId}" bpmnElement="Process_${sXMLProcessId}">
                            <bpmndi:BPMNShape id="_BPMNShape_StartEvent_${sXMLProcessId}" bpmnElement="StartEvent_${sXMLProcessId}">
                                <dc:Bounds x="299" y="91" width="36" height="36" />
                                <bpmndi:BPMNLabel>
                                    <dc:Bounds x="272" y="127" width="0" height="12" />
                                </bpmndi:BPMNLabel>
                            </bpmndi:BPMNShape>  
                        </bpmndi:BPMNPlane>
                    </bpmndi:BPMNDiagram>
                </bpmn:definitions>`
    },
  ];
};