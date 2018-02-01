package org.telegrambot.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.collections4.map.LinkedMap;
import org.telegram.entities.Request;
import org.telegram.interfaces.CustomEndCommunicationHandler;
import org.telegram.interfaces.EndCommunicationHandler;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class CommunicationHandler {
	private CustomEndCommunicationHandler customEndCommunicationHandler;
	private Request firstRequest;
	private HashMap<Long, Request> actualRequestForIds = new HashMap<Long, Request>();
	private HashMap<Long, LinkedMap<String, Update>> responsesForIds = new HashMap<Long, LinkedMap<String, Update>>(); //HashMap<ChatId, HashMap<IdOfRequest, Update>>
	private String commandForQuit;
	private TelegramLongPollingBot telegramLongPollingBot;
	private EndCommunicationHandler endCommunicationHandler;
	private boolean allowCommandForQuit = false;
	
	public CommunicationHandler(CustomEndCommunicationHandler endCommunicationHandler,
			TelegramLongPollingBot telegramLongPollingBot) {
		this.customEndCommunicationHandler = endCommunicationHandler;
		this.telegramLongPollingBot = telegramLongPollingBot;
	}

	public boolean isAllowCommandForQuit() {
		return allowCommandForQuit;
	}

	public void setAllowCommandForQuit(boolean allowCommandForQuit) {
		this.allowCommandForQuit = allowCommandForQuit;
	}

	public CustomEndCommunicationHandler getCustomEndCommunicationHandler() {
		return customEndCommunicationHandler;
	}

	public void setCustomEndCommunicationHandler(CustomEndCommunicationHandler customEndCommunicationHandler) {
		this.customEndCommunicationHandler = customEndCommunicationHandler;
	}

	public EndCommunicationHandler getEndCommunicationHandler() {
		return endCommunicationHandler;
	}

	public void setEndCommunicationHandler(EndCommunicationHandler endCommunicationHandler) {
		this.endCommunicationHandler = endCommunicationHandler;
	}

	public TelegramLongPollingBot getTelegramLongPollingBot() {
		return telegramLongPollingBot;
	}

	public void setTelegramLongPollingBot(TelegramLongPollingBot telegramLongPollingBot) {
		this.telegramLongPollingBot = telegramLongPollingBot;
	}

	public void start(Update update) throws RuntimeException, TelegramApiException, Exception {
		start(EasyBotHandler.getChatId(update));
	}
	
	public void start(Long chatId) throws RuntimeException, TelegramApiException {
		if(actualRequestForIds.containsKey(chatId)) {
			throw new RuntimeException("Error. Id already in communication");
		} else {
			actualRequestForIds.put(chatId, firstRequest);
			responsesForIds.put(chatId, new LinkedMap<String, Update>());	
			sendRequest(firstRequest, null, chatId, false);
		}
	}
	
	public boolean hasNext(Long chatId) {
		Request actualRequest = actualRequestForIds.get(chatId);
		return actualRequest != null && actualRequest.hasNext();
	}
	
	public void handle(Update update) throws Exception {
		Long chatId = EasyBotHandler.getChatId(update);
		if(actualRequestForIds.containsKey(chatId)) {
			boolean end = true;
			
			Request actualRequest = actualRequestForIds.get(chatId);
			LinkedMap<String, Update> responses = responsesForIds.get(chatId);
			boolean isQuitted = isQuitted(update);
			boolean passed = actualRequest.getTestCorrectness() == null || actualRequest.getTestCorrectness().test(update);
			if(!passed) {
				end = false;
				sendLastRequest(chatId, actualRequest, responses);
			} else if(actualRequest != null && actualRequest.hasNext() && !isQuitted) {
				end = false;
				Request nextRequest = getNextRequest(actualRequest, update);
				if(nextRequest != null) {
					nextRequest.setPrevRequest(actualRequest);
					actualRequestForIds.put(chatId, nextRequest);
					sendRequest(nextRequest, update, chatId, false);
				}
				responses.put(actualRequest.getId(), update); //Adding response in list
			}
			
			if(end) {
				//Handle end communication
				end(chatId, isQuitted);
			}
			
		} else {
			throw new RuntimeException("Error. Id never in communication");
		}
	}

	private void sendLastRequest(Long chatId, Request actualRequest, LinkedMap<String, Update> responses)
			throws TelegramApiException {
		Update lastResponse = null;
		int indexLast = responses.size() - 1;
		if(indexLast >= 0) {
			lastResponse = responses.get(responses.lastKey()); //getting last response
		}
		sendRequest(actualRequest, lastResponse, chatId, true); //Send again actualRequest, since all test to chose the next right request have failed
	}
	
	public boolean isQuitted(Update update) {
		if(commandForQuit == null) {
			return false;
		}
		return (update != null && update.getMessage() != null && update.getMessage().getText() != null && update.getMessage().getText().contains(commandForQuit));
	}

	public void end(Long chatId, boolean isQuitted) {
		LinkedMap<String, Update> responses = responsesForIds.get(chatId);
		actualRequestForIds.remove(chatId);
		responsesForIds.remove(chatId);
		if(customEndCommunicationHandler != null) {
			customEndCommunicationHandler.handleEndCommunication(responses, isQuitted);
		}
		if(endCommunicationHandler != null) {
			endCommunicationHandler.end(chatId);
		}
	}
	
	public Request getNextRequest(Request actualRequest, Update update) { //Test all request and give the right one
		List<Request> requests = actualRequest.getNextRequests();
		for(Request request : requests) {
			if(request.getChooseMe() == null || request.getChooseMe().test(update)) {
				return request;
			}
		}
		return null;
	}
	
	private void sendRequest(Request request, Update update, Long chatId, boolean error) throws RuntimeException, TelegramApiException {
		if(request == null) {
			throw new RuntimeException("First request not setted");
		}
		telegramLongPollingBot.execute(request.getMessage(responsesForIds.get(chatId), update, chatId, error));
	}
	
	public void setFirstRequest(Request firstRequest) {
		this.firstRequest = firstRequest;
	}

	public void addToRequest(Request prevRequest, Request... nextRequests) {
		prevRequest.setNextRequests(Arrays.asList(nextRequests));
	}
	
}
