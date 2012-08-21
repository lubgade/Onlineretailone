package snippets;

import java.util.Iterator;
import java.util.Set;

import com.cloudjini.onlineretailone.entities.ShoppingCart;
import com.cloudjini.onlineretailone.entities.ShoppingCartItem;
import com.google.appengine.api.datastore.KeyFactory;

import flexjson.JSONSerializer;

public class textFlexJson {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ShoppingCart cart = new ShoppingCart();
		ShoppingCartItem item1 = new ShoppingCartItem();
		
	   /* item1.setKey(20l); */
		item1.setProductId((long) 162);
		item1.setProductName("item1");
		item1.setProductprice((double) 52);
		item1.setQuantity((double) 2);
		item1.setAdjustments((double) 10);
		cart.addItem(item1);
		JSONSerializer serializer = new JSONSerializer();
		
		System.out.println(serializer.include("items").serialize(cart));
		String jsonstring = "{\"items\":[{\"adjustments\":10.0,\"key\":20,\"productId\":162,\"productName\":\"item1\",\"productprice\":52.0,\"quantity\":2.0,\"stringtoBeIndex\":null,\"subtotal\":0.0}],\"stringtoBeIndex\":null,\"userId\":null}";

		ShoppingCart fromStringCart = ShoppingCart.getShoppingCartfromjsonString(jsonstring);
		Set<ShoppingCartItem>  sCartItems = fromStringCart.getItems();
		Iterator<ShoppingCartItem>  itCart = sCartItems.iterator();
		while(itCart.hasNext()){
		System.out.println(" Shopping Cart Object:"+ itCart.next().getProductName());
		}
	}

}
