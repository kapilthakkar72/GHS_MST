package constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
<<<<<<< HEAD
	
=======

>>>>>>> babb41cf92e121c6260949cfe8c3c5099132bc5c
	public static synchronized StateType getStateType(String stateStr)
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
<<<<<<< HEAD
	
=======

>>>>>>> babb41cf92e121c6260949cfe8c3c5099132bc5c
	private static synchronized void initializeMap()
	{
		stateMap = new ConcurrentHashMap<>();
		for (StateType state : StateType.values())
		{
			stateMap.put(state.stateStr, state);
		}
	}
}
