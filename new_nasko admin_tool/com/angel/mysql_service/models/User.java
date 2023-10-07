package com.angel.mysql_service.models;

import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (
	name =  	users 
)
public class User { 

	@Column (
		name =  	firstName 
	)
	private String firstName; 

}