package org.telegram.entities;

import java.util.List;

import org.apache.commons.collections4.map.LinkedMap;
import org.telegram.interfaces.BuildRequestMessage;
import org.telegram.interfaces.Rule;
import org.telegram.interfaces.RuleWithError;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class Request {
	String id;
	List<Request> nextRequests;
	Rule chooseMe;
	RuleWithError testCorrectness;
	Request prevRequest;
	Long timeOut; //TODO
	BuildRequestMessage buildRequestMessage;	
	Request restartFromIfNotConfirmed;
	boolean mustConfirm;
	
	public Request(String id) {
		this.setChooseMe((Update update) -> true).setTestCorrectness((Update update) -> RuleWithError.NO_ERROR);
		this.id = id;
	}
	
	public Request(String id, String message) {
		this.setBuildRequestMessage((LinkedMap<String, Update> responses, Update update, Long chatId, String error) -> new SendMessage(chatId, message))
		.setChooseMe((Update update) -> true).setTestCorrectness((Update update) -> RuleWithError.NO_ERROR);
		this.id = id;
	}
	
	public Request(String id, String message, String parseMode) {
		this.setBuildRequestMessage((LinkedMap<String, Update> responses, Update update, Long chatId, String error) -> new SendMessage(chatId, message).setParseMode(parseMode))
		.setChooseMe((Update update) -> true).setTestCorrectness((Update update) -> RuleWithError.NO_ERROR);
		this.id = id;
	}
	
	public Request(String id, String message, Rule ruleForChoose) {
		this.setBuildRequestMessage((LinkedMap<String, Update> responses, Update update, Long chatId, String error) -> new SendMessage(chatId, message))
		.setChooseMe(ruleForChoose).setTestCorrectness((Update update) -> RuleWithError.NO_ERROR);
		this.id = id;
	}
	
	public Request(String id, String message, Rule ruleForChoose, RuleWithError testCorrectness) {
		this.setBuildRequestMessage((LinkedMap<String, Update> responses, Update update, Long chatId, String error) -> new SendMessage(chatId, message))
		.setChooseMe(ruleForChoose).setTestCorrectness(testCorrectness);
		this.id = id;
	}
	
	public Request(String id, BuildRequestMessage buildRequestMessage, Rule ruleForChoose, RuleWithError testCorrectness) {
		this.setBuildRequestMessage(buildRequestMessage).setChooseMe(ruleForChoose).setTestCorrectness(testCorrectness);
		this.id = id;
	}
	
	public Request(String id, BuildRequestMessage buildRequestMessage2) {
		this.setBuildRequestMessage(buildRequestMessage2).setChooseMe((Update update) -> true).setTestCorrectness((Update update) -> RuleWithError.NO_ERROR);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	
	public boolean mustConfirm() {
		return mustConfirm;
	}
	
	public void setMustConfirm(Request restartFromIfNotConfirmed) {
		this.mustConfirm = true;
		this.restartFromIfNotConfirmed = restartFromIfNotConfirmed;
	}

	public void dontHaveToConfirm() {
		this.mustConfirm = false;
		this.restartFromIfNotConfirmed = null;
	}

	
	
	public Request getRestartFromIfNotConfirmed() {
		return restartFromIfNotConfirmed;
	}

	public SendMessage getMessage(LinkedMap<String, Update> responses, Update lastUpdate, Long chatId, String error) {
		return buildRequestMessage.buildMessage(responses, lastUpdate, chatId, error);
	}

	public RuleWithError getTestCorrectness() {
		return testCorrectness;
	}

	public Request setTestCorrectness(RuleWithError testCorrectness) {
		this.testCorrectness = testCorrectness;
		return this;
	}

	public boolean hasNext() {
		return nextRequests != null && !nextRequests.isEmpty();
	}
	
	public List<Request> getNextRequests() {
		return nextRequests;
	}

	public void setNextRequests(List<Request> nextRequests) {
		this.nextRequests = nextRequests;
	}

	public Rule getChooseMe() {
		return chooseMe;
	}

	public Request setChooseMe(Rule testRightAnswer) {
		this.chooseMe = testRightAnswer;
		return this;
	}

	public Request getPrevRequest() {
		return prevRequest;
	}

	public void setPrevRequest(Request prevRequest) {
		this.prevRequest = prevRequest;
	}

	public Long getTimeOut() {
		return timeOut;
	}

	public Request setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
		return this;
	}

	public BuildRequestMessage getBuildRequestMessage() {
		return buildRequestMessage;
	}

	public Request setBuildRequestMessage(BuildRequestMessage buildRequestMessage) {
		this.buildRequestMessage = buildRequestMessage;
		return this;
	}
	
}
