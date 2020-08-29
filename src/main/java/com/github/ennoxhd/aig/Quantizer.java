package com.github.ennoxhd.aig;

import java.util.function.DoubleUnaryOperator;

final class Quantizer {
	
	enum Method implements DoubleUnaryOperator {
		CEIL(Math::ceil),
		ROUND(Math::round),
		FLOOR(Math::floor),
		DEFAULT(ROUND);
		
		private DoubleUnaryOperator method = null;
		
		private Method(final DoubleUnaryOperator method) {
			if(method == null)
				this.method = Math::round;
			else
				this.method = method;
		}
		
		private Method(final Method method) {
			this(method.method);
		}
		
		public double applyAsDouble(final double d) {
			return method.applyAsDouble(d);
		}
	}
	
	static int quantize(final int value, final int maxValue, final int destMaxValue) {
		return quantize(value, maxValue, destMaxValue, null);
	}
	
	static int quantize(final int value, final int maxValue, final int destMaxValue, final Method method) {
		Method methodToUse = method;
		if(method == null) methodToUse = Method.DEFAULT;
		return (int) methodToUse.applyAsDouble(((double) (destMaxValue - 1) * value) / (double) maxValue);
	}
}
