#!/bin/sh

# this script must be called from the project's root dir

# jflex must be in your path
# e.g. by doing `ln -s /path/to/jflex/bin/jflex ~/bin`


echo "----------\ngenerating parser code\n----------\n"
jflex -d src/haw/ai/ci input/grammar.flex

echo "\n----------\ncompiling code\n----------\n"
javac -d bin -cp /usr/share/java/junit.jar src/haw/ai/ci/*.java

echo "\n----------\nrunning tests\n----------\n"
java -cp bin:/usr/share/java/junit.jar org.junit.runner.JUnitCore haw.ai.ci.ParserTest
