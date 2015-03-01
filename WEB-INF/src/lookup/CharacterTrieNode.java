package lookup;


public class CharacterTrieNode extends AbstractCharacterTrieNode {

	private final AbstractCharacterTrieNode parent;
	private final Character character;
	
	public CharacterTrieNode(AbstractCharacterTrieNode parent, Character character) {
		if(character == null)
			throw new IllegalArgumentException("character is null");
		this.parent = parent;
		this.character = character;
	}
	
	public AbstractCharacterTrieNode getParent() {
		return parent;
	}
	
	public Character getValue() {
		return character;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(character);
		sb.append(":");
		sb.append(map.keySet());
		sb.append("\n");
		return sb.toString();
	}
	
}
