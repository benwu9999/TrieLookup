package lookup;

public interface TrieNode <V> {
	
	public V getValue();
	
	public TrieNode<V> addChild(V v);
	
	public TrieNode<V> getChild(V v);
	
}
