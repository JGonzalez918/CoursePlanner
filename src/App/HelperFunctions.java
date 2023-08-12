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
	
	public static boolean containsNonSpaceCharDigit(String str) 
	{
		for(int i = 0; i < str.length(); i++) 
		{
			if(Character.isUpperCase(str.charAt(i)) == false && Character.isLowerCase(str.charAt(i)) == false
					&& Character.isDigit(str.charAt(i)) == false && Character.isWhitespace(str.charAt(i)) == false) 
			{
				return true;
			}
		}
		return true;
	}
}
