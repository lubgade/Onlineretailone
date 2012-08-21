package snippets;

import java.util.ArrayList;
import java.util.List;



import com.cloudjini.onlineretail.json.JSONHelper;
import com.cloudjini.onlineretailone.entities.Address;
import com.cloudjini.onlineretailone.entities.Store;
import com.google.appengine.api.datastore.Email;

public class TestJSONHelper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Store storedb = new Store();
		storedb.setStoreName("Green Land");
		String storeDesc ="Green Land";
		storedb.setStoreDesc(storeDesc);
		String managerName ="Rushikesh";
		storedb.setManagerName(managerName);
		List  emails = new ArrayList();
		emails.add(new Email("rubgade@gmail.com"));
		emails.add(new Email("rubgade@gmail.com"));
		emails.add(new Email("rubgade@gmail.com"));
		
		storedb.seteMailaddreses(emails);
		List phones = new ArrayList();
		phones.add("63453535");
		phones.add("63453535");
		phones.add("63453535");

		storedb.setPhoneNumbers(phones);
		storedb.setDeliveryDistance(5);
		Address address = new Address();
		address.setAddressline1("ABC");
		address.setAddressline2("fffffffff");
		address.setCity("Bangalore");
		address.setState("KA");
		address.setCountry("India");
		address.setLat(43.424);
		address.setLng(71.3434);
		storedb.setAddress(address);
		storedb.setAllowStorePayment(true);
		storedb.setAllowOrderModification(false);
		storedb.setMultipleStores(true);
		System.out.println("Hello");
		System.out.println(JSONHelper.getJSON(storedb));
		
		

	}

}
