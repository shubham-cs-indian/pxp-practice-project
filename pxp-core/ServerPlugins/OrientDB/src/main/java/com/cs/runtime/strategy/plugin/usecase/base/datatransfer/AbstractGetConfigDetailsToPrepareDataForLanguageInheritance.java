package com.cs.runtime.strategy.plugin.usecase.base.datatransfer;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.language.IGetConfigDetailsToPrepareDataForLanguageInheritanceModel;
import com.cs.core.config.interactor.model.language.ILanguageHierarchyModel;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetConfigDetailsToPrepareDataForLanguageInheritance
    extends AbstractConfigDetails {
  
  public AbstractGetConfigDetailsToPrepareDataForLanguageInheritance(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> languageCodes = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    
    Map<String, Object> mapToReturn = getMapToReturn(languageCodes);
    return mapToReturn;
  }
  
  private Map<String, Object> getMapToReturn(List<String> languageCodes) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedLanguages = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsToPrepareDataForLanguageInheritanceModel.REFERENCED_LANGUAGES,
        referencedLanguages);
    fillLanguageHierarchyMap(languageCodes, referencedLanguages);
    return mapToReturn;
  }
  
  private void fillLanguageHierarchyMap(List<String> languageCodes,
      Map<String, Object> referencedLanguages) throws Exception
  {
    for (String languageCode : languageCodes) {
      fillLanguageHierarchyMap(languageCode, referencedLanguages);
    }
  }
  
  private void fillLanguageHierarchyMap(String languageCode,
      Map<String, Object> referencedLanguages) throws Exception
  {
    try {
      Vertex languageVertex = UtilClass.getVertexByCode(languageCode,
          VertexLabelConstants.LANGUAGE);
      Map<String, Object> languageCodeMapEntry = new HashMap<>();
      languageCodeMapEntry.put(ILanguageHierarchyModel.PARENTS,
          fetchParentHeirarchyLanguageCodes(languageVertex));
      languageCodeMapEntry.put(ILanguageHierarchyModel.CHILDREN,
          fetchChildrenLanguageCodes(languageVertex));
      referencedLanguages.put(languageCode, languageCodeMapEntry);
    }
    catch (NotFoundException ex) {
      throw new LanguageNotFoundException(ex);
    }
  }
  
  private List<String> fetchChildrenLanguageCodes(Vertex languageVertex)
  {
    List<String> languageCodesToReturn = new ArrayList<>();
    String query = "TRAVERSE in(" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
        + ") FROM " + languageVertex.getId() + " STRATEGY BREADTH_FIRST";
    Iterable<Vertex> childrenLanguageVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex childLanguageVertex : childrenLanguageVertices) {
      if (childLanguageVertex.equals(languageVertex)) {
        continue;
      }
      languageCodesToReturn.add(childLanguageVertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return languageCodesToReturn;
  }
  
  private List<String> fetchParentHeirarchyLanguageCodes(Vertex languageVertex)
      throws MultipleLinkFoundException
  {
    List<String> languageCodesToReturn = new ArrayList<>();
    String query = "TRAVERSE out(" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
        + ") FROM " + languageVertex.getId() + " STRATEGY BREADTH_FIRST";
    Iterable<Vertex> parentLanguageVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex parentLanguageVertex : parentLanguageVertices) {
      if (parentLanguageVertex.equals(languageVertex)) {
        continue;
      }
      languageCodesToReturn.add(parentLanguageVertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return languageCodesToReturn;
  }
}
