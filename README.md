#Rumor-fx
This is the repository for my *Computer networks and Distributed systems* class assignment.
I wrote a program that simulates the rumor spreading algorithms described in the paper, [How to Discreetly Spread a Rumor in a Crowd](http://people.cs.georgetown.edu/~cnewport/pubs/gn-disc2016.pdf) written by Mohsen Ghaffari and Calvin Newport in 2016.

The program was written in Kotlin with TornadoFX.
 
##Build and run
* Clone this repository `git clone https://github.com/pjozsef/Rumor-FX.git`
* Install a Java8 JDK
* Build
  * `./gradlew fatjar` on Linux
  * `./gradlew.bat fatjar` on Windows
* Run
  * `java -jar build/libs/rumorfx-all-1.0.0.jar`

##Usage
* Graph editor pane on the left
  * Add a new node by left clicking on the editor pane
  * Select a node by left clicking on it *(you to select a node to start the simulation)*
  * Delete a node by Ctrl + left clicking on it
  * Move a node by left mouse dragging it
  * Add a new edge by Shift + left click and dragging from one node to the other
  * Delete an edge by drawing a line crossing it with Shift + left click and drag
* Settings pane on the right
  * Adjust animation speed with the animation speed slider *(sliding it to zero disables animation and the simulation terminates immediately)*
  * Start a simulation with the `PUSH`, `PULL`, `PUSH-PULL B=0`, `PUSH-PULL B=1` buttons
  * To load a larger, predefined graph, click the `LOAD` button
  * To clear your current graph entirely, click the `Clear` button
  * While the simulation is running, you will see a label that shows how many rounds have passed since the start

Keep in mind that the simulations won't terminate if there are isolated nodes in the graph.