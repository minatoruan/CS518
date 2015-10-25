package project1;

public class Edge {
	private String _node1;
	private String _node2;
	
	public Edge(String node1, String node2) {
		this._node1 = node1;
		this._node2 = node2;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Edge)) return false;
		if (obj == this) return true;
		
		Edge another = (Edge) obj;
		return (_node1.equalsIgnoreCase(another._node1) && _node2.equalsIgnoreCase(another._node2)) || 
				(_node1.equalsIgnoreCase(another._node2) && _node2.equalsIgnoreCase(another._node1)); 
	}
	
    @Override
    public int hashCode() {
        return _node1.hashCode() ^ _node2.hashCode();
    }	
}