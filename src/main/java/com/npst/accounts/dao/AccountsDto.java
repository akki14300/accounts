package com.npst.accounts.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Accounts",
        description = "Schema to hold Account details")
public class AccountsDto {

    @Schema(
            description = "Account Number", example = "1234567890"
    )
    @NotEmpty(message = "Mobile number Cannot be null or Empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
    private Long accountNumber;

    @Schema(
            description = "Account Type", example = "Savings"
    )
    @NotEmpty(message = "Account Type cannot be null or empty")
    private String accountType;

    @Schema(
            description = "Branch Address", example = "Pune"
    )
    @NotEmpty(message = "Branch address cannot be null or empty")
    private String branchAddress;
}
