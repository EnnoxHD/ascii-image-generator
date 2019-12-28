package com.github.ennoxhd.aig;

final class CharacterMapper {

	private interface IntToCharFunction {
		public char apply(final int i);
	}
	
	enum Modus implements IntToCharFunction {
		DEPTH_10("@%#*+=-:. ".toCharArray()),
		DEPTH_70("$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ".toCharArray()),
		DEFAULT(DEPTH_10);
		
		private char[] characters = null;
		
		private Modus(final char[] characters) {
			if(characters == null)
				this.characters = new char[] {};
			else
				this.characters = characters;
		}
		
		private Modus(final Modus modus) {
			this(modus.characters);
		}
		
		int degrees() {
			return characters.length;
		}

		public char apply(final int i) {
			return characters[i];
		}
	}
	
	static char mapToChar(final int value) {
		return mapToChar(value, null);
	}
	
	static char mapToChar(final int value, final Modus modus) {
		if(modus == null) return Modus.DEFAULT.apply(value);
		return modus.apply(value);
	}
}
