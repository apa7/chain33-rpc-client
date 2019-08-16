package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;

public class RpcResult <E> implements Serializable{

	private static final long serialVersionUID = 1L;

	private E result;
	
	private String error;

	public boolean isValid(){
		if(this.error == null || "".equals(this.error) || "null".equals(this.error)){
			return true;
		}
		return false;
	}

	public E getResult() {
		return result;
	}

	public void setResult(E result) {
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
