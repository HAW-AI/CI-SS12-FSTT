package haw.ai.ci;

/**
 * The symbol table.
 * 
 * Declarations (used in Module and Procedure) need the parent table to look
 * up typedefs ("TYPE t = ARRAY[6] OF INTEGER"). Thus, the symbol table must
 * be built incrementally.
 * 
 * Required constructors:
 * SymbolTable()
 * SymbolTable(SymbolTable parent)
 */
public interface SymbolTable {
    void declare(String ident, Descriptor descr);
    Descriptor descriptorFor(String ident);
    int globalAddressOf(String ident);
    int localAddressOf(String ident);
    int size();
}
