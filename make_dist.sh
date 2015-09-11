#!/bin/bash
echo "==> Building FerbJmon Tools into folder dist"
echo "==> Building will FAIL if get_dependencies.sh has not been executed before!"
echo "==> Building callgraph"
cd callgraph 
ant
cd ..
echo "==> Building threadorder"
cd threadorder
ant
cd ..
echo "==> Copying wrapper scripts and 3rd party libs to dist"
mkdir -p dist/libbatik
cp 3rdparty-libs/batik/*.jar dist/libbatik/
cp 3rdparty-libs/batik/NOTICE dist/libbatik/
cp 3rdparty-libs/batik/LICENSE dist/libbatik/
cp -r 3rdparty-libs/batik/extensions dist/libbatik/
cp -r 3rdparty-libs/batik/lib dist/libbatik/
cp 3rdparty-libs/javassist/javassist.jar dist/
cp 3rdparty-libs/javassist/License.html dist/javasisst_license.html
cp scripts/java_* dist/
echo "==> Build finished"
echo "==> Creating common output folder for visualizations: capture"
mkdir -p dist/capture
echo "==> Modifying Thread.class in a test run"
cd build
echo "@de.ubt.ferbjmon.annotation.Monitored public class Main { 	public static void main(String[] args) {System.out.println(\"Hello World\");}}" > Main.java
javac -cp ../dist/threadorder.jar Main.java
../dist/java_threadorder Main
zip -u ../dist/threadorder.jar java/lang/Thread.class 
echo "==> Thread.class modification finished"
echo "==> This version of FerbJmon Tools was just created for Linux and the Java version shown below."
echo "==> You need to rebuild for a different Java version!"
java -version
