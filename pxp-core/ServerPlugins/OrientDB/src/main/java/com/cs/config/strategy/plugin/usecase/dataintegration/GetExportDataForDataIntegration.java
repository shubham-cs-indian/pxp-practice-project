package com.cs.config.strategy.plugin.usecase.dataintegration;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dataintegration.INomenclatureDiModel;
import com.cs.core.runtime.interactor.model.dataintegration.INomenclatureElementModel;
import com.cs.core.runtime.interactor.model.dataintegration.IRootNomenclatureModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetExportDataForDataIntegration extends AbstractOrientPlugin {
  
  public GetExportDataForDataIntegration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetExportDataForDataIntegration/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> taxonomies = new HashMap<>();
    ArrayList<String> selectedTaxonomies = (ArrayList<String>) requestMap
        .get("selectedTaxonomyIds");
    ArrayList<HashMap<String, Object>> nomenClatures = (ArrayList<HashMap<String, Object>>) this
        .getNomenClatures(selectedTaxonomies);
    taxonomies.put(INomenclatureDiModel.ROOTNOMENCLATURES, nomenClatures);
    return taxonomies;
  }
  
  private Object getNomenClatures(ArrayList<String> selectedTaxonomies) throws Exception
  {
    
    // Taxonomies
    // todo: addConstants according to the requested or response model.
    ArrayList<HashMap<String, Object>> NomenClatures = new ArrayList<>();
    if (selectedTaxonomies.size() > 0) {
      for (String taxonomyId : selectedTaxonomies) {
        Vertex taxonomyVertex = UtilClass.getVertexById(taxonomyId,
            // VertexLabelConstants.HIERARCHY_TAXONOMY); TODO: PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        String traverseQuery = "(traverse out('Child_Of') from " + taxonomyVertex.getId()
            + " strategy BREADTH_FIRST) where isTaxonomy = true";
        String query = "select from" + traverseQuery;
        String countQuery = "select count($path) from" + traverseQuery;
        Iterable<Vertex> resultIterable = UtilClass.getGraph()
            .command(new OCommandSQL(query))
            .execute();
        long levelcount = EntityUtil.executeCountQueryToGetTotalCount(countQuery) - 1;
        ArrayList<HashMap<String, Object>> childTaxonomies = new ArrayList<>();
        HashMap<String, Object> RootNomenClature = new HashMap<>();
        for (Vertex taxVertex : resultIterable) {
          
          HashMap<String, Object> childTaxonomy = new HashMap<>();
          String majorTaxonomy = taxVertex.getProperty(IMasterTaxonomy.TAXONOMY_TYPE);
          if (majorTaxonomy != null && majorTaxonomy.equals(CommonConstants.MAJOR_TAXONOMY)) {
            RootNomenClature.put(IRootNomenclatureModel.ID,
                taxVertex.getProperty(CommonConstants.CODE_PROPERTY));
            RootNomenClature.put(IRootNomenclatureModel.NAME,
                taxVertex.getProperty("label__" + UtilClass.getLanguage()
                    .getUiLanguage())); // todo get value by default language.
          }
          else {
            childTaxonomy.put(INomenclatureElementModel.ID,
                taxVertex.getProperty(CommonConstants.CODE_PROPERTY));
            childTaxonomy.put(INomenclatureElementModel.NAME,
                taxVertex.getProperty("label__" + UtilClass.getLanguage()
                    .getUiLanguage()));
            childTaxonomy.put(INomenclatureElementModel.LEVEL, levelcount);
            Iterable<Vertex> immediateParentIterator = taxVertex.getVertices(Direction.OUT,
                RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
            for (Vertex parentTaxonomy : immediateParentIterator) {
              Boolean isTaxonomy = parentTaxonomy.getProperty(IMasterTaxonomy.IS_TAXONOMY);
              if (isTaxonomy != null && isTaxonomy) {
                childTaxonomy.put(INomenclatureElementModel.PARENTID,
                    parentTaxonomy.getProperty(CommonConstants.CODE_PROPERTY));
              }
            }
            childTaxonomies.add(childTaxonomy);
            levelcount--;
          }
        }
        if (childTaxonomies.size() > 0) {
          RootNomenClature.put(IRootNomenclatureModel.PATH, childTaxonomies);
        }
        
        NomenClatures.add(RootNomenClature);
      }
    }
    
    return NomenClatures;
  }
}
