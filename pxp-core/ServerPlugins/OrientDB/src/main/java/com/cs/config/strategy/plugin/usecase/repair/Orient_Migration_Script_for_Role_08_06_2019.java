package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_for_Role_08_06_2019 extends AbstractOrientMigration {
  
  public Orient_Migration_Script_for_Role_08_06_2019(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_for_Role_08_06_2019/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    List<String> RACIVS_ROLE_IDS = Arrays.asList("responsiblerole", "accountablerole",
        "consultedrole", "verifyrole", "signoffrole", "informedrole");
    String deleteQuery = "DELETE VERTEX FROM " + VertexLabelConstants.ENTITY_TYPE_ROLE
        + " WHERE code IN " + EntityUtil.quoteIt(RACIVS_ROLE_IDS);
    UtilClass.getGraph()
        .command(new OCommandSQL(deleteQuery))
        .execute();
    UtilClass.getGraph()
        .commit();
    return null;
  }
}
