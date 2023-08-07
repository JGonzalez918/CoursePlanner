package App;

public class HelperFunctions
{
	public static String allCapsNoSpace(String str) 
	{
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < str.length(); i++) 
		{
			char c = str.charAt(i);
			if(Character.isDigit(c)) 
			{
				s.append(c);
			}
			else if(Character.isAlphabetic(c)) 
			{
				s.append(Character.toUpperCase(c));
			}
		}
		return s.toString();
	}
}
