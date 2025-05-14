package com.npst.accounts.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Customer",
        description = "Schema to hold Customer and Account details")
public class CustomerDto {


    @Schema(
            description = "Name of Customer", example = "Akash Waghmare"
    )
    @NotEmpty(message = "Name Cannot be null or Empty")
    @Size(min = 5, max = 30, message = "The Length of Customer Name should be between 5 and 30")
    private String name;

    @Schema(
            description = "Email of Customer", example = "akash.waghmare@gmail.com"
    )
    @NotEmpty(message = "Email Cannot be null or Empty")
    @Email(message = "Email address should be valid value")
    private String email;

    @Schema(
            description = "Mobile number of Customer", example = "8989873628"
    )
    @NotEmpty(message = "Mobile number Cannot be null or Empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Account Details of the Customer"
    )
    private AccountsDto accountsDto;

}
