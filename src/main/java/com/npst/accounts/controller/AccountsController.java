package com.npst.accounts.controller;


import com.npst.accounts.constants.ApplicationConstants;
import com.npst.accounts.dao.AccountsContactInfoDto;
import com.npst.accounts.dao.CustomerDto;
import com.npst.accounts.dao.ErrorResponseDto;
import com.npst.accounts.dao.ResponseDto;
import com.npst.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Slf4j
@Tag(name = "CRUD Rest Apis for Accounts Microservices", description = "Create, Read, Update, Delete")
public class AccountsController {

    @Autowired
    private IAccountsService iAccountsService;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    @Operation(summary = "Create Account REST API", description = "REST API to create new Customer &  Account")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        log.info("Received request to create account for mobile number: {}", customerDto.getMobileNumber());
        iAccountsService.createAccounts(customerDto);
        log.info("Account created successfully for mobile number: {}", customerDto.getMobileNumber());
        return ResponseEntity.ok(new ResponseDto("success", "Account created successfully"));
    }

    @Operation(summary = "Fetch Account Details REST API", description = "REST API to fetch Customer &  Account details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccount(@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        log.info("Fetching account details for mobile number: {}", mobileNumber);
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        log.info("Fetched account details for mobile number: {}", mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @Operation(summary = "Update Account Details REST API", description = "REST API to update Customer &  Account details based on a account number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"), @ApiResponse(responseCode = "417", description = "Expectation Failed"), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {
        log.info("Received request to update account for mobile number: {}", customerDto.getMobileNumber());
        boolean isUpdated = iAccountsService.updateCustomerWithAccount(customerDto);
        if (isUpdated) {
            log.info("Account updated successfully for mobile number: {}", customerDto.getMobileNumber());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(ApplicationConstants.STATUS_200, ApplicationConstants.MESSAGE_200));
        } else {
            log.error("Failed to update account for mobile number: {}", customerDto.getMobileNumber());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(ApplicationConstants.STATUS_417, ApplicationConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(summary = "Delete Account & Customer Details REST API", description = "REST API to delete Customer &  Account details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"), @ApiResponse(responseCode = "417", description = "Expectation Failed"), @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetail(@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        log.info("Received request to delete account for mobile number: {}", mobileNumber);
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if (isDeleted) {
            log.info("Account deleted successfully for mobile number: {}", mobileNumber);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(ApplicationConstants.STATUS_200, ApplicationConstants.MESSAGE_200));
        } else {
            log.error("Failed to delete account for mobile number: {}", mobileNumber);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(ApplicationConstants.STATUS_417, ApplicationConstants.MESSAGE_417_DELETE));
        }
    }


    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }

}
