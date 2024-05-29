package net.ultrabanana.ptcdb;

import net.ultrabanana.ptcdb.cl.HtmlPtcdbclListener;
import net.ultrabanana.ptcdb.cl.PTCDBCLLexer;
import net.ultrabanana.ptcdb.cl.PTCDBCLParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String... args) throws IOException {
        var lexer = new PTCDBCLLexer(CharStreams.fromFileName("example.pdc"));
        var parser = new PTCDBCLParser(new CommonTokenStream(lexer));
        var tree = parser.config();

        var stdpw = new PrintWriter(System.out);
        var listener = new HtmlPtcdbclListener(stdpw);

        ParseTreeWalker.DEFAULT.walk(listener, tree);

        stdpw.flush();
    }
}
