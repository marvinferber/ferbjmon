#/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export LOGDIR=$HOME/.ferbjmon/capture 
##############################
echo
echo "---- 1 Thread ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_threadorder -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.mergesort.Main 1
mv $LOGDIR/threadorder.log $LOGDIR/parallelmergesort1.log
mv $LOGDIR/parallelmergesort1.svg $LOGDIR/parallelmergesort1.svg
echo
echo "---- 2 Threads ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_threadorder -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.mergesort.Main 2
mv $LOGDIR/threadorder.log $LOGDIR/parallelmergesort2.log
mv $LOGDIR/parallelmergesort2.svg $LOGDIR/parallelmergesort2.svg
echo
echo "---- 4 Thread ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_threadorder -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.mergesort.Main 4
mv $LOGDIR/threadorder.log $LOGDIR/parallelmergesort4.log
mv $LOGDIR/parallelmergesort4.svg $LOGDIR/parallelmergesort4.svg
echo
echo "-------------------------------------"
echo "#### All logfiles have been written to $LOGDIR"
echo
