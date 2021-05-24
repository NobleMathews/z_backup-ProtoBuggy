package com.github.noblemathews.protobuggy.handleCpp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.io.IOUtils;
import org.snt.inmemantlr.GenericParser;
import org.snt.inmemantlr.exceptions.CompilationException;
import org.snt.inmemantlr.exceptions.IllegalWorkflowException;
import org.snt.inmemantlr.exceptions.ParsingException;
import org.snt.inmemantlr.listener.DefaultTreeListener;
import org.snt.inmemantlr.tree.ParseTree;
import org.snt.inmemantlr.utils.FileUtils;

//https://github.com/antlr/antlr4/blob/master/doc/getting-started.md have to do this

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class AntlrTreeGen {
    public static String generateAst(String src, Project project) throws IOException {
        String fileName = "config/Cpp14.g4";
        ClassLoader classLoader = AntlrTreeGen.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        assert inputStream != null;
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        try {
            GenericParser gp = new GenericParser(content);
            DefaultTreeListener dlist = new DefaultTreeListener();
            gp.setListener(dlist);
            gp.compile();
            ParserRuleContext ctx = gp.parse(src, "compilationUnit", GenericParser.CaseSensitiveType.NONE);
            ParseTree pt = dlist.getParseTree();
            Messages.showMessageDialog(project, pt.toXml(), "Cpp Ast Info", null);
            return pt.toXml();
        } catch (CompilationException | ParsingException | IllegalWorkflowException e) {
            Messages.showMessageDialog(project, String.valueOf(e), "Cpp Error", null);
            e.printStackTrace();
        }

        return "";
    }
}