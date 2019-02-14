package org.telegram.interfaces;

import org.apache.commons.collections4.map.LinkedMap;
import org.telegram.telegrambots.api.objects.Update;

public interface CustomEndCommunicationHandler {
	public void handleEndCommunication(LinkedMap<String, Update> responses, boolean quitted);
}
