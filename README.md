# FerbJmon Tools
## Visualizing Thread Access on Java Objects using Light-weight Runtime Monitoring
FerbJmon Tools can be used to monitor and visualize the accesses of threads on Java objects. The monitoring is injected into user code via bytecode instrumentation using a Java Agent. FerbJmon Tools can create timeline diagrams and callgraphs from Java programs. Its main purpose is the visualization of concurrent behavior in real programs. FerbJmon Tools are especially useful for teaching parallel and distributed programming in Java.

### Tools
Currently FerbJmon Tools consist of two separate tools:
* `java_callgraph`
* `java_threadorder`

The wrapper scripts provided can be used as replacements for the `java` command line program. Instead of 
`java -cp <Classpath> package.Main`
use
`java_threadorder -cp <Classpath> package.Main`.

### Building & Installation
The FerbJmon Tool `java_threadorder` uses a modified `java.lang.Thread` class, which is Java version dependent. Once compiled, the tools will only work with the Java version they were compiled with, e.g., Java 7. For a different Java version, the tools just need to be rebuilt with the corresponding compiler.

#### Linux (tested on Ubuntu 12.04 and Linux Mint 13)
1. for building the following packages are required: java-jdk, ant, zip, unzip, git ;-)
1. clone FerbJmon into a local folder (`git clone https://github.com/marvinferber/ferbjmon.git`)
2. cd to the newly created git repo of FerbJmon (`cd ferbjmon`)
3. get the dependencies using the script provided (`./getdependencies.sh`)
4. build the FerbJmon Tools using the script provided (`./make_dist.sh`)
5. on successful completion everything needed will be located in the `dist` folder and can be invoked from any directory 

#### Windows
TODO

### Dependencies
FerbJmon Tools are built on the open source software products named below:
* [Javassist](http://jboss-javassist.github.io/javassist/)
* [Apache Batik SVG Toolkit](https://xmlgraphics.apache.org/batik/)
