$function(){	
if ( orderViewModel.confirmedorder()){
   $("#barcodeTarget").barcode(orderViewModel.confirmedorder().orderNumber,"codabar",{barWidth:2, barHeight:30});
}
}