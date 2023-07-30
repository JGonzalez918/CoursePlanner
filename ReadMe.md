# What Did I Learn?
I learned that is important to plan out how you are going to implement something before actually writing any code. I had to consider what the benefits and downsides 
of certain implementations would be. For example, my original implementation merged the information from the prerequisite graph and the requirement graph into one. And I would have 
another graph that would hold the prerequisite information for each course. When a prerequisite was completed the edge was removed from the prerequisites graph.
The benefit to this implementation was that it was very easy to tell when a class was ready to be taken as it would have no outgoing edges meaning that all prerequisites were completed.
However, the code for the first implementation was too complicated and hard to refactor. I found that creating two seperate graphs and creating an array that held the indegree count
for each vertex lead to much simpler implementation and easy to refactor code. 
# What is it? 
Course Planner is a text based application meant to help students plan out the courses they taken during their time in college. The student supplies a text file
containing the course information and the information is then parsed into a graph. The application uses the graph to allow the student plan their course schedule. 
Each semester the student is given a list of courses they can take during that semester. The student picks the courses they want to take repeatedly until all the courses 
are planned for. The application different situations like taking a break from school for some semesters, starting school during a different term, and takking a course 
concurrently with its prerequisites. 
# How does it work? 
## Parsing Course Information
As states previously this application is based on a graph and uses the topological sorting algorithm. The user first has to create a text file which contains 
the information about each course they plan to take during their time in school. The very first line should contain the start term which is either fall or spring 
and the start year. Each line of that should contain information regarding a certain course and it follows this syntax:\
\<CourseID\>, \<CourseName\>, \<UnitValue\> {,\<CourseID\>}\
\<CourseID\> is a unique identifier for a course. For example a course by the name Computer Science 1 could have the course identifier CSC121. The only rules for it are
that it must consist of english letters a - z or A - Z or digits 0 - 9 and each course in the file must have a unique identifier.\
Also the course id does not have to be realated in any way to the course name. The Computer Science 1 coulds have the course id abcd1. \
<CourseName> is the name of the course nothing about it is modified other than any leading or trailing white space which is removed\
\<UnitValue\> is the units worth of the course and must be an integer\
{,\<CourseID\>} is a list of course identifiers all seperated by a comma. If a course has a prerequisite that can be taken concurrently then it has to have -C
at the end of the identifier. For example, suppose CSC121 is an identifier and is a prerequisites that can be taken concurrently with Class A then the definition 
will be listed as written in the file as "CA, Class A, 5 , CSC121-C"\
Note:
1. The application is implemented so that when the course id's are being parsed any characters that aren't a digit or letter are ignored. This is done as I expect the
   course information to be copy and pasted from the student's portal. All letters are also changed to uppercase so the casing does not matter. So in the input file
   the student may enter "Abc    123" as a course identifier and the application will change it to ABC123. This is done so the user doesn't have to rememember how they types a specific course id
2. The order in which courses are added do not matter.
3. The units of a specific course can be entered as a float or integer but it will only be read as an integer. For example, if a unit is entered as 4.5 it will be changed to
   4.
4. Any empty lines are ignored.
## Prerequisite and Requirement Graph
After the course information is parsed it is converted into a graph.
Each course is assaigned an integer from 0 to n - 1 where n is the number of courses that were entered in the file./
That integer is the vertex the course has in the graph./
There are two graphs that this application uses to function. The first is called the "prereqGraph" and the second is called the "requirementGraph".
Every edge in the prereqGraph represents a prerequisite relationship between two classes. If a vertex u has an edge to vertex v then that symbolizes that
vertex v is a prerequisites to vertex u. In other words, to take the class that is represented by vertex v you must take vertex u first./
On the other hand, every edge in the requirement graph has the opposite relationship. If there is an edge (u,v) in the requirementGraph then vertex u is a prerequisite to vertex v.
I implemented the application this way as it allows me to implememnt certain functions easily. For example, if I want to know the prerquisites to a certain course I can run
a breadth first search on the vetex in the preereqGraph. Futhermore, if I want to remove a class from the schedule there could be classes that were already taken that
had the vertex that is being removed as a prerequisite. In that case I would run a bread first search on the requirementGraph and have the vertex being removed
as the source. Then I would remove all class that were already completed.
## Khans Algorithm
This application is an augmentation of Khan's algorithm which creates a topological ordering of a DAG. The function that adds a class to the 
schedule is essentially one iteration of the while loop in the algorithm. In the algorithm the course is removed from the queue but in the application it is marked as being completed.
