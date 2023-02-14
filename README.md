##AI Project: Coast Guard Problem

This project is an implementation of various search algorithms on the Coast Guard problem. The implementation is done in Java, and the project consists of a few main parts, which are described below.

1. Search-tree node ADT implementation
A Node class was created to represent nodes in the search tree. The class contains several variables, including a State object, a String to record the operator, int variables to hold the depth of the node and the path cost, a double variable for the heuristic cost, and a String to specify the type of algorithm used. The State class contains information about the grid at the node, such as the number of passengers, the position of the Coast Guard, and whether the Coast Guard is on a ship or station.

2. Implementation of the search problem ADT
While no abstract data type was implemented for the actual search problem, various methods were implemented that work for each algorithm at hand. These methods are discussed below.

3. Implementation of the Coast Guard problem
The CoastGuard class was implemented to hold the number of passengers on the guard ship, the black boxes on the ship, and the capacity for the guard. The actual movement of the guard ship was done through the State object dependent on the move. The Node class had methods related to movement (move up, down, right, left). Each method was responsible for changing the guard’s x and y position given the convention used in the project description. The CoastGuard had a function called pickUp and retrieve. These functions were responsible for the logic of the pick up and retrieval of the black boxes (checking if we have capacity for passengers, checking if the ship is a wreck, and if so pick up the black box). When the movement was called on a node, it either returned a deep clone of the current node with the state changed to accommodate for the action itself, or it returned null. If null was returned, that means that this state is not a viable option.

4. Description of the main functions
There were multiple functions implemented, including genGrid(), solve(), BFS(), DFS(), ID(), and GreedyAndAstar(). The genGrid() function returns a randomly generated string that contains the information of the grid according to the given schema in the project description. The solve() function is responsible for parsing the string approach and calling the matching method that is required.

The BFS(), DFS(), and ID() functions were implemented using a similar approach, where we create the initial node and add it to a queue/stack. We then enter a while loop and expand the tree further by calling node movement functions. Each function handled its own logic. We then took the nodes returned from each function and made sure it did not pre-exist in the queue before. If the node existed, it was not added to the queue (eliminating repetitive states). We kept on expanding loops until a goal state was reached. The GreedyAndAstar() function was implemented in the same method as the previous functions but with a slight twist. A large loop was created that held the depth at a certain level, and then the while loop was entered, and we started expanding nodes. If the node at hand has exceeded the max depth allowed, we would break from the while loop and start again until we reached a goal state.

Overall, this project serves as an example of various search algorithms applied to the Coast Guard problem.
