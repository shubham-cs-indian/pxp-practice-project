package com.cs.config.strategy.plugin.usecase.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.CurrentLanguageCannotBeDeleted;
import com.cs.core.runtime.interactor.exception.language.DefaultLanguageCannotBeDeleted;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class DeleteLanguage extends AbstractOrientPlugin {
  
  public DeleteLanguage(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteLanguage/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<>();
    IExceptionModel failure = new ExceptionModel();
    List<String> idsToDelete = (List<String>) requestMap.get(IDeleteLanguageRequestModel.IDS);
    String currentDataLanguage = (String) requestMap.get(IDeleteLanguageRequestModel.DATA_LANGUAGE);
    String currentUiLanguage = (String) requestMap.get(IDeleteLanguageRequestModel.UI_LANGUAGE);
    Iterable<Vertex> vertices = UtilClass.getVerticesByIndexedIds(idsToDelete,
        VertexLabelConstants.LANGUAGE);
    
    List<String> idsDeletedSuccessfully = new ArrayList<>();
    List<String> finalDefaultLanguageIds = new ArrayList<>();
    List<String> finalCurrentLanguageIds = new ArrayList<>();
    
    for (Vertex vertex : vertices) {
      if(ValidationUtils.vaildateIfStandardEntity(vertex))
        continue;
      String rid = vertex.getId()
          .toString();
      String parentLanguageId = UtilClass.getCodeNew(vertex);
      String query = "select from(traverse in('Child_Of') from " + rid + "strategy BREADTH_FIRST)";
      Iterable<Vertex> languageNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      List<String> defaultLanguageIds = new ArrayList<>();
      List<String> currentLanguageIds = new ArrayList<>();
      List<Vertex> verticesToDelete = new ArrayList<>();
      
      for (Vertex child : languageNodes) {
        String id = UtilClass.getCodeNew(child);
        String languageCode = (String) child.getProperty(ILanguage.CODE);
        if ((Boolean) child.getProperty(ILanguage.IS_DEFAULT_LANGUAGE)) {
          defaultLanguageIds.add(id);
          finalDefaultLanguageIds.add(id);
          idsToDelete.remove(parentLanguageId);
          ExceptionUtil.addFailureDetailsToFailureObject(failure,
              new DefaultLanguageCannotBeDeleted(), id, null);
        }
        else if (languageCode.equals(currentUiLanguage)
            || languageCode.equals(currentDataLanguage)) {
          currentLanguageIds.add(id);
          finalCurrentLanguageIds.add(id);
          idsToDelete.remove(parentLanguageId);
          ExceptionUtil.addFailureDetailsToFailureObject(failure,
              new CurrentLanguageCannotBeDeleted(), id, null);
        }
        else {
          verticesToDelete.add(child);
        }
      }
      
      if (defaultLanguageIds.isEmpty() && currentLanguageIds.isEmpty()) {
        for (Vertex childToDelete : verticesToDelete) {
          childToDelete.remove();
          idsDeletedSuccessfully.add(UtilClass.getCodeNew(childToDelete));
        }
        AuditLogUtils.fillAuditLoginfo(responseMap, vertex, Entities.LANGUAGE_TREE, Elements.UNDEFINED);
      }
    }
    
    if (idsToDelete.isEmpty()) {
      if (!finalDefaultLanguageIds.isEmpty()) {
        throw new DefaultLanguageCannotBeDeleted();
      }
      else if (!finalCurrentLanguageIds.isEmpty()) {
        throw new CurrentLanguageCannotBeDeleted();
      }
    }
    
    UtilClass.getGraph()
        .commit();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, idsDeletedSuccessfully);
    responseMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    return responseMap;
  }
}
