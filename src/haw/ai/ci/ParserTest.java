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
}
