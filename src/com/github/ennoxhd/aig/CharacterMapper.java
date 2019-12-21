package com.github.ennoxhd.aig;

final class CharacterMapper {

	private interface IntToCharFunction {
		public char apply(int i);
	}
	
	enum Modus implements IntToCharFunction {
		DEPTH_10("@%#*+=-:. ".toCharArray()),
		DEPTH_70("$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ".toCharArray()),
		DEFAULT(DEPTH_10);
		
		private char[] characters = null;
		
		private Modus(char[] characters) {
			this.characters = characters;
		}
		
		private Modus(Modus modus) {
			this(modus.characters);
		}
		
		int levels() {
			return characters.length;
		}

		public char apply(int i) {
			return characters[i];
		}
	}
	
	static char mapToChar(int value) {
		return mapToChar(value, null);
	}
	
	static char mapToChar(int value, Modus modus) {
		if(modus == null) modus = Modus.DEFAULT;
		return modus.apply(value);
	}
}
