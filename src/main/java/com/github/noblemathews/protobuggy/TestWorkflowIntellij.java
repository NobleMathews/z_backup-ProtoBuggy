//package com.github.noblemathews.protobuggy;
//
//import com.intellij.codeInspection.dataFlow.ControlFlowAnalyzer;
//import com.intellij.codeInspection.dataFlow.DataFlowRunner;
//import com.intellij.codeInspection.dataFlow.StandardInstructionVisitor;
//import com.intellij.ide.highlighter.JavaFileType;
//import com.intellij.psi.PsiCodeBlock;
//import com.intellij.psi.PsiFileFactory;
//import com.intellij.psi.PsiJavaFile;
//import com.intellij.psi.PsiMethod;
//import com.intellij.testFramework.BombedProgressIndicator;
//import com.intellij.testFramework.LightProjectDescriptor;
//import org.intellij.lang.annotations.Language;
//import org.jetbrains.annotations.NotNull;
//import java.util.function.Predicate;
//
//public class TestWorkflowIntellij {
//    @Language("Java") String content = "import java.util.*;\n" +
//            "import java.util.function.*;\n" +
//            "import java.util.stream.*;\n" +
//            "\n" +
//            "/** @noinspection ConstantConditions*/ class X {\n" +
//            "  List<Integer> testTryCatchInStream(List<String> input) {\n" +
//            "    return input.stream().map(a -> {\n" +
//            "      try {\n" +
//            "        return Integer.parseInt(a);\n" +
//            "      }\n" +
//            "      catch (NumberFormatException ex) {\n" +
//            "        return -1;\n" +
//            "      }\n" +
//            "    }).filter(Objects::nonNull).collect(Collectors.toList());\n" +
//            "  }\n" +
//            "}\n";
//    PsiJavaFile file = (PsiJavaFile)PsiFileFactory.getInstance(getProject()).createFileFromText("X.java", JavaFileType.INSTANCE, content);
//    PsiMethod method = file.getClasses()[0].getMethods()[0];
//    PsiCodeBlock body = method.getBody();
//    assertNotNull(body);
//    DataFlowRunner runner = new DataFlowRunner(getProject(), body) {};
//
//}
