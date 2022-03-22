package com.cs.config.strategy.plugin.migration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

import java.util.List;
import java.util.Map;

/**
 * @author tauseef
 */
public class MigrateConfigIIDs extends AbstractOrientPlugin {

  public static String CLASSIFIERS = "classifiers";
  public static String USERS = "users";

  public MigrateConfigIIDs(OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception {
    List<Map<String, Object>> tags = (List<Map<String, Object>>) requestMap.get(CommonConstants.TAGS);
    List<Map<String, Object>> tagValues = (List<Map<String, Object>>) requestMap.get(CommonConstants.TAG_VALUES);
    List<Map<String, Object>> attributes = (List<Map<String, Object>>) requestMap.get(CommonConstants.ATTRIBUTES);
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) requestMap.get(CommonConstants.RELATIONSHIPS);

    List<Map<String, Object>> classifiers = (List<Map<String, Object>>) requestMap.get(CLASSIFIERS);
    List<Map<String, Object>> taxonomies = (List<Map<String, Object>>) requestMap.get(
        CommonConstants.TAXONOMIES);

    List<Map<String, Object>> users = (List<Map<String, Object>>) requestMap.get(USERS);

    updateIID(attributes, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, "propertyIID", "propertyIID",
        "code", "code");
    updateIID(tags, VertexLabelConstants.ENTITY_TAG, "propertyIID", "propertyIID", "code", "code");
    updateIID(tagValues, VertexLabelConstants.ENTITY_TAG, "propertyIID", "propertyIID", "code", "code");
    updateIID(relationships, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP, "propertyIID",
        "propertyIID", "code", "code");

    updateIID(classifiers, VertexLabelConstants.ENTITY_TYPE_KLASS, "classifierIID", "classifierIID",
        "code", "code");
    updateIID(taxonomies, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, "classifierIID",
        "classifierIID", "code", "code");

    updateIID(users, VertexLabelConstants.ENTITY_TYPE_USER, "iid", "userIID", "userName",
        "userName");

    return null;
  }

  private void updateIID(List<Map<String, Object>> itemList, String className, String iidTag,
      String iidTagToUpdate, String tagForCondition, String conditionTag) {

    for (Map<String, Object> item : itemList) {
      String conditionValue = (String) item.get(tagForCondition);
      long iid = Long.parseLong(String.valueOf(item.get(iidTag)));

      String query =
          "UPDATE " + className + " SET " + iidTagToUpdate + " = " + iid + " WHERE " + conditionTag
              + " = '" + conditionValue + "'";

      UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    }
  }

  @Override
  public String[] getNames() {
    return new String[] { "POST|MigrateConfigIIDs/*" };
  }
}


