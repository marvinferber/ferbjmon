#/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export LOGDIR=$HOME/.ferbjmon/capture 
##############################
echo
echo "---- Unsynchronized Example ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_threadorder -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.MyUnsynchronizedThreadTest
mv $LOGDIR/threadorder.log $LOGDIR/unsychronized.log
mv $LOGDIR/threadorder.svg $LOGDIR/unsychronized.svg
echo
echo "---- Improper Synchronized Example ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_threadorder -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.MyImproperSynchronizedThreadTest
mv $LOGDIR/threadorder.log $LOGDIR/improper.log
mv $LOGDIR/threadorder.svg $LOGDIR/improper.svg
echo
echo "---- Proper Synchronized Example (FastLogger) ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_threadorder -Dthreadorder.logger=FastLogger -Dthreadorder.logger.size=1000 -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.MyProperSynchronizedThreadTest
mv $LOGDIR/threadorder.log $LOGDIR/proper_fastlogger.log
mv $LOGDIR/threadorder.svg $LOGDIR/proper_fastlogger.svg
echo
echo "---- Proper Synchronized Example (StringBuilderLogger) ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_threadorder -Dthreadorder.logger=StringBuilderLogger -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.MyProperSynchronizedThreadTest
mv $LOGDIR/threadorder.log $LOGDIR/proper_stringbuilderlogger.log
mv $LOGDIR/threadorder.svg $LOGDIR/proper_stringbuilderlogger.svg
echo
echo "---- Proper Synchronized Example default (TmpFileLogger) ----"
echo
echo "#### Running Example Program"
$DIR/../dist/java_threadorder -cp $DIR/../build/examples/classes/ de.edu.ferbjmon.example.MyProperSynchronizedThreadTest
mv $LOGDIR/threadorder.log $LOGDIR/proper.log
mv $LOGDIR/threadorder.svg $LOGDIR/proper.svg
echo
echo "-------------------------------------"
echo "#### All logfiles have been written to $LOGDIR"
echo
