package com.github.noblemathews.protobuggy.handleCpp;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

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

    private static String getTreeText(TreeModel model, Object object, String indent) {
        String myRow = indent + object + "\n";
        for (int i = 0; i < model.getChildCount(object); i++) {
            myRow += getTreeText(model, model.getChild(object, i), indent + "  ");
        }
        return myRow;
    }
        public static String generateAst(String fileContents, Project project) throws CoreException {
//
//            JFileChooser fileChooser = new JFileChooser();
//
//            fileChooser.setCurrentDirectory(new File("./docs"));
//
//            int result = fileChooser.showOpenDialog (frame);
//            if (result != JFileChooser.APPROVE_OPTION)
//                return "";

            FileContent fileContent = FileContent.createForExternalFileLocation(fileContents);
//            .createForExternalFileLocation (fileChooser.getSelectedFile().toString());
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

//            JTree jTree1 = new JTree();
//            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Table Data");
//            for(int i = 0; i < treeTable.getRowCount(); i++){
//
//                int rowIndex = i+1;
//
//                DefaultMutableTreeNode row = new DefaultMutableTreeNode("Row "+ rowIndex);
//
//                // columns
//                for(int c = 0; c < treeTable.getColumnCount(); c++)
//                {
//                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(treeTable.getValueAt(i, c));
//                    // add data to the row
//                    row.add(node);
//                }
//                // add the row to the root
//                root.add(row);
//            }
//            DefaultTreeModel model = new DefaultTreeModel(root);
//            jTree1.setModel(model);

            JTree innerTable = new JTree();

            ArrayList<String> list = new ArrayList<String>();
            for(int i = 0;i<treeTable.getModel().getRowCount();i++)
            {
//                treeTable.getModel().getValueAt(i,0);
                TableCellRenderer tableCellRenderer = treeTable.getCellRenderer(i, 0);
                Component c = treeTable.prepareRenderer(tableCellRenderer, i, 0);
                    innerTable = (JTree) c;
            }
            TreeModel model = innerTable.getModel();


//            ArrayList<String> data = new ArrayList<String>();
//                for (int row = 0; row < innerTable.getRowCount(); row++){
//                    for (int column = 0; column < innerTable.getColumnCount(); column++){
//                        data.add(innerTable.getValueAt(row, column).toString());
//                    }
//                }
            Messages.showMessageDialog(project, getTreeText(model, model.getRoot(), ""), "Cpp Got", null);

            JFrame frame = new JFrame("ASTViewer");
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
