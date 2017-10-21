package snippets;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import flexjson.JSONSerializer;

public class TestFlexJson {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProductInfo P1 = new ProductInfo(1,"prod1");
		ProductInfo P2 = new ProductInfo(2,"prod2");
	    ArrayList infos = new ArrayList();
	    infos.add(P1);
	    infos.add(P2);
		JSONSerializer s = new JSONSerializer();
		
		
		System.out.println(s.exclude("*.class","description"). serialize(infos));
	
		
	}
	
	public static class ProductInfo{
		private int id;
	    private String name;
	    private String desc; 
	    public ProductInfo(int id, String name) {
	      this.id = id;
	      this.name = name;
	    }
	    public int getProductId() { return this.id; }
	    public String getName() { return this.name; }
	    public String getDescription() { return this.desc; }
	}

}
