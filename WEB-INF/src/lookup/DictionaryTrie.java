package lookup;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DictionaryTrie {
	
	Logger logger = Logger.getLogger(DictionaryTrie.class.getName());
	
	private CharacterTrieRootNode rootNode = new CharacterTrieRootNode();
	private Map<String, AbstractCharacterTrieNode> prefixEndNodeCache = new HashMap<String, AbstractCharacterTrieNode>();
	
	public List<String> findAllPossibleMatchForPrefix(String prefix) {
		logger.info(String.format("retrieving matches for prefix %s", prefix));
		AbstractCharacterTrieNode endNode = getCachedEndNode(prefix);
		if(endNode == null) {
			endNode = seekEndNode(prefix);
			if(endNode == null) {
				logger.info(String.format("Cannot find end node for prefix %s, returning null", prefix));
				return Collections.emptyList();
			} else
				cachedEndNode(prefix, endNode);
		}
		logger.info(String.format("found endNode %s for prefix %s", endNode, prefix));
		return findAllPossibleMatch(prefix, endNode);
	}
	
	private AbstractCharacterTrieNode seekEndNode(String prefix) {
		AbstractCharacterTrieNode currentNode = rootNode;
		for (int i = 0; i < prefix.length(); i++) {
			CharacterTrieNode nodeForChar = currentNode.getChild(prefix.charAt(i));
			if(nodeForChar != null)
				currentNode = nodeForChar;
			else
				return null;
		}
		return currentNode;
	}

	private AbstractCharacterTrieNode getCachedEndNode(String prefix) {
		logger.info(String.format("retrieving endNode for prefix %s from cache", prefix));
		return prefixEndNodeCache.get(prefix);
	}
	
	private void cachedEndNode(String prefix, AbstractCharacterTrieNode endNode) {
		logger.info(String.format("caching prefix endNode pair %s - %s", prefix, endNode));
		prefixEndNodeCache.put(prefix, endNode);
	}

	public List<String> findAllPossibleMatch(String prefix, AbstractCharacterTrieNode nodeForChar) {
  		if(nodeForChar != null) {
  			List<String> possibleSubWords = nodeForChar.findAllWords();
  			
  			List<String> possibleMatches = new LinkedList<String>();
  			StringBuilder sb = null;
  			for(String subWord : possibleSubWords) {
  				sb = new StringBuilder(prefix);
  				possibleMatches.add(sb.append(subWord).toString());
  			}
  			return possibleMatches;
  		} 
 		return new ArrayList<String>();
	}

	public void fill(String wordsFile) throws IOException, URISyntaxException {
		List<String> words = Files.readAllLines(Paths.get(wordsFile), Charset.defaultCharset());
		for(String word : words) {
			addWord(word.toLowerCase());
		}
	}

	private void addWord(String word) {
		AbstractCharacterTrieNode node = rootNode;
		for (int i = 0; i < word.length(); i++) {
			Character character = word.charAt(i);
			CharacterTrieNode child = node.getChild(character);
			if(child == null) {
				child = node.addChild(character);
			}
			node = child;
		}
	}

	public boolean isEmpty() {
		return rootNode.getChildren().isEmpty();
	}
	
}
