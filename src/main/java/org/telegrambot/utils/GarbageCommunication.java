package org.telegrambot.utils;

import java.util.HashMap;
import java.util.Map.Entry;

import org.telegrambot.handlers.CommunicationHandler;

public class GarbageCommunication implements Runnable {

	HashMap<Long, CommunicationHandler> communicationForIds;

	public GarbageCommunication(HashMap<Long, CommunicationHandler> communicationForIds) {
		this.communicationForIds = communicationForIds;
	}

	public void setCommunicationForIds(HashMap<Long, CommunicationHandler> communicationForIds) {
		this.communicationForIds = communicationForIds;
	}

	@Override
	public void run() {
		
		
		
		while(true) {
			System.out.println("Run...");
			
			for(Entry<Long, CommunicationHandler> entry : communicationForIds.entrySet()) {
				
				CommunicationHandler comunicazione = entry.getValue();
				
				System.out.println("Comunicazione: " + comunicazione);
				
				if(comunicazione != null) {
					long now = System.currentTimeMillis();
					long diff = Math.abs(comunicazione.getMilliseconds() - now);
					
					System.out.println("Now: " + now);
					System.out.println("Stored: " + comunicazione.getMilliseconds());
					System.out.println("Diff: " + diff);
					
					if(diff > 10 * 60 * 1000) {
						communicationForIds.remove(entry.getKey());
					}
				}
				
			}
			
			
			try {
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}

}
