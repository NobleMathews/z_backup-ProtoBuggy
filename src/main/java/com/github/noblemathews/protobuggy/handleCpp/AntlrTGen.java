package com.github.noblemathews.protobuggy.handleCpp;

import com.github.noblemathews.protobuggy.cpp.cppLexer;
import com.github.noblemathews.protobuggy.cpp.cppParser;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.io.IOUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AntlrTGen {
    public static String generateAst(String source_code, Project project) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        CodePointCharStream stream_from_string = CharStreams.fromString(source_code);
        cppLexer lexer = new cppLexer(new ANTLRInputStream(source_code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cppParser parser = new cppParser(tokens);

        String parserName = "cppParser";
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class<? extends Parser> parserClass = null;
        parserClass = cl.loadClass(parserName).asSubclass(Parser.class);

        String startRuleName = "translationunit"; //as specified in my CPP14.g4 file
        Method startRule = parserClass.getMethod(startRuleName);
        ParserRuleContext tree = (ParserRuleContext)startRule.invoke(parser, (Object[])null);
        Messages.showMessageDialog(project, tree.toStringTree(parser), "Cpp Ast Info", null);

        return "";
    }
}