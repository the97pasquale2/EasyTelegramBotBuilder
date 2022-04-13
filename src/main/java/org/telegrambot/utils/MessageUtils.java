package org.telegrambot.utils;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import com.vdurmont.emoji.EmojiParser;

public class MessageUtils {
	public static SendMessage getHTMLMessage(Long chatId, String text) {
		SendMessage sendMessage = null;
		try {
			sendMessage = new SendMessage(chatId, text);
			sendMessage.setParseMode("HTML");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendMessage;
	}
	
	public static SendMessage getHTMLMessage(Update update, String text) {
		SendMessage sendMessage = null;
		try {
			sendMessage = new SendMessage(UpdateUtils.getChatId(update), text);
			sendMessage.setParseMode("HTML");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendMessage;
	}

	public static String emoji(String text) {
		return EmojiParser.parseToUnicode(text);
	}
	
}
