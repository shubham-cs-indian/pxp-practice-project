package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.strategy.usecase.templating.IGetArticleInstanceForCustomTabStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetArticleInstanceById implements IGetArticleInstanceForCustomTabStrategy {
  
  @Override
  public IGetKlassInstanceCustomTabModel execute(IGetInstanceRequestStrategyModelForCustomTab model)
      throws Exception
  {
    IArticleInstance articleInstance = null;
    /*ResultSet resultSet = null;
    String itemByIdQuery = PostgreConnectionDetails.SCHEMA_AND_QUERIES.getProperty(ArticleInstanceQueryConstants.ARTICLE_GET_QUERY_KEY);
    try (PreparedStatement prepareStatement = DatabaseConnection.getConnection()
        .prepareStatement(itemByIdQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
      prepareStatement.setString(1, model.getId());
      resultSet = prepareStatement.executeQuery();
    
      resultSet.next();
      articleInstance = ResultSetUtils.mapArticleInstance(resultSet, 1);
      List<AttributeInstance> attributes = new ArrayList<>();
      List<TagInstance> tags = new ArrayList<>();
      resultSet.beforeFirst();
    
      List<String> processedAttributes = new ArrayList<String>();
      List<String> processedTags = new ArrayList<String>();
    
      while (resultSet.next()) {
        if (!processedAttributes.contains(resultSet.getString(38))) {
          attributes.add((AttributeInstance) ResultSetUtils.mapAttributeInstance(resultSet, 38));
          processedAttributes.add(resultSet.getString(38));
        }
    
        if (!processedTags.contains(resultSet.getString(65))) {
          tags.add((TagInstance) ResultSetUtils.mapTagInstance(resultSet, 65));
          processedTags.add(resultSet.getString(65));
        }
      }
    
      articleInstance.setAttributes(attributes);
      articleInstance.setTags(tags);
    
    }
    catch (Exception e) {
      throw new CSException(e);
    }
    finally {
      resultSet.close();
    }
    */
    IGetKlassInstanceCustomTabModel returnModel = new GetKlassInstanceForCustomTabModel();
    returnModel.setKlassInstance(articleInstance);
    
    return returnModel;
  }
  
  /*@SuppressWarnings("rawtypes")
  @Override
  public IGetKlassInstanceTreeModel execute(IGetKlassInstanceTreeStrategyModel model)
      throws Exception
  {
  
    List<? extends IPropertyInstanceFilterModel> attributes = model.getAttributes();
  
    IPropertyInstanceFilterModel propertyInstanceFilterModel = attributes.get(0);
    FilterValueMatchModel mandatory = (FilterValueMatchModel) propertyInstanceFilterModel.getMandatory().get(0);
  
    ArticleInstance articleInstance = PostgreSearchAPI.searchByItemId(mandatory.getValue());
  
    IGetKlassInstanceTreeModel responseModel = new GetKlassInstanceTreeModel();
    responseModel.setChildren(PostgreInstanceUtil.getIKlassInstanceInformationModels(Arrays.asList(articleInstance)));
  
    return responseModel;
  }*/
  
}
