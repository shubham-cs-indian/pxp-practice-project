package com.cs.config.strategy.plugin.usecase.target;

import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkSaveTargets extends AbstractOrientPlugin {
  
  public static ThreadLocal<Map<String, String>> threadLocalAddedStructrureIdMapping = new ThreadLocal<>();
  
  public BulkSaveTargets(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> klassList = new ArrayList<>();
    klassList = (List<Map<String, Object>>) requestMap.get("list");
    List<Map<String, Object>> createdKlassList = new ArrayList<>();
    List<Map<String, Object>> failedKlassList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setSectionPermissionIdMap(new HashMap<>());
    
    for (Map<String, Object> klass : klassList) {
      try {
        List<Map<String, Object>> responseList = SaveKlassUtil.createSaveKlassData(klass,
            UtilClass.getGraph(), VertexLabelConstants.ENTITY_TYPE_TARGET);
        for (Map<String, Object> klassMap : responseList) {
          Map<String, Object> returnAttributeMap = new HashMap<>();
          returnAttributeMap.put(ISummaryInformationModel.ID, klassMap.get("klassId"));
          returnAttributeMap.put(ISummaryInformationModel.LABEL, klassMap.get("klassLabel"));
          createdKlassList.add(returnAttributeMap);
        }
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) klass.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klass);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) klass.get(IKlassModel.LABEL));
        addToFailureIds(failedKlassList, klass);
      }
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdKlassList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedKlassList);
    return result;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedKlassList, Map<String, Object> klass)
  {
    Map<String, Object> failedKlassMap = new HashMap<>();
    failedKlassMap.put(ISummaryInformationModel.ID, klass.get(IKlassModel.ID));
    failedKlassMap.put(ISummaryInformationModel.LABEL, klass.get(IKlassModel.LABEL));
    failedKlassList.add(failedKlassMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveTargets/*" };
  }
}
