package de.epdv.module.netbeans.editor.antlr4.lexer;

public enum TokenType {
    TOKEN_REF(1, "reference"),
    RULE_REF(2, "reference"),
    LEXER_CHAR_SET(3, "keyword"),
    DOC_COMMENT(4, "comment"),
    BLOCK_COMMENT(5, "comment"),
    LINE_COMMENT(6, "comment"),
    INT(7, "keyword"),
    STRING_LITERAL(8, "character"),
    UNTERMINATED_STRING_LITERAL(9, "error"),
    BEGIN_ARGUMENT(10, "keyword"),
    BEGIN_ACTION(11, "keyword"),
    OPTIONS(12, "special"),
    TOKENS(13, "keyword"),
    CHANNELS(14, "keyword"),
    IMPORT(15, "keyword"),
    FRAGMENT(16, "keyword"),
    LEXER(17, "keyword"),
    PARSER(18, "keyword"),
    GRAMMAR(19, "keyword"),
    PROTECTED(20, "keyword"),
    PUBLIC(21, "keyword"),
    PRIVATE(22, "keyword"),
    RETURNS(23, "keyword"),
    LOCALS(24, "keyword"),
    THROWS(25, "keyword"),
    CATCH(26, "keyword"),
    FINALLY(27, "keyword"),
    MODE(28, "keyword"),
    COLON(29, "operator"),
    COLONCOLON(30, "operator"),
    COMMA(31, "operator"),
    SEMI(32, "operator"),
    LPAREN(33, "operator"),
    RPAREN(34, "operator"),
    LBRACE(35, "operator"),
    RBRACE(36, "operator"),
    RARROW(37, "operator"),
    LT(38, "operator"),
    GT(39, "operator"),
    ASSIGN(40, "operator"),
    QUESTION(41, "operator"),
    STAR(42, "operator"),
    PLUS_ASSIGN(43, "operator"),
    PLUS(44, "operator"),
    OR(45, "keyword"),
    DOLLAR(46, "operator"),
    RANGE(47, "keyword"),
    DOT(48, "operator"),
    AT(49, "operator"),
    POUND(50, "operator"),
    NOT(51, "keyword"),
    ID(52, "identifier"),
    WS(53, "whitespace"),
    ERRCHAR(54, "error"),
    END_ARGUMENT(55, "keyword"),
    UNTERMINATED_ARGUMENT(56, "error"),
    ARGUMENT_CONTENT(57, "default"),
    END_ACTION(58, "keyword"),
    UNTERMINATED_ACTION(59, "error"),
    ACTION_CONTENT(60, "action-content"),
    UNTERMINATED_CHAR_SET(61, "error");

    public int id;
    public String category;
    public String text;

    private TokenType(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public static TokenType valueOf(int id) {
        TokenType[] values = values();
        for (TokenType value : values) {
            if (value.id == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("The id " + id + " is not recognized");
    }
}
