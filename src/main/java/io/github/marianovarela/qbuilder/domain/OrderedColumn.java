package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedColumn {

	private String column;
	
	private Order order;

	public static OrderedColumn buildColumn(String column, Order order) {
		OrderedColumn orderedColumn = new OrderedColumn();
		orderedColumn.setColumn(column);
		orderedColumn.setOrder(order);
		return orderedColumn;
	}
}
