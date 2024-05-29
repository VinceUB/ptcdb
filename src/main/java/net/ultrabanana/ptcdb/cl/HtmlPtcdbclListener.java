package net.ultrabanana.ptcdb.cl;

import lombok.Setter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.PrintWriter;

public class HtmlPtcdbclListener implements PTCDBCLListener {
    private final PrintWriter writer;

    @Setter private int outerIndent = 0;
    private int innerIndent = 0;
    private int totalIndent(){return outerIndent + innerIndent;}

    public HtmlPtcdbclListener(PrintWriter writer){
        this.writer = writer;
    }

    private Card currentCard = null;

    @Override
    public void enterConfig(PTCDBCLParser.ConfigContext ctx) {
        writer.write("""
                <table>
                    <thead>
                        <tr>
                            <th>Common name</th>
                            <th>Description</th>
                            <th><abbr title="Generation">Gen.</abbr></t>
                            <th>Current owner</th>
                            <th>Ownership history</th>
                            <th>Image?</th>
                        </tr>
                    </thead>
                    <tbody>
                """.indent(outerIndent));
        innerIndent += 8;
    }

    @Override
    public void exitConfig(PTCDBCLParser.ConfigContext ctx) {
        writer.write("""
                    </tbody>
                </table>
                """.indent(outerIndent));
        innerIndent -= 8;
    }

    @Override
    public void enterEntry(PTCDBCLParser.EntryContext ctx) {
        currentCard = new Card();
    }

    @Override
    public void exitEntry(PTCDBCLParser.EntryContext ctx) {
        currentCard.setIndent(totalIndent());
        writer.write(currentCard.toHtml());
        currentCard = null;
    }

    private boolean inTitle = false;
    @Override
    public void enterTitle(PTCDBCLParser.TitleContext ctx) {
        if(inTitle)
            throw new RuntimeException("Attempting to create nested titles at " + ctx.start.getLine() +":"+ ctx.start.getCharPositionInLine() + ". How did you even manage that?");
        inTitle = true;
    }

    @Override
    public void exitTitle(PTCDBCLParser.TitleContext ctx) {
        if(!inTitle)
            throw new RuntimeException("Attempting to exit nested titles at " + ctx.start.getLine() +":"+ ctx.start.getCharPositionInLine() + ". How did you even manage that?");
        inTitle = false;
    }

    @Override
    public void enterTitleCardName(PTCDBCLParser.TitleCardNameContext ctx) {}

    @Override
    public void exitTitleCardName(PTCDBCLParser.TitleCardNameContext ctx) {
        currentCard.setName(ctx.CARD_NAME().getText());
    }

    CardProperty currentKey = null;
    @Override
    public void enterKvPair(PTCDBCLParser.KvPairContext ctx) {
        var descriptionName = ctx.KEY().getText().toLowerCase().strip();
        descriptionName = descriptionName.substring(0, descriptionName.length()-1);
        currentKey = switch (descriptionName) {
            case "description" -> CardProperty.DESCRIPTION;
            case "gen" -> CardProperty.GEN;
            case "owner" -> CardProperty.OWNER;
            case "history" -> CardProperty.HISTORY;
            case "image" -> CardProperty.IMAGE;
            default -> throw new RuntimeException("Unknown property \"" + descriptionName + "\" at " + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
        };
    }

    @Override
    public void exitKvPair(PTCDBCLParser.KvPairContext ctx) {
        currentKey = null;
    }

    StringBuilder currentValue = null;
    @Override
    public void enterValue(PTCDBCLParser.ValueContext ctx) {
        currentValue = new StringBuilder();
    }

    @Override
    public void exitValue(PTCDBCLParser.ValueContext ctx) {
        var v = currentValue.toString();
        switch(currentKey){
            case DESCRIPTION -> currentCard.setDescription(v);
            case GEN -> currentCard.setGeneration(v);
            case OWNER -> currentCard.setOwnersText(v);
            case HISTORY -> currentCard.setHistory(v);
            case IMAGE -> currentCard.setImage(v);
        }
        currentValue = null;
    }

    @Override
    public void enterMultiLine(PTCDBCLParser.MultiLineContext ctx) {}

    @Override
    public void exitMultiLine(PTCDBCLParser.MultiLineContext ctx) {}

    @Override
    public void enterSameLine(PTCDBCLParser.SameLineContext ctx) {}

    @Override
    public void exitSameLine(PTCDBCLParser.SameLineContext ctx) {}

    @Override
    public void enterMultiLineContent(PTCDBCLParser.MultiLineContentContext ctx) {}

    @Override
    public void exitMultiLineContent(PTCDBCLParser.MultiLineContentContext ctx) {}

    @Override
    public void enterStrWhitespace(PTCDBCLParser.StrWhitespaceContext ctx) {}

    @Override
    public void exitStrWhitespace(PTCDBCLParser.StrWhitespaceContext ctx) {
        currentValue.append(ctx.WHITESPACE().getText());
    }

    @Override
    public void enterStrNewline(PTCDBCLParser.StrNewlineContext ctx) {}

    @Override
    public void exitStrNewline(PTCDBCLParser.StrNewlineContext ctx) {
        currentValue.append("<br/>");
    }

    @Override
    public void enterWord(PTCDBCLParser.WordContext ctx) {}

    @Override
    public void exitWord(PTCDBCLParser.WordContext ctx) {}

    @Override
    public void enterName(PTCDBCLParser.NameContext ctx) {}

    @Override
    public void exitName(PTCDBCLParser.NameContext ctx) {
        var owner = ctx.OWNER().getText();

        if(currentKey==CardProperty.OWNER){
            currentCard.addPossibleOwner(owner);
            currentValue.append(owner);
        } else {
            var ownerLower = owner.toLowerCase();
            currentValue
                    .append("<span class=\"").append(ownerLower).append("\">")
                    .append(owner)
                    .append("</span>");
        }
    }

    @Override
    public void enterCardName(PTCDBCLParser.CardNameContext ctx) {}

    @Override
    public void exitCardName(PTCDBCLParser.CardNameContext ctx) {
        var cardName = ctx.CARD_NAME().getText();
        currentValue
                .append("<em>")
                .append(cardName)
                .append("</em>");
    }

    @Override
    public void enterNormalWord(PTCDBCLParser.NormalWordContext ctx) {}

    @Override
    public void exitNormalWord(PTCDBCLParser.NormalWordContext ctx) {
        var word = ctx.getText();
        currentValue.append(word);
    }

    @Override
    public void visitTerminal(TerminalNode node) {

    }

    @Override
    public void visitErrorNode(ErrorNode node) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {

    }
}

enum CardProperty {
    DESCRIPTION, GEN,
    OWNER, HISTORY, IMAGE
}