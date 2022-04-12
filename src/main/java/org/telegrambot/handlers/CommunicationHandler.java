package org.telegrambot.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.collections4.map.LinkedMap;
import org.telegram.entities.Request;
import org.telegram.interfaces.CustomEndCommunicationHandler;
import org.telegram.interfaces.EndCommunicationHandler;
import org.telegram.interfaces.RuleWithError;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegrambot.utils.InlineKeyboardFactory;
import org.telegrambot.utils.UpdateUtils;

public class CommunicationHandler {
	private static final int INDEX_NO = 1;
	private static final int INDEX_YES = 0;
	
	private static final String YES = "_YES";
	private static final String NO = "_NO";
	
	private CustomEndCommunicationHandler customEndCommunicationHandler;
	private Request firstRequest;
	private HashMap<Long, Request> actualRequestForIds = new HashMap<Long, Request>();
	private HashMap<Long, LinkedMap<String, Update>> responsesForIds = new HashMap<Long, LinkedMap<String, Update>>(); //HashMap<ChatId, HashMap<IdOfRequest, Update>>
	private LinkedMap<String, Request> requestForIds = new LinkedMap<>();
	private String commandForQuit;
	private TelegramLongPollingBot telegramLongPollingBot;
	private EndCommunicationHandler endCommunicationHandler;
	private long milliseconds = 0L;
	private boolean allowCommandForQuit = false;
	
	private String[] confirmString = new String[]{"YES", "NO"};
	
	public CommunicationHandler(CustomEndCommunicationHandler endCommunicationHandler,
			TelegramLongPollingBot telegramLongPollingBot) {
		this.customEndCommunicationHandler = endCommunicationHandler;
		this.telegramLongPollingBot = telegramLongPollingBot;
	}

	public void setConfirmString(String yes, String no) {
		confirmString[INDEX_YES] = yes;
		confirmString[INDEX_NO] = no;
	}
	
	public long getMilliseconds() {
		return milliseconds;
	}

	public void refresh() {
		milliseconds = System.currentTimeMillis();
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
		start(UpdateUtils.getChatId(update));
	}
	
	public void start(Long chatId, String id, Update firstResponse) throws RuntimeException, TelegramApiException {
		refresh();
		
		if(actualRequestForIds.containsKey(chatId)) {
			throw new RuntimeException("Error. Id already in communication");
		} else {
			actualRequestForIds.put(chatId, firstRequest);
			responsesForIds.put(chatId, new LinkedMap<String, Update>());	
			if(id != null && firstResponse != null) {
				responsesForIds.get(chatId).put(id, firstResponse);
			}
			sendRequest(firstRequest, null, chatId, null);
		}
	}
	
	public void start(Long chatId) throws RuntimeException, TelegramApiException {
		start(chatId, null, null);
	}
	
	public boolean hasNext(Long chatId) {
		Request actualRequest = actualRequestForIds.get(chatId);
		return actualRequest != null && actualRequest.hasNext();
	}
	
	public void handle(Update update) throws Exception {
		refresh();
		
		Long chatId = UpdateUtils.getChatId(update);
		if(actualRequestForIds.containsKey(chatId)) {
			boolean end = true;
			Request actualRequest = actualRequestForIds.get(chatId);
			
			if(actualRequest.mustConfirm() && (!update.hasCallbackQuery() || !YES.equals(update.getCallbackQuery().getData()))) {
				actualRequestForIds.put(chatId, actualRequest.getRestartFromIfNotConfirmed());
				sendRequest(actualRequest.getRestartFromIfNotConfirmed(), update, chatId, RuleWithError.NOT_CONFIRMED);
			}
			else {
				LinkedMap<String, Update> responses = responsesForIds.get(chatId);
				boolean isQuitted = isQuitted(update);
				String error = getError(actualRequest, update);
				boolean passed = isPassed(error);
				if(!passed) {
					end = false;
					sendLastRequest(chatId, actualRequest, responses, error);
				} else if(actualRequest != null && !isQuitted) {
					Request nextRequest = getNextRequest(actualRequest, update);
					if(nextRequest != null) {
						end = false;
						nextRequest.setPrevRequest(actualRequest);
						responses.put(actualRequest.getId(), update); //Adding response in list
						actualRequestForIds.put(chatId, nextRequest);
						sendRequest(nextRequest, update, chatId, RuleWithError.NO_ERROR);
					} else {
						end = true;
						responses.put(actualRequest.getId(), update); //Just adding response in list
					}
				}
				
				if(end) {
					//Handle end communication
					end(chatId, isQuitted);
				}
			}
			
		} else {
			throw new RuntimeException("Error. Id never in communication");
		}
	}

	private String getError(Request actualRequest, Update update) {
		String test = RuleWithError.NO_ERROR;
		if(actualRequest.getTestCorrectness() != null) {
			test = actualRequest.getTestCorrectness().test(update); 
		}
		
		return test;
	}
	
	private boolean isPassed(String error) {
		return RuleWithError.NO_ERROR.equalsIgnoreCase(error);
	}

	private void sendLastRequest(Long chatId, Request actualRequest, LinkedMap<String, Update> responses, String error)
			throws TelegramApiException {
		Update lastResponse = null;
		int indexLast = responses.size() - 1;
		if(indexLast >= 0) {
			lastResponse = responses.get(responses.lastKey()); //getting last response
		}
		sendRequest(actualRequest, lastResponse, chatId, error); //Send again actualRequest, since all test to chose the next right request have failed
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
		if(endCommunicationHandler != null) {
			endCommunicationHandler.end(chatId);
		}
		if(customEndCommunicationHandler != null) {
			customEndCommunicationHandler.handleEndCommunication(responses, isQuitted);
		}
	}
	
	public Request getNextRequest(Request actualRequest, Update update) { //Test all request and give the right one
		List<Request> requests = actualRequest.getNextRequests();
		if(requests != null) {
			for(Request request : requests) {
				if(request.getChooseMe() == null || request.getChooseMe().test(update)) {
					return request;
				}
			}
		}
		return null;
	}
	
	private void sendRequest(Request request, Update update, Long chatId, String error) throws RuntimeException, TelegramApiException {
		if(request == null) {
			throw new RuntimeException("First request not setted");
		}
		SendMessage sendMessage = request.getMessage(responsesForIds.get(chatId), update, chatId, error);
		
		if(request.mustConfirm()) {
			sendMessage.setReplyMarkup(new InlineKeyboardFactory().btn(confirmString[0], YES).btn(confirmString[1], NO).build());
		}
		
		telegramLongPollingBot.execute(sendMessage);
	}
	
	public void setFirstRequest(Request firstRequest) {
		requestForIds.put(firstRequest.getId(), firstRequest);
		this.firstRequest = firstRequest;
	}

	public void addToRequest(Request prevRequest, Request... nextRequests) {
		List<Request> list = Arrays.asList(nextRequests);
		list.forEach((Request r) -> requestForIds.put(r.getId(), r));
		requestForIds.put(prevRequest.getId(), prevRequest);
		prevRequest.setNextRequests(list);
	}
	
}
