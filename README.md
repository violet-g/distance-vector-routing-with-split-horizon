# Distance Vector Routing Agorithm simulations with Split Horizon functionality

## Usage:

Compile with: javac DVRRunner.java Network.java NetworkFileReader.java Node.java


Then, the user can choose what behaviour they would want to inspect. The program supports the following commands:
- ##### java DVRRunner iterate {numIterations}
Using this command, the algorithm will be run on the network provided in the input file for a specific number of iterations

- ##### java DVRRunner converge (split-horizon)
Using this command, the algorithm will be run until convergence. The user might also want to apply the split-horizon functionality by simply adding it as a second argument

- ##### java DVRRunner route N{#} N{#} {numIterations}
Using this command, the user can check what the shortest path found from one node to another is after a specific number of iterations

- ##### java DVRRunner trace {numIterations} N{#} N{#} …
Using this command, the routing tables for any number of nodes can be checked throughout a set number of executions of the algorithm

- ##### java DVRRunner fail N{#} N{#} {numIterations} (split-horizon)
Using this command, the user can execute the algorithm for a fixed number of iterations after which a specific link in the network can be intentionally failed. Then, the network will continue exchanging information until it converges. If it takes too long to converge, the user might want to add the split-horizon flag in order to avoid the count-to-infinity problem again.

- ##### java DVRRunner change-cost N{#} N{#} {cost} {numIterations} (split-horizon)
With this command, the user can change the cost of a specific link after a certain number of executions of the algorithm. Then, the network will exchange information until convergence. Again, if it takes too long to converge, the split-horizon tag can be added to avoid this.

