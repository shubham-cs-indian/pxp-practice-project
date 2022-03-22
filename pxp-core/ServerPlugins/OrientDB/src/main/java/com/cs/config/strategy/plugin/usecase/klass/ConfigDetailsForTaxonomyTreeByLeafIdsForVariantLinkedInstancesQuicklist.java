package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklist
    extends AbstractOrientPlugin {
  
  public ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklist(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] {
        "POST|ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklist/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    String userId = (String) requestMap.get(
        IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel.USER_ID);
    
    GlobalPermissionUtils.fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(userId,
        returnMap);
    
    Boolean isKlassTaxonomy = (Boolean) requestMap.get(
        IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel.IS_KLASS_TAXONOMY);
    
    if (isKlassTaxonomy) {
      String entityId = (String) requestMap.get(
          IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel.ENTITY_ID);
      String klassId = EntityUtil.getStandardKlassIdForModuleEntity(entityId);
      returnMap.put(
          IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel.CATEGORY_INFO,
          TaxonomyUtil.getKlassTaxonomyByKlassId(klassId));
    }
    else {
      returnMap.put(
          IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel.CATEGORY_INFO,
          TaxonomyUtil.getTaxonomyTreeByRootTaxonomyIdAndLeafIds((String) requestMap.get(
              IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel.PARENT_TAXONOMY_ID),
              (List<String>) requestMap.get(
                  IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel.LEAF_IDS)));
    }
    
    return returnMap;
  }
}
