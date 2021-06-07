package com.github.noblemathews.protobuggy.generatePsi;

import com.github.noblemathews.protobuggy.handleCpp.CDTGen;
import com.intellij.codeInspection.dataFlow.*;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.search.PsiShortNamesCache;
import org.eclipse.core.runtime.CoreException;
import org.jetbrains.annotations.NotNull;
import com.github.noblemathews.protobuggy.handleCpp.AntlrTreeGen;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UastContextKt;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class PsiAction extends AnAction {

    private String getPSIAsString(PsiFile file) {
        return DebugUtil.psiToString(file, true, true);
    }

    private void getAllClassesInProject(Project project){
//        returns all classes in project sim getAllFileNames
        String[] classNames = PsiShortNamesCache.getInstance(project).getAllClassNames();
//        for(String className: classNames) { cache.getClassesByName(name, GlobalSearchScope.projectScope(project);
        Messages.showMessageDialog(project, Arrays.toString(classNames), "All Classes Indexed", null);
    }

    private void iterateFilesProject(AnActionEvent event) {
        Project project = event.getProject();
//        List<String> classMain = new ArrayList<String>();
        assert project != null;
//      Processes all files and directories under content roots skipping excluded and ignored files and directories.
        ProjectFileIndex.SERVICE.getInstance(project).iterateContent(virtualFile -> {

            if (virtualFile.getFileType().getName().equals("JAVA")) {
                PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                assert psiFile != null;
                PsiJavaFile file = (PsiJavaFile) psiFile;
//                PsiJavaFile file = (PsiJavaFile) PsiFileFactory.getInstance(getProject()).createFileFromText("X.java", JavaFileType.INSTANCE, content);
                PsiMethod method = file.getClasses()[0].getMethods()[0];
//                file.getClasses()[0].getMethods()[0].getParameters()[0].getType()
//                UElement uastel = UastContextKt.toUElement(method.getReturnTypeElement(), UCallExpression.class);
                PsiCodeBlock body = method.getBody();
                DataFlowRunner runner = new DataFlowRunner(project, body) {};
                PsiParameter[] parameters = method.getParameterList().getParameters();
                final DfaValueFactory factory = runner.getFactory();

                //                for (int i = 0; i < contract.getParameterCount(); i++) {
//                    StandardMethodContract.ValueConstraint constraint = contract.getParameterConstraint(i);
//                    DfaValue comparisonValue = constraint.getComparisonValue(factory);
//                    if (comparisonValue != null) {
//                        boolean negated = constraint.shouldUseNonEqComparison();
//                        DfaVariableValue dfaParam = factory.getVarFactory().createVariableValue(parameters[i]);
//                        initialState.applyCondition(dfaParam.cond(RelationType.equivalence(!negated), comparisonValue));
//                    }
//                }
//
//                ContractChecker.ContractCheckerVisitor visitor = new ContractChecker.ContractCheckerVisitor(method, contract, ownContract);
//                runner.analyzeMethod(body, visitor, Collections.singletonList(initialState));

                Messages.showMessageDialog(project, Arrays.toString(parameters), "Visitor Response", null);


//                && ste.getMethodName().equals("processTryWithResources")
//                Check Iterated Files
//                Messages.showMessageDialog(project, ((ASTNode) fileASTNode).getText(), "File ASTNode", null);

//                Keep note of encountered classes
//                classMain.add(virtualFile.getNameWithoutExtension());

//                Show Java PSI ----
//                Messages.showMessageDialog(project, getPSIAsString(psiFile), "PSI Info", null);
            }
            else
//                if (Objects.equals(virtualFile.getExtension(), "cpp")){
                if (virtualFile.getFileType().getName().equals("C++")){
//                    need the header files - try out with clang
//                    virtualFile.getFileType().getName().equals("C++") this will take in h files as well
                String CppResponse;
//                ((String) LoadTextUtil.loadText(virtualFile))
//                    virtualFile.getPath()
                    try {
//                        CppResponse = AntlrTreeGen.generateAst(String.valueOf(LoadTextUtil.loadText(virtualFile)), project);
//                        String.valueOf(LoadTextUtil.loadText(virtualFile))
                        CDTGen.generateAst(virtualFile.getPath(), project);
                    } catch (CoreException e) {
                        e.printStackTrace();
                        Messages.showMessageDialog(project, String.valueOf(e), "Cpp Error", null);
                    }

//                Messages.showMessageDialog(project, CppResponse, "Cpp Ast Info", null);
            }
/*
            Get Skipped file type info
            else{
                Messages.showMessageDialog(project, virtualFile.getFileType().getName(), "Type Info", null);
            }
*/
            return true;
        });
//        Messages.showMessageDialog(project, String.valueOf(classMain), "Classes Checked", null);
//        direct method but lists even that of dependencies
//        getAllClassesInProject(project);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        iterateFilesProject(anActionEvent);
    }

    private void getClassesInFile(Project project, VirtualFile virtualFile){
        PsiJavaFile psiJavaFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(virtualFile);
        assert psiJavaFile != null;
        PsiClass[] javaFileClasses = psiJavaFile.getClasses();
//        getInnerClasses  Returns the list of class initializers for the class.
//        getAllInnerClasses  Searches the class (and optionally its superclasses) for the field with the specified name
        Messages.showMessageDialog(project, Arrays.toString(javaFileClasses), "Classes In File", null);
    }

    private void getModulesInProject(Project project){
        Module @NotNull [] arrayModules = ModuleManager.getInstance(project).getModules();
        Messages.showMessageDialog(project, Arrays.toString(arrayModules), "Modules Checked", null);
    }
}