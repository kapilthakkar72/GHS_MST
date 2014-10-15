package constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

public enum MessageType
{
	// Advisable to keep strings in lower case
	CONNECT("connect"), INITIATE("initiate"), TEST("test"), REJECT("reject"), ACCEPT("accept"), REPORT(
			"report"), CHANGEROOT("changeroot");
	
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
	
	public static synchronized MessageType getMsgType(String msgStr)
	{
		msgStr = StringUtils.lowerCase(msgStr);
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
	
	private static synchronized void initializeMap()
	{
		msgTypeMap = new ConcurrentHashMap<>();
		for (MessageType msgType : MessageType.values())
		{
			msgTypeMap.put(msgType.msgStr, msgType);
		}
	}
}
