package ch.vorburger.calcbeans.demo

import java.util.List
import org.eclipse.xtend.lib.annotations.Accessors

// TODO @Calc 
@Accessors class Order {
	List<Item> items
	double discount

	double total1 = discount * items.stream.map[price].reduce(0d, [$0 + $1])
	double total2 = discount * { 
		var total = 0d
		for(item : items) 
			total += item.price 
		total
	}
}

@Accessors class Item {
	Double price
}