package com.github.ennoxhd.aig;

import java.util.function.DoubleUnaryOperator;

final class Quantizer {
	
	enum Method implements DoubleUnaryOperator {
		CEIL(Math::ceil),
		ROUND(Math::round),
		FLOOR(Math::floor),
		DEFAULT(ROUND);
		
		private DoubleUnaryOperator method = null;
		
		private Method(DoubleUnaryOperator method) {
			this.method = method;
		}
		
		private Method(Method method) {
			this(method.method);
		}
		
		public double applyAsDouble(double d) {
			return method.applyAsDouble(d);
		}
	}
	
	static int quantize(int value, int maxValue, int destMaxValue) {
		return quantize(value, maxValue, destMaxValue, null);
	}
	
	static int quantize(int value, int maxValue, int destMaxValue, Method method) {
		if(method == null) method = Method.DEFAULT;
		return (int) method.applyAsDouble(((double) (destMaxValue - 1) * value) / (double) maxValue);
	}
}
