package lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractCharacterTrieNode implements TrieNode<Character> {
	
	protected final Map<Character, CharacterTrieNode> map = new HashMap<Character, CharacterTrieNode>();
	
	public CharacterTrieNode addChild(Character c) {
		CharacterTrieNode child = new CharacterTrieNode(this, c);
		map.put(c, child);
		return child;
	}
	
	public CharacterTrieNode getChild(Character character) {
		return map.get(character);
	}
	
	public Collection<CharacterTrieNode> getChildren() {
		return map.values();
	}

	public abstract AbstractCharacterTrieNode getParent();
	
	private LinkedList<LinkedList<Character>> findAllPath() {
		LinkedList<LinkedList<Character>> paths = new LinkedList<LinkedList<Character>>();
		Character value = getValue();
		if(map.isEmpty() && value != null) {
			LinkedList<Character> path = new LinkedList<Character>();
			path.add(value);
			paths.add(path);
			return paths;
		}
		for(AbstractCharacterTrieNode child : map.values()) {
			LinkedList<LinkedList<Character>> childPaths = child.findAllPath();
			for(LinkedList<Character> path : childPaths) {
				if(value != null)
					path.addFirst(value);
				paths.add(path);
			}
		}
		return paths;
	}
	
	private LinkedList<LinkedList<Character>> findAllSubPaths() {
		LinkedList<LinkedList<Character>> paths = new LinkedList<LinkedList<Character>>();
		for(AbstractCharacterTrieNode child : map.values()) {
			LinkedList<LinkedList<Character>> childPaths = child.findAllPath();
			for(LinkedList<Character> path : childPaths) {
				paths.add(path);
			}
		}
		return paths;
	}
	
	public List<String> findAllWords() {
		LinkedList<LinkedList<Character>> paths = findAllSubPaths();
		List<String> words = new ArrayList<String>(paths.size());
		StringBuilder sb = null;
		for(LinkedList<Character> path : paths) {
			sb = new StringBuilder();
			for(Character character : path) {
				sb.append(character.charValue());
			}
			words.add(sb.toString());
		}
		return words;
	}
}
