package com.oni.training.springboot.AOPLearn.JDKProxy;

public class Bird implements Flyable{
	public String name="憤怒雞";
	public int number=1;
	@Override
	public String fly(String route) {
		System.out.println("Route: " + route);
		return route;
	}
	

}
