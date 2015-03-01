package lookup;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;


public class TrieNodeTest {

	@Test
	public void testRootNode() {
		CharacterTrieRootNode trieNode = new CharacterTrieRootNode();
		assertTrue(trieNode.getParent() == null);
		assertTrue(trieNode.getChildren().isEmpty());
		assertTrue(trieNode.getValue() == null);
	}
	
	@Test
	public void testNode() {
		CharacterTrieRootNode rootNode = new CharacterTrieRootNode();
		
		CharacterTrieNode parent = rootNode.addChild(Character.valueOf('c'));
		assertTrue(parent.getValue().equals(Character.valueOf('c')));
		assertTrue(parent.getChildren().isEmpty());
		assertTrue(parent.getParent().equals(rootNode));
		
		CharacterTrieNode child = parent.addChild(Character.valueOf('d'));
		assertTrue(child.getValue().equals(Character.valueOf('d')));
		assertTrue(child.getChildren().isEmpty());
		assertTrue(child.getParent().equals(parent));
		
		assertTrue(rootNode.getChild('c').equals(parent));
		assertTrue(rootNode.getChildren().size() == 1);
		assertTrue(rootNode.getChildren().contains(parent));
		
		assertTrue(parent.getChild('d').equals(child));
		assertTrue(parent.getChildren().size() == 1);
		assertTrue(parent.getChildren().contains(child));
		
		List<String> wordsAtRoot = rootNode.findAllWords();
		assertTrue(wordsAtRoot.size() == 1);
		assertTrue(wordsAtRoot.contains("cd"));
		
		CharacterTrieNode child2 = parent.addChild(Character.valueOf('e'));
		assertTrue(parent.getChild('e').equals(child2));
		assertTrue(parent.getChildren().size() == 2);
		
		wordsAtRoot = rootNode.findAllWords();
		assertTrue(wordsAtRoot.size() == 2);
		assertTrue(wordsAtRoot.contains("cd"));
		assertTrue(wordsAtRoot.contains("ce"));
	}
}
