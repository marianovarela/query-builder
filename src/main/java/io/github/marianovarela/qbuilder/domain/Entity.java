package io.github.marianovarela.qbuilder.domain;

public enum Entity {

	Associations("associations"),
	Objects("objects");
	
	public final String value;

	Entity(String value) {
		this.value = value;
	}
}
