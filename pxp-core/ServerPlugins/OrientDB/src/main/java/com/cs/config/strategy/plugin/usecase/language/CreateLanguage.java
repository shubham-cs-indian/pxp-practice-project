package com.cs.config.strategy.plugin.usecase.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.language.ICreateLanguageModel;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.ParentLanguageNotFoundException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateLanguage extends AbstractOrientPlugin {
  
  public CreateLanguage(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateLanguage/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String parentId = (String) requestMap.remove(ICreateLanguageModel.PARENT_ID);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.LANGUAGE);
    Map<String, Object> mapToReturn = new HashMap<>();
    
    Vertex language = UtilClass.createNode(requestMap, vertexType, new ArrayList<>());
    manageParentLanguage(parentId, language);
    AuditLogUtils.fillAuditLoginfo(mapToReturn, language, Entities.LANGUAGE_TREE, Elements.UNDEFINED);
    
    Map<String, Object> languageMap = UtilClass.getMapFromVertex(new ArrayList<>(), language);
    
    mapToReturn.put(IGetLanguageModel.ENTITY, languageMap);
    return mapToReturn;
  }
  
  private void manageParentLanguage(String parentId, Vertex vertex) throws Exception
  {
    if (parentId == null || parentId.isEmpty() || parentId.equals("-1")) {
      return;
    }
    
    Vertex parentVertex = null;
    try {
      parentVertex = UtilClass.getVertexByIndexedId(parentId, VertexLabelConstants.LANGUAGE);
    }
    catch (NotFoundException e) {
      throw new ParentLanguageNotFoundException(e);
    }
    
    vertex.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentVertex);
  }
}
