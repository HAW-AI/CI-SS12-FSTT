package haw.ai.ci;
import static org.junit.Assert.*;
import haw.ai.ci.descriptor.Descriptor;

import org.junit.Test;




public class SymbolTableTest {
	
	
	private class TestDescriptor implements Descriptor {
		private int size;
		
		public TestDescriptor(int size){
			this.size = size;
		}
		
		public int size(){
			return this.size;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + size;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestDescriptor other = (TestDescriptor) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (size != other.size)
				return false;
			return true;
		}

		private SymbolTableTest getOuterType() {
			return SymbolTableTest.this;
		}
		
		
	}

	@Test
	public void test() {
		Descriptor d = new TestDescriptor(5);
		Descriptor x = new TestDescriptor(2);
		
		SymbolTable table = new SymbolTable();
		assertEquals(0,table.size());
		
		table.declare("d", d);
		assertEquals(5,table.size());
		assertEquals(d,table.descriptorFor("d"));
		assertEquals(0,table.addressOf("d"));
		
		table.declare("x", x);
		assertEquals(7,table.size());
		assertEquals(x,table.descriptorFor("x"));
		assertEquals(5,table.addressOf("x"));
	}

}
