package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GetAllArticleInstance implements IGetInstanceTreeStrategy {
  
  @Override
  public IGetKlassInstanceTreeModel execute(IGetKlassInstanceTreeStrategyModel model)
      throws Exception
  {
    List<IKlassInstanceInformationModel> infoModelList = new ArrayList<>();
    
    IGetKlassInstanceTreeModel responseModel = new GetKlassInstanceTreeModel();
    /*Connection connection = DatabaseConnection.getConnection();
    try (PreparedStatement prepareStatement = prepareStatement(connection, model.getSize(), model.getFrom());
        ResultSet resultSet = prepareStatement.executeQuery()){
    
      while (resultSet.next()) {
        infoModelList.add(new KlassInstanceInformationModel(this.mapResultSet(resultSet)));
      }
    
      responseModel.setTotalContents(PostgreSearchAPIUtil.getTotalArticleCount());
    }  */
    
    responseModel.setChildren(infoModelList);
    responseModel.setFrom(model.getFrom());
    return responseModel;
  }
  
  /*  private PreparedStatement prepareStatement(Connection connection, Integer size, Integer from) throws SQLException
  {
    PreparedStatement prepareStatement = connection.prepareStatement(PostgreConnectionDetails.SCHEMA_AND_QUERIES.getProperty(ArticleInstanceQueryConstants.GET_ALL_ARTICLE_QUERY));
    prepareStatement.setString(1, "nameattribute");
    prepareStatement.setInt(2, size);
    prepareStatement.setInt(3, from);
    return prepareStatement;
  }
  
  private IArticleInstance mapResultSet(ResultSet resultSet) throws SQLException, CSException
  {
     IArticleInstance articleInstance = ResultSetUtils.mapArticleInstance(resultSet, 1);
     ArrayList<AttributeInstance> attributes = new ArrayList<>();
     attributes.add((AttributeInstance) ResultSetUtils.mapAttributeInstance(resultSet, 38));
     articleInstance.setAttributes(attributes);
     return articleInstance;
  }*/
  
}
