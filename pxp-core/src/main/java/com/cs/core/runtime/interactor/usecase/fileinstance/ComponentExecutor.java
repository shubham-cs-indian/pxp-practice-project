package com.cs.core.runtime.interactor.usecase.fileinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.articleimportcomponent.ComponentModel;
import com.cs.core.config.interactor.model.articleimportcomponent.IComponentModel;
import com.cs.core.config.interactor.model.articleimportcomponent.IOrchestratorInstanceModel;
import com.cs.core.config.interactor.model.articleimportcomponent.IOrchestratorModel;
import com.cs.core.config.interactor.model.articleimportcomponent.IProcessContext;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.usecase.cammunda.IComponent;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("componentExecutor")
@Scope("prototype")
public class ComponentExecutor extends Thread {
  
  public static final String TAG_GROUP            = "PROCESS_STATUS_TAG";
  public static final String TAG_VALUE_COMPLETED  = "PROCESS_STATUS_COMPLETED";
  public static final String TAG_VALUE_INPROGRESS = "PROCESS_STATUS_INPROGRESS";
  public static final String TAG_VALUE_FAILED     = "PROCESS_STATUS_FAILED";
  
  String                     componentId;
  IProcessContext            processContext;
  IOrchestratorInstanceModel orchestratorInstanceModel;
  IOrchestratorModel         componentMap;
  String                     componentInstanceId;
  String                     processInstanceId;
  String                     id;
  String                     boardingType;
  String                     fileNameForExport;
  String                     userId;
  List<String>               selectedIds;
  String                     endpointId;
  String                     systemId;
  String                     physicalCatalogId;
  String                     organizationId;
  String                     logicalCatalogId;
  String                     fileInstanceIdForExport;
  
  @Autowired
  ISessionContext            context;
  
  @Autowired
  ApplicationContext         applicationContext;
  
  /*@Autowired
  ISaveProcessInstanceStrategy saveProcessInstanceStrategy;*/
  
