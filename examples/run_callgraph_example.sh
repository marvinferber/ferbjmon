#/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export LOGDIR=$HOME/.ferbjmon/capture 
##############################
echo
echo "---- Proper Synchronized Example (CallGraph) ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_callgraph -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.MyProperSynchronizedThreadTest
echo
echo "-------------------------------------"
echo "#### All logfiles have been written to $LOGDIR"
echo
