package project;

public class LinkStatePacket {
	private Router originate; // router that originate LSP
	private int LSPNumber; // how many LSP have been sent
	private int ttl; // time to live, initial value is 10
	Graph graph;//reachable network
	
	
	public LinkStatePacket(Router originateId, int lSPNumber, int ttl,Graph g) {
		this.originate = originateId;
		this.LSPNumber = lSPNumber;
		this.ttl = ttl;
		this.graph = g;
	}
	
	public LinkStatePacket(Router originateId, int lSPNumber,Graph g) {
		this.originate = originateId;
		this.LSPNumber = lSPNumber;
		this.ttl = 10;
		this.graph = g;
		
	}
	
	public Router getOriginate() {
		return originate;
	}

	public void setOriginate(Router originateId) {
		this.originate = originateId;
	}

	public int getLSPNumber() {
		return LSPNumber;
	}

	public void setLSPNumber(int lSPNumber) {
		LSPNumber = lSPNumber;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

}
