package org.telegram.interfaces;

import org.apache.commons.collections4.map.LinkedMap;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public interface BuildRequestMessage {
	//Update can be null if it's the first message
	SendMessage buildMessage(LinkedMap<String, Update> responses, Update lastUpdate, Long chatId, boolean error);
}
