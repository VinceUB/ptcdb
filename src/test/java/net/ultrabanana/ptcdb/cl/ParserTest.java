package net.ultrabanana.ptcdb.cl;

//Useful reference: https://ssricardo.github.io/2018/junit-antlr-parser/

import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestToken implements Token{
    private final String text;
    private final int type;

    TestToken(String text, int type){
        this.text = text;
        this.type = type;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getLine() {
        return -1;
    }

    @Override
    public int getCharPositionInLine() {
        return -1;
    }

    @Override
    public int getChannel() {
        //throw new UnsupportedOperationException();
        return DEFAULT_CHANNEL;
    }

    @Override
    public int getTokenIndex() {
        return -1;
    }

    @Override
    public int getStartIndex() {
        return -1;
    }

    @Override
    public int getStopIndex() {
        return -1;
    }

    @Override
    public TokenSource getTokenSource() {
        return null;
    }

    @Override
    public CharStream getInputStream() {
        return null;
    }
}

public class ParserTest {
    private PTCDBCLParser createParser(List<TestToken> tokens){
        return new PTCDBCLParser(
                new CommonTokenStream(
                        new ListTokenSource(tokens)
                )
        );
    }

    @Test
    void testTitle(){
        var title = createParser(List.of(new TestToken[]{
                new TestToken("Tester Martin", PTCDBCLLexer.CARD_NAME),
                new TestToken("", PTCDBCLLexer.TITLE_UNDERSCORE)
        }));

        assertEquals(title.title().titleCardName().getText(), "Tester Martin");
    }
}
