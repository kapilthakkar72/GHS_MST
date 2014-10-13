package constants;

import java.util.HashMap;
import java.util.Map;

public enum MessageType
{
	CONNECT("connect"), INITIATE("initiate"), TEST("test"), REJECT("reject"), ACCEPT("accept"), REPORT(
			"report"), CHANGEROOT("changeRoot");
	
	String	msgStr;
	
	MessageType(String msgStr)
	{
		this.msgStr = msgStr;
	}
	
	public String getMsgStr()
	{
		return msgStr;
	}
	
	// Map to hold all the MessageType(s)
	private static Map<String, MessageType>	msgTypeMap;
	
	public static MessageType getMsgType(String msgStr)
	{
		if (msgTypeMap == null)
		{
			initializeMap();
		}
		
		if (msgTypeMap.containsKey(msgStr))
		{
			return msgTypeMap.get(msgStr);
		}
		return null;
	}
	
	private static void initializeMap()
	{
		msgTypeMap = new HashMap<>();
		for (MessageType msgType : MessageType.values())
		{
			msgTypeMap.put(msgType.msgStr, msgType);
		}
	}
}
