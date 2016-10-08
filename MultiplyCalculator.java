package com.changel.test_calculator.util;

import java.util.List;

import com.changel.test_calculator.dao.ICalculator;
import com.changel.test_calculator.type.Item;

public class MultiplyCalculator implements ICalculator {

	@Override
	public double calculate(List<Item> items, StringBuffer sf) {
		if (items.size() >= 2) {
			double a = items.get(0).value;
			double b = items.get(1).value;

			items.clear();
			items.add(new Item(a * b));
			sf.append(a + " * " + b + " = " + items.get(0).value);
			return items.get(0).value;
		}
		return 0;
	}

}
