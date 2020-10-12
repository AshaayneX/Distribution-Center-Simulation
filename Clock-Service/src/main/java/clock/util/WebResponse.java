package clock.util;

public class WebResponse {
	private int code;
	private String payload;
	
	public WebResponse(int code, String payload) {
		this.code = code;
		this.payload = payload;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getPayload() {
		return payload;
	}
}
