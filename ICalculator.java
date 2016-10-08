package com.changel.test_calculator.dao;

import java.util.List;

import com.changel.test_calculator.type.Item;

public interface ICalculator {
	public double calculate(List<Item> items,StringBuffer sf);
}
