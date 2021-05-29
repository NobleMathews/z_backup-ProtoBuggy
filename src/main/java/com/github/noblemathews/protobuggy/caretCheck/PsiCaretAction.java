package com.github.noblemathews.protobuggy.caretCheck;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.slicer.LanguageSlicing;
import com.intellij.slicer.SliceHandler;
import com.intellij.slicer.SliceLanguageSupportProvider;
import com.intellij.slicer.SliceManager;

import java.io.File;
import java.util.Objects;

public class PsiCaretAction extends AnAction {

    public PsiElement getExpressionAtCaret(final Editor editor, final PsiFile file) {
        int offset = TargetElementUtil.adjustOffset(file, editor.getDocument(), editor.getCaretModel().getOffset());
        if (offset == 0) {
            return null;
        }
        PsiElement atCaret = file.findElementAt(offset);

        SliceLanguageSupportProvider provider = LanguageSlicing.getProvider(file);
        if (provider == null || atCaret == null) {
            return null;
        }
        return provider.getExpressionAtCaret(atCaret, true);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (editor == null || psiFile == null) {
            return;
        }
        int offset = editor.getCaretModel().getOffset();

//        PsiElement element = new SliceHandler(true).getExpressionAtCaret(editor, psiFile);

        SliceManager sliceManager = SliceManager.getInstance(Objects.requireNonNull(anActionEvent.getProject()));
//        sliceManager.slice(getExpressionAtCaret(editor,psiFile),true, new SliceHandler());

        final StringBuilder infoBuilder = new StringBuilder();
        PsiElement element = psiFile.findElementAt(offset);
        infoBuilder.append("Element at caret: ").append(element).append("\n");
        if (element != null) {
            PsiMethod containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
            infoBuilder
                    .append("Containing method: ")
                    .append(containingMethod != null ? containingMethod.getName() : "none")
                    .append("\n");
            if (containingMethod != null) {
                PsiClass containingClass = containingMethod.getContainingClass();
                infoBuilder
                        .append("Containing class: ")
                        .append(containingClass != null ? containingClass.getName() : "none")
                        .append("\n");

                infoBuilder.append("Local variables:\n");
                containingMethod.accept(new JavaRecursiveElementVisitor() {
                    @Override
                    public void visitLocalVariable(PsiLocalVariable variable) {
                        super.visitLocalVariable(variable);
                        infoBuilder.append(variable.getName()).append("\n");
                    }
                });
            }
        }
        Messages.showMessageDialog(anActionEvent.getProject(), infoBuilder.toString(), "PSI Info", null);
    }

    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        e.getPresentation().setEnabled(editor != null && psiFile != null);
    }

}