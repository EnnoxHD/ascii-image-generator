package com.github.ennoxhd.aig;

import java.awt.image.AffineTransformOp;

/**
 * Configuration of image conversion methods.
 * @see InterpolationType
 * @see Quantizer.Method
 * @see CharacterMapper.Mode 
 */
final class ImageConversionMethods {

	/**
	 * The interpolation type for image scaling.
	 */
	private InterpolationType interpolationType;
	
	/**
	 * The method used by the quantizer.
	 */
	private Quantizer.Method quantizerMethod;
	
	/**
	 * The character series to use in the final image.
	 */
	private CharacterMapper.Mode characterMode;
	
	/**
	 * Gets the interpolation type for image scaling.
	 * @return the interpolation type
	 */
	final InterpolationType getInterpolationType() {
		return interpolationType;
	}
	
	/**
	 * Sets the interpolation type for image scaling.
	 * @param interpolationType the interpolation type used
	 */
	final void setInterpolationType(final InterpolationType interpolationType) {
		this.interpolationType = interpolationType == null ?
				InterpolationType.DEFAULT : interpolationType;
	}
	
	/**
	 * Gets the method used by the quantizer.
	 * @return the quantization method
	 */
	final Quantizer.Method getQuantizerMethod() {
		return quantizerMethod;
	}
	
	/**
	 * Sets the method used by the quantizer.
	 * @param quantizerMethod the quantization method used
	 */
	final void setQuantizerMethod(final Quantizer.Method quantizerMethod) {
		this.quantizerMethod = quantizerMethod == null ?
				Quantizer.Method.DEFAULT : quantizerMethod;
	}
	
	/**
	 * Gets the character series to use in the final image.
	 * @return the mode representing a series of characters
	 */
	final CharacterMapper.Mode getCharacterMode() {
		return characterMode;
	}
	
	/**
	 * Sets the character series to use in the final image.
	 * @param characterMode the mode representing a series of characters
	 */
	final void setCharacterMode(final CharacterMapper.Mode characterMode) {
		this.characterMode = characterMode == null ?
				CharacterMapper.Mode.DEFAULT : characterMode;
	}
	
	/**
	 * Creates a configuration with the standard methods.
	 */
	ImageConversionMethods() {
		setInterpolationType(InterpolationType.DEFAULT);
		setQuantizerMethod(Quantizer.Method.DEFAULT);
		setCharacterMode(CharacterMapper.Mode.DEFAULT);
	}
	
	/**
	 * Wrapper enum for the {@link AffineTransformOp} interpolation types.
	 * @see AffineTransformOp#TYPE_BICUBIC
	 * @see AffineTransformOp#TYPE_BILINEAR
	 * @see AffineTransformOp#TYPE_NEAREST_NEIGHBOR
	 */
	static enum InterpolationType {
		/**
		 * Bicubic interpolation ({@link AffineTransformOp#TYPE_BICUBIC}).
		 */
		BICUBIC(AffineTransformOp.TYPE_BICUBIC),
		/**
		 * Bilinear interpolation ({@link AffineTransformOp#TYPE_BILINEAR}).
		 */
		BILINEAR(AffineTransformOp.TYPE_BILINEAR),
		/**
		 * Nearest neighbor interpolation ({@link AffineTransformOp#TYPE_NEAREST_NEIGHBOR}).
		 */
		NEAREST_NEIGHBOR(AffineTransformOp.TYPE_NEAREST_NEIGHBOR),
		/**
		 * The default is {@link #BILINEAR}.
		 */
		DEFAULT(BILINEAR);
		
		/**
		 * The native interpolation type.
		 */
		private int type = 0;
		
		/**
		 * Creates an interpolation type of the native type.
		 * @param type the native interpolation type
		 */
		private InterpolationType(final int type) {
			this.type = type;
		}
		
		/**
		 * Creates a new interpolation type out of an existing one.
		 * @param type the type to copy
		 */
		private InterpolationType(final InterpolationType type) {
			if(type == null)
				this.type = AffineTransformOp.TYPE_BILINEAR;
			else
				this.type = type.getType();
		}
		
		/**
		 * Gets the native interpolation type as defined in {@link AffineTransformOp}.
		 * @return native interpolation type
		 */
		final int getType() {
			return type;
		}
	}
}
