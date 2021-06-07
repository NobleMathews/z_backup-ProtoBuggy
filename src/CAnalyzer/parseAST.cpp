#include "parseAST.h"
#include <iostream>
#include <clang-c/Index.h>  // This is libclang.
using namespace std;

JNIEXPORT jint JNICALL Java_org_test_TestParameters_func 
(JNIEnv* env, jobject thisObject)
{
  CXIndex index = clang_createIndex(0, 0);
  CXTranslationUnit unit = clang_parseTranslationUnit(
    index,
    "/home/aniruddhan/jni-example/testlibclang/header.hpp", nullptr, 0,
    nullptr, 0,
    CXTranslationUnit_None);
  if (unit == nullptr)
  {
    cerr << "Unable to parse translation unit. Quitting." << endl;
    exit(-1);
  }

  CXCursor cursor = clang_getTranslationUnitCursor(unit);
  clang_visitChildren(
    cursor,
    [](CXCursor c, CXCursor parent, CXClientData client_data)
    {
      cout << "Cursor kind: " << clang_getCursorKind(c) << endl;
      return CXChildVisit_Recurse;
    },
    nullptr);

  clang_disposeTranslationUnit(unit);
  clang_disposeIndex(index);
  
  return 0;
}