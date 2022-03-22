/*package com.cs.core.runtime.strategy.apis.klassinstance;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceResponseModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceStrategyModel;
import com.cs.core.runtime.strategy.db.DatabaseConnection;
import com.cs.core.runtime.strategy.db.PostgreDBUtil;
import com.cs.core.runtime.strategy.usecase.klassinstance.ISaveKlassInstanceRelationshipsStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component()
public class SaveArticleInstanceRelationshipsStrategyPostgre implements ISaveKlassInstanceRelationshipsStrategy {

	@Override
	public ISaveRelationshipInstanceResponseModel execute(ISaveRelationshipInstanceStrategyModel model)
			throws Exception {
		List<IContentRelationshipInstanceModel> modifiedRelationships = model.getRelationshipAdm().getModifiedRelationships();
		List<IRelationshipInstance> relationshipInstances = getRelationshipInstance(modifiedRelationships);
		saveRelationshipInstances(relationshipInstances);
		return null;
	}

	private void saveRelationshipInstances(List<IRelationshipInstance> relationshipInstances) throws SQLException, Exception {
		Map<String, ArrayList<String>> fields = new HashMap<String, ArrayList<String>>();
		fields.put("completeFields", new ArrayList<>(Arrays.asList("id","commonRelationshipInstanceId","relationshipId","sideId","side1InstanceId",
				"side1BaseType","side2InstanceId","side2BaseType","originalInstanceId","count","versionId","versionTimestamp","context","tags"
				,"side1EntityType","otherSideId","side2EntityType")));


		fields.put("stringFields", new ArrayList<String>(Arrays.asList("id","commonRelationshipInstanceId","relationshipId","sideId","side1InstanceId","side1BaseType",
				"side2InstanceId","side2BaseType","originalInstanceId","side1EntityType","otherSideId","side2EntityType")));
		fields.put("timeStampFields", new ArrayList<String>(Arrays.asList("versionTimestamp")));
		fields.put("longFields", new ArrayList<String>(Arrays.asList("versionId")));
		fields.put("intFields", new ArrayList<String>(Arrays.asList("count")));
		fields.put("arrayFields", new ArrayList<String>());
		fields.put("jsonFields", new ArrayList<String>(Arrays.asList("context","tags")));
		fields.put("booleanFields",new ArrayList<String>());
		ObjectMapper oMapper = new ObjectMapper();
		String table = "relationships";
		Map<String, Object> values = oMapper.convertValue(new RelationshipInstance(), Map.class);

		Map<String, Object> queryWithColumnIndex = PostgreDBUtil.buildQuery(values,fields.get("completeFields"),table);
		String query = (String)queryWithColumnIndex.get("query");
		Map<String, Integer> queryIndexValue = (Map<String,Integer>)queryWithColumnIndex.get("columnIndex");



		try(PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);) {

      for(IRelationshipInstance relationshipInstance: relationshipInstances) {
      	values = oMapper.convertValue(relationshipInstance, Map.class);
      	PostgreDBUtil.setQueryParameter(fields,values,queryIndexValue,statement);
      	statement.executeUpdate();
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }		
	}

	private List<IRelationshipInstance> getRelationshipInstance(
			List<IContentRelationshipInstanceModel> modifiedRelationships) {
		
		List<IRelationshipInstance> relationshipInstances = new ArrayList<>();
		  for (IContentRelationshipInstanceModel iContentRelationshipInstance : modifiedRelationships) {
			  relationshipInstances.addAll(iContentRelationshipInstance.getAddedRelationshipInstances());
		
	      }
		return relationshipInstances;
	}

}
*/
