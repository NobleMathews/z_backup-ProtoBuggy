
1.  On linux distros, first install libclang-dev & java 
2.  Then ensure that the paths are correctly set in generatelib.sh into clang libraries. Ensure that -I flag is appropriate to your OS.
3.  You can add new functions to parseAST.cpp but include appropriate headers in parseAST.h
4.  bash generatelib.sh - To generate native shared library
5.  In the appropriate java class ensure that you load the native shared library using System.load("native.so"); using correct absolute path
6.  On the Java side you can declare functions from C++ which will be loaded from this native library with the 'native' keyword
eg. public native String func();
    As an example func signature has been also added to the repository in generatePsi/PsiAction.java