package com.cs.di.workflow.model.executionstatus;

import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;

import java.text.MessageFormat;

public class OutputExecutionStatusModel implements IOutputExecutionStatusModel {

	protected ObjectCode[] objectCodes;
	protected String[] objectValues;
	protected MessageCode messageCode;
	protected String messageType;

	@Override
	public String getMessageType() {
		return messageType;
	}

	@Override
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Override
	public ObjectCode[] getObjectCodes() {
		return objectCodes;
	}

	@Override
	public void setObjectCodes(ObjectCode[] objectCodes) {
		this.objectCodes = objectCodes;
	}

	@Override
	public MessageCode getMessageCode() {
		return messageCode;
	}

	@Override
	public void setMessageCode(MessageCode messageCode) {
		this.messageCode = messageCode;
	}

	@Override
	public String[] getObjectValues() {
		return this.objectValues;
	}

	@Override
	public void setObjectValues(String[] objectValues) {
		this.objectValues = objectValues;
	}

	@Override
	public String toString() {
		String messageText = messageCode.getMessage();
			messageText = MessageFormat.format(messageText, objectValues);//dynamic value replacement
		return messageText;
	}

}
