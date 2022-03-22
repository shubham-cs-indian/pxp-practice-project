package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetChildTaxonomiesByParentId extends AbstractOrientPlugin {
  
  public GetChildTaxonomiesByParentId(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetChildTaxonomiesByParentId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Object paginate = requestMap.get("paginate");
    if (paginate == null) {
      return executeWithoutPagination(requestMap);
    }
    else {
      return executeWithPagination(requestMap);
    }
  }
  
  private Map<String, Object> executeWithoutPagination(Map<String, Object> requestMap)
      throws Exception
  {
    String parentId = (String) requestMap.get(IIdParameterModel.ID);
    /*
    Vertex parentTaxonomy = null;
    try {
      parentTaxonomy = UtilClass.getVertexById(parentId, VertexLabelConstants.KLASS_TAXONOMY);
    }
    catch (NotFoundException e) {
      throw new KlassTaxonomyNotFoundException(e);
    }
    
    List<String> fieldsToFetchForParent = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IArticleTaxonomy.LABEL, IArticleTaxonomy.CHILD_COUNT);
    Map<String, Object> responseMap = UtilClass.getMapFromVertex(fieldsToFetchForParent, parentTaxonomy);
    
    Iterable<Vertex> childTaxonomies = parentTaxonomy.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    List<Map<String, Object>> children = new ArrayList<>();
    
    for (Vertex child : childTaxonomies) {
      List<String> fieldsToFetchForChild = Arrays.asList(CommonConstants.CODE_PROPERTY, IArticleTaxonomy.LABEL);
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetchForChild, child);
      children.add(childMap);
    }
    
    responseMap.put(ICategoryInformationModel.CHILDREN, children);
    responseMap.put(ICategoryInformationModel.COUNT, responseMap.remove(IArticleTaxonomy.CHILD_COUNT));
    return responseMap;
    */
    return getParentAndChildTaxonomiesMapByParentId(parentId);
  }
  
  /**
   * @param parentTaxonomyId
   * @return a map with id & label of parent taxonomy and its immediate children
   *         id & label
   * @throws Exception
   * @author Kshitij
   *         <p>
   *         Lokesh : copied this function from TaxonomyUtil to add
   *         isAttributionTaxonomy Field
   */
  private Map<String, Object> getParentAndChildTaxonomiesMapByParentId(String parentTaxonomyId)
      throws Exception
  {
    Vertex parentTaxonomy = null;
    try {
      parentTaxonomy = UtilClass.getVertexByIndexedId(parentTaxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    }
    catch (NotFoundException e) {
      throw new KlassTaxonomyNotFoundException(e);
    }
    
    List<String> fieldsToFetchForParentTaxonomy = Arrays.asList(CommonConstants.CODE_PROPERTY,
        ITaxonomy.LABEL, ITaxonomy.CHILD_COUNT, ITaxonomy.CODE, ITaxonomy.BASE_TYPE);
    Map<String, Object> taxonomyTree = UtilClass.getMapFromVertex(fieldsToFetchForParentTaxonomy,
        parentTaxonomy);
    
    List<Map<String, Object>> children = new ArrayList<>();
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> childTaxonomies = graph
        .command(new OCommandSQL(
            "select expand(in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
                + "')) from " + parentTaxonomy.getId() + " order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    
    List<String> fieldsToFetchForChildTaxonomy = Arrays.asList(CommonConstants.CODE_PROPERTY,
        ITaxonomy.LABEL, ITaxonomy.CODE);
    for (Vertex child : childTaxonomies) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetchForChildTaxonomy,
          child);
      children.add(childMap);
    }
    
    taxonomyTree.put(ICategoryInformationModel.CHILDREN, children);
    taxonomyTree.put(ICategoryInformationModel.COUNT, taxonomyTree.remove(ITaxonomy.CHILD_COUNT));
    taxonomyTree.put(ICategoryInformationModel.TYPE, taxonomyTree.remove(ITaxonomy.BASE_TYPE));
    return taxonomyTree;
  }
  
  // TODO :: avoid use of @rid to improve the performance.
  private Map<String, Object> executeWithPagination(Map<String, Object> requestMap) throws Exception
  {
    String parentId = (String) requestMap.get("parentTaxonomyId");
    Vertex parentTaxonomy = UtilClass.getVertexByIndexedId(parentId,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    
    List<String> fieldsToFetchForParent = Arrays.asList(CommonConstants.CODE_PROPERTY,
        ITaxonomy.LABEL, ITaxonomy.CHILD_COUNT, ITaxonomy.CODE);
    Map<String, Object> responseMap = UtilClass.getMapFromVertex(fieldsToFetchForParent,
        parentTaxonomy);
    
    String lastChildTaxonomyId = (String) requestMap.get("lastChildTaxonomyId");
    String whereClause = "";
    if (lastChildTaxonomyId != null && !lastChildTaxonomyId.isEmpty()) {
      Vertex lastChildTaxonomy = UtilClass.getVertexById(lastChildTaxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      String lastChildTaxonomyRID = lastChildTaxonomy.getId()
          .toString();
      whereClause = " and @rid < \"" + lastChildTaxonomyRID + "\"";
    }
    
    String parentRID = parentTaxonomy.getId()
        .toString();
    String queryToExecute = "select from (traverse in ('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from " + parentRID
        + " maxdepth 1 strategy BREADTH_FIRST) where @rid <> \"" + parentRID + "\"" + whereClause
        + " limit 10";
    Iterable<Vertex> childTaxonomies = UtilClass.getGraph()
        .command(new OCommandSQL(queryToExecute))
        .execute();
    
    List<Map<String, Object>> children = new ArrayList<>();
    for (Vertex child : childTaxonomies) {
      List<String> fieldsToFetchForChild = Arrays.asList(CommonConstants.CODE_PROPERTY,
          ITaxonomy.LABEL, ITaxonomy.CODE);
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetchForChild, child);
      children.add(childMap);
    }
    
    responseMap.put(ICategoryInformationModel.CHILDREN, children);
    responseMap.put(ICategoryInformationModel.COUNT, responseMap.remove(ITaxonomy.CHILD_COUNT));
    return responseMap;
  }
}
