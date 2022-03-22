package com.cs.config.strategy.plugin.usecase.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.template.util.CustomTemplateUtil;
import com.cs.config.strategy.plugin.usecase.template.util.TemplateUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.template.BulkSaveTemplateFailedException;
import com.cs.core.config.interactor.exception.template.TemplateNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.template.IBulkSaveTemplatesResponseModel;
import com.cs.core.config.interactor.model.template.IBulkSaveTemplatesSuccessModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class BulkSaveTemplate extends AbstractOrientPlugin {
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveTemplate/*" };
  }
  
  public BulkSaveTemplate(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfTemplates = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSavedTemplates = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();

    Map<String, Object> configDetails = new HashMap<>();
    Map<String, Object> referencedPCs = new HashMap<>();
    Map<String, Object> referencedRelationships = new HashMap<>();
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> referencedContexts = new HashMap<>();
    configDetails.put(IGetCustomTemplateModel.REFERENCED_PROPERTY_COLLECTIONS, referencedPCs);
    configDetails.put(IGetCustomTemplateModel.REFERENCED_RELATIONSHIPS, referencedRelationships);
    configDetails.put(IGetCustomTemplateModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    configDetails.put(IGetCustomTemplateModel.REFERENCED_CONTEXT, referencedContexts);
    
    for (Map<String, Object> saveTemplateMap : listOfTemplates) {
      try {
        String templateId = (String) saveTemplateMap.get(CommonConstants.ID_PROPERTY);
        
        Vertex templateNode = null;
        try {
          templateNode = UtilClass.getVertexById(templateId, VertexLabelConstants.TEMPLATE);
        }
        catch (NotFoundException e) {
          throw new TemplateNotFoundException();
        }
        
        UtilClass.saveNode(saveTemplateMap, templateNode, fieldsToExclude);
        AuditLogUtils.fillAuditLoginfo(auditLogInfoList, templateNode, Entities.TEMPLATES, Elements.UNDEFINED);

        TemplateUtils.manageAddedPropertyCollections(templateNode, saveTemplateMap);
        TemplateUtils.manageDeletedPropertyCollections(templateNode, saveTemplateMap);
        TemplateUtils.manageAddedRelationships(templateNode, saveTemplateMap);
        TemplateUtils.manageDeletedRelationships(templateNode, saveTemplateMap);
        TemplateUtils.manageAddedNatureRelationships(templateNode, saveTemplateMap);
        TemplateUtils.manageDeletedNatureRelationships(templateNode, saveTemplateMap);
        TemplateUtils.manageAddedContexts(templateNode, saveTemplateMap);
        TemplateUtils.manageDeletedContexts(templateNode, saveTemplateMap);
        
        CustomTemplateUtil.prepareTemplateResponseForGridTemplatesMap(templateNode, configDetails,
            listOfSuccessSavedTemplates);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    
    if (listOfSuccessSavedTemplates.isEmpty()) {
      throw new BulkSaveTemplateFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IBulkSaveTemplatesSuccessModel.LIST, listOfSuccessSavedTemplates);
    successMap.put(IBulkSaveTemplatesSuccessModel.CONFIG_DETAILS, configDetails);
    
    Map<String, Object> bulkSaveTemplatesResponse = new HashMap<String, Object>();
    bulkSaveTemplatesResponse.put(IBulkSaveTemplatesResponseModel.SUCCESS, successMap);
    bulkSaveTemplatesResponse.put(IBulkSaveTemplatesResponseModel.FAILURE, failure);
    bulkSaveTemplatesResponse.put(IBulkSaveTemplatesResponseModel.AUDIT_LOG_INFO, auditLogInfoList);
    return bulkSaveTemplatesResponse;
  }
  
  protected List<String> fieldsToExclude = Arrays.asList(
      ISaveCustomTemplateModel.ADDED_PROPERTYCOLLECTIONS,
      ISaveCustomTemplateModel.DELETED_PROPERTYCOLLECTIONS,
      ISaveCustomTemplateModel.ADDED_RELATIONSHIPS, ISaveCustomTemplateModel.DELETED_RELATIONSHIPS,
      ISaveCustomTemplateModel.ADDED_NATURE_RELATIONSHIPS,
      ISaveCustomTemplateModel.DELETED_NATURE_RELATIONSHIPS,
      ISaveCustomTemplateModel.DELETED_CONTEXTS, ISaveCustomTemplateModel.ADDED_CONTEXTS);
  }
