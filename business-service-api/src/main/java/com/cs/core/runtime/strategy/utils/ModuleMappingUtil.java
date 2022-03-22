package com.cs.core.runtime.strategy.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.base.interactor.model.portal.IPortalModel;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.entity.module.IScreen;
import com.cs.core.config.interactor.entity.module.IScreenModuleMapping;
import com.cs.core.config.interactor.entity.module.ScreenModuleMapping;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ModuleMappingUtil {
  
  @Autowired
  protected TransactionThreadData transactionThreadData;
  
  @Autowired
  protected List<IPortalModel>    portalModels;
  
  public IScreenModuleMapping getScreenModuleMapping(String portalId, String physicalCatalogId)
      throws Exception
  {
    IScreenModuleMapping screenModuleMapping = null;
    String fileName = null;
    TransactionData transactionData = transactionThreadData.getTransactionData();
    if (CommonConstants.OFFBOARDING_ENDPOINT.equals(transactionData.getEndpointType())
        && physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      fileName = "OffboardingPIMModulesMapping.json";
    }
    else if (CommonConstants.ONBOARDING_ENDPOINT.equals(transactionData.getEndpointType())
        || physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      fileName = "OnboardingPIMModulesMapping.json";
    }
    else if (!physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      fileName = "CentralPIMModulesMapping.json";
    }
    
    InputStream stream = ModuleMappingUtil.class.getClassLoader()
        .getResourceAsStream(fileName);
    InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
    screenModuleMapping = ObjectMapperUtil.readValue(inputStreamReader,
        new TypeReference<ScreenModuleMapping>()
        {
          
        });
    
    return screenModuleMapping;
  }
  
  public IScreenModuleMapping getScreenModuleMappingForCurrentUser(
      List<String> allowedPhysicalCatalogIds, List<String> allowedPortalIds) throws Exception
  {
    TransactionData transactionData = transactionThreadData.getTransactionData();
    String portalId = transactionData.getPortalId();
    String physicalCatalogId = transactionData.getPhysicalCatalogId();
    // Take Default Allowed Portal/Physical Catalog Id if Empty (PIM Preferred)
    if (portalId.isEmpty()) {
      portalId = allowedPortalIds.contains(Constants.PIM_PORTAL_ID) ? Constants.PIM_PORTAL_ID
          : allowedPortalIds.get(0);
    }
    
    if (physicalCatalogId.isEmpty()) {
      physicalCatalogId = allowedPhysicalCatalogIds.contains(Constants.PIM_CATALOG_IDS)
          ? Constants.PIM_CATALOG_IDS
          : allowedPhysicalCatalogIds.get(0);
    }
    
    return getScreenModuleMapping(portalId, physicalCatalogId);
  }
  
  public List<IModule> getRuntimeModule(IScreenModuleMapping screenModuleMapping) throws Exception
  {
    List<IModule> runtimeModules = new ArrayList<>();
    List<IScreen> screens = screenModuleMapping.getScreens();
    for (IScreen screen : screens) {
      if (screen.getId()
          .equals("runtime")) {
        runtimeModules = screen.getModules();
        break;
      }
    }
    
    Set<String> portalAllowedEntities = new HashSet<>(); 
    for (IPortalModel iPortalModel : portalModels) {
      portalAllowedEntities.addAll(iPortalModel.getAllowedEntities());
    }
    for (IModule runtimeModule : runtimeModules) {
      ArrayList<String> moduleEntities = new ArrayList<String>();
      for (String entity : runtimeModule.getEntities()) {
        if(portalAllowedEntities.contains(entity)) {
          moduleEntities.add(entity);
        }
      }
      runtimeModule.setEntities(moduleEntities);
    }
    
    return runtimeModules;
  }
  
  public List<IModule> getRuntimeModule() throws Exception
  {
    TransactionData transactionData = transactionThreadData.getTransactionData();
    IScreenModuleMapping screenModuleMapping = getScreenModuleMapping(transactionData.getPortalId(),
        transactionData.getPhysicalCatalogId());
    return getRuntimeModule(screenModuleMapping);
  }
  
  public IScreenModuleMapping getScreenModuleMappingToReturnForCurrentUser(
      List<String> allowedEntities, List<String> allowedPhysicalCatalogIds,
      List<String> allowedPortalIds, Boolean isDashboardEnable, String landingScreen)
      throws Exception
  {
    IScreenModuleMapping screenModuleMapping = getScreenModuleMappingForCurrentUser(
        allowedPhysicalCatalogIds, allowedPortalIds);
    List<IModule> modules = getRuntimeModule(screenModuleMapping);
    
    ArrayList<IModule> modulesToReturn = new ArrayList<>();
    for (IModule module : modules) {
      if (module.getId()
          .equals(CommonConstants.DASHBOARD) && !isDashboardEnable) {
        continue;
      }
      List<String> moduleEntities = module.getEntities();
      moduleEntities.retainAll(allowedEntities);
      if (moduleEntities.size() > 0 || module.getId()
          .equals("josstatus") || module.getId()
              .equals("filesmodule")) {
        modulesToReturn.add(module);
      }
    }
    
    Boolean isDataIntegrationDefault = (allowedPhysicalCatalogIds.size() == 1
        && allowedPhysicalCatalogIds.contains(Constants.DATA_INTEGRATION_CATALOG_IDS));
    TransactionData transactionData = transactionThreadData.getTransactionData();
    if (transactionData.getPhysicalCatalogId()
        .equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      isDataIntegrationDefault = false;
    }
    
    List<IScreen> screens = screenModuleMapping.getScreens();
    for (IScreen screen : screens) {
      if (screen.getId()
          .equals("runtime")) {
        
        screen.setModules(modulesToReturn);
        // Enters this condition if the user is System Admin
        if (isDataIntegrationDefault) {
          screen.setIsVisible(false);
        }
        /* Enters this condition if default screen is Content Screen or
        Dashboard is disabled for user*/
        /*else if (landingScreen.equals("explore") || !isDashboardEnable) {
          screen.setIsDefault(true);
        }*/
      }
    }
    return screenModuleMapping;
  }
  
  public IModule getModule(String moduleId) throws Exception
  {
    List<IModule> modules = getRuntimeModule();
    
    for (IModule iModule : modules) {
      if (iModule.getId()
          .equals(moduleId)) {
        
        return iModule;
      }
    }
    return null;
  }
}
