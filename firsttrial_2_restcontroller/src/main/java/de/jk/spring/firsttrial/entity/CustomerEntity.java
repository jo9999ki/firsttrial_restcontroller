package de.jk.spring.firsttrial.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="CUSTOMER")
public class CustomerEntity {
	

	@Id //Database
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Database
	private Long id;
     
    @Column(name="first_name", nullable=false, length=255) //Database
    @NotNull //Input Validation
    @Size(min=0, max = 255) //Input Validation + Swagger Output
    private String firstName;
     
    @Column(name="last_name", nullable=false, length=255)
    @NotNull //Input Validation
    @Size(min=0, max = 255) //Input Validation + Swagger Output
    private String lastName;
    //RFC 5321 for mail adress length
    
    @NotNull //Input Validation
    @Size(min=0, max = 320) //Input Validation + Swagger Output
    @Email //Input Validation
    @Column(name="email", nullable=false, length=320)
    private String email;
     
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
    public String toString() {
        return "EmployeeEntity [id=" + id + ", firstName=" + firstName + 
                ", lastName=" + lastName + ", email=" + email   + "]";
    }

}
