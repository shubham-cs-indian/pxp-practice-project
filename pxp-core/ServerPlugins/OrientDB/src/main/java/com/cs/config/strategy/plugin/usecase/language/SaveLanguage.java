package com.cs.config.strategy.plugin.usecase.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.language.util.LanguageUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class SaveLanguage extends AbstractOrientPlugin {
  
  public SaveLanguage(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveLanguage/*" };
  }
  
  private static final List<String> fieldsToExclude = Arrays.asList(ILanguage.CHILDREN,
      ILanguage.PARENT, ILanguage.CODE, ILanguage.TYPE);
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get(ILanguageModel.ID);
    Map<String, Object> mapToReturn = new HashMap<>();
    Vertex language = null;
    
    try {
      language = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.LANGUAGE);
    }
    catch (NotFoundException e) {
      throw new LanguageNotFoundException(e);
    }
    
    updateDefaultLanguage(requestMap);
    Vertex saveNode = UtilClass.saveNode(requestMap, language, fieldsToExclude);
    
    Boolean defaultLanguage = (Boolean) saveNode.getProperty(ILanguage.IS_DEFAULT_LANGUAGE);
    if (defaultLanguage) {
      saveNode.setProperty(ILanguage.IS_DATA_LANGUAGE, true);
      saveNode.setProperty(ILanguage.IS_USER_INTERFACE_LANGUAGE, true);
    }
    AuditLogUtils.fillAuditLoginfo(mapToReturn, saveNode, Entities.LANGUAGE_TREE, Elements.UNDEFINED);
    List<Map<String, Object>> children = new ArrayList<>();
    Iterable<Vertex> childrenVertices = language.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex child : childrenVertices) {
      Map<String, Object> childrenMap = UtilClass.getMapFromVertex(
          Arrays.asList(ILanguage.LABEL, CommonConstants.CODE_PROPERTY, ILanguage.CODE), child);
      children.add(childrenMap);
    }
    
    Map<String, Object> languageMap = UtilClass.getMapFromVertex(new ArrayList<>(), saveNode);
    languageMap.put(ILanguage.CHILDREN, children);
    mapToReturn.put(IGetLanguageModel.ENTITY, languageMap);
    UtilClass.getGraph()
        .commit();
    return mapToReturn;
  }
  
  public void updateDefaultLanguage(Map<String, Object> requestMap) throws Exception
  {
    Boolean defaultLanguage = (Boolean) requestMap.get(ILanguage.IS_DEFAULT_LANGUAGE);
    if (defaultLanguage) {
      Vertex languageNodeForDefaultLanguage = LanguageUtil.getDefaultLanguageVertex(false);
      if (languageNodeForDefaultLanguage != null) {
        languageNodeForDefaultLanguage.setProperty(ILanguage.IS_DEFAULT_LANGUAGE, false);
      }
    }
  }
}
