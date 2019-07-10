package integration.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Thing {
	@Id
	@GeneratedValue
	private int id;
	private String name;
}
