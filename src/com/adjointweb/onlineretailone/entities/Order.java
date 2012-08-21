package com.adjointweb.onlineretailone.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@PersistenceCapable 
public class Order extends EntityObject {
	@PrimaryKey
	@Persistent
	private String orderNumber;
	
	
	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the orderNotes
	 */
	public String getOrderNotes() {
		return orderNotes;
	}

	/**
	 * @param orderNotes the orderNotes to set
	 */
	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}

	/**
	 * @return the orderName
	 */
	public String getOrderName() {
		return orderName;
	}

	/**
	 * @param orderName the orderName to set
	 */
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	/**
	 * @return the orderUserId
	 */
	public String getOrderUserId() {
		return orderUserId;
	}

	/**
	 * @param orderUserId the orderUserId to set
	 */
	public void setOrderUserId(String orderUserId) {
		this.orderUserId = orderUserId;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderLineItems
	 */
	public Set<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	/**
	 * @param orderLineItems the orderLineItems to set
	 */
	public void setOrderLineItems(Set<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}
	public void addOrderLineItem(OrderLineItem orderLineItem){
		this.orderLineItems.add(orderLineItem);
	}

	/**
	 * @return the orderShippingMethod
	 */
	public String getOrderShippingMethod() {
		return orderShippingMethod;
	}

	/**
	 * @param orderShippingMethod the orderShippingMethod to set
	 */
	public void setOrderShippingMethod(String orderShippingMethod) {
		this.orderShippingMethod = orderShippingMethod;
	}

	/**
	 * @return the orderShippingAddress
	 */
	public OrderAddress getOrderShippingAddress() {
		return orderShippingAddress;
	}

	/**
	 * @param orderShippingAddress the orderShippingAddress to set
	 */
	public void setOrderShippingAddress(OrderAddress orderShippingAddress) {
		this.orderShippingAddress = orderShippingAddress;
	}

	/**
	 * @return the orderShipDate
	 */
	public Date getOrderShipDate() {
		return orderShipDate;
	}

	/**
	 * @param orderShipDate the orderShipDate to set
	 */
	public void setOrderShipDate(Date orderShipDate) {
		this.orderShipDate = orderShipDate;
	}

	/**
	 * @return the orderRequestedShipDate
	 */
	public Date getOrderRequestedShipDate() {
		return orderRequestedShipDate;
	}

	/**
	 * @param orderRequestedShipDate the orderRequestedShipDate to set
	 */
	public void setOrderRequestedShipDate(Date orderRequestedShipDate) {
		this.orderRequestedShipDate = orderRequestedShipDate;
	}

	/**
	 * @return the orderRequestedShipTime
	 */
	public Date getOrderRequestedShipTime() {
		return orderRequestedShipTime;
	}

	/**
	 * @param orderRequestedShipTime the orderRequestedShipTime to set
	 */
	public void setOrderRequestedShipTime(Date orderRequestedShipTime) {
		this.orderRequestedShipTime = orderRequestedShipTime;
	}

	/**
	 * @return the orderPaymentType
	 */
	public String getOrderPaymentType() {
		return orderPaymentType;
	}

	/**
	 * @param orderPaymentType the orderPaymentType to set
	 */
	public void setOrderPaymentType(String orderPaymentType) {
		this.orderPaymentType = orderPaymentType;
	}

	/**
	 * @return the orderTaxtype
	 */
	public TaxType getOrderTaxtype() {
		return orderTaxtype;
	}

	/**
	 * @param orderTaxtype the orderTaxtype to set
	 */
	public void setOrderTaxtype(TaxType orderTaxtype) {
		this.orderTaxtype = orderTaxtype;
	}

	/**
	 * @return the billtoParty
	 */
	public String getBilltoParty() {
		return billtoParty;
	}

	/**
	 * @param billtoParty the billtoParty to set
	 */
	public void setBilltoParty(String billtoParty) {
		this.billtoParty = billtoParty;
	}

	/**
	 * @return the billFromParty
	 */
	public String getBillFromParty() {
		return billFromParty;
	}

	/**
	 * @param billFromParty the billFromParty to set
	 */
	public void setBillFromParty(String billFromParty) {
		this.billFromParty = billFromParty;
	}

	/**
	 * @return the orderEmailAddress
	 */
	public String getOrderEmailAddress() {
		return orderEmailAddress;
	}

	/**
	 * @param orderEmailAddress the orderEmailAddress to set
	 */
	public void setOrderEmailAddress(String orderEmailAddress) {
		this.orderEmailAddress = orderEmailAddress;
	}

	/**
	 * @return the orderStoreId
	 */
	public Long getOrderStoreId() {
		return orderStoreId;
	}

	/**
	 * @param orderStoreId the orderStoreId to set
	 */
	public void setOrderStoreId(Long orderStoreId) {
		this.orderStoreId = orderStoreId;
	}

	/**
	 * @return the orderPaymentInfo
	 */
	public PaymentInformation getOrderPaymentInfo() {
		return orderPaymentInfo;
	}

	/**
	 * @param orderPaymentInfo the orderPaymentInfo to set
	 */
	public void setOrderPaymentInfo(PaymentInformation orderPaymentInfo) {
		this.orderPaymentInfo = orderPaymentInfo;
	}

	/**
	 * @return the orderTotalAmount
	 */
	public Double getOrderTotalAmount() {
		return orderTotalAmount;
	}

	/**
	 * @param orderTotalAmount the orderTotalAmount to set
	 */
	public void setOrderTotalAmount(Double orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	/**
	 * @return the orderTotalDiscount
	 */
	public Double getOrderTotalDiscount() {
		return orderTotalDiscount;
	}

	/**
	 * @param orderTotalDiscount the orderTotalDiscount to set
	 */
	public void setOrderTotalDiscount(Double orderTotalDiscount) {
		this.orderTotalDiscount = orderTotalDiscount;
	}

	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the orderAdjustment
	 */
	public Double getOrderAdjustment() {
		return orderAdjustment;
	}

	/**
	 * @param orderAdjustment the orderAdjustment to set
	 */
	public void setOrderAdjustment(Double orderAdjustment) {
		this.orderAdjustment = orderAdjustment;
	}

	/**
	 * @return the orderShipStatus
	 */
	public String getOrderShipStatus() {
		return orderShipStatus;
	}

	/**
	 * @param orderShipStatus the orderShipStatus to set
	 */
	public void setOrderShipStatus(String orderShipStatus) {
		this.orderShipStatus = orderShipStatus;
	}

	/**
	 * @return the oRDER_CHANNEL
	 */
	public String getORDER_CHANNEL() {
		return ORDER_CHANNEL;
	}

	private final String ORDER_CHANNEL = "WC";
	
	@Persistent
	private String orderStatus;
	
	@Persistent
	private String orderNotes;
	
	@Persistent
	private String orderName;
	
	@Persistent
	private String orderUserId;
	
	@Persistent 
	private String orderType;
   
	@Element(dependent = "true")
    @Persistent(mappedBy = "order")
	private Set<OrderLineItem> orderLineItems;
	
	
	@Persistent 
	private String orderShippingMethod;
	
	@Persistent
	private OrderAddress orderShippingAddress;
	
	@Persistent
	private Date orderShipDate;
	
	@Persistent
	private Date orderRequestedShipDate;
	
	@Persistent
	private Date orderRequestedShipTime;
	
	
	@Persistent
	private String orderPaymentType;
	
	@Persistent
	private TaxType orderTaxtype;
	
	@Persistent
	private String billtoParty;
	
	@Persistent
	private String billFromParty;
	
	@Persistent
	private String orderEmailAddress;
	
	@Persistent
	private Long orderStoreId;
	
	@Persistent
	private PaymentInformation orderPaymentInfo;
	
	@Persistent
	private Double orderTotalAmount;
	
	@Persistent
	private Double orderTotalDiscount;
	
	@Persistent
	private Date orderDate;
	
	@Persistent
	private Double orderAdjustment;
	
	@Persistent
	private String orderShipStatus;
	
	@Persistent
	private String selectedTmg;
	
	@Persistent
	private double shippingCharges;
	
	@Persistent
	private double netAmount;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Order(){
		this.orderLineItems = new HashSet<OrderLineItem>();
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
	public  String toJSON(){
	       JSONSerializer serializer = new JSONSerializer();
	       return serializer.exclude("*.class","orderLineItems.key").include("orderLineItems").serialize(this);
	}
	
	public static Order getOrderfromjsonString(String jsonstring){
		Order order = new Order();
		
		order = new JSONDeserializer<Order>().deserialize(jsonstring, Order.class);
		
		return order;
	}

	public String getSelectedTmg() {
		return selectedTmg;
	}

	public void setSelectedTmg(String selectedTmg) {
		this.selectedTmg = selectedTmg;
	}

	public double getShippingCharges() {
		return shippingCharges;
	}

	public void setShippingCharges(double shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}
	
}
