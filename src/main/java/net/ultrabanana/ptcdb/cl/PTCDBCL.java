package net.ultrabanana.ptcdb.cl;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;

public class PTCDBCL {
    public static void makeHtmlTable(Reader in, PrintWriter out, int indent) throws IOException {
        var lexer = new PTCDBCLLexer(CharStreams.fromReader(in));
        var parser = new PTCDBCLParser(new CommonTokenStream(lexer));
        var tree = parser.config();

        var listener = new HtmlPtcdbclListener(out);
        listener.setOuterIndent(indent);
        ParseTreeWalker.DEFAULT.walk(listener, tree);
    }

    public static void makeHtmlTable(Reader in, PrintWriter out) throws IOException {
        makeHtmlTable(in, out, 0);
    }
}
