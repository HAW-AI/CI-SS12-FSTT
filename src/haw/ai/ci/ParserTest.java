package haw.ai.ci;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.*;

public class ParserTest {
    private Parser createParser(String code) {
        Scanner scanner = new Scanner(new StringReader(code));
        Parser parser = new Parser(scanner, "test");
        parser.insymbol();
        return parser;
    }
    
    @Test
    public void testProgram() {
        AbstractNode actual = createParser("123\n32 1").program();
        AbstractNode expected = new IntNode(1);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConstIdent() {
        AbstractNode actual = createParser("varn3ame_").constIdent();
        AbstractNode expected = new IdentNode("varn3ame_");
        assertEquals(expected, actual);
    }
    
    @Test(expected=ParserException.class)
    public void testConstIdentNeg1() {
        createParser("1varname").constIdent();
    }
    
    @Test(expected=ParserException.class)
    public void testConstIdentNeg2() {
        createParser("_varname").constIdent();
    }
}
