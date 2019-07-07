package integration.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Concept {
	@Id
	private int id;
	private String name;
}
