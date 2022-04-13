package org.telegram.interfaces;

import java.util.List;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;

public interface UpdateHandler {
	public List<? extends BotApiMethod> onUpdateReceived(Update update);
}
