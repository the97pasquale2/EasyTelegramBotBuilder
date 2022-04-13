package org.telegrambot.handlers;

import org.telegram.interfaces.ErrorHandler;
import org.telegrambot.exceptions.InexistentCommandException;
import org.telegrambot.exceptions.InsufficientPermission;
import org.telegrambot.exceptions.WrongParamsNumberException;

public class DefaultErrorHandler implements ErrorHandler {

	@Override
	public void handleError(InsufficientPermission e) {
		e.printStackTrace();
	}

	@Override
	public void handleError(WrongParamsNumberException e) {
		e.printStackTrace();
	}

	@Override
	public void handleError(InexistentCommandException e) {
		e.printStackTrace();
	}

}
