package ar.com.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderBy {

	private List<OrderedColumn> columns = new LinkedList<OrderedColumn>();

	private OrderBy() {}
	
	public static OrderBy build() {
		OrderBy order = new OrderBy();
		return order;
	}

	public OrderBy addColumn(OrderedColumn column) {
		this.columns.add(column);
		return this;
	}

	public boolean isEmpty() {
		return this.getColumns().isEmpty();
	}
}
