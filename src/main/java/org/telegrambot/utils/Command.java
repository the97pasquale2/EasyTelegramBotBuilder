package org.telegrambot.utils;

import java.util.Arrays;

public class Command {
	String command;
	Object[] params;
		
	
	public Command(String textReceived) {
		Object[] splitBySpace = textReceived.split("\\s+");
		command = splitBySpace[0].toString();
		params = new Object[]{};
		if(splitBySpace.length >= 2) {
			params = Arrays.copyOfRange(splitBySpace, 1, splitBySpace.length); //Remove command from params
		}
	}
	public Command(String command, Object[] params) {
		this.command = command;
		this.params = params;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	

	@Override
	public String toString() {
		return "Command [command=" + command + ", params=" + Arrays.toString(params) + "]";
	}
	
}
