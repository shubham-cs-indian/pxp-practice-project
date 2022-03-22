package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassesByBaseTypeModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetKlassesListByBaseType extends AbstractOrientPlugin {
  
  public GetKlassesListByBaseType(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassesListByBaseType/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IGetKlassesByBaseTypeModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetKlassesByBaseTypeModel.SIZE)
        .toString());
    String searchText = requestMap.get(IGetKlassesByBaseTypeModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IGetKlassesByBaseTypeModel.SEARCH_COLUMN)
        .toString();
    Boolean isNature = (Boolean) requestMap.get(IGetKlassesByBaseTypeModel.IS_NATURE);
    String sortBy = requestMap.get(IGetKlassesByBaseTypeModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IGetKlassesByBaseTypeModel.SORT_ORDER)
        .toString();
    List<String> types = (List<String>) requestMap.get(IGetKlassesByBaseTypeModel.TYPES);
    Boolean isAbstract = (Boolean) requestMap.get(IGetKlassesByBaseTypeModel.IS_ABSTRACT);
    List<String> typesToExclude = (List<String>) requestMap
        .get(IGetKlassesByBaseTypeModel.TYPES_TO_EXCLUDE);
    Boolean isVariantAllowed = (Boolean) requestMap
        .get(IGetKlassesByBaseTypeModel.IS_VARIANT_ALLOWED);
    Boolean isNatureAndNonNature = (Boolean) requestMap
        .get(IGetKlassesByBaseTypeModel.IS_NATURE_AND_NON_NATURE);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    
    Boolean shouldGetAttributionTaxonomies = (Boolean) requestMap
        .get(IGetKlassesByBaseTypeModel.SHOULD_GET_ATTRIBUTION_TAXONOMIES);
    
    String vertexType = VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
    String isNatureQuery = "";
    String taxonomyTypeQuery = "";
    
    if (isNature != null) {
      isNatureQuery = IKlass.IS_NATURE + " = " + isNature;
      if (!typesToExclude.isEmpty()) {
        isNatureQuery = isNatureQuery + " and " + IKlass.NATURE_TYPE + " not in"
            + EntityUtil.quoteIt(typesToExclude);
      }
    }
    
    // For Partner Authorization Mapping
    if (isNatureAndNonNature != null && isNatureAndNonNature) {
      if (!typesToExclude.isEmpty()) {
        isNatureQuery = " " + IKlass.NATURE_TYPE + " not in" + EntityUtil.quoteIt(typesToExclude);
      }
    }
    
    if (shouldGetAttributionTaxonomies != null && shouldGetAttributionTaxonomies) {
      vertexType = VertexLabelConstants.ATTRIBUTION_TAXONOMY;
      taxonomyTypeQuery = ITaxonomy.TAXONOMY_TYPE + " = '" + CommonConstants.MAJOR_TAXONOMY + "' ";
      isNatureQuery = "";
    }
    
    StringBuilder typeQuery = UtilClass.getTypeQueryWithoutANDOperator(types, IKlass.TYPE);
    
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> KlassesList = new ArrayList<Map<String, Object>>();
    
    StringBuilder klassesToExcludeQuery = getKlassesToExcludeQuery();
    StringBuilder isAbstractQuery = getIsAbstractQuery(isAbstract);
    StringBuilder isVariantAllowedQuery = getIsVariantAllowedQuery(isVariantAllowed);
    
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, typeQuery,
        new StringBuilder(isNatureQuery), new StringBuilder(taxonomyTypeQuery),
        klassesToExcludeQuery, isAbstractQuery, isVariantAllowedQuery);
    
    String query = "select from " + vertexType + conditionQuery + " order by "
        + EntityUtil.getLanguageConvertedField(sortBy) + " " + sortOrder + " skip " + from
        + " limit " + size;
    
    String countQuery = "select count(*) from " + vertexType + conditionQuery;
    Long totalCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex klassNode : iterable) {
      Map<String, Object> klassEntityMap = new HashMap<String, Object>();
      klassEntityMap.put(IKlass.ID, klassNode.getProperty(CommonConstants.CODE_PROPERTY));
      klassEntityMap.put(IKlass.LABEL, UtilClass.getValueByLanguage(klassNode, IKlass.LABEL));
      klassEntityMap.put(IKlass.TYPE, klassNode.getProperty(IKlass.TYPE));
      klassEntityMap.put(IConfigEntityInformationModel.CODE, klassNode.getProperty(IKlass.CODE));
      UtilClass.fetchIconInfo(klassNode, klassEntityMap);
      KlassesList.add(klassEntityMap);
    }
    returnMap.put(IGetConfigDataEntityResponseModel.LIST, KlassesList);
    returnMap.put(IGetConfigDataEntityResponseModel.FROM, from);
    returnMap.put(IGetConfigDataEntityResponseModel.SIZE, size);
    returnMap.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, totalCount);
    return returnMap;
  }
  
  public static StringBuilder getKlassesToExcludeQuery()
  {
    String klassesToExcludeQuery = CommonConstants.CODE_PROPERTY + " NOT IN "
        + EntityUtil.quoteIt(SystemLevelIds.KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN);
    
    return new StringBuilder(klassesToExcludeQuery);
  }
  
  public static StringBuilder getIsAbstractQuery(Boolean isAbstract)
  {
    String isAbstractQuery = "";
    if (isAbstract != null) {
      isAbstractQuery = IKlass.IS_ABSTRACT + " = " + isAbstract;
    }
    return new StringBuilder(isAbstractQuery);
  }
  
  public static StringBuilder getIsVariantAllowedQuery(Boolean isVariantAllowed)
  {
    String isVariantAllowedQuery = "";
    if (!isVariantAllowed) {
      isVariantAllowedQuery = "out('" + RelationshipLabelConstants.VARIANT_CONTEXT_OF
          + "').size() = 0";
    }
    return new StringBuilder(isVariantAllowedQuery);
  }
}
