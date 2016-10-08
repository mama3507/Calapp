package com.changel.test_calculator.factory;

import com.changel.test_calculator.dao.ICalculator;
import com.changel.test_calculator.type.Types;
import com.changel.test_calculator.util.DivisionCalculator;
import com.changel.test_calculator.util.MinusCalculator;
import com.changel.test_calculator.util.MultiplyCalculator;
import com.changel.test_calculator.util.PlusCalculator;



public class CalculatorFactory {

	public static ICalculator getCalculator(Types type) {
		ICalculator calculator = null;
		switch (type) {
		case JIA:
			calculator = new PlusCalculator();
			break;
		case JIAN:
			calculator = new MinusCalculator();
			break;
		case CHENG:
			calculator = new MultiplyCalculator();
			break;
		case CHU:
			calculator = new DivisionCalculator();
			break;
		default:
			break;
		}
		return calculator;
	}
}
