package com.github.noblemathews.protobuggy.handleCpp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;

import treeview.JTreeTable;
import treeview.ast.ASTTreeModel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.runtime.CoreException;

public class CDTGen {
        public static String generateAst(String fileContents, Project project) throws CoreException {
            JFrame frame = new JFrame("ASTViewer");

            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setCurrentDirectory(new File("./docs"));

            int result = fileChooser.showOpenDialog (frame);
            if (result != JFileChooser.APPROVE_OPTION)
                return "";

            FileContent fileContent = FileContent.createForExternalFileLocation (fileChooser.getSelectedFile().toString());
            Map definedSymbols = new HashMap();
            String[] includePaths = new String[0];
            IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
            IParserLogService log = new DefaultLogService();

            IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

            int opts = 8;
            IASTTranslationUnit translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);

            IASTPreprocessorIncludeStatement[] includes = translationUnit.getIncludeDirectives();
            for (IASTPreprocessorIncludeStatement include : includes) {
                System.out.println("include - " + include.getName());
            }
            JTreeTable treeTable = new JTreeTable(new ASTTreeModel(translationUnit));

            Messages.showMessageDialog(project, String.valueOf(treeTable), "Cpp Error", null);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing( WindowEvent we ) {
                    System.exit(0);
                }
            });
            JScrollPane scrollPane = new JScrollPane(treeTable);
            frame.getContentPane().add(scrollPane);
            frame.pack();
            frame.show();

        return "";
    }
}
