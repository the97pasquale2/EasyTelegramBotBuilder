package org.telegram.interfaces;

import org.telegrambot.exceptions.InexistentCommandException;
import org.telegrambot.exceptions.InsufficientPermission;
import org.telegrambot.exceptions.WrongParamsNumberException;

public interface ErrorHandler {
	
	void handleError(InsufficientPermission e);
	void handleError(WrongParamsNumberException e);
	void handleError(InexistentCommandException e);

}
