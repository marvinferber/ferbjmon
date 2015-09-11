#!/bin/bash
echo "==> Getting Dependencies Apache Batik and JBOSS Javassist"
echo "==> Apache Batik"
mkdir -p 3rdparty-libs && cd 3rdparty-libs
wget http://apache.mirror.digionline.de/xmlgraphics/batik/binaries/batik-1.7.1.zip
unzip batik-1.7.1.zip 
mv batik-1.7.1/ batik
rm batik-1.7.1.zip
echo "==> JBOSS Javassist"
git clone https://github.com/jboss-javassist/javassist.git
cd javassist
git checkout tags/rel_3_13_0_ga
ant jar
cd ../../
echo "==> All operations finished"
