package org.telegram.entities;

import java.lang.reflect.Method;

import org.telegrambot.annotations.Role;

public class MethodRole {
	Method method;
	Role role;
	
	public MethodRole(Method method, Role role) {
		super();
		this.method = method;
		this.role = role;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	
}
