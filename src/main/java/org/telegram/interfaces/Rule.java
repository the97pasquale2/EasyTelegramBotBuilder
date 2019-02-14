package org.telegram.interfaces;

import org.telegram.telegrambots.api.objects.Update;

public interface Rule {
	boolean test(Update update);	
}
