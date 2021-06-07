# First install libclang-dev
# Remember to set your JAVA_HOME env var
JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64

# Set your OS here in -I option
clang++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -I/usr/lib/llvm-10/include parseAST.cpp -o parseAST.o

clang++ -lc -shared -fPIC -lclang -o native.so parseAST.o 