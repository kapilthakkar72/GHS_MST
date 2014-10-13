package constants;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public enum StateType
{
	SLEEP("sleep"), FIND("find"), FOUND("found");
	
	private String	stateStr;
	
	StateType(String stateStr)
	{
		this.stateStr = stateStr;
	}
	
	public String getStateStr()
	{
		return stateStr;
	}
	
	// Map to hold all the StateType(s)
	private static Map<String, StateType>	stateMap;
	
	public static StateType getStateType(String stateStr)
	{
		stateStr = StringUtils.lowerCase(stateStr);
		if (stateMap == null)
		{
			initializeMap();
		}
		
		if (stateMap.containsKey(stateStr))
		{
			return stateMap.get(stateStr);
		}
		return null;
	}
	
	private static void initializeMap()
	{
		stateMap = new HashMap<>();
		for (StateType state : StateType.values())
		{
			stateMap.put(state.stateStr, state);
		}
	}
}
