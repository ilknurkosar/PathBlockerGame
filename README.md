# Path Blocker Game - A* Search Algorithm with Elevation

## Overview
This project implements an **A* search algorithm** to solve the **Path Blocker** game, incorporating **elevation-based movement costs**. The player moves from a **starting position (S)** to a **goal position (G)** while navigating through obstacles and varying terrain heights.

## Features
- **Optimal Pathfinding:** Uses the **A* algorithm** to guarantee the shortest path with the lowest cost.
- **Height-Aware Path Calculation:** The movement cost considers both the Manhattan distance and the elevation difference.
- **Real-Time Visualization:** Each step updates the grid dynamically, marking visited paths.
- **Object-Oriented Design:** The project follows clean **OOP principles** to improve readability and maintainability.
- **Dynamic Terrain Generation:** Supports randomly generated elevations using a `Scene` class or reading predefined maps from text files.

## Search Approach
The project employs the **A* algorithm**, which guarantees an **optimal solution** when using a correct heuristic function. In the **PathFinder** class, the heuristic function combines **Manhattan distance** with **height differences**, ensuring that the search is efficient and accurate.

### Why A*?
- **Guaranteed Optimality:** A* finds the shortest possible path when a consistent heuristic is used.
- **Efficient Pruning:** Expands fewer nodes than uninformed search algorithms (e.g., BFS, DFS).
- **Flexible Heuristic:** Can be modified to incorporate different cost metrics, like terrain difficulty.

## Implementation Details
### Data Structures Used
- **Priority Queue (Min-Heap):** Used in A* to prioritize nodes with the lowest estimated cost.
- **HashMap (`costSoFar`)**: Tracks the minimum cost to reach each node efficiently.
- **2D Array (`grid[][]`)**: Stores the terrain, walls, and elevation values.

### Movement Cost Calculation
The **cost to move** from a cell `(x1, y1)` to `(x2, y2)` is calculated as:

```
Cost = 1 + |height(x2, y2) - height(x1, y1)|
```

This ensures that moving over steep terrain is more expensive than moving on flat ground.

### Complexity Analysis
#### **Time Complexity:**
- **Worst Case:** `O(b^d)`, where `b` is the branching factor and `d` is the depth of the optimal solution.
- **Practical Case:** Significantly faster due to heuristic pruning.

#### **Space Complexity:**
- **Worst Case:** `O(b^d)`, storing all explored nodes.
- **Optimized:** Uses **priority queues** and **hashmaps** to efficiently manage memory.
