package org.telegrambot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotHandler {
	
	public Type type() default Type.COMMAND;
	public String value();
	public Role role() default Role.ALL;
	
}
