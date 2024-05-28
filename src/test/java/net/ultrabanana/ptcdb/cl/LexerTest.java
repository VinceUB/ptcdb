package net.ultrabanana.ptcdb.cl;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Useful reference: https://ssricardo.github.io/2018/junit-antlr-lexer/

public class LexerTest {
    private List<Token> getTokensFromText(String text){
        var lexer = new PTCDBCLLexer(CharStreams.fromString(text));
        var tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();
        return tokenStream.getTokens();
    }

    @Test
    public void testTitle() {
        var tokens = getTokensFromText("""
                Tester Martin
                -------------""");

        assertEquals(tokens.size(), 3); //Third one is EOF
        assertEquals(tokens.get(0).getType(), PTCDBCLLexer.CARD_NAME);
        assertEquals(tokens.get(0).getText().trim(), "Tester Martin");
        assertEquals(tokens.get(1).getType(), PTCDBCLLexer.TITLE_UNDERSCORE);
        assertEquals(tokens.get(2).getType(), PTCDBCLLexer.EOF);
    }
}
