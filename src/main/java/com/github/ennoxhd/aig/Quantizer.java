package com.github.ennoxhd.aig;

import java.util.function.DoubleUnaryOperator;

/**
 * Provides utilities to map numbers from one range to another.
 */
final class Quantizer {
	
	/**
	 * Private default constructor (not used).
	 */
	private Quantizer() {}
	
	/**
	 * Provides different methods for quantization.
	 * @see Quantizer#quantize(int, int, int, Method)
	 */
	enum Method implements DoubleUnaryOperator {
		/**
		 * Uses the {@link Math#ceil(double)} method.
		 */
		CEIL(Math::ceil),
		/**
		 * Uses the {@link Math#round(double)} method.
		 */
		ROUND(Math::round),
		/**
		 * Uses the {@link Math#floor(double)} method.
		 */
		FLOOR(Math::floor),
		/**
		 * Default is {@link #ROUND}.
		 */
		DEFAULT(ROUND);
		
		/**
		 * The quantization method.
		 */
		private DoubleUnaryOperator method = null;
		
		/**
		 * Creates a method reference of the specified method.
		 * @param method quantization method
		 */
		private Method(final DoubleUnaryOperator method) {
			if(method == null)
				this.method = Math::round;
			else
				this.method = method;
		}
		
		/**
		 * Creates a new method out of an existing one (used for the default option).
		 * @param method the method to copy
		 */
		private Method(final Method method) {
			this(method.method);
		}
		
		/**
		 * Applies the method to an input value and gives the output.
		 * @see Quantizer#quantize(int, int, int, Method)
		 */
		public final double applyAsDouble(final double d) {
			return method.applyAsDouble(d);
		}
	}
	
	/**
	 * Quantizes values to another range based on a method.
	 * @param value value to be mapped to another range
	 * @param maxValue maximum value of the input range {@code [0,maxValue]}
	 * @param destMaxValue maximum value of the new range {@code [0,destMaxValue]}
	 * @param method method used for mapping in the quantization process (uses the default on {@code null})
	 * @return new value in the range {@code [0,destMaxValue]}
	 */
	static final int quantize(final int value, final int maxValue, final int destMaxValue, final Method method) {
		final Method methodToUse = method == null ? Method.DEFAULT : method;
		return (int) methodToUse.applyAsDouble(((double) (destMaxValue - 1) * value) / (double) maxValue);
	}
}
