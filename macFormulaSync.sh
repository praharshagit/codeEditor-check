
if ! JAVA_HOME=/Applications/SimplifyQA-CodeEditor.app/Contents/Resources/app/resources/jdk/Contents/Home /Applications/SimplifyQA-CodeEditor.app/Contents/Resources/app/resources/maven/bin/mvn compile exec:java -PsyncFormula; then
    echo -e "${OS/Darwin/\033[1m\033[31m}============================ COULD NOT SYNC CUSTOM FORMULAS.. REFER ABOVE ERROR/INFO IN LOGS ===================================="
else
    echo -e "${OS/Darwin/\033[1m\033[32m}================================ CUSTOM FORMULA SYNC REQUEST COMPLETED SUCCESSFULLY ==============================="
fi
