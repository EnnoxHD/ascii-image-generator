package com.github.ennoxhd.aig;

/**
 * Provides a mapping of integers to characters.
 * This is based on the visual density of a character
 * where {@code 0} maps to the most dense character in a series (e.g. {@code @}).
 * The highest integer often maps to a single space character. 
 */
final class CharacterMapper {

	/**
	 * Private default constructor (not used).
	 */
	private CharacterMapper() {}
	
	/**
	 * Functional interface that maps an primitive integer to a primitive character.
	 */
	private interface IntToCharFunction {
		
		/**
		 * Maps an integer to a character.
		 * @param i integer to map
		 * @return mapped character
		 */
		public char apply(final int i);
	}
	
	/**
	 * Provides different character series for mapping integers to chars.
	 * @see CharacterMapper
	 */
	enum Mode implements IntToCharFunction {
		/**
		 * Series of ten characters.
		 * {@code "@%#*+=-:. "}
		 */
		DEPTH_10("@%#*+=-:. ".toCharArray()),
		/**
		 * Series of 70 characters.
		 * {@code "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. "}
		 */
		DEPTH_70("$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ".toCharArray()),
		/**
		 * Default is {@link #DEPTH_10}.
		 */
		DEFAULT(DEPTH_10);
		
		/**
		 * The series of characters.
		 */
		private char[] characters = null;
		
		/**
		 * Creates a mode with the specified series of characters.
		 * @param characters series of characters
		 */
		private Mode(final char[] characters) {
			if(characters == null)
				this.characters = new char[] {};
			else
				this.characters = characters;
		}
		
		/**
		 * Creates a new mode out of an existing one (used for the default option).
		 * @param modus the modus to copy
		 */
		private Mode(final Mode modus) {
			this(modus.characters);
		}
		
		/**
		 * The length of the character series of the mode.
		 * @return length of the series
		 */
		final int seriesLength() {
			return characters.length;
		}
		
		/**
		 * Maps an integer to the corresponding character of the series.
		 * @see IntToCharFunction
		 * @see Mode
		 */
		public final char apply(final int i) {
			return characters[i];
		}
	}
}
