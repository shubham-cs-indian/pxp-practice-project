package com.cs.config.strategy.plugin.usecase.smartdocument.preset;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetAllSmartDocumentPresetResponseModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllSmartDocumentPreset extends AbstractOrientPlugin {
  
  public GetAllSmartDocumentPreset(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllSmartDocumentPreset/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    from = from == null ? 0 : from;
    size = size == null ? 0 : size;
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    Long count = new Long(0);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder searchQueryIsSaveOrIsPreview = new StringBuilder(
        "(" + ISmartDocumentPresetModel.SAVE_DOCUMENT + " = true OR "
            + ISmartDocumentPresetModel.SHOW_PREVIEW + " = true)");
    StringBuilder searchQuerySmartDocumentTemplateIds = new StringBuilder(
        ISmartDocumentPresetModel.SMART_DOCUMENT_TEMPLATE_ID + " IN (" + "SELECT "
            + CommonConstants.CODE_PROPERTY + " FROM "
            + VertexLabelConstants.SMART_DOCUMENT_TEMPLATE + " where "
            + ISmartDocumentTemplateModel.ZIP_TEMPLATE_ID + " IS NOT NULL)");
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery,
        searchQueryIsSaveOrIsPreview, searchQuerySmartDocumentTemplateIds);
    
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    String query = "select from " + VertexLabelConstants.SMART_DOCUMENT_PRESET + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    String countQuery = "select count(*) from " + VertexLabelConstants.SMART_DOCUMENT_PRESET
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Iterable<Vertex> smartDocumentPresets = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> smartDocumentPresetList = new ArrayList<>();
    
    for (Vertex smartDocumentPreset : smartDocumentPresets) {
      smartDocumentPresetList.add(UtilClass.getMapFromVertex(
          Arrays.asList(IIdLabelCodeModel.ID, IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE),
          smartDocumentPreset));
    }
    returnMap.put(IGetAllSmartDocumentPresetResponseModel.LIST, smartDocumentPresetList);
    returnMap.put(IGetAllSmartDocumentPresetResponseModel.COUNT, count);
    return returnMap;
  }
}
