package org.telegram.interfaces;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.objects.Update;

public interface RuleWithError {
	
	public final static String NO_ERROR = "NO_ERROR";
	public final static String VOID = "VOID";
	public final static String NAN = "NOT_A_NUMBER";
	public final static String NO_PHOTO = "NO_PHOTO";
	public final static String NOT_CONFIRMED = "NOT_CONFIRMED";
	String test(Update update);
	
	/**
	 * 
	 * @return VOID (if text is void), NO_ERROR (if text has no error)
	 */
	public static RuleWithError isNotVoid() {
		return (Update update) -> {
			if(update == null || update.getMessage() == null || update.getMessage().getText() == null || StringUtils.isEmpty(update.getMessage().getText().trim())) {
				return VOID;
			}
			return NO_ERROR;
		}; 
	}
	
	public static boolean hasSomeError(String error) {
		return error == null || !NO_ERROR.equalsIgnoreCase(error);
	}
	
	/**
	 * 
	 * @return VOID (if text is void), NAN (if text is not a number), NO_ERROR (if text has no error)
	 */
	public static RuleWithError isANumber() {
		return (Update update) -> {
			if(update == null || update.getMessage() == null || update.getMessage().getText() == null || StringUtils.isEmpty(update.getMessage().getText().trim())) {
				return VOID;
			} else {
				try {
					Double.parseDouble(update.getMessage().getText().trim());
					return NO_ERROR;
				} catch (NumberFormatException e) {
					return NAN;
				}
			}
		}; 
	}
	
	/**
	 * Useful to check if is a number with unit of measure (for example 3.5m will return NO_ERROR if there is the letter 'm' in removeFromString parameter)
	 * @return VOID (if text is void), NAN (if text is not a number), NO_ERROR (if text has no error)
	 */
	public static RuleWithError isANumber(String[] removeFromString) {
		return (Update update) -> {
			if(update == null || update.getMessage() == null || update.getMessage().getText() == null || StringUtils.isEmpty(update.getMessage().getText().trim())) {
				return VOID;
			} else {
				try {
					
					String text = update.getMessage().getText().trim();
					if(removeFromString != null) {
						for(String toRemove : removeFromString) {
							text = text.replace(toRemove, "");
						}
					}
					Double.parseDouble(text);
					return NO_ERROR;
				} catch (NumberFormatException e) {
					return NAN;
				}
			}
		}; 
	}
	
	/**
	 * Useful to check if is a number with unit of measure (for example 3,5m will return NO_ERROR if there is the letter 'm' in removeFromString parameter and in replacaments there is the following entry: ',' as key - '.' as value)
	 * @return VOID (if text is void), NAN (if text is not a number), NO_ERROR (if text has no error)
	 */
	public static RuleWithError isANumber(String[] removeFromString, HashMap<String, String> replacements) {
		return (Update update) -> {
			if(update == null || update.getMessage() == null || update.getMessage().getText() == null || StringUtils.isEmpty(update.getMessage().getText().trim())) {
				return VOID;
			} else {
				try {
					String text = update.getMessage().getText().trim();
					if(removeFromString != null) {
						for(String toRemove : removeFromString) {
							text = text.replace(toRemove, "");
						}
					}
					if(replacements != null) {
						for(String toRemove : replacements.keySet()) {
							text = text.replace(toRemove, replacements.get(toRemove));
						}
					}
					Double.parseDouble(text);
					return NO_ERROR;
				} catch (NumberFormatException e) {
					return NAN;
				}
			}
		}; 
	}
	
	/**
	 * 
	 * @return NO_PHOTO (if no foto are there), NO_ERROR (if no errors are there)
	 */
	public static RuleWithError hasPhoto() {
		return (Update update) -> {
			if(update == null || update.getMessage() == null || update.getMessage().getPhoto() == null || update.getMessage().getPhoto().isEmpty()) {
				return NO_PHOTO;
			} else {
				return NO_ERROR;
			}
		}; 
	}
	
}
