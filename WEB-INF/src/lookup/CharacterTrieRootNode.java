package lookup;

public class CharacterTrieRootNode extends AbstractCharacterTrieNode {

	public Character getValue() {
		return null;
	}

	@Override
	public AbstractCharacterTrieNode getParent() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("root");
		sb.append(":");
		sb.append(map.keySet());
		sb.append("\n");
		return sb.toString();
	}
}
