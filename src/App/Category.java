package App;

public class Category
{
	String categoryName;
	int unitRequirement;
	public Category(String categoryName, int unitRequirement)
	{
		this.categoryName = categoryName;
		this.unitRequirement = unitRequirement;
	}
	
	public String toString() 
	{
		return categoryName + " Needs " + unitRequirement + " to be satisfied"; 
	}
	
	
}