  public void setParameters(String componentId, IProcessContext processContext,
      IOrchestratorInstanceModel orchestratorInstanceModel, String componentInstanceId,
      String processInstanceId, String id, String boardingType, String fileNameForExport,
      String userId, List<String> selectedIds, String endpointId, String systemId,
      String physicalCatalogId, String organizationId, String logicalCatalogId,
      String fileInstanceIdForExport)
  {
    this.componentId = componentId;
    this.processContext = processContext;
    this.orchestratorInstanceModel = orchestratorInstanceModel;
    this.componentInstanceId = componentInstanceId;
    this.processInstanceId = processInstanceId;
    this.componentMap = orchestratorInstanceModel.getEvents()
        .get(componentInstanceId);
    this.id = id;
    this.boardingType = boardingType;
    this.fileNameForExport = fileNameForExport;
    this.userId = userId;
    this.selectedIds = selectedIds;
    this.endpointId = endpointId;
    this.systemId = systemId;
    this.physicalCatalogId = physicalCatalogId;
    this.organizationId = organizationId;
    this.logicalCatalogId = logicalCatalogId;
    this.fileInstanceIdForExport = fileInstanceIdForExport;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void run()
  {
    context.setUserId(userId);
    synchronized (this) {
      IComponentModel componentModel = new ComponentModel();
      componentModel.setFileNameForExport(fileNameForExport);
      componentModel.setProcessContext(processContext);
      componentModel.setParameters(componentMap.getComponentObject());
      componentModel.setComponentId(componentId);
      componentModel.setComponentInstanceId(componentInstanceId);
      componentModel.setProcessInstanceId(processInstanceId);
      componentModel.setId(id);
      componentModel.setIds(this.selectedIds);
      componentModel.setEndpointId(endpointId);
      componentModel.setSystemId(systemId);
      componentModel.setPhysicalCatalogId(physicalCatalogId);
      componentModel.setOrganizationId(organizationId);
      componentModel.setLogicalCatalogId(logicalCatalogId);
      componentModel.setFileInstanceIdForExport(fileInstanceIdForExport);
      
      IComponent component = null;
      String beanName = null;
      if (boardingType.equals(CommonConstants.ONBOARDING_PROCESS)
          || boardingType.equals(ProcessConstants.DI_ONBOARDING_PROCESS)) {
        beanName = getOnbaordingBeanName(componentId);
      }
      else {
        beanName = getOffboardingBeanName(componentId);
      }
      
      if (beanName != null) {
        component = (IComponent) applicationContext.getBean(beanName);
        try {
          component.execute(componentModel);
          orchestratorInstanceModel.getEvents()
              .get(componentInstanceId)
              .setComponentStatus(true);
        }
        catch (Exception e) {
          orchestratorInstanceModel.getEvents()
              .get(componentInstanceId)
              .setFailedStatus(true);
          orchestratorInstanceModel.setRunningStatus(false);
          e.printStackTrace();
          RDBMSLogger.instance().exception(e);
        }
      }
      else {
        System.out.println("*** Component bean name not found...");
        orchestratorInstanceModel.getEvents()
            .get(componentInstanceId)
            .setFailedStatus(true);
        orchestratorInstanceModel.setRunningStatus(false);
      }
      
      this.notify();
    }
  }
  
  private String getOnbaordingBeanName(String componentId)
  {
    switch (componentId) {
      case "Article_Import":
        return "articleInstanceImportComponent";
      case "Assets_Import":
        return "assetImportComponent";
      case "Extended_Article_Import":
        return "articleInstanceMulticlassificationImportComponent";
      case "Relationships_Import":
        return "relationshipImportComponent";
      case "Nature_Relationships_Import":
        return "naturerRelationshipImportComponent";
      case "Market_Import":
        return "marketInstanceImportComponent";
      case "TAM_Import":
        return "textAssetInstanceImportComponent";
      case "Supplier_Import":
        return "supplierInstanceImportComponent";
      case "Extended_Market_Import":
        return "marketInstanceMulticlassificationImportComponent";
      case "Extended_Assets_Import":
        return "assetInstanceMulticlassificationImportComponent";
      case "Extended_TAM_Import":
        return "textAssetInstanceMulticlassificationImportComponent";
      case "Extended_Supplier_Import":
        return "supplierInstanceMulticlassificationImportComponent";
      case "Article_Variant_Import":
        return "articleInstanceVariantImportComponent";
      case "Transfer_Component":
        return "transferToDestinationComponent";
      case "Di_Save_Article":
        return "articleDataDiComponent";
      case "Di_Save_Article_Variant":
        return "saveArticleDataDiComponent";
      case "Jms_Consumer":
        return "jmsConsumerDiComponent";
      case "Di_Talend_Component":
        return "talendDiComponent";
      default:
        return null;
    }
  }
  
  private String getOffboardingBeanName(String componentId)
  {
    switch (componentId) {
      case "Article_Import":
        return "articleInstancesOffboardingComponent";
      case "Assets_Import":
        return "assetInstanceOffboardingComponent";
      case "Relationships_Import":
        return "relationshipInstancesOffboardingComponent";
      case "Nature_Relationships_Import":
        return "naturerelationshipInstancesOffboardingComponent";
      case "Market_Import":
        return "marketInstancesOffboardingComponent";
      case "TAM_Import":
        return "textAssetInstancesOffboardingComponent";
      case "Di_Talend_Component":
        return "talendDiComponent";
      case "Supplier_Import":
        return "supplierInstancesOffboardingComponent";
      case "Jms_Producer":
        return "jmsProducerDiComponent";
      case "Article_Variant_Import":
        return "klassInstanceVariantExportComponent";
      case "Extended_Article_Import":
        return "extendedArticleInstancesOffboardingComponent";
      case "Extended_Market_Import":
        return "extendedMarketInstancesOffboardingComponent";
      case "Extended_Assets_Import":
        return "extendedAssetInstanceOffboardingComponent";
      case "Extended_TAM_Import":
        return "extendedTextAssetInstancesOffboardingComponent";
      case "Extended_Supplier_Import":
        return "extendedSupplierInstancesOffboardingComponent";
      case "Transfer_Component":
        return "transferToDestinationComponent";
      default:
        return null;
    }
  }
}
