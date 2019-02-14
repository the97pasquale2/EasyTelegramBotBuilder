package org.telegrambot.utils;

import java.util.List;

import org.telegram.interfaces.BuildMessageForEach;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegrambot.handlers.EasyBotHandler;

public class PagesFactory<T> {
	private static final String LAST = "last";
	private static final int NOTHING_INT = -1;
	private static final String NOTHING_STRING = "-1";
	private static final String FIRST = "first";
	String id;
	BuildMessageForEach<T> buildMessage;
	String parseMode;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PagesFactory(String id, BuildMessageForEach<T> buildMessage, String parseMode) {
		this.id = id;
		this.buildMessage = buildMessage;
		this.parseMode = parseMode;
	}
	
	private String getQuery(String query) {
		return id + "_" + query;
	}
	
	private String getQuery(int index) {
		return id + "_" + index;
	}
	
	private InlineKeyboardMarkup getButtons(int index, List<T> list) {
		
		boolean hasNext = (list.size() - 1) > index;   
		boolean hasPrev = index > 0;
		boolean isAlreadyFirst = index == 0;
		
		return new InlineKeyboardFactory()
			.btn("<<", getQuery(isAlreadyFirst ? NOTHING_STRING : FIRST))
			.btn("<", "" + getQuery(hasPrev ? index - 1 : NOTHING_INT))
			.btn((index + 1) + "", getQuery(NOTHING_INT))
			.btn(">", getQuery(hasNext ? index + 1 : NOTHING_INT))
			.btn(">>", getQuery(hasNext ? LAST : NOTHING_STRING)).build();
	}
	
	private EditMessageText getPage(int index, List<T> list) {
		EditMessageText edited = new EditMessageText();
		String text = buildMessage.messageForEach(list.get(index));
		edited.setText(text);
		edited.setReplyMarkup(getButtons(index, list));
		return edited;
	}
	
	private EditMessageText getPage(int index, Long chatId, Integer messageId, List<T> list) {
		EditMessageText edited = getPage(index, list);
		edited.setChatId(chatId);
		edited.setParseMode(parseMode);
		edited.setMessageId(messageId);
		edited.disableWebPagePreview();
		return edited;
	}
	
	public EditMessageText getPage(Update update, List<T> list) {
		EditMessageText edited = null;
		if(update.getCallbackQuery() != null && update.getCallbackQuery().getMessage() != null) {
			Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
			Long chatId = update.getCallbackQuery().getMessage().getChatId();
			int index = getIndex(update.getCallbackQuery().getData(), list);
			if(index != -1) {
				edited = getPage(index, chatId, messageId, list);
			}
		}
		
		return edited;
		
	}
	
	public SendMessage getFirst(Update update, List<T> list, String parseMode) throws Exception {
		SendMessage sendMessage = new SendMessage(EasyBotHandler.getChatId(update), buildMessage.messageForEach(list.get(0)));
		sendMessage.setParseMode(parseMode);
		sendMessage.setReplyMarkup(getButtons(0, list));
		return sendMessage;
	}
	
	public SendMessage getFirst(Update update, List<T> list) throws Exception {
		SendMessage sendMessage = new SendMessage(EasyBotHandler.getChatId(update), buildMessage.messageForEach(list.get(0)));
		sendMessage.setReplyMarkup(getButtons(0, list));
		return sendMessage;
	}
	
	private int getIndex(String callBackQuery, List<T> list) {
		String action = callBackQuery.replace(id + "_", "");
		if(FIRST.equalsIgnoreCase(action)) {
			return 0;
		} else if (NOTHING_STRING.equals(action)) {
			return NOTHING_INT;
		} else if (LAST.equalsIgnoreCase(action)) {
			return list.size() - 1;
		} else {
			return Integer.parseInt(action);
		}
	}
	
}
