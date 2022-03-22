package com.cs.config.strategy.plugin.usecase.viewconfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.viewconfiguration.ViewConfigurationNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class SaveViewConfiguration extends AbstractOrientPlugin {

	public SaveViewConfiguration(OServerCommandConfiguration iConfiguration) {
		super(iConfiguration);
	}

	@Override
	public String[] getNames() {
		return new String[] { "POST|SaveViewConfiguration/*" };
	}

	@Override
	protected Object execute(Map<String, Object> requestMap) throws Exception {
		Vertex viewConfigurationVertex = null;
		List<String> fieldsToIgnore = new ArrayList<String>();
    fieldsToIgnore.add(IConfigResponseWithAuditLogModel.AUDIT_LOG_INFO);
		try {
			viewConfigurationVertex = UtilClass.getVertexById(SystemLevelIds.VIEW_CODE,
					VertexLabelConstants.VIEW_CONFIGURATION);
		} catch (NotFoundException e) {
			throw new ViewConfigurationNotFoundException(e);
		}

		UtilClass.saveNode(requestMap, viewConfigurationVertex, fieldsToIgnore);
		UtilClass.getGraph().commit();
		 Map<String, Object> viewConfiguration = UtilClass.getMapFromVertex(new ArrayList<>(),
				 viewConfigurationVertex);
			    
		return viewConfiguration;
	}
}