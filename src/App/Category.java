package App;

public class Category
{
	String categoryID; 
	String categoryName;
	int unitRequirement;
	public Category(String categoryID, String categoryName, int unitRequirement)
	{
		this.categoryID = categoryID;
		this.categoryName = categoryName;
		this.unitRequirement = unitRequirement;
	}
	
	public String toString() 
	{
		return categoryID + " " + categoryName + " Needs " + unitRequirement + " to be satisfied"; 
	}
	
	
}
