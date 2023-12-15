package vttp.ssf.assessment.eventmanagement.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Details {
    
    @NotEmpty(message="Name field cannot be empty")
    @Size(min = 5, max = 25, message = "Name field can only contain between 5 to 25 characters")
    private String fullName;

    @NotNull
    @Past(message = "DOB cannot be today or in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    
    @NotEmpty(message = "Email field cannot be empty")
    @Size(max=50, message = "Cannot exceed 50 characters")
    @Email(message = "Email field is not in the correct format")
    private String email;

    @Pattern(regexp = "(8|9)[0-9]{7}", message = "Invalid phone number entered")
    private String mobileNumber;

    private String gender;

    @NotNull(message = "Minimum of 1 ticket to request")
    @Min(value=1, message = "Minimum of 1 ticket to request")
    @Max(value=3, message = "Maximum of 3 tickets per request")
    private Integer numberOfTickets;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Details(
            @NotEmpty(message = "Name field cannot be empty") @Size(min = 5, max = 25, message = "Name field can only contain between 5 to 25 characters") String fullName,
            @Past(message = "DOB cannot be today or in the future") Date birthDate,
            @NotEmpty(message = "Email field cannot be empty") @Size(max = 50, message = "Cannot exceed 50 characters") @Email(message = "Email field is not in the correct format") String email,
            @Pattern(regexp = "(8|9)[0-9]{7}", message = "Invalid phone number entered") String mobileNumber,
            String gender,
            @Min(value = 1, message = "Minimum of 1 ticket to request") @Max(value = 3, message = "Maximum of 3 tickets per request") Integer numberOfTickets) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.numberOfTickets = numberOfTickets;
    }

    public Details(){
        
    }

    
}
